// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.particle;

import com.TominoCZ.FBP.util.FBPRenderUtil;
import com.TominoCZ.FBP.vector.FBPVector3d;
import net.minecraft.client.renderer.BufferBuilder;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import javax.annotation.Nullable;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import com.TominoCZ.FBP.FBP;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;

public class FBPParticleRain extends ParticleDigging
{
    private final IBlockState sourceState;
    Minecraft mc;
    double AngleY;
    double particleHeight;
    double prevParticleScale;
    double prevParticleHeight;
    double prevParticleAlpha;
    double scalar;
    double endMult;
    Vec2f[] par;
    
    public FBPParticleRain(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final IBlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
        this.scalar = FBP.scaleMult;
        this.endMult = 1.0;
        try {
            FBP.setSourcePos.invokeExact((ParticleDigging)this, new BlockPos(xCoordIn, yCoordIn, zCoordIn));
        }
        catch (final Throwable e1) {
            e1.printStackTrace();
        }
        this.AngleY = FBP.random.nextDouble() * 45.0;
        this.motionX = xSpeedIn;
        this.motionY = -ySpeedIn;
        this.motionZ = zSpeedIn;
        this.particleGravity = 0.025f;
        this.sourceState = state;
        this.mc = Minecraft.getMinecraft();
        this.particleMaxAge = (int)FBP.random.nextDouble(50.0, 70.0);
        this.particleAlpha = 0.0f;
        this.particleScale = 0.0f;
        this.canCollide = true;
        if (FBP.randomFadingSpeed) {
            this.endMult *= FBP.random.nextDouble(0.85, 1.0);
        }
    }
    
    @Override
    public void setParticleTextureIndex(final int particleTextureIndex) {
        if (!FBP.isEnabled()) {
            super.setParticleTextureIndex(particleTextureIndex);
        }
    }
    
    public Particle MultiplyVelocity(final float multiplier) {
        this.motionX *= multiplier;
        this.motionY = (this.motionY - 0.10000000149011612) * (multiplier / 2.0f) + 0.10000000149011612;
        this.motionZ *= multiplier;
        return this;
    }
    
