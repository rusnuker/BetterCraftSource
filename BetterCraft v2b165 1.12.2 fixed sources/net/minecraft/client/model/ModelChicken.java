// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelChicken extends ModelBase
{
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer bill;
    public ModelRenderer chin;
    
    public ModelChicken() {
        final int i = 16;
        (this.head = new ModelRenderer(this, 0, 0)).addBox(-2.0f, -6.0f, -2.0f, 4, 6, 3, 0.0f);
        this.head.setRotationPoint(0.0f, 15.0f, -4.0f);
        (this.bill = new ModelRenderer(this, 14, 0)).addBox(-2.0f, -4.0f, -4.0f, 4, 2, 2, 0.0f);
        this.bill.setRotationPoint(0.0f, 15.0f, -4.0f);
        (this.chin = new ModelRenderer(this, 14, 4)).addBox(-1.0f, -2.0f, -3.0f, 2, 2, 2, 0.0f);
        this.chin.setRotationPoint(0.0f, 15.0f, -4.0f);
        (this.body = new ModelRenderer(this, 0, 9)).addBox(-3.0f, -4.0f, -3.0f, 6, 8, 6, 0.0f);
        this.body.setRotationPoint(0.0f, 16.0f, 0.0f);
        (this.rightLeg = new ModelRenderer(this, 26, 0)).addBox(-1.0f, 0.0f, -3.0f, 3, 5, 3);
        this.rightLeg.setRotationPoint(-2.0f, 19.0f, 1.0f);
        (this.leftLeg = new ModelRenderer(this, 26, 0)).addBox(-1.0f, 0.0f, -3.0f, 3, 5, 3);
        this.leftLeg.setRotationPoint(1.0f, 19.0f, 1.0f);
        (this.rightWing = new ModelRenderer(this, 24, 13)).addBox(0.0f, 0.0f, -3.0f, 1, 4, 6);
        this.rightWing.setRotationPoint(-4.0f, 13.0f, 0.0f);
        (this.leftWing = new ModelRenderer(this, 24, 13)).addBox(-1.0f, 0.0f, -3.0f, 1, 4, 6);
        this.leftWing.setRotationPoint(4.0f, 13.0f, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 5.0f * scale, 2.0f * scale);
            this.head.render(scale);
            this.bill.render(scale);
            this.chin.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.body.render(scale);
            this.rightLeg.render(scale);
            this.leftLeg.render(scale);
            this.rightWing.render(scale);
            this.leftWing.render(scale);
            GlStateManager.popMatrix();
        }
        else {
            this.head.render(scale);
            this.bill.render(scale);
            this.chin.render(scale);
            this.body.render(scale);
            this.rightLeg.render(scale);
            this.leftLeg.render(scale);
            this.rightWing.render(scale);
            this.leftWing.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        this.head.rotateAngleX = headPitch * 0.017453292f;
        this.head.rotateAngleY = netHeadYaw * 0.017453292f;
        this.bill.rotateAngleX = this.head.rotateAngleX;
        this.bill.rotateAngleY = this.head.rotateAngleY;
        this.chin.rotateAngleX = this.head.rotateAngleX;
        this.chin.rotateAngleY = this.head.rotateAngleY;
        this.body.rotateAngleX = 1.5707964f;
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
        this.rightWing.rotateAngleZ = ageInTicks;
        this.leftWing.rotateAngleZ = -ageInTicks;
    }
}
