// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin;

import java.util.HashSet;
import org.spongepowered.asm.service.MixinService;
import java.util.Collections;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.LinkedHashSet;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.Config;
import java.util.Set;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.logging.ILogger;

public final class Mixins
{
    private static final ILogger logger;
    private static final GlobalProperties.Keys CONFIGS_KEY;
    private static final Set<String> errorHandlers;
    private static final Set<String> registeredConfigs;
    
    private Mixins() {
    }
    
    public static void addConfigurations(final String... configFiles) {
        final MixinEnvironment fallback = MixinEnvironment.getDefaultEnvironment();
        for (final String configFile : configFiles) {
            createConfiguration(configFile, fallback);
        }
    }
    
    public static void addConfiguration(final String configFile) {
        createConfiguration(configFile, MixinEnvironment.getDefaultEnvironment());
    }
    
    @Deprecated
    static void addConfiguration(final String configFile, final MixinEnvironment fallback) {
        createConfiguration(configFile, fallback);
    }
    
    private static void createConfiguration(final String configFile, final MixinEnvironment fallback) {
        Config config = null;
        try {
            config = Config.create(configFile, fallback);
        }
        catch (final Exception ex) {
            Mixins.logger.error("Error encountered reading mixin config " + configFile + ": " + ex.getClass().getName() + " " + ex.getMessage(), ex);
        }
        registerConfiguration(config);
    }
    
    private static void registerConfiguration(final Config config) {
        if (config == null || Mixins.registeredConfigs.contains(config.getName())) {
            return;
        }
        final MixinEnvironment env = config.getEnvironment();
        if (env != null) {
            env.registerConfig(config.getName());
        }
        getConfigs().add(config);
        Mixins.registeredConfigs.add(config.getName());
        final Config parent = config.getParent();
        if (parent != null) {
            registerConfiguration(parent);
        }
    }
    
    public static int getUnvisitedCount() {
        int count = 0;
        for (final Config config : getConfigs()) {
            if (!config.isVisited()) {
                ++count;
            }
        }
        return count;
    }
    
    public static Set<Config> getConfigs() {
        Set<Config> mixinConfigs = GlobalProperties.get(Mixins.CONFIGS_KEY);
        if (mixinConfigs == null) {
            mixinConfigs = new LinkedHashSet<Config>();
            GlobalProperties.put(Mixins.CONFIGS_KEY, mixinConfigs);
        }
        return mixinConfigs;
    }
    
    public static Set<IMixinInfo> getMixinsForClass(final String className) {
        final ClassInfo classInfo = ClassInfo.fromCache(className);
        if (classInfo != null) {
            return classInfo.getAppliedMixins();
        }
        return Collections.emptySet();
    }
    
    public static void registerErrorHandlerClass(final String handlerName) {
        if (handlerName != null) {
            Mixins.errorHandlers.add(handlerName);
        }
    }
    
    public static Set<String> getErrorHandlerClasses() {
        return Collections.unmodifiableSet((Set<? extends String>)Mixins.errorHandlers);
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
        CONFIGS_KEY = GlobalProperties.Keys.of(GlobalProperties.Keys.CONFIGS + ".queue");
        errorHandlers = new LinkedHashSet<String>();
        registeredConfigs = new HashSet<String>();
    }
}
