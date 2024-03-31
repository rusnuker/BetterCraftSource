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

public class CosmeticBandana extends CosmeticBase
{
    private BandanaModel bandanaModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/bandana.png");
    }
    
    public CosmeticBandana(final RenderPlayer player) {
        super(player);
        this.bandanaModel = new BandanaModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        final float f = this.getFirstRotationX(player, partialTicks);
        final float f2 = this.getSecondRotationX(player, partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(CosmeticBandana.TEXTURE);
        GlStateManager.rotate(f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2, 1.0f, 0.0f, 0.0f);
        if (player.isSneaking()) {
            GlStateManager.translate(0.0, 0.27, 0.0);
        }
        this.bandanaModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
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
        return 0;
    }
    
    public static class BandanaModel extends CosmeticModelBase
    {
        private final ModelRenderer Bandana1;
        private final ModelRenderer Bandana2;
        private final ModelRenderer Bandana3;
        private final ModelRenderer Bandana4;
        
        public BandanaModel(final RenderPlayer player) {
            super(player);
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.Bandana1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 8, 2, 1);
            this.Bandana1.setRotationPoint(-4.0f, -7.0f, -5.0f);
            this.Bandana1.setTextureSize(64, 32);
            this.Bandana1.mirror = true;
            (this.Bandana2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 10);
            this.Bandana2.setRotationPoint(4.0f, -7.0f, -5.0f);
            this.Bandana2.setTextureSize(64, 32);
            this.Bandana2.mirror = true;
            (this.Bandana3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 8, 2, 1);
            this.Bandana3.setRotationPoint(-4.0f, -7.0f, 4.0f);
            this.Bandana3.setTextureSize(64, 32);
            this.Bandana3.mirror = true;
            (this.Bandana4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 10);
            this.Bandana4.setRotationPoint(-5.0f, -7.0f, -5.0f);
            this.Bandana4.setTextureSize(64, 32);
            this.Bandana4.mirror = true;
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            this.Bandana1.render(scale);
            this.Bandana2.render(scale);
            this.Bandana3.render(scale);
            this.Bandana4.render(scale);
        }
    }
}
