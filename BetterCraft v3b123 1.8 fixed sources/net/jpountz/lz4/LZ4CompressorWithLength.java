// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.util.Arrays;
import java.nio.ByteBuffer;

public class LZ4CompressorWithLength
{
    private final LZ4Compressor compressor;
    
    public LZ4CompressorWithLength(final LZ4Compressor compressor) {
        this.compressor = compressor;
    }
    
    private void putOriginalLength(final byte[] dest, final int destOff, final int originalLength) {
        dest[destOff] = (byte)originalLength;
        dest[destOff + 1] = (byte)(originalLength >> 8);
        dest[destOff + 2] = (byte)(originalLength >> 16);
        dest[destOff + 3] = (byte)(originalLength >> 24);
    }
    
    private void putOriginalLength(final ByteBuffer dest, final int destOff, final int originalLength) {
        dest.put(destOff, (byte)originalLength);
        dest.put(destOff + 1, (byte)(originalLength >> 8));
        dest.put(destOff + 2, (byte)(originalLength >> 16));
        dest.put(destOff + 3, (byte)(originalLength >> 24));
    }
    
    public int maxCompressedLength(final int length) {
        return this.compressor.maxCompressedLength(length) + 4;
    }
    
    public byte[] compress(final byte[] src) {
        return this.compress(src, 0, src.length);
    }
    
    public byte[] compress(final byte[] src, final int srcOff, final int srcLen) {
        final int maxCompressedLength = this.maxCompressedLength(srcLen);
        final byte[] compressed = new byte[maxCompressedLength];
        final int compressedLength = this.compress(src, srcOff, srcLen, compressed, 0);
        return Arrays.copyOf(compressed, compressedLength);
    }
    
    public int compress(final byte[] src, final byte[] dest) {
        return this.compress(src, 0, src.length, dest, 0);
    }
    
    public int compress(final byte[] src, final int srcOff, final int srcLen, final byte[] dest, final int destOff) {
        return this.compress(src, srcOff, srcLen, dest, destOff, dest.length - destOff);
    }
    
    public int compress(final byte[] src, final int srcOff, final int srcLen, final byte[] dest, final int destOff, final int maxDestLen) {
        final int compressedLength = this.compressor.compress(src, srcOff, srcLen, dest, destOff + 4, maxDestLen - 4);
        this.putOriginalLength(dest, destOff, srcLen);
        return compressedLength + 4;
    }
    
    public void compress(final ByteBuffer src, final ByteBuffer dest) {
        final int compressedLength = this.compress(src, src.position(), src.remaining(), dest, dest.position(), dest.remaining());
        src.position(src.limit());
        dest.position(dest.position() + compressedLength);
    }
    
    public int compress(final ByteBuffer src, final int srcOff, final int srcLen, final ByteBuffer dest, final int destOff, final int maxDestLen) {
        final int compressedLength = this.compressor.compress(src, srcOff, srcLen, dest, destOff + 4, maxDestLen - 4);
        this.putOriginalLength(dest, destOff, srcLen);
        return compressedLength + 4;
    }
}
