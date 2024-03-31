// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityMob;

public class RenderEvoker extends RenderLiving<EntityMob>
{
    private static final ResourceLocation field_191338_a;
    
    static {
        field_191338_a = new ResourceLocation("textures/entity/illager/evoker.png");
    }
    
    public RenderEvoker(final RenderManager p_i47207_1_) {
        super(p_i47207_1_, new ModelIllager(0.0f, 0.0f, 64, 64), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this) {
            @Override
            public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
                if (((EntitySpellcasterIllager)entitylivingbaseIn).func_193082_dl()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            
            @Override
            protected void func_191361_a(final EnumHandSide p_191361_1_) {
                ((ModelIllager)this.livingEntityRenderer.getMainModel()).func_191216_a(p_191361_1_).postRender(0.0625f);
            }
        });
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityMob entity) {
        return RenderEvoker.field_191338_a;
    }
    
    @Override
    protected void preRenderCallback(final EntityMob entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }
}
