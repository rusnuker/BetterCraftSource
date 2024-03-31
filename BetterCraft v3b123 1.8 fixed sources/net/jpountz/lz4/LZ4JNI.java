// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import net.jpountz.util.Native;
import java.nio.ByteBuffer;

enum LZ4JNI
{
    static native void init();
    
    static native int LZ4_compress_limitedOutput(final byte[] p0, final ByteBuffer p1, final int p2, final int p3, final byte[] p4, final ByteBuffer p5, final int p6, final int p7);
    
    static native int LZ4_compressHC(final byte[] p0, final ByteBuffer p1, final int p2, final int p3, final byte[] p4, final ByteBuffer p5, final int p6, final int p7, final int p8);
    
    static native int LZ4_decompress_fast(final byte[] p0, final ByteBuffer p1, final int p2, final byte[] p3, final ByteBuffer p4, final int p5, final int p6);
    
    static native int LZ4_decompress_safe(final byte[] p0, final ByteBuffer p1, final int p2, final int p3, final byte[] p4, final ByteBuffer p5, final int p6, final int p7);
    
    static native int LZ4_compressBound(final int p0);
    
    static {
        Native.load();
        init();
    }
}
