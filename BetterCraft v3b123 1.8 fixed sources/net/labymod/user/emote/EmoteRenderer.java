// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.emote;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelBiped;
import net.labymod.core.LabyModCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.labymod.main.LabyMod;
import net.labymod.user.emote.keys.EmotePose;
import net.labymod.main.Source;
import net.labymod.user.emote.keys.EmoteBodyPart;
import net.labymod.user.emote.keys.PoseAtTime;
import net.labymod.user.emote.keys.provider.EmoteProvider;
import java.util.UUID;

public class EmoteRenderer
{
    private UUID uuid;
    private short emoteId;
    private long timeout;
    private boolean stream;
    private EmoteProvider emoteProvider;
    private long startTime;
    private PoseAtTime[] emotePosesAtTime;
    private boolean aborted;
    private long resetKeyframeEnd;
    private EmoteBodyPart[] bodyParts;
    private float fadedYaw;
    private float fadedPitch;
    private boolean visible;
    private boolean mc18;
    
    public EmoteRenderer(final UUID uuid, final short emoteId, final long timeout, final boolean stream, final EmoteProvider emoteProvider, final EmoteRenderer prevRenderer) {
        this.startTime = System.currentTimeMillis();
        this.emotePosesAtTime = new PoseAtTime[7];
        this.aborted = false;
        this.resetKeyframeEnd = -1L;
        this.visible = true;
        this.mc18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
        this.uuid = uuid;
        this.emoteId = emoteId;
        this.timeout = timeout;
        this.stream = stream;
        this.emoteProvider = emoteProvider;
        this.bodyParts = new EmoteBodyPart[7];
        for (int i = 0; i < this.bodyParts.length; ++i) {
            this.bodyParts[i] = new EmoteBodyPart(i);
        }
        if (prevRenderer != null) {
            this.bodyParts = prevRenderer.getBodyParts();
            EmoteBodyPart[] bodyParts;
            for (int length = (bodyParts = this.bodyParts).length, j = 0; j < length; ++j) {
                final EmoteBodyPart bodyPart = bodyParts[j];
                bodyPart.cancel();
            }
        }
    }
    
    public void checkForNextFrame() {
        final long currentTick = System.currentTimeMillis() - this.startTime;
        if (this.emoteProvider.isWaiting()) {
            return;
        }
        boolean shouldReset = true;
        if (this.resetKeyframeEnd == -1L) {
            for (int bodyPartId = 0; bodyPartId < this.emotePosesAtTime.length; ++bodyPartId) {
                final PoseAtTime emotePose = this.emotePosesAtTime[bodyPartId];
                if ((emotePose != null && emotePose.getOffset() >= currentTick) || this.emoteProvider.hasNext(bodyPartId)) {
                    shouldReset = false;
                }
                if ((emotePose == null || emotePose.getOffset() < currentTick) && this.emoteProvider.hasNext(bodyPartId)) {
                    final PoseAtTime nextPose = this.emoteProvider.next(bodyPartId);
                    this.bindNextPose(this.emotePosesAtTime[bodyPartId] = nextPose);
                }
            }
        }
        if (shouldReset) {
            if (this.resetKeyframeEnd == -1L) {
                this.resetKeyframeEnd = currentTick + this.timeout;
                for (int bodyPartId = 0; bodyPartId < this.emotePosesAtTime.length; ++bodyPartId) {
                    final EmotePose pose = new EmotePose(bodyPartId, 0.0f, 0.0f, 0.0f, false);
                    EmoteBodyPart[] bodyParts;
                    for (int length = (bodyParts = this.bodyParts).length, i = 0; i < length; ++i) {
                        final EmoteBodyPart bodyPart = bodyParts[i];
                        if (bodyPart.getId() == pose.getBodyPart()) {
                            bodyPart.cancel();
                            bodyPart.applyPose(pose, this.timeout);
                        }
                    }
                }
            }
            else if (this.resetKeyframeEnd < currentTick && !this.aborted) {
                this.aborted = true;
                if (this.aborted) {
                    LabyMod.getInstance().getEmoteRegistry().setCleanPlayingMap(true);
                }
            }
        }
    }
    
    public void bindNextPose(final PoseAtTime poseAtTime) {
        EmoteBodyPart[] bodyParts;
        for (int length = (bodyParts = this.bodyParts).length, i = 0; i < length; ++i) {
            final EmoteBodyPart bodyPart = bodyParts[i];
            if (bodyPart.getId() == poseAtTime.getPose().getBodyPart()) {
                bodyPart.applyPose(poseAtTime.getPose(), poseAtTime.isAnimate() ? poseAtTime.getOffset() : 0L);
            }
        }
    }
    
