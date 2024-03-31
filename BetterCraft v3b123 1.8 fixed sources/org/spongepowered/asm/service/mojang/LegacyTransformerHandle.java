// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.mojang;

import java.lang.annotation.Annotation;
import org.spongepowered.asm.service.IClassProvider;
import org.spongepowered.asm.service.MixinService;
import net.minecraft.launchwrapper.IClassTransformer;
import org.spongepowered.asm.service.ILegacyClassTransformer;

class LegacyTransformerHandle implements ILegacyClassTransformer
{
    private final IClassTransformer transformer;
    
    LegacyTransformerHandle(final IClassTransformer transformer) {
        this.transformer = transformer;
    }
    
    @Override
    public String getName() {
        return this.transformer.getClass().getName();
    }
    
    @Override
    public boolean isDelegationExcluded() {
        try {
            final IClassProvider classProvider = MixinService.getService().getClassProvider();
            final Class<? extends Annotation> clResource = (Class<? extends Annotation>)classProvider.findClass("javax.annotation.Resource");
            return this.transformer.getClass().getAnnotation(clResource) != null;
        }
        catch (final ClassNotFoundException ex) {
            return false;
        }
    }
    
    @Override
    public byte[] transformClassBytes(final String name, final String transformedName, final byte[] basicClass) {
        return this.transformer.transform(name, transformedName, basicClass);
    }
}
