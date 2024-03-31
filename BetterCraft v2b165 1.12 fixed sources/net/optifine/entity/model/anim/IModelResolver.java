// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;

public interface IModelResolver
{
    ModelRenderer getModelRenderer(final String p0);
    
    ModelVariable getModelVariable(final String p0);
    
    IExpression getExpression(final String p0);
}
