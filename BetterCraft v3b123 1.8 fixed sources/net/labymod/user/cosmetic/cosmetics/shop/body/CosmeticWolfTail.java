// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.body;

import java.awt.Color;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.core.LabyModCore;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.lwjgl.opengl.GL11;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticWolfTail extends CosmeticRenderer<CosmeticWolfData>
{
    public static final int ID = 1;
    private ModelRenderer wolfTail;
    private ModelRenderer wolfTailPlayerSkin;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        for (int i = 0; i <= 1; ++i) {
            final boolean playerSkin = i != 0;
            final ModelRenderer target = new ModelRenderer(modelCosmetics);
            if (playerSkin) {
                target.setTextureOffset(56, 30);
            }
            else {
                target.setTextureSize(8, 10);
                target.setTextureOffset(0, 0);
            }
            target.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, modelSize);
            target.setRotationPoint(-0.2f, 10.0f, 3.0f);
            target.isHidden = true;
            if (i == 0) {
                this.wolfTail = target;
            }
            else {
                this.wolfTailPlayerSkin = target;
            }
        }
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.wolfTail.showModel = invisible;
        this.wolfTailPlayerSkin.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticWolfData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        final ModelRenderer targetModel = this.bindTextureAndColor(cosmeticData.getColor(), cosmeticData.isUseSkinTexture() ? null : ModTextures.COSMETIC_TAIL_WOLF, cosmeticData.isUseSkinTexture() ? this.wolfTailPlayerSkin : this.wolfTail);
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0f, 0.2f, -0.25f);
            GlStateManager.rotate(45.0f, 45.0f, 0.0f, 0.0f);
        }
        else {
            GlStateManager.translate(0.0f, 0.1f, -0.25f);
            GlStateManager.rotate(15.0f, 15.0f, 0.0f, 0.0f);
        }
        if (cosmeticData.isUseSkinTexture()) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
        }
        float health = ((AbstractClientPlayer)entityIn).getHealth();
        if (health > 20.0f || Float.isNaN(health)) {
            health = 20.0f;
        }
        if (health < 0.0f) {
            health = 0.0f;
        }
        GlStateManager.translate(0.0f, health / 80.0f, health / 50.0f * -1.0f);
        GlStateManager.rotate(health * 2.0f, health * 2.0f, 0.0f, 0.0f);
        targetModel.isHidden = false;
        targetModel.renderWithRotation(scale);
        targetModel.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setRotationAngles(final ModelCosmetics modelCosmetics, final float movementFactor, final float walkingSpeed, final float tickValue, final float var4, final float var5, final float var6, final Entity entityIn) {
        this.wolfTail.rotateAngleY = LabyModCore.getMath().cos(movementFactor * 0.6662f) * 1.4f * walkingSpeed;
        this.wolfTail.rotateAngleX = walkingSpeed;
        this.wolfTailPlayerSkin.rotateAngleY = LabyModCore.getMath().cos(movementFactor * 0.6662f) * 1.4f * walkingSpeed;
        this.wolfTailPlayerSkin.rotateAngleX = walkingSpeed;
    }
    
    @Override
    public int getCosmeticId() {
        return 1;
    }
    
    @Override
    public String getCosmeticName() {
        return "Wolf tail";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticWolfData extends CosmeticData
    {
        private Color color;
        private boolean useSkinTexture;
        
        public CosmeticWolfData() {
            this.color = Color.WHITE;
            this.useSkinTexture = false;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.color = Color.decode("#" + data[0]);
            this.useSkinTexture = (Integer.parseInt(data[1]) == 1);
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public boolean isUseSkinTexture() {
            return this.useSkinTexture;
        }
    }
}
