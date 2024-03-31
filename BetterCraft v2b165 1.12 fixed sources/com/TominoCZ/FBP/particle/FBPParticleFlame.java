// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.particle;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.particle.Particle;
import net.minecraft.block.state.IBlockState;
import com.TominoCZ.FBP.util.FBPRenderUtil;
import com.TominoCZ.FBP.FBP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleFlame;

public class FBPParticleFlame extends ParticleFlame
{
    Minecraft mc;
    double startScale;
    double scaleAlpha;
    double prevParticleScale;
    double prevParticleAlpha;
    double endMult;
    boolean spawnAnother;
    Vec3d startPos;
    Vec3d[] cube;
    Vec2f par;
    
    protected FBPParticleFlame(final World worldObjIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double mX, final double mY, final double mZ, boolean spawnAnother) {
        super(worldObjIn, xCoordIn, yCoordIn - 0.06, zCoordIn, mX, mY, mZ);
        this.endMult = 1.0;
        this.spawnAnother = true;
        final IBlockState bs = worldObjIn.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));
        this.spawnAnother = spawnAnother;
        if (bs.getBlock() != Blocks.TORCH) {
            spawnAnother = false;
        }
        if (bs == Blocks.TORCH.getDefaultState()) {
            final double n = this.posY + 0.03999999910593033;
            this.posY = n;
            this.prevPosY = n;
        }
        this.startPos = new Vec3d(this.posX, this.posY, this.posZ);
        this.mc = Minecraft.getMinecraft();
        this.motionY = -8.500000112690032E-4;
        this.particleGravity = -0.05f;
        this.particleTexture = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.SNOW.getDefaultState());
        this.particleScale *= (float)(FBP.scaleMult * 2.5);
        this.particleMaxAge = FBP.random.nextInt(3, 5);
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 0.0f;
        final float angleY = this.rand.nextFloat() * 80.0f;
        this.cube = new Vec3d[FBP.CUBE.length];
        for (int i = 0; i < FBP.CUBE.length; ++i) {
            final Vec3d vec = FBP.CUBE[i];
            this.cube[i] = FBPRenderUtil.rotatef_d(vec, 0.0f, angleY, 0.0f);
        }
        this.particleAlpha = 1.0f;
        if (FBP.randomFadingSpeed) {
            this.endMult *= FBP.random.nextDouble(0.9875, 1.0);
        }
        this.multipleParticleScaleBy(1.0f);
    }
    
    @Override
    public Particle multipleParticleScaleBy(final float scale) {
        final Particle p = super.multipleParticleScaleBy(scale);
        if (!FBP.isEnabled()) {
            return p;
        }
        this.startScale = this.particleScale;
        this.scaleAlpha = this.particleScale * 0.35;
        final float f = this.particleScale / 80.0f;
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - f, this.posY - f, this.posZ - f, this.posX + f, this.posY + f, this.posZ + f));
        return p;
    }
    
    @Override
    public int getFXLayer() {
        if (!FBP.isEnabled()) {
            return super.getFXLayer();
        }
        return 0;
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
        if (!FBP.fancyFlame) {
            this.isExpired = true;
        }
        if (++this.particleAge >= this.particleMaxAge) {
            if (FBP.randomFadingSpeed) {
                this.particleScale *= (float)(0.949999988079071 * this.endMult);
            }
            else {
                this.particleScale *= 0.95f;
            }
            if (this.particleAlpha > 0.01 && this.particleScale <= this.scaleAlpha) {
                if (FBP.randomFadingSpeed) {
                    this.particleAlpha *= (float)(0.8999999761581421 * this.endMult);
                }
                else {
                    this.particleAlpha *= 0.9f;
                }
            }
            if (this.particleAlpha <= 0.01) {
                this.setExpired();
            }
            else if (this.particleAlpha <= 0.325 && this.spawnAnother && this.worldObj.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getBlock() == Blocks.TORCH) {
                this.spawnAnother = false;
                this.mc.effectRenderer.addEffect(new FBPParticleFlame(this.worldObj, this.startPos.xCoord, this.startPos.yCoord, this.startPos.zCoord, 0.0, 0.0, 0.0, this.spawnAnother));
            }
        }
        this.moveEntity(0.0, this.motionY -= 0.02 * this.particleGravity, 0.0);
        this.motionY *= 0.95;
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
    public void renderParticle(final BufferBuilder buf, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (!FBP.isEnabled()) {
            super.renderParticle(buf, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            return;
        }
        if (!FBP.isEnabled() && this.particleMaxAge != 0) {
            this.particleMaxAge = 0;
        }
        final float f = this.particleTexture.getInterpolatedU(4.400000095367432);
        final float f2 = this.particleTexture.getInterpolatedV(4.400000095367432);
        final float f3 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - FBPParticleFlame.interpPosX);
        final float f4 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - FBPParticleFlame.interpPosY);
        final float f5 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - FBPParticleFlame.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final float alpha = (float)(this.prevParticleAlpha + (this.particleAlpha - this.prevParticleAlpha) * partialTicks);
        final float f6 = (float)(this.prevParticleScale + (this.particleScale - this.prevParticleScale) * partialTicks);
        if (this.particleAge >= this.particleMaxAge) {
            this.particleGreen = (float)(f6 / this.startScale);
        }
        GlStateManager.enableCull();
        this.par = new Vec2f(f, f2);
        Tessellator.getInstance().draw();
        this.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buf.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        buf.setTranslation(f3, f4, f5);
        this.putCube(buf, f6 / 80.0f, i >> 16 & 0xFFFF, i & 0xFFFF, this.particleRed, this.particleGreen, this.particleBlue, alpha);
        buf.setTranslation(0.0, 0.0, 0.0);
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(FBP.LOCATION_PARTICLE_TEXTURE);
        buf.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }
    
    public void putCube(final BufferBuilder worldObjRendererIn, final double scale, final int j, final int k, final float r, final float g, final float b, final float a) {
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
            brightnessForRender *= (float)0.95;
            this.addVt(worldObjRendererIn, scale, v1, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldObjRendererIn, scale, v2, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldObjRendererIn, scale, v3, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldObjRendererIn, scale, v4, this.par.x, this.par.y, j, k, R, G, B, a);
        }
    }
    
    private void addVt(final BufferBuilder worldObjRendererIn, final double scale, final Vec3d pos, final double u, final double v, final int j, final int k, final float r, final float g, final float b, final float a) {
        worldObjRendererIn.pos(pos.xCoord * scale, pos.yCoord * scale, pos.zCoord * scale).tex(u, v).color(r, g, b, a).lightmap(j, k).endVertex();
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
