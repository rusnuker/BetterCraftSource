// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticModelBase;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticWitchHat extends CosmeticBase
{
    public final WitchHatModel witchHatModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/witchhat.png");
    }
    
    public CosmeticWitchHat(final RenderPlayer player) {
        super(player);
        this.witchHatModel = new WitchHatModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        final float f = this.getFirstRotationX(player, partialTicks);
        final float f2 = this.getSecondRotationX(player, partialTicks);
        Minecraft.getMinecraft().getTextureManager().bindTexture(CosmeticWitchHat.TEXTURE);
        if (player.isSneaking()) {
            GlStateManager.translate(0.0f, 0.27f, 0.0f);
        }
        GlStateManager.rotate(f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2, 1.0f, 0.0f, 0.0f);
        this.witchHatModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
        GlStateManager.popMatrix();
    }
    
    private float getFirstRotationX(final AbstractClientPlayer Player, final float partialTicks) {
        float f = this.interpolateRotation(Player.prevRenderYawOffset, Player.renderYawOffset, partialTicks);
        final float f2 = this.interpolateRotation(Player.prevRotationYawHead, Player.rotationYawHead, partialTicks);
        float f3 = f2 - f;
        if (Player.isRiding() && Player.ridingEntity instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)Player.ridingEntity;
            f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
            f3 = f2 - f;
            float f4 = MathHelper.wrapAngleTo180_float(f3);
            if (f4 < -85.0f) {
                f4 = -85.0f;
            }
            if (f4 >= 85.0f) {
                f4 = 85.0f;
            }
            f = f2 - f4;
            if (f4 * f4 > 2500.0f) {}
        }
        return f3;
    }
    
    private float getSecondRotationX(final AbstractClientPlayer Player, final float partialTicks) {
        return Player.prevRotationPitch + (Player.rotationPitch - Player.prevRotationPitch) * partialTicks;
    }
    
    private float interpolateRotation(final float par1, final float par2, final float par3) {
        float f;
        for (f = par2 - par1; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return par1 + par3 * f;
    }
    
    @Override
    public int getId() {
        return 25;
    }
    
    public static class WitchHatModel extends CosmeticModelBase
    {
        private final ModelRenderer witchHat;
        int textureWidth;
        int textureHeight;
        
        public WitchHatModel(final RenderPlayer player) {
            super(player);
            this.witchHat = new ModelRenderer(this).setTextureSize(64, 128);
            this.textureWidth = 64;
            this.textureHeight = 128;
            this.witchHat.setRotationPoint(-5.0f, -10.03125f, -5.0f);
            this.witchHat.setTextureOffset(0, 64).addBox(0.0f, 0.0f, 0.0f, 10, 2, 10);
            final ModelRenderer modelrenderer = new ModelRenderer(this).setTextureSize(64, 128);
            modelrenderer.setRotationPoint(1.75f, -4.0f, 2.0f);
            modelrenderer.setTextureOffset(0, 76).addBox(0.0f, 0.0f, 0.0f, 7, 4, 7);
            modelrenderer.rotateAngleX = -0.05235988f;
            modelrenderer.rotateAngleZ = 0.02617994f;
            this.witchHat.addChild(modelrenderer);
            final ModelRenderer modelrenderer2 = new ModelRenderer(this).setTextureSize(64, 128);
            modelrenderer2.setRotationPoint(1.75f, -4.0f, 2.0f);
            modelrenderer2.setTextureOffset(0, 87).addBox(0.0f, 0.0f, 0.0f, 4, 4, 4);
            modelrenderer2.rotateAngleX = -0.10471976f;
            modelrenderer2.rotateAngleZ = 0.05235988f;
            modelrenderer.addChild(modelrenderer2);
            final ModelRenderer modelrenderer3 = new ModelRenderer(this).setTextureSize(64, 128);
            modelrenderer3.setRotationPoint(1.75f, -2.0f, 2.0f);
            modelrenderer3.setTextureOffset(0, 95).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.25f);
            modelrenderer3.rotateAngleX = -0.20943952f;
            modelrenderer3.rotateAngleZ = 0.10471976f;
            modelrenderer2.addChild(modelrenderer3);
            this.witchHat.isHidden = true;
            this.playerModel.bipedHead.addChild(this.witchHat);
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            this.witchHat.isHidden = false;
            this.witchHat.render(scale);
            this.witchHat.isHidden = true;
        }
    }
}
