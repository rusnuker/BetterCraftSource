// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

import java.nio.ByteBuffer;

public abstract class XXHash64
{
    public abstract long hash(final byte[] p0, final int p1, final int p2, final long p3);
    
    public abstract long hash(final ByteBuffer p0, final int p1, final int p2, final long p3);
    
    public final long hash(final ByteBuffer buf, final long seed) {
        final long hash = this.hash(buf, buf.position(), buf.remaining(), seed);
        buf.position(buf.limit());
        return hash;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
