// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.wings;

import net.labymod.user.cosmetic.util.CosmeticData;
import java.util.Map;
import net.labymod.user.User;
import java.awt.Color;
import net.labymod.user.cosmetic.util.AnimatedCosmeticData;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticWingsButterfly extends CosmeticRenderer<CosmeticWingsButterflyData>
{
    public static final int ID = 35;
    private ModelRenderer wingMain;
    private ModelRenderer wingSub;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 26;
        final int height = 16;
        (this.wingMain = new ModelRenderer(modelCosmetics).setTextureSize(26, 16).setTextureOffset(0, 0)).setRotationPoint(0.0f, -1.5f, 0.0f);
        this.wingMain.addBox(-0.0f, -2.0f, -1.0f, 12, 7, 1);
        this.wingMain.offsetZ = 0.115f;
        this.wingMain.offsetX = -0.02f;
        this.wingMain.rotateAngleZ = -0.1f;
        this.wingMain.isHidden = true;
        (this.wingSub = new ModelRenderer(modelCosmetics).setTextureSize(26, 16).setTextureOffset(0, 8)).setRotationPoint(0.0f, 4.7f, 0.0f);
        this.wingSub.addBox(-0.0f, -2.0f, -0.5f, 12, 7, 1);
        this.wingSub.rotateAngleZ = 0.1f;
        this.wingSub.rotateAngleX = 0.3f;
        this.wingSub.offsetZ = 0.115f;
        this.wingSub.offsetX = 0.02f;
        this.wingSub.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.wingMain.showModel = invisible;
        this.wingSub.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticWingsButterflyData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.2, 1.2, 1.2);
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, -0.019999999552965164, 0.029999999329447746);
            GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
            final ModelRenderer wingSub = this.wingSub;
            final ModelRenderer wingMain = this.wingMain;
            final float n = -0.83f;
            wingMain.rotateAngleY = -0.83f;
            wingSub.rotateAngleY = -0.83f;
        }
        else {
            float idleAnimation = (float)Math.cos(tickValue / 13.0f) / 5.0f - 0.5f;
            float flyingAnimation = (float)Math.cos(tickValue / 1.0f) / 5.0f - 0.5f;
            cosmeticData.updateFadeAnimation(entityIn.onGround || System.currentTimeMillis() - cosmeticData.lastOnGround < 500L);
            idleAnimation *= cosmeticData.getOnGroundStrength();
            flyingAnimation *= cosmeticData.getAirStrength();
            final ModelRenderer wingSub2 = this.wingSub;
            final ModelRenderer wingMain2 = this.wingMain;
            final float n2 = idleAnimation + flyingAnimation - walkingSpeed / 7.0f;
            wingMain2.rotateAngleY = n2;
            wingSub2.rotateAngleY = n2;
        }
        if (entityIn.onGround) {
            cosmeticData.lastOnGround = System.currentTimeMillis();
        }
        if (cosmeticData.isCape() && ((AbstractClientPlayer)entityIn).isWearing(EnumPlayerModelParts.CAPE)) {
            GlStateManager.translate(0.0f, 0.0f, 0.037f);
            this.wingMain.rotationPointX = 1.0f;
            this.wingSub.rotationPointX = 1.0f;
        }
        else {
            this.wingMain.rotationPointX = 0.0f;
            this.wingSub.rotationPointX = 0.0f;
        }
        this.wingMain.isHidden = false;
        this.wingSub.isHidden = false;
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.alphaFunc(516, 0.003921569f);
            if (i == 0) {
                final int mainColor = cosmeticData.getMainColor().getRGB();
                final int red = mainColor >> 16 & 0xFF;
                final int green = mainColor >> 8 & 0xFF;
                final int blue = mainColor >> 0 & 0xFF;
                GL11.glColor4f(red / 255.0f, green / 255.0f, blue / 255.0f, 0.8f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_WINGS_BUTTERFLY);
            }
            else {
                final int patternColor = cosmeticData.getPatternColor().getRGB();
                final int red = patternColor >> 16 & 0xFF;
                final int green = patternColor >> 8 & 0xFF;
                final int blue = patternColor >> 0 & 0xFF;
                GL11.glColor4f(red / 255.0f, green / 255.0f, blue / 255.0f, 0.8f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_WINGS_BUTTERFLY_OVERLAY);
            }
            this.wingMain.render(scale);
            this.wingSub.render(scale);
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            this.wingMain.render(scale);
            this.wingSub.render(scale);
            GlStateManager.popMatrix();
        }
        this.wingMain.isHidden = true;
        this.wingSub.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 35;
    }
    
    @Override
    public String getCosmeticName() {
        return "Butterfly Wings";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticWingsButterflyData extends AnimatedCosmeticData
    {
        private Color mainColor;
        private Color patternColor;
        private boolean cape;
        protected long lastOnGround;
        
        public CosmeticWingsButterflyData() {
            this.mainColor = new Color(39, 131, 173);
            this.patternColor = new Color(255, 255, 255);
            this.lastOnGround = 0L;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.mainColor = Color.decode("#" + data[0]);
            if (data.length > 1) {
                this.patternColor = Color.decode("#" + data[1]);
            }
        }
        
        @Override
        public void completed(final User user) {
            final Map<Integer, CosmeticData> cosmetics = user.getCosmetics();
            this.cape = cosmetics.containsKey(0);
        }
        
        public Color getMainColor() {
            return this.mainColor;
        }
        
        public Color getPatternColor() {
            return this.patternColor;
        }
        
        public boolean isCape() {
            return this.cape;
        }
    }
}
