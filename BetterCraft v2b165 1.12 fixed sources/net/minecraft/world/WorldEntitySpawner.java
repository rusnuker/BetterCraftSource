// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

import java.util.List;
import net.minecraft.util.WeightedRandom;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockRailBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.biome.Biome;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.management.PlayerChunkMapEntry;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Sets;
import net.minecraft.util.math.ChunkPos;
import java.util.Set;

public final class WorldEntitySpawner
{
    private static final int MOB_COUNT_DIV;
    private final Set<ChunkPos> eligibleChunksForSpawning;
    
    static {
        MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
    }
    
    public WorldEntitySpawner() {
        this.eligibleChunksForSpawning = (Set<ChunkPos>)Sets.newHashSet();
    }
    
    public int findChunksForSpawning(final WorldServer worldServerIn, final boolean spawnHostileMobs, final boolean spawnPeacefulMobs, final boolean spawnOnSetTickRate) {
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
        this.eligibleChunksForSpawning.clear();
        int i = 0;
        for (final EntityPlayer entityplayer : worldServerIn.playerEntities) {
            if (!entityplayer.isSpectator()) {
                final int j = MathHelper.floor(entityplayer.posX / 16.0);
                final int k = MathHelper.floor(entityplayer.posZ / 16.0);
                final int l = 8;
                for (int i2 = -8; i2 <= 8; ++i2) {
                    for (int j2 = -8; j2 <= 8; ++j2) {
                        final boolean flag = i2 == -8 || i2 == 8 || j2 == -8 || j2 == 8;
                        final ChunkPos chunkpos = new ChunkPos(i2 + j, j2 + k);
                        if (!this.eligibleChunksForSpawning.contains(chunkpos)) {
                            ++i;
                            if (!flag && worldServerIn.getWorldBorder().contains(chunkpos)) {
                                final PlayerChunkMapEntry playerchunkmapentry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.chunkXPos, chunkpos.chunkZPos);
                                if (playerchunkmapentry != null && playerchunkmapentry.isSentToPlayers()) {
                                    this.eligibleChunksForSpawning.add(chunkpos);
                                }
                            }
                        }
                    }
                }
            }
        }
        int j3 = 0;
        final BlockPos blockpos1 = worldServerIn.getSpawnPoint();
        EnumCreatureType[] values;
        for (int length = (values = EnumCreatureType.values()).length, n = 0; n < length; ++n) {
            final EnumCreatureType enumcreaturetype = values[n];
            if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs) && (enumcreaturetype.getPeacefulCreature() || spawnHostileMobs) && (!enumcreaturetype.getAnimal() || spawnOnSetTickRate)) {
                final int k2 = worldServerIn.countEntities(enumcreaturetype.getCreatureClass());
                final int l2 = enumcreaturetype.getMaxNumberOfCreature() * i / WorldEntitySpawner.MOB_COUNT_DIV;
                if (k2 <= l2) {
                    final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                Label_0869:
                    while (true) {
                        for (final ChunkPos chunkpos2 : this.eligibleChunksForSpawning) {
                            final BlockPos blockpos2 = getRandomChunkPosition(worldServerIn, chunkpos2.chunkXPos, chunkpos2.chunkZPos);
                            final int k3 = blockpos2.getX();
                            final int l3 = blockpos2.getY();
                            final int i3 = blockpos2.getZ();
                            final IBlockState iblockstate = worldServerIn.getBlockState(blockpos2);
                            if (!iblockstate.isNormalCube()) {
                                int j4 = 0;
                                for (int k4 = 0; k4 < 3; ++k4) {
                                    int l4 = k3;
                                    int i4 = l3;
                                    int j5 = i3;
                                    final int k5 = 6;
                                    Biome.SpawnListEntry biome$spawnlistentry = null;
                                    IEntityLivingData ientitylivingdata = null;
                                    for (int l5 = MathHelper.ceil(Math.random() * 4.0), i5 = 0; i5 < l5; ++i5) {
                                        l4 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6);
                                        i4 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1);
                                        j5 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6);
                                        blockpos$mutableblockpos.setPos(l4, i4, j5);
                                        final float f = l4 + 0.5f;
                                        final float f2 = j5 + 0.5f;
                                        if (!worldServerIn.isAnyPlayerWithinRangeAt(f, i4, f2, 24.0) && blockpos1.distanceSq(f, i4, f2) >= 576.0) {
                                            if (biome$spawnlistentry == null) {
                                                biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos$mutableblockpos);
                                                if (biome$spawnlistentry == null) {
                                                    break;
                                                }
                                            }
                                            if (worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, blockpos$mutableblockpos) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biome$spawnlistentry.entityClass), worldServerIn, blockpos$mutableblockpos)) {
                                                EntityLiving entityliving;
                                                try {
                                                    entityliving = (EntityLiving)biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldServerIn);
                                                }
                                                catch (final Exception exception) {
                                                    exception.printStackTrace();
                                                    return j3;
                                                }
                                                entityliving.setLocationAndAngles(f, i4, f2, worldServerIn.rand.nextFloat() * 360.0f, 0.0f);
                                                if (entityliving.getCanSpawnHere() && entityliving.isNotColliding()) {
                                                    ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                                                    if (entityliving.isNotColliding()) {
                                                        ++j4;
                                                        worldServerIn.spawnEntityInWorld(entityliving);
                                                    }
                                                    else {
                                                        entityliving.setDead();
                                                    }
                                                    if (j4 >= entityliving.getMaxSpawnedInChunk()) {
                                                        continue Label_0869;
                                                    }
                                                }
                                                j3 += j4;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return j3;
    }
    
    private static BlockPos getRandomChunkPosition(final World worldIn, final int x, final int z) {
        final Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
        final int i = x * 16 + worldIn.rand.nextInt(16);
        final int j = z * 16 + worldIn.rand.nextInt(16);
        final int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
        final int l = worldIn.rand.nextInt((k > 0) ? k : (chunk.getTopFilledSegment() + 16 - 1));
        return new BlockPos(i, l, j);
    }
    
    public static boolean isValidEmptySpawnBlock(final IBlockState state) {
        return !state.isBlockNormalCube() && !state.canProvidePower() && !state.getMaterial().isLiquid() && !BlockRailBase.isRailBlock(state);
    }
    
    public static boolean canCreatureTypeSpawnAtLocation(final EntityLiving.SpawnPlacementType spawnPlacementTypeIn, final World worldIn, final BlockPos pos) {
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (spawnPlacementTypeIn == EntityLiving.SpawnPlacementType.IN_WATER) {
            return iblockstate.getMaterial() == Material.WATER && worldIn.getBlockState(pos.down()).getMaterial() == Material.WATER && !worldIn.getBlockState(pos.up()).isNormalCube();
        }
        final BlockPos blockpos = pos.down();
        if (!worldIn.getBlockState(blockpos).isFullyOpaque()) {
            return false;
        }
        final Block block = worldIn.getBlockState(blockpos).getBlock();
        final boolean flag = block != Blocks.BEDROCK && block != Blocks.BARRIER;
        return flag && isValidEmptySpawnBlock(iblockstate) && isValidEmptySpawnBlock(worldIn.getBlockState(pos.up()));
    }
    
    public static void performWorldGenSpawning(final World worldIn, final Biome biomeIn, final int p_77191_2_, final int p_77191_3_, final int p_77191_4_, final int p_77191_5_, final Random randomIn) {
        final List<Biome.SpawnListEntry> list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
                final Biome.SpawnListEntry biome$spawnlistentry = WeightedRandom.getRandomItem(worldIn.rand, list);
                final int i = biome$spawnlistentry.minGroupCount + randomIn.nextInt(1 + biome$spawnlistentry.maxGroupCount - biome$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = p_77191_2_ + randomIn.nextInt(p_77191_4_);
                int k = p_77191_3_ + randomIn.nextInt(p_77191_5_);
                final int l = j;
                final int i2 = k;
                for (int j2 = 0; j2 < i; ++j2) {
                    boolean flag = false;
                    for (int k2 = 0; !flag && k2 < 4; ++k2) {
                        final BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                        if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                            EntityLiving entityliving;
                            try {
                                entityliving = (EntityLiving)biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                            }
                            catch (final Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }
                            entityliving.setLocationAndAngles(j + 0.5f, blockpos.getY(), k + 0.5f, randomIn.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntityInWorld(entityliving);
                            ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                            flag = true;
                        }
                        for (j += randomIn.nextInt(5) - randomIn.nextInt(5), k += randomIn.nextInt(5) - randomIn.nextInt(5); j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_; j = l + randomIn.nextInt(5) - randomIn.nextInt(5), k = i2 + randomIn.nextInt(5) - randomIn.nextInt(5)) {}
                    }
                }
            }
        }
    }
}
