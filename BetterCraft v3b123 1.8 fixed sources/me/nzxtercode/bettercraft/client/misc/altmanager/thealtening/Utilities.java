// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager.thealtening;

import java.io.IOException;
import java.util.Arrays;
import java.io.InputStream;

public class Utilities
{
    private static final Utilities INSTANCE;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int MAX_BUFFER_SIZE = 2147483639;
    
    static {
        INSTANCE = new Utilities();
    }
    
    public byte[] readAllBytes(final InputStream inputStream) throws IOException {
        byte[] buf = new byte[8192];
        int capacity = buf.length;
        int nread = 0;
        while (true) {
            final int n;
            if ((n = inputStream.read(buf, nread, capacity - nread)) <= 0) {
                if (n < 0) {
                    return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
                }
                if (capacity <= 2147483639 - capacity) {
                    capacity <<= 1;
                }
                else {
                    if (capacity == 2147483639) {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    capacity = 2147483639;
                }
                buf = Arrays.copyOf(buf, capacity);
            }
            else {
                nread += n;
            }
        }
    }
    
    public static Utilities getInstance() {
        return Utilities.INSTANCE;
    }
}
