// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.labymod.user.User;
import net.minecraft.item.Item;
import net.minecraft.entity.Entity;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerCustomCape implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;
    private CapeParticleRenderer capeParticleRenderer;
    
    public LayerCustomCape(final RenderPlayer playerRendererIn) {
        this.capeParticleRenderer = new CapeParticleRenderer();
        this.playerRenderer = playerRendererIn;
    }
    
    @Override
    public void doRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float var1, final float var2, final float partialTicks, final float var3, final float var4, final float var5, final float scale) {
        if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE)) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final User user = (entitylivingbaseIn == null) ? null : LabyMod.getInstance().getUserManager().getUser(entitylivingbaseIn.getUniqueID());
            if ((user != null && !user.canRenderMojangCape(entitylivingbaseIn)) || entitylivingbaseIn.getLocationCape() == null || LabyModCore.getMinecraft().isWearingElytra(entitylivingbaseIn)) {
                return;
            }
            this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationCape());
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            final double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * partialTicks);
            final double d2 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * partialTicks);
            final double d3 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * partialTicks);
            final float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
            final double d4 = LabyModCore.getMath().sin(f * 3.1415927f / 180.0f);
            final double d5 = -LabyModCore.getMath().cos(f * 3.1415927f / 180.0f);
            float f2 = (float)d2 * 10.0f;
            f2 = LabyModCore.getMath().clamp_float(f2, -6.0f, 32.0f);
            float f3 = (float)(d0 * d4 + d3 * d5) * 100.0f;
            final float f4 = (float)(d0 * d5 - d3 * d4) * 100.0f;
            if (f3 < 0.0f) {
                f3 = 0.0f;
            }
            if (f3 >= 180.0f) {
                f3 = 180.0f + (f3 - 180.0f) * 0.2f;
            }
            final float f5 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
            f2 += LabyModCore.getMath().sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f5;
            if (entitylivingbaseIn.isSneaking()) {
                f3 += 50.0f;
            }
            boolean swap = LabyMod.getSettings().leftHand;
            final ItemStack itemStack = LabyModCore.getMinecraft().getMainHandItem();
            final int itemId = (itemStack != null && itemStack.getItem() != null) ? Item.getIdFromItem(itemStack.getItem()) : 0;
            if (LabyMod.getSettings().swapBow && itemId == 261) {
                swap = !swap;
            }
            if ((swap && LabyModCore.getMinecraft().getItemInUseMaxCount() != 0 && itemId == 261) || (swap && LabyMod.getInstance().isHasLeftHand())) {
                swap = false;
            }
            if (swap) {
                GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            }
            GlStateManager.rotate(6.0f + f3 / 2.0f + f2, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f4 / 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(-f4 / 2.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.113f, 0.085f);
            }
            this.playerRenderer.getMainModel().renderCape(0.0625f);
            if (swap) {
                GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            }
            this.capeParticleRenderer.render(user, entitylivingbaseIn, partialTicks);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
