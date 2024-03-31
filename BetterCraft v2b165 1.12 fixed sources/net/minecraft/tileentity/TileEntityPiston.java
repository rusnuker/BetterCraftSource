// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.MoverType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;

public class TileEntityPiston extends TileEntity implements ITickable
{
    private IBlockState pistonState;
    private EnumFacing pistonFacing;
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private static final ThreadLocal<EnumFacing> field_190613_i;
    private float progress;
    private float lastProgress;
    
    static {
        field_190613_i = new ThreadLocal<EnumFacing>() {
            @Override
            protected EnumFacing initialValue() {
                return null;
            }
        };
    }
    
    public TileEntityPiston() {
    }
    
    public TileEntityPiston(final IBlockState pistonStateIn, final EnumFacing pistonFacingIn, final boolean extendingIn, final boolean shouldHeadBeRenderedIn) {
        this.pistonState = pistonStateIn;
        this.pistonFacing = pistonFacingIn;
        this.extending = extendingIn;
        this.shouldHeadBeRendered = shouldHeadBeRenderedIn;
    }
    
    public IBlockState getPistonState() {
        return this.pistonState;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public int getBlockMetadata() {
        return 0;
    }
    
    public boolean isExtending() {
        return this.extending;
    }
    
    public EnumFacing getFacing() {
        return this.pistonFacing;
    }
    
    public boolean shouldPistonHeadBeRendered() {
        return this.shouldHeadBeRendered;
    }
    
    public float getProgress(float ticks) {
        if (ticks > 1.0f) {
            ticks = 1.0f;
        }
        return this.lastProgress + (this.progress - this.lastProgress) * ticks;
    }
    
    public float getOffsetX(final float ticks) {
        return this.pistonFacing.getFrontOffsetX() * this.getExtendedProgress(this.getProgress(ticks));
    }
    
    public float getOffsetY(final float ticks) {
        return this.pistonFacing.getFrontOffsetY() * this.getExtendedProgress(this.getProgress(ticks));
    }
    
    public float getOffsetZ(final float ticks) {
        return this.pistonFacing.getFrontOffsetZ() * this.getExtendedProgress(this.getProgress(ticks));
    }
    
    private float getExtendedProgress(final float p_184320_1_) {
        return this.extending ? (p_184320_1_ - 1.0f) : (1.0f - p_184320_1_);
    }
    
    public AxisAlignedBB getAABB(final IBlockAccess p_184321_1_, final BlockPos p_184321_2_) {
        return this.getAABB(p_184321_1_, p_184321_2_, this.progress).union(this.getAABB(p_184321_1_, p_184321_2_, this.lastProgress));
    }
    
    public AxisAlignedBB getAABB(final IBlockAccess p_184319_1_, final BlockPos p_184319_2_, float p_184319_3_) {
        p_184319_3_ = this.getExtendedProgress(p_184319_3_);
        final IBlockState iblockstate = this.func_190606_j();
        return iblockstate.getBoundingBox(p_184319_1_, p_184319_2_).offset(p_184319_3_ * this.pistonFacing.getFrontOffsetX(), p_184319_3_ * this.pistonFacing.getFrontOffsetY(), p_184319_3_ * this.pistonFacing.getFrontOffsetZ());
    }
    
    private IBlockState func_190606_j() {
        return (!this.isExtending() && this.shouldPistonHeadBeRendered()) ? Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.TYPE, (this.pistonState.getBlock() == Blocks.STICKY_PISTON) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT).withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, (EnumFacing)this.pistonState.getValue((IProperty<V>)BlockPistonBase.FACING)) : this.pistonState;
    }
    
    private void moveCollidedEntities(final float p_184322_1_) {
        final EnumFacing enumfacing = this.extending ? this.pistonFacing : this.pistonFacing.getOpposite();
        final double d0 = p_184322_1_ - this.progress;
        final List<AxisAlignedBB> list = (List<AxisAlignedBB>)Lists.newArrayList();
        this.func_190606_j().addCollisionBoxToList(this.world, BlockPos.ORIGIN, new AxisAlignedBB(BlockPos.ORIGIN), list, null, true);
        if (!list.isEmpty()) {
            final AxisAlignedBB axisalignedbb = this.func_190607_a(this.func_191515_a(list));
            final List<Entity> list2 = this.world.getEntitiesWithinAABBExcludingEntity(null, this.func_190610_a(axisalignedbb, enumfacing, d0).union(axisalignedbb));
            if (!list2.isEmpty()) {
                final boolean flag = this.pistonState.getBlock() == Blocks.SLIME_BLOCK;
                for (int i = 0; i < list2.size(); ++i) {
                    final Entity entity = list2.get(i);
                    if (entity.getPushReaction() != EnumPushReaction.IGNORE) {
                        if (flag) {
                            switch (enumfacing.getAxis()) {
                                case X: {
                                    entity.motionX = enumfacing.getFrontOffsetX();
                                    break;
                                }
                                case Y: {
                                    entity.motionY = enumfacing.getFrontOffsetY();
                                    break;
                                }
                                case Z: {
                                    entity.motionZ = enumfacing.getFrontOffsetZ();
                                    break;
                                }
                            }
                        }
                        double d2 = 0.0;
                        for (int j = 0; j < list.size(); ++j) {
                            final AxisAlignedBB axisalignedbb2 = this.func_190610_a(this.func_190607_a(list.get(j)), enumfacing, d0);
                            final AxisAlignedBB axisalignedbb3 = entity.getEntityBoundingBox();
                            if (axisalignedbb2.intersectsWith(axisalignedbb3)) {
                                d2 = Math.max(d2, this.func_190612_a(axisalignedbb2, enumfacing, axisalignedbb3));
                                if (d2 >= d0) {
                                    break;
                                }
                            }
                        }
                        if (d2 > 0.0) {
                            d2 = Math.min(d2, d0) + 0.01;
                            TileEntityPiston.field_190613_i.set(enumfacing);
                            entity.moveEntity(MoverType.PISTON, d2 * enumfacing.getFrontOffsetX(), d2 * enumfacing.getFrontOffsetY(), d2 * enumfacing.getFrontOffsetZ());
                            TileEntityPiston.field_190613_i.set(null);
                            if (!this.extending && this.shouldHeadBeRendered) {
                                this.func_190605_a(entity, enumfacing, d0);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private AxisAlignedBB func_191515_a(final List<AxisAlignedBB> p_191515_1_) {
        double d0 = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 1.0;
        double d5 = 1.0;
        double d6 = 1.0;
        for (final AxisAlignedBB axisalignedbb : p_191515_1_) {
            d0 = Math.min(axisalignedbb.minX, d0);
            d2 = Math.min(axisalignedbb.minY, d2);
            d3 = Math.min(axisalignedbb.minZ, d3);
            d4 = Math.max(axisalignedbb.maxX, d4);
            d5 = Math.max(axisalignedbb.maxY, d5);
            d6 = Math.max(axisalignedbb.maxZ, d6);
        }
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    private double func_190612_a(final AxisAlignedBB p_190612_1_, final EnumFacing p_190612_2_, final AxisAlignedBB p_190612_3_) {
        switch (p_190612_2_.getAxis()) {
            case X: {
                return func_190611_b(p_190612_1_, p_190612_2_, p_190612_3_);
            }
            default: {
                return func_190608_c(p_190612_1_, p_190612_2_, p_190612_3_);
            }
            case Z: {
                return func_190604_d(p_190612_1_, p_190612_2_, p_190612_3_);
            }
        }
    }
    
    private AxisAlignedBB func_190607_a(final AxisAlignedBB p_190607_1_) {
        final double d0 = this.getExtendedProgress(this.progress);
        return p_190607_1_.offset(this.pos.getX() + d0 * this.pistonFacing.getFrontOffsetX(), this.pos.getY() + d0 * this.pistonFacing.getFrontOffsetY(), this.pos.getZ() + d0 * this.pistonFacing.getFrontOffsetZ());
    }
    
    private AxisAlignedBB func_190610_a(final AxisAlignedBB p_190610_1_, final EnumFacing p_190610_2_, final double p_190610_3_) {
        final double d0 = p_190610_3_ * p_190610_2_.getAxisDirection().getOffset();
        final double d2 = Math.min(d0, 0.0);
        final double d3 = Math.max(d0, 0.0);
        switch (p_190610_2_) {
            case WEST: {
                return new AxisAlignedBB(p_190610_1_.minX + d2, p_190610_1_.minY, p_190610_1_.minZ, p_190610_1_.minX + d3, p_190610_1_.maxY, p_190610_1_.maxZ);
            }
            case EAST: {
                return new AxisAlignedBB(p_190610_1_.maxX + d2, p_190610_1_.minY, p_190610_1_.minZ, p_190610_1_.maxX + d3, p_190610_1_.maxY, p_190610_1_.maxZ);
            }
            case DOWN: {
                return new AxisAlignedBB(p_190610_1_.minX, p_190610_1_.minY + d2, p_190610_1_.minZ, p_190610_1_.maxX, p_190610_1_.minY + d3, p_190610_1_.maxZ);
            }
            default: {
                return new AxisAlignedBB(p_190610_1_.minX, p_190610_1_.maxY + d2, p_190610_1_.minZ, p_190610_1_.maxX, p_190610_1_.maxY + d3, p_190610_1_.maxZ);
            }
            case NORTH: {
                return new AxisAlignedBB(p_190610_1_.minX, p_190610_1_.minY, p_190610_1_.minZ + d2, p_190610_1_.maxX, p_190610_1_.maxY, p_190610_1_.minZ + d3);
            }
            case SOUTH: {
                return new AxisAlignedBB(p_190610_1_.minX, p_190610_1_.minY, p_190610_1_.maxZ + d2, p_190610_1_.maxX, p_190610_1_.maxY, p_190610_1_.maxZ + d3);
            }
        }
    }
    
    private void func_190605_a(final Entity p_190605_1_, final EnumFacing p_190605_2_, final double p_190605_3_) {
        final AxisAlignedBB axisalignedbb = p_190605_1_.getEntityBoundingBox();
        final AxisAlignedBB axisalignedbb2 = Block.FULL_BLOCK_AABB.offset(this.pos);
        if (axisalignedbb.intersectsWith(axisalignedbb2)) {
            final EnumFacing enumfacing = p_190605_2_.getOpposite();
            double d0 = this.func_190612_a(axisalignedbb2, enumfacing, axisalignedbb) + 0.01;
            final double d2 = this.func_190612_a(axisalignedbb2, enumfacing, axisalignedbb.func_191500_a(axisalignedbb2)) + 0.01;
            if (Math.abs(d0 - d2) < 0.01) {
                d0 = Math.min(d0, p_190605_3_) + 0.01;
                TileEntityPiston.field_190613_i.set(p_190605_2_);
                p_190605_1_.moveEntity(MoverType.PISTON, d0 * enumfacing.getFrontOffsetX(), d0 * enumfacing.getFrontOffsetY(), d0 * enumfacing.getFrontOffsetZ());
                TileEntityPiston.field_190613_i.set(null);
            }
        }
    }
    
    private static double func_190611_b(final AxisAlignedBB p_190611_0_, final EnumFacing p_190611_1_, final AxisAlignedBB p_190611_2_) {
        return (p_190611_1_.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? (p_190611_0_.maxX - p_190611_2_.minX) : (p_190611_2_.maxX - p_190611_0_.minX);
    }
    
    private static double func_190608_c(final AxisAlignedBB p_190608_0_, final EnumFacing p_190608_1_, final AxisAlignedBB p_190608_2_) {
        return (p_190608_1_.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? (p_190608_0_.maxY - p_190608_2_.minY) : (p_190608_2_.maxY - p_190608_0_.minY);
    }
    
    private static double func_190604_d(final AxisAlignedBB p_190604_0_, final EnumFacing p_190604_1_, final AxisAlignedBB p_190604_2_) {
        return (p_190604_1_.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? (p_190604_0_.maxZ - p_190604_2_.minZ) : (p_190604_2_.maxZ - p_190604_0_.minZ);
    }
    
    public void clearPistonTileEntity() {
        if (this.lastProgress < 1.0f && this.world != null) {
            this.progress = 1.0f;
            this.lastProgress = this.progress;
            this.world.removeTileEntity(this.pos);
            this.invalidate();
            if (this.world.getBlockState(this.pos).getBlock() == Blocks.PISTON_EXTENSION) {
                this.world.setBlockState(this.pos, this.pistonState, 3);
                this.world.func_190524_a(this.pos, this.pistonState.getBlock(), this.pos);
            }
        }
    }
    
    @Override
    public void update() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0f) {
            this.world.removeTileEntity(this.pos);
            this.invalidate();
            if (this.world.getBlockState(this.pos).getBlock() == Blocks.PISTON_EXTENSION) {
                this.world.setBlockState(this.pos, this.pistonState, 3);
                this.world.func_190524_a(this.pos, this.pistonState.getBlock(), this.pos);
            }
        }
        else {
            final float f = this.progress + 0.5f;
            this.moveCollidedEntities(f);
            this.progress = f;
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            }
        }
    }
    
    public static void registerFixesPiston(final DataFixer fixer) {
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.pistonState = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
        this.pistonFacing = EnumFacing.getFront(compound.getInteger("facing"));
        this.progress = compound.getFloat("progress");
        this.lastProgress = this.progress;
        this.extending = compound.getBoolean("extending");
        this.shouldHeadBeRendered = compound.getBoolean("source");
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
        compound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
        compound.setInteger("facing", this.pistonFacing.getIndex());
        compound.setFloat("progress", this.lastProgress);
        compound.setBoolean("extending", this.extending);
        compound.setBoolean("source", this.shouldHeadBeRendered);
        return compound;
    }
    
    public void func_190609_a(final World p_190609_1_, final BlockPos p_190609_2_, final AxisAlignedBB p_190609_3_, final List<AxisAlignedBB> p_190609_4_, @Nullable final Entity p_190609_5_) {
        if (!this.extending && this.shouldHeadBeRendered) {
            this.pistonState.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true).addCollisionBoxToList(p_190609_1_, p_190609_2_, p_190609_3_, p_190609_4_, p_190609_5_, false);
        }
        final EnumFacing enumfacing = TileEntityPiston.field_190613_i.get();
        if (this.progress >= 1.0 || enumfacing != (this.extending ? this.pistonFacing : this.pistonFacing.getOpposite())) {
            final int i = p_190609_4_.size();
            IBlockState iblockstate;
            if (this.shouldPistonHeadBeRendered()) {
                iblockstate = Blocks.PISTON_HEAD.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, this.pistonFacing).withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, this.extending ^ 1.0f - this.progress < 0.25f);
            }
            else {
                iblockstate = this.pistonState;
            }
            final float f = this.getExtendedProgress(this.progress);
            final double d0 = this.pistonFacing.getFrontOffsetX() * f;
            final double d2 = this.pistonFacing.getFrontOffsetY() * f;
            final double d3 = this.pistonFacing.getFrontOffsetZ() * f;
            iblockstate.addCollisionBoxToList(p_190609_1_, p_190609_2_, p_190609_3_.offset(-d0, -d2, -d3), p_190609_4_, p_190609_5_, true);
            for (int j = i; j < p_190609_4_.size(); ++j) {
                p_190609_4_.set(j, p_190609_4_.get(j).offset(d0, d2, d3));
            }
        }
    }
}
