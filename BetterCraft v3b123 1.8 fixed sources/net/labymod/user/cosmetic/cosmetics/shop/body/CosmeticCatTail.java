// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.body;

import java.awt.Color;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.user.emote.keys.PoseAtTime;
import net.labymod.user.emote.EmoteRenderer;
import net.labymod.core.LabyModCore;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.ModTextures;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticCatTail extends CosmeticRenderer<CosmeticCatTailData>
{
    public static final int ID = 3;
    private ModelRenderer tail;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        (this.tail = new ModelRenderer(modelCosmetics).setTextureSize(4, 3).setTextureOffset(0, 0)).addBox(-0.5f, 0.0f, 0.0f, 1, 2, 1, modelSize);
        this.tail.setRotationPoint(0.0f, 11.0f, 2.0f);
        this.tail.rotateAngleX = 0.9424778f;
        ModelRenderer last = this.tail;
        for (int i = 0; i <= 7; ++i) {
            final ModelRenderer part = new ModelRenderer(modelCosmetics).setTextureSize(4, 3);
            part.addBox(-0.5f, 0.0f, 0.0f, 1, 2, 1, modelSize - 0.01f * i);
            part.setRotationPoint(0.0f, 1.6f, 0.0f);
            last.addChild(part);
            last = part;
        }
        this.tail.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.tail.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticCatTailData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        final ModelRenderer tail = this.bindTextureAndColor(cosmeticData.getColor(), ModTextures.COSMETIC_CAT_TAIL, this.tail);
        GlStateManager.pushMatrix();
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0f, 0.1f, -0.15f);
            GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        }
        final float partialTicks = LabyMod.getInstance().getPartialTicks();
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
        try {
            float roll = 0.0f;
            final EmoteRenderer emoteRenderer = LabyMod.getInstance().getEmoteRegistry().getPlayingEmotes().get(entitylivingbaseIn.getUniqueID());
            if (emoteRenderer != null && !emoteRenderer.isAborted() && emoteRenderer.isVisible() && !emoteRenderer.isStream()) {
                final long duration = System.currentTimeMillis() - emoteRenderer.getStartTime();
                float percent = 0.001f * Math.min(duration, 1000L);
                final float fadeOut = (emoteRenderer.getTimeout() == 0L) ? 0.0f : ((float)((duration > emoteRenderer.getResetKeyframeEnd()) ? emoteRenderer.getTimeout() : (emoteRenderer.getResetKeyframeEnd() - duration)));
                final float timeout = (emoteRenderer.getTimeout() == 0L) ? 1.0f : (1.0f / emoteRenderer.getTimeout() * fadeOut);
                percent *= timeout;
                roll = 0.3f * percent;
                boolean isBlockMovement = false;
                PoseAtTime[] emotePosesAtTime;
                for (int length = (emotePosesAtTime = emoteRenderer.getEmotePosesAtTime()).length, i = 0; i < length; ++i) {
                    final PoseAtTime poseAtTime = emotePosesAtTime[i];
                    if (poseAtTime != null && poseAtTime.getPose().isBlockMovement()) {
                        isBlockMovement = true;
                        break;
                    }
                }
                if (!isBlockMovement) {
                    roll = 0.0f;
                }
            }
            int index = 0;
            for (ModelRenderer next = this.tail; next != null && next.childModels != null; next = next.childModels.get(0), ++index) {
                if (next.childModels.size() == 0) {
                    break;
                }
                final float walk = LabyModCore.getMath().cos(movementFactor + index * 0.5f) * 0.03f * index * ((index % 4 != 0) ? -1 : 1);
                if (index >= 0 && index < 2) {
                    next.rotateAngleX = -0.4f + walkingSpeed / (index + 1) + walk + roll / 2.0f;
                }
                else if (index >= 5) {
                    next.rotateAngleX = -walkingSpeed / 2.0f + 0.4f + walk + roll / 2.0f;
                }
                else {
                    next.rotateAngleX = roll;
                }
                if (index == 0) {
                    final ModelRenderer modelRenderer2;
                    final ModelRenderer modelRenderer = modelRenderer2 = next;
                    modelRenderer2.rotateAngleX += (float)(0.85 + roll * 3.0f);
                }
                next.rotateAngleZ = motionSub / 400.0f;
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            GlStateManager.popMatrix();
        }
        tail.isHidden = false;
        tail.render(scale);
        tail.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 3;
    }
    
    @Override
    public String getCosmeticName() {
        return "Cat Tail";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticCatTailData extends CosmeticData
    {
        private Color color;
        
        public CosmeticCatTailData() {
            this.color = Color.WHITE;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) {
            this.color = Color.decode("#" + data[0]);
        }
        
        public Color getColor() {
            return this.color;
        }
    }
}
