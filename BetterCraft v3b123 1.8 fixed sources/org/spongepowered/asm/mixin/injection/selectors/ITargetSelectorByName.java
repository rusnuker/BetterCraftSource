// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

public interface ITargetSelectorByName extends ITargetSelector
{
    String getOwner();
    
    String getName();
    
    String getDesc();
    
    String toDescriptor();
    
    MatchResult matches(final String p0, final String p1, final String p2);
}
