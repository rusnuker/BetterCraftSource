// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.core.util.ExecutorServices;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.util.Iterator;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import java.beans.PropertyChangeEvent;
import org.apache.logging.log4j.core.util.NetUtils;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.AbstractLogger;
import java.util.Collection;
import org.apache.logging.log4j.message.MessageFactory;
import java.util.Objects;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import java.util.concurrent.locks.Lock;
import org.apache.logging.log4j.core.util.Cancellable;
import java.net.URI;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.spi.Terminable;

public class LoggerContext extends AbstractLifeCycle implements org.apache.logging.log4j.spi.LoggerContext, AutoCloseable, Terminable, ConfigurationListener
{
    public static final String PROPERTY_CONFIG = "config";
    private static final Configuration NULL_CONFIGURATION;
    private final LoggerRegistry<Logger> loggerRegistry;
    private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners;
    private volatile Configuration configuration;
    private Object externalContext;
    private String contextName;
    private volatile URI configLocation;
    private Cancellable shutdownCallback;
    private final Lock configLock;
    
    public LoggerContext(final String name) {
        this(name, null, (URI)null);
    }
    
    public LoggerContext(final String name, final Object externalContext) {
        this(name, externalContext, (URI)null);
    }
    
    public LoggerContext(final String name, final Object externalContext, final URI configLocn) {
        this.loggerRegistry = new LoggerRegistry<Logger>();
        this.propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
        this.configuration = new DefaultConfiguration();
        this.configLock = new ReentrantLock();
        this.contextName = name;
        this.externalContext = externalContext;
        this.configLocation = configLocn;
    }
    
    public LoggerContext(final String name, final Object externalContext, final String configLocn) {
        this.loggerRegistry = new LoggerRegistry<Logger>();
        this.propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
        this.configuration = new DefaultConfiguration();
        this.configLock = new ReentrantLock();
        this.contextName = name;
        this.externalContext = externalContext;
        if (configLocn != null) {
            URI uri;
            try {
                uri = new File(configLocn).toURI();
            }
            catch (final Exception ex) {
                uri = null;
            }
            this.configLocation = uri;
        }
        else {
            this.configLocation = null;
        }
    }
    
    public static LoggerContext getContext() {
        return (LoggerContext)LogManager.getContext();
    }
    
    public static LoggerContext getContext(final boolean currentContext) {
        return (LoggerContext)LogManager.getContext(currentContext);
    }
    
    public static LoggerContext getContext(final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        return (LoggerContext)LogManager.getContext(loader, currentContext, configLocation);
    }
    
    @Override
    public void start() {
        LoggerContext.LOGGER.debug("Starting LoggerContext[name={}, {}]...", this.getName(), this);
        if (PropertiesUtil.getProperties().getBooleanProperty("log4j.LoggerContext.stacktrace.on.start", false)) {
            LoggerContext.LOGGER.debug("Stack trace to locate invoker", new Exception("Not a real error, showing stack trace to locate invoker"));
        }
        if (this.configLock.tryLock()) {
            try {
                if (this.isInitialized() || this.isStopped()) {
                    this.setStarting();
                    this.reconfigure();
                    if (this.configuration.isShutdownHookEnabled()) {
                        this.setUpShutdownHook();
                    }
                    this.setStarted();
                }
            }
            finally {
                this.configLock.unlock();
            }
        }
        LoggerContext.LOGGER.debug("LoggerContext[name={}, {}] started OK.", this.getName(), this);
    }
    
    public void start(final Configuration config) {
        LoggerContext.LOGGER.debug("Starting LoggerContext[name={}, {}] with configuration {}...", this.getName(), this, config);
        if (this.configLock.tryLock()) {
            try {
                if (this.isInitialized() || this.isStopped()) {
                    if (this.configuration.isShutdownHookEnabled()) {
                        this.setUpShutdownHook();
                    }
                    this.setStarted();
                }
            }
            finally {
                this.configLock.unlock();
            }
        }
        this.setConfiguration(config);
        LoggerContext.LOGGER.debug("LoggerContext[name={}, {}] started OK with configuration {}.", this.getName(), this, config);
    }
    
