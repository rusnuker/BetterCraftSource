// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.head;

import java.awt.Color;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.main.ModTextures;
import net.labymod.core.LabyModCore;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticDog extends CosmeticRenderer<CosmeticDogData>
{
    public static final int ID = 26;
    private ModelRenderer dogEar;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 8;
        final int height = 8;
        (this.dogEar = new ModelRenderer(modelCosmetics).setTextureSize(8, 8).setTextureOffset(0, 0)).addBox(0.0f, -3.0f, 0.0f, 3, 3, 1, modelSize);
        this.dogEar.rotateAngleX = 0.3f;
        this.dogEar.rotateAngleY = -0.2f;
        this.dogEar.rotateAngleZ = 0.3f;
        this.dogEar.isHidden = true;
        final ModelRenderer dogEarEnd = new ModelRenderer(modelCosmetics).setTextureSize(8, 8).setTextureOffset(0, 4);
        dogEarEnd.addBox(0.0f, -3.0f, 0.0f, 3, 3, 1, modelSize);
        dogEarEnd.setRotationPoint(-0.1f, -2.3f, -0.2f);
        dogEarEnd.rotateAngleX = 1.7f;
        dogEarEnd.rotateAngleY = -0.2f;
        dogEarEnd.rotateAngleZ = -0.2f;
        this.dogEar.addChild(dogEarEnd);
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.dogEar.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticDogData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0, -0.5, -0.1);
        float partialTicks = LabyMod.getInstance().getPartialTicks();
        if (!canAnimate) {
            tickValue += partialTicks;
            partialTicks = 0.0f;
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
        float motionAdd = (float)(motionX * yawSin + motionZ * yawCos) * 100.0f;
        final float motionSub = (float)(motionX * yawCos - motionZ * yawSin) * 100.0f;
        if (motionAdd < 0.0f) {
            motionAdd = 0.0f;
        }
        if (motionAdd >= 130.0f) {
            motionAdd = 130.0f + (motionAdd - 180.0f) * 0.2f;
        }
        final float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
        rotation += LabyModCore.getMath().sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f4;
        final ModelRenderer targetModel = this.bindTextureAndColor(cosmeticData.getColor(), ModTextures.COSMETIC_DOG, this.dogEar);
        targetModel.isHidden = false;
        final double distanceToMid = 0.1;
        for (int i = -1; i < 2; i += 2) {
            GlStateManager.pushMatrix();
            if (i == 1) {
                GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            }
            GlStateManager.translate(0.1, 0.0, 0.0);
            GlStateManager.rotate(6.0f - motionAdd / 2.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate((-motionSub / 2.0f * -i - rotation) / 2.0f, 0.0f, 0.0f, 1.0f);
            if (entityIn.isSneaking()) {
                GlStateManager.rotate((float)(30 * (i + 1)), 0.0f, -1.0f, 1.0f);
            }
            targetModel.render(scale);
            GlStateManager.popMatrix();
        }
        targetModel.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 26;
    }
    
    @Override
    public String getCosmeticName() {
        return "Dog Ears";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.2f;
    }
    
    public static class CosmeticDogData extends CosmeticData
    {
        private Color color;
        
        public CosmeticDogData() {
            this.color = Color.WHITE;
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
