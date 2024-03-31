// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.util.Strings;
import java.util.ArrayList;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import java.util.Iterator;
import java.sql.Timestamp;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import java.sql.NClob;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import org.apache.logging.log4j.core.config.plugins.convert.DateTypeConverter;
import java.util.Date;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.core.LogEvent;
import java.sql.SQLException;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import java.sql.DatabaseMetaData;
import org.apache.logging.log4j.core.util.Closer;
import java.sql.PreparedStatement;
import java.sql.Connection;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import java.util.List;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class JdbcDatabaseManager extends AbstractDatabaseManager
{
    private static final JdbcDatabaseManagerFactory INSTANCE;
    private final List<ColumnMapping> columnMappings;
    private final List<ColumnConfig> columnConfigs;
    private final ConnectionSource connectionSource;
    private final String sqlStatement;
    private Connection connection;
    private PreparedStatement statement;
    private boolean isBatchSupported;
    
    private JdbcDatabaseManager(final String name, final int bufferSize, final ConnectionSource connectionSource, final String sqlStatement, final List<ColumnConfig> columnConfigs, final List<ColumnMapping> columnMappings) {
        super(name, bufferSize);
        this.connectionSource = connectionSource;
        this.sqlStatement = sqlStatement;
        this.columnConfigs = columnConfigs;
        this.columnMappings = columnMappings;
    }
    
    @Override
    protected void startupInternal() throws Exception {
        this.connection = this.connectionSource.getConnection();
        final DatabaseMetaData metaData = this.connection.getMetaData();
        this.isBatchSupported = metaData.supportsBatchUpdates();
        Closer.closeSilently(this.connection);
    }
    
    @Override
    protected boolean shutdownInternal() {
        return (this.connection == null && this.statement == null) || this.commitAndClose();
    }
    
    @Override
    protected void connectAndStart() {
        try {
            (this.connection = this.connectionSource.getConnection()).setAutoCommit(false);
            this.statement = this.connection.prepareStatement(this.sqlStatement);
        }
        catch (final SQLException e) {
            throw new AppenderLoggingException("Cannot write logging event or flush buffer; JDBC manager cannot connect to the database.", e);
        }
    }
    
    @Override
    protected void writeInternal(final LogEvent event) {
        StringReader reader = null;
        try {
            if (!this.isRunning() || this.connection == null || this.connection.isClosed() || this.statement == null || this.statement.isClosed()) {
                throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database.");
            }
            int i = 1;
            for (final ColumnMapping mapping : this.columnMappings) {
                if (ThreadContextMap.class.isAssignableFrom(mapping.getType()) || ReadOnlyStringMap.class.isAssignableFrom(mapping.getType())) {
                    this.statement.setObject(i++, event.getContextData().toMap());
                }
                else if (ThreadContextStack.class.isAssignableFrom(mapping.getType())) {
                    this.statement.setObject(i++, event.getContextStack().asList());
                }
                else if (Date.class.isAssignableFrom(mapping.getType())) {
                    this.statement.setObject(i++, DateTypeConverter.fromMillis(event.getTimeMillis(), (Class<Object>)mapping.getType().asSubclass(Date.class)));
                }
                else if (Clob.class.isAssignableFrom(mapping.getType())) {
                    this.statement.setClob(i++, new StringReader(mapping.getLayout().toSerializable(event)));
                }
                else if (NClob.class.isAssignableFrom(mapping.getType())) {
                    this.statement.setNClob(i++, new StringReader(mapping.getLayout().toSerializable(event)));
                }
                else {
                    final Object value = TypeConverters.convert((String)mapping.getLayout().toSerializable(event), mapping.getType(), (Object)null);
                    if (value == null) {
                        this.statement.setNull(i++, 0);
                    }
                    else {
                        this.statement.setObject(i++, value);
                    }
                }
            }
            for (final ColumnConfig column : this.columnConfigs) {
                if (column.isEventTimestamp()) {
                    this.statement.setTimestamp(i++, new Timestamp(event.getTimeMillis()));
                }
                else if (column.isClob()) {
                    reader = new StringReader(column.getLayout().toSerializable(event));
                    if (column.isUnicode()) {
                        this.statement.setNClob(i++, reader);
                    }
                    else {
                        this.statement.setClob(i++, reader);
                    }
                }
                else if (column.isUnicode()) {
                    this.statement.setNString(i++, column.getLayout().toSerializable(event));
                }
                else {
                    this.statement.setString(i++, column.getLayout().toSerializable(event));
                }
            }
            if (this.isBatchSupported) {
                this.statement.addBatch();
            }
            else if (this.statement.executeUpdate() == 0) {
                throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager.");
            }
        }
        catch (final SQLException e) {
            throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + e.getMessage(), e);
        }
        finally {
            Closer.closeSilently(reader);
        }
    }
    
    @Override
    protected boolean commitAndClose() {
        boolean closed = true;
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                if (this.isBatchSupported) {
                    this.statement.executeBatch();
                }
                this.connection.commit();
            }
        }
        catch (final SQLException e) {
            throw new AppenderLoggingException("Failed to commit transaction logging event or flushing buffer.", e);
        }
        finally {
            try {
                Closer.close(this.statement);
            }
            catch (final Exception e2) {
                this.logWarn("Failed to close SQL statement logging event or flushing buffer", e2);
                closed = false;
                this.statement = null;
            }
            finally {
                this.statement = null;
            }
            try {
                Closer.close(this.connection);
            }
            catch (final Exception e2) {
                this.logWarn("Failed to close database connection logging event or flushing buffer", e2);
                closed = false;
                this.connection = null;
            }
            finally {
                this.connection = null;
            }
        }
        return closed;
    }
    
    @Deprecated
    public static JdbcDatabaseManager getJDBCDatabaseManager(final String name, final int bufferSize, final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, connectionSource, tableName, columnConfigs, new ColumnMapping[0]), (ManagerFactory<JdbcDatabaseManager, FactoryData>)getFactory());
    }
    
    public static JdbcDatabaseManager getManager(final String name, final int bufferSize, final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs, final ColumnMapping[] columnMappings) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, connectionSource, tableName, columnConfigs, columnMappings), (ManagerFactory<JdbcDatabaseManager, FactoryData>)getFactory());
    }
    
    private static JdbcDatabaseManagerFactory getFactory() {
        return JdbcDatabaseManager.INSTANCE;
    }
    
    static {
        INSTANCE = new JdbcDatabaseManagerFactory();
    }
    
    private static final class FactoryData extends AbstractFactoryData
    {
        private final ConnectionSource connectionSource;
        private final String tableName;
        private final ColumnConfig[] columnConfigs;
        private final ColumnMapping[] columnMappings;
        
        protected FactoryData(final int bufferSize, final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs, final ColumnMapping[] columnMappings) {
            super(bufferSize);
            this.connectionSource = connectionSource;
            this.tableName = tableName;
            this.columnConfigs = columnConfigs;
            this.columnMappings = columnMappings;
        }
    }
    
    private static final class JdbcDatabaseManagerFactory implements ManagerFactory<JdbcDatabaseManager, FactoryData>
    {
        @Override
        public JdbcDatabaseManager createManager(final String name, final FactoryData data) {
            final StringBuilder sb = new StringBuilder("INSERT INTO ").append(data.tableName).append(" (");
            for (final ColumnMapping mapping : data.columnMappings) {
                sb.append(mapping.getName()).append(',');
            }
            for (final ColumnConfig config : data.columnConfigs) {
                sb.append(config.getColumnName()).append(',');
            }
            sb.setCharAt(sb.length() - 1, ')');
            sb.append(" VALUES (");
            final List<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>(data.columnMappings.length);
            for (final ColumnMapping mapping2 : data.columnMappings) {
                if (Strings.isNotEmpty(mapping2.getLiteralValue())) {
                    sb.append(mapping2.getLiteralValue());
                }
                else {
                    sb.append('?');
                    columnMappings.add(mapping2);
                }
                sb.append(',');
            }
            final List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>(data.columnConfigs.length);
            for (final ColumnConfig config2 : data.columnConfigs) {
                if (Strings.isNotEmpty(config2.getLiteralValue())) {
                    sb.append(config2.getLiteralValue());
                }
                else {
                    sb.append('?');
                    columnConfigs.add(config2);
                }
                sb.append(',');
            }
            sb.setCharAt(sb.length() - 1, ')');
            final String sqlStatement = sb.toString();
            return new JdbcDatabaseManager(name, data.getBufferSize(), data.connectionSource, sqlStatement, columnConfigs, columnMappings, null);
        }
    }
}
