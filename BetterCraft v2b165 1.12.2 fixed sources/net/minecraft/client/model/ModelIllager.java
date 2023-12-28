// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.model;

import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.Entity;

public class ModelIllager extends ModelBase
{
    public ModelRenderer field_191217_a;
    public ModelRenderer field_193775_b;
    public ModelRenderer field_191218_b;
    public ModelRenderer field_191219_c;
    public ModelRenderer field_191220_d;
    public ModelRenderer field_191221_e;
    public ModelRenderer field_191222_f;
    public ModelRenderer field_191223_g;
    public ModelRenderer field_191224_h;
    
    public ModelIllager(final float p_i47227_1_, final float p_i47227_2_, final int p_i47227_3_, final int p_i47227_4_) {
        (this.field_191217_a = new ModelRenderer(this).setTextureSize(p_i47227_3_, p_i47227_4_)).setRotationPoint(0.0f, 0.0f + p_i47227_2_, 0.0f);
        this.field_191217_a.setTextureOffset(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, p_i47227_1_);
        (this.field_193775_b = new ModelRenderer(this, 32, 0).setTextureSize(p_i47227_3_, p_i47227_4_)).addBox(-4.0f, -10.0f, -4.0f, 8, 12, 8, p_i47227_1_ + 0.45f);
        this.field_191217_a.addChild(this.field_193775_b);
        this.field_193775_b.showModel = false;
        (this.field_191222_f = new ModelRenderer(this).setTextureSize(p_i47227_3_, p_i47227_4_)).setRotationPoint(0.0f, p_i47227_2_ - 2.0f, 0.0f);
        this.field_191222_f.setTextureOffset(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2, 4, 2, p_i47227_1_);
        this.field_191217_a.addChild(this.field_191222_f);
        (this.field_191218_b = new ModelRenderer(this).setTextureSize(p_i47227_3_, p_i47227_4_)).setRotationPoint(0.0f, 0.0f + p_i47227_2_, 0.0f);
        this.field_191218_b.setTextureOffset(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8, 12, 6, p_i47227_1_);
        this.field_191218_b.setTextureOffset(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8, 18, 6, p_i47227_1_ + 0.5f);
        (this.field_191219_c = new ModelRenderer(this).setTextureSize(p_i47227_3_, p_i47227_4_)).setRotationPoint(0.0f, 0.0f + p_i47227_2_ + 2.0f, 0.0f);
        this.field_191219_c.setTextureOffset(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4, 8, 4, p_i47227_1_);
        final ModelRenderer modelrenderer = new ModelRenderer(this, 44, 22).setTextureSize(p_i47227_3_, p_i47227_4_);
        modelrenderer.mirror = true;
        modelrenderer.addBox(4.0f, -2.0f, -2.0f, 4, 8, 4, p_i47227_1_);
        this.field_191219_c.addChild(modelrenderer);
        this.field_191219_c.setTextureOffset(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8, 4, 4, p_i47227_1_);
        (this.field_191220_d = new ModelRenderer(this, 0, 22).setTextureSize(p_i47227_3_, p_i47227_4_)).setRotationPoint(-2.0f, 12.0f + p_i47227_2_, 0.0f);
        this.field_191220_d.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i47227_1_);
        this.field_191221_e = new ModelRenderer(this, 0, 22).setTextureSize(p_i47227_3_, p_i47227_4_);
        this.field_191221_e.mirror = true;
        this.field_191221_e.setRotationPoint(2.0f, 12.0f + p_i47227_2_, 0.0f);
        this.field_191221_e.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i47227_1_);
        (this.field_191223_g = new ModelRenderer(this, 40, 46).setTextureSize(p_i47227_3_, p_i47227_4_)).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, p_i47227_1_);
        this.field_191223_g.setRotationPoint(-5.0f, 2.0f + p_i47227_2_, 0.0f);
        this.field_191224_h = new ModelRenderer(this, 40, 46).setTextureSize(p_i47227_3_, p_i47227_4_);
        this.field_191224_h.mirror = true;
        this.field_191224_h.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i47227_1_);
        this.field_191224_h.setRotationPoint(5.0f, 2.0f + p_i47227_2_, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.field_191217_a.render(scale);
        this.field_191218_b.render(scale);
        this.field_191220_d.render(scale);
        this.field_191221_e.render(scale);
        final AbstractIllager abstractillager = (AbstractIllager)entityIn;
        if (abstractillager.func_193077_p() == AbstractIllager.IllagerArmPose.CROSSED) {
            this.field_191219_c.render(scale);
        }
        else {
            this.field_191223_g.render(scale);
            this.field_191224_h.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        this.field_191217_a.rotateAngleY = netHeadYaw * 0.017453292f;
        this.field_191217_a.rotateAngleX = headPitch * 0.017453292f;
        this.field_191219_c.rotationPointY = 3.0f;
        this.field_191219_c.rotationPointZ = -1.0f;
        this.field_191219_c.rotateAngleX = -0.75f;
        this.field_191220_d.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount * 0.5f;
        this.field_191221_e.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount * 0.5f;
        this.field_191220_d.rotateAngleY = 0.0f;
        this.field_191221_e.rotateAngleY = 0.0f;
        final AbstractIllager.IllagerArmPose abstractillager$illagerarmpose = ((AbstractIllager)entityIn).func_193077_p();
        if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.ATTACKING) {
            final float f = MathHelper.sin(this.swingProgress * 3.1415927f);
            final float f2 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * 3.1415927f);
            this.field_191223_g.rotateAngleZ = 0.0f;
            this.field_191224_h.rotateAngleZ = 0.0f;
            this.field_191223_g.rotateAngleY = 0.15707964f;
            this.field_191224_h.rotateAngleY = -0.15707964f;
            if (((EntityLivingBase)entityIn).getPrimaryHand() == EnumHandSide.RIGHT) {
                this.field_191223_g.rotateAngleX = -1.8849558f + MathHelper.cos(ageInTicks * 0.09f) * 0.15f;
                this.field_191224_h.rotateAngleX = -0.0f + MathHelper.cos(ageInTicks * 0.19f) * 0.5f;
                final ModelRenderer field_191223_g = this.field_191223_g;
                field_191223_g.rotateAngleX += f * 2.2f - f2 * 0.4f;
                final ModelRenderer field_191224_h = this.field_191224_h;
                field_191224_h.rotateAngleX += f * 1.2f - f2 * 0.4f;
            }
            else {
                this.field_191223_g.rotateAngleX = -0.0f + MathHelper.cos(ageInTicks * 0.19f) * 0.5f;
                this.field_191224_h.rotateAngleX = -1.8849558f + MathHelper.cos(ageInTicks * 0.09f) * 0.15f;
                final ModelRenderer field_191223_g2 = this.field_191223_g;
                field_191223_g2.rotateAngleX += f * 1.2f - f2 * 0.4f;
                final ModelRenderer field_191224_h2 = this.field_191224_h;
                field_191224_h2.rotateAngleX += f * 2.2f - f2 * 0.4f;
            }
            final ModelRenderer field_191223_g3 = this.field_191223_g;
            field_191223_g3.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            final ModelRenderer field_191224_h3 = this.field_191224_h;
            field_191224_h3.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            final ModelRenderer field_191223_g4 = this.field_191223_g;
            field_191223_g4.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
            final ModelRenderer field_191224_h4 = this.field_191224_h;
            field_191224_h4.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        }
        else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.SPELLCASTING) {
            this.field_191223_g.rotationPointZ = 0.0f;
            this.field_191223_g.rotationPointX = -5.0f;
            this.field_191224_h.rotationPointZ = 0.0f;
            this.field_191224_h.rotationPointX = 5.0f;
            this.field_191223_g.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662f) * 0.25f;
            this.field_191224_h.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662f) * 0.25f;
            this.field_191223_g.rotateAngleZ = 2.3561945f;
            this.field_191224_h.rotateAngleZ = -2.3561945f;
            this.field_191223_g.rotateAngleY = 0.0f;
            this.field_191224_h.rotateAngleY = 0.0f;
        }
        else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
            this.field_191223_g.rotateAngleY = -0.1f + this.field_191217_a.rotateAngleY;
            this.field_191223_g.rotateAngleX = -1.5707964f + this.field_191217_a.rotateAngleX;
            this.field_191224_h.rotateAngleX = -0.9424779f + this.field_191217_a.rotateAngleX;
            this.field_191224_h.rotateAngleY = this.field_191217_a.rotateAngleY - 0.4f;
            this.field_191224_h.rotateAngleZ = 1.5707964f;
        }
    }
    
    public ModelRenderer func_191216_a(final EnumHandSide p_191216_1_) {
        return (p_191216_1_ == EnumHandSide.LEFT) ? this.field_191224_h : this.field_191223_g;
    }
}
