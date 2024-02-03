// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.partner;

import java.util.Random;
import net.minecraft.client.renderer.entity.RenderManager;
import net.labymod.main.LabyMod;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.Minecraft;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.main.ModTextures;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.client.model.ModelBase;
import com.google.common.collect.Lists;
import net.minecraft.client.model.ModelBox;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import java.util.List;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticReved extends CosmeticRenderer<CosmeticRevedData>
{
    public static final int ID = 30;
    private List<ModelRenderer> framesRight;
    private List<ModelRenderer> framesLeft;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 5;
        final int height = 12;
        final int totalFrames = 120;
        final boolean isSlim = modelCosmetics.bipedLeftArm.cubeList.get(0).posX2 == 2.0f;
        this.framesRight = (List<ModelRenderer>)Lists.newArrayList();
        this.framesLeft = (List<ModelRenderer>)Lists.newArrayList();
        int slimSkinOffset = 0;
        for (int currentFrame = 0; currentFrame < 120; ++currentFrame) {
            for (int sides = 0; sides < 2; ++sides) {
                final boolean rightSide = sides == 0;
                final ModelRenderer modelFire = new ModelRenderer(modelCosmetics).setTextureSize(600, 34);
                modelFire.setTextureOffset(isSlim ? slimSkinOffset : (currentFrame * 5), isSlim ? 17 : 0);
                modelFire.setRotationPoint(rightSide ? -5.0f : 5.0f, 12.0f, 0.0f);
                modelFire.addBox(rightSide ? (-2.6f - (isSlim ? 0 : 1)) : -1.4f, -0.72f, -2.5f, isSlim ? 4 : 5, 12, 5, modelSize);
                modelFire.isHidden = true;
                if (rightSide) {
                    this.framesRight.add(modelFire);
                }
                else {
                    this.framesLeft.add(modelFire);
                }
            }
            slimSkinOffset += 5 - currentFrame % 2;
        }
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        for (final ModelRenderer frame : this.framesRight) {
            frame.showModel = invisible;
        }
        for (final ModelRenderer frame : this.framesLeft) {
            frame.showModel = invisible;
        }
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticRevedData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        final boolean fireOnRightHand = cosmeticData.isUseRightSide();
        final boolean coloredTexture = cosmeticData.isUseColoredTexture() && !cosmeticData.isRainbow();
        final List<ModelRenderer> modelFire = fireOnRightHand ? this.framesRight : this.framesLeft;
        final int index = (int)(System.currentTimeMillis() / 70L % (modelFire.size() / 2 - 2)) * 2;
        final ModelRenderer modelFireRing = modelFire.get(index);
        int color = cosmeticData.getColor().getRGB();
        if (cosmeticData.isRainbow()) {
            final long time = System.currentTimeMillis() % 10000L;
            color = Color.HSBtoRGB(time / 10000.0f, 1.0f, 1.0f);
        }
        this.bindTextureAndColor(color, coloredTexture ? ModTextures.COSMETIC_REVED_ARM_COLORED : ModTextures.COSMETIC_REVED_ARM, modelFireRing);
        ModelBase.copyModelAngles(fireOnRightHand ? modelCosmetics.bipedRightArm : modelCosmetics.bipedLeftArm, modelFireRing);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569f);
        final float size = 0.91f;
        GlStateManager.translate(modelFireRing.rotationPointX * scale, modelFireRing.rotationPointY * scale, modelFireRing.rotationPointZ * scale);
        GlStateManager.scale(0.91f, 0.91f, 0.91f);
        GlStateManager.translate(-modelFireRing.rotationPointX * scale, -modelFireRing.rotationPointY * scale, -modelFireRing.rotationPointZ * scale);
        modelFireRing.isHidden = false;
        modelFireRing.render(scale);
        modelFireRing.isHidden = true;
        GlStateManager.pushMatrix();
        GlStateManager.translate(modelFireRing.rotationPointX * scale, modelFireRing.rotationPointY * scale, modelFireRing.rotationPointZ * scale);
        GlStateManager.translate(-0.25, -0.2, -0.2);
        FireParticle[] particle2;
        for (int length = (particle2 = cosmeticData.getParticle()).length, i = 0; i < length; ++i) {
            final FireParticle particle = particle2[i];
            particle.render(entityIn, coloredTexture, firstRotationX, color);
        }
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 30;
    }
    
    @Override
    public String getCosmeticName() {
        return "Reved";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.0f;
    }
    
    public static class CosmeticRevedData extends CosmeticData
    {
        private Color color;
        private boolean useRightSide;
        private boolean useColoredTexture;
        private FireParticle[] particle;
        private boolean rainbow;
        
        public CosmeticRevedData() {
            this.color = Color.WHITE;
            this.useRightSide = false;
            this.useColoredTexture = true;
            this.particle = new FireParticle[0];
            this.rainbow = false;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.particle = new FireParticle[20];
            for (int i = 0; i < 20; ++i) {
                this.particle[i] = new FireParticle();
            }
            this.color = Color.decode("#" + data[0]);
            if (!this.color.equals(Color.WHITE)) {
                this.useColoredTexture = false;
            }
            if (data.length >= 2) {
                this.useRightSide = (Integer.parseInt(data[1]) == 1);
            }
            if (data.length >= 3 && Integer.parseInt(data[2]) == 1) {
                this.rainbow = true;
                this.useColoredTexture = true;
            }
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public boolean isUseRightSide() {
            return this.useRightSide;
        }
        
        public boolean isUseColoredTexture() {
            return this.useColoredTexture;
        }
        
        public FireParticle[] getParticle() {
            return this.particle;
        }
        
        public boolean isRainbow() {
            return this.rainbow;
        }
    }
    
    public static class FireParticle
    {
        private double x;
        private double y;
        private double z;
        private float rotZ;
        private long timestamp;
        private long lifetime;
        
        public void render(final Entity entityIn, final boolean coloredTexture, final float firstRotationX, final int color) {
            final long alive = System.currentTimeMillis() - this.timestamp;
            final double percent = 100.0 / this.lifetime * alive - 50.0;
            final double fade = 255.0 - Math.min(255.0, percent * percent);
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.translate(this.x, this.y - alive / 6000.0f, this.z);
            final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
            if (entityIn instanceof AbstractClientPlayer) {
                final AbstractClientPlayer entity = (AbstractClientPlayer)entityIn;
                final float rotation = renderManager.playerViewY - entity.rotationYawHead;
                if (entity != Minecraft.getMinecraft().getRenderViewEntity()) {
                    GlStateManager.rotate(rotation + 180.0f, 0.0f, 1.0f, 0.0f);
                }
                else if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
            }
            GlStateManager.rotate(this.rotZ + alive / 100.0f, 0.0f, 0.0f, 1.0f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            if (!coloredTexture) {
                final int red = color >> 16 & 0xFF;
                final int green = color >> 8 & 0xFF;
                final int blue = color >> 0 & 0xFF;
                GL11.glColor4f(red / 255.0f, green / 255.0f, blue / 255.0f, (float)fade / 255.0f);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_REVED_FLAME);
            LabyMod.getInstance().getDrawUtils().drawTexture(0.0, 0.0, coloredTexture ? 0.0 : 127.0, 0.0, 127.0, 255.0, 0.05, 0.05);
            GlStateManager.popMatrix();
            if (alive > this.lifetime) {
                final Random random = LabyMod.getRandom();
                this.timestamp = System.currentTimeMillis();
                this.lifetime = (random.nextInt(4) + 1) * 1000;
                this.x = random.nextDouble() / 2.8;
                this.y = random.nextDouble();
                this.z = random.nextDouble() / 2.5;
                this.rotZ = random.nextFloat() * 360.0f;
            }
        }
    }
}
