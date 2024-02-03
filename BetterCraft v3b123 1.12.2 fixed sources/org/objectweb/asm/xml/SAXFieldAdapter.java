// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.xml.sax.Attributes;
import org.objectweb.asm.FieldVisitor;

public final class SAXFieldAdapter extends FieldVisitor
{
    SAXAdapter sa;
    
    public SAXFieldAdapter(final SAXAdapter sa, final Attributes attributes) {
        super(327680);
        (this.sa = sa).addStart("field", attributes);
    }
    
    public AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "annotation", b ? 1 : -1, null, s);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "typeAnnotation", b ? 1 : -1, null, s, n, typePath);
    }
    
    public void visitEnd() {
        this.sa.addEnd("field");
    }
}
