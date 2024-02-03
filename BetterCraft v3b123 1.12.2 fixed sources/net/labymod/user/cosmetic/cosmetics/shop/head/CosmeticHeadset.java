// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.head;

import net.labymod.user.cosmetic.util.CosmeticData;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticHeadset extends CosmeticRenderer<CosmeticHeadsetData>
{
    public static final int ID = 23;
    private ModelRenderer earCup;
    private ModelRenderer headBandSide;
    private ModelRenderer headBandTop;
    private ModelRenderer mic;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 18;
        final int height = 7;
        (this.earCup = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(18, 7).setTextureOffset(0, 0)).addBox(-1.5f, -1.5f, 0.0f, 3, 3, 1, modelSize);
        this.earCup.isHidden = true;
        (this.headBandSide = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(18, 7).setTextureOffset(8, 0)).addBox(-0.5f, -4.0f, 0.0f, 1, 3, 1, modelSize);
        this.headBandSide.isHidden = true;
        (this.headBandTop = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(18, 7).setTextureOffset(0, 5)).addBox(-4.0f, 0.0f, -2.0f, 8, 1, 1, modelSize);
        this.headBandTop.isHidden = true;
        (this.mic = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(18, 7).setTextureOffset(12, 0)).addBox(-0.5f, -4.0f, 0.0f, 1, 4, 1, modelSize);
        this.mic.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.earCup.showModel = invisible;
        this.headBandSide.showModel = invisible;
        this.headBandTop.showModel = invisible;
        this.mic.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticHeadsetData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_HEADSET);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.2f, 1.2f, 1.2f);
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0, -0.3, 0.1);
        this.earCup.isHidden = false;
        this.headBandSide.isHidden = false;
        this.headBandTop.isHidden = false;
        this.mic.isHidden = false;
        final double distanceToMid = 0.21;
        final double earCupWidth = 0.1;
        final double earCupModuleScale = 0.6;
        final double headBandSideHight = -0.0317;
        for (int i = -1; i < 2; i += 2) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
            if (i == 1) {
                GlStateManager.scale(1.0f, 1.0f, -1.0f);
            }
            GlStateManager.translate(0.1, 0.1, 0.21);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0, -0.0317, 0.0);
            this.headBandSide.render(scale);
            if (cosmeticData.isMic() && i == -1) {
                GlStateManager.translate(0.028, 0.0817, 0.0);
                GlStateManager.scale(0.8, 0.8, 0.8);
                GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
                this.headBandSide.render(scale);
                GlStateManager.scale(0.65, 0.65, 0.65);
                GlStateManager.translate(0.01, -0.37, 0.08);
                GlStateManager.rotate(-30.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-60.0f, -1.0f, 0.0f, 0.0f);
                this.mic.render(scale);
            }
            GlStateManager.popMatrix();
            this.earCup.render(scale);
            GlStateManager.scale(0.6, 0.6, 0.6);
            GlStateManager.translate(0.0, 0.0, 0.1);
            GlStateManager.rotate(45.0f, 0.0f, 0.0f, 1.0f);
            this.earCup.render(scale);
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, -0.1817, -0.0063);
        GlStateManager.scale(0.83999, 1.0, 1.0);
        this.headBandTop.render(scale);
        GlStateManager.popMatrix();
        this.earCup.isHidden = true;
        this.headBandSide.isHidden = true;
        this.headBandTop.isHidden = true;
        this.mic.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 23;
    }
    
    @Override
    public String getCosmeticName() {
        return "Headset";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticHeadsetData extends CosmeticData
    {
        private boolean mic;
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.mic = (Integer.parseInt(data[0]) == 1);
        }
        
        public boolean isMic() {
            return this.mic;
        }
    }
}
