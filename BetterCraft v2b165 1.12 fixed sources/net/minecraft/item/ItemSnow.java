// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.block.SoundType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSnow;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemSnow extends ItemBlock
{
    public ItemSnow(final Block block) {
        super(block);
        this.setMaxDamage(0);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!itemstack.func_190926_b() && stack.canPlayerEdit(worldIn, hand, itemstack)) {
            IBlockState iblockstate = playerIn.getBlockState(worldIn);
            Block block = iblockstate.getBlock();
            BlockPos blockpos = worldIn;
            if ((hand != EnumFacing.UP || block != this.block) && !block.isReplaceable(playerIn, worldIn)) {
                blockpos = worldIn.offset(hand);
                iblockstate = playerIn.getBlockState(blockpos);
                block = iblockstate.getBlock();
            }
            if (block == this.block) {
                final int i = iblockstate.getValue((IProperty<Integer>)BlockSnow.LAYERS);
                if (i < 8) {
                    final IBlockState iblockstate2 = iblockstate.withProperty((IProperty<Comparable>)BlockSnow.LAYERS, i + 1);
                    final AxisAlignedBB axisalignedbb = iblockstate2.getCollisionBoundingBox(playerIn, blockpos);
                    if (axisalignedbb != Block.NULL_AABB && playerIn.checkNoEntityCollision(axisalignedbb.offset(blockpos)) && playerIn.setBlockState(blockpos, iblockstate2, 10)) {
                        final SoundType soundtype = this.block.getSoundType();
                        playerIn.playSound(stack, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
                        if (stack instanceof EntityPlayerMP) {
                            CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
                        }
                        itemstack.func_190918_g(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
            return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY);
        }
        return EnumActionResult.FAIL;
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
}
