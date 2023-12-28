// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticSlimeGel extends CosmeticBase
{
    private static final ResourceLocation TEXTURE;
    private final RenderPlayer renderPlayer;
    private ModelPlayer playerModel;
    
    static {
        TEXTURE = new ResourceLocation("textures/misc/slime.png");
    }
    
    public CosmeticSlimeGel(final RenderPlayer renderPlayer) {
        super(renderPlayer);
        this.renderPlayer = renderPlayer;
        this.playerModel = renderPlayer.getMainModel();
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.slimeGelCosmetic && (InterClienChatConnection.onlinePlayers.contains(player.getNameClear()) || player.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            this.playerModel = new ModelPlayer(0.4f, false);
            this.renderPlayer.bindTexture(CosmeticSlimeGel.TEXTURE);
            Minecraft.getMinecraft().fontRendererObj.setColor(0.5f, 1.5f, 0.5f, 0.5f);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            this.playerModel.setModelAttributes(this.renderPlayer.getMainModel());
            this.playerModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }
}
