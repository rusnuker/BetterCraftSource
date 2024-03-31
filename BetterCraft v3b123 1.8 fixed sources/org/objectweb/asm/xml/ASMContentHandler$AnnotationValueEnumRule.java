// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.xml.sax.Attributes;

final class ASMContentHandler$AnnotationValueEnumRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$AnnotationValueEnumRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) {
        final AnnotationVisitor annotationVisitor = (AnnotationVisitor)this.this$0.peek();
        if (annotationVisitor != null) {
            annotationVisitor.visitEnum(attributes.getValue("name"), attributes.getValue("desc"), attributes.getValue("value"));
        }
    }
}
