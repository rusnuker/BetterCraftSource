// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import java.lang.reflect.Constructor;
import org.spongepowered.asm.mixin.throwables.MixinError;
import java.util.ArrayList;
import java.util.Iterator;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.IMixinInternal;
import java.util.List;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.launch.platform.MixinPlatformManager;
import org.spongepowered.asm.logging.ILogger;

public abstract class MixinBootstrap
{
    public static final String VERSION = "0.8.5";
    private static final String MIXIN_TRANSFORMER_FACTORY_CLASS = "org.spongepowered.asm.mixin.transformer.MixinTransformer$Factory";
    private static boolean initialised;
    private static boolean initState;
    private static ILogger logger;
    private static MixinPlatformManager platform;
    
    private MixinBootstrap() {
    }
    
    @Deprecated
    public static void addProxy() {
        MixinService.getService().beginPhase();
    }
    
    public static MixinPlatformManager getPlatform() {
        if (MixinBootstrap.platform == null) {
            final Object globalPlatformManager = GlobalProperties.get(GlobalProperties.Keys.PLATFORM_MANAGER);
            if (globalPlatformManager instanceof MixinPlatformManager) {
                MixinBootstrap.platform = (MixinPlatformManager)globalPlatformManager;
            }
            else {
                MixinBootstrap.platform = new MixinPlatformManager();
                GlobalProperties.put(GlobalProperties.Keys.PLATFORM_MANAGER, MixinBootstrap.platform);
                MixinBootstrap.platform.init();
            }
        }
        return MixinBootstrap.platform;
    }
    
    public static void init() {
        if (!start()) {
            return;
        }
        doInit(CommandLineOptions.defaultArgs());
    }
    
    static boolean start() {
        if (!isSubsystemRegistered()) {
            registerSubsystem("0.8.5");
            offerInternals();
            if (!MixinBootstrap.initialised) {
                MixinBootstrap.initialised = true;
                final MixinEnvironment.Phase initialPhase = MixinService.getService().getInitialPhase();
                if (initialPhase == MixinEnvironment.Phase.DEFAULT) {
                    MixinBootstrap.logger.error("Initialising mixin subsystem after game pre-init phase! Some mixins may be skipped.", new Object[0]);
                    MixinEnvironment.init(initialPhase);
                    getPlatform().prepare(CommandLineOptions.defaultArgs());
                    MixinBootstrap.initState = false;
                }
                else {
                    MixinEnvironment.init(initialPhase);
                }
                MixinService.getService().beginPhase();
            }
            getPlatform();
            return true;
        }
        if (!checkSubsystemVersion()) {
            throw new MixinInitialisationError("Mixin subsystem version " + getActiveSubsystemVersion() + " was already initialised. Cannot bootstrap version " + "0.8.5");
        }
        return false;
    }
    
    @Deprecated
    static void doInit(final List<String> args) {
        doInit(CommandLineOptions.ofArgs(args));
    }
    
    static void doInit(final CommandLineOptions args) {
        if (MixinBootstrap.initialised) {
            getPlatform().getPhaseProviderClasses();
            if (MixinBootstrap.initState) {
                getPlatform().prepare(args);
                MixinService.getService().init();
            }
            return;
        }
        if (isSubsystemRegistered()) {
            MixinBootstrap.logger.warn("Multiple Mixin containers present, init suppressed for {}", "0.8.5");
            return;
        }
        throw new IllegalStateException("MixinBootstrap.doInit() called before MixinBootstrap.start()");
    }
    
    static void inject() {
        getPlatform().inject();
    }
    
    private static boolean isSubsystemRegistered() {
        return GlobalProperties.get(GlobalProperties.Keys.INIT) != null;
    }
    
    private static boolean checkSubsystemVersion() {
        return "0.8.5".equals(getActiveSubsystemVersion());
    }
    
    private static Object getActiveSubsystemVersion() {
        final Object version = GlobalProperties.get(GlobalProperties.Keys.INIT);
        return (version != null) ? version : "";
    }
    
    private static void registerSubsystem(final String version) {
        GlobalProperties.put(GlobalProperties.Keys.INIT, version);
    }
    
    private static void offerInternals() {
        final IMixinService service = MixinService.getService();
        try {
            for (final IMixinInternal internal : getInternals()) {
                service.offer(internal);
            }
        }
        catch (final AbstractMethodError ex) {
            ex.printStackTrace();
        }
    }
    
    private static List<IMixinInternal> getInternals() throws MixinError {
        final List<IMixinInternal> internals = new ArrayList<IMixinInternal>();
        try {
            final Class<IMixinInternal> clTransformerFactory = (Class<IMixinInternal>)Class.forName("org.spongepowered.asm.mixin.transformer.MixinTransformer$Factory");
            final Constructor<IMixinInternal> ctor = clTransformerFactory.getDeclaredConstructor((Class<?>[])new Class[0]);
            ctor.setAccessible(true);
            internals.add(ctor.newInstance(new Object[0]));
        }
        catch (final ReflectiveOperationException ex) {
            throw new MixinError(ex);
        }
        return internals;
    }
    
    static {
        MixinBootstrap.initialised = false;
        MixinBootstrap.initState = true;
        MixinService.boot();
        MixinService.getService().prepare();
        MixinBootstrap.logger = MixinService.getService().getLogger("mixin");
    }
}
