// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

@Deprecated
public interface LZ4UnknownSizeDecompressor
{
    int decompress(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4, final int p5);
    
    int decompress(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4);
}
