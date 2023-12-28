// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemRedstone extends Item
{
    public ItemRedstone() {
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final boolean flag = playerIn.getBlockState(worldIn).getBlock().isReplaceable(playerIn, worldIn);
        final BlockPos blockpos = flag ? worldIn : worldIn.offset(hand);
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (stack.canPlayerEdit(blockpos, hand, itemstack) && playerIn.func_190527_a(playerIn.getBlockState(blockpos).getBlock(), blockpos, false, hand, null) && Blocks.REDSTONE_WIRE.canPlaceBlockAt(playerIn, blockpos)) {
            playerIn.setBlockState(blockpos, Blocks.REDSTONE_WIRE.getDefaultState());
            if (stack instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, blockpos, itemstack);
            }
            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
}
