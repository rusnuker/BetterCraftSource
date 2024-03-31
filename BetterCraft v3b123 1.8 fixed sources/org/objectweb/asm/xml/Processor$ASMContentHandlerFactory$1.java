// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

class Processor$ASMContentHandlerFactory$1 extends ASMContentHandler
{
    final /* synthetic */ ClassWriter val$cw;
    final /* synthetic */ Processor$ASMContentHandlerFactory this$0;
    
    Processor$ASMContentHandlerFactory$1(final Processor$ASMContentHandlerFactory this$0, final ClassVisitor classVisitor, final ClassWriter val$cw) {
        this.this$0 = this$0;
        this.val$cw = val$cw;
        super(classVisitor);
    }
    
    public void endDocument() throws SAXException {
        try {
            this.this$0.os.write(this.val$cw.toByteArray());
        }
        catch (final IOException ex) {
            throw new SAXException(ex);
        }
    }
}
