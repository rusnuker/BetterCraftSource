// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm;

public abstract class FieldVisitor
{
    protected final int api;
    protected FieldVisitor fv;
    
    public FieldVisitor(final int api) {
        this(api, null);
    }
    
    public FieldVisitor(final int api, final FieldVisitor fieldVisitor) {
        if (api != 393216 && api != 327680 && api != 262144 && api != 458752) {
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.fv = fieldVisitor;
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (this.fv != null) {
            return this.fv.visitAnnotation(descriptor, visible);
        }
        return null;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        if (this.api < 327680) {
            throw new UnsupportedOperationException("This feature requires ASM5");
        }
        if (this.fv != null) {
            return this.fv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
        }
        return null;
    }
    
    public void visitAttribute(final Attribute attribute) {
        if (this.fv != null) {
            this.fv.visitAttribute(attribute);
        }
    }
    
    public void visitEnd() {
        if (this.fv != null) {
            this.fv.visitEnd();
        }
    }
}
