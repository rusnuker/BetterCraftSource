// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.xml.sax.Attributes;

final class ASMContentHandler$AnnotationValueArrayRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$AnnotationValueArrayRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) {
        final AnnotationVisitor annotationVisitor = (AnnotationVisitor)this.this$0.peek();
        this.this$0.push((annotationVisitor == null) ? null : annotationVisitor.visitArray(attributes.getValue("name")));
    }
    
    public void end(final String s) {
        final AnnotationVisitor annotationVisitor = (AnnotationVisitor)this.this$0.pop();
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
    }
}
