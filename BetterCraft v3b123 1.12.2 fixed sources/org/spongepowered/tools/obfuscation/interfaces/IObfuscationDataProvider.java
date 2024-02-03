// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.tools.obfuscation.ObfuscationData;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorRemappable;

public interface IObfuscationDataProvider
{
     <T> ObfuscationData<T> getObfEntryRecursive(final ITargetSelectorRemappable p0);
    
     <T> ObfuscationData<T> getObfEntry(final ITargetSelectorRemappable p0);
    
     <T> ObfuscationData<T> getObfEntry(final IMapping<T> p0);
    
    ObfuscationData<MappingMethod> getObfMethodRecursive(final ITargetSelectorRemappable p0);
    
    ObfuscationData<MappingMethod> getObfMethod(final ITargetSelectorRemappable p0);
    
    ObfuscationData<MappingMethod> getRemappedMethod(final ITargetSelectorRemappable p0);
    
    ObfuscationData<MappingMethod> getObfMethod(final MappingMethod p0);
    
    ObfuscationData<MappingMethod> getRemappedMethod(final MappingMethod p0);
    
    ObfuscationData<MappingField> getObfFieldRecursive(final ITargetSelectorRemappable p0);
    
    ObfuscationData<MappingField> getObfField(final ITargetSelectorRemappable p0);
    
    ObfuscationData<MappingField> getObfField(final MappingField p0);
    
    ObfuscationData<String> getObfClass(final TypeHandle p0);
    
    ObfuscationData<String> getObfClass(final String p0);
}
