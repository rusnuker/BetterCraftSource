// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.SAXException;
import org.objectweb.asm.AnnotationVisitor;
import org.xml.sax.Attributes;

final class ASMContentHandler$AnnotationValueRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$AnnotationValueRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) throws SAXException {
        final AnnotationVisitor annotationVisitor = (AnnotationVisitor)this.this$0.peek();
        if (annotationVisitor != null) {
            annotationVisitor.visit(attributes.getValue("name"), this.getValue(attributes.getValue("desc"), attributes.getValue("value")));
        }
    }
}
