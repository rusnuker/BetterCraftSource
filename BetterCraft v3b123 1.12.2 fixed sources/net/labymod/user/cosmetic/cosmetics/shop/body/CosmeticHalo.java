// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.body;

import java.util.Map;
import net.labymod.user.User;
import java.awt.Color;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticHalo extends CosmeticRenderer<CosmeticHaloData>
{
    public static final int ID = 9;
    private ModelRenderer halo;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        (this.halo = new ModelRenderer(modelCosmetics).setTextureSize(14, 2)).addBox(-3.0f, -12.5f, -4.0f, 6, 1, 1, modelSize);
        this.halo.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.halo.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticHaloData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        final float bounceAnimation = (float)Math.cos(tickValue / 10.0) / 20.0f;
        GlStateManager.rotate(firstRotationX + tickValue / 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, bounceAnimation - (cosmeticData.isHat() ? 0.4f : 0.0f), 0.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_HALO);
        GlStateManager.disableLighting();
        final ModelRenderer targetModel = this.bindTextureAndColor(cosmeticData.getColor(), ModTextures.COSMETIC_HALO, this.halo);
        targetModel.isHidden = false;
        for (int i = 0; i < 4; ++i) {
            targetModel.render(scale);
            GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        }
        targetModel.isHidden = true;
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 9;
    }
    
    @Override
    public String getCosmeticName() {
        return "Halo";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.2f;
    }
    
    public static class CosmeticHaloData extends CosmeticData
    {
        private Color color;
        private boolean hat;
        
        public CosmeticHaloData() {
            this.color = Color.YELLOW;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) {
            this.color = Color.decode("#" + data[0]);
        }
        
        @Override
        public void completed(final User user) {
            final Map<Integer, CosmeticData> cosmetics = user.getCosmetics();
            this.hat = (cosmetics.containsKey(16) || cosmetics.containsKey(7));
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public boolean isHat() {
            return this.hat;
        }
    }
}
