// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import java.nio.charset.Charset;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.util.Arrays;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import java.util.Objects;
import org.apache.logging.log4j.core.util.Assert;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;

@Plugin(name = "JDBC", category = "Core", elementType = "appender", printObject = true)
public final class JdbcAppender extends AbstractDatabaseAppender<JdbcDatabaseManager>
{
    private final String description;
    
    private JdbcAppender(final String name, final Filter filter, final boolean ignoreExceptions, final JdbcDatabaseManager manager) {
        super(name, filter, ignoreExceptions, manager);
        this.description = this.getName() + "{ manager=" + ((AbstractDatabaseAppender<Object>)this).getManager() + " }";
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @Deprecated
    public static <B extends Builder<B>> JdbcAppender createAppender(final String name, final String ignore, final Filter filter, final ConnectionSource connectionSource, final String bufferSize, final String tableName, final ColumnConfig[] columnConfigs) {
        Assert.requireNonEmpty(name, "Name cannot be empty");
        Objects.requireNonNull(connectionSource, "ConnectionSource cannot be null");
        Assert.requireNonEmpty(tableName, "Table name cannot be empty");
        Assert.requireNonEmpty(columnConfigs, "ColumnConfigs cannot be empty");
        final int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        return newBuilder().setBufferSize(bufferSizeInt).setColumnConfigs(columnConfigs).setConnectionSource(connectionSource).setTableName(tableName).withName(name).withIgnoreExceptions(ignoreExceptions).withFilter(filter).build();
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<JdbcAppender>
    {
        @PluginElement("ConnectionSource")
        @Required(message = "No ConnectionSource provided")
        private ConnectionSource connectionSource;
        @PluginBuilderAttribute
        private int bufferSize;
        @PluginBuilderAttribute
        @Required(message = "No table name provided")
        private String tableName;
        @PluginElement("ColumnConfigs")
        private ColumnConfig[] columnConfigs;
        @PluginElement("ColumnMappings")
        private ColumnMapping[] columnMappings;
        
        public B setConnectionSource(final ConnectionSource connectionSource) {
            this.connectionSource = connectionSource;
            return this.asBuilder();
        }
        
        public B setBufferSize(final int bufferSize) {
            this.bufferSize = bufferSize;
            return this.asBuilder();
        }
        
        public B setTableName(final String tableName) {
            this.tableName = tableName;
            return this.asBuilder();
        }
        
        public B setColumnConfigs(final ColumnConfig... columnConfigs) {
            this.columnConfigs = columnConfigs;
            return this.asBuilder();
        }
        
        public B setColumnMappings(final ColumnMapping... columnMappings) {
            this.columnMappings = columnMappings;
            return this.asBuilder();
        }
        
        @Override
        public JdbcAppender build() {
            if (Assert.isEmpty(this.columnConfigs) && Assert.isEmpty(this.columnMappings)) {
                JdbcAppender.LOGGER.error("Cannot create JdbcAppender without any columns configured.");
                return null;
            }
            final String managerName = "JdbcManager{name=" + this.getName() + ", bufferSize=" + this.bufferSize + ", tableName=" + this.tableName + ", columnConfigs=" + Arrays.toString(this.columnConfigs) + ", columnMappings=" + Arrays.toString(this.columnMappings) + '}';
            final JdbcDatabaseManager manager = JdbcDatabaseManager.getManager(managerName, this.bufferSize, this.connectionSource, this.tableName, this.columnConfigs, this.columnMappings);
            if (manager == null) {
                return null;
            }
            return new JdbcAppender(this.getName(), this.getFilter(), this.isIgnoreExceptions(), manager, null);
        }
        
        @Deprecated
        @Override
        public Layout<? extends Serializable> getLayout() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public B withLayout(final Layout<? extends Serializable> layout) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Layout<? extends Serializable> getOrCreateLayout() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Layout<? extends Serializable> getOrCreateLayout(final Charset charset) {
            throw new UnsupportedOperationException();
        }
    }
}
