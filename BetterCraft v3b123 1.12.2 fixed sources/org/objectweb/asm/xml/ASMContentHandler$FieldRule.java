// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.FieldVisitor;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

final class ASMContentHandler$FieldRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$FieldRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) throws SAXException {
        final int access = this.getAccess(attributes.getValue("access"));
        final String value = attributes.getValue("name");
        final String value2 = attributes.getValue("signature");
        final String value3 = attributes.getValue("desc");
        this.this$0.push(this.this$0.cv.visitField(access, value, value3, value2, this.getValue(value3, attributes.getValue("value"))));
    }
    
    public void end(final String s) {
        ((FieldVisitor)this.this$0.pop()).visitEnd();
    }
}
