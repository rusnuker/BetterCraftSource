// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import java.util.Collection;
import java.lang.reflect.Method;
import java.util.Optional;
import cpw.mods.modlauncher.Environment;
import cpw.mods.modlauncher.api.ILaunchHandlerService;
import java.util.Locale;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.TypesafeMap;
import cpw.mods.modlauncher.Launcher;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;

public class MixinPlatformAgentMinecraftForge extends MixinPlatformAgentAbstract implements IMixinPlatformServiceAgent
{
    private static final String GET_DIST_METHOD = "getDist";
    
    @Override
    public void init() {
    }
    
    @Override
    public IMixinPlatformAgent.AcceptResult accept(final MixinPlatformManager manager, final IContainerHandle handle) {
        return IMixinPlatformAgent.AcceptResult.REJECTED;
    }
    
    @Override
    public String getSideName() {
        final Environment environment = Launcher.INSTANCE.environment();
        final String launchTarget = environment.getProperty((TypesafeMap.Key)IEnvironment.Keys.LAUNCHTARGET.get()).orElse("missing").toLowerCase(Locale.ROOT);
        if (launchTarget.contains("server")) {
            return "SERVER";
        }
        if (launchTarget.contains("client")) {
            return "CLIENT";
        }
        final Optional<ILaunchHandlerService> launchHandler = environment.findLaunchHandler(launchTarget);
        if (launchHandler.isPresent()) {
            final ILaunchHandlerService service = launchHandler.get();
            try {
                final Method mdGetDist = service.getClass().getDeclaredMethod("getDist", (Class<?>[])new Class[0]);
                final String strDist = mdGetDist.invoke(service, new Object[0]).toString().toLowerCase(Locale.ROOT);
                if (strDist.contains("server")) {
                    return "SERVER";
                }
                if (strDist.contains("client")) {
                    return "CLIENT";
                }
            }
            catch (final Exception ex) {
                return null;
            }
        }
        return null;
    }
    
    @Override
    public Collection<IContainerHandle> getMixinContainers() {
        return null;
    }
}
