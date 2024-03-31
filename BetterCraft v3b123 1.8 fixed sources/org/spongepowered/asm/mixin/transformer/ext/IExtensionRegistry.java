// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.ext;

import org.spongepowered.asm.service.ISyntheticClassRegistry;
import java.util.List;

public interface IExtensionRegistry
{
    List<IExtension> getExtensions();
    
    List<IExtension> getActiveExtensions();
    
     <T extends IExtension> T getExtension(final Class<? extends IExtension> p0);
    
    ISyntheticClassRegistry getSyntheticClassRegistry();
}
