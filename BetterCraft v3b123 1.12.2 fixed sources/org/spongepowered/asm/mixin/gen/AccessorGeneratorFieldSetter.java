// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.gen;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.util.Bytecode;

public class AccessorGeneratorFieldSetter extends AccessorGeneratorField
{
    private boolean mutable;
    
    public AccessorGeneratorFieldSetter(final AccessorInfo info) {
        super(info);
    }
    
    @Override
    public void validate() {
        super.validate();
        final ClassInfo.Method method = this.info.getClassInfo().findMethod(this.info.getMethod());
        this.mutable = method.isDecoratedMutable();
        if (this.mutable || !Bytecode.hasFlag(this.targetField, 16)) {
            return;
        }
        if (this.info.getMixin().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            MixinService.getService().getLogger("mixin").warn("{} for final field {}::{} is not @Mutable", this.info, ((MixinTargetContext)this.info.getMixin()).getTarget(), this.targetField.name);
        }
    }
    
    @Override
    public MethodNode generate() {
        if (this.mutable) {
            final FieldNode targetField = this.targetField;
            targetField.access &= 0xFFFFFFEF;
        }
        final int stackSpace = this.targetIsStatic ? 0 : 1;
        final int maxLocals = stackSpace + this.targetType.getSize();
        final int maxStack = stackSpace + this.targetType.getSize();
        final MethodNode method = this.createMethod(maxLocals, maxStack);
        if (!this.targetIsStatic) {
            method.instructions.add(new VarInsnNode(25, 0));
        }
        method.instructions.add(new VarInsnNode(this.targetType.getOpcode(21), stackSpace));
        final int opcode = this.targetIsStatic ? 179 : 181;
        method.instructions.add(new FieldInsnNode(opcode, this.info.getClassNode().name, this.targetField.name, this.targetField.desc));
        method.instructions.add(new InsnNode(177));
        return method;
    }
}
