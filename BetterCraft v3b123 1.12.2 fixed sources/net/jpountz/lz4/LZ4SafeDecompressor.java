// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.util.Arrays;
import java.nio.ByteBuffer;

public abstract class LZ4SafeDecompressor implements LZ4UnknownSizeDecompressor
{
    @Override
    public abstract int decompress(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4, final int p5);
    
    public abstract int decompress(final ByteBuffer p0, final int p1, final int p2, final ByteBuffer p3, final int p4, final int p5);
    
    @Override
    public final int decompress(final byte[] src, final int srcOff, final int srcLen, final byte[] dest, final int destOff) {
        return this.decompress(src, srcOff, srcLen, dest, destOff, dest.length - destOff);
    }
    
    public final int decompress(final byte[] src, final byte[] dest) {
        return this.decompress(src, 0, src.length, dest, 0);
    }
    
    public final byte[] decompress(final byte[] src, final int srcOff, final int srcLen, final int maxDestLen) {
        byte[] decompressed = new byte[maxDestLen];
        final int decompressedLength = this.decompress(src, srcOff, srcLen, decompressed, 0, maxDestLen);
        if (decompressedLength != decompressed.length) {
            decompressed = Arrays.copyOf(decompressed, decompressedLength);
        }
        return decompressed;
    }
    
    public final byte[] decompress(final byte[] src, final int maxDestLen) {
        return this.decompress(src, 0, src.length, maxDestLen);
    }
    
    public final void decompress(final ByteBuffer src, final ByteBuffer dest) {
        final int decompressed = this.decompress(src, src.position(), src.remaining(), dest, dest.position(), dest.remaining());
        src.position(src.limit());
        dest.position(dest.position() + decompressed);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
