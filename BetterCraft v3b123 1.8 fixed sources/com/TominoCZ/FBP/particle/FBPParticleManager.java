// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.particle;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.particle.EntityFlameFX;
import com.TominoCZ.FBP.FBP;
import java.lang.reflect.Field;
import com.google.common.base.Throwables;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import com.TominoCZ.FBP.util.FBPReflectionHelper;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import java.lang.invoke.MethodHandles;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import java.lang.invoke.MethodHandle;
import net.minecraft.client.particle.EffectRenderer;

public class FBPParticleManager extends EffectRenderer
{
    private static MethodHandle getBlockDamage;
    private static MethodHandle getParticleScale;
    private static MethodHandle getParticleTexture;
    private static MethodHandle getParticleTypes;
    private static MethodHandle getSourceState;
    private static MethodHandle getParticleMaxAge;
    private static MethodHandle X;
    private static MethodHandle Y;
    private static MethodHandle Z;
    private static MethodHandle mX;
    private static MethodHandle mY;
    private static MethodHandle mZ;
    private static IParticleFactory particleFactory;
    private static IBlockState blockState;
    private static TextureAtlasSprite white;
    private static MethodHandles.Lookup lookup;
    Minecraft mc;
    
    public FBPParticleManager(final World worldIn, final TextureManager rendererIn, final IParticleFactory particleFactory) {
        super(worldIn, rendererIn);
        this.mc = Minecraft.getMinecraft();
        FBPParticleManager.particleFactory = particleFactory;
        FBPParticleManager.white = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.snow.getDefaultState());
        FBPParticleManager.lookup = MethodHandles.publicLookup();
        try {
            FBPParticleManager.getParticleTypes = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(EffectRenderer.class, "particleTypes", "particleTypes"));
            FBPParticleManager.X = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Entity.class, "posX", "posX"));
            FBPParticleManager.Y = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Entity.class, "posY", "posY"));
            FBPParticleManager.Z = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Entity.class, "posZ", "posZ"));
            FBPParticleManager.mX = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Entity.class, "motionX", "motionX"));
            FBPParticleManager.mY = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Entity.class, "motionY", "motionY"));
            FBPParticleManager.mZ = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Entity.class, "motionZ", "motionZ"));
            FBPParticleManager.getParticleScale = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(EntityFX.class, "particleScale", "particleScale"));
            FBPParticleManager.getParticleTexture = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(EntityFX.class, "particleIcon", "particleIcon"));
            FBPParticleManager.getParticleMaxAge = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(EntityFX.class, "particleMaxAge", "particleMaxAge"));
            FBPParticleManager.getSourceState = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(EntityDiggingFX.class, "field_174847_a"));
            FBPParticleManager.getBlockDamage = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(RenderGlobal.class, "damagedBlocks", "damagedBlocks"));
        }
        catch (final Throwable e) {
            throw Throwables.propagate(e);
        }
    }
    
    public void carryOver() {
        if (Minecraft.getMinecraft().effectRenderer == this) {
            return;
        }
        final Field f1 = FBPReflectionHelper.findField(EffectRenderer.class, "fxLayers", "fxLayers");
        final Field f2 = FBPReflectionHelper.findField(EffectRenderer.class, "particleEmitters", "particleEmitters");
        try {
            final MethodHandle getF1 = FBPParticleManager.lookup.unreflectGetter(f1);
            final MethodHandle setF1 = FBPParticleManager.lookup.unreflectSetter(f1);
            final MethodHandle getF2 = FBPParticleManager.lookup.unreflectGetter(f2);
            final MethodHandle setF2 = FBPParticleManager.lookup.unreflectSetter(f2);
            setF1.invokeExact((EffectRenderer)this, getF1.invokeExact(this.mc.effectRenderer));
            setF2.invokeExact((EffectRenderer)this, getF2.invokeExact(this.mc.effectRenderer));
        }
        catch (final Throwable e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void addEffect(final EntityFX efx) {
        Entity toAdd = efx;
        final Entity effect = efx;
        if (toAdd != null && !(toAdd instanceof FBPParticleSnow) && !(toAdd instanceof FBPParticleRain)) {
            if (FBP.fancyFlame && toAdd instanceof EntityFlameFX && !(toAdd instanceof FBPParticleFlame) && Minecraft.getMinecraft().gameSettings.particleSetting < 2) {
                try {
                    toAdd = new FBPParticleFlame(this.worldObj, FBPParticleManager.X.invokeExact(effect), FBPParticleManager.Y.invokeExact(effect), FBPParticleManager.Z.invokeExact(effect), 0.0, FBP.random.nextDouble() * 0.25, 0.0, true);
                    effect.setDead();
                }
                catch (final Throwable t) {
                    t.printStackTrace();
                }
            }
            else if (FBP.fancySmoke && toAdd instanceof EntitySmokeFX && !(toAdd instanceof FBPParticleSmokeNormal) && Minecraft.getMinecraft().gameSettings.particleSetting < 2) {
                final EntitySmokeFX p = (EntitySmokeFX)effect;
                try {
                    toAdd = new FBPParticleSmokeNormal(this.worldObj, FBPParticleManager.X.invokeExact(effect), FBPParticleManager.Y.invokeExact(effect), FBPParticleManager.Z.invokeExact(effect), FBPParticleManager.mX.invokeExact(effect), FBPParticleManager.mY.invokeExact(effect), FBPParticleManager.mZ.invokeExact(effect), FBPParticleManager.getParticleScale.invokeExact((EntityFX)effect), true, FBPParticleManager.white, p);
                }
                catch (final Throwable t2) {
                    t2.printStackTrace();
                }
            }
            else if (FBP.fancyRain && toAdd instanceof EntityRainFX) {
                efx.setAlphaF(0.0f);
            }
            else if (toAdd instanceof EntityDiggingFX && !(toAdd instanceof FBPParticleDigging)) {
                try {
                    FBPParticleManager.blockState = FBPParticleManager.getSourceState.invokeExact((EntityDiggingFX)effect);
                    if (FBPParticleManager.blockState != null && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || FBPParticleManager.blockState.getBlock() != Blocks.redstone_block)) {
                        effect.setDead();
                        if (FBPParticleManager.blockState.getBlock() instanceof BlockLiquid || FBP.getInstance().isBlacklisted(FBPParticleManager.blockState.getBlock())) {
                            return;
                        }
                        toAdd = new FBPParticleDigging(this.worldObj, FBPParticleManager.X.invokeExact(effect), FBPParticleManager.Y.invokeExact(effect), FBPParticleManager.Z.invokeExact(effect), 0.0, 0.0, 0.0, FBPParticleManager.getParticleScale.invokeExact((EntityFX)effect), ((EntityFX)toAdd).getRedColorF(), ((EntityFX)toAdd).getGreenColorF(), ((EntityFX)toAdd).getBlueColorF(), FBPParticleManager.blockState, null, FBPParticleManager.getParticleTexture.invokeExact((EntityFX)effect));
                    }
                }
                catch (final Throwable e) {
                    e.printStackTrace();
                }
            }
            else if (toAdd instanceof FBPParticleDigging) {
                try {
                    FBPParticleManager.blockState = FBPParticleManager.getSourceState.invokeExact((EntityDiggingFX)effect);
                    if (FBPParticleManager.blockState != null && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || FBPParticleManager.blockState.getBlock() != Blocks.redstone_block) && (FBPParticleManager.blockState.getBlock() instanceof BlockLiquid || FBP.getInstance().isBlacklisted(FBPParticleManager.blockState.getBlock()))) {
                        effect.setDead();
                        return;
                    }
                }
                catch (final Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        super.addEffect((EntityFX)toAdd);
    }
    
    @Override
    public EntityFX spawnEffectParticle(final int particleId, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
        IParticleFactory iparticlefactory = null;
        try {
            iparticlefactory = FBPParticleManager.getParticleTypes.invokeExact((EffectRenderer)this).get(particleId);
        }
        catch (final Throwable e) {
            e.printStackTrace();
        }
        if (iparticlefactory != null) {
            final EntityFX particle;
            EntityFX toSpawn = particle = iparticlefactory.getEntityFX(particleId, this.mc.theWorld, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
            if (particle instanceof EntityDiggingFX && !(particle instanceof FBPParticleDigging)) {
                FBPParticleManager.blockState = Block.getStateById(parameters[0]);
                if (FBPParticleManager.blockState != null && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || FBPParticleManager.blockState.getBlock() != Blocks.redstone_block)) {
                    if (!(FBPParticleManager.blockState.getBlock() instanceof BlockLiquid) && !FBP.getInstance().isBlacklisted(FBPParticleManager.blockState.getBlock())) {
                        toSpawn = new FBPParticleDigging(this.mc.theWorld, xCoord, yCoord + 0.10000000149011612, zCoord, xSpeed, ySpeed, zSpeed, -1.0f, 1.0f, 1.0f, 1.0f, FBPParticleManager.blockState, null, null).func_174845_l().multipleParticleScaleBy(0.6f);
                    }
                    else {
                        toSpawn = particle;
                    }
                }
            }
            this.addEffect(toSpawn);
            return toSpawn;
        }
        return null;
    }
    
    @Override
    public void addBlockDestroyEffects(final BlockPos pos, IBlockState state) {
        Block b = state.getBlock();
        state = state.getBlock().getActualState(state, this.worldObj, pos);
        b = state.getBlock();
        final int i = 4;
        final TextureAtlasSprite texture = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
        for (int j = 0; j < FBP.particlesPerAxis; ++j) {
            for (int k = 0; k < FBP.particlesPerAxis; ++k) {
                for (int l = 0; l < FBP.particlesPerAxis; ++l) {
                    final double d0 = pos.getX() + (j + 0.5) / FBP.particlesPerAxis;
                    final double d2 = pos.getY() + (k + 0.5) / FBP.particlesPerAxis;
                    final double d3 = pos.getZ() + (l + 0.5) / FBP.particlesPerAxis;
                    if (state != null && !(b instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || b != Blocks.redstone_block) && !FBP.getInstance().isBlacklisted(b)) {
                        final float scale = (float)FBP.random.nextDouble(0.75, 1.0);
                        final FBPParticleDigging toSpawn = new FBPParticleDigging(this.worldObj, d0, d2, d3, d0 - pos.getX() - 0.5, -0.001, d3 - pos.getZ() - 0.5, scale, 1.0f, 1.0f, 1.0f, state, null, texture).setBlockPos(pos);
                        this.addEffect(toSpawn);
                    }
                }
            }
        }
    }
    
    @Override
    public void addBlockHitEffects(final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = this.worldObj.getBlockState(pos);
        if (iblockstate.getBlock().getRenderType() != -1) {
            final int i = pos.getX();
            final int j = pos.getY();
            final int k = pos.getZ();
            final float f = 0.1f;
            final AxisAlignedBB axisalignedbb = iblockstate.getBlock().getSelectedBoundingBox(this.worldObj, pos);
            double d0 = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            MovingObjectPosition obj = Minecraft.getMinecraft().objectMouseOver;
            if (obj == null || obj.hitVec == null) {
                obj = new MovingObjectPosition(null, new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
            }
            if (FBP.smartBreaking && iblockstate != null && !(iblockstate.getBlock() instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || iblockstate.getBlock() != Blocks.redstone_block)) {
                d0 = obj.hitVec.xCoord + FBP.random.nextDouble(-0.21, 0.21) * Math.abs(axisalignedbb.maxX - axisalignedbb.minX);
                d2 = obj.hitVec.yCoord + FBP.random.nextDouble(-0.21, 0.21) * Math.abs(axisalignedbb.maxY - axisalignedbb.minY);
                d3 = obj.hitVec.zCoord + FBP.random.nextDouble(-0.21, 0.21) * Math.abs(axisalignedbb.maxZ - axisalignedbb.minZ);
            }
            else {
                d0 = i + this.worldObj.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minX;
                d2 = j + this.worldObj.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minY;
                d3 = k + this.worldObj.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minZ;
            }
            switch (side) {
                case DOWN: {
                    d2 = j + iblockstate.getBlock().getBlockBoundsMinY() - 0.10000000149011612;
                    break;
                }
                case EAST: {
                    d0 = i + iblockstate.getBlock().getBlockBoundsMaxX() + 0.10000000149011612;
                    break;
                }
                case NORTH: {
                    d3 = k + iblockstate.getBlock().getBlockBoundsMinZ() - 0.10000000149011612;
                    break;
                }
                case SOUTH: {
                    d3 = k + iblockstate.getBlock().getBlockBoundsMaxZ() + 0.10000000149011612;
                    break;
                }
                case UP: {
                    d2 = j + iblockstate.getBlock().getBlockBoundsMaxY() + 0.10000000149011612;
                    break;
                }
                case WEST: {
                    d0 = i + iblockstate.getBlock().getBlockBoundsMinX() - 0.10000000149011612;
                    break;
                }
            }
            if (iblockstate != null && !(iblockstate.getBlock() instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || iblockstate.getBlock() != Blocks.redstone_block)) {
                int damage = 0;
                try {
                    DestroyBlockProgress progress = null;
                    final Map mp = FBPParticleManager.getBlockDamage.invokeExact(Minecraft.getMinecraft().renderGlobal);
                    if (!mp.isEmpty()) {
                        final Iterator it = mp.values().iterator();
                        while (it.hasNext()) {
                            progress = it.next();
                            if (progress.getPosition().equals(pos)) {
                                damage = progress.getPartialBlockDamage();
                                break;
                            }
                        }
                    }
                }
                catch (final Throwable t) {}
                if (!FBP.getInstance().isBlacklisted(iblockstate.getBlock())) {
                    EntityFX toSpawn = new FBPParticleDigging(this.worldObj, d0, d2, d3, 0.0, 0.0, 0.0, -2.0f, 1.0f, 1.0f, 1.0f, iblockstate, side, null).setBlockPos(pos);
                    if (FBP.smartBreaking) {
                        toSpawn = ((FBPParticleDigging)toSpawn).MultiplyVelocity((side == EnumFacing.UP) ? 0.7f : 0.15f);
                        toSpawn = toSpawn.multipleParticleScaleBy(0.325f + damage / 10.0f * 0.5f);
                    }
                    else {
                        toSpawn = ((FBPParticleDigging)toSpawn).MultiplyVelocity(0.2f);
                        toSpawn = toSpawn.multipleParticleScaleBy(0.6f);
                    }
                    this.addEffect(toSpawn);
                }
            }
        }
    }
}
