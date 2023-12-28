// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

public final class ThrowableUtil
{
    private ThrowableUtil() {
    }
    
    public static <T extends Throwable> T unknownStackTrace(final T cause, final Class<?> clazz, final String method) {
        cause.setStackTrace(new StackTraceElement[] { new StackTraceElement(clazz.getName(), method, null, -1) });
        return cause;
    }
    
    public static String stackTraceToString(final Throwable cause) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream pout = new PrintStream(out);
        cause.printStackTrace(pout);
        pout.flush();
        try {
            return new String(out.toByteArray());
        }
        finally {
            try {
                out.close();
            }
            catch (final IOException ex) {}
        }
    }
}
