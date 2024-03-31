// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerLlamaDecor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLlama;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityLlama;

public class RenderLlama extends RenderLiving<EntityLlama>
{
    private static final ResourceLocation[] field_191350_a;
    
    static {
        field_191350_a = new ResourceLocation[] { new ResourceLocation("textures/entity/llama/llama_creamy.png"), new ResourceLocation("textures/entity/llama/llama_white.png"), new ResourceLocation("textures/entity/llama/llama_brown.png"), new ResourceLocation("textures/entity/llama/llama_gray.png") };
    }
    
    public RenderLlama(final RenderManager p_i47203_1_) {
        super(p_i47203_1_, new ModelLlama(0.0f), 0.7f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerLlamaDecor(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityLlama entity) {
        return RenderLlama.field_191350_a[entity.func_190719_dM()];
    }
}
