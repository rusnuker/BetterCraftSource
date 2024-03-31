// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.head;

import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.utils.DrawUtils;
import net.minecraft.util.ResourceLocation;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticBandana extends CosmeticRenderer<CosmeticBandanaData>
{
    public static final int ID = 22;
    private ModelRenderer front;
    private ModelRenderer right;
    private ModelRenderer left;
    private ModelRenderer back;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 20;
        final int height = 16;
        final int heightPerSide = 100;
        (this.front = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 16).setTextureOffset(0, 0)).addBox(0.0f, 0.0f, 0.0f, 9, 3, 1, modelSize);
        this.front.isHidden = true;
        (this.right = new ModelRenderer(modelCosmetics, 100, 0).setTextureSize(20, 16).setTextureOffset(0, 100)).addBox(0.0f, 0.0f, 0.0f, 9, 3, 1, modelSize);
        this.right.isHidden = true;
        (this.left = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 16).setTextureOffset(0, 200)).addBox(0.0f, 0.0f, 0.0f, 9, 3, 1, modelSize);
        this.left.isHidden = true;
        (this.back = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(20, 16).setTextureOffset(0, 300)).addBox(0.0f, 0.0f, 0.0f, 9, 3, 1, modelSize);
        this.back.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.front.showModel = invisible;
        this.right.showModel = invisible;
        this.left.showModel = invisible;
        this.back.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticBandanaData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        final ResourceLocation bandanaLocation = LabyMod.getInstance().getUserManager().getCosmeticImageManager().getBandanaImageHandler().getResourceLocation((AbstractClientPlayer)entityIn);
        if (bandanaLocation == null) {
            return;
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(bandanaLocation);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        if (entityIn.isSneaking()) {
            final float m = entityIn.rotationPitch * -7.0E-4f;
            GlStateManager.translate(0.0, 0.06f - Math.abs(m) + 0.02, m);
        }
        if (cosmeticData.isUnderSecondLayer()) {
            final double scaling = 0.88;
            GlStateManager.scale(0.88, 0.88, 0.88);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.292, -0.51, -0.318);
        GlStateManager.scale(1.039, 1.0, 0.6);
        this.front.isHidden = false;
        this.front.render(scale);
        this.front.isHidden = true;
        GlStateManager.popMatrix();
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.086, 0.05, 0.0);
            GlStateManager.translate(-0.267, -0.5, ((i == 0) ? 1 : -1) * -0.28 - 0.015);
            if (i == 1) {
                GlStateManager.translate(0.0, 0.0, 0.03);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(-0.485, 0.0, 0.0);
            }
            GlStateManager.scale(1.078, 1.0, 0.5);
            GlStateManager.translate(-0.057, 0.0, 0.0);
            final ModelRenderer model = (i == 0) ? this.right : this.left;
            model.isHidden = false;
            model.render(scale);
            model.isHidden = true;
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.293, -0.404, 0.29);
        GlStateManager.rotate(5.0f, -1.0f, 0.0f, 0.0f);
        GlStateManager.scale(1.039, 1.0, 0.5);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-0.563, 0.0, 0.0);
        GlStateManager.translate(0.0, 0.0, -0.06);
        this.back.isHidden = false;
        this.back.render(scale);
        this.back.isHidden = true;
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
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
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        GlStateManager.translate(0.0, -0.28, 0.32);
        final double scaling2 = 0.06;
        GlStateManager.scale(0.06, 0.06, 0.06);
        GlStateManager.scale(-1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5, -0.5, 0.2);
        draw.drawTexture(0.0, 0.0, 0.0, 64.0, 12.8, 16.0, 1.0, 1.0, 1.1f);
        GlStateManager.popMatrix();
        for (int t = 0; t < 2; ++t) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(t * -1.3 + 0.15, 0.0, 0.0);
            GlStateManager.rotate(6.0f + motionAdd / 2.0f + rotation, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-motionSub / 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(motionSub / 2.0f, 0.0f, 1.0f, 0.0f);
            for (int j = 0; j < 3; ++j) {
                final double animation = 1.0 + motionAdd / 160.0;
                GlStateManager.rotate((float)(j * 4 * ((j % 2 == ((t == 0) ? 2 : 1)) ? 1 : -1)), 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.0, 0.0, j / 50.0);
                GlStateManager.translate(j / -4.0 * ((t == 0) ? -1 : 1) * animation, j / 4.0 * animation, 0.0);
                GlStateManager.rotate(motionAdd * ((t == 0) ? -1 : 1) / 20.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(rotation * ((t == 0) ? -1 : 1), 0.0f, 0.0f, 1.0f);
                if (t == 1) {
                    draw.drawTexture(0.0, 0.0, 243.2, 0.0, 12.8, 16.0, 1.0, 1.0, 1.1f);
                }
                else {
                    draw.drawTexture(0.0, 0.0, 0.0, 0.0, 12.8, 16.0, 1.0, 1.0, 1.1f);
                }
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 22;
    }
    
    @Override
    public String getCosmeticName() {
        return "Bandana";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticBandanaData extends CosmeticData
    {
        private boolean underSecondLayer;
        
        @Override
        public boolean isEnabled() {
            return LabyMod.getSettings().cosmeticsCustomTextures;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.underSecondLayer = (Integer.parseInt(data[0]) == 1);
        }
        
        public boolean isUnderSecondLayer() {
            return this.underSecondLayer;
        }
    }
}
