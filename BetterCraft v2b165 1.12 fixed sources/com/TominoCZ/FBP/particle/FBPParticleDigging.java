// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.particle;

import net.minecraft.client.particle.IParticleFactory;
import com.TominoCZ.FBP.util.FBPRenderUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import java.util.Iterator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.particle.Particle;
import java.util.List;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import com.TominoCZ.FBP.util.FBPMathUtil;
import net.minecraft.util.math.BlockPos;
import com.TominoCZ.FBP.FBP;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec2f;
import com.TominoCZ.FBP.vector.FBPVector3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;

public class FBPParticleDigging extends ParticleDigging
{
    private final IBlockState sourceState;
    Minecraft mc;
    float prevGravity;
    double startY;
    double scaleAlpha;
    double prevParticleScale;
    double prevParticleAlpha;
    double prevMotionX;
    double prevMotionZ;
    double endMult;
    boolean modeDebounce;
    boolean wasFrozen;
    boolean destroyed;
    boolean killToggle;
    EnumFacing facing;
    FBPVector3d rot;
    FBPVector3d prevRot;
    FBPVector3d rotStep;
    Vec2f[] par;
    static Entity dummyEntity;
    
    static {
        FBPParticleDigging.dummyEntity = new Entity() {
            @Override
            protected void writeEntityToNBT(final NBTTagCompound compound) {
            }
            
            @Override
            protected void readEntityFromNBT(final NBTTagCompound compound) {
            }
            
            @Override
            protected void entityInit() {
            }
        };
    }
    
