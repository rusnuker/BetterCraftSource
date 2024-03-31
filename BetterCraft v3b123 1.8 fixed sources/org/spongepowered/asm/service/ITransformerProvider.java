// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

import java.util.Collection;

public interface ITransformerProvider
{
    Collection<ITransformer> getTransformers();
    
    Collection<ITransformer> getDelegatedTransformers();
    
    void addTransformerExclusion(final String p0);
}
