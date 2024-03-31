// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.protocol.chunk;

import java.beans.ConstructorProperties;

public class Extracted
{
    public short dataSize;
    public byte[] data;
    public int decompressedLength;
    
    @ConstructorProperties({ "dataSize", "data", "decompressedLength" })
    public Extracted(final short dataSize, final byte[] data, final int decompressedLength) {
        this.dataSize = dataSize;
        this.data = data;
        this.decompressedLength = decompressedLength;
    }
}
