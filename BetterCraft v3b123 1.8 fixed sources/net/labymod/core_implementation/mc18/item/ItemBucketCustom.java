// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.item;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.labymod.api.protocol.liquid.FixedLiquidBucketProtocol;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.MovingObjectPosition;
import net.labymod.api.permissions.Permissions;
import net.labymod.main.LabyMod;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;

public class ItemBucketCustom extends ItemBucket
{
    private Block isFull;
    
    public ItemBucketCustom(final Block containedBlock) {
        super(containedBlock);
        this.isFull = containedBlock;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final boolean flag = this.isFull == Blocks.air;
        final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, flag);
        if (movingobjectposition == null) {
            return itemStackIn;
        }
        final boolean allowed = worldIn.isRemote && LabyMod.getSettings().improvedLavaFixedGhostBlocks && Permissions.isAllowed(Permissions.Permission.IMPROVED_LAVA);
        if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final BlockPos blockpos = movingobjectposition.getBlockPos();
            if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
                return itemStackIn;
            }
            if (flag) {
                if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn)) {
                    return itemStackIn;
                }
                final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                final Material material = iblockstate.getBlock().getMaterial();
                if (material == Material.water && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) {
                    if (!allowed) {
                        worldIn.setBlockToAir(blockpos);
                        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        return this.fillBucket(itemStackIn, playerIn, Items.water_bucket);
                    }
                    FixedLiquidBucketProtocol.handleBucketAction(FixedLiquidBucketProtocol.Action.FILL_BUCKET, blockpos.getX(), blockpos.getY(), blockpos.getZ());
                }
                if (material == Material.lava && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) {
                    if (!allowed) {
                        worldIn.setBlockToAir(blockpos);
                        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        return this.fillBucket(itemStackIn, playerIn, Items.lava_bucket);
                    }
                    FixedLiquidBucketProtocol.handleBucketAction(FixedLiquidBucketProtocol.Action.FILL_BUCKET, blockpos.getX(), blockpos.getY(), blockpos.getZ());
                }
            }
            else {
                if (this.isFull == Blocks.air) {
                    return new ItemStack(Items.bucket);
                }
                final BlockPos blockpos2 = blockpos.offset(movingobjectposition.sideHit);
                if (!playerIn.canPlayerEdit(blockpos2, movingobjectposition.sideHit, itemStackIn)) {
                    return itemStackIn;
                }
                if (this.tryPlaceContainedLiquid(worldIn, blockpos2) && !playerIn.capabilities.isCreativeMode && !allowed) {
                    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                    return new ItemStack(Items.bucket);
                }
            }
        }
        return itemStackIn;
    }
    
    private ItemStack fillBucket(final ItemStack emptyBuckets, final EntityPlayer player, final Item fullBucket) {
        if (player.capabilities.isCreativeMode) {
            return emptyBuckets;
        }
        if (--emptyBuckets.stackSize <= 0) {
            return new ItemStack(fullBucket);
        }
        if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket))) {
            player.dropPlayerItemWithRandomChoice(new ItemStack(fullBucket, 1, 0), false);
        }
        return emptyBuckets;
    }
    
    @Override
    public boolean tryPlaceContainedLiquid(final World worldIn, final BlockPos pos) {
        if (this.isFull == Blocks.air) {
            return false;
        }
        final Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
        final boolean flag = !material.isSolid();
        if (!worldIn.isAirBlock(pos) && !flag) {
            return false;
        }
        if (worldIn.provider.doesWaterVaporize() && this.isFull == Blocks.flowing_water) {
            final int i = pos.getX();
            final int j = pos.getY();
            final int k = pos.getZ();
            worldIn.playSoundEffect(i + 0.5f, j + 0.5f, k + 0.5f, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
            for (int l = 0; l < 8; ++l) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        else {
            if (!worldIn.isRemote && flag && !material.isLiquid()) {
                worldIn.destroyBlock(pos, true);
            }
            final boolean allowed = worldIn.isRemote && LabyMod.getSettings().improvedLavaFixedGhostBlocks && Permissions.isAllowed(Permissions.Permission.IMPROVED_LAVA);
            if (allowed) {
                FixedLiquidBucketProtocol.handleBucketAction(FixedLiquidBucketProtocol.Action.EMPTY_BUCKET, pos.getX(), pos.getY(), pos.getZ());
            }
            else {
                worldIn.setBlockState(pos, this.isFull.getDefaultState(), 3);
            }
        }
        return true;
    }
}