    private void setUpShutdownHook() {
        if (this.shutdownCallback == null) {
            final LoggerContextFactory factory = LogManager.getFactory();
            if (factory instanceof ShutdownCallbackRegistry) {
                LoggerContext.LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Shutdown hook enabled. Registering a new one.");
                try {
                    final long shutdownTimeoutMillis = this.configuration.getShutdownTimeoutMillis();
                    this.shutdownCallback = ((ShutdownCallbackRegistry)factory).addShutdownCallback(new Runnable() {
                        @Override
                        public void run() {
                            final LoggerContext context = LoggerContext.this;
                            AbstractLifeCycle.LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Stopping LoggerContext[name={}, {}]", context.getName(), context);
                            context.stop(shutdownTimeoutMillis, TimeUnit.MILLISECONDS);
                        }
                        
                        @Override
                        public String toString() {
                            return "Shutdown callback for LoggerContext[name=" + LoggerContext.this.getName() + ']';
                        }
                    });
                }
                catch (final IllegalStateException e) {
                    throw new IllegalStateException("Unable to register Log4j shutdown hook because JVM is shutting down.", e);
                }
                catch (final SecurityException e2) {
                    LoggerContext.LOGGER.error(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Unable to register shutdown hook due to security restrictions", e2);
                }
            }
        }
    }
    
    @Override
    public void close() {
        this.stop();
    }
    
    @Override
    public void terminate() {
        this.stop();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        LoggerContext.LOGGER.debug("Stopping LoggerContext[name={}, {}]...", this.getName(), this);
        this.configLock.lock();
        try {
            if (this.isStopped()) {
                return true;
            }
            this.setStopping();
            try {
                Server.unregisterLoggerContext(this.getName());
            }
            catch (final LinkageError | Exception e) {
                LoggerContext.LOGGER.error("Unable to unregister MBeans", e);
            }
            if (this.shutdownCallback != null) {
                this.shutdownCallback.cancel();
                this.shutdownCallback = null;
            }
            final Configuration prev = this.configuration;
            this.configuration = LoggerContext.NULL_CONFIGURATION;
            this.updateLoggers();
            if (prev instanceof LifeCycle2) {
                ((LifeCycle2)prev).stop(timeout, timeUnit);
            }
            else {
                prev.stop();
            }
            this.externalContext = null;
            LogManager.getFactory().removeContext(this);
        }
        finally {
            this.configLock.unlock();
            this.setStopped();
        }
        LoggerContext.LOGGER.debug("Stopped LoggerContext[name={}, {}] with status {}", this.getName(), this, true);
        return true;
    }
    
    public String getName() {
        return this.contextName;
    }
    
    public Logger getRootLogger() {
        return this.getLogger("");
    }
    
    public void setName(final String name) {
        this.contextName = Objects.requireNonNull(name);
    }
    
    public void setExternalContext(final Object context) {
        this.externalContext = context;
    }
    
    @Override
    public Object getExternalContext() {
        return this.externalContext;
    }
    
    @Override
    public Logger getLogger(final String name) {
        return this.getLogger(name, null);
    }
    
    public Collection<Logger> getLoggers() {
        return this.loggerRegistry.getLoggers();
    }
    
    @Override
    public Logger getLogger(final String name, final MessageFactory messageFactory) {
        Logger logger = this.loggerRegistry.getLogger(name, messageFactory);
        if (logger != null) {
            AbstractLogger.checkMessageFactory(logger, messageFactory);
            return logger;
        }
        logger = this.newInstance(this, name, messageFactory);
        this.loggerRegistry.putIfAbsent(name, messageFactory, logger);
        return this.loggerRegistry.getLogger(name, messageFactory);
    }
    
    @Override
    public boolean hasLogger(final String name) {
        return this.loggerRegistry.hasLogger(name);
    }
    
    @Override
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return this.loggerRegistry.hasLogger(name, messageFactory);
    }
    
    @Override
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return this.loggerRegistry.hasLogger(name, messageFactoryClass);
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public void addFilter(final Filter filter) {
        this.configuration.addFilter(filter);
    }
    
    public void removeFilter(final Filter filter) {
        this.configuration.removeFilter(filter);
    }
    
