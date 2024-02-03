// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import java.util.Collection;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.Label;
import java.util.HashMap;
import java.util.ArrayList;
import org.objectweb.asm.MethodVisitor;
import java.util.Map;
import java.util.List;
import org.objectweb.asm.Opcodes;

public abstract class AdviceAdapter extends GeneratorAdapter implements Opcodes
{
    private static final Object THIS;
    private static final Object OTHER;
    protected int methodAccess;
    protected String methodDesc;
    private boolean constructor;
    private boolean superInitialized;
    private List stackFrame;
    private Map branches;
    
    protected AdviceAdapter(final int n, final MethodVisitor methodVisitor, final int methodAccess, final String s, final String methodDesc) {
        super(n, methodVisitor, methodAccess, s, methodDesc);
        this.methodAccess = methodAccess;
        this.methodDesc = methodDesc;
        this.constructor = "<init>".equals(s);
    }
    
    public void visitCode() {
        this.mv.visitCode();
        if (this.constructor) {
            this.stackFrame = new ArrayList();
            this.branches = new HashMap();
        }
        else {
            this.superInitialized = true;
            this.onMethodEnter();
        }
    }
    
    public void visitLabel(final Label label) {
        this.mv.visitLabel(label);
        if (this.constructor && this.branches != null) {
            final List stackFrame = this.branches.get(label);
            if (stackFrame != null) {
                this.stackFrame = stackFrame;
                this.branches.remove(label);
            }
        }
    }
    
