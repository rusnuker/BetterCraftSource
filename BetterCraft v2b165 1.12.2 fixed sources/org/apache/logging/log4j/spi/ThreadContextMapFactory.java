// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.ProviderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.Logger;

public final class ThreadContextMapFactory
{
    private static final Logger LOGGER;
    private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
    private static final String GC_FREE_THREAD_CONTEXT_KEY = "log4j2.garbagefree.threadContextMap";
    
    private ThreadContextMapFactory() {
    }
    
    public static ThreadContextMap createThreadContextMap() {
        final PropertiesUtil managerProps = PropertiesUtil.getProperties();
        final String threadContextMapName = managerProps.getStringProperty("log4j2.threadContextMap");
        final ClassLoader cl = ProviderUtil.findClassLoader();
        ThreadContextMap result = null;
        if (threadContextMapName != null) {
            try {
                final Class<?> clazz = cl.loadClass(threadContextMapName);
                if (ThreadContextMap.class.isAssignableFrom(clazz)) {
                    result = (ThreadContextMap)clazz.newInstance();
                }
            }
            catch (final ClassNotFoundException cnfe) {
                ThreadContextMapFactory.LOGGER.error("Unable to locate configured ThreadContextMap {}", threadContextMapName);
            }
            catch (final Exception ex) {
                ThreadContextMapFactory.LOGGER.error("Unable to create configured ThreadContextMap {}", threadContextMapName, ex);
            }
        }
        if (result == null && ProviderUtil.hasProviders() && LogManager.getFactory() != null) {
            final String factoryClassName = LogManager.getFactory().getClass().getName();
            for (final Provider provider : ProviderUtil.getProviders()) {
                if (factoryClassName.equals(provider.getClassName())) {
                    final Class<? extends ThreadContextMap> clazz2 = provider.loadThreadContextMap();
                    if (clazz2 == null) {
                        continue;
                    }
                    try {
                        result = (ThreadContextMap)clazz2.newInstance();
                        break;
                    }
                    catch (final Exception e) {
                        ThreadContextMapFactory.LOGGER.error("Unable to locate or load configured ThreadContextMap {}", provider.getThreadContextMap(), e);
                        result = createDefaultThreadContextMap();
                    }
                }
            }
        }
        if (result == null) {
            result = createDefaultThreadContextMap();
        }
        return result;
    }
    
    private static ThreadContextMap createDefaultThreadContextMap() {
        if (!Constants.ENABLE_THREADLOCALS) {
            return new DefaultThreadContextMap(true);
        }
        if (PropertiesUtil.getProperties().getBooleanProperty("log4j2.garbagefree.threadContextMap")) {
            return new GarbageFreeSortedArrayThreadContextMap();
        }
        return new CopyOnWriteSortedArrayThreadContextMap();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
