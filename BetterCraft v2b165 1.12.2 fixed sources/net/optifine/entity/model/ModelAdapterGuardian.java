// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderGuardian;
import net.minecraft.client.Minecraft;
import optifine.Config;
import optifine.Reflector;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.monster.EntityGuardian;

public class ModelAdapterGuardian extends ModelAdapter
{
    public ModelAdapterGuardian() {
        super(EntityGuardian.class, "guardian", 0.5f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelGuardian();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelGuardian)) {
            return null;
        }
        final ModelGuardian modelguardian = (ModelGuardian)model;
        if (modelPart.equals("body")) {
            return (ModelRenderer)Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_body);
        }
        if (modelPart.equals("eye")) {
            return (ModelRenderer)Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_eye);
        }
        final String s = "spine";
        if (modelPart.startsWith(s)) {
            final ModelRenderer[] amodelrenderer1 = (ModelRenderer[])Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_spines);
            if (amodelrenderer1 == null) {
                return null;
            }
            final String s2 = modelPart.substring(s.length());
            int j = Config.parseInt(s2, -1);
            return (--j >= 0 && j < amodelrenderer1.length) ? amodelrenderer1[j] : null;
        }
        else {
            final String s3 = "tail";
            if (!modelPart.startsWith(s3)) {
                return null;
            }
            final ModelRenderer[] amodelrenderer2 = (ModelRenderer[])Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_tail);
            if (amodelrenderer2 == null) {
                return null;
            }
            final String s4 = modelPart.substring(s3.length());
            int i = Config.parseInt(s4, -1);
            return (--i >= 0 && i < amodelrenderer2.length) ? amodelrenderer2[i] : null;
        }
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        final RenderGuardian renderguardian = new RenderGuardian(rendermanager);
        renderguardian.mainModel = modelBase;
        renderguardian.shadowSize = shadowSize;
        return renderguardian;
    }
}
