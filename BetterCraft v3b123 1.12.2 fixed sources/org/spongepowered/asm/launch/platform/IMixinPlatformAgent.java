// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import org.spongepowered.asm.launch.platform.container.IContainerHandle;

public interface IMixinPlatformAgent
{
    AcceptResult accept(final MixinPlatformManager p0, final IContainerHandle p1);
    
    String getPhaseProvider();
    
    void prepare();
    
    void initPrimaryContainer();
    
    void inject();
    
    public enum AcceptResult
    {
        ACCEPTED, 
        REJECTED, 
        INVALID;
    }
}
