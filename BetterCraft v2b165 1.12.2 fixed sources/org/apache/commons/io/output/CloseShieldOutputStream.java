// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.io.output;

import java.io.OutputStream;

public class CloseShieldOutputStream extends ProxyOutputStream
{
    public CloseShieldOutputStream(final OutputStream out) {
        super(out);
    }
    
    @Override
    public void close() {
        this.out = new ClosedOutputStream();
    }
}
