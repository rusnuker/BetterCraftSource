// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal;

import java.lang.reflect.Field;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator
{
    public static final UnsafeAllocator INSTANCE;
    
    public abstract <T> T newInstance(final Class<T> p0) throws Exception;
    
    private static void assertInstantiable(final Class<?> c) {
        final String exceptionMessage = ConstructorConstructor.checkInstantiable(c);
        if (exceptionMessage != null) {
            throw new AssertionError((Object)("UnsafeAllocator is used for non-instantiable type: " + exceptionMessage));
        }
    }
    
    private static UnsafeAllocator create() {
        try {
            final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            final Object unsafe = f.get(null);
            final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
            return new UnsafeAllocator() {
                @Override
                public <T> T newInstance(final Class<T> c) throws Exception {
                    assertInstantiable(c);
                    return (T)allocateInstance.invoke(unsafe, c);
                }
            };
        }
        catch (final Exception ex) {
            try {
                final Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                getConstructorId.setAccessible(true);
                final int constructorId = (int)getConstructorId.invoke(null, Object.class);
                final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                newInstance.setAccessible(true);
                return new UnsafeAllocator() {
                    @Override
                    public <T> T newInstance(final Class<T> c) throws Exception {
                        assertInstantiable(c);
                        return (T)newInstance.invoke(null, c, constructorId);
                    }
                };
            }
            catch (final Exception ex2) {
                try {
                    final Method newInstance2 = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                    newInstance2.setAccessible(true);
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> c) throws Exception {
                            assertInstantiable(c);
                            return (T)newInstance2.invoke(null, c, Object.class);
                        }
                    };
                }
                catch (final Exception ex3) {
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> c) {
                            throw new UnsupportedOperationException("Cannot allocate " + c + ". Usage of JDK sun.misc.Unsafe is enabled, but it could not be used. Make sure your runtime is configured correctly.");
                        }
                    };
                }
            }
        }
    }
    
    static {
        INSTANCE = create();
    }
}
