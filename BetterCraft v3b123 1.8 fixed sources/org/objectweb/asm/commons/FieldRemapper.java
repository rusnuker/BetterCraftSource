// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;

public class FieldRemapper extends FieldVisitor
{
    private final Remapper remapper;
    
    public FieldRemapper(final FieldVisitor fieldVisitor, final Remapper remapper) {
        this(327680, fieldVisitor, remapper);
    }
    
    protected FieldRemapper(final int api, final FieldVisitor fieldVisitor, final Remapper remapper) {
        super(api, fieldVisitor);
        this.remapper = remapper;
    }
    
    public AnnotationVisitor visitAnnotation(final String s, final boolean visible) {
        final AnnotationVisitor visitAnnotation = this.fv.visitAnnotation(this.remapper.mapDesc(s), visible);
        return (visitAnnotation == null) ? null : new AnnotationRemapper(visitAnnotation, this.remapper);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String s, final boolean visible) {
        final AnnotationVisitor visitTypeAnnotation = super.visitTypeAnnotation(typeRef, typePath, this.remapper.mapDesc(s), visible);
        return (visitTypeAnnotation == null) ? null : new AnnotationRemapper(visitTypeAnnotation, this.remapper);
    }
}