    protected FBPParticleDigging(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final float scale, final float R, final float G, final float B, final IBlockState state, @Nullable final EnumFacing facing, @Nullable final TextureAtlasSprite texture) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
        this.endMult = 0.75;
        this.particleRed = R;
        this.particleGreen = G;
        this.particleBlue = B;
        this.mc = Minecraft.getMinecraft();
        this.rot = new FBPVector3d();
        this.prevRot = new FBPVector3d();
        this.facing = facing;
        this.createRotationMatrix();
        try {
            FBP.setSourcePos.invokeExact((ParticleDigging)this, new BlockPos(xCoordIn, yCoordIn, zCoordIn));
        }
        catch (final Throwable e1) {
            e1.printStackTrace();
        }
        if (scale > -1.0f) {
            this.particleScale = scale;
        }
        if (scale < -1.0f && facing != null && facing == EnumFacing.UP && FBP.smartBreaking) {
            this.motionX *= 1.5;
            this.motionY *= 0.1;
            this.motionZ *= 1.5;
            final double particleSpeed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            final double x = FBPMathUtil.add(FBPParticleDigging.cameraViewDir.xCoord, 0.01);
            final double z = FBPMathUtil.add(FBPParticleDigging.cameraViewDir.zCoord, 0.01);
            this.motionX = x * particleSpeed;
            this.motionZ = z * particleSpeed;
        }
        if (this.modeDebounce = !FBP.randomRotation) {
            this.rot.zero();
            this.calculateYAngle();
        }
        this.sourceState = state;
        final Block b = state.getBlock();
        this.particleGravity = (float)(b.blockParticleGravity * FBP.gravityMult);
        this.particleScale = (float)(FBP.scaleMult * (FBP.randomizedScale ? this.particleScale : 1.0f));
        this.particleMaxAge = (int)FBP.random.nextDouble(FBP.minAge, FBP.maxAge + 0.5);
        this.scaleAlpha = this.particleScale * 0.82;
        this.destroyed = (facing == null);
        if (texture == null) {
            final BlockModelShapes blockModelShapes = this.mc.getBlockRendererDispatcher().getBlockModelShapes();
            if (!this.destroyed) {
                try {
                    final List<BakedQuad> quads = blockModelShapes.getModelForState(state).getQuads(state, facing, 0L);
                    if (quads != null && !quads.isEmpty()) {
                        this.particleTexture = quads.get(0).getSprite();
                    }
                }
                catch (final Exception ex) {}
            }
            if (this.particleTexture == null || this.particleTexture.getIconName().equals("missingno")) {
                this.setParticleTexture(blockModelShapes.getTexture(state));
            }
        }
        else {
            this.particleTexture = texture;
        }
        if (FBP.randomFadingSpeed) {
            this.endMult = MathHelper.clamp(FBP.random.nextDouble(0.5, 0.9), 0.55, 0.8);
        }
        this.prevGravity = this.particleGravity;
        this.startY = this.posY;
        this.multipleParticleScaleBy(1.0f);
    }
    
    @Override
    public Particle multipleParticleScaleBy(final float scale) {
        final Particle p = super.multipleParticleScaleBy(scale);
        if (!FBP.isEnabled()) {
            return p;
        }
        final float f = this.particleScale / 10.0f;
        if (FBP.restOnFloor && this.destroyed) {
            final double n = this.startY - f;
            this.prevPosY = n;
            this.posY = n;
        }
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - f, this.posY, this.posZ - f, this.posX + f, this.posY + 2.0f * f, this.posZ + f));
        return p;
    }
    
    public Particle MultiplyVelocity(final float multiplier) {
        this.motionX *= multiplier;
        this.motionY = (this.motionY - 0.10000000149011612) * (multiplier / 2.0f) + 0.10000000149011612;
        this.motionZ *= multiplier;
        return this;
    }
    
    @Override
    protected void multiplyColor(@Nullable final BlockPos p_187154_1_) {
        if (this.sourceState.getBlock() == Blocks.GRASS && this.facing != EnumFacing.UP) {
            return;
        }
        final int i = this.mc.getBlockColors().colorMultiplier(this.sourceState, this.worldObj, p_187154_1_, 0);
        this.particleRed *= (i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (i & 0xFF) / 255.0f;
    }
    
    @Override
    public FBPParticleDigging init() {
        if (!FBP.isEnabled()) {
            return this;
        }
        this.multiplyColor(new BlockPos(this.posX, this.posY, this.posZ));
        return this;
    }
    
    @Override
    public FBPParticleDigging setBlockPos(final BlockPos pos) {
        if (!FBP.isEnabled()) {
            return this;
        }
        this.multiplyColor(pos);
        return this;
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void onUpdate() {
        if (!FBP.isEnabled()) {
            super.onUpdate();
            return;
        }
        final boolean allowedToMove = MathHelper.abs((float)this.motionX) > 1.0E-4 || MathHelper.abs((float)this.motionZ) > 1.0E-4;
        if (!FBP.frozen && FBP.bounceOffWalls && !this.mc.isGamePaused() && this.particleAge > 0) {
            if (!this.wasFrozen && allowedToMove) {
                final boolean xCollided = this.prevPosX == this.posX;
                final boolean zCollided = this.prevPosZ == this.posZ;
                if (xCollided) {
                    this.motionX = -this.prevMotionX * 0.625;
                }
                if (zCollided) {
                    this.motionZ = -this.prevMotionZ * 0.625;
                }
                if (!FBP.randomRotation && (xCollided || zCollided)) {
                    this.calculateYAngle();
                }
            }
            else {
                this.wasFrozen = false;
            }
        }
        if (FBP.frozen && FBP.bounceOffWalls && !this.wasFrozen) {
            this.wasFrozen = true;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRot.copyFrom(this.rot);
        this.prevParticleAlpha = this.particleAlpha;
        this.prevParticleScale = this.particleScale;
        if (!this.mc.isGamePaused() && (!FBP.frozen || this.killToggle)) {
            if (!this.killToggle) {
                if (!FBP.randomRotation) {
                    if (!this.modeDebounce) {
                        this.modeDebounce = true;
                        this.rot.z = 0.0;
                        this.calculateYAngle();
                    }
                    if (allowedToMove) {
                        final double x = MathHelper.abs((float)(this.rotStep.x * this.getMult()));
                        if (this.motionX > 0.0) {
                            if (this.motionZ > 0.0) {
                                final FBPVector3d rot = this.rot;
                                rot.x -= x;
                            }
                            else if (this.motionZ < 0.0) {
                                final FBPVector3d rot2 = this.rot;
                                rot2.x += x;
                            }
                        }
                        else if (this.motionX < 0.0) {
                            if (this.motionZ < 0.0) {
                                final FBPVector3d rot3 = this.rot;
                                rot3.x += x;
                            }
                            else if (this.motionZ > 0.0) {
                                final FBPVector3d rot4 = this.rot;
                                rot4.x -= x;
                            }
                        }
                    }
                }
                else {
                    if (this.modeDebounce) {
                        this.modeDebounce = false;
                        this.rot.z = FBP.random.nextDouble(30.0, 400.0);
                    }
                    if (allowedToMove) {
                        this.rot.add(this.rotStep.multiply(this.getMult()));
                    }
                }
            }
            if (!FBP.infiniteDuration) {
                ++this.particleAge;
            }
            if (this.particleAge >= this.particleMaxAge || this.killToggle) {
                this.particleScale *= (float)(0.8876543045043945 * this.endMult);
                if (this.particleAlpha > 0.01 && this.particleScale <= this.scaleAlpha) {
                    this.particleAlpha *= (float)(0.6875200271606445 * this.endMult);
                }
                if (this.particleAlpha <= 0.01) {
                    this.setExpired();
                }
            }
            if (!this.killToggle) {
                if (!this.onGround) {
                    this.motionY -= 0.04 * this.particleGravity;
                }
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                if (this.onGround && FBP.restOnFloor) {
                    this.rot.x = Math.round(this.rot.x / 90.0) * 90.0f;
                    this.rot.z = Math.round(this.rot.z / 90.0) * 90.0f;
                }
                if (MathHelper.abs((float)this.motionX) > 1.0E-5) {
                    this.prevMotionX = this.motionX;
                }
                if (MathHelper.abs((float)this.motionZ) > 1.0E-5) {
                    this.prevMotionZ = this.motionZ;
                }
                if (allowedToMove) {
                    this.motionX *= 0.9800000190734863;
                    this.motionZ *= 0.9800000190734863;
                }
                this.motionY *= 0.9800000190734863;
                if (FBP.entityCollision) {
                    final List<Entity> list = this.worldObj.getEntitiesWithinAABB((Class<? extends Entity>)Entity.class, this.getEntityBoundingBox());
                    for (final Entity entityIn : list) {
                        if (!entityIn.noClip) {
                            double d0 = this.posX - entityIn.posX;
                            double d2 = this.posZ - entityIn.posZ;
                            double d3 = MathHelper.absMax(d0, d2);
                            if (d3 < 0.009999999776482582) {
                                continue;
                            }
                            d3 = Math.sqrt(d3);
                            d0 /= d3;
                            d2 /= d3;
                            double d4 = 1.0 / d3;
                            if (d4 > 1.0) {
                                d4 = 1.0;
                            }
                            this.motionX += d0 * d4 / 20.0;
                            this.motionZ += d2 * d4 / 20.0;
                            if (!FBP.randomRotation) {
                                this.calculateYAngle();
                            }
                            if (FBP.frozen) {
                                continue;
                            }
                            this.onGround = false;
                        }
                    }
                }
                if (FBP.waterPhysics) {
                    if (this.isInWater()) {
                        this.handleWaterMovement();
                        if (FBP.INSTANCE.doesMaterialFloat(this.sourceState.getMaterial())) {
                            this.motionY = 0.11f + this.particleScale / 1.25f * 0.02f;
                        }
                        else {
                            this.motionX *= 0.932515086137662;
                            this.motionZ *= 0.932515086137662;
                            this.particleGravity = 0.35f;
                            this.motionY *= 0.8500000238418579;
                        }
                        if (!FBP.randomRotation) {
                            this.calculateYAngle();
                        }
                        if (this.onGround) {
                            this.onGround = false;
                        }
                    }
                    else {
                        this.particleGravity = this.prevGravity;
                    }
                }
                if (this.onGround) {
                    if (FBP.lowTraction) {
                        this.motionX *= 0.932515086137662;
                        this.motionZ *= 0.932515086137662;
                    }
                    else {
                        this.motionX *= 0.6654999988079071;
                        this.motionZ *= 0.6654999988079071;
                    }
                }
            }
        }
    }
    
    public boolean isInWater() {
        final double scale = this.particleScale / 20.0f;
        final int minX = MathHelper.floor(this.posX - scale);
        final int maxX = MathHelper.ceil(this.posX + scale);
        final int minY = MathHelper.floor(this.posY - scale);
        final int maxY = MathHelper.ceil(this.posY + scale);
        final int minZ = MathHelper.floor(this.posZ - scale);
        final int maxZ = MathHelper.ceil(this.posZ + scale);
        if (this.worldObj.isAreaLoaded(new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ), true)) {
            for (int x = minX; x < maxX; ++x) {
                for (int y = minY; y < maxY; ++y) {
                    for (int z = minZ; z < maxZ; ++z) {
                        final IBlockState block = this.worldObj.getBlockState(new BlockPos(x, y, z));
                        if (block.getMaterial() == Material.WATER) {
                            final double d0 = y + 1 - BlockLiquid.getLiquidHeightPercent(block.getValue((IProperty<Integer>)BlockLiquid.LEVEL));
                            if (this.posY <= d0) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void handleWaterMovement() {
        FBPParticleDigging.dummyEntity.motionX = this.motionX;
        FBPParticleDigging.dummyEntity.motionY = this.motionY;
        FBPParticleDigging.dummyEntity.motionZ = this.motionZ;
        final double scale = this.particleScale / 20.0f;
        if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0, -0.4000000059604645, 0.0).contract(0.001), Material.WATER, FBPParticleDigging.dummyEntity)) {
            this.motionX = FBPParticleDigging.dummyEntity.motionX;
            this.motionY = FBPParticleDigging.dummyEntity.motionY;
            this.motionZ = FBPParticleDigging.dummyEntity.motionZ;
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
        if (!FBP.lowTraction && !FBP.bounceOffWalls) {
            if (x != X) {
                this.motionX *= 0.699999988079071;
            }
            if (z != Z) {
                this.motionZ *= 0.699999988079071;
            }
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
        final float f5 = (float)(this.prevParticleScale + (this.particleScale - this.prevParticleScale) * partialTicks);
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
        final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - FBPParticleDigging.interpPosX);
        float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - FBPParticleDigging.interpPosY);
        final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - FBPParticleDigging.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        this.par = new Vec2f[] { new Vec2f(f2, f4), new Vec2f(f2, f3), new Vec2f(f, f3), new Vec2f(f, f4) };
        final float alpha = (float)(this.prevParticleAlpha + (this.particleAlpha - this.prevParticleAlpha) * partialTicks);
        if (FBP.restOnFloor) {
            f7 += f5 / 10.0f;
        }
        final FBPVector3d smoothRot = new FBPVector3d(0.0, 0.0, 0.0);
        if (FBP.rotationMult > 0.0) {
            smoothRot.y = this.rot.y;
            smoothRot.z = this.rot.z;
            if (!FBP.randomRotation) {
                smoothRot.x = this.rot.x;
            }
            if (!FBP.frozen) {
                final FBPVector3d vec = this.rot.partialVec(this.prevRot, partialTicks);
                if (FBP.randomRotation) {
                    smoothRot.y = vec.y;
                    smoothRot.z = vec.z;
                }
                else {
                    smoothRot.x = vec.x;
                }
            }
        }
        final int j = i >> 16 & 0xFFFF;
        final int k = i & 0xFFFF;
        final float[] arr = { f2, f4, f2, f3, f, f3, f, f4 };
        FBPRenderUtil.renderCubeShaded_S(buf, this.par, f6, f7, f8, f5 / 10.0f, smoothRot, i >> 16 & 0xFFFF, i & 0xFFFF, this.particleRed, this.particleGreen, this.particleBlue, alpha, FBP.cartoonMode);
    }
    
    private void createRotationMatrix() {
        final double rx0 = FBP.random.nextDouble();
        final double ry0 = FBP.random.nextDouble();
        final double rz0 = FBP.random.nextDouble();
        this.rotStep = new FBPVector3d((rx0 > 0.5) ? 1 : -1, (ry0 > 0.5) ? 1 : -1, (rz0 > 0.5) ? 1 : -1);
        this.rot.copyFrom(this.rotStep);
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        final AxisAlignedBB box = this.getEntityBoundingBox();
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0, this.posZ))) {
            final double d0 = (box.maxY - box.minY) * 0.66;
            final double k = this.posY + d0 + 0.01 - (FBP.restOnFloor ? (this.particleScale / 10.0f) : 0.0f);
            return this.worldObj.getCombinedLight(new BlockPos(this.posX, k, this.posZ), 0);
        }
        return 0;
    }
    
    private void calculateYAngle() {
        final double angleSin = Math.toDegrees(Math.asin(this.motionX / Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)));
        if (this.motionX > 0.0) {
            if (this.motionZ > 0.0) {
                this.rot.y = -angleSin;
            }
            else {
                this.rot.y = angleSin;
            }
        }
        else if (this.motionZ > 0.0) {
            this.rot.y = -angleSin;
        }
        else {
            this.rot.y = angleSin;
        }
    }
    
    double getMult() {
        return Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * (FBP.randomRotation ? 200 : 500) * FBP.rotationMult;
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new FBPParticleDigging(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, -1.0f, 1.0f, 1.0f, 1.0f, Block.getStateById(p_178902_15_[0]), null, null).init();
        }
    }
}
