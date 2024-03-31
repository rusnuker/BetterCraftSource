// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

public class LZ4Exception extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public LZ4Exception(final String msg, final Throwable t) {
        super(msg, t);
    }
    
    public LZ4Exception(final String msg) {
        super(msg);
    }
    
    public LZ4Exception() {
    }
}
