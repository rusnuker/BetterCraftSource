// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.event;

import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.labymod.main.ModTextures;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticBeard extends CosmeticRenderer<CosmeticBeardData>
{
    public static final int ID = 5;
    private ModelRenderer beard;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int beardWidth = 7;
        final int partLength = 3;
        (this.beard = new ModelRenderer(modelCosmetics).setTextureSize(40, 34)).setRotationPoint(0.0f, -2.0f, -5.0f);
        this.beard.setTextureOffset(0, 0).addBox(-3.5f, 0.0f, 0.0f, 7, 1, 1);
        final ModelRenderer rightBeardCorner = new ModelRenderer(modelCosmetics).setTextureSize(40, 34);
        rightBeardCorner.setRotationPoint(-1.5f, 1.0f, 0.0f);
        rightBeardCorner.setTextureOffset(0, 0).addBox(0.0f, 0.0f, 0.0f, 1, 3, 1);
        rightBeardCorner.rotateAngleZ = 1.5707964f;
        this.beard.addChild(rightBeardCorner);
        final ModelRenderer leftBeardCorner = new ModelRenderer(modelCosmetics).setTextureSize(40, 34);
        leftBeardCorner.setRotationPoint(1.5f, 1.0f, 0.0f);
        leftBeardCorner.setTextureOffset(0, 0).addBox(0.0f, 0.0f, 0.0f, 3, 1, 1);
        this.beard.addChild(leftBeardCorner);
        final ModelRenderer beardFront = new ModelRenderer(modelCosmetics).setTextureSize(40, 34);
        beardFront.setRotationPoint(0.0f, 1.3f, -0.3f);
        beardFront.setTextureOffset(0, 0).addBox(-3.5f, 0.0f, 0.0f, 7, 2, 2);
        this.beard.addChild(beardFront);
        final ModelRenderer firstPart = new ModelRenderer(modelCosmetics).setTextureSize(40, 34);
        firstPart.setRotationPoint(0.0f, 2.0f, 0.0f);
        firstPart.setTextureOffset(0, 0).addBox(-2.5f, 0.0f, 0.0f, 5, 3, 2);
        beardFront.addChild(firstPart);
        final ModelRenderer secondPart = new ModelRenderer(modelCosmetics).setTextureSize(40, 34);
        secondPart.setRotationPoint(0.0f, 3.0f, 0.0f);
        secondPart.setTextureOffset(0, 0).addBox(-2.0f, 0.0f, 0.0f, 4, 3, 2);
        firstPart.addChild(secondPart);
        final ModelRenderer thirdPart = new ModelRenderer(modelCosmetics).setTextureSize(40, 34);
        thirdPart.setRotationPoint(0.0f, 3.0f, 0.0f);
        thirdPart.setTextureOffset(0, 0).addBox(-1.5f, 0.0f, 0.0f, 3, 3, 2);
        secondPart.addChild(thirdPart);
        this.beard.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.beard.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticBeardData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        final float partialTicks = LabyMod.getInstance().getPartialTicks();
        final AbstractClientPlayer clientPlayer = (AbstractClientPlayer)entityIn;
        final ModelRenderer beard = this.bindTextureAndColor(null, ModTextures.COSMETIC_XMAS, this.beard);
        GlStateManager.pushMatrix();
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.scale(0.95f, 0.95f, 0.95f);
        final ModelRenderer beardFront = beard.childModels.get(2);
        final ModelRenderer firstPart = beardFront.childModels.get(0);
        final ModelRenderer secondPart = firstPart.childModels.get(0);
        final ModelRenderer thirdPart = secondPart.childModels.get(0);
        if (canAnimate) {
            final AbstractClientPlayer entitylivingbaseIn = (AbstractClientPlayer)entityIn;
            final double motionX = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * partialTicks);
            final double motionY = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * partialTicks);
            final double motionZ = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * partialTicks);
            final float motionYaw = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
            final double motionSin = LabyModCore.getMath().sin(motionYaw * 3.1415927f / 180.0f);
            final double motionCos = -LabyModCore.getMath().cos(motionYaw * 3.1415927f / 180.0f);
            float rotation = (float)motionY * 10.0f;
            rotation = LabyModCore.getMath().clamp_float(rotation, -6.0f, 32.0f);
            float motionAdd = (float)(motionX * motionSin + motionZ * motionCos) * 100.0f;
            final float motionSub = (float)(motionX * motionCos - motionZ * motionSin) * 100.0f;
            if (motionAdd < 0.0f) {
                motionAdd = 0.0f;
            }
            float pitch = clientPlayer.prevRotationPitch + (clientPlayer.rotationPitch - clientPlayer.prevRotationPitch) * partialTicks;
            final float bendLimit = 30.0f;
            if (pitch < -30.0f) {
                pitch = -30.0f;
            }
            beardFront.rotateAngleX = pitch / -50.0f;
            final float strength = 200.0f;
            final float bend = (pitch < 0.0f) ? (pitch / -50.0f) : 0.0f;
            firstPart.rotateAngleZ = motionSub / 200.0f;
            secondPart.rotateAngleZ = motionSub / 200.0f;
            thirdPart.rotateAngleZ = motionSub / 200.0f;
            firstPart.rotateAngleX = motionAdd / 200.0f;
            secondPart.rotateAngleX = motionAdd / 200.0f;
            thirdPart.rotateAngleX = motionAdd / 200.0f;
            firstPart.rotationPointZ = pitch / 400.0f - bend;
            secondPart.rotationPointZ = pitch / 400.0f;
            thirdPart.rotationPointZ = pitch / 400.0f + bend;
        }
        switch (cosmeticData.getLength()) {
            case 0: {
                firstPart.isHidden = true;
                break;
            }
            case 1: {
                secondPart.isHidden = true;
                break;
            }
            case 2: {
                thirdPart.isHidden = true;
                break;
            }
        }
        beard.isHidden = false;
        beard.render(scale);
        firstPart.isHidden = false;
        secondPart.isHidden = false;
        thirdPart.isHidden = false;
        beard.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public void onTick() {
    }
    
    @Override
    public int getCosmeticId() {
        return 5;
    }
    
    @Override
    public String getCosmeticName() {
        return "Beard";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    @Override
    public float getNameTagHeight() {
        return 0.0f;
    }
    
    public static class CosmeticBeardData extends CosmeticData
    {
        private int length;
        
        public CosmeticBeardData() {
            this.length = 3;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.length = Integer.parseInt(data[0]);
        }
        
        public int getLength() {
            return this.length;
        }
    }
}