    public void visitInsn(final int opcode) {
        if (this.constructor) {
            switch (opcode) {
                case 177: {
                    this.onMethodExit(opcode);
                    break;
                }
                case 172:
                case 174:
                case 176:
                case 191: {
                    this.popValue();
                    this.onMethodExit(opcode);
                    break;
                }
                case 173:
                case 175: {
                    this.popValue();
                    this.popValue();
                    this.onMethodExit(opcode);
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 11:
                case 12:
                case 13:
                case 133:
                case 135:
                case 140:
                case 141: {
                    this.pushValue(AdviceAdapter.OTHER);
                    break;
                }
                case 9:
                case 10:
                case 14:
                case 15: {
                    this.pushValue(AdviceAdapter.OTHER);
                    this.pushValue(AdviceAdapter.OTHER);
                    break;
                }
                case 46:
                case 48:
                case 50:
                case 51:
                case 52:
                case 53:
                case 87:
                case 96:
                case 98:
                case 100:
                case 102:
                case 104:
                case 106:
                case 108:
                case 110:
                case 112:
                case 114:
                case 120:
                case 121:
                case 122:
                case 123:
                case 124:
                case 125:
                case 126:
                case 128:
                case 130:
                case 136:
                case 137:
                case 142:
                case 144:
                case 149:
                case 150:
                case 194:
                case 195: {
                    this.popValue();
                    break;
                }
                case 88:
                case 97:
                case 99:
                case 101:
                case 103:
                case 105:
                case 107:
                case 109:
                case 111:
                case 113:
                case 115:
                case 127:
                case 129:
                case 131: {
                    this.popValue();
                    this.popValue();
                    break;
                }
                case 79:
                case 81:
                case 83:
                case 84:
                case 85:
                case 86:
                case 148:
                case 151:
                case 152: {
                    this.popValue();
                    this.popValue();
                    this.popValue();
                    break;
                }
                case 80:
                case 82: {
                    this.popValue();
                    this.popValue();
                    this.popValue();
                    this.popValue();
                    break;
                }
                case 89: {
                    this.pushValue(this.peekValue());
                    break;
                }
                case 90: {
                    final int size = this.stackFrame.size();
                    this.stackFrame.add(size - 2, this.stackFrame.get(size - 1));
                    break;
                }
                case 91: {
                    final int size2 = this.stackFrame.size();
                    this.stackFrame.add(size2 - 3, this.stackFrame.get(size2 - 1));
                    break;
                }
                case 92: {
                    final int size3 = this.stackFrame.size();
                    this.stackFrame.add(size3 - 2, this.stackFrame.get(size3 - 1));
                    this.stackFrame.add(size3 - 2, this.stackFrame.get(size3 - 1));
                    break;
                }
                case 93: {
                    final int size4 = this.stackFrame.size();
                    this.stackFrame.add(size4 - 3, this.stackFrame.get(size4 - 1));
                    this.stackFrame.add(size4 - 3, this.stackFrame.get(size4 - 1));
                    break;
                }
                case 94: {
                    final int size5 = this.stackFrame.size();
                    this.stackFrame.add(size5 - 4, this.stackFrame.get(size5 - 1));
                    this.stackFrame.add(size5 - 4, this.stackFrame.get(size5 - 1));
                    break;
                }
                case 95: {
                    final int size6 = this.stackFrame.size();
                    this.stackFrame.add(size6 - 2, this.stackFrame.get(size6 - 1));
                    this.stackFrame.remove(size6);
                    break;
                }
            }
        }
        else {
            switch (opcode) {
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 191: {
                    this.onMethodExit(opcode);
                    break;
                }
            }
        }
        this.mv.visitInsn(opcode);
    }
    
    public void visitVarInsn(final int n, final int n2) {
        super.visitVarInsn(n, n2);
        if (this.constructor) {
            switch (n) {
                case 21:
                case 23: {
                    this.pushValue(AdviceAdapter.OTHER);
                    break;
                }
                case 22:
                case 24: {
                    this.pushValue(AdviceAdapter.OTHER);
                    this.pushValue(AdviceAdapter.OTHER);
                    break;
                }
                case 25: {
                    this.pushValue((n2 == 0) ? AdviceAdapter.THIS : AdviceAdapter.OTHER);
                    break;
                }
                case 54:
                case 56:
                case 58: {
                    this.popValue();
                    break;
                }
                case 55:
                case 57: {
                    this.popValue();
                    this.popValue();
                    break;
                }
            }
        }
    }
    
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        this.mv.visitFieldInsn(opcode, owner, name, descriptor);
        if (this.constructor) {
            final char char1 = descriptor.charAt(0);
            final boolean b = char1 == 'J' || char1 == 'D';
            switch (opcode) {
                case 178: {
                    this.pushValue(AdviceAdapter.OTHER);
                    if (b) {
                        this.pushValue(AdviceAdapter.OTHER);
                        break;
                    }
                    break;
                }
                case 179: {
                    this.popValue();
                    if (b) {
                        this.popValue();
                        break;
                    }
                    break;
                }
                case 181: {
                    this.popValue();
                    this.popValue();
                    if (b) {
                        this.popValue();
                        break;
                    }
                    break;
                }
                default: {
                    if (b) {
                        this.pushValue(AdviceAdapter.OTHER);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public void visitIntInsn(final int opcode, final int operand) {
        this.mv.visitIntInsn(opcode, operand);
        if (this.constructor && opcode != 188) {
            this.pushValue(AdviceAdapter.OTHER);
        }
    }
    
    public void visitLdcInsn(final Object value) {
        this.mv.visitLdcInsn(value);
        if (this.constructor) {
            this.pushValue(AdviceAdapter.OTHER);
            if (value instanceof Double || value instanceof Long) {
                this.pushValue(AdviceAdapter.OTHER);
            }
        }
    }
    
    public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
        this.mv.visitMultiANewArrayInsn(descriptor, numDimensions);
        if (this.constructor) {
            for (int i = 0; i < numDimensions; ++i) {
                this.popValue();
            }
            this.pushValue(AdviceAdapter.OTHER);
        }
    }
    
    public void visitTypeInsn(final int opcode, final String type) {
        this.mv.visitTypeInsn(opcode, type);
        if (this.constructor && opcode == 187) {
            this.pushValue(AdviceAdapter.OTHER);
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
    
    private void doVisitMethodInsn(final int opcode, final String owner, final String name, final String methodDescriptor, final boolean isInterface) {
        this.mv.visitMethodInsn(opcode, owner, name, methodDescriptor, isInterface);
        if (this.constructor) {
            final Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
            for (int i = 0; i < argumentTypes.length; ++i) {
                this.popValue();
                if (argumentTypes[i].getSize() == 2) {
                    this.popValue();
                }
            }
            switch (opcode) {
                case 182:
                case 185: {
                    this.popValue();
                    break;
                }
                case 183: {
                    if (this.popValue() == AdviceAdapter.THIS && !this.superInitialized) {
                        this.onMethodEnter();
                        this.superInitialized = true;
                        this.constructor = false;
                        break;
                    }
                    break;
                }
            }
            final Type returnType = Type.getReturnType(methodDescriptor);
            if (returnType != Type.VOID_TYPE) {
                this.pushValue(AdviceAdapter.OTHER);
                if (returnType.getSize() == 2) {
                    this.pushValue(AdviceAdapter.OTHER);
                }
            }
        }
    }
    
    public void visitInvokeDynamicInsn(final String name, final String methodDescriptor, final Handle bootstrapMethodHandle, final Object... bootstrapMethodArguments) {
        this.mv.visitInvokeDynamicInsn(name, methodDescriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        if (this.constructor) {
            final Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
            for (int i = 0; i < argumentTypes.length; ++i) {
                this.popValue();
                if (argumentTypes[i].getSize() == 2) {
                    this.popValue();
                }
            }
            final Type returnType = Type.getReturnType(methodDescriptor);
            if (returnType != Type.VOID_TYPE) {
                this.pushValue(AdviceAdapter.OTHER);
                if (returnType.getSize() == 2) {
                    this.pushValue(AdviceAdapter.OTHER);
                }
            }
        }
    }
    
    public void visitJumpInsn(final int opcode, final Label label) {
        this.mv.visitJumpInsn(opcode, label);
        if (this.constructor) {
            switch (opcode) {
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 198:
                case 199: {
                    this.popValue();
                    break;
                }
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166: {
                    this.popValue();
                    this.popValue();
                    break;
                }
                case 168: {
                    this.pushValue(AdviceAdapter.OTHER);
                    break;
                }
            }
            this.addBranch(label);
        }
    }
    
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        this.mv.visitLookupSwitchInsn(dflt, keys, labels);
        if (this.constructor) {
            this.popValue();
            this.addBranches(dflt, labels);
        }
    }
    
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
        this.mv.visitTableSwitchInsn(min, max, dflt, labels);
        if (this.constructor) {
            this.popValue();
            this.addBranches(dflt, labels);
        }
    }
    
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
        super.visitTryCatchBlock(start, end, handler, type);
        if (this.constructor && !this.branches.containsKey(handler)) {
            final ArrayList list = new ArrayList();
            list.add(AdviceAdapter.OTHER);
            this.branches.put(handler, list);
        }
    }
    
    private void addBranches(final Label label, final Label[] array) {
        this.addBranch(label);
        for (int i = 0; i < array.length; ++i) {
            this.addBranch(array[i]);
        }
    }
    
    private void addBranch(final Label label) {
        if (this.branches.containsKey(label)) {
            return;
        }
        this.branches.put(label, new ArrayList(this.stackFrame));
    }
    
    private Object popValue() {
        return this.stackFrame.remove(this.stackFrame.size() - 1);
    }
    
    private Object peekValue() {
        return this.stackFrame.get(this.stackFrame.size() - 1);
    }
    
    private void pushValue(final Object o) {
        this.stackFrame.add(o);
    }
    
    protected void onMethodEnter() {
    }
    
    protected void onMethodExit(final int n) {
    }
    
    static {
        _clinit_();
        THIS = new Object();
        OTHER = new Object();
    }
    
    static /* synthetic */ void _clinit_() {
    }
}
