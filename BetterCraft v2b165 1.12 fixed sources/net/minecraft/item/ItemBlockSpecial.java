// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemBlockSpecial extends Item
{
    private final Block block;
    
    public ItemBlockSpecial(final Block block) {
        this.block = block;
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final Block block = iblockstate.getBlock();
        if (block == Blocks.SNOW_LAYER && iblockstate.getValue((IProperty<Integer>)BlockSnow.LAYERS) < 1) {
            hand = EnumFacing.UP;
        }
        else if (!block.isReplaceable(playerIn, worldIn)) {
            worldIn = worldIn.offset(hand);
        }
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (itemstack.func_190926_b() || !stack.canPlayerEdit(worldIn, hand, itemstack) || !playerIn.func_190527_a(this.block, worldIn, false, hand, null)) {
            return EnumActionResult.FAIL;
        }
        IBlockState iblockstate2 = this.block.onBlockPlaced(playerIn, worldIn, hand, facing, hitX, hitY, 0, stack);
        if (!playerIn.setBlockState(worldIn, iblockstate2, 11)) {
            return EnumActionResult.FAIL;
        }
        iblockstate2 = playerIn.getBlockState(worldIn);
        if (iblockstate2.getBlock() == this.block) {
            ItemBlock.setTileEntityNBT(playerIn, stack, worldIn, itemstack);
            iblockstate2.getBlock().onBlockPlacedBy(playerIn, worldIn, iblockstate2, stack, itemstack);
            if (stack instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
            }
        }
        final SoundType soundtype = this.block.getSoundType();
        playerIn.playSound(stack, worldIn, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }
}
