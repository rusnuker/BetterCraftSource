// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.xml.sax.ContentHandler;
import java.io.OutputStream;

final class Processor$ASMContentHandlerFactory implements Processor$ContentHandlerFactory
{
    final OutputStream os;
    
    Processor$ASMContentHandlerFactory(final OutputStream os) {
        this.os = os;
    }
    
    public final ContentHandler createContentHandler() {
        final ClassWriter classWriter = new ClassWriter(1);
        return new Processor$ASMContentHandlerFactory$1(this, classWriter, classWriter);
    }
}
