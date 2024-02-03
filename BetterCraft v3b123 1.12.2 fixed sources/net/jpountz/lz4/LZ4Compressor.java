// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.util.Arrays;
import java.nio.ByteBuffer;

public abstract class LZ4Compressor
{
    public final int maxCompressedLength(final int length) {
        return LZ4Utils.maxCompressedLength(length);
    }
    
    public abstract int compress(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4, final int p5);
    
    public abstract int compress(final ByteBuffer p0, final int p1, final int p2, final ByteBuffer p3, final int p4, final int p5);
    
    public final int compress(final byte[] src, final int srcOff, final int srcLen, final byte[] dest, final int destOff) {
        return this.compress(src, srcOff, srcLen, dest, destOff, dest.length - destOff);
    }
    
    public final int compress(final byte[] src, final byte[] dest) {
        return this.compress(src, 0, src.length, dest, 0);
    }
    
    public final byte[] compress(final byte[] src, final int srcOff, final int srcLen) {
        final int maxCompressedLength = this.maxCompressedLength(srcLen);
        final byte[] compressed = new byte[maxCompressedLength];
        final int compressedLength = this.compress(src, srcOff, srcLen, compressed, 0);
        return Arrays.copyOf(compressed, compressedLength);
    }
    
    public final byte[] compress(final byte[] src) {
        return this.compress(src, 0, src.length);
    }
    
    public final void compress(final ByteBuffer src, final ByteBuffer dest) {
        final int cpLen = this.compress(src, src.position(), src.remaining(), dest, dest.position(), dest.remaining());
        src.position(src.limit());
        dest.position(dest.position() + cpLen);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
