// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.security.Permission;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Stack;
import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;

public final class ReflectionUtil
{
    static final int JDK_7u25_OFFSET;
    private static final Logger LOGGER;
    private static final boolean SUN_REFLECTION_SUPPORTED;
    private static final Method GET_CALLER_CLASS;
    private static final PrivateSecurityManager SECURITY_MANAGER;
    
    private ReflectionUtil() {
    }
    
    public static boolean supportsFastReflection() {
        return ReflectionUtil.SUN_REFLECTION_SUPPORTED;
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final int depth) {
        if (depth < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(depth));
        }
        if (supportsFastReflection()) {
            try {
                return (Class)ReflectionUtil.GET_CALLER_CLASS.invoke(null, depth + 1 + ReflectionUtil.JDK_7u25_OFFSET);
            }
            catch (final Exception e) {
                ReflectionUtil.LOGGER.error("Error in ReflectionUtil.getCallerClass({}).", (Object)depth, e);
                return null;
            }
        }
        final StackTraceElement element = getEquivalentStackTraceElement(depth + 1);
        try {
            return LoaderUtil.loadClass(element.getClassName());
        }
        catch (final ClassNotFoundException e2) {
            ReflectionUtil.LOGGER.error("Could not find class in ReflectionUtil.getCallerClass({}).", (Object)depth, e2);
            return null;
        }
    }
    
    static StackTraceElement getEquivalentStackTraceElement(final int depth) {
        final StackTraceElement[] elements = new Throwable().getStackTrace();
        int i = 0;
        for (final StackTraceElement element : elements) {
            if (isValid(element)) {
                if (i == depth) {
                    return element;
                }
                ++i;
            }
        }
        ReflectionUtil.LOGGER.error("Could not find an appropriate StackTraceElement at index {}", (Object)depth);
        throw new IndexOutOfBoundsException(Integer.toString(depth));
    }
    
    private static boolean isValid(final StackTraceElement element) {
        if (element.isNativeMethod()) {
            return false;
        }
        final String cn = element.getClassName();
        if (cn.startsWith("sun.reflect.")) {
            return false;
        }
        final String mn = element.getMethodName();
        return (!cn.startsWith("java.lang.reflect.") || (!mn.equals("invoke") && !mn.equals("newInstance"))) && !cn.startsWith("jdk.internal.reflect.") && (!cn.equals("java.lang.Class") || !mn.equals("newInstance")) && (!cn.equals("java.lang.invoke.MethodHandle") || !mn.startsWith("invoke"));
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final String fqcn) {
        return getCallerClass(fqcn, "");
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final String fqcn, final String pkg) {
        if (supportsFastReflection()) {
            boolean next = false;
            Class<?> clazz;
            for (int i = 2; null != (clazz = getCallerClass(i)); ++i) {
                if (fqcn.equals(clazz.getName())) {
                    next = true;
                }
                else if (next && clazz.getName().startsWith(pkg)) {
                    return clazz;
                }
            }
            return null;
        }
        if (ReflectionUtil.SECURITY_MANAGER != null) {
            return ReflectionUtil.SECURITY_MANAGER.getCallerClass(fqcn, pkg);
        }
        try {
            return LoaderUtil.loadClass(getCallerClassName(fqcn, pkg, new Throwable().getStackTrace()));
        }
        catch (final ClassNotFoundException ex) {
            return null;
        }
    }
    
    @PerformanceSensitive
    public static Class<?> getCallerClass(final Class<?> anchor) {
        if (supportsFastReflection()) {
            boolean next = false;
            Class<?> clazz;
            for (int i = 2; null != (clazz = getCallerClass(i)); ++i) {
                if (anchor.equals(clazz)) {
                    next = true;
                }
                else if (next) {
                    return clazz;
                }
            }
            return Object.class;
        }
        if (ReflectionUtil.SECURITY_MANAGER != null) {
            return ReflectionUtil.SECURITY_MANAGER.getCallerClass(anchor);
        }
        try {
            return LoaderUtil.loadClass(getCallerClassName(anchor.getName(), "", new Throwable().getStackTrace()));
        }
        catch (final ClassNotFoundException ex) {
            return Object.class;
        }
    }
    
    private static String getCallerClassName(final String fqcn, final String pkg, final StackTraceElement... elements) {
        boolean next = false;
        for (final StackTraceElement element : elements) {
            final String className = element.getClassName();
            if (className.equals(fqcn)) {
                next = true;
            }
            else if (next && className.startsWith(pkg)) {
                return className;
            }
        }
        return Object.class.getName();
    }
    
    @PerformanceSensitive
    public static Stack<Class<?>> getCurrentStackTrace() {
        if (ReflectionUtil.SECURITY_MANAGER != null) {
            final Class<?>[] array = ReflectionUtil.SECURITY_MANAGER.getClassContext();
            final Stack<Class<?>> classes = new Stack<Class<?>>();
            classes.ensureCapacity(array.length);
            for (final Class<?> clazz : array) {
                classes.push(clazz);
            }
            return classes;
        }
        if (supportsFastReflection()) {
            final Stack<Class<?>> classes2 = new Stack<Class<?>>();
            Class<?> clazz2;
            for (int i = 1; null != (clazz2 = getCallerClass(i)); ++i) {
                classes2.push(clazz2);
            }
            return classes2;
        }
        return new Stack<Class<?>>();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        int java7u25CompensationOffset = 0;
        Method getCallerClass;
        try {
            final Class<?> sunReflectionClass = LoaderUtil.loadClass("sun.reflect.Reflection");
            getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", Integer.TYPE);
            Object o = getCallerClass.invoke(null, 0);
            final Object test1 = getCallerClass.invoke(null, 0);
            if (o == null || o != sunReflectionClass) {
                ReflectionUtil.LOGGER.warn("Unexpected return value from Reflection.getCallerClass(): {}", test1);
                getCallerClass = null;
                java7u25CompensationOffset = -1;
            }
            else {
                o = getCallerClass.invoke(null, 1);
                if (o == sunReflectionClass) {
                    ReflectionUtil.LOGGER.warn("You are using Java 1.7.0_25 which has a broken implementation of Reflection.getCallerClass.");
                    ReflectionUtil.LOGGER.warn("You should upgrade to at least Java 1.7.0_40 or later.");
                    ReflectionUtil.LOGGER.debug("Using stack depth compensation offset of 1 due to Java 7u25.");
                    java7u25CompensationOffset = 1;
                }
            }
        }
        catch (final Exception | LinkageError e) {
            ReflectionUtil.LOGGER.info("sun.reflect.Reflection.getCallerClass is not supported. ReflectionUtil.getCallerClass will be much slower due to this.", e);
            getCallerClass = null;
            java7u25CompensationOffset = -1;
        }
        SUN_REFLECTION_SUPPORTED = (getCallerClass != null);
        GET_CALLER_CLASS = getCallerClass;
        JDK_7u25_OFFSET = java7u25CompensationOffset;
        PrivateSecurityManager psm;
        try {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new RuntimePermission("createSecurityManager"));
            }
            psm = new PrivateSecurityManager();
        }
        catch (final SecurityException ignored) {
            ReflectionUtil.LOGGER.debug("Not allowed to create SecurityManager. Falling back to slowest ReflectionUtil implementation.");
            psm = null;
        }
        SECURITY_MANAGER = psm;
    }
    
    static final class PrivateSecurityManager extends SecurityManager
    {
        @Override
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
        
        protected Class<?> getCallerClass(final String fqcn, final String pkg) {
            boolean next = false;
            for (final Class<?> clazz : this.getClassContext()) {
                if (fqcn.equals(clazz.getName())) {
                    next = true;
                }
                else if (next && clazz.getName().startsWith(pkg)) {
                    return clazz;
                }
            }
            return null;
        }
        
        protected Class<?> getCallerClass(final Class<?> anchor) {
            boolean next = false;
            for (final Class<?> clazz : this.getClassContext()) {
                if (anchor.equals(clazz)) {
                    next = true;
                }
                else if (next) {
                    return clazz;
                }
            }
            return Object.class;
        }
    }
}
