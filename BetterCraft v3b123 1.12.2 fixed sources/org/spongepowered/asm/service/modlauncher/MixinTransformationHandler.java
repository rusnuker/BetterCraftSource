// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.modlauncher;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.launch.Phases;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import java.util.EnumSet;
import org.objectweb.asm.Type;
import com.google.common.base.Preconditions;
import org.spongepowered.asm.service.ISyntheticClassRegistry;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.IMixinTransformerFactory;
import org.spongepowered.asm.launch.IClassProcessor;

public class MixinTransformationHandler implements IClassProcessor
{
    private IMixinTransformerFactory transformerFactory;
    private IMixinTransformer transformer;
    private ISyntheticClassRegistry registry;
    
    void offer(final IMixinTransformerFactory transformerFactory) {
        Preconditions.checkNotNull(transformerFactory, (Object)"transformerFactory");
        this.transformerFactory = transformerFactory;
    }
    
    @Override
    public EnumSet<ILaunchPluginService.Phase> handlesClass(final Type classType, final boolean isEmpty, final String reason) {
        if (!isEmpty) {
            return Phases.AFTER_ONLY;
        }
        if (this.registry == null) {
            return null;
        }
        return this.generatesClass(classType) ? Phases.AFTER_ONLY : null;
    }
    
    @Override
    public boolean generatesClass(final Type classType) {
        return this.registry.findSyntheticClass(classType.getClassName()) != null;
    }
    
    @Override
    public synchronized boolean processClass(final ILaunchPluginService.Phase phase, final ClassNode classNode, final Type classType, final String reason) {
        if (phase == ILaunchPluginService.Phase.BEFORE) {
            return false;
        }
        if (this.transformer == null) {
            if (this.transformerFactory == null) {
                throw new IllegalStateException("processClass called before transformer factory offered to transformation handler");
            }
            this.transformer = this.transformerFactory.createTransformer();
            this.registry = this.transformer.getExtensions().getSyntheticClassRegistry();
        }
        if ("mixin".equals(reason)) {
            return false;
        }
        if (this.generatesClass(classType)) {
            return this.generateClass(classType, classNode);
        }
        final MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
        if ("computing_frames".equals(reason)) {
            return this.transformer.computeFramesForClass(environment, classType.getClassName(), classNode);
        }
        return this.transformer.transformClass(environment, classType.getClassName(), classNode);
    }
    
    @Override
    public boolean generateClass(final Type classType, final ClassNode classNode) {
        return this.transformer.generateClass(MixinEnvironment.getCurrentEnvironment(), classType.getClassName(), classNode);
    }
}
