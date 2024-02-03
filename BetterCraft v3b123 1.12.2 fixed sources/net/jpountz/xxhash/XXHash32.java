// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

import java.nio.ByteBuffer;

public abstract class XXHash32
{
    public abstract int hash(final byte[] p0, final int p1, final int p2, final int p3);
    
    public abstract int hash(final ByteBuffer p0, final int p1, final int p2, final int p3);
    
    public final int hash(final ByteBuffer buf, final int seed) {
        final int hash = this.hash(buf, buf.position(), buf.remaining(), seed);
        buf.position(buf.limit());
        return hash;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
