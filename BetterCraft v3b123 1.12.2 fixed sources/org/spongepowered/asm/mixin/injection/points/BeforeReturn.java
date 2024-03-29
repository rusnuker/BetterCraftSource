// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.points;

import java.util.ListIterator;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Collection;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("RETURN")
public class BeforeReturn extends InjectionPoint
{
    private final int ordinal;
    
    public BeforeReturn(final InjectionPointData data) {
        super(data);
        this.ordinal = data.getOrdinal();
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
        boolean found = false;
        final int returnOpcode = Type.getReturnType(desc).getOpcode(172);
        int ordinal = 0;
        for (final AbstractInsnNode insn : insns) {
            if (insn instanceof InsnNode && insn.getOpcode() == returnOpcode) {
                if (this.ordinal == -1 || this.ordinal == ordinal) {
                    nodes.add(insn);
                    found = true;
                }
                ++ordinal;
            }
        }
        return found;
    }
}
