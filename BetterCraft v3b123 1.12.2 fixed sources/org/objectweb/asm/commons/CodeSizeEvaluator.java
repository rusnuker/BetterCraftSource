// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import org.objectweb.asm.Label;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.MethodVisitor;

public class CodeSizeEvaluator extends MethodVisitor implements Opcodes
{
    private int minSize;
    private int maxSize;
    
    public CodeSizeEvaluator(final MethodVisitor methodVisitor) {
        this(327680, methodVisitor);
    }
    
    protected CodeSizeEvaluator(final int api, final MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }
    
    public int getMinSize() {
        return this.minSize;
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    public void visitInsn(final int opcode) {
        ++this.minSize;
        ++this.maxSize;
        if (this.mv != null) {
            this.mv.visitInsn(opcode);
        }
    }
    
    public void visitIntInsn(final int opcode, final int operand) {
        if (opcode == 17) {
            this.minSize += 3;
            this.maxSize += 3;
        }
        else {
            this.minSize += 2;
            this.maxSize += 2;
        }
        if (this.mv != null) {
            this.mv.visitIntInsn(opcode, operand);
        }
    }
    
    public void visitVarInsn(final int opcode, final int var) {
        if (var < 4 && opcode != 169) {
            ++this.minSize;
            ++this.maxSize;
        }
        else if (var >= 256) {
            this.minSize += 4;
            this.maxSize += 4;
        }
        else {
            this.minSize += 2;
            this.maxSize += 2;
        }
        if (this.mv != null) {
            this.mv.visitVarInsn(opcode, var);
        }
    }
    
    public void visitTypeInsn(final int opcode, final String type) {
        this.minSize += 3;
        this.maxSize += 3;
        if (this.mv != null) {
            this.mv.visitTypeInsn(opcode, type);
        }
    }
    
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        this.minSize += 3;
        this.maxSize += 3;
        if (this.mv != null) {
            this.mv.visitFieldInsn(opcode, owner, name, descriptor);
        }
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
    
    private void doVisitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
        if (opcode == 185) {
            this.minSize += 5;
            this.maxSize += 5;
        }
        else {
            this.minSize += 3;
            this.maxSize += 3;
        }
        if (this.mv != null) {
            this.mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
    
    public void visitInvokeDynamicInsn(final String name, final String descriptor, final Handle bootstrapMethodHandle, final Object... bootstrapMethodArguments) {
        this.minSize += 5;
        this.maxSize += 5;
        if (this.mv != null) {
            this.mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        }
    }
    
    public void visitJumpInsn(final int opcode, final Label label) {
        this.minSize += 3;
        if (opcode == 167 || opcode == 168) {
            this.maxSize += 5;
        }
        else {
            this.maxSize += 8;
        }
        if (this.mv != null) {
            this.mv.visitJumpInsn(opcode, label);
        }
    }
    
    public void visitLdcInsn(final Object value) {
        if (value instanceof Long || value instanceof Double) {
            this.minSize += 3;
            this.maxSize += 3;
        }
        else {
            this.minSize += 2;
            this.maxSize += 3;
        }
        if (this.mv != null) {
            this.mv.visitLdcInsn(value);
        }
    }
    
    public void visitIincInsn(final int var, final int increment) {
        if (var > 255 || increment > 127 || increment < -128) {
            this.minSize += 6;
            this.maxSize += 6;
        }
        else {
            this.minSize += 3;
            this.maxSize += 3;
        }
        if (this.mv != null) {
            this.mv.visitIincInsn(var, increment);
        }
    }
    
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
        this.minSize += 13 + labels.length * 4;
        this.maxSize += 16 + labels.length * 4;
        if (this.mv != null) {
            this.mv.visitTableSwitchInsn(min, max, dflt, labels);
        }
    }
    
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        this.minSize += 9 + keys.length * 8;
        this.maxSize += 12 + keys.length * 8;
        if (this.mv != null) {
            this.mv.visitLookupSwitchInsn(dflt, keys, labels);
        }
    }
    
    public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
        this.minSize += 4;
        this.maxSize += 4;
        if (this.mv != null) {
            this.mv.visitMultiANewArrayInsn(descriptor, numDimensions);
        }
    }
}
