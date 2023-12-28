// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

public final class Closer
{
    private Closer() {
    }
    
    public static void close(final AutoCloseable closeable) throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }
    
    public static boolean closeSilently(final AutoCloseable closeable) {
        try {
            close(closeable);
            return true;
        }
        catch (final Exception ignored) {
            return false;
        }
    }
}
