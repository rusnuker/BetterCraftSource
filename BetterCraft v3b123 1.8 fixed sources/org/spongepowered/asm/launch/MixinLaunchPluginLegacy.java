// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import org.objectweb.asm.ClassReader;
import java.net.URL;
import org.objectweb.asm.ClassVisitor;
import org.spongepowered.asm.transformers.MixinClassReader;
import com.google.common.io.Resources;
import java.io.IOException;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;
import cpw.mods.modlauncher.api.IEnvironment;
import java.util.Iterator;
import java.util.Collection;
import org.objectweb.asm.tree.ClassNode;
import java.util.EnumSet;
import org.objectweb.asm.Type;
import java.util.ArrayList;
import org.spongepowered.asm.service.modlauncher.ModLauncherAuditTrail;
import org.spongepowered.asm.service.modlauncher.MixinServiceModLauncher;
import java.util.List;
import org.spongepowered.asm.service.IClassBytecodeProvider;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;

public class MixinLaunchPluginLegacy implements ILaunchPluginService, IClassBytecodeProvider
{
    public static final String NAME = "mixin";
    private final List<IClassProcessor> processors;
    private List<String> commandLineMixins;
    private ILaunchPluginService.ITransformerLoader transformerLoader;
    private MixinServiceModLauncher service;
    private ModLauncherAuditTrail auditTrail;
    
    public MixinLaunchPluginLegacy() {
        this.processors = new ArrayList<IClassProcessor>();
    }
    
    public String name() {
        return "mixin";
    }
    
    public EnumSet<ILaunchPluginService.Phase> handlesClass(final Type classType, final boolean isEmpty) {
        throw new IllegalStateException("Outdated ModLauncher");
    }
    
    public boolean processClass(final ILaunchPluginService.Phase phase, final ClassNode classNode, final Type classType) {
        throw new IllegalStateException("Outdated ModLauncher");
    }
    
    public EnumSet<ILaunchPluginService.Phase> handlesClass(final Type classType, final boolean isEmpty, final String reason) {
        if ("mixin".equals(reason)) {
            return Phases.NONE;
        }
        final EnumSet<ILaunchPluginService.Phase> phases = EnumSet.noneOf(ILaunchPluginService.Phase.class);
        synchronized (this.processors) {
            for (final IClassProcessor postProcessor : this.processors) {
                final EnumSet<ILaunchPluginService.Phase> processorVote = postProcessor.handlesClass(classType, isEmpty, reason);
                if (processorVote != null) {
                    phases.addAll((Collection<?>)processorVote);
                }
            }
        }
        return phases;
    }
    
    public boolean processClass(final ILaunchPluginService.Phase phase, final ClassNode classNode, final Type classType, final String reason) {
        boolean processed = false;
        synchronized (this.processors) {
            for (final IClassProcessor processor : this.processors) {
                processed |= processor.processClass(phase, classNode, classType, reason);
            }
        }
        return processed;
    }
    
    void init(final IEnvironment environment, final List<String> commandLineMixins) {
        final IMixinService service = MixinService.getService();
        if (!(service instanceof MixinServiceModLauncher)) {
            throw new IllegalStateException("Unsupported service type for ModLauncher Mixin Service");
        }
        this.service = (MixinServiceModLauncher)service;
        this.auditTrail = (ModLauncherAuditTrail)this.service.getAuditTrail();
        synchronized (this.processors) {
            this.processors.addAll(this.service.getProcessors());
        }
        this.commandLineMixins = commandLineMixins;
        this.service.onInit(this);
    }
    
    public void customAuditConsumer(final String className, final Consumer<String[]> auditDataAcceptor) {
        if (this.auditTrail != null) {
            this.auditTrail.setConsumer(className, auditDataAcceptor);
        }
    }
    
    @Deprecated
    public void addResource(final Path resource, final String name) {
        this.service.getPrimaryContainer().addResource(name, resource);
    }
    
    public void offerResource(final Path resource, final String name) {
        this.service.getPrimaryContainer().addResource(name, resource);
    }
    
    public void addResources(final List resources) {
        this.service.getPrimaryContainer().addResources(resources);
    }
    
    public <T> T getExtension() {
        return null;
    }
    
    public void initializeLaunch(final ILaunchPluginService.ITransformerLoader transformerLoader, final Path[] specialPaths) {
        this.initializeLaunch(transformerLoader);
    }
    
    protected void initializeLaunch(final ILaunchPluginService.ITransformerLoader transformerLoader) {
        this.transformerLoader = transformerLoader;
        MixinBootstrap.doInit(CommandLineOptions.of(this.commandLineMixins));
        MixinBootstrap.inject();
        this.service.onStartup();
    }
    
    public ClassNode getClassNode(final String name) throws ClassNotFoundException, IOException {
        return this.getClassNode(name, true);
    }
    
    public ClassNode getClassNode(final String name, final boolean runTransformers) throws ClassNotFoundException, IOException {
        if (!runTransformers) {
            throw new IllegalArgumentException("ModLauncher service does not currently support retrieval of untransformed bytecode");
        }
        final String canonicalName = name.replace('/', '.');
        final String internalName = name.replace('.', '/');
        byte[] classBytes;
        try {
            classBytes = this.transformerLoader.buildTransformedClassNodeFor(canonicalName);
        }
        catch (final ClassNotFoundException ex) {
            final URL url = Thread.currentThread().getContextClassLoader().getResource(internalName + ".class");
            if (url == null) {
                throw ex;
            }
            try {
                classBytes = Resources.asByteSource(url).read();
            }
            catch (final IOException ioex) {
                throw ex;
            }
        }
        if (classBytes != null && classBytes.length != 0) {
            final ClassNode classNode = new ClassNode();
            final ClassReader classReader = new MixinClassReader(classBytes, canonicalName);
            classReader.accept(classNode, 8);
            return classNode;
        }
        final Type classType = Type.getObjectType(internalName);
        synchronized (this.processors) {
            for (final IClassProcessor processor : this.processors) {
                if (!processor.generatesClass(classType)) {
                    continue;
                }
                final ClassNode classNode2 = new ClassNode();
                if (processor.generateClass(classType, classNode2)) {
                    return classNode2;
                }
            }
        }
        throw new ClassNotFoundException(canonicalName);
    }
}
