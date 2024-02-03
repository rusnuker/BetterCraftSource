// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

import java.util.zip.Checksum;
import java.io.Closeable;

public abstract class StreamingXXHash32 implements Closeable
{
    final int seed;
    
    StreamingXXHash32(final int seed) {
        this.seed = seed;
    }
    
    public abstract int getValue();
    
    public abstract void update(final byte[] p0, final int p1, final int p2);
    
    public abstract void reset();
    
    @Override
    public void close() {
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(seed=" + this.seed + ")";
    }
    
    public final Checksum asChecksum() {
        return new Checksum() {
            @Override
            public long getValue() {
                return (long)StreamingXXHash32.this.getValue() & 0xFFFFFFFL;
            }
            
            @Override
            public void reset() {
                StreamingXXHash32.this.reset();
            }
            
            @Override
            public void update(final int b) {
                StreamingXXHash32.this.update(new byte[] { (byte)b }, 0, 1);
            }
            
            @Override
            public void update(final byte[] b, final int off, final int len) {
                StreamingXXHash32.this.update(b, off, len);
            }
            
            @Override
            public String toString() {
                return StreamingXXHash32.this.toString();
            }
        };
    }
    
    interface Factory
    {
        StreamingXXHash32 newStreamingHash(final int p0);
    }
}
