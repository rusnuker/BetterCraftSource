// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import java.io.Serializable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;

public class LocalVariablesSorter extends MethodVisitor
{
    private static final Type OBJECT_TYPE;
    private int[] mapping;
    private Object[] newLocals;
    protected final int firstLocal;
    protected int nextLocal;
    static /* synthetic */ Class class$org$objectweb$asm$commons$LocalVariablesSorter;
    
    public LocalVariablesSorter(final int n, final String s, final MethodVisitor methodVisitor) {
        this(327680, n, s, methodVisitor);
        if (this.getClass() != LocalVariablesSorter.class$org$objectweb$asm$commons$LocalVariablesSorter) {
            throw new IllegalStateException();
        }
    }
    
    protected LocalVariablesSorter(final int api, final int n, final String methodDescriptor, final MethodVisitor methodVisitor) {
        super(api, methodVisitor);
        this.mapping = new int[40];
        this.newLocals = new Object[20];
        final Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
        this.nextLocal = (((0x8 & n) == 0x0) ? 1 : 0);
        for (int i = 0; i < argumentTypes.length; ++i) {
            this.nextLocal += argumentTypes[i].getSize();
        }
        this.firstLocal = this.nextLocal;
    }
    
    public void visitVarInsn(final int opcode, final int n) {
        Type type = null;
        switch (opcode) {
            case 22:
            case 55: {
                type = Type.LONG_TYPE;
                break;
            }
            case 24:
            case 57: {
                type = Type.DOUBLE_TYPE;
                break;
            }
            case 23:
            case 56: {
                type = Type.FLOAT_TYPE;
                break;
            }
            case 21:
            case 54: {
                type = Type.INT_TYPE;
                break;
            }
            default: {
                type = LocalVariablesSorter.OBJECT_TYPE;
                break;
            }
        }
        this.mv.visitVarInsn(opcode, this.remap(n, type));
    }
    
    public void visitIincInsn(final int n, final int increment) {
        this.mv.visitIincInsn(this.remap(n, Type.INT_TYPE), increment);
    }
    
    public void visitMaxs(final int maxStack, final int n) {
        this.mv.visitMaxs(maxStack, this.nextLocal);
    }
    
    public void visitLocalVariable(final String name, final String s, final String signature, final Label start, final Label end, final int n) {
        this.mv.visitLocalVariable(name, s, signature, start, end, this.remap(n, Type.getType(s)));
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int typeRef, final TypePath typePath, final Label[] start, final Label[] end, final int[] array, final String s, final boolean visible) {
        final Type type = Type.getType(s);
        final int[] index = new int[array.length];
        for (int i = 0; i < index.length; ++i) {
            index[i] = this.remap(array[i], type);
        }
        return this.mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, s, visible);
    }
    
    public void visitFrame(final int type, final int n, final Object[] array, final int numStack, final Object[] stack) {
        if (type != -1) {
            throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
        }
        final Object[] newLocals = new Object[this.newLocals.length];
        System.arraycopy(this.newLocals, 0, newLocals, 0, newLocals.length);
        this.updateNewLocals(this.newLocals);
        int n2 = 0;
        for (final Object o : array) {
            final int n3 = (o == Opcodes.LONG || o == Opcodes.DOUBLE) ? 2 : 1;
            if (o != Opcodes.TOP) {
                Type type2 = LocalVariablesSorter.OBJECT_TYPE;
                if (o == Opcodes.INTEGER) {
                    type2 = Type.INT_TYPE;
                }
                else if (o == Opcodes.FLOAT) {
                    type2 = Type.FLOAT_TYPE;
                }
                else if (o == Opcodes.LONG) {
                    type2 = Type.LONG_TYPE;
                }
                else if (o == Opcodes.DOUBLE) {
                    type2 = Type.DOUBLE_TYPE;
                }
                else if (o instanceof String) {
                    type2 = Type.getObjectType((String)o);
                }
                this.setFrameLocal(this.remap(n2, type2), o);
            }
            n2 += n3;
        }
        int j = 0;
        int numLocal = 0;
        int n4 = 0;
        while (j < this.newLocals.length) {
            final Object o2 = this.newLocals[j++];
            if (o2 != null && o2 != Opcodes.TOP) {
                this.newLocals[n4] = o2;
                numLocal = n4 + 1;
                if (o2 == Opcodes.LONG || o2 == Opcodes.DOUBLE) {
                    ++j;
                }
            }
            else {
                this.newLocals[n4] = Opcodes.TOP;
            }
            ++n4;
        }
        this.mv.visitFrame(type, numLocal, this.newLocals, numStack, stack);
        this.newLocals = newLocals;
    }
    
    public int newLocal(final Type type) {
        Serializable s = null;
        switch (type.getSort()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                s = Opcodes.INTEGER;
                break;
            }
            case 6: {
                s = Opcodes.FLOAT;
                break;
            }
            case 7: {
                s = Opcodes.LONG;
                break;
            }
            case 8: {
                s = Opcodes.DOUBLE;
                break;
            }
            case 9: {
                s = type.getDescriptor();
                break;
            }
            default: {
                s = type.getInternalName();
                break;
            }
        }
        final int localMapping = this.newLocalMapping(type);
        this.setLocalType(localMapping, type);
        this.setFrameLocal(localMapping, s);
        return localMapping;
    }
    
    protected void updateNewLocals(final Object[] array) {
    }
    
    protected void setLocalType(final int n, final Type type) {
    }
    
    private void setFrameLocal(final int n, final Object o) {
        final int length = this.newLocals.length;
        if (n >= length) {
            final Object[] newLocals = new Object[Math.max(2 * length, n + 1)];
            System.arraycopy(this.newLocals, 0, newLocals, 0, length);
            this.newLocals = newLocals;
        }
        this.newLocals[n] = o;
    }
    
    private int remap(final int n, final Type type) {
        if (n + type.getSize() <= this.firstLocal) {
            return n;
        }
        final int n2 = 2 * n + type.getSize() - 1;
        final int length = this.mapping.length;
        if (n2 >= length) {
            final int[] mapping = new int[Math.max(2 * length, n2 + 1)];
            System.arraycopy(this.mapping, 0, mapping, 0, length);
            this.mapping = mapping;
        }
        int localMapping = this.mapping[n2];
        if (localMapping == 0) {
            localMapping = this.newLocalMapping(type);
            this.setLocalType(localMapping, type);
            this.mapping[n2] = localMapping + 1;
        }
        else {
            --localMapping;
        }
        return localMapping;
    }
    
    protected int newLocalMapping(final Type type) {
        final int nextLocal = this.nextLocal;
        this.nextLocal += type.getSize();
        return nextLocal;
    }
    
    static {
        _clinit_();
        OBJECT_TYPE = Type.getObjectType("java/lang/Object");
    }
    
    static /* synthetic */ Class class$(final String s) {
        try {
            return Class.forName(s);
        }
        catch (final ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    private static void _clinit_() {
        LocalVariablesSorter.class$org$objectweb$asm$commons$LocalVariablesSorter = class$("org.objectweb.asm.commons.LocalVariablesSorter");
    }
}
