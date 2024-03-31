// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.ClassVisitor;
import org.xml.sax.Attributes;

final class ASMContentHandler$AnnotationRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$AnnotationRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) {
        final String value = attributes.getValue("desc");
        final boolean booleanValue = Boolean.valueOf(attributes.getValue("visible"));
        final Object peek = this.this$0.peek();
        if (peek instanceof ClassVisitor) {
            this.this$0.push(((ClassVisitor)peek).visitAnnotation(value, booleanValue));
        }
        else if (peek instanceof FieldVisitor) {
            this.this$0.push(((FieldVisitor)peek).visitAnnotation(value, booleanValue));
        }
        else if (peek instanceof MethodVisitor) {
            this.this$0.push(((MethodVisitor)peek).visitAnnotation(value, booleanValue));
        }
    }
    
    public void end(final String s) {
        final AnnotationVisitor annotationVisitor = (AnnotationVisitor)this.this$0.pop();
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
    }
}
