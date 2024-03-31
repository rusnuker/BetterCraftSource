// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.lang.reflect.Method;
import java.util.Map;
import java.net.URL;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.security.PrivilegedAction;
import java.security.AccessController;
import java.util.logging.Logger;

class ContextFinder
{
    private static final String PLATFORM_DEFAULT_FACTORY_CLASS = "com.sun.xml.bind.v2.ContextFactory";
    private static final String JAXB_CONTEXT_FACTORY_DEPRECATED = "javax.xml.bind.context.factory";
    private static final Logger logger;
    private static ServiceLoaderUtil.ExceptionHandler<JAXBException> EXCEPTION_HANDLER;
    
    static {
        logger = Logger.getLogger("javax.xml.bind");
        try {
            if (AccessController.doPrivileged((PrivilegedAction<Object>)new GetPropertyAction("jaxb.debug")) != null) {
                ContextFinder.logger.setUseParentHandlers(false);
                ContextFinder.logger.setLevel(Level.ALL);
                final ConsoleHandler handler = new ConsoleHandler();
                handler.setLevel(Level.ALL);
                ContextFinder.logger.addHandler(handler);
            }
        }
        catch (final Throwable t) {}
        ContextFinder.EXCEPTION_HANDLER = new ServiceLoaderUtil.ExceptionHandler<JAXBException>() {
            @Override
            public JAXBException createException(final Throwable throwable, final String message) {
                return new JAXBException(message, throwable);
            }
        };
    }
    
    private static Throwable handleInvocationTargetException(final InvocationTargetException x) throws JAXBException {
        final Throwable t = x.getTargetException();
        if (t == null) {
            return x;
        }
        if (t instanceof JAXBException) {
            throw (JAXBException)t;
        }
        if (t instanceof RuntimeException) {
            throw (RuntimeException)t;
        }
        if (t instanceof Error) {
            throw (Error)t;
        }
        return t;
    }
    
    private static JAXBException handleClassCastException(final Class originalType, final Class targetType) {
        final URL targetTypeURL = which(targetType);
        return new JAXBException(Messages.format("JAXBContext.IllegalCast", getClassClassLoader(originalType).getResource("javax/xml/bind/JAXBContext.class"), targetTypeURL));
    }
    
    static JAXBContext newInstance(final String contextPath, final Class[] contextPathClasses, final String className, final ClassLoader classLoader, final Map properties) throws JAXBException {
        try {
            final Class spFactory = ServiceLoaderUtil.safeLoadClass(className, "com.sun.xml.bind.v2.ContextFactory", classLoader);
            return newInstance(contextPath, contextPathClasses, spFactory, classLoader, properties);
        }
        catch (final ClassNotFoundException x) {
            throw new JAXBException(Messages.format("ContextFinder.DefaultProviderNotFound"), x);
        }
        catch (final RuntimeException | JAXBException x2) {
            throw x2;
        }
        catch (final Exception x2) {
            throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", className, x2), x2);
        }
    }
    
    static JAXBContext newInstance(final String contextPath, final Class[] contextPathClasses, final Class spFactory, final ClassLoader classLoader, final Map properties) throws JAXBException {
        try {
            ModuleUtil.delegateAddOpensToImplModule(contextPathClasses, spFactory);
            Object context = null;
            try {
                final Method m = spFactory.getMethod("createContext", String.class, ClassLoader.class, Map.class);
                final Object obj = instantiateProviderIfNecessary(spFactory);
                context = m.invoke(obj, contextPath, classLoader, properties);
            }
            catch (final NoSuchMethodException ex) {}
            if (context == null) {
                final Method m = spFactory.getMethod("createContext", String.class, ClassLoader.class);
                final Object obj = instantiateProviderIfNecessary(spFactory);
                context = m.invoke(obj, contextPath, classLoader);
            }
            if (!(context instanceof JAXBContext)) {
                throw handleClassCastException(context.getClass(), JAXBContext.class);
            }
            return (JAXBContext)context;
        }
        catch (final InvocationTargetException x) {
            final Throwable e = handleInvocationTargetException(x);
            throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", spFactory, e), e);
        }
        catch (final Exception x2) {
            throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", spFactory, x2), x2);
        }
    }
    
