// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemBlock extends Item
{
    protected final Block block;
    
    public ItemBlock(final Block block) {
        this.block = block;
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final Block block = iblockstate.getBlock();
        if (!block.isReplaceable(playerIn, worldIn)) {
            worldIn = worldIn.offset(hand);
        }
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!itemstack.func_190926_b() && stack.canPlayerEdit(worldIn, hand, itemstack) && playerIn.func_190527_a(this.block, worldIn, false, hand, null)) {
            final int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate2 = this.block.onBlockPlaced(playerIn, worldIn, hand, facing, hitX, hitY, i, stack);
            if (playerIn.setBlockState(worldIn, iblockstate2, 11)) {
                iblockstate2 = playerIn.getBlockState(worldIn);
                if (iblockstate2.getBlock() == this.block) {
                    setTileEntityNBT(playerIn, stack, worldIn, itemstack);
                    this.block.onBlockPlacedBy(playerIn, worldIn, iblockstate2, stack, itemstack);
                    if (stack instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
                    }
                }
                final SoundType soundtype = this.block.getSoundType();
                playerIn.playSound(stack, worldIn, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
                itemstack.func_190918_g(1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
    
    public static boolean setTileEntityNBT(final World worldIn, @Nullable final EntityPlayer player, final BlockPos pos, final ItemStack stackIn) {
        final MinecraftServer minecraftserver = worldIn.getMinecraftServer();
        if (minecraftserver == null) {
            return false;
        }
        final NBTTagCompound nbttagcompound = stackIn.getSubCompound("BlockEntityTag");
        if (nbttagcompound != null) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity != null) {
                if (!worldIn.isRemote && tileentity.onlyOpsCanSetNbt() && (player == null || !player.canUseCommandBlock())) {
                    return false;
                }
                final NBTTagCompound nbttagcompound2 = tileentity.writeToNBT(new NBTTagCompound());
                final NBTTagCompound nbttagcompound3 = nbttagcompound2.copy();
                nbttagcompound2.merge(nbttagcompound);
                nbttagcompound2.setInteger("x", pos.getX());
                nbttagcompound2.setInteger("y", pos.getY());
                nbttagcompound2.setInteger("z", pos.getZ());
                if (!nbttagcompound2.equals(nbttagcompound3)) {
                    tileentity.readFromNBT(nbttagcompound2);
                    tileentity.markDirty();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean canPlaceBlockOnSide(final World worldIn, BlockPos pos, EnumFacing side, final EntityPlayer player, final ItemStack stack) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.SNOW_LAYER) {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        return worldIn.func_190527_a(this.block, pos, false, side, null);
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return this.block.getUnlocalizedName();
    }
    
    @Override
    public String getUnlocalizedName() {
        return this.block.getUnlocalizedName();
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
        return this.block.getCreativeTabToDisplayOn();
    }
    
    @Override
    public void getSubItems(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            this.block.getSubBlocks(itemIn, tab);
        }
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World playerIn, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        this.block.func_190948_a(stack, playerIn, tooltip, advanced);
    }
    
    public Block getBlock() {
        return this.block;
    }
}
