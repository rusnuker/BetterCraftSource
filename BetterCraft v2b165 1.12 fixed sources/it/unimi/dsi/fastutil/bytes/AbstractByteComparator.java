// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;

public abstract class AbstractByteComparator implements ByteComparator, Serializable
{
    private static final long serialVersionUID = 0L;
    
    protected AbstractByteComparator() {
    }
    
    @Override
    public int compare(final Byte ok1, final Byte ok2) {
        return this.compare((byte)ok1, (byte)ok2);
    }
    
    @Override
    public abstract int compare(final byte p0, final byte p1);
}
