// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

import java.util.zip.Checksum;
import java.io.Closeable;

public abstract class StreamingXXHash64 implements Closeable
{
    final long seed;
    
    StreamingXXHash64(final long seed) {
        this.seed = seed;
    }
    
    public abstract long getValue();
    
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
                return StreamingXXHash64.this.getValue();
            }
            
            @Override
            public void reset() {
                StreamingXXHash64.this.reset();
            }
            
            @Override
            public void update(final int b) {
                StreamingXXHash64.this.update(new byte[] { (byte)b }, 0, 1);
            }
            
            @Override
            public void update(final byte[] b, final int off, final int len) {
                StreamingXXHash64.this.update(b, off, len);
            }
            
            @Override
            public String toString() {
                return StreamingXXHash64.this.toString();
            }
        };
    }
    
    interface Factory
    {
        StreamingXXHash64 newStreamingHash(final long p0);
    }
}
