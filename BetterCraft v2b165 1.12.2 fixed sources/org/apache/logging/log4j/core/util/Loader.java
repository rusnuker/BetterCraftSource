// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.lang.reflect.InvocationTargetException;
import java.io.InputStream;
import java.net.URL;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.Logger;

public final class Loader
{
    private static final Logger LOGGER;
    private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
    
    private Loader() {
    }
    
    public static ClassLoader getClassLoader() {
        return getClassLoader(Loader.class, null);
    }
    
    public static ClassLoader getThreadContextClassLoader() {
        return LoaderUtil.getThreadContextClassLoader();
    }
    
    public static ClassLoader getClassLoader(final Class<?> class1, final Class<?> class2) {
        final ClassLoader threadContextClassLoader = getThreadContextClassLoader();
        final ClassLoader loader1 = (class1 == null) ? null : class1.getClassLoader();
        final ClassLoader loader2 = (class2 == null) ? null : class2.getClassLoader();
        if (isChild(threadContextClassLoader, loader1)) {
            return isChild(threadContextClassLoader, loader2) ? threadContextClassLoader : loader2;
        }
        return isChild(loader1, loader2) ? loader1 : loader2;
    }
    
    public static URL getResource(final String resource, final ClassLoader defaultLoader) {
        try {
            ClassLoader classLoader = getThreadContextClassLoader();
            if (classLoader != null) {
                Loader.LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
                final URL url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                Loader.LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
                final URL url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            if (defaultLoader != null) {
                Loader.LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
                final URL url = defaultLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        }
        catch (final Throwable t) {
            Loader.LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
        }
        Loader.LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
        return ClassLoader.getSystemResource(resource);
    }
    
    public static InputStream getResourceAsStream(final String resource, final ClassLoader defaultLoader) {
        try {
            ClassLoader classLoader = getThreadContextClassLoader();
            if (classLoader != null) {
                Loader.LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
                final InputStream is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                Loader.LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
                final InputStream is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
            if (defaultLoader != null) {
                Loader.LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
                final InputStream is = defaultLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
        }
        catch (final Throwable t) {
            Loader.LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
        }
        Loader.LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
        return ClassLoader.getSystemResourceAsStream(resource);
    }
    
    private static boolean isChild(final ClassLoader loader1, final ClassLoader loader2) {
        if (loader1 != null && loader2 != null) {
            ClassLoader parent;
            for (parent = loader1.getParent(); parent != null && parent != loader2; parent = parent.getParent()) {}
            return parent != null;
        }
        return loader1 != null;
    }
    
    public static Class<?> initializeClass(final String className, final ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(className, true, loader);
    }
    
    public static Class<?> loadClass(final String className, final ClassLoader loader) throws ClassNotFoundException {
        return (loader != null) ? loader.loadClass(className) : null;
    }
    
    public static Class<?> loadSystemClass(final String className) throws ClassNotFoundException {
        try {
            return Class.forName(className, true, ClassLoader.getSystemClassLoader());
        }
        catch (final Throwable t) {
            Loader.LOGGER.trace("Couldn't use SystemClassLoader. Trying Class.forName({}).", className, t);
            return Class.forName(className);
        }
    }
    
    public static Object newInstanceOf(final String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return LoaderUtil.newInstanceOf(className);
    }
    
    public static <T> T newCheckedInstanceOf(final String className, final Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return LoaderUtil.newCheckedInstanceOf(className, clazz);
    }
    
    public static boolean isClassAvailable(final String className) {
        return LoaderUtil.isClassAvailable(className);
    }
    
    public static boolean isJansiAvailable() {
        return isClassAvailable("org.fusesource.jansi.AnsiRenderer");
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
