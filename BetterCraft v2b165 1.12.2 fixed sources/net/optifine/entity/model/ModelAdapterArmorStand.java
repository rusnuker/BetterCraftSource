// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelAdapterArmorStand extends ModelAdapter
{
    public ModelAdapterArmorStand() {
        super(EntityArmorStand.class, "armor_stand", 0.7f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelArmorStand();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelArmorStand)) {
            return null;
        }
        final ModelArmorStand modelarmorstand = (ModelArmorStand)model;
        if (modelPart.equals("right")) {
            return modelarmorstand.standRightSide;
        }
        if (modelPart.equals("left")) {
            return modelarmorstand.standLeftSide;
        }
        if (modelPart.equals("waist")) {
            return modelarmorstand.standWaist;
        }
        return modelPart.equals("base") ? modelarmorstand.standBase : null;
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderArmorStand renderarmorstand = new RenderArmorStand(rendermanager);
        renderarmorstand.mainModel = modelBase;
        renderarmorstand.shadowSize = shadowSize;
        return renderarmorstand;
    }
}
