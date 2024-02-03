// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.head.masks;

import net.labymod.user.User;
import java.util.UUID;
import net.labymod.user.cosmetic.custom.UserTextureContainer;
import java.awt.Color;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticMaskCover extends CosmeticRenderer<CosmeticMaskCoverData>
{
    public static final int ID = 31;
    private ModelRenderer mask;
    private ModelRenderer strap;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 20;
        final int height = 20;
        (this.mask = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 20).setTextureOffset(0, 0)).addBox(-3.5f, -8.7f, -5.0f, 7, 9, 1, modelSize);
        this.mask.addBox(-4.5f, -7.7f, -5.0f, 1, 7, 1, modelSize);
        this.mask.addBox(3.5f, -7.7f, -5.0f, 1, 7, 1, modelSize);
        this.mask.isHidden = true;
        (this.strap = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 20).setTextureOffset(0, 0)).addBox(-4.5f, -5.0f, -4.5f, 9, 1, 9, modelSize);
        this.strap.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.mask.showModel = invisible;
        this.strap.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticMaskCoverData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(1.01f, 1.01f, 1.01f);
        this.mask.isHidden = false;
        this.strap.isHidden = false;
        this.bindTextureAndColor(cosmeticData.getMask(), ModTextures.COSMETIC_MASK_FACE, this.mask).render(scale);
        GlStateManager.scale(1.05f, 1.05f, 1.05f);
        this.bindTextureAndColor(cosmeticData.getStrap(), ModTextures.COSMETIC_MASK_FACE, this.strap).render(scale);
        this.strap.isHidden = true;
        this.mask.isHidden = true;
        final ResourceLocation location = LabyMod.getInstance().getUserManager().getCosmeticImageManager().getCoverMaskImageHandler().getResourceLocation((AbstractClientPlayer)entityIn);
        if (location != null) {
            GlStateManager.translate(0.0f, -0.0315f, -5.0f * scale + 0.01f);
            GlStateManager.resetColor();
            GlStateManager.disableLighting();
            Minecraft.getMinecraft().getTextureManager().bindTexture(location);
            LabyMod.getInstance().getDrawUtils().drawTexture(-scale * 4.0f, -scale * 7.5, 255.0, 255.0, scale * 8.0f, scale * 8.0f);
        }
    }
    
    @Override
    public int getCosmeticId() {
        return 31;
    }
    
    @Override
    public String getCosmeticName() {
        return "Cover Mask";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticMaskCoverData extends CosmeticData
    {
        private Color mask;
        private Color strap;
        private UserTextureContainer userTextureContainer;
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.mask = Color.decode("#" + data[0]);
            this.strap = Color.decode("#" + data[1]);
            this.userTextureContainer.setFileName(UUID.fromString(data[2]));
        }
        
        @Override
        public void init(final User user) {
            this.userTextureContainer = user.getCoverMaskContainer();
        }
        
        public Color getMask() {
            return this.mask;
        }
        
        public Color getStrap() {
            return this.strap;
        }
    }
}
