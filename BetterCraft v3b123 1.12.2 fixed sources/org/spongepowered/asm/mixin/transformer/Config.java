// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.HashMap;
import org.spongepowered.asm.service.MixinService;
import com.google.common.base.Strings;
import org.spongepowered.asm.launch.MixinInitialisationError;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import java.util.Map;
import org.spongepowered.asm.logging.ILogger;

public class Config
{
    private static final ILogger logger;
    private static final Map<String, Config> allConfigs;
    private final String name;
    private final MixinConfig config;
    
    public Config(final MixinConfig config) {
        this.name = config.getName();
        this.config = config;
    }
    
    public String getName() {
        return this.name;
    }
    
    MixinConfig get() {
        return this.config;
    }
    
    public boolean isVisited() {
        return this.config.isVisited();
    }
    
    public IMixinConfig getConfig() {
        return this.config;
    }
    
    public MixinEnvironment getEnvironment() {
        return this.config.getEnvironment();
    }
    
    public Config getParent() {
        final MixinConfig parent = this.config.getParent();
        return (parent != null) ? parent.getHandle() : null;
    }
    
    @Override
    public String toString() {
        return this.config.toString();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Config && this.name.equals(((Config)obj).name);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    @Deprecated
    public static Config create(final String configFile, final MixinEnvironment outer) {
        Config config = Config.allConfigs.get(configFile);
        if (config != null) {
            return config;
        }
        try {
            config = MixinConfig.create(configFile, outer);
            if (config != null) {
                Config.allConfigs.put(config.getName(), config);
            }
        }
        catch (final Exception ex) {
            throw new MixinInitialisationError("Error initialising mixin config " + configFile, ex);
        }
        if (config == null) {
            return null;
        }
        final String parent = config.get().getParentName();
        if (!Strings.isNullOrEmpty(parent)) {
            Config parentConfig;
            try {
                parentConfig = create(parent, outer);
                if (parentConfig != null && !config.get().assignParent(parentConfig)) {
                    config = null;
                }
            }
            catch (final Throwable th) {
                throw new MixinInitialisationError("Error initialising parent mixin config " + parent + " of " + configFile, th);
            }
            if (parentConfig == null) {
                Config.logger.error("Error encountered initialising mixin config {0}: The parent {1} could not be read.", configFile, parent);
            }
        }
        return config;
    }
    
    public static Config create(final String configFile) {
        return MixinConfig.create(configFile, MixinEnvironment.getDefaultEnvironment());
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
        allConfigs = new HashMap<String, Config>();
    }
}
