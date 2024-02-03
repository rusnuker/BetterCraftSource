// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.launch.MixinInitialisationError;
import org.spongepowered.asm.util.asm.ASM;
import java.util.List;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.transformer.ext.IExtensionRegistry;
import java.lang.reflect.Constructor;
import org.spongepowered.asm.service.ISyntheticClassRegistry;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.transformers.TreeTransformer;

final class MixinTransformer extends TreeTransformer implements IMixinTransformer
{
    private static final String MIXIN_AGENT_CLASS = "org.spongepowered.tools.agent.MixinAgent";
    private final SyntheticClassRegistry syntheticClassRegistry;
    private final Extensions extensions;
    private final IHotSwap hotSwapper;
    private final MixinCoprocessorNestHost nestHostCoprocessor;
    private final MixinProcessor processor;
    private final MixinClassGenerator generator;
    
    MixinTransformer() {
        final MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
        final Object globalMixinTransformer = environment.getActiveTransformer();
        if (globalMixinTransformer instanceof IMixinTransformer) {
            throw new MixinException("Terminating MixinTransformer instance " + this);
        }
        environment.setActiveTransformer(this);
        this.syntheticClassRegistry = new SyntheticClassRegistry();
        this.extensions = new Extensions(this.syntheticClassRegistry);
        this.hotSwapper = this.initHotSwapper(environment);
        this.nestHostCoprocessor = new MixinCoprocessorNestHost();
        this.processor = new MixinProcessor(environment, this.extensions, this.hotSwapper, this.nestHostCoprocessor);
        this.generator = new MixinClassGenerator(environment, this.extensions);
        DefaultExtensions.create(environment, this.extensions, this.syntheticClassRegistry, this.nestHostCoprocessor);
    }
    
    private IHotSwap initHotSwapper(final MixinEnvironment environment) {
        if (!environment.getOption(MixinEnvironment.Option.HOT_SWAP)) {
            return null;
        }
        try {
            MixinProcessor.logger.info("Attempting to load Hot-Swap agent", new Object[0]);
            final Class<? extends IHotSwap> clazz = (Class<? extends IHotSwap>)Class.forName("org.spongepowered.tools.agent.MixinAgent");
            final Constructor<? extends IHotSwap> ctor = clazz.getDeclaredConstructor(IMixinTransformer.class);
            return (IHotSwap)ctor.newInstance(this);
        }
        catch (final Throwable th) {
            MixinProcessor.logger.info("Hot-swap agent could not be loaded, hot swapping of mixins won't work. {}: {}", th.getClass().getSimpleName(), th.getMessage());
            return null;
        }
    }
    
    @Override
    public IExtensionRegistry getExtensions() {
        return this.extensions;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public boolean isDelegationExcluded() {
        return true;
    }
    
    @Override
    public void audit(final MixinEnvironment environment) {
        this.processor.audit(environment);
    }
    
    @Override
    public List<String> reload(final String mixinClass, final ClassNode classNode) {
        return this.processor.reload(mixinClass, classNode);
    }
    
    @Override
    public byte[] transformClassBytes(final String name, final String transformedName, final byte[] basicClass) {
        if (transformedName == null) {
            return basicClass;
        }
        final MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
        if (basicClass == null) {
            return this.generateClass(environment, transformedName);
        }
        return this.transformClass(environment, transformedName, basicClass);
    }
    
    @Override
    public boolean computeFramesForClass(final MixinEnvironment environment, final String name, final ClassNode classNode) {
        return false;
    }
    
    @Override
    public byte[] transformClass(final MixinEnvironment environment, final String name, final byte[] classBytes) {
        final ClassNode classNode = this.readClass(name, classBytes);
        if (this.processor.applyMixins(environment, name, classNode)) {
            return this.writeClass(classNode);
        }
        return classBytes;
    }
    
    @Override
    public boolean transformClass(final MixinEnvironment environment, final String name, final ClassNode classNode) {
        return this.processor.applyMixins(environment, name, classNode);
    }
    
    @Override
    public byte[] generateClass(final MixinEnvironment environment, final String name) {
        final ClassNode classNode = createEmptyClass(name);
        if (this.generator.generateClass(environment, name, classNode)) {
            return this.writeClass(classNode);
        }
        return null;
    }
    
    @Override
    public boolean generateClass(final MixinEnvironment environment, final String name, final ClassNode classNode) {
        return this.generator.generateClass(environment, name, classNode);
    }
    
    private static ClassNode createEmptyClass(final String name) {
        final ClassNode classNode = new ClassNode(ASM.API_VERSION);
        classNode.name = name.replace('.', '/');
        classNode.version = MixinEnvironment.getCompatibilityLevel().getClassVersion();
        classNode.superName = "java/lang/Object";
        return classNode;
    }
    
    static class Factory implements IMixinTransformerFactory
    {
        @Override
        public IMixinTransformer createTransformer() throws MixinInitialisationError {
            return new MixinTransformer();
        }
    }
}
