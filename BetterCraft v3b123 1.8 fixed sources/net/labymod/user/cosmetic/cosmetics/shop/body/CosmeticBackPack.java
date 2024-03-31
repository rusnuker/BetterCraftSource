// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.body;

import java.awt.Color;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticBackPack extends CosmeticRenderer<CosmeticBackPackData>
{
    public static final int ID = 20;
    private ModelRenderer backPack;
    private ModelRenderer primaryColors;
    private ModelRenderer strap;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 38;
        final int height = 16;
        final ModelRenderer backPack = new ModelRenderer(modelCosmetics).setTextureSize(38, 16).setTextureOffset(0, 0);
        backPack.addBox(-3.5f, 0.0f, -0.5f, 7, 9, 2, modelSize);
        backPack.setRotationPoint(0.0f, 0.0f, 0.0f);
        final ModelRenderer button = new ModelRenderer(modelCosmetics).setTextureSize(38, 16).setTextureOffset(34, 0);
        button.addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1, modelSize);
        button.setRotationPoint(0.0f, 2.2f, 1.7f);
        backPack.addChild(button);
        backPack.isHidden = true;
        this.backPack = backPack;
        final ModelRenderer primaryColors = new ModelRenderer(modelCosmetics);
        primaryColors.setRotationPoint(0.0f, 5.8f, 1.9f);
        ModelRenderer model = new ModelRenderer(modelCosmetics).setTextureSize(38, 16).setTextureOffset(18, 0);
        model.addBox(-3.5f, 0.0f, -0.5f, 7, 4, 1, modelSize);
        primaryColors.addChild(model);
        model = new ModelRenderer(modelCosmetics).setTextureSize(38, 16).setTextureOffset(18, 5);
        model.addBox(-3.5f, 0.0f, -0.5f, 7, 3, 1, modelSize);
        model.setRotationPoint(0.0f, -5.5f, -0.4f);
        primaryColors.addChild(model);
        model = new ModelRenderer(modelCosmetics).setTextureSize(38, 16).setTextureOffset(0, 11);
        model.addBox(-4.0f, 0.0f, -0.5f, 8, 3, 2, modelSize);
        model.setRotationPoint(0.0f, 0.9f, -1.8f);
        primaryColors.addChild(model);
        primaryColors.isHidden = true;
        this.primaryColors = primaryColors;
        final ModelRenderer straps = new ModelRenderer(modelCosmetics).setTextureSize(38, 16).setTextureOffset(34, 3);
        straps.addBox(-4.0f, 0.0f, -0.5f, 1, 3, 1, modelSize);
        straps.setRotationPoint(0.5f, -1.3f, -0.2f);
        model = new ModelRenderer(modelCosmetics).setTextureSize(38, 16).setTextureOffset(20, 12);
        model.addBox(-4.0f, 0.0f, -0.5f, 1, 1, 3, modelSize);
        model.setRotationPoint(0.1f, -0.3f, -2.2f);
        straps.addChild(model);
        straps.isHidden = true;
        this.strap = straps;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.backPack.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticBackPackData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.029999999329447746, -0.019999999552965164);
            GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.translate(0.0f, 0.05f, 0.2f);
        final ModelRenderer backpackModel = this.bindTextureAndColor(cosmeticData.getSecondColor(), ModTextures.COSMETIC_BACKPACK, this.backPack);
        this.backPack.isHidden = false;
        this.primaryColors.isHidden = false;
        this.strap.isHidden = false;
        backpackModel.render(scale);
        final ModelRenderer primaryColors = this.bindTextureAndColor(cosmeticData.getColor(), ModTextures.COSMETIC_BACKPACK, this.primaryColors);
        GlStateManager.scale(0.9, 0.9, 0.9);
        primaryColors.render(scale);
        for (int j = 0; j < 2; ++j) {
            this.strap.render(scale);
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
        }
        this.strap.isHidden = true;
        this.primaryColors.isHidden = true;
        this.backPack.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 20;
    }
    
    @Override
    public String getCosmeticName() {
        return "Backpack";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.0f;
    }
    
    public static class CosmeticBackPackData extends CosmeticData
    {
        private Color color;
        private Color secondColor;
        
        public CosmeticBackPackData() {
            this.color = Color.RED;
            this.secondColor = new Color(127, 51, 0);
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.color = Color.decode("#" + data[0]);
            if (data.length > 1) {
                this.secondColor = Color.decode("#" + data[1]);
            }
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public Color getSecondColor() {
            return this.secondColor;
        }
    }
}
