// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityMob;

public class RenderIllusionIllager extends RenderLiving<EntityMob>
{
    private static final ResourceLocation field_193121_a;
    
    static {
        field_193121_a = new ResourceLocation("textures/entity/illager/illusionist.png");
    }
    
    public RenderIllusionIllager(final RenderManager p_i47477_1_) {
        super(p_i47477_1_, new ModelIllager(0.0f, 0.0f, 64, 64), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this) {
            @Override
            public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
                if (((EntityIllusionIllager)entitylivingbaseIn).func_193082_dl() || ((EntityIllusionIllager)entitylivingbaseIn).func_193096_dj()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            
            @Override
            protected void func_191361_a(final EnumHandSide p_191361_1_) {
                ((ModelIllager)this.livingEntityRenderer.getMainModel()).func_191216_a(p_191361_1_).postRender(0.0625f);
            }
        });
        ((ModelIllager)this.getMainModel()).field_193775_b.showModel = true;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityMob entity) {
        return RenderIllusionIllager.field_193121_a;
    }
    
    @Override
    protected void preRenderCallback(final EntityMob entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
    @Override
    public void doRender(final EntityMob entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (entity.isInvisible()) {
            final Vec3d[] avec3d = ((EntityIllusionIllager)entity).func_193098_a(partialTicks);
            final float f = this.handleRotationFloat(entity, partialTicks);
            for (int i = 0; i < avec3d.length; ++i) {
                super.doRender(entity, x + avec3d[i].xCoord + MathHelper.cos(i + f * 0.5f) * 0.025, y + avec3d[i].yCoord + MathHelper.cos(i + f * 0.75f) * 0.0125, z + avec3d[i].zCoord + MathHelper.cos(i + f * 0.7f) * 0.025, entityYaw, partialTicks);
            }
        }
        else {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
    
    @Override
    public void renderName(final EntityMob entity, final double x, final double y, final double z) {
        super.renderName(entity, x, y, z);
    }
    
    @Override
    protected boolean func_193115_c(final EntityMob p_193115_1_) {
        return true;
    }
}
