// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.points;

import java.util.ListIterator;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Collection;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("TAIL")
public class BeforeFinalReturn extends InjectionPoint
{
    private final IMixinContext context;
    
    public BeforeFinalReturn(final InjectionPointData data) {
        super(data);
        this.context = data.getMixin();
    }
    
    @Override
    public boolean checkPriority(final int targetPriority, final int ownerPriority) {
        return true;
    }
    
    @Override
    public RestrictTargetLevel getTargetRestriction(final IInjectionPointContext context) {
        return RestrictTargetLevel.ALLOW_ALL;
    }
    
    @Override
    public boolean find(final String desc, final InsnList insns, final Collection<AbstractInsnNode> nodes) {
        AbstractInsnNode ret = null;
        final int returnOpcode = Type.getReturnType(desc).getOpcode(172);
        for (final AbstractInsnNode insn : insns) {
            if (insn instanceof InsnNode && insn.getOpcode() == returnOpcode) {
                ret = insn;
            }
        }
        if (ret == null) {
            throw new InvalidInjectionException(this.context, "TAIL could not locate a valid RETURN in the target method!");
        }
        nodes.add(ret);
        return true;
    }
}
