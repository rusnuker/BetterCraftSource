// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.io.IOException;
import java.io.OutputStream;

final class Processor$SingleDocElement implements Processor$EntryElement
{
    private final OutputStream os;
    
    Processor$SingleDocElement(final OutputStream os) {
        this.os = os;
    }
    
    public OutputStream openEntry(final String s) throws IOException {
        return this.os;
    }
    
    public void closeEntry() throws IOException {
        this.os.flush();
    }
}
