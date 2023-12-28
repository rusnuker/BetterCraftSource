// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import shadersmod.client.ShadersRender;
import shadersmod.client.Shaders;
import optifine.CustomItems;
import optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class CosmeticEnchantingGlint implements LayerRenderer<AbstractClientPlayer>
{
    protected static final ResourceLocation TEXTURE;
    private final RenderPlayer renderPlayer;
    private final ModelPlayer playerModel;
    
    static {
        TEXTURE = new ResourceLocation("textures/misc/enchantGlint.png");
    }
    
    public CosmeticEnchantingGlint(final RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
        this.playerModel = new ModelPlayer(0.2f, false);
    }
    
    @Override
    public void doRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (GuiCosmetics.enchantCosmetic && (InterClienChatConnection.onlinePlayers.contains(entitylivingbaseIn.getNameClear()) || entitylivingbaseIn.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            this.createEnchantGlint(entitylivingbaseIn, this.renderPlayer.getMainModel(), p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }
    }
    
    private void createEnchantGlint(final EntityLivingBase entitylivingbaseIn, final ModelBase modelbaseIn, final float p_177183_3_, final float p_177183_4_, final float p_177183_5_, final float p_177183_6_, final float p_177183_7_, final float p_177183_8_, final float p_177183_9_) {
        if ((!Config.isCustomItems() || CustomItems.isUseGlint()) && (!Config.isShaders() || !Shaders.isShadowPass)) {
            final float f2 = entitylivingbaseIn.ticksExisted + p_177183_5_;
            this.renderPlayer.bindTexture(CosmeticEnchantingGlint.TEXTURE);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            final float f3 = 0.5f;
            GlStateManager.color(f3, f3, f3, 1.0f);
            for (int i2 = 0; i2 < 2; ++i2) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(768, 1);
                final float f4 = 0.76f;
                GlStateManager.color(0.5f * f4, 0.25f * f4, 0.8f * f4, 1.0f);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                final float f5 = 0.33333334f;
                GlStateManager.scale(f5, f5, f5);
                GlStateManager.rotate(30.0f - i2 * 60.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.0f, f2 * (0.001f + i2 * 0.003f) * 20.0f, 0.0f);
                GlStateManager.matrixMode(5888);
                modelbaseIn.render(entitylivingbaseIn, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
            }
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
