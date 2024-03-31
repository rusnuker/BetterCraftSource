// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;

public final class TraceAnnotationVisitor extends AnnotationVisitor
{
    private final Printer p;
    
    public TraceAnnotationVisitor(final Printer printer) {
        this(null, printer);
    }
    
    public TraceAnnotationVisitor(final AnnotationVisitor annotationVisitor, final Printer p2) {
        super(327680, annotationVisitor);
        this.p = p2;
    }
    
    public void visit(final String name, final Object value) {
        this.p.visit(name, value);
        super.visit(name, value);
    }
    
    public void visitEnum(final String name, final String descriptor, final String value) {
        this.p.visitEnum(name, descriptor, value);
        super.visitEnum(name, descriptor, value);
    }
    
    public AnnotationVisitor visitAnnotation(final String name, final String descriptor) {
        return new TraceAnnotationVisitor((this.av == null) ? null : this.av.visitAnnotation(name, descriptor), this.p.visitAnnotation(name, descriptor));
    }
    
    public AnnotationVisitor visitArray(final String name) {
        return new TraceAnnotationVisitor((this.av == null) ? null : this.av.visitArray(name), this.p.visitArray(name));
    }
    
    public void visitEnd() {
        this.p.visitAnnotationEnd();
        super.visitEnd();
    }
}
