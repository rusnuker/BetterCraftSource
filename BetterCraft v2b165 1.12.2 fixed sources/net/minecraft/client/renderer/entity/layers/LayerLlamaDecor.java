// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityLlama;

public class LayerLlamaDecor implements LayerRenderer<EntityLlama>
{
    private static final ResourceLocation[] field_191364_a;
    private final RenderLlama field_191365_b;
    private final ModelLlama field_191366_c;
    
    static {
        field_191364_a = new ResourceLocation[] { new ResourceLocation("textures/entity/llama/decor/decor_white.png"), new ResourceLocation("textures/entity/llama/decor/decor_orange.png"), new ResourceLocation("textures/entity/llama/decor/decor_magenta.png"), new ResourceLocation("textures/entity/llama/decor/decor_light_blue.png"), new ResourceLocation("textures/entity/llama/decor/decor_yellow.png"), new ResourceLocation("textures/entity/llama/decor/decor_lime.png"), new ResourceLocation("textures/entity/llama/decor/decor_pink.png"), new ResourceLocation("textures/entity/llama/decor/decor_gray.png"), new ResourceLocation("textures/entity/llama/decor/decor_silver.png"), new ResourceLocation("textures/entity/llama/decor/decor_cyan.png"), new ResourceLocation("textures/entity/llama/decor/decor_purple.png"), new ResourceLocation("textures/entity/llama/decor/decor_blue.png"), new ResourceLocation("textures/entity/llama/decor/decor_brown.png"), new ResourceLocation("textures/entity/llama/decor/decor_green.png"), new ResourceLocation("textures/entity/llama/decor/decor_red.png"), new ResourceLocation("textures/entity/llama/decor/decor_black.png") };
    }
    
    public LayerLlamaDecor(final RenderLlama p_i47184_1_) {
        this.field_191366_c = new ModelLlama(0.5f);
        this.field_191365_b = p_i47184_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityLlama entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (entitylivingbaseIn.func_190717_dN()) {
            this.field_191365_b.bindTexture(LayerLlamaDecor.field_191364_a[entitylivingbaseIn.func_190704_dO().getMetadata()]);
            this.field_191366_c.setModelAttributes(this.field_191365_b.getMainModel());
            this.field_191366_c.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
