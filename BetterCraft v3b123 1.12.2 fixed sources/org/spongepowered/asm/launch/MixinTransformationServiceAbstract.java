// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import com.google.common.collect.ImmutableList;
import cpw.mods.modlauncher.api.ITransformer;
import java.util.Collections;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import java.util.Set;
import cpw.mods.modlauncher.api.IEnvironment;
import java.util.Collection;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;
import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.List;
import joptsimple.ArgumentAcceptingOptionSpec;
import cpw.mods.modlauncher.api.ITransformationService;

public abstract class MixinTransformationServiceAbstract implements ITransformationService
{
    private ArgumentAcceptingOptionSpec<String> mixinsArgument;
    private List<String> commandLineMixins;
    private MixinLaunchPluginLegacy plugin;
    
    public MixinTransformationServiceAbstract() {
        this.commandLineMixins = new ArrayList<String>();
    }
    
    public String name() {
        return "mixin";
    }
    
    public void arguments(final BiFunction<String, String, OptionSpecBuilder> argumentBuilder) {
        this.mixinsArgument = argumentBuilder.apply("config", "a mixin config to load").withRequiredArg().ofType(String.class);
    }
    
    public void argumentValues(final ITransformationService.OptionResult option) {
        this.commandLineMixins.addAll(option.values((OptionSpec)this.mixinsArgument));
    }
    
    public void onLoad(final IEnvironment environment, final Set<String> otherServices) throws IncompatibleEnvironmentException {
    }
    
    public void initialize(final IEnvironment environment) {
        final Optional<ILaunchPluginService> plugin = environment.findLaunchPlugin("mixin");
        if (!plugin.isPresent()) {
            throw new MixinInitialisationError("Mixin Launch Plugin Service could not be located");
        }
        final ILaunchPluginService launchPlugin = plugin.get();
        if (!(launchPlugin instanceof MixinLaunchPluginLegacy)) {
            throw new MixinInitialisationError("Mixin Launch Plugin Service is present but not compatible");
        }
        this.plugin = (MixinLaunchPluginLegacy)launchPlugin;
        MixinBootstrap.start();
        this.plugin.init(environment, this.commandLineMixins);
    }
    
    public List<Map.Entry<String, Path>> runScan(final IEnvironment environment) {
        return Collections.emptyList();
    }
    
    public List<ITransformer> transformers() {
        return (List<ITransformer>)ImmutableList.of();
    }
}
