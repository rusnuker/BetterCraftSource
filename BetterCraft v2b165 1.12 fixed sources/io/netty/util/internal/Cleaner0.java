// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.lang.reflect.Field;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import io.netty.util.internal.logging.InternalLogger;
import java.lang.reflect.Method;

final class Cleaner0
{
    private static final long CLEANER_FIELD_OFFSET;
    private static final Method CLEAN_METHOD;
    private static final boolean CLEANER_IS_RUNNABLE;
    private static final InternalLogger logger;
    
    static void freeDirectBuffer(final ByteBuffer buffer) {
        if (Cleaner0.CLEANER_FIELD_OFFSET == -1L || !buffer.isDirect()) {
            return;
        }
        assert !(!Cleaner0.CLEANER_IS_RUNNABLE) : "CLEANER_FIELD_OFFSET != -1 implies CLEAN_METHOD != null or CLEANER_IS_RUNNABLE == true";
        try {
            final Object cleaner = PlatformDependent0.getObject(buffer, Cleaner0.CLEANER_FIELD_OFFSET);
            if (cleaner != null) {
                if (Cleaner0.CLEANER_IS_RUNNABLE) {
                    ((Runnable)cleaner).run();
                }
                else {
                    Cleaner0.CLEAN_METHOD.invoke(cleaner, new Object[0]);
                }
            }
        }
        catch (final Throwable t) {}
    }
    
    private Cleaner0() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Cleaner0.class);
        final ByteBuffer direct = ByteBuffer.allocateDirect(1);
        long fieldOffset = -1L;
        Method clean = null;
        boolean cleanerIsRunnable = false;
        Throwable error = null;
        if (PlatformDependent0.hasUnsafe()) {
            try {
                final Field cleanerField = direct.getClass().getDeclaredField("cleaner");
                fieldOffset = PlatformDependent0.objectFieldOffset(cleanerField);
                final Object cleaner = PlatformDependent0.getObject(direct, fieldOffset);
                try {
                    final Runnable runnable = (Runnable)cleaner;
                    runnable.run();
                    cleanerIsRunnable = true;
                }
                catch (final ClassCastException ignored) {
                    clean = cleaner.getClass().getDeclaredMethod("clean", (Class<?>[])new Class[0]);
                    clean.invoke(cleaner, new Object[0]);
                }
            }
            catch (final Throwable t) {
                fieldOffset = -1L;
                clean = null;
                cleanerIsRunnable = false;
                error = t;
            }
        }
        if (error == null) {
            Cleaner0.logger.debug("java.nio.ByteBuffer.cleaner(): available");
        }
        else {
            Cleaner0.logger.debug("java.nio.ByteBuffer.cleaner(): unavailable", error);
        }
        CLEANER_FIELD_OFFSET = fieldOffset;
        CLEAN_METHOD = clean;
        CLEANER_IS_RUNNABLE = cleanerIsRunnable;
        freeDirectBuffer(direct);
    }
}
