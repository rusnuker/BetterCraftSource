// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.IMapping;

public interface ITargetSelectorRemappable extends ITargetSelectorByName
{
    boolean isFullyQualified();
    
    boolean isField();
    
    boolean isConstructor();
    
    boolean isClassInitialiser();
    
    boolean isInitialiser();
    
    IMapping<?> asMapping();
    
    MappingMethod asMethodMapping();
    
    MappingField asFieldMapping();
    
    ITargetSelectorRemappable move(final String p0);
    
    ITargetSelectorRemappable transform(final String p0);
    
    ITargetSelectorRemappable remapUsing(final MappingMethod p0, final boolean p1);
}