    private Configuration setConfiguration(final Configuration config) {
        if (config == null) {
            LoggerContext.LOGGER.error("No configuration found for context '{}'.", this.contextName);
            return this.configuration;
        }
        this.configLock.lock();
        try {
            final Configuration prev = this.configuration;
            config.addListener(this);
            final ConcurrentMap<String, String> map = config.getComponent("ContextProperties");
            try {
                map.putIfAbsent("hostName", NetUtils.getLocalHostname());
            }
            catch (final Exception ex) {
                LoggerContext.LOGGER.debug("Ignoring {}, setting hostName to 'unknown'", ex.toString());
                map.putIfAbsent("hostName", "unknown");
            }
            map.putIfAbsent("contextName", this.contextName);
            config.start();
            this.configuration = config;
            this.updateLoggers();
            if (prev != null) {
                prev.removeListener(this);
                prev.stop();
            }
            this.firePropertyChangeEvent(new PropertyChangeEvent(this, "config", prev, config));
            try {
                Server.reregisterMBeansAfterReconfigure();
            }
            catch (final LinkageError | Exception e) {
                LoggerContext.LOGGER.error("Could not reconfigure JMX", e);
            }
            Log4jLogEvent.setNanoClock(this.configuration.getNanoClock());
            return prev;
        }
        finally {
            this.configLock.unlock();
        }
    }
    
    private void firePropertyChangeEvent(final PropertyChangeEvent event) {
        for (final PropertyChangeListener listener : this.propertyChangeListeners) {
            listener.propertyChange(event);
        }
    }
    
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeListeners.add(Objects.requireNonNull(listener, "listener"));
    }
    
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeListeners.remove(listener);
    }
    
    public URI getConfigLocation() {
        return this.configLocation;
    }
    
    public void setConfigLocation(final URI configLocation) {
        this.reconfigure(this.configLocation = configLocation);
    }
    
    private void reconfigure(final URI configURI) {
        final ClassLoader cl = ClassLoader.class.isInstance(this.externalContext) ? ((ClassLoader)this.externalContext) : null;
        LoggerContext.LOGGER.debug("Reconfiguration started for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, configURI, this, cl);
        final Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this, this.contextName, configURI, cl);
        if (instance == null) {
            LoggerContext.LOGGER.error("Reconfiguration failed: No configuration found for '{}' at '{}' in '{}'", this.contextName, configURI, cl);
        }
        else {
            this.setConfiguration(instance);
            final String location = (this.configuration == null) ? "?" : String.valueOf(this.configuration.getConfigurationSource());
            LoggerContext.LOGGER.debug("Reconfiguration complete for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, location, this, cl);
        }
    }
    
    public void reconfigure() {
        this.reconfigure(this.configLocation);
    }
    
    public void updateLoggers() {
        this.updateLoggers(this.configuration);
    }
    
    public void updateLoggers(final Configuration config) {
        final Configuration old = this.configuration;
        for (final Logger logger : this.loggerRegistry.getLoggers()) {
            logger.updateConfiguration(config);
        }
        this.firePropertyChangeEvent(new PropertyChangeEvent(this, "config", old, config));
    }
    
    @Override
    public synchronized void onChange(final Reconfigurable reconfigurable) {
        LoggerContext.LOGGER.debug("Reconfiguration started for context {} ({})", this.contextName, this);
        final Configuration newConfig = reconfigurable.reconfigure();
        if (newConfig != null) {
            this.setConfiguration(newConfig);
            LoggerContext.LOGGER.debug("Reconfiguration completed for {} ({})", this.contextName, this);
        }
        else {
            LoggerContext.LOGGER.debug("Reconfiguration failed for {} ({})", this.contextName, this);
        }
    }
    
    protected Logger newInstance(final LoggerContext ctx, final String name, final MessageFactory messageFactory) {
        return new Logger(ctx, name, messageFactory);
    }
    
    static {
        try {
            LoaderUtil.loadClass(ExecutorServices.class.getName());
        }
        catch (final Exception e) {
            LoggerContext.LOGGER.error("Failed to preload ExecutorServices class.", e);
        }
        NULL_CONFIGURATION = new NullConfiguration();
    }
}
