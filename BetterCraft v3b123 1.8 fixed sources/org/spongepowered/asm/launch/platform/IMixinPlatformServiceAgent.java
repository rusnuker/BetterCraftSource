// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import org.spongepowered.asm.util.IConsumer;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import java.util.Collection;

public interface IMixinPlatformServiceAgent extends IMixinPlatformAgent
{
    void init();
    
    String getSideName();
    
    Collection<IContainerHandle> getMixinContainers();
    
    @Deprecated
    void wire(final MixinEnvironment.Phase p0, final IConsumer<MixinEnvironment.Phase> p1);
    
    @Deprecated
    void unwire();
}
