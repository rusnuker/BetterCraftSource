// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.gen;

import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

public class AccessorGeneratorMethodProxy extends AccessorGenerator
{
    protected final MethodNode targetMethod;
    protected final Type[] argTypes;
    protected final Type returnType;
    
    public AccessorGeneratorMethodProxy(final AccessorInfo info) {
        super(info, Bytecode.isStatic(info.getTargetMethod()));
        this.targetMethod = info.getTargetMethod();
        this.argTypes = info.getArgTypes();
        this.returnType = info.getReturnType();
        this.checkModifiers();
    }
    
    protected AccessorGeneratorMethodProxy(final AccessorInfo info, final boolean isStatic) {
        super(info, isStatic);
        this.targetMethod = info.getTargetMethod();
        this.argTypes = info.getArgTypes();
        this.returnType = info.getReturnType();
    }
    
    @Override
    public MethodNode generate() {
        final int size = Bytecode.getArgsSize(this.argTypes) + this.returnType.getSize() + (this.targetIsStatic ? 0 : 1);
        final MethodNode method = this.createMethod(size, size);
        if (!this.targetIsStatic) {
            method.instructions.add(new VarInsnNode(25, 0));
        }
        Bytecode.loadArgs(this.argTypes, method.instructions, this.info.isStatic ? 0 : 1);
        final boolean isPrivate = Bytecode.hasFlag(this.targetMethod, 2);
        final int opcode = this.targetIsStatic ? 184 : (isPrivate ? 183 : 182);
        method.instructions.add(new MethodInsnNode(opcode, this.info.getClassNode().name, this.targetMethod.name, this.targetMethod.desc, false));
        method.instructions.add(new InsnNode(this.returnType.getOpcode(172)));
        return method;
    }
}
