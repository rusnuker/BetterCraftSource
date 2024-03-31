// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.model.ModelRenderer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticCrystalWings extends CosmeticBase
{
    private CrytsalWingsModel crytsalWingsModel;
    
    public CosmeticCrystalWings(final RenderPlayer playerRenderer) {
        super(playerRenderer);
        this.crytsalWingsModel = new CrytsalWingsModel(playerRenderer);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.crytsalWingsModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
    }
    
    @Override
    public int getId() {
        return 7;
    }
    
    class CrytsalWingsModel extends CosmeticModelBase
    {
        private ModelRenderer model;
        ResourceLocation resourceLocation;
        
        public CrytsalWingsModel(final RenderPlayer player) {
            super(player);
            this.resourceLocation = new ResourceLocation("client/cosmetic/crystalwings.png");
            final int i = 30;
            final int j = 24;
            (this.model = new ModelRenderer(this).setTextureSize(i, j).setTextureOffset(0, 8)).setRotationPoint(-0.0f, 1.0f, 0.0f);
            this.model.addBox(0.0f, -3.0f, 0.0f, 14, 7, 1);
            this.model.isHidden = true;
            final ModelRenderer modelrenderer = new ModelRenderer(this).setTextureSize(i, j).setTextureOffset(0, 16);
            modelrenderer.setRotationPoint(-0.0f, 0.0f, 0.2f);
            modelrenderer.addBox(0.0f, -3.0f, 0.0f, 14, 7, 1);
            this.model.addChild(modelrenderer);
            final ModelRenderer modelrenderer2 = new ModelRenderer(this).setTextureSize(i, j).setTextureOffset(0, 0);
            modelrenderer2.setRotationPoint(-0.0f, 0.0f, 0.2f);
            modelrenderer2.addBox(0.0f, -3.0f, 0.0f, 14, 7, 1);
            modelrenderer.addChild(modelrenderer2);
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float walkingSpeed, final float tickValue, final float p_78088_5_, final float p_78088_6_, final float scale) {
            final float f = (float)Math.cos(tickValue / 10.0f) / 20.0f - 0.03f - walkingSpeed / 20.0f;
            final ModelRenderer modelrenderer = this.model.childModels.get(0);
            final ModelRenderer modelrenderer2 = modelrenderer.childModels.get(0);
            this.model.rotateAngleZ = f * 3.0f;
            modelrenderer.rotateAngleZ = f / 2.0f;
            modelrenderer2.rotateAngleZ = f / 2.0f;
            this.model.rotateAngleY = -0.3f - walkingSpeed / 3.0f;
            this.model.rotateAngleX = 0.3f;
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.6, 1.6, 1.0);
            GlStateManager.translate(0.0, 0.05000000074505806, 0.05000000074505806);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0, 0.07999999821186066, 0.029999999329447746);
                GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                this.model.rotateAngleZ = 0.8f;
                modelrenderer.rotateAngleZ = 0.0f;
                modelrenderer2.rotateAngleZ = 0.0f;
            }
            else {
                final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
                if (rendermanager != null) {
                    GlStateManager.rotate(rendermanager.playerViewX / 3.0f, 1.0f, 0.0f, 0.0f);
                }
            }
            final Color color = Color.WHITE;
            this.model.isHidden = false;
            for (int i = -1; i <= 1; i += 2) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.3f);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569f);
                GlStateManager.disableLighting();
                Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
                if (i == 1) {
                    GlStateManager.scale(-1.0f, 1.0f, 1.0f);
                }
                GlStateManager.translate(0.05, 0.0, 0.0);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
                this.model.render(scale);
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
            this.model.isHidden = true;
            GlStateManager.popMatrix();
        }
    }
}
