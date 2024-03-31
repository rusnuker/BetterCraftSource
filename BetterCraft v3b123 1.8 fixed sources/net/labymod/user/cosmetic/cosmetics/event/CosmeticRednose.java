// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.event;

import net.labymod.user.cosmetic.util.CosmeticData;
import java.awt.Color;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticRednose extends CosmeticRenderer<CosmeticRedNoseData>
{
    public static final int ID = 6;
    private ModelRenderer nose;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        (this.nose = new ModelRenderer(modelCosmetics).setTextureSize(8, 4)).setRotationPoint(-1.0f, -4.0f, -5.3f);
        this.nose.setTextureOffset(0, 0).addBox(0.0f, 0.0f, 0.0f, 2, 2, 2);
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.nose.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticRedNoseData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        final ModelRenderer targetModel = this.bindTextureAndColor(null, ModTextures.COSMETIC_REDNOSE, this.nose);
        targetModel.isHidden = false;
        GlStateManager.translate(0.0f, cosmeticData.getOffset() / 100.0f, 0.0f);
        targetModel.render(scale);
        GlStateManager.scale(1.02f, 1.02f, 1.02f);
        GlStateManager.translate(0.0f, 0.004f, 0.0f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 772, 1, 1);
        GlStateManager.shadeModel(7425);
        final float shine = (float)Math.cos(tickValue / 10.0f) / 3.0f + 0.6f;
        GlStateManager.color(1.0f, 0.0f, 0.0f, shine);
        targetModel.render(scale);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        targetModel.isHidden = true;
    }
    
    @Override
    public int getCosmeticId() {
        return 6;
    }
    
    @Override
    public String getCosmeticName() {
        return "Red nose";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.0f;
    }
    
    public static class CosmeticRedNoseData extends CosmeticData
    {
        private int offset;
        
        public CosmeticRedNoseData() {
            this.offset = 0;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.offset = Integer.parseInt(data[0]);
        }
        
        public int getOffset() {
            return this.offset;
        }
    }
}
