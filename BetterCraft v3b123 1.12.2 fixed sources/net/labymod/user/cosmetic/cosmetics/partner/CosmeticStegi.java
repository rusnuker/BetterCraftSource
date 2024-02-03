// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.partner;

import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.core.LabyModCore;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import java.awt.Color;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticStegi extends CosmeticRenderer<CosmeticStegiData>
{
    public static final int ID = 17;
    private ModelRenderer hat;
    private ModelRenderer feather;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 56;
        final int height = 20;
        final ModelRenderer baseHat = new ModelRenderer(modelCosmetics).setTextureSize(56, 20).setTextureOffset(0, 0);
        baseHat.addBox(-3.5f, 1.0f, -3.5f, 8, 2, 8, modelSize);
        baseHat.setRotationPoint(-1.56f, -1.2f, 0.5f);
        baseHat.rotateAngleX = -0.18f;
        baseHat.rotateAngleY = 0.03f;
        baseHat.rotateAngleZ = -0.18f;
        baseHat.isHidden = true;
        final ModelRenderer layer = new ModelRenderer(modelCosmetics).setTextureSize(56, 20).setTextureOffset(0, 10);
        layer.addBox(-4.0f, -0.0f, -4.0f, 8, 2, 8, modelSize);
        layer.setRotationPoint(-0.0f, 0.0f, -0.0f);
        layer.rotateAngleX = -0.05f;
        layer.rotateAngleY = 0.05f;
        layer.rotateAngleZ = -0.05f;
        baseHat.addChild(layer);
        final ModelRenderer layer2 = new ModelRenderer(modelCosmetics).setTextureSize(56, 20).setTextureOffset(32, 0);
        layer2.addBox(-3.0f, -0.5f, -3.0f, 6, 2, 6, modelSize);
        layer2.setRotationPoint(-2.0f, 0.0f, 2.0f);
        layer2.rotateAngleX = -0.1f;
        layer2.rotateAngleY = 0.1f;
        layer2.rotateAngleZ = -0.1f;
        layer.addChild(layer2);
        final ModelRenderer feather = new ModelRenderer(modelCosmetics).setTextureSize(56, 20).setTextureOffset(32, 8);
        feather.addBox(0.0f, -1.0f, -1.0f, 8, 8, 1, modelSize);
        feather.setRotationPoint(3.0f, -1.0f, -1.0f);
        feather.rotateAngleX = -1.9707963f;
        feather.rotateAngleY = 3.4415927f;
        feather.rotateAngleZ = 1.5707964f;
        this.feather = feather;
        this.hat = baseHat;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.hat.showModel = invisible;
        this.feather.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticStegiData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, -0.54f, 0.0f);
        GlStateManager.scale(1.1f, 1.1f, 1.1f);
        final ModelRenderer hat = this.bindTextureAndColor(cosmeticData.getColor(), ModTextures.COSMETIC_STEGI, this.hat);
        hat.isHidden = false;
        hat.render(scale);
        hat.isHidden = true;
        final ModelRenderer feather = this.bindTextureAndColor(Color.WHITE, ModTextures.COSMETIC_STEGI, this.feather);
        GlStateManager.disableLighting();
        final float partialTicks = LabyMod.getInstance().getPartialTicks();
        if (!canAnimate) {
            tickValue += partialTicks;
        }
        final AbstractClientPlayer entitylivingbaseIn = (AbstractClientPlayer)entityIn;
        final double motionX = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * partialTicks);
        final double motionY = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * partialTicks);
        final double motionZ = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * partialTicks);
        final float motionYaw = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
        final double yawSin = LabyModCore.getMath().sin(motionYaw * 3.1415927f / 180.0f);
        final double yawCos = -LabyModCore.getMath().cos(motionYaw * 3.1415927f / 180.0f);
        float rotation = (float)motionY * 10.0f;
        rotation = LabyModCore.getMath().clamp_float(rotation, -6.0f, 32.0f);
        float motionAdd = (float)(motionX * yawSin + motionZ * yawCos) * 50.0f;
        float motionSub = (float)(motionX * yawCos - motionZ * yawSin) * 50.0f;
        if (motionAdd < 0.0f) {
            motionAdd = 0.0f;
        }
        if (motionAdd >= 60.0f) {
            motionAdd = 60.0f + (motionAdd - 60.0f) * 0.2f;
        }
        if (motionSub <= -100.0f) {
            motionSub = -100.0f + (motionSub + 100.0f) * 0.2f;
        }
        final float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
        rotation += LabyModCore.getMath().sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f4;
        GlStateManager.rotate(6.0f - motionAdd / 2.0f + rotation, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-motionSub / 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-motionSub / 2.0f, 0.0f, 1.0f, 0.0f);
        feather.isHidden = false;
        feather.render(scale);
        feather.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 17;
    }
    
    @Override
    public String getCosmeticName() {
        return "Stegi";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.2f;
    }
    
    public static class CosmeticStegiData extends CosmeticData
    {
        private Color color;
        
        public CosmeticStegiData() {
            this.color = Color.RED;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.color = Color.decode("#" + data[0]);
        }
        
        public Color getColor() {
            return this.color;
        }
    }
}
