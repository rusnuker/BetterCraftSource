// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.event;

import net.minecraft.util.ResourceLocation;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticCap extends CosmeticRenderer<CosmeticCapData>
{
    public static final int ID = 19;
    private ModelRenderer model;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final float capScale = 0.02f;
        this.model = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(38, 38);
        this.model.setTextureOffset(0, 0).addBox(-4.0f, -6.0f, -7.0f, 8, 1, 11, 0.02f);
        this.model.setTextureOffset(0, 12).addBox(-4.0f, -8.0f, -4.0f, 8, 2, 8, 0.02f);
        this.model.setTextureOffset(0, 22).addBox(-4.0f, -9.0f, -3.0f, 8, 1, 6, 0.02f);
        this.model.setTextureOffset(0, 29).addBox(-3.0f, -9.0f, -4.0f, 6, 1, 1, 0.02f);
        this.model.setTextureOffset(14, 29).addBox(-3.0f, -9.0f, 3.0f, 6, 1, 1, 0.02f);
        this.model.setTextureOffset(0, 31).addBox(-3.0f, -6.0f, -8.0f, 6, 1, 1, 0.02f);
        this.model.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.model.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticCapData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(cosmeticData.getEnumCapType().getBaseTexture());
        final float scaleUp = 1.226f;
        GlStateManager.scale(1.226f, 1.226f, 1.226f);
        if (cosmeticData.isSnapBack()) {
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        }
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        this.model.isHidden = false;
        this.model.render(0.0571f);
        this.model.isHidden = true;
        if (cosmeticData.getEnumCapType().getFrontTexture() != null) {
            final float scaleWolf = 0.3f;
            GlStateManager.translate(0.0, -0.425, -0.24);
            GlStateManager.scale(0.3f, 0.3f, 0.3f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(cosmeticData.getEnumCapType().getFrontTexture());
            LabyMod.getInstance().getDrawUtils().drawTexture(-0.2, -0.2, 256.0, 256.0, 0.4, 0.4);
            GlStateManager.enableAlpha();
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 19;
    }
    
    @Override
    public String getCosmeticName() {
        return "Cap";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticCapData extends CosmeticData
    {
        private EnumCapType enumCapType;
        private boolean snapBack;
        
        public CosmeticCapData() {
            this.enumCapType = EnumCapType.DEFAULT;
            this.snapBack = false;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.snapBack = (Integer.parseInt(data[0]) == 1);
            this.enumCapType = EnumCapType.values()[Integer.parseInt(data[1])];
        }
        
        public EnumCapType getEnumCapType() {
            return this.enumCapType;
        }
        
        public boolean isSnapBack() {
            return this.snapBack;
        }
        
        public enum EnumCapType
        {
            DEFAULT("DEFAULT", 0, "default", (String)null), 
            MERCH("MERCH", 1, "merch", "wolf"), 
            LABYMOD3("LABYMOD3", 2, "labymod3", "number");
            
            private ResourceLocation baseTexture;
            private ResourceLocation frontTexture;
            
            private EnumCapType(final String s, final int n, final String baseName, final String frontName) {
                this.baseTexture = new ResourceLocation("labymod/textures/cosmetics/caps/" + baseName + ".png");
                if (frontName != null) {
                    this.frontTexture = new ResourceLocation("labymod/textures/cosmetics/caps/" + baseName + "_" + frontName + ".png");
                }
            }
            
            private EnumCapType(final String s, final int n, final ResourceLocation baseTexture, final ResourceLocation frontTexture) {
                this.baseTexture = baseTexture;
                this.frontTexture = frontTexture;
            }
            
            public ResourceLocation getBaseTexture() {
                return this.baseTexture;
            }
            
            public ResourceLocation getFrontTexture() {
                return this.frontTexture;
            }
        }
    }
}
