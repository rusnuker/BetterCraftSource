// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.model.ModelRenderer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticHeadset extends CosmeticBase
{
    HeadsetModel headsetModel;
    
    public CosmeticHeadset(final RenderPlayer playerRenderer) {
        super(playerRenderer);
        this.headsetModel = new HeadsetModel(playerRenderer);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.headsetCosmetic && (InterClienChatConnection.onlinePlayers.contains(player.getNameClear()) || player.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            this.headsetModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    class HeadsetModel extends CosmeticModelBase
    {
        private ModelRenderer earCup;
        private ModelRenderer headBandSide;
        private ModelRenderer headBandTop;
        private ModelRenderer mic;
        private ResourceLocation resourceLocation;
        private boolean modelSize;
        
        public HeadsetModel(final RenderPlayer player) {
            super(player);
            this.resourceLocation = new ResourceLocation("textures/misc/headset.png");
            this.modelSize = true;
            final int i = 18;
            final int j = 7;
            (this.earCup = new ModelRenderer(this, 0, 0).setTextureSize(i, j).setTextureOffset(0, 0)).addBox(-1.5f, -1.5f, 0.0f, 3, 3, 1, this.modelSize);
            this.earCup.isHidden = true;
            (this.headBandSide = new ModelRenderer(this, 0, 0).setTextureSize(i, j).setTextureOffset(8, 0)).addBox(-0.5f, -4.0f, 0.0f, 1, 3, 1, this.modelSize);
            this.headBandSide.isHidden = true;
            (this.headBandTop = new ModelRenderer(this, 0, 0).setTextureSize(i, j).setTextureOffset(0, 5)).addBox(-4.0f, 0.0f, -2.0f, 8, 1, 1, this.modelSize);
            this.headBandTop.isHidden = true;
            (this.mic = new ModelRenderer(this, 0, 0).setTextureSize(i, j).setTextureOffset(12, 0)).addBox(-0.5f, -4.0f, 0.0f, 1, 4, 1, this.modelSize);
            this.mic.isHidden = true;
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.disableBlend();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.2f, 1.2f, 1.2f);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0, 0.22, 0.0);
            }
            GlStateManager.rotate(p_78088_5_, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(p_78088_6_, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0, -0.3, 0.1);
            this.earCup.isHidden = false;
            this.headBandSide.isHidden = false;
            this.headBandTop.isHidden = false;
            this.mic.isHidden = false;
            final double d0 = 0.21;
            final double d2 = 0.1;
            final double d3 = 0.6;
            final double d4 = -0.0317;
            for (int i = -1; i < 2; i += 2) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                if (i == 1) {
                    GlStateManager.scale(1.0f, 1.0f, -1.0f);
                }
                GlStateManager.translate(0.1, 0.1, d0);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0, d4, 0.0);
                this.headBandSide.render(scale);
                if (i == -1) {
                    GlStateManager.translate(0.028, -d4 + 0.05, 0.0);
                    GlStateManager.scale(0.8, 0.8, 0.8);
                    GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
                    this.headBandSide.render(scale);
                    GlStateManager.scale(0.65, 0.65, 0.65);
                    GlStateManager.translate(0.01, -0.37, 0.08);
                    GlStateManager.rotate(-30.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.rotate(-60.0f, -1.0f, 0.0f, 0.0f);
                    this.mic.render(scale);
                }
                GlStateManager.popMatrix();
                this.earCup.render(scale);
                GlStateManager.scale(d3, d3, d3);
                GlStateManager.translate(0.0, 0.0, d2);
                GlStateManager.rotate(45.0f, 0.0f, 0.0f, 1.0f);
                this.earCup.render(scale);
                GlStateManager.popMatrix();
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0, -0.1817, -0.0063);
            GlStateManager.scale(0.83999, 1.0, 1.0);
            this.headBandTop.render(scale);
            GlStateManager.popMatrix();
            this.earCup.isHidden = true;
            this.headBandSide.isHidden = true;
            this.headBandTop.isHidden = true;
            this.mic.isHidden = true;
            GlStateManager.popMatrix();
        }
    }
}
