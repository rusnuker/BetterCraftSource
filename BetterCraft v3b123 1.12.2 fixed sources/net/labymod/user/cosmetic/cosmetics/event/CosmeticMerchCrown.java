// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.event;

import net.labymod.user.cosmetic.util.CosmeticData;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticMerchCrown extends CosmeticRenderer<CosmeticMerchCrownData>
{
    public static final int ID = 18;
    private ModelRenderer base;
    private ModelRenderer diamond;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 30;
        final int height = 16;
        final float crownScale = 0.02f;
        this.base = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(30, 16);
        this.base.setTextureOffset(4, 0).addBox(-4.0f, 0.0f, -5.0f, 8, 2, 1, 0.02f);
        this.base.setTextureOffset(0, 0).addBox(-5.0f, -2.0f, -5.0f, 1, 4, 1, 0.02f);
        this.base.setTextureOffset(0, 5).addBox(-4.0f, -1.0f, -5.0f, 1, 1, 1, 0.02f);
        this.base.setTextureOffset(0, 5).addBox(3.0f, -1.0f, -5.0f, 1, 1, 1, 0.02f);
        this.base.setTextureOffset(4, 5).addBox(-1.5f, -1.0f, -5.0f, 3, 1, 1, 0.02f);
        this.base.setTextureOffset(0, 5).addBox(-0.5f, -2.0f, -5.0f, 1, 1, 1, 0.02f);
        this.base.isHidden = true;
        (this.diamond = new ModelRenderer(modelCosmetics, 12, 5).setTextureSize(30, 16)).addBox(-0.5f, -0.0f, -6.0f, 1, 1, 1, 0.02f);
        this.diamond.rotateAngleZ = 0.8f;
        this.diamond.rotationPointZ = 0.5f;
        this.diamond.rotationPointX = 0.4f;
        this.diamond.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.base.showModel = invisible;
        this.diamond.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticMerchCrownData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        for (int i = 0; i < 4; ++i) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
            final float scaleUp = 1.085f;
            GlStateManager.scale(1.085f, 1.085f, 1.085f);
            if (entityIn.isSneaking()) {
                final float m = entityIn.rotationPitch * -7.0E-4f;
                GlStateManager.translate(0.0, 0.06f - Math.abs(m) + 0.02, m);
            }
            GlStateManager.rotate((float)(90 * i), 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0, -0.4753, 0.0);
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_CROWN);
            this.base.isHidden = false;
            this.base.render(0.0571f);
            this.base.isHidden = true;
            this.diamond.isHidden = false;
            this.diamond.rotateAngleZ = 0.8f;
            this.diamond.rotationPointZ = 0.6f;
            this.diamond.rotationPointX = 0.4f;
            GlStateManager.translate(-0.22f, 0.0f, 0.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            final Color color = cosmeticData.getDiamondColor();
            if (color != null) {
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
            }
            for (int d = 0; d < 3; ++d) {
                this.diamond.render(0.0561f);
                GlStateManager.translate(0.218f, 0.0f, 0.0f);
            }
            GL11.glColor3d(1.0, 1.0, 1.0);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            this.diamond.isHidden = true;
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public int getCosmeticId() {
        return 18;
    }
    
    @Override
    public String getCosmeticName() {
        return "Merch Crown";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticMerchCrownData extends CosmeticData
    {
        private Color diamondColor;
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.diamondColor = Color.decode("#" + data[0]);
        }
        
        public Color getDiamondColor() {
            return this.diamondColor;
        }
    }
}
