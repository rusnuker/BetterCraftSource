// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.mojang;

import org.spongepowered.asm.service.ServiceInitialisationException;
import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.service.IMixinServiceBootstrap;

public class MixinServiceLaunchWrapperBootstrap implements IMixinServiceBootstrap
{
    private static final String SERVICE_PACKAGE = "org.spongepowered.asm.service.";
    private static final String LAUNCH_PACKAGE = "org.spongepowered.asm.launch.";
    private static final String LOGGING_PACKAGE = "org.spongepowered.asm.logging.";
    private static final String MIXIN_UTIL_PACKAGE = "org.spongepowered.asm.util.";
    private static final String LEGACY_ASM_PACKAGE = "org.spongepowered.asm.lib.";
    private static final String ASM_PACKAGE = "org.objectweb.asm.";
    private static final String MIXIN_PACKAGE = "org.spongepowered.asm.mixin.";
    
    @Override
    public String getName() {
        return "LaunchWrapper";
    }
    
    @Override
    public String getServiceClassName() {
        return "org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper";
    }
    
    @Override
    public void bootstrap() {
        try {
            Launch.classLoader.hashCode();
        }
        catch (final Throwable th) {
            throw new ServiceInitialisationException(this.getName() + " is not available");
        }
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.service.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.launch.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.logging.");
        Launch.classLoader.addClassLoaderExclusion("org.objectweb.asm.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.lib.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.mixin.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.util.");
    }
}
