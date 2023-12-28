// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.util.math.MathHelper;
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

public class CosmeticGuardianSpikes extends CosmeticBase
{
    private static ResourceLocation TEXTURE;
    private GuardianSpikesModel guardianSpikesModel;
    
    static {
        CosmeticGuardianSpikes.TEXTURE = new ResourceLocation("textures/misc/guardian.png");
    }
    
    public CosmeticGuardianSpikes(final RenderPlayer player) {
        super(player);
        this.guardianSpikesModel = new GuardianSpikesModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.guardianSpikesCosmetic && (InterClienChatConnection.onlinePlayers.contains(player.getNameClear()) || player.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            this.guardianSpikesModel = new GuardianSpikesModel(this.playerRenderer);
            GlStateManager.pushMatrix();
            GL11.glTranslated(0.0, -1.2000000476837158, 0.0);
            if (player.isSneaking()) {
                GL11.glTranslated(0.0, 0.20000000298023224, 0.0);
            }
            final float[] color = { 1.0f, 1.0f, 1.0f };
            GL11.glColor3d(color[0], color[1], color[2]);
            this.playerRenderer.bindTexture(CosmeticGuardianSpikes.TEXTURE);
            this.guardianSpikesModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
            GlStateManager.popMatrix();
        }
    }
    
    public static class GuardianSpikesModel extends CosmeticModelBase
    {
        private ModelRenderer[] guardianSpines;
        
        public GuardianSpikesModel(final RenderPlayer player) {
            super(player);
            this.textureWidth = 64;
            this.textureHeight = 64;
            this.guardianSpines = new ModelRenderer[12];
            for (int i = 0; i < this.guardianSpines.length; ++i) {
                (this.guardianSpines[i] = new ModelRenderer(this, 0, 0)).addBox(-1.0f, -4.5f, -1.0f, 2, 9, 2);
            }
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            final float[] afloat = { 1.75f, 0.25f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 1.25f, 0.75f, 0.0f, 0.0f };
            final float[] afloat2 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.25f, 1.75f, 1.25f, 0.75f, 0.0f, 0.0f, 0.0f, 0.0f };
            final float[] afloat3 = { 0.0f, 0.0f, 0.25f, 1.75f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.75f, 1.25f };
            final float[] afloat4 = { 0.0f, 0.0f, 8.0f, -8.0f, -8.0f, 8.0f, 8.0f, -8.0f, 0.0f, 0.0f, 8.0f, -8.0f };
            final float[] afloat5 = { -8.0f, -8.0f, -8.0f, -8.0f, 0.0f, 0.0f, 0.0f, 0.0f, 8.0f, 8.0f, 8.0f, 8.0f };
            final float[] afloat6 = { 8.0f, -8.0f, 0.0f, 0.0f, -8.0f, -8.0f, 8.0f, 8.0f, 8.0f, -8.0f, 0.0f, 0.0f };
            final float f = (1.0f - p_78088_3_) * 0.55f;
            for (int i = 0; i < 12; ++i) {
                this.guardianSpines[i].rotateAngleX = 3.1415927f * afloat[i];
                this.guardianSpines[i].rotateAngleY = 3.1415927f * afloat2[i];
                this.guardianSpines[i].rotateAngleZ = 3.1415927f * afloat3[i];
                this.guardianSpines[i].rotationPointX = afloat4[i] * (1.0f + MathHelper.cos(p_78088_3_ * 1.5f + i) * 0.01f - f);
                this.guardianSpines[i].rotationPointY = 16.0f + afloat5[i] * (1.0f + MathHelper.cos(p_78088_3_ * 1.5f + i) * 0.01f - f);
                this.guardianSpines[i].rotationPointZ = afloat6[i] * (1.0f + MathHelper.cos(p_78088_3_ * 1.5f + i) * 0.01f - f);
                this.guardianSpines[i].render(scale);
            }
        }
    }
}
