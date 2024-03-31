// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.nio.ByteBuffer;

public abstract class LZ4FastDecompressor implements LZ4Decompressor
{
    @Override
    public abstract int decompress(final byte[] p0, final int p1, final byte[] p2, final int p3, final int p4);
    
    public abstract int decompress(final ByteBuffer p0, final int p1, final ByteBuffer p2, final int p3, final int p4);
    
    public final int decompress(final byte[] src, final byte[] dest, final int destLen) {
        return this.decompress(src, 0, dest, 0, destLen);
    }
    
    public final int decompress(final byte[] src, final byte[] dest) {
        return this.decompress(src, dest, dest.length);
    }
    
    public final byte[] decompress(final byte[] src, final int srcOff, final int destLen) {
        final byte[] decompressed = new byte[destLen];
        this.decompress(src, srcOff, decompressed, 0, destLen);
        return decompressed;
    }
    
    public final byte[] decompress(final byte[] src, final int destLen) {
        return this.decompress(src, 0, destLen);
    }
    
    public final void decompress(final ByteBuffer src, final ByteBuffer dest) {
        final int read = this.decompress(src, src.position(), dest, dest.position(), dest.remaining());
        dest.position(dest.limit());
        src.position(src.position() + read);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
