// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.Entity;

public class ModelVex extends ModelBiped
{
    protected ModelRenderer field_191229_a;
    protected ModelRenderer field_191230_b;
    
    public ModelVex() {
        this(0.0f);
    }
    
    public ModelVex(final float p_i47224_1_) {
        super(p_i47224_1_, 0.0f, 64, 64);
        this.bipedLeftLeg.showModel = false;
        this.bipedHeadwear.showModel = false;
        (this.bipedRightLeg = new ModelRenderer(this, 32, 0)).addBox(-1.0f, -1.0f, -2.0f, 6, 10, 4, 0.0f);
        this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f, 0.0f);
        (this.field_191230_b = new ModelRenderer(this, 0, 32)).addBox(-20.0f, 0.0f, 0.0f, 20, 12, 1);
        this.field_191229_a = new ModelRenderer(this, 0, 32);
        this.field_191229_a.mirror = true;
        this.field_191229_a.addBox(0.0f, 0.0f, 0.0f, 20, 12, 1);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.field_191230_b.render(scale);
        this.field_191229_a.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        final EntityVex entityvex = (EntityVex)entityIn;
        if (entityvex.func_190647_dj()) {
            if (entityvex.getPrimaryHand() == EnumHandSide.RIGHT) {
                this.bipedRightArm.rotateAngleX = 3.7699115f;
            }
            else {
                this.bipedLeftArm.rotateAngleX = 3.7699115f;
            }
        }
        final ModelRenderer bipedRightLeg = this.bipedRightLeg;
        bipedRightLeg.rotateAngleX += 0.62831855f;
        this.field_191230_b.rotationPointZ = 2.0f;
        this.field_191229_a.rotationPointZ = 2.0f;
        this.field_191230_b.rotationPointY = 1.0f;
        this.field_191229_a.rotationPointY = 1.0f;
        this.field_191230_b.rotateAngleY = 0.47123894f + MathHelper.cos(ageInTicks * 0.8f) * 3.1415927f * 0.05f;
        this.field_191229_a.rotateAngleY = -this.field_191230_b.rotateAngleY;
        this.field_191229_a.rotateAngleZ = -0.47123894f;
        this.field_191229_a.rotateAngleX = 0.47123894f;
        this.field_191230_b.rotateAngleX = 0.47123894f;
        this.field_191230_b.rotateAngleZ = 0.47123894f;
    }
    
    public int func_191228_a() {
        return 23;
    }
}
