// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSign extends Item
{
    public ItemSign() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final boolean flag = iblockstate.getBlock().isReplaceable(playerIn, worldIn);
        if (hand == EnumFacing.DOWN || (!iblockstate.getMaterial().isSolid() && !flag) || (flag && hand != EnumFacing.UP)) {
            return EnumActionResult.FAIL;
        }
        worldIn = worldIn.offset(hand);
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn, hand, itemstack) || !Blocks.STANDING_SIGN.canPlaceBlockAt(playerIn, worldIn)) {
            return EnumActionResult.FAIL;
        }
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        worldIn = (flag ? worldIn.down() : worldIn);
        if (hand == EnumFacing.UP) {
            final int i = MathHelper.floor((stack.rotationYaw + 180.0f) * 16.0f / 360.0f + 0.5) & 0xF;
            playerIn.setBlockState(worldIn, Blocks.STANDING_SIGN.getDefaultState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, i), 11);
        }
        else {
            playerIn.setBlockState(worldIn, Blocks.WALL_SIGN.getDefaultState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, hand), 11);
        }
        final TileEntity tileentity = playerIn.getTileEntity(worldIn);
        if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(playerIn, stack, worldIn, itemstack)) {
            stack.openEditSign((TileEntitySign)tileentity);
        }
        if (stack instanceof EntityPlayerMP) {
            CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
        }
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }
}
