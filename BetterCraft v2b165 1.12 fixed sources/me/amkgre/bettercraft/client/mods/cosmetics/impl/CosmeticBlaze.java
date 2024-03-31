// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.model.ModelRenderer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticBlaze extends CosmeticBase
{
    BlazeModel blazeModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("textures/misc/blaze.png");
    }
    
    public CosmeticBlaze(final RenderPlayer player) {
        super(player);
        this.blazeModel = new BlazeModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.blazeCosmetic && (InterClienChatConnection.onlinePlayers.contains(player.getNameClear()) || player.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            GlStateManager.pushMatrix();
            this.playerRenderer.bindTexture(CosmeticBlaze.TEXTURE);
            this.blazeModel.setModelAttributes(this.playerRenderer.getMainModel());
            final float[] color = { 1.0f, 1.0f, 0.9f };
            GL11.glColor3f(color[0], color[1], color[2]);
            this.blazeModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }
    
    private static class BlazeModel extends CosmeticModelBase
    {
        private ModelRenderer[] blazeSticks;
        
        public BlazeModel(final RenderPlayer player) {
            super(player);
            this.blazeSticks = new ModelRenderer[12];
            for (int i2 = 0; i2 < this.blazeSticks.length; ++i2) {
                (this.blazeSticks[i2] = new ModelRenderer(this.playerModel, 0, 16)).addBox(0.0f, 0.0f, 0.0f, 2, 8, 2);
            }
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
            ModelRenderer[] blazeSticks;
            for (int length = (blazeSticks = this.blazeSticks).length, i = 0; i < length; ++i) {
                final ModelRenderer blazeStick = blazeSticks[i];
                blazeStick.render(scale);
            }
        }
        
        @Override
        public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity entityIn) {
            float f2 = p_78087_3_ * 3.1415927f * -0.1f;
            for (int i2 = 0; i2 < 4; ++i2) {
                this.blazeSticks[i2].rotationPointY = -2.0f + MathHelper.cos((i2 * 2 + p_78087_3_) * 0.25f);
                this.blazeSticks[i2].rotationPointX = MathHelper.cos(f2) * 9.0f;
                this.blazeSticks[i2].rotationPointZ = MathHelper.sin(f2) * 9.0f;
                ++f2;
            }
            f2 = 0.7853982f + p_78087_3_ * 3.1415927f * 0.03f;
            for (int j2 = 4; j2 < 8; ++j2) {
                this.blazeSticks[j2].rotationPointY = 2.0f + MathHelper.cos((j2 * 2 + p_78087_3_) * 0.25f);
                this.blazeSticks[j2].rotationPointX = MathHelper.cos(f2) * 7.0f;
                this.blazeSticks[j2].rotationPointZ = MathHelper.sin(f2) * 7.0f;
                ++f2;
            }
            f2 = 0.47123894f + p_78087_3_ * 3.1415927f * -0.05f;
            for (int k2 = 8; k2 < 12; ++k2) {
                this.blazeSticks[k2].rotationPointY = 11.0f + MathHelper.cos((k2 * 1.5f + p_78087_3_) * 0.5f);
                this.blazeSticks[k2].rotationPointX = MathHelper.cos(f2) * 5.0f;
                this.blazeSticks[k2].rotationPointZ = MathHelper.sin(f2) * 5.0f;
                ++f2;
            }
        }
    }
}
