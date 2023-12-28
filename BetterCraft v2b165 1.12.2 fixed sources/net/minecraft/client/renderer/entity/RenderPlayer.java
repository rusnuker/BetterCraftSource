// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.item.EnumAction;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.renderer.GlStateManager;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticCape;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticCap;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticBandana;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticNerdGlasses;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticVillagerNose;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticVexWings;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticSlimeGel;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticSusanno;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticGuardianSpikes;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticEnderCrystal;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticBatWings;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticSnoxhEyes;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticHeadset;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticCrystalWings;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticEnchantingGlint;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticDevilWings;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticBeeWings;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticDragonWings;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticBcCape2;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticBcCape1;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticDevilHorns;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticCrownKing;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticTopHat;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticHalo;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticSixPath;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticWitherArmor;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticBlaze;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticGalaxySkin;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticCreeperLightning;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticWitchHat;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;

public class RenderPlayer extends RenderLivingBase<AbstractClientPlayer>
{
    private final boolean smallArms;
    
    public RenderPlayer(final RenderManager renderManager) {
        this(renderManager, false);
    }
    
    public RenderPlayer(final RenderManager renderManager, final boolean useSmallArms) {
        super(renderManager, new ModelPlayer(0.0f, useSmallArms), 0.5f);
        this.smallArms = useSmallArms;
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerBipedArmor(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerArrow(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerDeadmau5Head(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerCape(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerElytra(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerEntityOnShoulder(renderManager));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticWitchHat(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticCreeperLightning(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticGalaxySkin(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticBlaze(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticWitherArmor(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticSixPath(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticHalo(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticTopHat(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticCrownKing(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticDevilHorns(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticBcCape1(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticBcCape2(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticDragonWings(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticBeeWings(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticDevilWings(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticEnchantingGlint(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticCrystalWings(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticHeadset(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticSnoxhEyes(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticBatWings(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticEnderCrystal(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticGuardianSpikes(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticSusanno(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticSlimeGel(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticVexWings(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticVillagerNose(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticNerdGlasses(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticBandana(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticCap(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new CosmeticCape(this));
    }
    
    @Override
    public ModelPlayer getMainModel() {
        return (ModelPlayer)super.getMainModel();
    }
    
    @Override
    public void doRender(final AbstractClientPlayer entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!entity.isUser() || this.renderManager.renderViewEntity == entity) {
            double d0 = y;
            if (entity.isSneaking()) {
                d0 = y - 0.125;
            }
            this.setModelVisibilities(entity);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            super.doRender(entity, x, d0, z, entityYaw, partialTicks);
            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        }
    }
    
    private void setModelVisibilities(final AbstractClientPlayer clientPlayer) {
        final ModelPlayer modelplayer = this.getMainModel();
        if (clientPlayer.isSpectator()) {
            modelplayer.setInvisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        }
        else {
            final ItemStack itemstack = clientPlayer.getHeldItemMainhand();
            final ItemStack itemstack2 = clientPlayer.getHeldItemOffhand();
            modelplayer.setInvisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.isSneak = clientPlayer.isSneaking();
            ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
            ModelBiped.ArmPose modelbiped$armpose2 = ModelBiped.ArmPose.EMPTY;
            if (!itemstack.func_190926_b()) {
                modelbiped$armpose = ModelBiped.ArmPose.ITEM;
                if (clientPlayer.getItemInUseCount() > 0) {
                    final EnumAction enumaction = itemstack.getItemUseAction();
                    if (enumaction == EnumAction.BLOCK) {
                        modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                    }
                    else if (enumaction == EnumAction.BOW) {
                        modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                    }
                }
            }
            if (!itemstack2.func_190926_b()) {
                modelbiped$armpose2 = ModelBiped.ArmPose.ITEM;
                if (clientPlayer.getItemInUseCount() > 0) {
                    final EnumAction enumaction2 = itemstack2.getItemUseAction();
                    if (enumaction2 == EnumAction.BLOCK) {
                        modelbiped$armpose2 = ModelBiped.ArmPose.BLOCK;
                    }
                }
            }
            if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
                modelplayer.rightArmPose = modelbiped$armpose;
                modelplayer.leftArmPose = modelbiped$armpose2;
            }
            else {
                modelplayer.rightArmPose = modelbiped$armpose2;
                modelplayer.leftArmPose = modelbiped$armpose;
            }
        }
    }
    
    public ResourceLocation getEntityTexture(final AbstractClientPlayer entity) {
        return entity.getLocationSkin();
    }
    
    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }
    
    @Override
    protected void preRenderCallback(final AbstractClientPlayer entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
    @Override
    protected void renderEntityName(final AbstractClientPlayer entityIn, final double x, double y, final double z, final String name, final double distanceSq) {
        if (distanceSq < 100.0) {
            final Scoreboard scoreboard = entityIn.getWorldScoreboard();
            final ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
            if (scoreobjective != null) {
                final Score score = scoreboard.getOrCreateScore(entityIn.getName(), scoreobjective);
                this.renderLivingLabel(entityIn, String.valueOf(score.getScorePoints()) + " " + scoreobjective.getDisplayName(), x, y, z, 64);
                y += this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15f * 0.025f;
            }
        }
        super.renderEntityName(entityIn, x, y, z, name, distanceSq);
    }
    
    public void renderRightArm(final AbstractClientPlayer clientPlayer) {
        final float f = 1.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final float f2 = 0.0625f;
        final ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.swingProgress = 0.0f;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer);
        modelplayer.bipedRightArm.rotateAngleX = 0.0f;
        modelplayer.bipedRightArm.render(0.0625f);
        modelplayer.bipedRightArmwear.rotateAngleX = 0.0f;
        modelplayer.bipedRightArmwear.render(0.0625f);
        GlStateManager.disableBlend();
    }
    
    public void renderLeftArm(final AbstractClientPlayer clientPlayer) {
        final float f = 1.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final float f2 = 0.0625f;
        final ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(modelplayer.swingProgress = 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer);
        modelplayer.bipedLeftArm.rotateAngleX = 0.0f;
        modelplayer.bipedLeftArm.render(0.0625f);
        modelplayer.bipedLeftArmwear.rotateAngleX = 0.0f;
        modelplayer.bipedLeftArmwear.render(0.0625f);
        GlStateManager.disableBlend();
    }
    
    @Override
    protected void renderLivingAt(final AbstractClientPlayer entityLivingBaseIn, final double x, final double y, final double z) {
        if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping()) {
            super.renderLivingAt(entityLivingBaseIn, x + entityLivingBaseIn.renderOffsetX, y + entityLivingBaseIn.renderOffsetY, z + entityLivingBaseIn.renderOffsetZ);
        }
        else {
            super.renderLivingAt(entityLivingBaseIn, x, y, z);
        }
    }
    
    @Override
    protected void rotateCorpse(final AbstractClientPlayer entityLiving, final float p_77043_2_, final float p_77043_3_, final float partialTicks) {
        if (entityLiving.isEntityAlive() && entityLiving.isPlayerSleeping()) {
            GlStateManager.rotate(entityLiving.getBedOrientationInDegrees(), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.getDeathMaxRotation(entityLiving), 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
        }
        else if (entityLiving.isElytraFlying()) {
            super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
            final float f = entityLiving.getTicksElytraFlying() + partialTicks;
            final float f2 = MathHelper.clamp(f * f / 100.0f, 0.0f, 1.0f);
            GlStateManager.rotate(f2 * (-90.0f - entityLiving.rotationPitch), 1.0f, 0.0f, 0.0f);
            final Vec3d vec3d = entityLiving.getLook(partialTicks);
            final double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
            final double d2 = vec3d.xCoord * vec3d.xCoord + vec3d.zCoord * vec3d.zCoord;
            if (d0 > 0.0 && d2 > 0.0) {
                final double d3 = (entityLiving.motionX * vec3d.xCoord + entityLiving.motionZ * vec3d.zCoord) / (Math.sqrt(d0) * Math.sqrt(d2));
                final double d4 = entityLiving.motionX * vec3d.zCoord - entityLiving.motionZ * vec3d.xCoord;
                GlStateManager.rotate((float)(Math.signum(d4) * Math.acos(d3)) * 180.0f / 3.1415927f, 0.0f, 1.0f, 0.0f);
            }
        }
        else {
            super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
        }
    }
}
