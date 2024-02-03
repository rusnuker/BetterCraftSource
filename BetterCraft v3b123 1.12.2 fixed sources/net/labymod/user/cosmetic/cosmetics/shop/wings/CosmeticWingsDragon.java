// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.wings;

import net.labymod.user.cosmetic.util.CosmeticData;
import java.awt.Color;
import net.labymod.core.LabyModCore;
import org.lwjgl.opengl.GL11;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticWingsDragon extends CosmeticRenderer<CosmeticWingsData>
{
    public static final int ID = 2;
    private ModelRenderer wing;
    private ModelRenderer wingTip;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        modelCosmetics.setTextureOffset("body.scale", 220, 53);
        modelCosmetics.setTextureOffset("body.body", 0, 0);
        modelCosmetics.setTextureOffset("wingtip.bone", 112, 136);
        modelCosmetics.setTextureOffset("wing.skin", -56, 88);
        modelCosmetics.setTextureOffset("wing.bone", 112, 88);
        modelCosmetics.setTextureOffset("wingtip.skin", -56, 144);
        final int bw = modelCosmetics.textureWidth;
        final int bh = modelCosmetics.textureHeight;
        modelCosmetics.textureWidth = 256;
        modelCosmetics.textureHeight = 256;
        (this.wing = new ModelRenderer(modelCosmetics, "wing")).setRotationPoint(-12.0f, 5.0f, 2.0f);
        this.wing.addBox("bone", -56.0f, -4.0f, -4.0f, 56, 8, 8);
        this.wing.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 1, 56);
        this.wing.isHidden = true;
        (this.wingTip = new ModelRenderer(modelCosmetics, "wingtip")).setRotationPoint(-56.0f, 0.0f, 0.0f);
        this.wingTip.isHidden = true;
        this.wingTip.addBox("bone", -56.0f, -2.0f, -2.0f, 56, 4, 4);
        this.wingTip.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 1, 56);
        this.wing.addChild(this.wingTip);
        modelCosmetics.textureWidth = bw;
        modelCosmetics.textureHeight = bh;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.wing.showModel = invisible;
        this.wingTip.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticWingsData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        final double movement = Math.abs(entityIn.motionX + entityIn.motionZ);
        final float rotationTick = walkingSpeed + ((entityIn.onGround && !entityIn.isSprinting()) ? tickValue : (tickValue * 12.0f + (float)movement + walkingSpeed)) / 100.0f;
        final int wingsScale = 25;
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_ENDER_DRAGON);
        GlStateManager.scale(0.12, 0.12, 0.12);
        GlStateManager.translate(0.0, -0.3, 1.1);
        GlStateManager.rotate(50.0f, -50.0f, 0.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final Color color = cosmeticData.getWingsColor();
        if (color != null) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
        }
        for (int i = 0; i < 2; ++i) {
            GlStateManager.enableCull();
            final float rotation = rotationTick * 3.1415927f * 2.0f;
            this.wing.rotateAngleX = 0.125f - (float)Math.cos(rotation) * 0.2f;
            this.wing.rotateAngleY = 0.25f;
            this.wing.rotateAngleZ = (float)(Math.sin(rotation) + 1.225) * 0.3f;
            this.wingTip.rotateAngleZ = -(float)(Math.sin(rotation + 2.0f) + 0.5) * 0.75f;
            this.wing.isHidden = false;
            this.wingTip.isHidden = false;
            this.wing.render(scale);
            this.wing.isHidden = true;
            this.wingTip.isHidden = true;
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            if (i == 0) {
                LabyModCore.getRenderImplementation().cullFaceFront();
                if (cosmeticData.getSecondColor() != null) {
                    final Color secondColor = cosmeticData.getSecondColor();
                    GL11.glColor4f(secondColor.getRed() / 255.0f, secondColor.getGreen() / 255.0f, secondColor.getBlue() / 255.0f, 0.5f);
                }
            }
        }
        LabyModCore.getRenderImplementation().cullFaceBack();
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.disableCull();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 2;
    }
    
    @Override
    public String getCosmeticName() {
        return "Dragon Wings";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticWingsData extends CosmeticData
    {
        private long flying;
        private boolean direction;
        private float lastTick;
        private Color wingsColor;
        private Color secondColor;
        
        public CosmeticWingsData() {
            this.flying = -1L;
            this.wingsColor = Color.WHITE;
            this.secondColor = null;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.wingsColor = Color.decode("#" + data[0]);
            if (data.length > 1) {
                this.secondColor = Color.decode("#" + data[1]);
            }
        }
        
        public long getFlying() {
            return this.flying;
        }
        
        public void setFlying(final long flying) {
            this.flying = flying;
        }
        
        public boolean isDirection() {
            return this.direction;
        }
        
        public void setDirection(final boolean direction) {
            this.direction = direction;
        }
        
        public float getLastTick() {
            return this.lastTick;
        }
        
        public void setLastTick(final float lastTick) {
            this.lastTick = lastTick;
        }
        
        public Color getWingsColor() {
            return this.wingsColor;
        }
        
        public Color getSecondColor() {
            return this.secondColor;
        }
    }
}
