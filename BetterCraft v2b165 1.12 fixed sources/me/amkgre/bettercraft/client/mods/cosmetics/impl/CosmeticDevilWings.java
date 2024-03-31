// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticDevilWings extends CosmeticBase
{
    private DevilWingsModel devilWingsModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("textures/misc/devilwings.png");
    }
    
    public CosmeticDevilWings(final RenderPlayer player) {
        super(player);
        this.devilWingsModel = new DevilWingsModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.devilWingsCosmetic && (InterClienChatConnection.onlinePlayers.contains(player.getNameClear()) || player.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            GlStateManager.pushMatrix();
            if (player.isSneaking()) {
                GlStateManager.translate(0.0, 0.225, 0.0);
            }
            GlStateManager.enableBlend();
            this.playerRenderer.bindTexture(CosmeticDevilWings.TEXTURE);
            this.devilWingsModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }
    
    private static class DevilWingsModel extends CosmeticModelBase
    {
        private final ModelRenderer rightWing;
        private final ModelRenderer leftWing;
        
        public DevilWingsModel(final RenderPlayer renderPlayer) {
            super(renderPlayer);
            this.textureWidth = 64;
            this.textureHeight = 64;
            (this.rightWing = new ModelRenderer(this)).setRotationPoint(0.0f, 0.0f, 0.0f);
            this.rightWing.setTextureOffset(39, 30).addBox(3.0f, 0.0f, 2.0f, 1, 5, 1);
            this.rightWing.setTextureOffset(50, 25).addBox(3.0f, 0.0f, 2.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(50, 22).addBox(4.0f, 0.0f, 3.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(47, 38).addBox(5.0f, -2.0f, 3.0f, 1, 2, 1);
            this.rightWing.setTextureOffset(50, 19).addBox(6.0f, -3.0f, 3.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(50, 16).addBox(7.0f, -5.0f, 3.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(50, 13).addBox(7.0f, -4.0f, 3.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(50, 10).addBox(8.0f, -5.0f, 3.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(47, 29).addBox(9.0f, -7.0f, 4.0f, 1, 2, 1);
            this.rightWing.setTextureOffset(50, 7).addBox(10.0f, -7.0f, 4.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(50, 2).addBox(11.0f, -8.0f, 5.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(50, 0).addBox(12.0f, -8.0f, 5.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(21, 50).addBox(14.0f, -9.0f, 4.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(49, 50).addBox(13.0f, -8.0f, 5.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(14, 50).addBox(16.0f, -8.0f, 3.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(47, 26).addBox(17.0f, -7.0f, 2.0f, 1, 2, 1);
            this.rightWing.setTextureOffset(42, 50).addBox(15.0f, -9.0f, 4.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(29, 50).addBox(14.0f, -11.0f, 4.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(25, 50).addBox(14.0f, -10.0f, 5.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(4, 50).addBox(20.0f, -2.0f, 0.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(47, 17).addBox(19.0f, -4.0f, 1.0f, 1, 2, 1);
            this.rightWing.setTextureOffset(10, 50).addBox(18.0f, -5.0f, 2.0f, 1, 1, 1);
            this.rightWing.setTextureOffset(8, 20).addBox(21.0f, -1.0f, 0.0f, 1, 10, 1);
            this.rightWing.setTextureOffset(39, 24).addBox(13.0f, -8.0f, 5.0f, 1, 5, 1);
            this.rightWing.setTextureOffset(43, 44).addBox(17.0f, 1.0f, 3.0f, 1, 3, 1);
            this.rightWing.setTextureOffset(47, 4).addBox(16.0f, -2.0f, 4.0f, 1, 3, 1);
            this.rightWing.setTextureOffset(46, 47).addBox(15.0f, -5.0f, 5.0f, 1, 3, 1);
            this.rightWing.setTextureOffset(39, 47).addBox(14.0f, -8.0f, 4.0f, 1, 3, 1);
            this.rightWing.setTextureOffset(36, 36).addBox(13.0f, -3.0f, 6.0f, 1, 6, 1);
            this.rightWing.setTextureOffset(27, 11).addBox(16.0f, -5.0f, 5.0f, 1, 10, 1);
            this.rightWing.setTextureOffset(34, 0).addBox(17.0f, -4.0f, 4.0f, 1, 8, 1);
            this.rightWing.setTextureOffset(16, 16).addBox(18.0f, -5.0f, 3.0f, 1, 10, 1);
            this.rightWing.setTextureOffset(20, 20).addBox(19.0f, -4.0f, 2.0f, 1, 10, 1);
            this.rightWing.setTextureOffset(16, 0).addBox(20.0f, -2.0f, 1.0f, 1, 10, 1);
            this.rightWing.setTextureOffset(43, 30).addBox(16.0f, -8.0f, 4.0f, 1, 4, 1);
            this.rightWing.setTextureOffset(43, 20).addBox(17.0f, -7.0f, 3.0f, 1, 4, 1);
            this.rightWing.setTextureOffset(12, 0).addBox(15.0f, -5.0f, 6.0f, 1, 11, 1);
            this.rightWing.setTextureOffset(4, 4).addBox(14.0f, -6.0f, 6.0f, 1, 11, 1);
            this.rightWing.setTextureOffset(0, 0).addBox(13.0f, -7.0f, 7.0f, 1, 11, 1);
            this.rightWing.setTextureOffset(28, 28).addBox(12.0f, -6.0f, 7.0f, 1, 9, 1);
            this.rightWing.setTextureOffset(0, 24).addBox(11.0f, -5.0f, 7.0f, 1, 10, 1);
            this.rightWing.setTextureOffset(23, 10).addBox(10.0f, -4.0f, 6.0f, 1, 10, 1);
            this.rightWing.setTextureOffset(43, 10).addBox(14.0f, -9.0f, 5.0f, 1, 4, 1);
            this.rightWing.setTextureOffset(36, 43).addBox(15.0f, -9.0f, 5.0f, 1, 4, 1);
            this.rightWing.setTextureOffset(12, 43).addBox(11.0f, -8.0f, 6.0f, 1, 4, 1);
            this.rightWing.setTextureOffset(46, 34).addBox(12.0f, -8.0f, 6.0f, 1, 3, 1);
            this.rightWing.setTextureOffset(42, 35).addBox(13.0f, -8.0f, 6.0f, 1, 4, 1);
            this.rightWing.setTextureOffset(32, 41).addBox(9.0f, -7.0f, 5.0f, 1, 4, 1);
            this.rightWing.setTextureOffset(20, 40).addBox(10.0f, -7.0f, 5.0f, 1, 4, 1);
            this.rightWing.setTextureOffset(31, 10).addBox(9.0f, -3.0f, 6.0f, 1, 8, 1);
            this.rightWing.setTextureOffset(35, 17).addBox(8.0f, -3.0f, 5.0f, 1, 7, 1);
            this.rightWing.setTextureOffset(35, 9).addBox(7.0f, -2.0f, 5.0f, 1, 7, 1);
            this.rightWing.setTextureOffset(32, 46).addBox(7.0f, -5.0f, 4.0f, 1, 3, 1);
            this.rightWing.setTextureOffset(8, 45).addBox(8.0f, -5.0f, 4.0f, 1, 3, 1);
            this.rightWing.setTextureOffset(47, 23).addBox(6.0f, -3.0f, 4.0f, 1, 2, 1);
            this.rightWing.setTextureOffset(8, 0).addBox(6.0f, -1.0f, 5.0f, 1, 7, 1);
            this.rightWing.setTextureOffset(20, 31).addBox(5.0f, -2.0f, 4.0f, 1, 8, 1);
            this.rightWing.setTextureOffset(39, 18).addBox(4.0f, 0.0f, 4.0f, 1, 5, 1);
            this.rightWing.setTextureOffset(39, 12).addBox(3.0f, 0.0f, 3.0f, 1, 5, 1);
            this.rightWing.setTextureOffset(28, 38).addBox(2.0f, 1.0f, 3.0f, 1, 5, 1);
            (this.leftWing = new ModelRenderer(this)).setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leftWing.setTextureOffset(16, 27).addBox(-21.0f, -26.0f, 1.0f, 1, 10, 1);
            this.leftWing.setTextureOffset(26, 0).addBox(-19.0f, -29.0f, 3.0f, 1, 10, 1);
            this.leftWing.setTextureOffset(4, 27).addBox(-20.0f, -28.0f, 2.0f, 1, 10, 1);
            this.leftWing.setTextureOffset(0, 12).addBox(-15.0f, -30.0f, 6.0f, 1, 11, 1);
            this.leftWing.setTextureOffset(32, 32).addBox(-18.0f, -28.0f, 4.0f, 1, 8, 1);
            this.leftWing.setTextureOffset(24, 24).addBox(-17.0f, -29.0f, 5.0f, 1, 10, 1);
            this.leftWing.setTextureOffset(43, 15).addBox(-18.0f, -31.0f, 3.0f, 1, 4, 1);
            this.leftWing.setTextureOffset(43, 25).addBox(-17.0f, -32.0f, 4.0f, 1, 4, 1);
            this.leftWing.setTextureOffset(12, 12).addBox(-16.0f, -28.0f, 6.0f, 1, 11, 1);
            this.leftWing.setTextureOffset(30, 0).addBox(-13.0f, -30.0f, 7.0f, 1, 9, 1);
            this.leftWing.setTextureOffset(20, 0).addBox(-11.0f, -28.0f, 6.0f, 1, 10, 1);
            this.leftWing.setTextureOffset(43, 5).addBox(-16.0f, -33.0f, 5.0f, 1, 4, 1);
            this.leftWing.setTextureOffset(38, 0).addBox(-3.0f, -23.0f, 3.0f, 1, 5, 1);
            this.leftWing.setTextureOffset(24, 43).addBox(-15.0f, -33.0f, 5.0f, 1, 4, 1);
            this.leftWing.setTextureOffset(42, 0).addBox(-12.0f, -32.0f, 6.0f, 1, 4, 1);
            this.leftWing.setTextureOffset(46, 0).addBox(-13.0f, -32.0f, 6.0f, 1, 3, 1);
            this.leftWing.setTextureOffset(40, 40).addBox(-11.0f, -31.0f, 5.0f, 1, 4, 1);
            this.leftWing.setTextureOffset(8, 40).addBox(-10.0f, -31.0f, 5.0f, 1, 4, 1);
            this.leftWing.setTextureOffset(31, 19).addBox(-10.0f, -27.0f, 6.0f, 1, 8, 1);
            this.leftWing.setTextureOffset(0, 35).addBox(-7.0f, -25.0f, 5.0f, 1, 7, 1);
            this.leftWing.setTextureOffset(47, 20).addBox(-7.0f, -27.0f, 4.0f, 1, 2, 1);
            this.leftWing.setTextureOffset(12, 35).addBox(-8.0f, -26.0f, 5.0f, 1, 7, 1);
            this.leftWing.setTextureOffset(44, 40).addBox(-9.0f, -29.0f, 4.0f, 1, 3, 1);
            this.leftWing.setTextureOffset(24, 35).addBox(-9.0f, -27.0f, 5.0f, 1, 7, 1);
            this.leftWing.setTextureOffset(39, 6).addBox(-4.0f, -24.0f, 3.0f, 1, 5, 1);
            this.leftWing.setTextureOffset(20, 45).addBox(-8.0f, -29.0f, 4.0f, 1, 3, 1);
            this.leftWing.setTextureOffset(15, 11).addBox(-13.0f, -32.0f, 5.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(27, 23).addBox(-9.0f, -29.0f, 3.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(47, 32).addBox(-14.0f, -32.0f, 5.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(4, 0).addBox(-16.0f, -29.0f, 5.0f, 1, 3, 1);
            this.leftWing.setTextureOffset(4, 44).addBox(-15.0f, -32.0f, 4.0f, 1, 3, 1);
            this.leftWing.setTextureOffset(16, 44).addBox(-18.0f, -23.0f, 3.0f, 1, 3, 1);
            this.leftWing.setTextureOffset(19, 11).addBox(-14.0f, -32.0f, 5.0f, 1, 5, 1);
            this.leftWing.setTextureOffset(28, 44).addBox(-17.0f, -26.0f, 4.0f, 1, 3, 1);
            this.leftWing.setTextureOffset(24, 21).addBox(-18.0f, -31.0f, 2.0f, 1, 2, 1);
            this.leftWing.setTextureOffset(47, 43).addBox(-8.0f, -28.0f, 3.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(47, 45).addBox(-21.0f, -26.0f, 0.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(0, 48).addBox(-8.0f, -29.0f, 3.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(47, 8).addBox(-20.0f, -28.0f, 1.0f, 1, 2, 1);
            this.leftWing.setTextureOffset(4, 48).addBox(-11.0f, -31.0f, 4.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(4, 38).addBox(-4.0f, -24.0f, 2.0f, 1, 5, 1);
            this.leftWing.setTextureOffset(11, 48).addBox(-7.0f, -27.0f, 3.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(15, 48).addBox(-17.0f, -32.0f, 3.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(23, 48).addBox(-5.0f, -24.0f, 3.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(27, 48).addBox(-15.0f, -33.0f, 4.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(47, 11).addBox(-6.0f, -26.0f, 3.0f, 1, 2, 1);
            this.leftWing.setTextureOffset(48, 41).addBox(-16.0f, -33.0f, 4.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(47, 14).addBox(-10.0f, -31.0f, 4.0f, 1, 2, 1);
            this.leftWing.setTextureOffset(7, 49).addBox(-15.0f, -34.0f, 5.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(4, 16).addBox(-22.0f, -25.0f, 0.0f, 1, 10, 1);
            this.leftWing.setTextureOffset(18, 49).addBox(-15.0f, -35.0f, 4.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(8, 31).addBox(-6.0f, -26.0f, 4.0f, 1, 8, 1);
            this.leftWing.setTextureOffset(16, 38).addBox(-5.0f, -24.0f, 4.0f, 1, 5, 1);
            this.leftWing.setTextureOffset(35, 25).addBox(-14.0f, -27.0f, 6.0f, 1, 6, 1);
            this.leftWing.setTextureOffset(35, 49).addBox(-12.0f, -32.0f, 5.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(0, 50).addBox(-19.0f, -29.0f, 2.0f, 1, 1, 1);
            this.leftWing.setTextureOffset(0, 43).addBox(-14.0f, -32.0f, 6.0f, 1, 4, 1);
            this.leftWing.setTextureOffset(12, 24).addBox(-12.0f, -29.0f, 7.0f, 1, 10, 1);
            this.leftWing.setTextureOffset(8, 8).addBox(-14.0f, -31.0f, 7.0f, 1, 11, 1);
            this.leftWing.offsetY = 1.5f;
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            this.rightWing.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leftWing.setRotationPoint(0.0f, 0.0f, 0.0f);
            final float f = System.currentTimeMillis() % 2000L / 2000.0f * 3.1415927f * 2.0f;
            this.rightWing.rotateAngleY = (float)Math.toRadians(-20.0) + (float)Math.sin(f) * 0.4f;
            this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
            this.rightWing.render(scale);
            this.leftWing.render(scale);
        }
    }
}
