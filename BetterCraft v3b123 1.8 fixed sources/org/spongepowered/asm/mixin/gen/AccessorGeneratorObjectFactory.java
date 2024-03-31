// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.gen;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;

public class AccessorGeneratorObjectFactory extends AccessorGeneratorMethodProxy
{
    public AccessorGeneratorObjectFactory(final AccessorInfo info) {
        super(info, true);
        if (!info.isStatic()) {
            throw new InvalidInjectionException(info.getMixin(), String.format("%s is invalid. Factory method must be static.", this.info));
        }
    }
    
    @Override
    public MethodNode generate() {
        final int returnSize = this.returnType.getSize();
        final int size = Bytecode.getArgsSize(this.argTypes) + returnSize * 2;
        final MethodNode method = this.createMethod(size, size);
        final String className = this.info.getClassNode().name;
        method.instructions.add(new TypeInsnNode(187, className));
        method.instructions.add(new InsnNode((returnSize == 1) ? 89 : 92));
        Bytecode.loadArgs(this.argTypes, method.instructions, 0);
        method.instructions.add(new MethodInsnNode(183, className, "<init>", this.targetMethod.desc, false));
        method.instructions.add(new InsnNode(176));
        return method;
    }
}
