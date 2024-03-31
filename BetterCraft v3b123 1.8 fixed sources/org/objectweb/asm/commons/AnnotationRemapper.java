// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;

public class AnnotationRemapper extends AnnotationVisitor
{
    protected final Remapper remapper;
    
    public AnnotationRemapper(final AnnotationVisitor annotationVisitor, final Remapper remapper) {
        this(327680, annotationVisitor, remapper);
    }
    
    protected AnnotationRemapper(final int api, final AnnotationVisitor annotationVisitor, final Remapper remapper) {
        super(api, annotationVisitor);
        this.remapper = remapper;
    }
    
    public void visit(final String name, final Object o) {
        this.av.visit(name, this.remapper.mapValue(o));
    }
    
    public void visitEnum(final String name, final String s, final String value) {
        this.av.visitEnum(name, this.remapper.mapDesc(s), value);
    }
    
    public AnnotationVisitor visitAnnotation(final String name, final String s) {
        final AnnotationVisitor visitAnnotation = this.av.visitAnnotation(name, this.remapper.mapDesc(s));
        return (visitAnnotation == null) ? null : ((visitAnnotation == this.av) ? this : new AnnotationRemapper(visitAnnotation, this.remapper));
    }
    
    public AnnotationVisitor visitArray(final String name) {
        final AnnotationVisitor visitArray = this.av.visitArray(name);
        return (visitArray == null) ? null : ((visitArray == this.av) ? this : new AnnotationRemapper(visitArray, this.remapper));
    }
}
