// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.Label;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;

public final class TraceMethodVisitor extends MethodVisitor
{
    public final Printer p;
    
    public TraceMethodVisitor(final Printer printer) {
        this(null, printer);
    }
    
    public TraceMethodVisitor(final MethodVisitor methodVisitor, final Printer p2) {
        super(327680, methodVisitor);
        this.p = p2;
    }
    
    public void visitParameter(final String name, final int access) {
        this.p.visitParameter(name, access);
        super.visitParameter(name, access);
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.mv == null) ? null : this.mv.visitAnnotation(descriptor, visible), this.p.visitMethodAnnotation(descriptor, visible));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.mv == null) ? null : this.mv.visitTypeAnnotation(typeRef, typePath, descriptor, visible), this.p.visitMethodTypeAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitAttribute(final Attribute attribute) {
        this.p.visitMethodAttribute(attribute);
        super.visitAttribute(attribute);
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        return new TraceAnnotationVisitor((this.mv == null) ? null : this.mv.visitAnnotationDefault(), this.p.visitAnnotationDefault());
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int parameter, final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.mv == null) ? null : this.mv.visitParameterAnnotation(parameter, descriptor, visible), this.p.visitParameterAnnotation(parameter, descriptor, visible));
    }
    
    public void visitCode() {
        this.p.visitCode();
        super.visitCode();
    }
    
    public void visitFrame(final int type, final int numLocal, final Object[] local, final int numStack, final Object[] stack) {
        this.p.visitFrame(type, numLocal, local, numStack, stack);
        super.visitFrame(type, numLocal, local, numStack, stack);
    }
    
    public void visitInsn(final int opcode) {
        this.p.visitInsn(opcode);
        super.visitInsn(opcode);
    }
    
    public void visitIntInsn(final int opcode, final int operand) {
        this.p.visitIntInsn(opcode, operand);
        super.visitIntInsn(opcode, operand);
    }
    
    public void visitVarInsn(final int opcode, final int var) {
        this.p.visitVarInsn(opcode, var);
        super.visitVarInsn(opcode, var);
    }
    
    public void visitTypeInsn(final int opcode, final String type) {
        this.p.visitTypeInsn(opcode, type);
        super.visitTypeInsn(opcode, type);
    }
    
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        this.p.visitFieldInsn(opcode, owner, name, descriptor);
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }
    
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(n, s, s2, s3);
            return;
        }
        this.p.visitMethodInsn(n, s, s2, s3);
        if (this.mv != null) {
            this.mv.visitMethodInsn(n, s, s2, s3);
        }
    }
    
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3, final boolean b) {
        if (this.api < 327680) {
            super.visitMethodInsn(n, s, s2, s3, b);
            return;
        }
        this.p.visitMethodInsn(n, s, s2, s3, b);
        if (this.mv != null) {
            this.mv.visitMethodInsn(n, s, s2, s3, b);
        }
    }
    
    public void visitInvokeDynamicInsn(final String name, final String descriptor, final Handle bootstrapMethodHandle, final Object... bootstrapMethodArguments) {
        this.p.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }
    
    public void visitJumpInsn(final int opcode, final Label label) {
        this.p.visitJumpInsn(opcode, label);
        super.visitJumpInsn(opcode, label);
    }
    
    public void visitLabel(final Label label) {
        this.p.visitLabel(label);
        super.visitLabel(label);
    }
    
    public void visitLdcInsn(final Object value) {
        this.p.visitLdcInsn(value);
        super.visitLdcInsn(value);
    }
    
    public void visitIincInsn(final int var, final int increment) {
        this.p.visitIincInsn(var, increment);
        super.visitIincInsn(var, increment);
    }
    
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
        this.p.visitTableSwitchInsn(min, max, dflt, labels);
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }
    
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        this.p.visitLookupSwitchInsn(dflt, keys, labels);
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }
    
    public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
        this.p.visitMultiANewArrayInsn(descriptor, numDimensions);
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.mv == null) ? null : this.mv.visitInsnAnnotation(typeRef, typePath, descriptor, visible), this.p.visitInsnAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
        this.p.visitTryCatchBlock(start, end, handler, type);
        super.visitTryCatchBlock(start, end, handler, type);
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.mv == null) ? null : this.mv.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible), this.p.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitLocalVariable(final String name, final String descriptor, final String signature, final Label start, final Label end, final int index) {
        this.p.visitLocalVariable(name, descriptor, signature, start, end, index);
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int typeRef, final TypePath typePath, final Label[] start, final Label[] end, final int[] index, final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.mv == null) ? null : this.mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible), this.p.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible));
    }
    
    public void visitLineNumber(final int line, final Label start) {
        this.p.visitLineNumber(line, start);
        super.visitLineNumber(line, start);
    }
    
    public void visitMaxs(final int maxStack, final int maxLocals) {
        this.p.visitMaxs(maxStack, maxLocals);
        super.visitMaxs(maxStack, maxLocals);
    }
    
    public void visitEnd() {
        this.p.visitMethodEnd();
        super.visitEnd();
    }
}
