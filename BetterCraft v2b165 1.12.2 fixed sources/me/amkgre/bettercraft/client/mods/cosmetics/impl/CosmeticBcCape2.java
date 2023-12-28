// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticController;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.model.ModelPlayer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticBcCape2 extends CosmeticBase
{
    CapeModel capeModel;
    ModelPlayer modelPlayer;
    private static final ResourceLocation CAPE;
    private static final ResourceLocation CAPEOVERLAY;
    
    static {
        CAPE = new ResourceLocation("textures/misc/bccape2.png");
        CAPEOVERLAY = new ResourceLocation("textures/misc/capeoverlay.png");
    }
    
    public CosmeticBcCape2(final RenderPlayer player) {
        super(player);
        this.modelPlayer = player.getMainModel();
        this.capeModel = new CapeModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.bcCapeCosmetic2 && (InterClienChatConnection.onlinePlayers.contains(entitylivingbaseIn.getNameClear()) || entitylivingbaseIn.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            if (this.modelPlayer.scale) {
                this.modelPlayer = this.playerRenderer.getMainModel();
                this.capeModel = new CapeModel(this.playerRenderer);
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            this.playerRenderer.bindTexture(CosmeticBcCape2.CAPE);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            final double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * partialTicks);
            final double d2 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * partialTicks);
            final double d3 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * partialTicks);
            final float f2 = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
            final double d4 = MathHelper.sin(f2 * 3.1415927f / 180.0f);
            final double d5 = -MathHelper.cos(f2 * 3.1415927f / 180.0f);
            float f3 = (float)d2 * 10.0f;
            f3 = MathHelper.clamp(f3, -6.0f, 32.0f);
            float f4 = (float)(d0 * d4 + d3 * d5) * 100.0f;
            final float f5 = (float)(d0 * d5 - d3 * d4) * 100.0f;
            if (f4 < 0.0f) {
                f4 = 0.0f;
            }
            if (f4 > 165.0f) {
                f4 = 165.0f;
            }
            final float f6 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
            f3 += MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f6;
            if (entitylivingbaseIn.isSneaking()) {
                f3 += 25.0f;
                GlStateManager.translate(0.0f, 0.142f, -0.0178f);
            }
            GlStateManager.rotate(6.0f + f4 / 2.0f + f3, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f5 / 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(-f5 / 2.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            final float[] color = CosmeticController.getTopHatColor(entitylivingbaseIn);
            GL11.glColor3f(color[0], color[1], color[2]);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            this.capeModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public static class CapeModel extends CosmeticModelBase
    {
        private ModelRenderer capeModel;
        
        public CapeModel(final RenderPlayer player) {
            super(player);
            (this.capeModel = new ModelRenderer(this.playerModel, 0, 0)).setTextureSize(64, 32);
            this.capeModel.addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1, player.getMainModel().scale);
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            this.capeModel.render(scale);
        }
    }
}
