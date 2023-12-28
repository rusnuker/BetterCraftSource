// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticBatWings extends CosmeticBase
{
    private BatWingsModel batWingsModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("textures/misc/batwings.png");
    }
    
    public CosmeticBatWings(final RenderPlayer player) {
        super(player);
        this.batWingsModel = new BatWingsModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.batWingsCosmetic && (InterClienChatConnection.onlinePlayers.contains(player.getNameClear()) || player.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            GlStateManager.pushMatrix();
            final float f = 0.5f;
            GlStateManager.translate(0.0, 0.125, 0.0);
            if (player.isSneaking()) {
                GL11.glTranslated(0.0, 0.125, 0.0);
            }
            GlStateManager.enableBlend();
            this.playerRenderer.bindTexture(CosmeticBatWings.TEXTURE);
            this.batWingsModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }
    
    public static class BatWingsModel extends CosmeticModelBase
    {
        private final ModelRenderer batRightWing;
        private final ModelRenderer batLeftWing;
        private final ModelRenderer batOuterLeftWing;
        private final ModelRenderer batOuterRightWing;
        
        public BatWingsModel(final RenderPlayer player) {
            super(player);
            this.textureWidth = 64;
            this.textureHeight = 64;
            (this.batRightWing = new ModelRenderer(this, 42, 0)).addBox(-12.0f, -5.0f, 2.8f, 10, 16, 1);
            this.batLeftWing = new ModelRenderer(this, 42, 0);
            this.batLeftWing.mirror = true;
            this.batLeftWing.addBox(2.0f, -5.0f, 2.8f, 10, 16, 1);
            (this.batOuterRightWing = new ModelRenderer(this, 24, 16)).addBox(-8.0f, -5.0f, 1.3f, 8, 12, 1);
            this.batOuterLeftWing = new ModelRenderer(this, 24, 16);
            this.batOuterLeftWing.mirror = true;
            this.batOuterLeftWing.addBox(0.0f, -5.0f, 1.3f, 8, 12, 1);
            this.batRightWing.addChild(this.batOuterRightWing);
            this.batLeftWing.addChild(this.batOuterLeftWing);
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            this.batRightWing.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.batLeftWing.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.batOuterRightWing.setRotationPoint(-12.0f, 1.0f, 1.5f);
            this.batOuterLeftWing.setRotationPoint(12.0f, 1.0f, 1.5f);
            final float f = System.currentTimeMillis() % 1000L / 1000.0f * 3.1415927f * 2.0f;
            this.batRightWing.rotateAngleY = (float)Math.toRadians(20.0) + (float)Math.sin(f) * 0.4f;
            this.batLeftWing.rotateAngleY = -this.batRightWing.rotateAngleY;
            this.batOuterRightWing.rotateAngleY = this.batRightWing.rotateAngleY * 0.5f;
            this.batOuterLeftWing.rotateAngleY = -this.batRightWing.rotateAngleY * 0.5f;
            this.batRightWing.render(scale);
            this.batLeftWing.render(scale);
        }
    }
}
