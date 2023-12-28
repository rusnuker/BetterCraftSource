// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.world.end.DragonFightManager;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEndCrystal extends Item
{
    public ItemEndCrystal() {
        this.setUnlocalizedName("end_crystal");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        if (iblockstate.getBlock() != Blocks.OBSIDIAN && iblockstate.getBlock() != Blocks.BEDROCK) {
            return EnumActionResult.FAIL;
        }
        final BlockPos blockpos = worldIn.up();
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(blockpos, hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final BlockPos blockpos2 = blockpos.up();
        boolean flag = !playerIn.isAirBlock(blockpos) && !playerIn.getBlockState(blockpos).getBlock().isReplaceable(playerIn, blockpos);
        flag |= (!playerIn.isAirBlock(blockpos2) && !playerIn.getBlockState(blockpos2).getBlock().isReplaceable(playerIn, blockpos2));
        if (flag) {
            return EnumActionResult.FAIL;
        }
        final double d0 = blockpos.getX();
        final double d2 = blockpos.getY();
        final double d3 = blockpos.getZ();
        final List<Entity> list = playerIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d2, d3, d0 + 1.0, d2 + 2.0, d3 + 1.0));
        if (!list.isEmpty()) {
            return EnumActionResult.FAIL;
        }
        if (!playerIn.isRemote) {
            final EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(playerIn, worldIn.getX() + 0.5f, worldIn.getY() + 1, worldIn.getZ() + 0.5f);
            entityendercrystal.setShowBottom(false);
            playerIn.spawnEntityInWorld(entityendercrystal);
            if (playerIn.provider instanceof WorldProviderEnd) {
                final DragonFightManager dragonfightmanager = ((WorldProviderEnd)playerIn.provider).getDragonFightManager();
                dragonfightmanager.respawnDragon();
            }
        }
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }
}
