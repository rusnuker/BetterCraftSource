// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.block;

import net.minecraft.util.EnumBlockRenderType;
import java.util.Random;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.client.Minecraft;
import com.TominoCZ.FBP.FBP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import com.TominoCZ.FBP.particle.FBPParticleBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import com.TominoCZ.FBP.material.FBPMaterial;
import com.TominoCZ.FBP.node.FBPBlockNode;
import net.minecraft.util.math.BlockPos;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;

public class FBPAnimationDummyBlock extends Block
{
    public ConcurrentHashMap<BlockPos, FBPBlockNode> blockNodes;
    
    public FBPAnimationDummyBlock() {
        super(new FBPMaterial());
        this.blockNodes = new ConcurrentHashMap<BlockPos, FBPBlockNode>();
        this.setUnlocalizedName("FBPPlaceholderBlock");
        this.translucent = true;
    }
    
    public void copyState(final World w, final BlockPos pos, final IBlockState state, final FBPParticleBlock p) {
        if (this.blockNodes.containsKey(pos)) {
            return;
        }
        this.blockNodes.put(pos, new FBPBlockNode(state, p));
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (this.blockNodes.containsKey(pos)) {
            final FBPBlockNode n = this.blockNodes.get(pos);
            try {
                return n.originalBlock.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
            }
            catch (final Throwable t) {
                return false;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean isAir() {
        return this == Blocks.AIR;
    }
    
    @Override
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        try {
            if (this.blockNodes.containsKey(pos)) {
                final FBPBlockNode n = this.blockNodes.get(pos);
                return n.state.getMaterial().isReplaceable();
            }
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
        return this.blockMaterial.isReplaceable();
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        try {
            if (this.blockNodes.containsKey(pos)) {
                final FBPBlockNode n = this.blockNodes.get(pos);
                return n.originalBlock.isPassable(worldIn, pos);
            }
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
        return !this.blockMaterial.blocksMovement();
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        try {
            if (this.blockNodes.containsKey(pos)) {
                final FBPBlockNode n = this.blockNodes.get(pos);
                n.originalBlock.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
            }
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        try {
            if (this.blockNodes.containsKey(pos)) {
                final FBPBlockNode n = this.blockNodes.get(pos);
                return n.state.getCollisionBoundingBox(worldIn, pos);
            }
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
        return Block.FULL_BLOCK_AABB.offset(pos);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        try {
            if (this.blockNodes.containsKey(pos)) {
                final FBPBlockNode n = this.blockNodes.get(pos);
                return n.state.getBoundingBox(worldIn, pos);
            }
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
        return Block.FULL_BLOCK_AABB.offset(pos);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    
    @Override
    public float getBlockHardness(final IBlockState blockState, final World w, final BlockPos pos) {
        try {
            if (this.blockNodes.containsKey(pos)) {
                final FBPBlockNode n = this.blockNodes.get(pos);
                return n.state.getBlockHardness(w, pos);
            }
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
        return blockState.getBlockHardness(w, pos);
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        try {
            final FBPBlockNode node = FBP.FBPBlock.blockNodes.get(pos);
            if (node == null) {
                return;
            }
            if (worldIn.isRemote && state.getBlock() != node.originalBlock && (worldIn.getBlockState(pos).getBlock() instanceof FBPAnimationDummyBlock || state.getBlock() instanceof FBPAnimationDummyBlock)) {
                Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos, node.originalBlock.getStateFromMeta(node.meta));
            }
            if (node.particle != null) {
                node.particle.killParticle();
            }
            FBP.INSTANCE.eventHandler.removePosEntry(pos);
        }
        catch (final Throwable t) {
            t.printStackTrace();
        }
    }
    
    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean b) {
        try {
            if (this.blockNodes.containsKey(pos)) {
                this.blockNodes.get(pos).state.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, b);
            }
        }
        catch (final Exception ex) {}
    }
    
    @Override
    public float getExplosionResistance(final Entity e) {
        if (this.blockNodes.containsKey(e.getPosition())) {
            return this.blockNodes.get(e.getPosition()).originalBlock.getExplosionResistance(e);
        }
        return super.getExplosionResistance(e);
    }
    
    @Override
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (this.blockNodes.containsKey(pos)) {
            return this.blockNodes.get(pos).state.getWeakPower(blockAccess, pos, side);
        }
        return 0;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return this.blockNodes.containsKey(pos) && this.blockNodes.get(pos).originalBlock.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return this.blockNodes.containsKey(pos) && this.blockNodes.get(pos).originalBlock.canPlaceBlockOnSide(worldIn, pos, side);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        try {
            if (this.blockNodes.containsKey(pos)) {
                new ItemStack(Item.getItemFromBlock(this.blockNodes.get(pos).originalBlock), 1, this.damageDropped(state));
            }
            return new ItemStack(Item.getItemFromBlock(this), 1, this.damageDropped(state));
        }
        catch (final Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.blockNodes.containsKey(pos)) {
            this.blockNodes.get(pos).originalBlock.onBlockAdded(worldIn, pos, state);
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random r, final int i) {
        return null;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public float getAmbientOcclusionLightValue(final IBlockState state) {
        return 1.0f;
    }
}
