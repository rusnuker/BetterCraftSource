// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import java.util.EnumSet;

public final class Phases
{
    public static final EnumSet<ILaunchPluginService.Phase> NONE;
    public static final EnumSet<ILaunchPluginService.Phase> BEFORE_ONLY;
    public static final EnumSet<ILaunchPluginService.Phase> AFTER_ONLY;
    
    private Phases() {
    }
    
    static {
        NONE = EnumSet.noneOf(ILaunchPluginService.Phase.class);
        BEFORE_ONLY = EnumSet.of(ILaunchPluginService.Phase.BEFORE);
        AFTER_ONLY = EnumSet.of(ILaunchPluginService.Phase.AFTER);
    }
}
