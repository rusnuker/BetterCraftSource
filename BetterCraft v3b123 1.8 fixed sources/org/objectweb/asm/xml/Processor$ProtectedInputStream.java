// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.io.IOException;
import java.io.InputStream;

final class Processor$ProtectedInputStream extends InputStream
{
    private final InputStream is;
    
    Processor$ProtectedInputStream(final InputStream is) {
        this.is = is;
    }
    
    public final void close() throws IOException {
    }
    
    public final int read() throws IOException {
        return this.is.read();
    }
    
    public final int read(final byte[] array, final int n, final int n2) throws IOException {
        return this.is.read(array, n, n2);
    }
    
    public final int available() throws IOException {
        return this.is.available();
    }
}
