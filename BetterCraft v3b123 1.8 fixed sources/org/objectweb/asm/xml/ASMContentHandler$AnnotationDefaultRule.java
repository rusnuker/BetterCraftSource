// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.xml.sax.Attributes;

final class ASMContentHandler$AnnotationDefaultRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$AnnotationDefaultRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) {
        final MethodVisitor methodVisitor = (MethodVisitor)this.this$0.peek();
        this.this$0.push((methodVisitor == null) ? null : methodVisitor.visitAnnotationDefault());
    }
    
    public void end(final String s) {
        final AnnotationVisitor annotationVisitor = (AnnotationVisitor)this.this$0.pop();
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
    }
}
