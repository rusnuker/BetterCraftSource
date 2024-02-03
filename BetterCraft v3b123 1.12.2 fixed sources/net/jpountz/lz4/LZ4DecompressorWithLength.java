// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.nio.ByteBuffer;

public class LZ4DecompressorWithLength
{
    private final LZ4FastDecompressor decompressor;
    
    public static int getDecompressedLength(final byte[] src) {
        return getDecompressedLength(src, 0);
    }
    
    public static int getDecompressedLength(final byte[] src, final int srcOff) {
        return (src[srcOff] & 0xFF) | (src[srcOff + 1] & 0xFF) << 8 | (src[srcOff + 2] & 0xFF) << 16 | src[srcOff + 3] << 24;
    }
    
    public static int getDecompressedLength(final ByteBuffer src) {
        return getDecompressedLength(src, src.position());
    }
    
    public static int getDecompressedLength(final ByteBuffer src, final int srcOff) {
        return (src.get(srcOff) & 0xFF) | (src.get(srcOff + 1) & 0xFF) << 8 | (src.get(srcOff + 2) & 0xFF) << 16 | src.get(srcOff + 3) << 24;
    }
    
    public LZ4DecompressorWithLength(final LZ4FastDecompressor decompressor) {
        this.decompressor = decompressor;
    }
    
    public int decompress(final byte[] src, final byte[] dest) {
        return this.decompress(src, 0, dest, 0);
    }
    
    public int decompress(final byte[] src, final int srcOff, final byte[] dest, final int destOff) {
        final int destLen = getDecompressedLength(src, srcOff);
        return this.decompressor.decompress(src, srcOff + 4, dest, destOff, destLen) + 4;
    }
    
    public byte[] decompress(final byte[] src) {
        return this.decompress(src, 0);
    }
    
    public byte[] decompress(final byte[] src, final int srcOff) {
        final int destLen = getDecompressedLength(src, srcOff);
        return this.decompressor.decompress(src, srcOff + 4, destLen);
    }
    
    public void decompress(final ByteBuffer src, final ByteBuffer dest) {
        final int destLen = getDecompressedLength(src, src.position());
        final int read = this.decompressor.decompress(src, src.position() + 4, dest, dest.position(), destLen);
        src.position(src.position() + 4 + read);
        dest.position(dest.position() + destLen);
    }
    
    public int decompress(final ByteBuffer src, final int srcOff, final ByteBuffer dest, final int destOff) {
        final int destLen = getDecompressedLength(src, srcOff);
        return this.decompressor.decompress(src, srcOff + 4, dest, destOff, destLen) + 4;
    }
}
