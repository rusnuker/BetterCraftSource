// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

class ServiceLoaderUtil
{
    private static final String OSGI_SERVICE_LOADER_CLASS_NAME = "org.glassfish.hk2.osgiresourcelocator.ServiceLoader";
    private static final String OSGI_SERVICE_LOADER_METHOD_NAME = "lookupProviderClasses";
    
    static <P, T extends Exception> P firstByServiceLoader(final Class<P> spiClass, final Logger logger, final ExceptionHandler<T> handler) throws T, Exception {
        try {
            final ServiceLoader<P> serviceLoader = ServiceLoader.load(spiClass);
            final Iterator<P> iterator = serviceLoader.iterator();
            if (iterator.hasNext()) {
                final P impl = iterator.next();
                logger.fine("ServiceProvider loading Facility used; returning object [" + impl.getClass().getName() + "]");
                return impl;
            }
        }
        catch (final Throwable t) {
            throw handler.createException(t, "Error while searching for service [" + spiClass.getName() + "]");
        }
        return null;
    }
    
    static Object lookupUsingOSGiServiceLoader(final String factoryId, final Logger logger) {
        try {
            final Class serviceClass = Class.forName(factoryId);
            final Class target = Class.forName("org.glassfish.hk2.osgiresourcelocator.ServiceLoader");
            final Method m = target.getMethod("lookupProviderClasses", Class.class);
            final Iterator iter = ((Iterable)m.invoke(null, serviceClass)).iterator();
            if (iter.hasNext()) {
                final Object next = iter.next();
                logger.fine("Found implementation using OSGi facility; returning object [" + next.getClass().getName() + "].");
                return next;
            }
            return null;
        }
        catch (final IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException ignored) {
            logger.log(Level.FINE, "Unable to find from OSGi: [" + factoryId + "]", ignored);
            return null;
        }
    }
    
    static void checkPackageAccess(final String className) {
        final SecurityManager s = System.getSecurityManager();
        if (s != null) {
            final int i = className.lastIndexOf(46);
            if (i != -1) {
                s.checkPackageAccess(className.substring(0, i));
            }
        }
    }
    
    static Class nullSafeLoadClass(final String className, final ClassLoader classLoader) throws ClassNotFoundException {
        if (classLoader == null) {
            return Class.forName(className);
        }
        return classLoader.loadClass(className);
    }
    
    static <T extends Exception> Object newInstance(final String className, final String defaultImplClassName, final ExceptionHandler<T> handler) throws T, Exception {
        try {
            return safeLoadClass(className, defaultImplClassName, contextClassLoader(handler)).newInstance();
        }
        catch (final ClassNotFoundException x) {
            throw handler.createException(x, "Provider " + className + " not found");
        }
        catch (final Exception x2) {
            throw handler.createException(x2, "Provider " + className + " could not be instantiated: " + x2);
        }
    }
    
    static Class safeLoadClass(final String className, final String defaultImplClassName, final ClassLoader classLoader) throws ClassNotFoundException {
        try {
            checkPackageAccess(className);
        }
        catch (final SecurityException se) {
            if (defaultImplClassName != null && defaultImplClassName.equals(className)) {
                return Class.forName(className);
            }
            throw se;
        }
        return nullSafeLoadClass(className, classLoader);
    }
    
    static ClassLoader contextClassLoader(final ExceptionHandler exceptionHandler) throws Exception {
        try {
            return Thread.currentThread().getContextClassLoader();
        }
        catch (final Exception x) {
            throw exceptionHandler.createException(x, x.toString());
        }
    }
    
    abstract static class ExceptionHandler<T extends Exception>
    {
        public abstract T createException(final Throwable p0, final String p1);
    }
}
