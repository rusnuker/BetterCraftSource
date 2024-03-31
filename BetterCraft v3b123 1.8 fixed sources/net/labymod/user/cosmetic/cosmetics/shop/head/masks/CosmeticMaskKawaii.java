// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.head.masks;

import java.util.UUID;
import net.labymod.user.User;
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

public class CosmeticMaskKawaii extends CosmeticRenderer<CosmeticMaskKawaiiData>
{
    public static final int ID = 34;
    private ModelRenderer mask;
    private ModelRenderer strap;
    private ModelRenderer blankMask;
    private ModelRenderer extendedMask;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 20;
        final int height = 20;
        (this.mask = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 20).setTextureOffset(0, 0)).addBox(-4.5f, 0.0f, 0.0f, 9, 1, 1, modelSize);
        this.mask.isHidden = true;
        final ModelRenderer modelBottom = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 20).setTextureOffset(0, 0);
        modelBottom.addBox(-4.5f, 1.0f, 0.0f, 9, 1, 1, modelSize);
        modelBottom.rotationPointZ = -0.3f;
        modelBottom.rotateAngleX = 0.31f;
        this.mask.addChild(modelBottom);
        final ModelRenderer modelTop = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 20).setTextureOffset(0, 0);
        modelTop.addBox(-3.0f, -0.5f, -0.2f, 6, 2, 1, modelSize);
        this.mask.addChild(modelTop);
        (this.extendedMask = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 20).setTextureOffset(0, 0)).addBox(-4.5f, -1.0f, 0.0f, 9, 1, 1, modelSize);
        this.extendedMask.isHidden = true;
        (this.blankMask = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 20).setTextureOffset(0, 0)).addBox(-2.0f, 0.0f, -0.4f, 4, 1, 1, modelSize);
        this.blankMask.isHidden = true;
        (this.strap = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 20).setTextureOffset(0, 0)).addBox(-12.0f, 1.0f, 2.3f, 1, 1, 6, modelSize);
        this.strap.rotateAngleX = 0.1f;
        this.strap.rotateAngleY = 0.03f;
        this.strap.rotateAngleZ = 0.0f;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.mask.showModel = invisible;
        this.strap.showModel = invisible;
        this.extendedMask.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticMaskKawaiiData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        final boolean extended = cosmeticData.isExtended();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.1f, 1.1f, 1.1f);
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0, 0.1, -0.28);
        this.mask.isHidden = false;
        this.strap.isHidden = false;
        this.extendedMask.isHidden = false;
        this.blankMask.isHidden = false;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, -0.1817, -0.0063);
        GlStateManager.scale(0.83999, 1.0, 1.0);
        this.bindTextureAndColor(cosmeticData.getMask(), ModTextures.COSMETIC_MASK_FACE, this.mask);
        this.mask.render(scale);
        if (extended) {
            this.extendedMask.render(scale);
        }
        final ResourceLocation location = LabyMod.getInstance().getUserManager().getCosmeticImageManager().getKawaiiMaskImageHandler().getResourceLocation((AbstractClientPlayer)entityIn);
        if (location != null && cosmeticData.isUsingTexture()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(location);
            final double k = 0.0625;
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.1875, -0.03150000050663948, -0.012520000338554382);
            GlStateManager.scale(0.0625, 0.0625, 0.0625);
            GlStateManager.resetColor();
            GlStateManager.disableLighting();
            LabyMod.getInstance().getDrawUtils().drawTexture(0.0, 0.0, 255.0, 255.0, 6.0, 2.0);
            GlStateManager.popMatrix();
        }
        else {
            this.blankMask.render(scale);
        }
        GlStateManager.scale(0.4, 0.4, 0.4);
        GlStateManager.translate(0.0, 0.18, -0.08);
        GlStateManager.rotate(10.0f, 1.0f, 0.0f, 0.0f);
        this.bindTextureAndColor(cosmeticData.getStrap(), ModTextures.COSMETIC_MASK_FACE, this.mask);
        for (int side = 0; side < 2; ++side) {
            for (int amount = 0; amount < 2; ++amount) {
                this.strap.render(scale);
                if (extended) {
                    GlStateManager.translate(0.0f, -0.1f, 0.0f);
                }
                GlStateManager.scale(1.0f, -1.0f, 1.0f);
            }
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
        }
        GlStateManager.popMatrix();
        this.blankMask.isHidden = true;
        this.strap.isHidden = true;
        this.mask.isHidden = true;
        this.extendedMask.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 34;
    }
    
    @Override
    public String getCosmeticName() {
        return "Kawaii Mask";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticMaskKawaiiData extends CosmeticData
    {
        private Color mask;
        private Color strap;
        private boolean extended;
        private UserTextureContainer userTextureContainer;
        private boolean usingTexture;
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void init(final User user) {
            this.userTextureContainer = user.getKawaiiMaskContainer();
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.mask = Color.decode("#" + data[0]);
            this.strap = Color.decode("#" + data[1]);
            this.extended = (Integer.parseInt(data[2]) == 1);
            final boolean usingTexture = data.length > 3 && !data[3].equals("null");
            this.usingTexture = usingTexture;
            if (usingTexture) {
                this.userTextureContainer.setFileName(UUID.fromString(data[3]));
            }
        }
        
        public Color getMask() {
            return this.mask;
        }
        
        public Color getStrap() {
            return this.strap;
        }
        
        public boolean isExtended() {
            return this.extended;
        }
        
        public boolean isUsingTexture() {
            return this.usingTexture;
        }
    }
}
