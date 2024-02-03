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

public class CosmeticCap extends CosmeticBase
{
    private CapModel capModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/cap.png");
    }
    
    public CosmeticCap(final RenderPlayer player) {
        super(player);
        this.capModel = new CapModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        final float f = this.getFirstRotationX(player, partialTicks);
        final float f2 = this.getSecondRotationX(player, partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(CosmeticCap.TEXTURE);
        GlStateManager.rotate(f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2, 1.0f, 0.0f, 0.0f);
        if (player.isSneaking()) {
            GlStateManager.translate(0.0, 0.27, 0.0);
        }
        this.capModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getId() {
        return 4;
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
    
    public static class CapModel extends CosmeticModelBase
    {
        private final ModelRenderer Cap1;
        private final ModelRenderer Cap2;
        private final ModelRenderer Cap3;
        private final ModelRenderer Cap4;
        private final ModelRenderer Cap5;
        private final ModelRenderer Cap6;
        private final ModelRenderer Cap7;
        
        public CapModel(final RenderPlayer player) {
            super(player);
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.Cap1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 8, 1, 11);
            this.Cap1.setRotationPoint(-4.0f, -9.0f, -7.0f);
            this.Cap1.setTextureSize(64, 32);
            this.Cap1.mirror = true;
            (this.Cap2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 7, 1, 1);
            this.Cap2.setRotationPoint(-4.0f, -9.0f, -8.0f);
            this.Cap2.setTextureSize(64, 32);
            this.Cap2.mirror = true;
            (this.Cap3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 8, 3, 1);
            this.Cap3.setRotationPoint(-4.0f, -12.0f, -4.0f);
            this.Cap3.setTextureSize(64, 32);
            this.Cap3.mirror = true;
            (this.Cap4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 8, 3, 1);
            this.Cap4.setRotationPoint(-4.0f, -12.0f, 3.0f);
            this.Cap4.setTextureSize(64, 32);
            this.Cap4.mirror = true;
            (this.Cap5 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 6);
            this.Cap5.setRotationPoint(-4.0f, -12.0f, -3.0f);
            this.Cap5.setTextureSize(64, 32);
            this.Cap5.mirror = true;
            (this.Cap6 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 6);
            this.Cap6.setRotationPoint(3.0f, -12.0f, -3.0f);
            this.Cap6.setTextureSize(64, 32);
            this.Cap6.mirror = true;
            (this.Cap7 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 6, 1, 6);
            this.Cap7.setRotationPoint(-3.0f, -12.0f, -3.0f);
            this.Cap7.setTextureSize(64, 32);
            this.Cap7.mirror = true;
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            this.Cap1.render(scale);
            this.Cap2.render(scale);
            this.Cap3.render(scale);
            this.Cap4.render(scale);
            this.Cap5.render(scale);
            this.Cap6.render(scale);
            this.Cap7.render(scale);
        }
    }
}