    @Override
    protected void multiplyColor(@Nullable final BlockPos p_187154_1_) {
        if (!FBP.isEnabled()) {
            super.multiplyColor(p_187154_1_);
            return;
        }
        final int i = this.mc.getBlockColors().colorMultiplier(this.sourceState, this.worldObj, p_187154_1_, 0);
        this.particleRed *= (i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (i & 0xFF) / 255.0f;
    }
    
    @Override
    public int getFXLayer() {
        if (!FBP.isEnabled()) {
            return super.getFXLayer();
        }
        return 1;
    }
    
    @Override
    public void onUpdate() {
        if (!FBP.isEnabled()) {
            super.onUpdate();
            return;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevParticleAlpha = this.particleAlpha;
        this.prevParticleScale = this.particleScale;
        this.prevParticleHeight = this.particleHeight;
        if (!this.mc.isGamePaused()) {
            ++this.particleAge;
            if (this.posY < this.mc.player.posY - this.mc.gameSettings.renderDistanceChunks * 9) {
                this.setExpired();
            }
            if (!this.onGround) {
                if (this.particleAge < this.particleMaxAge) {
                    final double max = this.scalar * 0.5;
                    if (this.particleScale < max) {
                        if (FBP.randomFadingSpeed) {
                            this.particleScale += (float)(0.05000000074505806 * this.endMult);
                        }
                        else {
                            this.particleScale += 0.05f;
                        }
                        if (this.particleScale > max) {
                            this.particleScale = (float)max;
                        }
                        this.particleHeight = this.particleScale;
                    }
                    if (this.particleAlpha < 0.65f) {
                        if (FBP.randomFadingSpeed) {
                            this.particleAlpha += (float)(0.08500000089406967 * this.endMult);
                        }
                        else {
                            this.particleAlpha += 0.085f;
                        }
                        if (this.particleAlpha > 0.65f) {
                            this.particleAlpha = 0.65f;
                        }
                    }
                }
                else {
                    this.setExpired();
                }
            }
            if (this.worldObj.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getMaterial().isLiquid()) {
                this.setExpired();
            }
            this.motionY -= 0.04 * this.particleGravity;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionY *= 1.0002500019073486;
            if (this.onGround) {
                this.motionX = 0.0;
                this.motionY = -0.25;
                this.motionZ = 0.0;
                if (this.particleHeight > 0.07500000298023224) {
                    this.particleHeight *= 0.7250000238418579;
                }
                final float max2 = (float)this.scalar * 4.25f;
                if (this.particleScale < max2) {
                    this.particleScale += max2 / 10.0f;
                    if (this.particleScale > max2) {
                        this.particleScale = max2;
                    }
                }
                if (this.particleScale >= max2 / 2.0f) {
                    if (FBP.randomFadingSpeed) {
                        this.particleAlpha *= (float)(0.75 * this.endMult);
                    }
                    else {
                        this.particleAlpha *= 0.75f;
                    }
                    if (this.particleAlpha <= 0.001f) {
                        this.setExpired();
                    }
                }
            }
        }
        final Vec3d rgb = this.mc.world.getSkyColor(this.mc.player, 0.0f);
        this.particleRed = (float)rgb.xCoord;
        this.particleGreen = (float)MathHelper.clamp(rgb.yCoord + 0.25, 0.25, 1.0);
        this.particleBlue = (float)MathHelper.clamp(rgb.zCoord + 0.5, 0.5, 1.0);
        if (this.particleGreen > 1.0f) {
            this.particleGreen = 1.0f;
        }
        if (this.particleBlue > 1.0f) {
            this.particleBlue = 1.0f;
        }
    }
    
    @Override
    public void moveEntity(double x, double y, double z) {
        if (!FBP.isEnabled()) {
            super.moveEntity(x, y, z);
            return;
        }
        final double X = x;
        final double Y = y;
        final double Z = z;
        final List<AxisAlignedBB> list = this.worldObj.getCollisionBoxes(null, this.getEntityBoundingBox().expand(x, y, z));
        for (final AxisAlignedBB axisalignedbb : list) {
            y = axisalignedbb.calculateYOffset(this.getEntityBoundingBox(), y);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
        for (final AxisAlignedBB axisalignedbb : list) {
            x = axisalignedbb.calculateXOffset(this.getEntityBoundingBox(), x);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0, 0.0));
        for (final AxisAlignedBB axisalignedbb : list) {
            z = axisalignedbb.calculateZOffset(this.getEntityBoundingBox(), z);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z));
        this.resetPositionToBB();
        this.onGround = (y != Y && Y < 0.0);
        if (x != X) {
            this.motionX *= 0.699999988079071;
        }
        if (z != Z) {
            this.motionZ *= 0.699999988079071;
        }
    }
    
    @Override
    public void renderParticle(final BufferBuilder buf, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (!FBP.isEnabled()) {
            super.renderParticle(buf, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            return;
        }
        if (!FBP.isEnabled() && this.particleMaxAge != 0) {
            this.particleMaxAge = 0;
        }
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        if (this.particleTexture != null) {
            if (!FBP.cartoonMode) {
                f = this.particleTexture.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
                f3 = this.particleTexture.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            }
            f2 = this.particleTexture.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            f4 = this.particleTexture.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        else {
            f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
            f2 = f + 0.015609375f;
            f3 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
            f4 = f3 + 0.015609375f;
        }
        final float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - FBPParticleRain.interpPosX);
        final float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - FBPParticleRain.interpPosY);
        final float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - FBPParticleRain.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final float alpha = (float)(this.prevParticleAlpha + (this.particleAlpha - this.prevParticleAlpha) * partialTicks);
        final float f8 = (float)(this.prevParticleScale + (this.particleScale - this.prevParticleScale) * partialTicks);
        final float height = (float)(this.prevParticleHeight + (this.particleHeight - this.prevParticleHeight) * partialTicks);
        FBPRenderUtil.renderCubeShaded_WH(buf, this.par = new Vec2f[] { new Vec2f(f2, f4), new Vec2f(f2, f3), new Vec2f(f, f3), new Vec2f(f, f4) }, f5, f6 + height / 10.0f, f7, f8 / 10.0f, height / 10.0f, new FBPVector3d(0.0, this.AngleY, 0.0), i >> 16 & 0xFFFF, i & 0xFFFF, this.particleRed, this.particleGreen, this.particleBlue, alpha, FBP.cartoonMode);
    }
    
    @Override
    public int getBrightnessForRender(final float p_189214_1_) {
        if (!FBP.isEnabled()) {
            return super.getBrightnessForRender(p_189214_1_);
        }
        final int i = super.getBrightnessForRender(p_189214_1_);
        int j = 0;
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, this.posY, this.posZ))) {
            j = this.worldObj.getCombinedLight(new BlockPos(this.posX, this.posY, this.posZ), 0);
        }
        return (i == 0) ? j : i;
    }
}
