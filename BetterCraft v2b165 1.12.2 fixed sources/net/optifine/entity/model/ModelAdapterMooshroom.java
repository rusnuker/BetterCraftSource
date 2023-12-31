// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityMooshroom;

public class ModelAdapterMooshroom extends ModelAdapterQuadruped
{
    public ModelAdapterMooshroom() {
        super(EntityMooshroom.class, "mooshroom", 0.7f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelCow();
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderMooshroom rendermooshroom = new RenderMooshroom(rendermanager);
        rendermooshroom.mainModel = modelBase;
        rendermooshroom.shadowSize = shadowSize;
        return rendermooshroom;
    }
}
