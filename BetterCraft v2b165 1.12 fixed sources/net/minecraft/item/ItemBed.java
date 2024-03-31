// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.util.NonNullList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.SoundCategory;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBed extends Item
{
    public ItemBed() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (hand != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final Block block = iblockstate.getBlock();
        final boolean flag = block.isReplaceable(playerIn, worldIn);
        if (!flag) {
            worldIn = worldIn.up();
        }
        final int i = MathHelper.floor(stack.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        final EnumFacing enumfacing = EnumFacing.getHorizontal(i);
        final BlockPos blockpos = worldIn.offset(enumfacing);
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn, hand, itemstack) || !stack.canPlayerEdit(blockpos, hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate2 = playerIn.getBlockState(blockpos);
        final boolean flag2 = iblockstate2.getBlock().isReplaceable(playerIn, blockpos);
        final boolean flag3 = flag || playerIn.isAirBlock(worldIn);
        final boolean flag4 = flag2 || playerIn.isAirBlock(blockpos);
        if (flag3 && flag4 && playerIn.getBlockState(worldIn.down()).isFullyOpaque() && playerIn.getBlockState(blockpos.down()).isFullyOpaque()) {
            final IBlockState iblockstate3 = Blocks.BED.getDefaultState().withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false).withProperty((IProperty<Comparable>)BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
            playerIn.setBlockState(worldIn, iblockstate3, 10);
            playerIn.setBlockState(blockpos, iblockstate3.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
            final SoundType soundtype = iblockstate3.getBlock().getSoundType();
            playerIn.playSound(null, worldIn, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
            final TileEntity tileentity = playerIn.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityBed) {
                ((TileEntityBed)tileentity).func_193051_a(itemstack);
            }
            final TileEntity tileentity2 = playerIn.getTileEntity(worldIn);
            if (tileentity2 instanceof TileEntityBed) {
                ((TileEntityBed)tileentity2).func_193051_a(itemstack);
            }
            playerIn.notifyNeighborsRespectDebug(worldIn, block, false);
            playerIn.notifyNeighborsRespectDebug(blockpos, iblockstate2.getBlock(), false);
            if (stack instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
            }
            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
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
