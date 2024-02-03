// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;

public class MixinTweaker implements ITweaker
{
    public MixinTweaker() {
        MixinBootstrap.start();
    }
    
    @Override
    public final void acceptOptions(final List<String> args, final File gameDir, final File assetsDir, final String profile) {
        MixinBootstrap.doInit(CommandLineOptions.ofArgs(args));
    }
    
    @Override
    public final void injectIntoClassLoader(final LaunchClassLoader classLoader) {
        MixinBootstrap.inject();
    }
    
    @Override
    public String getLaunchTarget() {
        return MixinBootstrap.getPlatform().getLaunchTarget();
    }
    
    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
