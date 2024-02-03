// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.points;

import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Collection;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("INVOKE_ASSIGN")
public class AfterInvoke extends BeforeInvoke
{
    public AfterInvoke(final InjectionPointData data) {
        super(data);
    }
    
    @Override
    protected boolean addInsn(final InsnList insns, final Collection<AbstractInsnNode> nodes, AbstractInsnNode insn) {
        final MethodInsnNode methodNode = (MethodInsnNode)insn;
        if (Type.getReturnType(methodNode.desc) == Type.VOID_TYPE) {
            return false;
        }
        insn = InjectionPoint.nextNode(insns, insn);
        if (insn instanceof VarInsnNode && insn.getOpcode() >= 54) {
            insn = InjectionPoint.nextNode(insns, insn);
        }
        nodes.add(insn);
        return true;
    }
}
