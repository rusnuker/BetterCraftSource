// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.service;

import java.util.Collection;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import java.util.Set;

public interface IObfuscationService
{
    Set<String> getSupportedOptions();
    
    Collection<ObfuscationTypeDescriptor> getObfuscationTypes(final IMixinAnnotationProcessor p0);
}
