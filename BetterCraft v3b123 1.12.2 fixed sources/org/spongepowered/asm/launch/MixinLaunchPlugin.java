// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import cpw.mods.modlauncher.api.NamedPath;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;

public class MixinLaunchPlugin extends MixinLaunchPluginLegacy
{
    public void initializeLaunch(final ILaunchPluginService.ITransformerLoader transformerLoader, final NamedPath[] specialPaths) {
        this.initializeLaunch(transformerLoader);
    }
}
