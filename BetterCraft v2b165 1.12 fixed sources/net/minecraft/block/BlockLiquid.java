// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public abstract class BlockLiquid extends Block
{
    public static final PropertyInteger LEVEL;
    
    static {
        LEVEL = PropertyInteger.create("level", 0, 15);
    }
    
    protected BlockLiquid(final Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockLiquid.LEVEL, 0));
        this.setTickRandomly(true);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockLiquid.FULL_BLOCK_AABB;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockLiquid.NULL_AABB;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return this.blockMaterial != Material.LAVA;
    }
    
    public static float getLiquidHeightPercent(int meta) {
        if (meta >= 8) {
            meta = 0;
        }
        return (meta + 1) / 9.0f;
    }
    
    protected int getDepth(final IBlockState p_189542_1_) {
        return (p_189542_1_.getMaterial() == this.blockMaterial) ? p_189542_1_.getValue((IProperty<Integer>)BlockLiquid.LEVEL) : -1;
    }
    
    protected int getRenderedDepth(final IBlockState p_189545_1_) {
        final int i = this.getDepth(p_189545_1_);
        return (i >= 8) ? 0 : i;
    }
    
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean canCollideCheck(final IBlockState state, final boolean hitIfLiquid) {
        return hitIfLiquid && state.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0;
    }
    
    private boolean isBlockSolid(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        final Material material = iblockstate.getMaterial();
        if (material == this.blockMaterial) {
            return false;
        }
        if (side == EnumFacing.UP) {
            return true;
        }
        if (material == Material.ICE) {
            return false;
        }
        final boolean flag = Block.func_193382_c(block) || block instanceof BlockStairs;
        return !flag && iblockstate.func_193401_d(worldIn, pos, side) == BlockFaceShape.SOLID;
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getMaterial() != this.blockMaterial && (side == EnumFacing.UP || super.shouldSideBeRendered(blockState, blockAccess, pos, side));
    }
    
    public boolean shouldRenderSides(final IBlockAccess blockAccess, final BlockPos pos) {
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                final IBlockState iblockstate = blockAccess.getBlockState(pos.add(i, 0, j));
                if (iblockstate.getMaterial() != this.blockMaterial && !iblockstate.isFullBlock()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.LIQUID;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.field_190931_a;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    protected Vec3d getFlow(final IBlockAccess p_189543_1_, final BlockPos p_189543_2_, final IBlockState p_189543_3_) {
        double d0 = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        final int i = this.getRenderedDepth(p_189543_3_);
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            blockpos$pooledmutableblockpos.setPos(p_189543_2_).move(enumfacing);
            int j = this.getRenderedDepth(p_189543_1_.getBlockState(blockpos$pooledmutableblockpos));
            if (j < 0) {
                if (p_189543_1_.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement()) {
                    continue;
                }
                j = this.getRenderedDepth(p_189543_1_.getBlockState(blockpos$pooledmutableblockpos.down()));
                if (j < 0) {
                    continue;
                }
                final int k = j - (i - 8);
                d0 += enumfacing.getFrontOffsetX() * k;
                d2 += enumfacing.getFrontOffsetY() * k;
                d3 += enumfacing.getFrontOffsetZ() * k;
            }
            else {
                if (j < 0) {
                    continue;
                }
                final int l = j - i;
                d0 += enumfacing.getFrontOffsetX() * l;
                d2 += enumfacing.getFrontOffsetY() * l;
                d3 += enumfacing.getFrontOffsetZ() * l;
            }
        }
        Vec3d vec3d = new Vec3d(d0, d2, d3);
        if (p_189543_3_.getValue((IProperty<Integer>)BlockLiquid.LEVEL) >= 8) {
            for (final EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                blockpos$pooledmutableblockpos.setPos(p_189543_2_).move(enumfacing2);
                if (this.isBlockSolid(p_189543_1_, blockpos$pooledmutableblockpos, enumfacing2) || this.isBlockSolid(p_189543_1_, blockpos$pooledmutableblockpos.up(), enumfacing2)) {
                    vec3d = vec3d.normalize().addVector(0.0, -6.0, 0.0);
                    break;
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return vec3d.normalize();
    }
    
    @Override
    public Vec3d modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3d motion) {
        return motion.add(this.getFlow(worldIn, pos, worldIn.getBlockState(pos)));
    }
    
    @Override
    public int tickRate(final World worldIn) {
        if (this.blockMaterial == Material.WATER) {
            return 5;
        }
        if (this.blockMaterial == Material.LAVA) {
            return worldIn.provider.getHasNoSky() ? 10 : 30;
        }
        return 0;
    }
    
    @Override
    public int getPackedLightmapCoords(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        final int i = source.getCombinedLight(pos, 0);
        final int j = source.getCombinedLight(pos.up(), 0);
        final int k = i & 0xFF;
        final int l = j & 0xFF;
        final int i2 = i >> 16 & 0xFF;
        final int j2 = j >> 16 & 0xFF;
        return ((k > l) ? k : l) | ((i2 > j2) ? i2 : j2) << 16;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return (this.blockMaterial == Material.WATER) ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final double d0 = pos.getX();
        final double d2 = pos.getY();
        final double d3 = pos.getZ();
        if (this.blockMaterial == Material.WATER) {
            final int i = stateIn.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
            if (i > 0 && i < 8) {
                if (rand.nextInt(64) == 0) {
                    worldIn.playSound(d0 + 0.5, d2 + 0.5, d3 + 0.5, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25f + 0.75f, rand.nextFloat() + 0.5f, false);
                }
            }
            else if (rand.nextInt(10) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, d0 + rand.nextFloat(), d2 + rand.nextFloat(), d3 + rand.nextFloat(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        if (this.blockMaterial == Material.LAVA && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && !worldIn.getBlockState(pos.up()).isOpaqueCube()) {
            if (rand.nextInt(100) == 0) {
                final double d4 = d0 + rand.nextFloat();
                final double d5 = d2 + stateIn.getBoundingBox(worldIn, pos).maxY;
                final double d6 = d3 + rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, d4, d5, d6, 0.0, 0.0, 0.0, new int[0]);
                worldIn.playSound(d4, d5, d6, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
            if (rand.nextInt(200) == 0) {
                worldIn.playSound(d0, d2, d3, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
        }
        if (rand.nextInt(10) == 0 && worldIn.getBlockState(pos.down()).isFullyOpaque()) {
            final Material material = worldIn.getBlockState(pos.down(2)).getMaterial();
            if (!material.blocksMovement() && !material.isLiquid()) {
                final double d7 = d0 + rand.nextFloat();
                final double d8 = d2 - 1.05;
                final double d9 = d3 + rand.nextFloat();
                if (this.blockMaterial == Material.WATER) {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d7, d8, d9, 0.0, 0.0, 0.0, new int[0]);
                }
                else {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d7, d8, d9, 0.0, 0.0, 0.0, new int[0]);
                }
            }
        }
    }
    
    public static float getSlopeAngle(final IBlockAccess p_189544_0_, final BlockPos p_189544_1_, final Material p_189544_2_, final IBlockState p_189544_3_) {
        final Vec3d vec3d = getFlowingBlock(p_189544_2_).getFlow(p_189544_0_, p_189544_1_, p_189544_3_);
        return (vec3d.xCoord == 0.0 && vec3d.zCoord == 0.0) ? -1000.0f : ((float)MathHelper.atan2(vec3d.zCoord, vec3d.xCoord) - 1.5707964f);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.checkForMixing(worldIn, pos, state);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos p_189540_5_) {
        this.checkForMixing(worldIn, pos, state);
    }
    
    public boolean checkForMixing(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.blockMaterial == Material.LAVA) {
            boolean flag = false;
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                if (enumfacing != EnumFacing.DOWN && worldIn.getBlockState(pos.offset(enumfacing)).getMaterial() == Material.WATER) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                final Integer integer = state.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
                if (integer == 0) {
                    worldIn.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
                if (integer <= 4) {
                    worldIn.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
            }
        }
        return false;
    }
    
    protected void triggerMixEffects(final World worldIn, final BlockPos pos) {
        final double d0 = pos.getX();
        final double d2 = pos.getY();
        final double d3 = pos.getZ();
        worldIn.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
        for (int i = 0; i < 8; ++i) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d2 + 1.2, d3 + Math.random(), 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockLiquid.LEVEL, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockLiquid.LEVEL });
    }
    
    public static BlockDynamicLiquid getFlowingBlock(final Material materialIn) {
        if (materialIn == Material.WATER) {
            return Blocks.FLOWING_WATER;
        }
        if (materialIn == Material.LAVA) {
            return Blocks.FLOWING_LAVA;
        }
        throw new IllegalArgumentException("Invalid material");
    }
    
    public static BlockStaticLiquid getStaticBlock(final Material materialIn) {
        if (materialIn == Material.WATER) {
            return Blocks.WATER;
        }
        if (materialIn == Material.LAVA) {
            return Blocks.LAVA;
        }
        throw new IllegalArgumentException("Invalid material");
    }
    
    public static float func_190973_f(final IBlockState p_190973_0_, final IBlockAccess p_190973_1_, final BlockPos p_190973_2_) {
        final int i = p_190973_0_.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
        return ((i & 0x7) == 0x0 && p_190973_1_.getBlockState(p_190973_2_.up()).getMaterial() == Material.WATER) ? 1.0f : (1.0f - getLiquidHeightPercent(i));
    }
    
    public static float func_190972_g(final IBlockState p_190972_0_, final IBlockAccess p_190972_1_, final BlockPos p_190972_2_) {
        return p_190972_2_.getY() + func_190973_f(p_190972_0_, p_190972_1_, p_190972_2_);
    }
    
    @Override
    public BlockFaceShape func_193383_a(final IBlockAccess p_193383_1_, final IBlockState p_193383_2_, final BlockPos p_193383_3_, final EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}
