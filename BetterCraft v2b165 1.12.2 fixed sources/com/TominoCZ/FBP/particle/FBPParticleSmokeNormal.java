// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.particle;

import net.minecraft.client.renderer.BufferBuilder;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.particle.Particle;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import com.TominoCZ.FBP.util.FBPRenderUtil;
import com.TominoCZ.FBP.FBP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSmokeNormal;

public class FBPParticleSmokeNormal extends ParticleSmokeNormal
{
    Minecraft mc;
    double startScale;
    double scaleAlpha;
    double prevParticleScale;
    double prevParticleAlpha;
    double endMult;
    Vec3d[] cube;
    Vec2f par;
    ParticleSmokeNormal original;
    
    protected FBPParticleSmokeNormal(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double mX, final double mY, final double mZ, final float scale, final boolean b, final TextureAtlasSprite tex, final ParticleSmokeNormal original) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, mX, mY, mZ, scale);
        this.endMult = 0.75;
        this.original = original;
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.mc = Minecraft.getMinecraft();
        this.particleTexture = tex;
        this.scaleAlpha = this.particleScale * 0.85;
        final Block block = worldIn.getBlockState(new BlockPos(xCoordIn, yCoordIn, zCoordIn)).getBlock();
        if (block == Blocks.FIRE) {
            this.particleScale *= 0.65f;
            this.particleGravity *= 0.25f;
            this.motionX = FBP.random.nextDouble(-0.05, 0.05);
            this.motionY = FBP.random.nextDouble() * 0.5;
            this.motionZ = FBP.random.nextDouble(-0.05, 0.05);
            this.motionY *= 0.3499999940395355;
            this.scaleAlpha = this.particleScale * 0.5;
            this.particleMaxAge = FBP.random.nextInt(7, 18);
        }
        else if (block == Blocks.TORCH) {
            this.particleScale *= 0.45f;
            this.motionX = FBP.random.nextDouble(-0.05, 0.05);
            this.motionY = FBP.random.nextDouble() * 0.5;
            this.motionZ = FBP.random.nextDouble(-0.05, 0.05);
            this.motionX *= 0.925000011920929;
            this.motionY = 0.004999999888241291;
            this.motionZ *= 0.925000011920929;
            this.particleRed = 0.275f;
            this.particleGreen = 0.275f;
            this.particleBlue = 0.275f;
            this.scaleAlpha = this.particleScale * 0.75;
            this.particleMaxAge = FBP.random.nextInt(5, 10);
        }
        else {
            this.particleScale = scale;
            this.motionY *= 0.935;
        }
        this.particleScale *= (float)FBP.scaleMult;
        this.startScale = this.particleScale;
        final float angleY = this.rand.nextFloat() * 80.0f;
        this.cube = new Vec3d[FBP.CUBE.length];
        for (int i = 0; i < FBP.CUBE.length; ++i) {
            final Vec3d vec = FBP.CUBE[i];
            this.cube[i] = FBPRenderUtil.rotatef_d(vec, 0.0f, angleY, 0.0f);
        }
        this.particleAlpha = 1.0f;
        if (FBP.randomFadingSpeed) {
            this.endMult = MathHelper.clamp(FBP.random.nextDouble(0.425, 1.15), 0.5432, 1.0);
        }
        this.multipleParticleScaleBy(1.0f);
    }
    
    @Override
    public Particle multipleParticleScaleBy(final float scale) {
        final Particle p = super.multipleParticleScaleBy(scale);
        if (!FBP.isEnabled()) {
            return p;
        }
        final float f = this.particleScale / 20.0f;
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - f, this.posY - f, this.posZ - f, this.posX + f, this.posY + f, this.posZ + f));
        return p;
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
        if (!FBP.fancySmoke) {
            this.isExpired = true;
        }
        if (++this.particleAge >= this.particleMaxAge) {
            if (FBP.randomFadingSpeed) {
                this.particleScale *= (float)(0.8876543045043945 * this.endMult);
            }
            else {
                this.particleScale *= 0.8876543f;
            }
            if (this.particleAlpha > 0.01 && this.particleScale <= this.scaleAlpha) {
                if (FBP.randomFadingSpeed) {
                    this.particleAlpha *= (float)(0.7654321193695068 * this.endMult);
                }
                else {
                    this.particleAlpha *= 0.7654321f;
                }
            }
            if (this.particleAlpha <= 0.01) {
                this.setExpired();
            }
        }
        this.motionY += 0.004;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= 0.9599999785423279;
        this.motionY *= 0.9599999785423279;
        this.motionZ *= 0.9599999785423279;
        if (this.onGround) {
            this.motionX *= 0.899999988079071;
            this.motionZ *= 0.899999988079071;
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
        this.onGround = (y != Y);
    }
    
    @Override
    protected void resetPositionToBB() {
        if (!FBP.isEnabled()) {
            super.resetPositionToBB();
            return;
        }
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0;
        this.posY = (axisalignedbb.minY + axisalignedbb.maxY) / 2.0;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0;
    }
    
    @Override
    public void renderParticle(final BufferBuilder worldRendererIn, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (!FBP.isEnabled()) {
            super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            return;
        }
        if (!FBP.isEnabled() && this.particleMaxAge != 0) {
            this.particleMaxAge = 0;
        }
        final float f = this.particleTexture.getInterpolatedU(4.400000095367432);
        final float f2 = this.particleTexture.getInterpolatedV(4.400000095367432);
        final float f3 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - FBPParticleSmokeNormal.interpPosX);
        final float f4 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - FBPParticleSmokeNormal.interpPosY);
        final float f5 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - FBPParticleSmokeNormal.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final float alpha = (float)(this.prevParticleAlpha + (this.particleAlpha - this.prevParticleAlpha) * partialTicks);
        final float f6 = (float)(this.prevParticleScale + (this.particleScale - this.prevParticleScale) * partialTicks);
        this.par = new Vec2f(f, f2);
        worldRendererIn.setTranslation(f3, f4, f5);
        this.putCube(worldRendererIn, f6 / 20.0f, i >> 16 & 0xFFFF, i & 0xFFFF, this.particleRed, this.particleGreen, this.particleBlue, alpha);
        worldRendererIn.setTranslation(0.0, 0.0, 0.0);
    }
    
    public void putCube(final BufferBuilder worldRendererIn, final double scale, final int j, final int k, final float r, final float g, final float b, final float a) {
        float brightnessForRender = 1.0f;
        float R = 0.0f;
        float G = 0.0f;
        float B = 0.0f;
        for (int i = 0; i < this.cube.length; i += 4) {
            final Vec3d v1 = this.cube[i];
            final Vec3d v2 = this.cube[i + 1];
            final Vec3d v3 = this.cube[i + 2];
            final Vec3d v4 = this.cube[i + 3];
            R = r * brightnessForRender;
            G = g * brightnessForRender;
            B = b * brightnessForRender;
            brightnessForRender *= 0.875;
            this.addVt(worldRendererIn, scale, v1, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v2, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v3, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v4, this.par.x, this.par.y, j, k, R, G, B, a);
        }
    }
    
    private void addVt(final BufferBuilder worldRendererIn, final double scale, final Vec3d pos, final double u, final double v, final int j, final int k, final float r, final float g, final float b, final float a) {
        worldRendererIn.pos(pos.xCoord * scale, pos.yCoord * scale, pos.zCoord * scale).tex(u, v).color(r, g, b, a).lightmap(j, k).endVertex();
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
    
    @Override
    public void setExpired() {
        if (!FBP.isEnabled()) {
            super.setExpired();
            return;
        }
        this.isExpired = true;
        this.original.setExpired();
    }
}
