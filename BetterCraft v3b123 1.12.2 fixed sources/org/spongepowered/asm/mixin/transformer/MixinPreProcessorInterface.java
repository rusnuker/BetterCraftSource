// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidInterfaceMixinException;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.util.Bytecode;

class MixinPreProcessorInterface extends MixinPreProcessorStandard
{
    MixinPreProcessorInterface(final MixinInfo mixin, final MixinInfo.MixinClassNode classNode) {
        super(mixin, classNode);
    }
    
    @Override
    protected void prepareMethod(final MixinInfo.MixinMethodNode mixinMethod, final ClassInfo.Method method) {
        if (!Bytecode.hasFlag(mixinMethod, 1)) {
            if (!Bytecode.hasFlag(mixinMethod, 4096)) {
                throw new InvalidInterfaceMixinException(this.mixin, String.format("Interface mixin contains a non-public method! Found %s in %s", method, this.mixin));
            }
            final MixinEnvironment.CompatibilityLevel requiredLevel = MixinEnvironment.CompatibilityLevel.requiredFor(2);
            if (MixinEnvironment.getCompatibilityLevel().isLessThan(requiredLevel)) {
                throw new InvalidInterfaceMixinException(this.mixin, String.format("Interface mixin contains a synthetic private method but compatibility level %s is required! Found %s in %s", requiredLevel, method, this.mixin));
            }
        }
        super.prepareMethod(mixinMethod, method);
    }
    
    @Override
    protected boolean validateField(final MixinTargetContext context, final FieldNode field, final AnnotationNode shadow) {
        if (!Bytecode.isStatic(field)) {
            throw new InvalidInterfaceMixinException(this.mixin, String.format("Interface mixin contains an instance field! Found %s in %s", field.name, this.mixin));
        }
        return super.validateField(context, field, shadow);
    }
}
