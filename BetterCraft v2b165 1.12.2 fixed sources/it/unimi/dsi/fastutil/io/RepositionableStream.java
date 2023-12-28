// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.io;

import java.io.IOException;

public interface RepositionableStream
{
    void position(final long p0) throws IOException;
    
    long position() throws IOException;
}
