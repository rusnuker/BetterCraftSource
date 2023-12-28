// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.particle;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.particle.ParticleRain;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.particle.ParticleFlame;
import com.TominoCZ.FBP.FBP;
import java.lang.reflect.Field;
import com.google.common.base.Throwables;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.Particle;
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
import net.minecraft.client.particle.ParticleManager;

public class FBPParticleManager extends ParticleManager
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
    
    public FBPParticleManager(final World worldObjIn, final TextureManager rendererIn, final IParticleFactory particleFactory) {
        super(worldObjIn, rendererIn);
        FBPParticleManager.particleFactory = particleFactory;
        this.mc = Minecraft.getMinecraft();
        FBPParticleManager.white = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.SNOW.getDefaultState());
        FBPParticleManager.lookup = MethodHandles.publicLookup();
        try {
            FBPParticleManager.getParticleTypes = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(ParticleManager.class, "field_178932_g", "particleTypes"));
            FBPParticleManager.X = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_187126_f", "posX"));
            FBPParticleManager.Y = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_187127_g", "posY"));
            FBPParticleManager.Z = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_187128_h", "posZ"));
            FBPParticleManager.mX = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_187129_i", "motionX"));
            FBPParticleManager.mY = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_187130_j", "motionY"));
            FBPParticleManager.mZ = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_187131_k", "motionZ"));
            FBPParticleManager.getParticleScale = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_70544_f", "particleScale"));
            FBPParticleManager.getParticleTexture = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_187119_C", "particleTexture"));
            FBPParticleManager.getParticleMaxAge = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(Particle.class, "field_70547_e", "particleMaxAge"));
            FBPParticleManager.getSourceState = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(ParticleDigging.class, "field_174847_a", "sourceState"));
            FBPParticleManager.getBlockDamage = FBPParticleManager.lookup.unreflectGetter(FBPReflectionHelper.findField(RenderGlobal.class, "field_72738_E", "damagedBlocks"));
        }
        catch (final Throwable e) {
            throw Throwables.propagate(e);
        }
    }
    
    public void carryOver() {
        if (Minecraft.getMinecraft().effectRenderer == this) {
            return;
        }
        final Field f1 = FBPReflectionHelper.findField(ParticleManager.class, "field_78876_b", "fxLayers");
        final Field f2 = FBPReflectionHelper.findField(ParticleManager.class, "field_178933_d", "particleEmitters");
        final Field f3 = FBPReflectionHelper.findField(ParticleManager.class, "field_187241_h", "queue");
        try {
            final MethodHandle getF1 = FBPParticleManager.lookup.unreflectGetter(f1);
            final MethodHandle setF1 = FBPParticleManager.lookup.unreflectSetter(f1);
            final MethodHandle getF2 = FBPParticleManager.lookup.unreflectGetter(f2);
            final MethodHandle setF2 = FBPParticleManager.lookup.unreflectSetter(f2);
            final MethodHandle getF3 = FBPParticleManager.lookup.unreflectGetter(f3);
            final MethodHandle setF3 = FBPParticleManager.lookup.unreflectSetter(f3);
            setF1.invokeExact((ParticleManager)this, getF1.invokeExact(this.mc.effectRenderer));
            setF2.invokeExact((ParticleManager)this, getF2.invokeExact(this.mc.effectRenderer));
            setF3.invokeExact((ParticleManager)this, getF3.invokeExact(this.mc.effectRenderer));
        }
        catch (final Throwable e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void addEffect(final Particle effect) {
        Particle toAdd = effect;
        Label_0595: {
            if (toAdd != null && !(toAdd instanceof FBPParticleSnow) && !(toAdd instanceof FBPParticleRain)) {
                if (FBP.fancyFlame && toAdd instanceof ParticleFlame && !(toAdd instanceof FBPParticleFlame) && Minecraft.getMinecraft().gameSettings.particleSetting < 2) {
                    try {
                        toAdd = new FBPParticleFlame(this.worldObj, FBPParticleManager.X.invokeExact(effect), FBPParticleManager.Y.invokeExact(effect), FBPParticleManager.Z.invokeExact(effect), 0.0, FBP.random.nextDouble() * 0.25, 0.0, true);
                        effect.setExpired();
                    }
                    catch (final Throwable t) {
                        t.printStackTrace();
                    }
                }
                else if (FBP.fancySmoke && toAdd instanceof ParticleSmokeNormal && !(toAdd instanceof FBPParticleSmokeNormal) && Minecraft.getMinecraft().gameSettings.particleSetting < 2) {
                    final ParticleSmokeNormal p = (ParticleSmokeNormal)effect;
                    try {
                        toAdd = new FBPParticleSmokeNormal(this.worldObj, FBPParticleManager.X.invokeExact(effect), FBPParticleManager.Y.invokeExact(effect), FBPParticleManager.Z.invokeExact(effect), FBPParticleManager.mX.invokeExact(effect), FBPParticleManager.mY.invokeExact(effect), FBPParticleManager.mZ.invokeExact(effect), FBPParticleManager.getParticleScale.invokeExact(effect), true, FBPParticleManager.white, p);
                        toAdd.setRBGColorF(MathHelper.clamp(effect.getRedColorF() + 0.1f, 0.1f, 1.0f), MathHelper.clamp(effect.getGreenColorF() + 0.1f, 0.1f, 1.0f), MathHelper.clamp(effect.getBlueColorF() + 0.1f, 0.1f, 1.0f));
                        toAdd.setMaxAge(FBPParticleManager.getParticleMaxAge.invokeExact(effect));
                    }
                    catch (final Throwable t2) {
                        t2.printStackTrace();
                    }
                }
                else if (FBP.fancyRain && toAdd instanceof ParticleRain) {
                    effect.setAlphaF(0.0f);
                }
                else {
                    if (toAdd instanceof ParticleDigging && !(toAdd instanceof FBPParticleDigging)) {
                        try {
                            FBPParticleManager.blockState = FBPParticleManager.getSourceState.invokeExact((ParticleDigging)effect);
                            if (FBPParticleManager.blockState == null || (FBP.frozen && !FBP.spawnWhileFrozen) || (!FBP.spawnRedstoneBlockParticles && FBPParticleManager.blockState.getBlock() == Blocks.REDSTONE_BLOCK)) {
                                break Label_0595;
                            }
                            effect.setExpired();
                            if (!(FBPParticleManager.blockState.getBlock() instanceof BlockLiquid) && !FBP.INSTANCE.isBlacklisted(FBPParticleManager.blockState.getBlock(), true)) {
                                toAdd = new FBPParticleDigging(this.worldObj, FBPParticleManager.X.invokeExact(effect), FBPParticleManager.Y.invokeExact(effect) - 0.10000000149011612, FBPParticleManager.Z.invokeExact(effect), 0.0, 0.0, 0.0, FBPParticleManager.getParticleScale.invokeExact(effect), toAdd.getRedColorF(), toAdd.getGreenColorF(), toAdd.getBlueColorF(), FBPParticleManager.blockState, null, FBPParticleManager.getParticleTexture.invokeExact(effect));
                                break Label_0595;
                            }
                            return;
                        }
                        catch (final Throwable e) {
                            e.printStackTrace();
                            break Label_0595;
                        }
                    }
                    if (toAdd instanceof FBPParticleDigging) {
                        try {
                            FBPParticleManager.blockState = FBPParticleManager.getSourceState.invokeExact((ParticleDigging)effect);
                            if (FBPParticleManager.blockState != null && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || FBPParticleManager.blockState.getBlock() != Blocks.REDSTONE_BLOCK) && (FBPParticleManager.blockState.getBlock() instanceof BlockLiquid || FBP.INSTANCE.isBlacklisted(FBPParticleManager.blockState.getBlock(), true))) {
                                effect.setExpired();
                                return;
                            }
                        }
                        catch (final Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (toAdd != effect) {
            effect.setExpired();
        }
        super.addEffect(toAdd);
    }
    
    @Nullable
    @Override
    public Particle spawnEffectParticle(final int particleId, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
        IParticleFactory iparticlefactory = null;
        try {
            iparticlefactory = FBPParticleManager.getParticleTypes.invokeExact((ParticleManager)this).get(particleId);
        }
        catch (final Throwable e) {
            e.printStackTrace();
        }
        if (iparticlefactory != null) {
            Particle toSpawn;
            final Particle particle = toSpawn = iparticlefactory.createParticle(particleId, this.worldObj, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
            if (particle instanceof ParticleDigging && !(particle instanceof FBPParticleDigging)) {
                FBPParticleManager.blockState = Block.getStateById(parameters[0]);
                if (FBPParticleManager.blockState != null && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || FBPParticleManager.blockState.getBlock() != Blocks.REDSTONE_BLOCK)) {
                    if (!(FBPParticleManager.blockState.getBlock() instanceof BlockLiquid) && !FBP.INSTANCE.isBlacklisted(FBPParticleManager.blockState.getBlock(), true)) {
                        toSpawn = new FBPParticleDigging(this.worldObj, xCoord, yCoord + 0.10000000149011612, zCoord, xSpeed, ySpeed, zSpeed, -1.0f, 1.0f, 1.0f, 1.0f, FBPParticleManager.blockState, null, null).init().multipleParticleScaleBy(0.6f);
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
        if (!b.isAir() && b != FBP.FBPBlock) {
            state = state.getActualState(this.worldObj, pos);
            b = state.getBlock();
            final int i = 4;
            final TextureAtlasSprite texture = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
            for (int j = 0; j < FBP.particlesPerAxis; ++j) {
                for (int k = 0; k < FBP.particlesPerAxis; ++k) {
                    for (int l = 0; l < FBP.particlesPerAxis; ++l) {
                        final double d0 = pos.getX() + (j + 0.5) / FBP.particlesPerAxis;
                        final double d2 = pos.getY() + (k + 0.5) / FBP.particlesPerAxis;
                        final double d3 = pos.getZ() + (l + 0.5) / FBP.particlesPerAxis;
                        if (state != null && !(b instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || b != Blocks.REDSTONE_BLOCK) && !FBP.INSTANCE.isBlacklisted(b, true)) {
                            final float scale = (float)FBP.random.nextDouble(0.75, 1.0);
                            final FBPParticleDigging toSpawn = new FBPParticleDigging(this.worldObj, d0, d2, d3, d0 - pos.getX() - 0.5, -0.001, d3 - pos.getZ() - 0.5, scale, 1.0f, 1.0f, 1.0f, state, null, texture).setBlockPos(pos);
                            this.addEffect(toSpawn);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void addBlockHitEffects(final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = this.worldObj.getBlockState(pos);
        if (iblockstate.getBlock() == FBP.FBPBlock) {
            return;
        }
        if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            final int i = pos.getX();
            final int j = pos.getY();
            final int k = pos.getZ();
            final float f = 0.1f;
            final AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(this.worldObj, pos);
            double d0 = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            RayTraceResult obj = Minecraft.getMinecraft().objectMouseOver;
            if (obj == null || obj.hitVec == null) {
                obj = new RayTraceResult(null, new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
            }
            if (FBP.smartBreaking && iblockstate != null && !(iblockstate.getBlock() instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || iblockstate.getBlock() != Blocks.REDSTONE_BLOCK)) {
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
                    d2 = j + axisalignedbb.minY - 0.10000000149011612;
                    break;
                }
                case EAST: {
                    d0 = i + axisalignedbb.maxX + 0.10000000149011612;
                    break;
                }
                case NORTH: {
                    d3 = k + axisalignedbb.minZ - 0.10000000149011612;
                    break;
                }
                case SOUTH: {
                    d3 = k + axisalignedbb.maxZ + 0.10000000149011612;
                    break;
                }
                case UP: {
                    d2 = j + axisalignedbb.maxY + 0.08000000119;
                    break;
                }
                case WEST: {
                    d0 = i + axisalignedbb.minX - 0.10000000149011612;
                    break;
                }
            }
            if (iblockstate != null && !(iblockstate.getBlock() instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || iblockstate.getBlock() != Blocks.REDSTONE_BLOCK)) {
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
                if (!FBP.INSTANCE.isBlacklisted(iblockstate.getBlock(), true)) {
                    Particle toSpawn = new FBPParticleDigging(this.worldObj, d0, d2, d3, 0.0, 0.0, 0.0, -2.0f, 1.0f, 1.0f, 1.0f, iblockstate, side, null).setBlockPos(pos);
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
