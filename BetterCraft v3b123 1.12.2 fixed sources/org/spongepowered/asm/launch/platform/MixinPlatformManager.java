// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import org.spongepowered.asm.mixin.throwables.MixinError;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import org.spongepowered.asm.service.ServiceVersionError;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import org.spongepowered.asm.service.MixinService;
import java.util.LinkedHashMap;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import java.util.Map;
import org.spongepowered.asm.logging.ILogger;

public class MixinPlatformManager
{
    private static final String DEFAULT_MAIN_CLASS = "net.minecraft.client.main.Main";
    private static final ILogger logger;
    private final Map<IContainerHandle, MixinContainer> containers;
    private final MixinConnectorManager connectors;
    private MixinContainer primaryContainer;
    private boolean prepared;
    private boolean injected;
    
    public MixinPlatformManager() {
        this.containers = new LinkedHashMap<IContainerHandle, MixinContainer>();
        this.connectors = new MixinConnectorManager();
        this.prepared = false;
    }
    
    public void init() {
        MixinPlatformManager.logger.debug("Initialising Mixin Platform Manager", new Object[0]);
        final IContainerHandle primaryContainerHandle = MixinService.getService().getPrimaryContainer();
        this.primaryContainer = this.addContainer(primaryContainerHandle);
        this.scanForContainers();
    }
    
    public Collection<String> getPhaseProviderClasses() {
        final Collection<String> phaseProviders = this.primaryContainer.getPhaseProviders();
        if (phaseProviders != null) {
            return Collections.unmodifiableCollection((Collection<? extends String>)phaseProviders);
        }
        return (Collection<String>)Collections.emptyList();
    }
    
    public final MixinContainer addContainer(final IContainerHandle handle) {
        final MixinContainer existingContainer = this.containers.get(handle);
        if (existingContainer != null) {
            return existingContainer;
        }
        final MixinContainer container = this.createContainerFor(handle);
        this.containers.put(handle, container);
        this.addNestedContainers(handle);
        return container;
    }
    
    private MixinContainer createContainerFor(final IContainerHandle handle) {
        MixinPlatformManager.logger.debug("Adding mixin platform agents for container {}", handle);
        final MixinContainer container = new MixinContainer(this, handle);
        if (this.prepared) {
            container.prepare();
        }
        return container;
    }
    
    private void addNestedContainers(final IContainerHandle handle) {
        for (final IContainerHandle nested : handle.getNestedContainers()) {
            if (!this.containers.containsKey(nested)) {
                this.addContainer(nested);
            }
        }
    }
    
    public final void prepare(final CommandLineOptions args) {
        this.prepared = true;
        for (final MixinContainer container : this.containers.values()) {
            container.prepare();
        }
        for (final String config : args.getConfigs()) {
            this.addConfig(config);
        }
    }
    
    public final void inject() {
        if (this.injected) {
            return;
        }
        this.injected = true;
        if (this.primaryContainer != null) {
            this.primaryContainer.initPrimaryContainer();
        }
        this.scanForContainers();
        MixinPlatformManager.logger.debug("inject() running with {} agents", this.containers.size());
        for (final MixinContainer container : this.containers.values()) {
            try {
                container.inject();
            }
            catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
        this.connectors.inject();
    }
    
    private void scanForContainers() {
        Collection<IContainerHandle> mixinContainers = null;
        try {
            mixinContainers = MixinService.getService().getMixinContainers();
        }
        catch (final AbstractMethodError ame) {
            throw new ServiceVersionError("Mixin service is out of date");
        }
        final List<IContainerHandle> existingContainers = new ArrayList<IContainerHandle>(this.containers.keySet());
        for (final IContainerHandle existingContainer : existingContainers) {
            this.addNestedContainers(existingContainer);
        }
        for (final IContainerHandle handle : mixinContainers) {
            try {
                MixinPlatformManager.logger.debug("Adding agents for Mixin Container {}", handle);
                this.addContainer(handle);
            }
            catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }
    
    final void setCompatibilityLevel(final String level) {
        try {
            final MixinEnvironment.CompatibilityLevel value = MixinEnvironment.CompatibilityLevel.valueOf(level.toUpperCase(Locale.ROOT));
            MixinPlatformManager.logger.debug("Setting mixin compatibility level: {}", value);
            MixinEnvironment.setCompatibilityLevel(value);
        }
        catch (final IllegalArgumentException ex) {
            MixinPlatformManager.logger.warn("Invalid compatibility level specified: {}", level);
        }
    }
    
    final void addConfig(final String config) {
        if (config.endsWith(".json")) {
            MixinPlatformManager.logger.debug("Registering mixin config: {}", config);
            Mixins.addConfiguration(config);
        }
        else if (config.contains(".json@")) {
            throw new MixinError("Setting config phase via manifest is no longer supported: " + config + ". Specify target in config instead");
        }
    }
    
    final void addTokenProvider(final String provider) {
        if (provider.contains("@")) {
            final String[] parts = provider.split("@", 2);
            final MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(parts[1]);
            if (phase != null) {
                MixinPlatformManager.logger.debug("Registering token provider class: {}", parts[0]);
                MixinEnvironment.getEnvironment(phase).registerTokenProviderClass(parts[0]);
            }
            return;
        }
        MixinEnvironment.getDefaultEnvironment().registerTokenProviderClass(provider);
    }
    
    final void addConnector(final String connectorClass) {
        this.connectors.addConnector(connectorClass);
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
}
