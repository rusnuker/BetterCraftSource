// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;

public class RemappingClassAdapter extends ClassVisitor
{
    protected final Remapper remapper;
    protected String className;
    
    public RemappingClassAdapter(final ClassVisitor classVisitor, final Remapper remapper) {
        this(327680, classVisitor, remapper);
    }
    
    protected RemappingClassAdapter(final int api, final ClassVisitor classVisitor, final Remapper remapper) {
        super(api, classVisitor);
        this.remapper = remapper;
    }
    
    public void visit(final int version, final int access, final String className, final String s, final String s2, final String[] array) {
        this.className = className;
        super.visit(version, access, this.remapper.mapType(className), this.remapper.mapSignature(s, false), this.remapper.mapType(s2), (String[])((array == null) ? null : this.remapper.mapTypes(array)));
    }
    
    public AnnotationVisitor visitAnnotation(final String s, final boolean visible) {
        final AnnotationVisitor visitAnnotation = super.visitAnnotation(this.remapper.mapDesc(s), visible);
        return (visitAnnotation == null) ? null : this.createRemappingAnnotationAdapter(visitAnnotation);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String s, final boolean visible) {
        final AnnotationVisitor visitTypeAnnotation = super.visitTypeAnnotation(typeRef, typePath, this.remapper.mapDesc(s), visible);
        return (visitTypeAnnotation == null) ? null : this.createRemappingAnnotationAdapter(visitTypeAnnotation);
    }
    
    public FieldVisitor visitField(final int access, final String s, final String s2, final String s3, final Object o) {
        final FieldVisitor visitField = super.visitField(access, this.remapper.mapFieldName(this.className, s, s2), this.remapper.mapDesc(s2), this.remapper.mapSignature(s3, true), this.remapper.mapValue(o));
        return (visitField == null) ? null : this.createRemappingFieldAdapter(visitField);
    }
    
    public MethodVisitor visitMethod(final int access, final String s, final String s2, final String s3, final String[] array) {
        final String mapMethodDesc = this.remapper.mapMethodDesc(s2);
        final MethodVisitor visitMethod = super.visitMethod(access, this.remapper.mapMethodName(this.className, s, s2), mapMethodDesc, this.remapper.mapSignature(s3, false), (String[])((array == null) ? null : this.remapper.mapTypes(array)));
        return (visitMethod == null) ? null : this.createRemappingMethodAdapter(access, mapMethodDesc, visitMethod);
    }
    
    public void visitInnerClass(final String s, final String s2, final String innerName, final int access) {
        super.visitInnerClass(this.remapper.mapType(s), (s2 == null) ? null : this.remapper.mapType(s2), innerName, access);
    }
    
    public void visitOuterClass(final String s, final String s2, final String s3) {
        super.visitOuterClass(this.remapper.mapType(s), (s2 == null) ? null : this.remapper.mapMethodName(s, s2, s3), (s3 == null) ? null : this.remapper.mapMethodDesc(s3));
    }
    
    protected FieldVisitor createRemappingFieldAdapter(final FieldVisitor fieldVisitor) {
        return new RemappingFieldAdapter(fieldVisitor, this.remapper);
    }
    
    protected MethodVisitor createRemappingMethodAdapter(final int n, final String s, final MethodVisitor methodVisitor) {
        return new RemappingMethodAdapter(n, s, methodVisitor, this.remapper);
    }
    
    protected AnnotationVisitor createRemappingAnnotationAdapter(final AnnotationVisitor annotationVisitor) {
        return new RemappingAnnotationAdapter(annotationVisitor, this.remapper);
    }
}
