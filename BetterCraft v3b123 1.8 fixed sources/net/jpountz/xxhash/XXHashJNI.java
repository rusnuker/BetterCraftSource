// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

import net.jpountz.util.Native;
import java.nio.ByteBuffer;

enum XXHashJNI
{
    private static native void init();
    
    static native int XXH32(final byte[] p0, final int p1, final int p2, final int p3);
    
    static native int XXH32BB(final ByteBuffer p0, final int p1, final int p2, final int p3);
    
    static native long XXH32_init(final int p0);
    
    static native void XXH32_update(final long p0, final byte[] p1, final int p2, final int p3);
    
    static native int XXH32_digest(final long p0);
    
    static native void XXH32_free(final long p0);
    
    static native long XXH64(final byte[] p0, final int p1, final int p2, final long p3);
    
    static native long XXH64BB(final ByteBuffer p0, final int p1, final int p2, final long p3);
    
    static native long XXH64_init(final long p0);
    
    static native void XXH64_update(final long p0, final byte[] p1, final int p2, final int p3);
    
    static native long XXH64_digest(final long p0);
    
    static native void XXH64_free(final long p0);
    
    static {
        Native.load();
        init();
    }
}
