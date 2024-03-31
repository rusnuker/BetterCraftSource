// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

abstract class AbstractStreamingXXHash32Java extends StreamingXXHash32
{
    int v1;
    int v2;
    int v3;
    int v4;
    int memSize;
    long totalLen;
    final byte[] memory;
    
    AbstractStreamingXXHash32Java(final int seed) {
        super(seed);
        this.memory = new byte[16];
        this.reset();
    }
    
    @Override
    public void reset() {
        this.v1 = this.seed - 1640531535 - 2048144777;
        this.v2 = this.seed - 2048144777;
        this.v3 = this.seed + 0;
        this.v4 = this.seed + 1640531535;
        this.totalLen = 0L;
        this.memSize = 0;
    }
}
