// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform.container;

import java.util.Collection;

public interface IContainerHandle
{
    String getAttribute(final String p0);
    
    Collection<IContainerHandle> getNestedContainers();
}
