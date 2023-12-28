// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class UnsafeAccess
{
    public static final boolean SUPPORTS_GET_AND_SET;
    public static final Unsafe UNSAFE;
    
    static {
        Unsafe instance;
        try {
            final Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            instance = (Unsafe)field.get(null);
        }
        catch (final Exception ignored) {
            try {
                final Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor((Class<?>[])new Class[0]);
                c.setAccessible(true);
                instance = c.newInstance(new Object[0]);
            }
            catch (final Exception e) {
                SUPPORTS_GET_AND_SET = false;
                throw new RuntimeException(e);
            }
        }
        boolean getAndSetSupport = false;
        try {
            Unsafe.class.getMethod("getAndSetObject", Object.class, Long.TYPE, Object.class);
            getAndSetSupport = true;
        }
        catch (final Exception ex) {}
        UNSAFE = instance;
        SUPPORTS_GET_AND_SET = getAndSetSupport;
    }
}