    private static Object instantiateProviderIfNecessary(final Class<?> implClass) throws JAXBException {
        try {
            if (JAXBContextFactory.class.isAssignableFrom(implClass)) {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Object>)new PrivilegedExceptionAction<Object>() {
                    @Override
                    public Object run() throws Exception {
                        return implClass.newInstance();
                    }
                });
            }
            return null;
        }
        catch (final PrivilegedActionException x) {
            final Throwable e = (x.getCause() == null) ? x : x.getCause();
            throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", implClass, e), e);
        }
    }
    
    static JAXBContext newInstance(final Class[] classes, final Map properties, final String className) throws JAXBException {
        Class spi;
        try {
            spi = ServiceLoaderUtil.safeLoadClass(className, "com.sun.xml.bind.v2.ContextFactory", getContextClassLoader());
        }
        catch (final ClassNotFoundException e) {
            throw new JAXBException(Messages.format("ContextFinder.DefaultProviderNotFound"), e);
        }
        if (ContextFinder.logger.isLoggable(Level.FINE)) {
            ContextFinder.logger.log(Level.FINE, "loaded {0} from {1}", new Object[] { className, which(spi) });
        }
        return newInstance(classes, properties, spi);
    }
    
    static JAXBContext newInstance(final Class[] classes, final Map properties, final Class spFactory) throws JAXBException {
        try {
            ModuleUtil.delegateAddOpensToImplModule(classes, spFactory);
            final Method m = spFactory.getMethod("createContext", Class[].class, Map.class);
            final Object obj = instantiateProviderIfNecessary(spFactory);
            final Object context = m.invoke(obj, classes, properties);
            if (!(context instanceof JAXBContext)) {
                throw handleClassCastException(context.getClass(), JAXBContext.class);
            }
            return (JAXBContext)context;
        }
        catch (final NoSuchMethodException | IllegalAccessException e) {
            throw new JAXBException(e);
        }
        catch (final InvocationTargetException e2) {
            final Throwable x = handleInvocationTargetException(e2);
            throw new JAXBException(x);
        }
    }
    
    static JAXBContext find(final String factoryId, final String contextPath, final ClassLoader classLoader, final Map properties) throws JAXBException {
        if (contextPath == null || contextPath.isEmpty()) {
            throw new JAXBException(Messages.format("ContextFinder.NoPackageInContextPath"));
        }
        final Class[] contextPathClasses = ModuleUtil.getClassesFromContextPath(contextPath, classLoader);
        String factoryClassName = jaxbProperties(contextPath, classLoader, factoryId);
        if (factoryClassName == null && contextPathClasses != null) {
            factoryClassName = jaxbProperties(contextPathClasses, factoryId);
        }
        if (factoryClassName != null) {
            return newInstance(contextPath, contextPathClasses, factoryClassName, classLoader, properties);
        }
        String factoryName = classNameFromSystemProperties();
        if (factoryName != null) {
            return newInstance(contextPath, contextPathClasses, factoryName, classLoader, properties);
        }
        final JAXBContextFactory obj = ServiceLoaderUtil.firstByServiceLoader(JAXBContextFactory.class, ContextFinder.logger, ContextFinder.EXCEPTION_HANDLER);
        if (obj != null) {
            ModuleUtil.delegateAddOpensToImplModule(contextPathClasses, obj.getClass());
            return obj.createContext(contextPath, classLoader, properties);
        }
        factoryName = firstByServiceLoaderDeprecated(JAXBContext.class, classLoader);
        if (factoryName != null) {
            return newInstance(contextPath, contextPathClasses, factoryName, classLoader, properties);
        }
        final Class ctxFactory = (Class)ServiceLoaderUtil.lookupUsingOSGiServiceLoader("javax.xml.bind.JAXBContext", ContextFinder.logger);
        if (ctxFactory != null) {
            return newInstance(contextPath, contextPathClasses, ctxFactory, classLoader, properties);
        }
        ContextFinder.logger.fine("Trying to create the platform default provider");
        return newInstance(contextPath, contextPathClasses, "com.sun.xml.bind.v2.ContextFactory", classLoader, properties);
    }
    
    static JAXBContext find(final Class<?>[] classes, final Map<String, ?> properties) throws JAXBException {
        ContextFinder.logger.fine("Searching jaxb.properties");
        for (final Class c : classes) {
            if (c.getPackage() != null) {
                final URL jaxbPropertiesUrl = getResourceUrl(c, "jaxb.properties");
                if (jaxbPropertiesUrl != null) {
                    final String factoryClassName = classNameFromPackageProperties(jaxbPropertiesUrl, "javax.xml.bind.JAXBContextFactory", "javax.xml.bind.context.factory");
                    return newInstance(classes, properties, factoryClassName);
                }
            }
        }
        final String factoryClassName2 = classNameFromSystemProperties();
        if (factoryClassName2 != null) {
            return newInstance(classes, properties, factoryClassName2);
        }
        final JAXBContextFactory factory = ServiceLoaderUtil.firstByServiceLoader(JAXBContextFactory.class, ContextFinder.logger, ContextFinder.EXCEPTION_HANDLER);
        if (factory != null) {
            ModuleUtil.delegateAddOpensToImplModule(classes, factory.getClass());
            return factory.createContext(classes, properties);
        }
        final String className = firstByServiceLoaderDeprecated(JAXBContext.class, getContextClassLoader());
        if (className != null) {
            return newInstance(classes, properties, className);
        }
        ContextFinder.logger.fine("Trying to create the platform default provider");
        final Class ctxFactoryClass = (Class)ServiceLoaderUtil.lookupUsingOSGiServiceLoader("javax.xml.bind.JAXBContext", ContextFinder.logger);
        if (ctxFactoryClass != null) {
            return newInstance(classes, properties, ctxFactoryClass);
        }
        ContextFinder.logger.fine("Trying to create the platform default provider");
        return newInstance(classes, properties, "com.sun.xml.bind.v2.ContextFactory");
    }
    
    private static String classNameFromPackageProperties(final URL packagePropertiesUrl, final String... factoryIds) throws JAXBException {
        ContextFinder.logger.log(Level.FINE, "Trying to locate {0}", packagePropertiesUrl.toString());
        final Properties props = loadJAXBProperties(packagePropertiesUrl);
        for (final String factoryId : factoryIds) {
            if (props.containsKey(factoryId)) {
                return props.getProperty(factoryId);
            }
        }
        final String propertiesUrl = packagePropertiesUrl.toExternalForm();
        final String packageName = propertiesUrl.substring(0, propertiesUrl.indexOf("/jaxb.properties"));
        throw new JAXBException(Messages.format("ContextFinder.MissingProperty", packageName, factoryIds[0]));
    }
    
    private static String classNameFromSystemProperties() throws JAXBException {
        String factoryClassName = getSystemProperty("javax.xml.bind.JAXBContextFactory");
        if (factoryClassName != null) {
            return factoryClassName;
        }
        factoryClassName = getDeprecatedSystemProperty("javax.xml.bind.context.factory");
        if (factoryClassName != null) {
            return factoryClassName;
        }
        factoryClassName = getDeprecatedSystemProperty(JAXBContext.class.getName());
        if (factoryClassName != null) {
            return factoryClassName;
        }
        return null;
    }
    
    private static String getDeprecatedSystemProperty(final String property) {
        final String value = getSystemProperty(property);
        if (value != null) {
            ContextFinder.logger.log(Level.WARNING, "Using non-standard property: {0}. Property {1} should be used instead.", new Object[] { property, "javax.xml.bind.JAXBContextFactory" });
        }
        return value;
    }
    
    private static String getSystemProperty(final String property) {
        ContextFinder.logger.log(Level.FINE, "Checking system property {0}", property);
        final String value = AccessController.doPrivileged((PrivilegedAction<String>)new GetPropertyAction(property));
        if (value != null) {
            ContextFinder.logger.log(Level.FINE, "  found {0}", value);
        }
        else {
            ContextFinder.logger.log(Level.FINE, "  not found");
        }
        return value;
    }
    
    private static Properties loadJAXBProperties(final URL url) throws JAXBException {
        try {
            ContextFinder.logger.log(Level.FINE, "loading props from {0}", url);
            final Properties props = new Properties();
            final InputStream is = url.openStream();
            props.load(is);
            is.close();
            return props;
        }
        catch (final IOException ioe) {
            ContextFinder.logger.log(Level.FINE, "Unable to load " + url.toString(), ioe);
            throw new JAXBException(ioe.toString(), ioe);
        }
    }
    
    private static URL getResourceUrl(final ClassLoader classLoader, final String resourceName) {
        URL url;
        if (classLoader == null) {
            url = ClassLoader.getSystemResource(resourceName);
        }
        else {
            url = classLoader.getResource(resourceName);
        }
        return url;
    }
    
    private static URL getResourceUrl(final Class<?> clazz, final String resourceName) {
        return clazz.getResource(resourceName);
    }
    
    static URL which(final Class clazz, ClassLoader loader) {
        final String classnameAsResource = String.valueOf(clazz.getName().replace('.', '/')) + ".class";
        if (loader == null) {
            loader = getSystemClassLoader();
        }
        return loader.getResource(classnameAsResource);
    }
    
    static URL which(final Class clazz) {
        return which(clazz, getClassClassLoader(clazz));
    }
    
    private static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction() {
            @Override
            public Object run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }
    
    private static ClassLoader getClassClassLoader(final Class c) {
        if (System.getSecurityManager() == null) {
            return c.getClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction() {
            @Override
            public Object run() {
                return c.getClassLoader();
            }
        });
    }
    
    private static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction() {
            @Override
            public Object run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }
    
    @Deprecated
    static String firstByServiceLoaderDeprecated(final Class spiClass, final ClassLoader classLoader) throws JAXBException {
        final String jaxbContextFQCN = spiClass.getName();
        ContextFinder.logger.fine("Searching META-INF/services");
        BufferedReader r = null;
        final String resource = "META-INF/services/" + jaxbContextFQCN;
        try {
            final InputStream resourceStream = (classLoader == null) ? ClassLoader.getSystemResourceAsStream(resource) : classLoader.getResourceAsStream(resource);
            if (resourceStream != null) {
                r = new BufferedReader(new InputStreamReader(resourceStream, "UTF-8"));
                String factoryClassName = r.readLine();
                if (factoryClassName != null) {
                    factoryClassName = factoryClassName.trim();
                }
                r.close();
                ContextFinder.logger.log(Level.FINE, "Configured factorty class:{0}", factoryClassName);
                return factoryClassName;
            }
            ContextFinder.logger.log(Level.FINE, "Unable to load:{0}", resource);
            return null;
        }
        catch (final IOException e) {
            throw new JAXBException(e);
        }
        finally {
            try {
                if (r != null) {
                    r.close();
                }
            }
            catch (final IOException ex) {
                ContextFinder.logger.log(Level.SEVERE, "Unable to close resource: " + resource, ex);
            }
        }
    }
    
    private static String jaxbProperties(final String contextPath, final ClassLoader classLoader, final String factoryId) throws JAXBException {
        final String[] packages = contextPath.split(":");
        String[] array;
        for (int length = (array = packages).length, i = 0; i < length; ++i) {
            final String pkg = array[i];
            final String pkgUrl = pkg.replace('.', '/');
            final URL jaxbPropertiesUrl = getResourceUrl(classLoader, String.valueOf(pkgUrl) + "/jaxb.properties");
            if (jaxbPropertiesUrl != null) {
                return classNameFromPackageProperties(jaxbPropertiesUrl, factoryId, "javax.xml.bind.context.factory");
            }
        }
        return null;
    }
    
    private static String jaxbProperties(final Class[] classesFromContextPath, final String factoryId) throws JAXBException {
        for (final Class c : classesFromContextPath) {
            final URL jaxbPropertiesUrl = getResourceUrl(c, "jaxb.properties");
            if (jaxbPropertiesUrl != null) {
                return classNameFromPackageProperties(jaxbPropertiesUrl, factoryId, "javax.xml.bind.context.factory");
            }
        }
        return null;
    }
}
