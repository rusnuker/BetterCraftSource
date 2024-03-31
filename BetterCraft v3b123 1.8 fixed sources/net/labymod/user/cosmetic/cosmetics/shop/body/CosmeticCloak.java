// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.body;

import net.labymod.user.cosmetic.util.CosmeticData;
import net.minecraft.util.ResourceLocation;
import net.labymod.user.User;
import net.labymod.user.UserManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.labymod.core.LabyModCore;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticCloak extends CosmeticRenderer<CosmeticCloakData>
{
    public static final int ID = 0;
    private ModelRenderer cloak;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 64;
        final int height = 32;
        (this.cloak = new ModelRenderer(modelCosmetics, 0, 0).setTextureSize(64, 32).setTextureOffset(0, 0)).addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1, modelSize);
        this.cloak.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.cloak.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticCloakData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        final User user = userManager.getUser(entityIn.getUniqueID());
        final boolean mojangCapeVisible = userManager.getCosmeticImageManager().getCloakImageHandler().canRenderMojangCape(user, (AbstractClientPlayer)entityIn);
        final boolean wearingCape = ((AbstractClientPlayer)entityIn).isWearing(EnumPlayerModelParts.CAPE);
        if (mojangCapeVisible || !wearingCape || LabyModCore.getMinecraft().isWearingElytra(entityIn)) {
            return;
        }
        final ResourceLocation cloakLocation = LabyMod.getInstance().getUserManager().getCosmeticImageManager().getCloakImageHandler().getResourceLocation((AbstractClientPlayer)entityIn);
        if (cloakLocation == null) {
            return;
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(cloakLocation);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.125f);
        final float partialTicks = LabyMod.getInstance().getPartialTicks();
        final AbstractClientPlayer entitylivingbaseIn = (AbstractClientPlayer)entityIn;
        final double motionX = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * partialTicks);
        final double motionY = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * partialTicks);
        final double motionZ = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * partialTicks);
        final float motionYaw = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
        final double yawSin = LabyModCore.getMath().sin(motionYaw * 3.1415927f / 180.0f);
        final double yawCos = -LabyModCore.getMath().cos(motionYaw * 3.1415927f / 180.0f);
        float cloakRotation = (float)motionY * 10.0f;
        cloakRotation = LabyModCore.getMath().clamp_float(cloakRotation, -6.0f, 32.0f);
        float motionAdd = (float)(motionX * yawSin + motionZ * yawCos) * 100.0f;
        final float motionSub = (float)(motionX * yawCos - motionZ * yawSin) * 100.0f;
        if (motionAdd < 0.0f) {
            motionAdd = 0.0f;
        }
        if (motionAdd >= 130.0f) {
            motionAdd = 130.0f + (motionAdd - 180.0f) * 0.2f;
        }
        final float cameraMotionYaw = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
        cloakRotation += LabyModCore.getMath().sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * cameraMotionYaw;
        if (entitylivingbaseIn.isSneaking()) {
            cloakRotation += 25.0f;
        }
        GlStateManager.rotate(6.0f + motionAdd / 2.0f + cloakRotation, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(motionSub / 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-motionSub / 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        this.cloak.isHidden = false;
        this.cloak.render(scale);
        this.cloak.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setRotationAngles(final ModelCosmetics modelCosmetics, final float movementFactor, final float walkingSpeed, final float tickValue, final float var4, final float var5, final float var6, final Entity entityIn) {
        this.cloak.rotationPointY = (entityIn.isSneaking() ? -1.0f : 0.0f);
    }
    
    @Override
    public int getCosmeticId() {
        return 0;
    }
    
    @Override
    public String getCosmeticName() {
        return "Cloak";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticCloakData extends CosmeticData
    {
        @Override
        public boolean isEnabled() {
            return LabyMod.getSettings().cosmeticsCustomTextures;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
        }
    }
}
