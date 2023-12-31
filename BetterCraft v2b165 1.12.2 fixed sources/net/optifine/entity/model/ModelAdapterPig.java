// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityPig;

public class ModelAdapterPig extends ModelAdapterQuadruped
{
    public ModelAdapterPig() {
        super(EntityPig.class, "pig", 0.7f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelPig();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderPig renderpig = new RenderPig(rendermanager);
        renderpig.mainModel = modelBase;
        renderpig.shadowSize = shadowSize;
        return renderpig;
    }
}
