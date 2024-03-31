// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.Type;
import org.objectweb.asm.AnnotationVisitor;

public class CheckAnnotationAdapter extends AnnotationVisitor
{
    private final boolean named;
    private boolean end;
    
    public CheckAnnotationAdapter(final AnnotationVisitor annotationVisitor) {
        this(annotationVisitor, true);
    }
    
    CheckAnnotationAdapter(final AnnotationVisitor annotationVisitor, final boolean named) {
        super(327680, annotationVisitor);
        this.named = named;
    }
    
    public void visit(final String name, final Object value) {
        this.checkEnd();
        this.checkName(name);
        if (!(value instanceof Byte) && !(value instanceof Boolean) && !(value instanceof Character) && !(value instanceof Short) && !(value instanceof Integer) && !(value instanceof Long) && !(value instanceof Float) && !(value instanceof Double) && !(value instanceof String) && !(value instanceof Type) && !(value instanceof byte[]) && !(value instanceof boolean[]) && !(value instanceof char[]) && !(value instanceof short[]) && !(value instanceof int[]) && !(value instanceof long[]) && !(value instanceof float[]) && !(value instanceof double[])) {
            throw new IllegalArgumentException("Invalid annotation value");
        }
        if (value instanceof Type && ((Type)value).getSort() == 11) {
            throw new IllegalArgumentException("Invalid annotation value");
        }
        if (this.av != null) {
            this.av.visit(name, value);
        }
    }
    
    public void visitEnum(final String name, final String descriptor, final String value) {
        this.checkEnd();
        this.checkName(name);
        CheckMethodAdapter.checkDesc(descriptor, false);
        if (value == null) {
            throw new IllegalArgumentException("Invalid enum value");
        }
        if (this.av != null) {
            this.av.visitEnum(name, descriptor, value);
        }
    }
    
    public AnnotationVisitor visitAnnotation(final String name, final String descriptor) {
        this.checkEnd();
        this.checkName(name);
        CheckMethodAdapter.checkDesc(descriptor, false);
        return new CheckAnnotationAdapter((this.av == null) ? null : this.av.visitAnnotation(name, descriptor));
    }
    
    public AnnotationVisitor visitArray(final String name) {
        this.checkEnd();
        this.checkName(name);
        return new CheckAnnotationAdapter((this.av == null) ? null : this.av.visitArray(name), false);
    }
    
    public void visitEnd() {
        this.checkEnd();
        this.end = true;
        if (this.av != null) {
            this.av.visitEnd();
        }
    }
    
    private void checkEnd() {
        if (this.end) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }
    
    private void checkName(final String s) {
        if (this.named && s == null) {
            throw new IllegalArgumentException("Annotation value name must not be null");
        }
    }
}
