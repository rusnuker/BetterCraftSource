// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.util.NonNullList;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.material.Material;
import net.minecraft.block.IGrowable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockOldLog;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemDye extends Item
{
    public static final int[] DYE_COLORS;
    
    static {
        DYE_COLORS = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320 };
    }
    
    public ItemDye() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        final int i = stack.getMetadata();
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumDyeColor.byDyeDamage(i).getUnlocalizedName();
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());
        if (enumdyecolor == EnumDyeColor.WHITE) {
            if (applyBonemeal(itemstack, playerIn, worldIn)) {
                if (!playerIn.isRemote) {
                    playerIn.playEvent(2005, worldIn, 0);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        else if (enumdyecolor == EnumDyeColor.BROWN) {
            final IBlockState iblockstate = playerIn.getBlockState(worldIn);
            final Block block = iblockstate.getBlock();
            if (block == Blocks.LOG && iblockstate.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
                if (hand == EnumFacing.DOWN || hand == EnumFacing.UP) {
                    return EnumActionResult.FAIL;
                }
                worldIn = worldIn.offset(hand);
                if (playerIn.isAirBlock(worldIn)) {
                    final IBlockState iblockstate2 = Blocks.COCOA.onBlockPlaced(playerIn, worldIn, hand, facing, hitX, hitY, 0, stack);
                    playerIn.setBlockState(worldIn, iblockstate2, 10);
                    if (!stack.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
            return EnumActionResult.FAIL;
        }
        return EnumActionResult.PASS;
    }
    
    public static boolean applyBonemeal(final ItemStack stack, final World worldIn, final BlockPos target) {
        final IBlockState iblockstate = worldIn.getBlockState(target);
        if (iblockstate.getBlock() instanceof IGrowable) {
            final IGrowable igrowable = (IGrowable)iblockstate.getBlock();
            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                    }
                    stack.func_190918_g(1);
                }
                return true;
            }
        }
        return false;
    }
    
    public static void spawnBonemealParticles(final World worldIn, final BlockPos pos, int amount) {
        if (amount == 0) {
            amount = 15;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getMaterial() != Material.AIR) {
            for (int i = 0; i < amount; ++i) {
                final double d0 = ItemDye.itemRand.nextGaussian() * 0.02;
                final double d2 = ItemDye.itemRand.nextGaussian() * 0.02;
                final double d3 = ItemDye.itemRand.nextGaussian() * 0.02;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, pos.getX() + ItemDye.itemRand.nextFloat(), pos.getY() + ItemDye.itemRand.nextFloat() * iblockstate.getBoundingBox(worldIn, pos).maxY, pos.getZ() + ItemDye.itemRand.nextFloat(), d0, d2, d3, new int[0]);
            }
        }
    }
    
    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target, final EnumHand hand) {
        if (target instanceof EntitySheep) {
            final EntitySheep entitysheep = (EntitySheep)target;
            final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor) {
                entitysheep.setFleeceColor(enumdyecolor);
                stack.func_190918_g(1);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void getSubItems(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            for (int i = 0; i < 16; ++i) {
                tab.add(new ItemStack(this, 1, i));
            }
        }
    }
}
