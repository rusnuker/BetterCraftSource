// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckInterfaces;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionClassExporter;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgsClassGenerator;
import org.spongepowered.asm.service.ISyntheticClassInfo;
import org.spongepowered.asm.util.IConsumer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.MixinEnvironment;

final class DefaultExtensions
{
    private DefaultExtensions() {
    }
    
    static void create(final MixinEnvironment environment, final Extensions extensions, final SyntheticClassRegistry registry, final MixinCoprocessorNestHost nestHostCoprocessor) {
        final IConsumer<ISyntheticClassInfo> registryDelegate = new IConsumer<ISyntheticClassInfo>() {
            @Override
            public void accept(final ISyntheticClassInfo item) {
                registry.registerSyntheticClass(item);
            }
        };
        extensions.add(new ArgsClassGenerator(registryDelegate));
        extensions.add(new InnerClassGenerator(registryDelegate, nestHostCoprocessor));
        extensions.add(new ExtensionClassExporter(environment));
        extensions.add(new ExtensionCheckClass());
        extensions.add(new ExtensionCheckInterfaces());
    }
}
