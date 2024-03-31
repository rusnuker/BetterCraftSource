// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.modlauncher;

import org.spongepowered.asm.service.ServiceInitialisationException;
import cpw.mods.modlauncher.Launcher;
import org.spongepowered.asm.service.IMixinServiceBootstrap;

public class MixinServiceModLauncherBootstrap implements IMixinServiceBootstrap
{
    @Override
    public String getName() {
        return "ModLauncher";
    }
    
    @Override
    public String getServiceClassName() {
        return "org.spongepowered.asm.service.modlauncher.MixinServiceModLauncher";
    }
    
    @Override
    public void bootstrap() {
        try {
            Launcher.INSTANCE.hashCode();
        }
        catch (final Throwable th) {
            throw new ServiceInitialisationException(this.getName() + " is not available");
        }
    }
}
