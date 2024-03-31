// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import java.util.Collection;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorRemappable;

public interface IObfuscationEnvironment
{
    MappingMethod getObfMethod(final ITargetSelectorRemappable p0);
    
    MappingMethod getObfMethod(final MappingMethod p0);
    
    MappingMethod getObfMethod(final MappingMethod p0, final boolean p1);
    
    MappingField getObfField(final ITargetSelectorRemappable p0);
    
    MappingField getObfField(final MappingField p0);
    
    MappingField getObfField(final MappingField p0, final boolean p1);
    
    String getObfClass(final String p0);
    
    ITargetSelectorRemappable remapDescriptor(final ITargetSelectorRemappable p0);
    
    String remapDescriptor(final String p0);
    
    void writeMappings(final Collection<IMappingConsumer> p0);
}
