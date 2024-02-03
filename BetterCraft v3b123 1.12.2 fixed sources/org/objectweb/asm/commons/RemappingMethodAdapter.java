// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import org.objectweb.asm.Label;
import org.objectweb.asm.Handle;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;

public class RemappingMethodAdapter extends LocalVariablesSorter
{
    protected final Remapper remapper;
    
    public RemappingMethodAdapter(final int n, final String s, final MethodVisitor methodVisitor, final Remapper remapper) {
        this(327680, n, s, methodVisitor, remapper);
    }
    
    protected RemappingMethodAdapter(final int n, final int n2, final String s, final MethodVisitor methodVisitor, final Remapper remapper) {
        super(n, n2, s, methodVisitor);
        this.remapper = remapper;
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        final AnnotationVisitor visitAnnotationDefault = super.visitAnnotationDefault();
        return (visitAnnotationDefault == null) ? visitAnnotationDefault : new RemappingAnnotationAdapter(visitAnnotationDefault, this.remapper);
    }
    
    public AnnotationVisitor visitAnnotation(final String s, final boolean visible) {
        final AnnotationVisitor visitAnnotation = super.visitAnnotation(this.remapper.mapDesc(s), visible);
        return (visitAnnotation == null) ? visitAnnotation : new RemappingAnnotationAdapter(visitAnnotation, this.remapper);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String s, final boolean visible) {
        final AnnotationVisitor visitTypeAnnotation = super.visitTypeAnnotation(typeRef, typePath, this.remapper.mapDesc(s), visible);
        return (visitTypeAnnotation == null) ? visitTypeAnnotation : new RemappingAnnotationAdapter(visitTypeAnnotation, this.remapper);
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int parameter, final String s, final boolean visible) {
        final AnnotationVisitor visitParameterAnnotation = super.visitParameterAnnotation(parameter, this.remapper.mapDesc(s), visible);
        return (visitParameterAnnotation == null) ? visitParameterAnnotation : new RemappingAnnotationAdapter(visitParameterAnnotation, this.remapper);
    }
    
    public void visitFrame(final int n, final int n2, final Object[] array, final int n3, final Object[] array2) {
        super.visitFrame(n, n2, this.remapEntries(n2, array), n3, this.remapEntries(n3, array2));
    }
    
    private Object[] remapEntries(final int n, final Object[] array) {
        for (int i = 0; i < n; ++i) {
            if (array[i] instanceof String) {
                final Object[] array2 = new Object[n];
                if (i > 0) {
                    System.arraycopy(array, 0, array2, 0, i);
                }
                do {
                    final Object o = array[i];
                    array2[i++] = ((o instanceof String) ? this.remapper.mapType((String)o) : o);
                } while (i < n);
                return array2;
            }
        }
        return array;
    }
    
    public void visitFieldInsn(final int opcode, final String s, final String s2, final String s3) {
        super.visitFieldInsn(opcode, this.remapper.mapType(s), this.remapper.mapFieldName(s, s2, s3), this.remapper.mapDesc(s3));
    }
    
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor) {
        if (this.api >= 327680) {
            super.visitMethodInsn(opcode, owner, name, descriptor);
            return;
        }
        this.doVisitMethodInsn(opcode, owner, name, descriptor, opcode == 185);
    }
    
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
        if (this.api < 327680) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            return;
        }
        this.doVisitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
    
    private void doVisitMethodInsn(final int opcode, final String s, final String s2, final String s3, final boolean isInterface) {
        if (this.mv != null) {
            this.mv.visitMethodInsn(opcode, this.remapper.mapType(s), this.remapper.mapMethodName(s, s2, s3), this.remapper.mapMethodDesc(s3), isInterface);
        }
    }
    
    public void visitInvokeDynamicInsn(final String s, final String s2, final Handle handle, final Object... bootstrapMethodArguments) {
        for (int i = 0; i < bootstrapMethodArguments.length; ++i) {
            bootstrapMethodArguments[i] = this.remapper.mapValue(bootstrapMethodArguments[i]);
        }
        super.visitInvokeDynamicInsn(this.remapper.mapInvokeDynamicMethodName(s, s2), this.remapper.mapMethodDesc(s2), (Handle)this.remapper.mapValue(handle), bootstrapMethodArguments);
    }
    
    public void visitTypeInsn(final int opcode, final String s) {
        super.visitTypeInsn(opcode, this.remapper.mapType(s));
    }
    
    public void visitLdcInsn(final Object o) {
        super.visitLdcInsn(this.remapper.mapValue(o));
    }
    
    public void visitMultiANewArrayInsn(final String s, final int numDimensions) {
        super.visitMultiANewArrayInsn(this.remapper.mapDesc(s), numDimensions);
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int typeRef, final TypePath typePath, final String s, final boolean visible) {
        final AnnotationVisitor visitInsnAnnotation = super.visitInsnAnnotation(typeRef, typePath, this.remapper.mapDesc(s), visible);
        return (visitInsnAnnotation == null) ? visitInsnAnnotation : new RemappingAnnotationAdapter(visitInsnAnnotation, this.remapper);
    }
    
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String s) {
        super.visitTryCatchBlock(start, end, handler, (s == null) ? null : this.remapper.mapType(s));
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int typeRef, final TypePath typePath, final String s, final boolean visible) {
        final AnnotationVisitor visitTryCatchAnnotation = super.visitTryCatchAnnotation(typeRef, typePath, this.remapper.mapDesc(s), visible);
        return (visitTryCatchAnnotation == null) ? visitTryCatchAnnotation : new RemappingAnnotationAdapter(visitTryCatchAnnotation, this.remapper);
    }
    
    public void visitLocalVariable(final String s, final String s2, final String s3, final Label label, final Label label2, final int n) {
        super.visitLocalVariable(s, this.remapper.mapDesc(s2), this.remapper.mapSignature(s3, true), label, label2, n);
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int n, final TypePath typePath, final Label[] array, final Label[] array2, final int[] array3, final String s, final boolean b) {
        final AnnotationVisitor visitLocalVariableAnnotation = super.visitLocalVariableAnnotation(n, typePath, array, array2, array3, this.remapper.mapDesc(s), b);
        return (visitLocalVariableAnnotation == null) ? visitLocalVariableAnnotation : new RemappingAnnotationAdapter(visitLocalVariableAnnotation, this.remapper);
    }
}
