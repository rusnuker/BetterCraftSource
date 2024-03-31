// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public interface ISyntheticClassInfo
{
    IMixinInfo getMixin();
    
    String getName();
    
    String getClassName();
    
    boolean isLoaded();
}
