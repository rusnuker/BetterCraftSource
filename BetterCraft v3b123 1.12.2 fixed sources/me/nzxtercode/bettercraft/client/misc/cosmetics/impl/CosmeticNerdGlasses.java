// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticModelBase;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticNerdGlasses extends CosmeticBase
{
    private static final ResourceLocation TEXTURE;
    private NerdGlassesModel nerdGlassesModel;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/nerdglasses.png");
    }
    
    public CosmeticNerdGlasses(final RenderPlayer player) {
        super(player);
        this.nerdGlassesModel = new NerdGlassesModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        if (player.isSneaking()) {
            GlStateManager.translate(0.0, 0.225, 0.0);
        }
        this.playerRenderer.bindTexture(CosmeticNerdGlasses.TEXTURE);
        this.nerdGlassesModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
        GL11.glPopMatrix();
    }
    
    @Override
    public int getId() {
        return 16;
    }
    
    private class NerdGlassesModel extends CosmeticModelBase
    {
        private final ModelRenderer glasses;
        
        public NerdGlassesModel(final RenderPlayer player) {
            super(player);
            final float f = 0.0f;
            this.textureWidth = 16;
            this.textureHeight = 16;
            (this.glasses = new ModelRenderer(this)).setRotationPoint(0.0f, 24.0f, 0.0f);
            this.glasses.setTextureOffset(0, 4).addBox(3.3f, -4.0f - f, -4.0f, 1, 1, 3);
            this.glasses.setTextureOffset(0, 0).addBox(-3.7f, -4.0f - f, -4.0f, 1, 1, 3);
            this.glasses.setTextureOffset(11, 11).addBox(3.3f, -4.0f - f, -1.0f, 1, 1, 1);
            this.glasses.setTextureOffset(7, 11).addBox(-3.7f, -4.0f - f, -1.0f, 1, 1, 1);
            this.glasses.setTextureOffset(3, 11).addBox(3.3f, -3.0f - f, 0.0f, 1, 1, 1);
            this.glasses.setTextureOffset(0, 10).addBox(-3.7f, -3.0f - f, 0.0f, 1, 1, 1);
            this.glasses.setTextureOffset(9, 9).addBox(3.3f, -3.0f - f, -4.0f, 1, 1, 1);
            this.glasses.setTextureOffset(0, 8).addBox(1.3f, -2.0f - f, -4.0f, 2, 1, 1);
            this.glasses.setTextureOffset(5, 0).addBox(-0.7f, -4.0f - f, -4.0f, 2, 2, 1);
            this.glasses.setTextureOffset(7, 3).addBox(1.3f, -5.0f - f, -4.0f, 2, 1, 1);
            this.glasses.setTextureOffset(5, 9).addBox(-3.7f, -3.0f - f, -4.0f, 1, 1, 1);
            this.glasses.setTextureOffset(7, 7).addBox(-2.7f, -2.0f - f, -4.0f, 2, 1, 1);
            this.glasses.setTextureOffset(5, 5).addBox(-2.7f, -5.0f - f, -4.0f, 2, 1, 1);
            this.glasses.offsetY = 0.05f;
            this.glasses.offsetZ = -0.1f;
        }
        
        @Override
        public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
            this.glasses.rotateAngleX = this.playerModel.bipedHead.rotateAngleX;
            this.glasses.rotateAngleY = this.playerModel.bipedHead.rotateAngleY;
            this.glasses.rotationPointX = this.playerModel.bipedHead.rotationPointX;
            this.glasses.rotationPointY = this.playerModel.bipedHead.rotationPointY;
            this.glasses.render(0.08f);
        }
    }
}
