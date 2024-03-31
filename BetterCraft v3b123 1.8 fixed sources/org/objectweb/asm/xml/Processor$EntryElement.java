// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.io.IOException;
import java.io.OutputStream;

interface Processor$EntryElement
{
    OutputStream openEntry(final String p0) throws IOException;
    
    void closeEntry() throws IOException;
}
