// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jpa;

import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import javax.persistence.Persistence;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Constructor;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class JpaDatabaseManager extends AbstractDatabaseManager
{
    private static final JPADatabaseManagerFactory FACTORY;
    private final String entityClassName;
    private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
    private final String persistenceUnitName;
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;
    
    private JpaDatabaseManager(final String name, final int bufferSize, final Class<? extends AbstractLogEventWrapperEntity> entityClass, final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, final String persistenceUnitName) {
        super(name, bufferSize);
        this.entityClassName = entityClass.getName();
        this.entityConstructor = entityConstructor;
        this.persistenceUnitName = persistenceUnitName;
    }
    
    @Override
    protected void startupInternal() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnitName);
    }
    
    @Override
    protected boolean shutdownInternal() {
        boolean closed = true;
        if (this.entityManager != null || this.transaction != null) {
            closed &= this.commitAndClose();
        }
        if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
            this.entityManagerFactory.close();
        }
        return closed;
    }
    
    @Override
    protected void connectAndStart() {
        try {
            this.entityManager = this.entityManagerFactory.createEntityManager();
            (this.transaction = this.entityManager.getTransaction()).begin();
        }
        catch (final Exception e) {
            throw new AppenderLoggingException("Cannot write logging event or flush buffer; manager cannot create EntityManager or transaction.", e);
        }
    }
    
    @Override
    protected void writeInternal(final LogEvent event) {
        if (!this.isRunning() || this.entityManagerFactory == null || this.entityManager == null || this.transaction == null) {
            throw new AppenderLoggingException("Cannot write logging event; JPA manager not connected to the database.");
        }
        AbstractLogEventWrapperEntity entity;
        try {
            entity = (AbstractLogEventWrapperEntity)this.entityConstructor.newInstance(event);
        }
        catch (final Exception e) {
            throw new AppenderLoggingException("Failed to instantiate entity class [" + this.entityClassName + "].", e);
        }
        try {
            this.entityManager.persist((Object)entity);
        }
        catch (final Exception e) {
            if (this.transaction != null && this.transaction.isActive()) {
                this.transaction.rollback();
                this.transaction = null;
            }
            throw new AppenderLoggingException("Failed to insert record for log event in JPA manager: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected boolean commitAndClose() {
        boolean closed = true;
        try {
            if (this.transaction != null && this.transaction.isActive()) {
                this.transaction.commit();
            }
        }
        catch (final Exception e) {
            if (this.transaction != null && this.transaction.isActive()) {
                this.transaction.rollback();
            }
            this.transaction = null;
            try {
                if (this.entityManager != null && this.entityManager.isOpen()) {
                    this.entityManager.close();
                }
            }
            catch (final Exception e) {
                this.logWarn("Failed to close entity manager while logging event or flushing buffer", e);
                closed = false;
            }
            finally {
                this.entityManager = null;
            }
        }
        finally {
            this.transaction = null;
            try {
                if (this.entityManager != null && this.entityManager.isOpen()) {
                    this.entityManager.close();
                }
            }
            catch (final Exception e2) {
                this.logWarn("Failed to close entity manager while logging event or flushing buffer", e2);
                closed = false;
                this.entityManager = null;
            }
            finally {
                this.entityManager = null;
            }
        }
        return closed;
    }
    
    public static JpaDatabaseManager getJPADatabaseManager(final String name, final int bufferSize, final Class<? extends AbstractLogEventWrapperEntity> entityClass, final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, final String persistenceUnitName) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, entityClass, entityConstructor, persistenceUnitName), (ManagerFactory<JpaDatabaseManager, FactoryData>)JpaDatabaseManager.FACTORY);
    }
    
    static {
        FACTORY = new JPADatabaseManagerFactory();
    }
    
    private static final class FactoryData extends AbstractFactoryData
    {
        private final Class<? extends AbstractLogEventWrapperEntity> entityClass;
        private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
        private final String persistenceUnitName;
        
        protected FactoryData(final int bufferSize, final Class<? extends AbstractLogEventWrapperEntity> entityClass, final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, final String persistenceUnitName) {
            super(bufferSize);
            this.entityClass = entityClass;
            this.entityConstructor = entityConstructor;
            this.persistenceUnitName = persistenceUnitName;
        }
    }
    
    private static final class JPADatabaseManagerFactory implements ManagerFactory<JpaDatabaseManager, FactoryData>
    {
        @Override
        public JpaDatabaseManager createManager(final String name, final FactoryData data) {
            return new JpaDatabaseManager(name, data.getBufferSize(), data.entityClass, data.entityConstructor, data.persistenceUnitName, null);
        }
    }
}
