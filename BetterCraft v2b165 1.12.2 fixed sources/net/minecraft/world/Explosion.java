// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.block.state.IBlockState;
import java.util.Set;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.material.Material;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.entity.Entity;
import java.util.Random;

public class Explosion
{
    private final boolean isFlaming;
    private final boolean isSmoking;
    private final Random explosionRNG;
    private final World worldObj;
    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;
    private final Entity exploder;
    private final float explosionSize;
    private final List<BlockPos> affectedBlockPositions;
    private final Map<EntityPlayer, Vec3d> playerKnockbackMap;
    
    public Explosion(final World worldIn, final Entity entityIn, final double x, final double y, final double z, final float size, final List<BlockPos> affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }
    
    public Explosion(final World worldIn, final Entity entityIn, final double x, final double y, final double z, final float size, final boolean flaming, final boolean smoking, final List<BlockPos> affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, flaming, smoking);
        this.affectedBlockPositions.addAll(affectedPositions);
    }
    
    public Explosion(final World worldIn, final Entity entityIn, final double x, final double y, final double z, final float size, final boolean flaming, final boolean smoking) {
        this.explosionRNG = new Random();
        this.affectedBlockPositions = (List<BlockPos>)Lists.newArrayList();
        this.playerKnockbackMap = (Map<EntityPlayer, Vec3d>)Maps.newHashMap();
        this.worldObj = worldIn;
        this.exploder = entityIn;
        this.explosionSize = size;
        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;
        this.isFlaming = flaming;
        this.isSmoking = smoking;
    }
    
    public void doExplosionA() {
        final Set<BlockPos> set = (Set<BlockPos>)Sets.newHashSet();
        final int i = 16;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = j / 15.0f * 2.0f - 1.0f;
                        double d2 = k / 15.0f * 2.0f - 1.0f;
                        double d3 = l / 15.0f * 2.0f - 1.0f;
                        final double d4 = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
                        d0 /= d4;
                        d2 /= d4;
                        d3 /= d4;
                        float f = this.explosionSize * (0.7f + this.worldObj.rand.nextFloat() * 0.6f);
                        double d5 = this.explosionX;
                        double d6 = this.explosionY;
                        double d7 = this.explosionZ;
                        final float f2 = 0.3f;
                        while (f > 0.0f) {
                            final BlockPos blockpos = new BlockPos(d5, d6, d7);
                            final IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                            if (iblockstate.getMaterial() != Material.AIR) {
                                final float f3 = (this.exploder != null) ? this.exploder.getExplosionResistance(this, this.worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(null);
                                f -= (f3 + 0.3f) * 0.3f;
                            }
                            if (f > 0.0f && (this.exploder == null || this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f))) {
                                set.add(blockpos);
                            }
                            d5 += d0 * 0.30000001192092896;
                            d6 += d2 * 0.30000001192092896;
                            d7 += d3 * 0.30000001192092896;
                            f -= 0.22500001f;
                        }
                    }
                }
            }
        }
        this.affectedBlockPositions.addAll(set);
        final float f4 = this.explosionSize * 2.0f;
        final int k2 = MathHelper.floor(this.explosionX - f4 - 1.0);
        final int l2 = MathHelper.floor(this.explosionX + f4 + 1.0);
        final int i2 = MathHelper.floor(this.explosionY - f4 - 1.0);
        final int i3 = MathHelper.floor(this.explosionY + f4 + 1.0);
        final int j2 = MathHelper.floor(this.explosionZ - f4 - 1.0);
        final int j3 = MathHelper.floor(this.explosionZ + f4 + 1.0);
        final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k2, i2, j2, l2, i3, j3));
        final Vec3d vec3d = new Vec3d(this.explosionX, this.explosionY, this.explosionZ);
        for (int k3 = 0; k3 < list.size(); ++k3) {
            final Entity entity = list.get(k3);
            if (!entity.isImmuneToExplosions()) {
                final double d8 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / f4;
                if (d8 <= 1.0) {
                    double d9 = entity.posX - this.explosionX;
                    double d10 = entity.posY + entity.getEyeHeight() - this.explosionY;
                    double d11 = entity.posZ - this.explosionZ;
                    final double d12 = MathHelper.sqrt(d9 * d9 + d10 * d10 + d11 * d11);
                    if (d12 != 0.0) {
                        d9 /= d12;
                        d10 /= d12;
                        d11 /= d12;
                        final double d13 = this.worldObj.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        final double d14 = (1.0 - d8) * d13;
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)(int)((d14 * d14 + d14) / 2.0 * 7.0 * f4 + 1.0));
                        double d15 = d14;
                        if (entity instanceof EntityLivingBase) {
                            d15 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d14);
                        }
                        final Entity entity2 = entity;
                        entity2.motionX += d9 * d15;
                        final Entity entity3 = entity;
                        entity3.motionY += d10 * d15;
                        final Entity entity4 = entity;
                        entity4.motionZ += d11 * d15;
                        if (entity instanceof EntityPlayer) {
                            final EntityPlayer entityplayer = (EntityPlayer)entity;
                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)) {
                                this.playerKnockbackMap.put(entityplayer, new Vec3d(d9 * d14, d10 * d14, d11 * d14));
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void doExplosionB(final boolean spawnParticles) {
        this.worldObj.playSound(null, this.explosionX, this.explosionY, this.explosionZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2f) * 0.7f);
        if (this.explosionSize >= 2.0f && this.isSmoking) {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        }
        else {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        }
        if (this.isSmoking) {
            for (final BlockPos blockpos : this.affectedBlockPositions) {
                final IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (spawnParticles) {
                    final double d0 = blockpos.getX() + this.worldObj.rand.nextFloat();
                    final double d2 = blockpos.getY() + this.worldObj.rand.nextFloat();
                    final double d3 = blockpos.getZ() + this.worldObj.rand.nextFloat();
                    double d4 = d0 - this.explosionX;
                    double d5 = d2 - this.explosionY;
                    double d6 = d3 - this.explosionZ;
                    final double d7 = MathHelper.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
                    d4 /= d7;
                    d5 /= d7;
                    d6 /= d7;
                    double d8 = 0.5 / (d7 / this.explosionSize + 0.1);
                    d8 *= this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3f;
                    d4 *= d8;
                    d5 *= d8;
                    d6 *= d8;
                    this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX) / 2.0, (d2 + this.explosionY) / 2.0, (d3 + this.explosionZ) / 2.0, d4, d5, d6, new int[0]);
                    this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, d4, d5, d6, new int[0]);
                }
                if (iblockstate.getMaterial() != Material.AIR) {
                    if (block.canDropFromExplosion(this)) {
                        block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0f / this.explosionSize, 0);
                    }
                    this.worldObj.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
                    block.onBlockDestroyedByExplosion(this.worldObj, blockpos, this);
                }
            }
        }
        if (this.isFlaming) {
            for (final BlockPos blockpos2 : this.affectedBlockPositions) {
                if (this.worldObj.getBlockState(blockpos2).getMaterial() == Material.AIR && this.worldObj.getBlockState(blockpos2.down()).isFullBlock() && this.explosionRNG.nextInt(3) == 0) {
                    this.worldObj.setBlockState(blockpos2, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }
    
    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }
    
    @Nullable
    public EntityLivingBase getExplosivePlacedBy() {
        if (this.exploder == null) {
            return null;
        }
        if (this.exploder instanceof EntityTNTPrimed) {
            return ((EntityTNTPrimed)this.exploder).getTntPlacedBy();
        }
        return (this.exploder instanceof EntityLivingBase) ? ((EntityLivingBase)this.exploder) : null;
    }
    
    public void clearAffectedBlockPositions() {
        this.affectedBlockPositions.clear();
    }
    
    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }
}
