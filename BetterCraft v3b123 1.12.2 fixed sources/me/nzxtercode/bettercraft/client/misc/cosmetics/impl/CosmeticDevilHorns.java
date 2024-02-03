// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticModelBase;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticDevilHorns extends CosmeticBase
{
    private static final ResourceLocation TEXTURE;
    private DevilHornsModel devilHornsModel;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/devilhorns.png");
    }
    
    public CosmeticDevilHorns(final RenderPlayer player) {
        super(player);
        this.devilHornsModel = new DevilHornsModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        if (player.isSneaking()) {
            GlStateManager.translate(0.0, 0.225, 0.0);
        }
        this.playerRenderer.bindTexture(CosmeticDevilHorns.TEXTURE);
        this.devilHornsModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
        GL11.glPopMatrix();
    }
    
    @Override
    public int getId() {
        return 8;
    }
    
    private class DevilHornsModel extends CosmeticModelBase
    {
        private final ModelRenderer rightHorn;
        private final ModelRenderer leftHorn;
        
        public DevilHornsModel(final RenderPlayer player) {
            super(player);
            this.textureWidth = 64;
            this.textureHeight = 64;
            (this.rightHorn = new ModelRenderer(this)).setRotationPoint(0.0f, 0.0f, 0.0f);
            this.rightHorn.setTextureOffset(0, 0).addBox(6.0f, -12.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(3, 3).addBox(5.0f, -13.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(3, 1).addBox(5.0f, -14.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(0, 4).addBox(3.0f, -15.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(0, 6).addBox(4.0f, -15.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(3, 5).addBox(3.0f, -14.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(0, 2).addBox(6.0f, -13.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(6, 2).addBox(5.0f, -12.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(6, 0).addBox(4.0f, -13.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(6, 4).addBox(6.0f, -11.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(0, 8).addBox(6.0f, -10.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(6, 8).addBox(5.0f, -10.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(6, 6).addBox(4.0f, -14.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(3, 7).addBox(5.0f, -11.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(9, 9).addBox(5.0f, -9.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(15, 21).addBox(3.0f, -9.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(9, 21).addBox(4.0f, -9.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 0).addBox(4.0f, -14.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 4).addBox(5.0f, -13.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(3, 9).addBox(4.0f, -10.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(6, 18).addBox(3.0f, -9.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 10).addBox(4.0f, -15.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 8).addBox(5.0f, -14.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 16).addBox(4.0f, -13.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(12, 20).addBox(5.0f, -10.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(15, 19).addBox(5.0f, -11.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 14).addBox(3.0f, -14.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(12, 18).addBox(4.0f, -9.0f, -1.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(9, 19).addBox(6.0f, -11.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 20).addBox(5.0f, -9.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(3, 21).addBox(4.0f, -8.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 12).addBox(3.0f, -15.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(0, 20).addBox(6.0f, -10.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(3, 19).addBox(5.0f, -12.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(6, 20).addBox(4.0f, -10.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 18).addBox(4.0f, -9.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 6).addBox(6.0f, -12.0f, 0.0f, 1, 1, 1);
            this.rightHorn.setTextureOffset(18, 2).addBox(6.0f, -13.0f, 0.0f, 1, 1, 1);
            (this.leftHorn = new ModelRenderer(this)).setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leftHorn.setTextureOffset(0, 18).addBox(-5.0f, -8.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 17).addBox(-5.0f, -9.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(9, 17).addBox(-4.0f, -9.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(3, 17).addBox(-6.0f, -9.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 16).addBox(-5.0f, -10.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(6, 16).addBox(-6.0f, -10.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(0, 16).addBox(-6.0f, -11.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 13).addBox(-7.0f, -10.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 11).addBox(-7.0f, -11.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 9).addBox(-6.0f, -12.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 7).addBox(-5.0f, -13.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 5).addBox(-4.0f, -14.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 3).addBox(-4.0f, -15.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 1).addBox(-5.0f, -14.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(15, 15).addBox(-5.0f, -15.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(9, 15).addBox(-7.0f, -12.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(3, 15).addBox(-6.0f, -13.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 14).addBox(-7.0f, -13.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(6, 14).addBox(-6.0f, -14.0f, -1.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(0, 14).addBox(-5.0f, -9.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(9, 13).addBox(-4.0f, -9.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(3, 13).addBox(-5.0f, -10.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 10).addBox(-6.0f, -9.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 8).addBox(-6.0f, -10.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 6).addBox(-7.0f, -10.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 4).addBox(-7.0f, -11.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 2).addBox(-6.0f, -11.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 0).addBox(-7.0f, -12.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(12, 12).addBox(-6.0f, -12.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(6, 12).addBox(-5.0f, -13.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(0, 12).addBox(-5.0f, -14.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(9, 11).addBox(-5.0f, -15.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(3, 11).addBox(-4.0f, -14.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(6, 10).addBox(-4.0f, -15.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(0, 10).addBox(-6.0f, -13.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(9, 7).addBox(-6.0f, -14.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(9, 5).addBox(-7.0f, -13.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(9, 3).addBox(-5.0f, -9.0f, 0.0f, 1, 1, 1);
            this.leftHorn.setTextureOffset(9, 1).addBox(-5.0f, -8.0f, 0.0f, 1, 1, 1);
        }
        
        @Override
        public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
            this.leftHorn.rotateAngleX = this.playerModel.bipedHead.rotateAngleX;
            this.leftHorn.rotateAngleY = this.playerModel.bipedHead.rotateAngleY;
            this.leftHorn.rotationPointX = this.playerModel.bipedHead.rotationPointX;
            this.leftHorn.rotationPointY = this.playerModel.bipedHead.rotationPointY;
            this.rightHorn.rotateAngleX = this.playerModel.bipedHead.rotateAngleX;
            this.rightHorn.rotateAngleY = this.playerModel.bipedHead.rotateAngleY;
            this.rightHorn.rotationPointX = this.playerModel.bipedHead.rotationPointX;
            this.rightHorn.rotationPointY = this.playerModel.bipedHead.rotationPointY;
            this.leftHorn.render(scale);
            this.rightHorn.render(scale);
        }
    }
}
