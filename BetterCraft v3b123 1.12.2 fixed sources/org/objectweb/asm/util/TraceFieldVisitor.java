// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;

public final class TraceFieldVisitor extends FieldVisitor
{
    public final Printer p;
    
    public TraceFieldVisitor(final Printer printer) {
        this(null, printer);
    }
    
    public TraceFieldVisitor(final FieldVisitor fieldVisitor, final Printer p2) {
        super(327680, fieldVisitor);
        this.p = p2;
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.fv == null) ? null : this.fv.visitAnnotation(descriptor, visible), this.p.visitFieldAnnotation(descriptor, visible));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.fv == null) ? null : this.fv.visitTypeAnnotation(typeRef, typePath, descriptor, visible), this.p.visitFieldTypeAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitAttribute(final Attribute attribute) {
        this.p.visitFieldAttribute(attribute);
        super.visitAttribute(attribute);
    }
    
    public void visitEnd() {
        this.p.visitFieldEnd();
        super.visitEnd();
    }
}
