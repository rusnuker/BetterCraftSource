// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.util.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticBeeWings extends CosmeticBase
{
    private BeeWingsModel beeWingsModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/beewings.png");
    }
    
    public CosmeticBeeWings(final RenderPlayer player) {
        super(player);
        this.beeWingsModel = new BeeWingsModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 0.3, -0.23);
        final float f = 1.5f;
        GlStateManager.scale(f, f, f);
        GlStateManager.rotate(270.0f, 1.0f, 0.0f, 0.0f);
        if (player.isSneaking()) {
            GlStateManager.rotate(35.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0, 0.01, 0.23);
        }
        GlStateManager.enableBlend();
        this.playerRenderer.bindTexture(CosmeticBeeWings.TEXTURE);
        this.beeWingsModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getId() {
        return 2;
    }
    
    private static class BeeWingsModel extends CosmeticModelBase
    {
        private final ModelRenderer rightWing;
        private final ModelRenderer leftWing;
        
        public BeeWingsModel(final RenderPlayer renderPlayer) {
            super(renderPlayer);
            this.textureHeight = 64;
            this.textureWidth = 64;
            (this.rightWing = new ModelRenderer(this, 0, 18)).setRotationPoint(-1.5f, -4.0f, -3.0f);
            this.rightWing.rotateAngleX = 0.0f;
            this.rightWing.rotateAngleY = -0.2618f;
            this.rightWing.rotateAngleZ = 0.0f;
            this.rightWing.addBox(-9.0f, 0.0f, 0.0f, 9, 0, 6, 0.001f);
            (this.leftWing = new ModelRenderer(this, 0, 18)).setRotationPoint(1.5f, -4.0f, -3.0f);
            this.leftWing.rotateAngleX = 0.0f;
            this.leftWing.rotateAngleY = 0.2618f;
            this.leftWing.rotateAngleZ = 0.0f;
            this.leftWing.mirror = true;
            this.leftWing.addBox(0.0f, 0.0f, 0.0f, 9, 0, 6, 0.001f);
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            final float f = p_78088_4_ * 1.3f;
            this.rightWing.rotateAngleY = 0.0f;
            this.rightWing.rotateAngleZ = MathHelper.cos(f) * 3.1415927f * 0.15f;
            this.leftWing.rotateAngleX = this.rightWing.rotateAngleX;
            this.leftWing.rotateAngleY = this.rightWing.rotateAngleY;
            this.leftWing.rotateAngleZ = -this.rightWing.rotateAngleZ;
            this.leftWing.render(scale);
            this.rightWing.render(scale);
        }
    }
}
