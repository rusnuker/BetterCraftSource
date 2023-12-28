// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticSnoxhEyes extends CosmeticBase
{
    SnoxhModel snoxhModel;
    
    public CosmeticSnoxhEyes(final RenderPlayer playerRenderer) {
        super(playerRenderer);
        this.snoxhModel = new SnoxhModel(playerRenderer);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.snoxhEyesCosmetic && (InterClienChatConnection.onlinePlayers.contains(player.getNameClear()) || player.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            this.snoxhModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    class SnoxhModel extends CosmeticModelBase
    {
        private int shadowOffset;
        private ResourceLocation resourceLocation;
        private ResourceLocation resourceLocation2;
        
        public SnoxhModel(final RenderPlayer player) {
            super(player);
            this.shadowOffset = -6942612;
            this.resourceLocation = new ResourceLocation("textures/misc/mask.png");
            this.resourceLocation2 = new ResourceLocation("textures/misc/eye.png");
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float tickValue, final float firstRotationX, final float secondRotationX, final float scale) {
            final long i = Minecraft.getMinecraft().player.getUniqueID().getLeastSignificantBits() / 1000L;
            final int j = 132;
            final int k = 244;
            final double d0 = j + Math.abs(Math.cos(tickValue / 30.0f) * (k - j));
            final int l = 699;
            final int i2 = (int)(i / (double)this.shadowOffset) - l;
            final int j2 = 2;
            final double d2 = 0.9;
            final int k2 = (int)d0;
            final double d3 = 0.3;
            final float f = (float)(0.0 + d2 / i2 * Math.cos(Math.toRadians(45.0))) / i2;
            final float f2 = (float)(0.0 + d2 / i2 * Math.sin(Math.toRadians(45.0))) / i2;
            final float f3 = (float)(0.0 + d2 / i2 * Math.cos(Math.toRadians(22.5))) / i2;
            final float f4 = (float)(0.0 + d2 / i2 * Math.sin(Math.toRadians(22.5))) / i2;
            GlStateManager.pushMatrix();
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0, 0.25, 0.0);
            }
            GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(0.8, 0.8, 0.8);
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.color(0.8f, 0.8f, 0.8f);
            final double d4 = 0.1;
            GlStateManager.scale(d4 * i2, d4 * i2, d4 * i2);
            GlStateManager.translate(-0.32 / d4, -0.635 / d4, -0.315 / d4);
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.popMatrix();
            final double d5 = 0.1;
            GlStateManager.scale(d5, d5, d5);
            GlStateManager.translate(-0.15 / d5, -0.275 / d5, -0.323 / d5);
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation2);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 772, 1, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder worldrendereradapter = tessellator.getBuffer();
            for (int l2 = 0; l2 < 2; ++l2) {
                GlStateManager.translate((float)(l2 * 3), 0.0f, 0.0f);
                worldrendereradapter.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                for (int i3 = 0; i3 < j2; ++i3) {
                    worldrendereradapter.pos(d3 + 0.0, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(d3 + d2, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + f3, f4, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + f, f2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + 0.0, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(d3 + f, f2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + f4, f3, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + 0.0, d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(0.0 - d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(-d2 - d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-f3 - d3, f4, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-f - d3, f2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(0.0 - d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(-f - d3, f2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-f4 - d3, f3, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(0.0 - d3, d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + 0.0, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(d3 + d2, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + f3, -f4, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + f, -f2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + 0.0, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(d3 + f, -f2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + f4, -f3, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3 + 0.0, -d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(0.0 - d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(-d2 - d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-f3 - d3, -f4, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-f - d3, -f2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(0.0 - d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(-f - d3, -f2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-f4 - d3, -f3, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(0.0 - d3, -d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-d3, -d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-d3, -d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(-d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-d3, -d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3, -d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-d3, d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-d3, d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, k2).endVertex();
                    worldrendereradapter.pos(-d3, 0.0, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(-d3, d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                    worldrendereradapter.pos(d3, d2, 0.0).tex(1.0, 1.0).color(k2, k2, k2, 0).endVertex();
                }
                tessellator.draw();
            }
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
