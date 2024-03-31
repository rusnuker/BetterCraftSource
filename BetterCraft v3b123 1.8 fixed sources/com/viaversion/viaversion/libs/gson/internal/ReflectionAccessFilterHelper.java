// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal;

import java.lang.reflect.Method;
import java.lang.reflect.AccessibleObject;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.ReflectionAccessFilter;
import java.util.List;

public class ReflectionAccessFilterHelper
{
    private ReflectionAccessFilterHelper() {
    }
    
    public static boolean isJavaType(final Class<?> c) {
        return isJavaType(c.getName());
    }
    
    private static boolean isJavaType(final String className) {
        return className.startsWith("java.") || className.startsWith("javax.");
    }
    
    public static boolean isAndroidType(final Class<?> c) {
        return isAndroidType(c.getName());
    }
    
    private static boolean isAndroidType(final String className) {
        return className.startsWith("android.") || className.startsWith("androidx.") || isJavaType(className);
    }
    
    public static boolean isAnyPlatformType(final Class<?> c) {
        final String className = c.getName();
        return isAndroidType(className) || className.startsWith("kotlin.") || className.startsWith("kotlinx.") || className.startsWith("scala.");
    }
    
    public static ReflectionAccessFilter.FilterResult getFilterResult(final List<ReflectionAccessFilter> reflectionFilters, final Class<?> c) {
        for (final ReflectionAccessFilter filter : reflectionFilters) {
            final ReflectionAccessFilter.FilterResult result = filter.check(c);
            if (result != ReflectionAccessFilter.FilterResult.INDECISIVE) {
                return result;
            }
        }
        return ReflectionAccessFilter.FilterResult.ALLOW;
    }
    
    public static boolean canAccess(final AccessibleObject accessibleObject, final Object object) {
        return AccessChecker.INSTANCE.canAccess(accessibleObject, object);
    }
    
    private abstract static class AccessChecker
    {
        public static final AccessChecker INSTANCE;
        
        public abstract boolean canAccess(final AccessibleObject p0, final Object p1);
        
        static {
            AccessChecker accessChecker = null;
            if (JavaVersion.isJava9OrLater()) {
                try {
                    final Method canAccessMethod = AccessibleObject.class.getDeclaredMethod("canAccess", Object.class);
                    accessChecker = new AccessChecker() {
                        final /* synthetic */ Method val$canAccessMethod;
                        
                        @Override
                        public boolean canAccess(final AccessibleObject accessibleObject, final Object object) {
                            try {
                                return (boolean)this.val$canAccessMethod.invoke(accessibleObject, object);
                            }
                            catch (final Exception e) {
                                throw new RuntimeException("Failed invoking canAccess", e);
                            }
                        }
                    };
                }
                catch (final NoSuchMethodException ex) {}
            }
            if (accessChecker == null) {
                accessChecker = new AccessChecker() {
                    @Override
                    public boolean canAccess(final AccessibleObject accessibleObject, final Object object) {
                        return true;
                    }
                };
            }
            INSTANCE = accessChecker;
        }
    }
}
