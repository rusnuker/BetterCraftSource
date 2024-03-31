// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticModelBase;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticSixPath extends CosmeticBase
{
    private SixPathModel sixPathModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("textures/misc/sixpath.png");
    }
    
    public CosmeticSixPath(final RenderPlayer player) {
        super(player);
        this.sixPathModel = new SixPathModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (GuiCosmetics.sixPathCosmetic && (InterClienChatConnection.onlinePlayers.contains(player.getNameClear()) || player.getNameClear().equals(Minecraft.getMinecraft().player.getNameClear()))) {
            GlStateManager.pushMatrix();
            this.playerRenderer.bindTexture(CosmeticSixPath.TEXTURE);
            this.sixPathModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
            GL11.glPopMatrix();
        }
    }
    
    private class SixPathModel extends CosmeticModelBase
    {
        private final ModelRenderer six_path;
        
        public SixPathModel(final RenderPlayer player) {
            super(player);
            this.textureWidth = 128;
            this.textureHeight = 128;
            (this.six_path = new ModelRenderer(this)).setRotationPoint(0.0f, 0.0f, 0.0f);
            this.six_path.setTextureOffset(120, 0).addBox(-1.0f, -10.0f, 6.0f, 2, 2, 2, 0.0f);
            this.six_path.setTextureOffset(120, 0).addBox(-1.0f, -10.0f, 5.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-1.0f, -10.0f, 6.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-1.0f, -10.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-1.0f, -9.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-0.5f, -10.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-1.5f, -10.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-8.0f, -4.0f, 6.0f, 2, 2, 2, 0.0f);
            this.six_path.setTextureOffset(120, 0).addBox(-8.0f, -4.0f, 5.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-8.0f, -4.0f, 6.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-8.0f, -4.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-8.0f, -3.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-7.5f, -4.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-8.5f, -4.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(6.0f, -4.0f, 6.0f, 2, 2, 2, 0.0f);
            this.six_path.setTextureOffset(120, 0).addBox(6.0f, -4.0f, 5.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(6.0f, -4.0f, 6.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(6.0f, -4.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(6.0f, -3.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(6.5f, -4.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(5.5f, -4.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(8.0f, 5.0f, 6.0f, 2, 2, 2, 0.0f);
            this.six_path.setTextureOffset(120, 0).addBox(8.0f, 5.0f, 5.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(8.0f, 5.0f, 6.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(8.0f, 4.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(8.0f, 5.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(8.5f, 5.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(7.5f, 5.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-10.0f, 5.0f, 6.0f, 2, 2, 2, 0.0f);
            this.six_path.setTextureOffset(120, 0).addBox(-10.0f, 5.0f, 5.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-10.0f, 5.0f, 6.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-10.0f, 4.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-10.0f, 5.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-9.5f, 5.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-10.5f, 5.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-6.0f, 13.0f, 6.0f, 2, 2, 2, 0.0f);
            this.six_path.setTextureOffset(120, 0).addBox(-6.0f, 13.0f, 5.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-6.0f, 13.0f, 6.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-6.0f, 12.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-6.0f, 13.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-5.5f, 13.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(-6.5f, 13.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(4.0f, 13.0f, 6.0f, 2, 2, 2, 0.0f);
            this.six_path.setTextureOffset(120, 0).addBox(4.0f, 13.0f, 5.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(4.0f, 13.0f, 6.5f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(4.0f, 12.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(4.0f, 13.5f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(4.5f, 13.0f, 6.0f, 2, 2, 2, -0.25f);
            this.six_path.setTextureOffset(120, 0).addBox(3.5f, 13.0f, 6.0f, 2, 2, 2, -0.25f);
        }
        
        @Override
        public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
            this.six_path.render(scale);
        }
    }
}
