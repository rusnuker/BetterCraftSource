// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.particle;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.block.state.IBlockState;
import com.TominoCZ.FBP.util.FBPRenderUtil;
import com.TominoCZ.FBP.FBP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import javax.vecmath.Vector2f;
import net.minecraft.util.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFlameFX;

public class FBPParticleFlame extends EntityFlameFX
{
    Minecraft mc;
    double startScale;
    double scaleAlpha;
    double prevParticleScale;
    double prevParticleAlpha;
    double endMult;
    boolean spawnAnother;
    Vec3 startPos;
    Vec3[] cube;
    Vector2f par;
    
    protected FBPParticleFlame(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double mX, final double mY, final double mZ, boolean spawnAnother) {
        super(worldIn, xCoordIn, yCoordIn - 0.06, zCoordIn, mX, mY, mZ);
        this.endMult = 1.0;
        this.spawnAnother = true;
        final IBlockState bs = worldIn.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));
        this.spawnAnother = spawnAnother;
        if (bs.getBlock() != Blocks.torch) {
            spawnAnother = false;
        }
        if (bs == Blocks.torch.getDefaultState()) {
            final double n = this.posY + 0.03999999910593033;
            this.posY = n;
            this.prevPosY = n;
        }
        this.startPos = new Vec3(this.posX, this.posY, this.posZ);
        this.mc = Minecraft.getMinecraft();
        this.motionY = -8.500000112690032E-4;
        this.particleGravity = -0.05f;
        this.particleIcon = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.snow.getDefaultState());
        this.particleScale *= (float)(FBP.scaleMult * 2.5);
        this.particleMaxAge = FBP.random.nextInt(3, 5);
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 0.0f;
        final float angleY = this.rand.nextFloat() * 80.0f;
        this.cube = new Vec3[FBP.CUBE.length];
        for (int i = 0; i < FBP.CUBE.length; ++i) {
            final Vec3 vec = FBP.CUBE[i];
            this.cube[i] = FBPRenderUtil.rotatef_d(vec, 0.0f, angleY, 0.0f);
        }
        this.particleAlpha = 1.0f;
        if (FBP.randomFadingSpeed) {
            this.endMult *= FBP.random.nextDouble(0.9875, 1.0);
        }
        this.multipleParticleScaleBy(1.0f);
    }
    
    @Override
    public EntityFX multipleParticleScaleBy(final float scale) {
        final EntityFX p = super.multipleParticleScaleBy(scale);
        this.startScale = this.particleScale;
        this.scaleAlpha = this.particleScale * 0.35;
        final float f = this.particleScale / 80.0f;
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - f, this.posY - f, this.posZ - f, this.posX + f, this.posY + f, this.posZ + f));
        return p;
    }
    
    @Override
    public int getFXLayer() {
        return 0;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevParticleAlpha = this.particleAlpha;
        this.prevParticleScale = this.particleScale;
        if (!FBP.fancyFlame) {
            this.isDead = true;
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
                this.setDead();
            }
            else if (this.particleAlpha <= 0.325 && this.spawnAnother && this.worldObj.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getBlock() == Blocks.torch) {
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
        final double X = x;
        final double Y = y;
        final double Z = z;
        final List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
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
        final AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
        this.posX = (axisalignedbb2.minX + axisalignedbb2.maxX) / 2.0;
        this.posY = (axisalignedbb2.minY + axisalignedbb2.maxY) / 2.0;
        this.posZ = (axisalignedbb2.minZ + axisalignedbb2.maxZ) / 2.0;
        this.onGround = (y != Y);
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (!FBP.isEnabled() && this.particleMaxAge != 0) {
            this.particleMaxAge = 0;
        }
        final float f = this.particleIcon.getInterpolatedU(4.400000095367432);
        final float f2 = this.particleIcon.getInterpolatedV(4.400000095367432);
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
        this.par = new Vector2f(f, f2);
        Tessellator.getInstance().draw();
        this.mc.getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        worldRendererIn.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        worldRendererIn.setTranslation(f3, f4, f5);
        this.putCube(worldRendererIn, f6 / 80.0f, i >> 16 & 0xFFFF, i & 0xFFFF, this.particleRed, this.particleGreen, this.particleBlue, alpha);
        worldRendererIn.setTranslation(0.0, 0.0, 0.0);
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(FBP.LOCATION_PARTICLE_TEXTURE);
        worldRendererIn.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }
    
    public void putCube(final WorldRenderer worldRendererIn, final double scale, final int j, final int k, final float r, final float g, final float b, final float a) {
        float brightnessForRender = 1.0f;
        float R = 0.0f;
        float G = 0.0f;
        float B = 0.0f;
        for (int i = 0; i < this.cube.length; i += 4) {
            final Vec3 v1 = this.cube[i];
            final Vec3 v2 = this.cube[i + 1];
            final Vec3 v3 = this.cube[i + 2];
            final Vec3 v4 = this.cube[i + 3];
            R = r * brightnessForRender;
            G = g * brightnessForRender;
            B = b * brightnessForRender;
            brightnessForRender *= 0.95f;
            this.addVt(worldRendererIn, scale, v1, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v2, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v3, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v4, this.par.x, this.par.y, j, k, R, G, B, a);
        }
    }
    
    private void addVt(final WorldRenderer worldRendererIn, final double scale, final Vec3 pos, final double u, final double v, final int j, final int k, final float r, final float g, final float b, final float a) {
        worldRendererIn.pos(pos.xCoord * scale, pos.yCoord * scale, pos.zCoord * scale).tex(u, v).color(r, g, b, a).lightmap(j, k).endVertex();
    }
    
    @Override
    public int getBrightnessForRender(final float p_189214_1_) {
        final int i = super.getBrightnessForRender(p_189214_1_);
        int j = 0;
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, this.posY, this.posZ))) {
            j = this.worldObj.getCombinedLight(new BlockPos(this.posX, this.posY, this.posZ), 0);
        }
        return (i == 0) ? j : i;
    }
}
