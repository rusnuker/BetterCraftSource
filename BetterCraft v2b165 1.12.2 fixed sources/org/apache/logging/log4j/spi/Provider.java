// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.Properties;
import java.lang.ref.WeakReference;
import java.net.URL;
import org.apache.logging.log4j.Logger;

public class Provider
{
    public static final String FACTORY_PRIORITY = "FactoryPriority";
    public static final String THREAD_CONTEXT_MAP = "ThreadContextMap";
    public static final String LOGGER_CONTEXT_FACTORY = "LoggerContextFactory";
    private static final Integer DEFAULT_PRIORITY;
    private static final Logger LOGGER;
    private final Integer priority;
    private final String className;
    private final String threadContextMap;
    private final URL url;
    private final WeakReference<ClassLoader> classLoader;
    
    public Provider(final Properties props, final URL url, final ClassLoader classLoader) {
        this.url = url;
        this.classLoader = new WeakReference<ClassLoader>(classLoader);
        final String weight = props.getProperty("FactoryPriority");
        this.priority = ((weight == null) ? Provider.DEFAULT_PRIORITY : Integer.valueOf(weight));
        this.className = props.getProperty("LoggerContextFactory");
        this.threadContextMap = props.getProperty("ThreadContextMap");
    }
    
    public Integer getPriority() {
        return this.priority;
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public Class<? extends LoggerContextFactory> loadLoggerContextFactory() {
        if (this.className == null) {
            return null;
        }
        final ClassLoader loader = this.classLoader.get();
        if (loader == null) {
            return null;
        }
        try {
            final Class<?> clazz = loader.loadClass(this.className);
            if (LoggerContextFactory.class.isAssignableFrom(clazz)) {
                return clazz.asSubclass(LoggerContextFactory.class);
            }
        }
        catch (final Exception e) {
            Provider.LOGGER.error("Unable to create class {} specified in {}", this.className, this.url.toString(), e);
        }
        return null;
    }
    
    public String getThreadContextMap() {
        return this.threadContextMap;
    }
    
    public Class<? extends ThreadContextMap> loadThreadContextMap() {
        if (this.threadContextMap == null) {
            return null;
        }
        final ClassLoader loader = this.classLoader.get();
        if (loader == null) {
            return null;
        }
        try {
            final Class<?> clazz = loader.loadClass(this.threadContextMap);
            if (ThreadContextMap.class.isAssignableFrom(clazz)) {
                return clazz.asSubclass(ThreadContextMap.class);
            }
        }
        catch (final Exception e) {
            Provider.LOGGER.error("Unable to create class {} specified in {}", this.threadContextMap, this.url.toString(), e);
        }
        return null;
    }
    
    public URL getUrl() {
        return this.url;
    }
    
    @Override
    public String toString() {
        String result = "Provider[";
        if (!Provider.DEFAULT_PRIORITY.equals(this.priority)) {
            result = result + "priority=" + this.priority + ", ";
        }
        if (this.threadContextMap != null) {
            result = result + "threadContextMap=" + this.threadContextMap + ", ";
        }
        if (this.className != null) {
            result = result + "className=" + this.className + ", ";
        }
        result = result + "url=" + this.url;
        final ClassLoader loader = this.classLoader.get();
        if (loader == null) {
            result += ", classLoader=null(not reachable)";
        }
        else {
            result = result + ", classLoader=" + loader;
        }
        result += "]";
        return result;
    }
    
    static {
        DEFAULT_PRIORITY = -1;
        LOGGER = StatusLogger.getLogger();
    }
}