    public void animate() {
        EmoteBodyPart[] bodyParts;
        for (int length = (bodyParts = this.bodyParts).length, i = 0; i < length; ++i) {
            final EmoteBodyPart bodyPart = bodyParts[i];
            bodyPart.animateOnTime();
        }
    }
    
    public void transformEntity(final Entity entity, final boolean firstPerson, final float yaw, final float pitch) {
        if (this.aborted || !this.visible) {
            return;
        }
        if (!this.mc18 && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            this.checkForNextFrame();
            this.animate();
        }
        this.fadedYaw = yaw;
        this.fadedPitch = pitch;
        EmoteBodyPart[] bodyParts;
        for (int length = (bodyParts = this.bodyParts).length, i = 0; i < length; ++i) {
            final EmoteBodyPart bodyPart = bodyParts[i];
            PoseAtTime[] emotePosesAtTime;
            for (int length2 = (emotePosesAtTime = this.emotePosesAtTime).length, j = 0; j < length2; ++j) {
                final PoseAtTime pose = emotePosesAtTime[j];
                if (pose != null && pose.getPose().getBodyPart() == bodyPart.getId()) {
                    switch (bodyPart.getId()) {
                        case 0: {
                            final float emoteYaw = bodyPart.getX() * 57.295776f / 2.0f;
                            final float emotePitch = bodyPart.getY() * 57.295776f / 2.0f;
                            if (this.stream || this.timeout == 0L) {
                                this.fadedYaw = emoteYaw;
                                this.fadedPitch = emotePitch;
                                break;
                            }
                            final long currentTick = System.currentTimeMillis() - this.startTime;
                            final long keyframeEnd = this.resetKeyframeEnd;
                            final float maxFade = (float)this.timeout;
                            final float fadeIn = maxFade - ((currentTick > maxFade) ? maxFade : ((float)currentTick));
                            final float fadeOut = (float)(this.timeout - ((currentTick > keyframeEnd) ? 0L : (keyframeEnd - currentTick)));
                            float fade = fadeIn;
                            if (keyframeEnd != -1L) {
                                fade = fadeOut;
                            }
                            final float diff = (yaw - emoteYaw + 180.0f) % 360.0f - 180.0f;
                            final float rangeYaw = (diff < -180.0f) ? (diff + 360.0f) : diff;
                            this.fadedYaw = emoteYaw + rangeYaw / maxFade * fade;
                            final float rangePitch = pitch - emotePitch;
                            this.fadedPitch = emotePitch + rangePitch / maxFade * fade;
                            break;
                        }
                        case 5: {
                            GlStateManager.translate(0.0f, firstPerson ? 1.0f : 0.4f, 0.0f);
                            final int modifier = firstPerson ? -1 : 1;
                            GlStateManager.rotate(bodyPart.getX() * 57.295776f * modifier, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(bodyPart.getY() * 57.295776f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate(bodyPart.getZ() * 57.295776f, 0.0f, 0.0f, 1.0f);
                            GlStateManager.translate(0.0f, firstPerson ? -1.0f : -0.4f, 0.0f);
                            break;
                        }
                        case 6: {
                            GlStateManager.translate(bodyPart.getX() / 10.0, bodyPart.getY() / 10.0, bodyPart.getZ() / 10.0);
                            break;
                        }
                    }
                }
            }
        }
        if (Math.abs(entity.prevPosX - entity.posX) > 0.04 || Math.abs(entity.prevPosY - entity.posY) > 0.04 || Math.abs(entity.prevPosZ - entity.posZ) > 0.04) {
            PoseAtTime[] emotePosesAtTime2;
            for (int length3 = (emotePosesAtTime2 = this.emotePosesAtTime).length, k = 0; k < length3; ++k) {
                final PoseAtTime poseAtTime = emotePosesAtTime2[k];
                if (poseAtTime != null && poseAtTime.getPose().isBlockMovement()) {
                    this.abort();
                    break;
                }
            }
        }
        if (entity instanceof EntityPlayer && ((EntityPlayer)entity).hurtTime != 0) {
            this.abort();
        }
        if (LabyModCore.getMinecraft().isElytraFlying(entity)) {
            this.abort();
        }
    }
    
    public void transformModel(final ModelBiped model) {
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            this.checkForNextFrame();
            this.animate();
            Minecraft.getMinecraft().renderGlobal.setDisplayListEntitiesDirty();
        }
        if (model == null || this.aborted || !this.visible) {
            return;
        }
        EmoteBodyPart[] bodyParts;
        for (int length = (bodyParts = this.bodyParts).length, i = 0; i < length; ++i) {
            final EmoteBodyPart bodyPart = bodyParts[i];
            PoseAtTime[] emotePosesAtTime;
            for (int length2 = (emotePosesAtTime = this.emotePosesAtTime).length, j = 0; j < length2; ++j) {
                final PoseAtTime pose = emotePosesAtTime[j];
                if (pose != null && pose.getPose().getBodyPart() == bodyPart.getId()) {
                    switch (bodyPart.getId()) {
                        case 0: {
                            model.bipedHead.rotateAngleY = this.getFadedYaw() / 57.295776f;
                            model.bipedHead.rotateAngleX = this.getFadedPitch() / 57.295776f;
                            break;
                        }
                        case 1: {
                            model.bipedRightArm.rotateAngleX = bodyPart.getX();
                            model.bipedRightArm.rotateAngleY = bodyPart.getY();
                            model.bipedRightArm.rotateAngleZ = bodyPart.getZ();
                            break;
                        }
                        case 2: {
                            model.bipedLeftArm.rotateAngleX = bodyPart.getX();
                            model.bipedLeftArm.rotateAngleY = bodyPart.getY();
                            model.bipedLeftArm.rotateAngleZ = bodyPart.getZ();
                            break;
                        }
                        case 3: {
                            model.bipedLeftLeg.rotateAngleX = bodyPart.getX();
                            model.bipedLeftLeg.rotateAngleY = bodyPart.getY();
                            model.bipedLeftLeg.rotateAngleZ = bodyPart.getZ();
                            break;
                        }
                        case 4: {
                            model.bipedRightLeg.rotateAngleX = bodyPart.getX();
                            model.bipedRightLeg.rotateAngleY = bodyPart.getY();
                            model.bipedRightLeg.rotateAngleZ = bodyPart.getZ();
                            break;
                        }
                    }
                }
                if (model instanceof ModelPlayer) {
                    copyToSecondLayer((ModelPlayer)model);
                }
            }
        }
    }
    
    public static void resetModel(final ModelBiped model) {
        resetModelRenderer(model.bipedRightArm);
        resetModelRenderer(model.bipedLeftArm);
        resetModelRenderer(model.bipedLeftLeg);
        resetModelRenderer(model.bipedRightLeg);
        resetModelRenderer(model.bipedBody);
        if (model instanceof ModelPlayer) {
            copyToSecondLayer((ModelPlayer)model);
        }
    }
    
    private static void resetModelRenderer(final ModelRenderer modelRenderer) {
        modelRenderer.rotateAngleX = 0.0f;
        modelRenderer.rotateAngleY = 0.0f;
        modelRenderer.rotateAngleZ = 0.0f;
    }
    
    private static void copyToSecondLayer(final ModelPlayer model) {
        ModelBase.copyModelAngles(model.bipedHead, model.bipedHeadwear);
        ModelBase.copyModelAngles(model.bipedBody, model.bipedBodyWear);
        ModelBase.copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
        ModelBase.copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
        ModelBase.copyModelAngles(model.bipedLeftLeg, model.bipedLeftLegwear);
        ModelBase.copyModelAngles(model.bipedRightLeg, model.bipedRightLegwear);
    }
    
    public void abort() {
        EmoteBodyPart[] bodyParts;
        for (int length = (bodyParts = this.bodyParts).length, i = 0; i < length; ++i) {
            final EmoteBodyPart bodyPart = bodyParts[i];
            bodyPart.cancel();
        }
        this.emoteProvider.clear();
        this.resetKeyframeEnd = 0L;
        this.checkForNextFrame();
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public short getEmoteId() {
        return this.emoteId;
    }
    
    public long getTimeout() {
        return this.timeout;
    }
    
    public boolean isStream() {
        return this.stream;
    }
    
    public EmoteProvider getEmoteProvider() {
        return this.emoteProvider;
    }
    
    public long getStartTime() {
        return this.startTime;
    }
    
    public PoseAtTime[] getEmotePosesAtTime() {
        return this.emotePosesAtTime;
    }
    
    public boolean isAborted() {
        return this.aborted;
    }
    
    public long getResetKeyframeEnd() {
        return this.resetKeyframeEnd;
    }
    
    public EmoteBodyPart[] getBodyParts() {
        return this.bodyParts;
    }
    
    public float getFadedYaw() {
        return this.fadedYaw;
    }
    
    public float getFadedPitch() {
        return this.fadedPitch;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public boolean isMc18() {
        return this.mc18;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
