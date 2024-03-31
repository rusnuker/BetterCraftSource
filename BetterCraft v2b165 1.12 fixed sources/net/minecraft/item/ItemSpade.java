// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.Set;

public class ItemSpade extends ItemTool
{
    private static final Set<Block> EFFECTIVE_ON;
    
    static {
        EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.field_192444_dS);
    }
    
    public ItemSpade(final ToolMaterial material) {
        super(1.5f, -3.0f, material, ItemSpade.EFFECTIVE_ON);
    }
    
    @Override
    public boolean canHarvestBlock(final IBlockState blockIn) {
        final Block block = blockIn.getBlock();
        return block == Blocks.SNOW_LAYER || block == Blocks.SNOW;
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final Block block = iblockstate.getBlock();
        if (hand != EnumFacing.DOWN && playerIn.getBlockState(worldIn.up()).getMaterial() == Material.AIR && block == Blocks.GRASS) {
            final IBlockState iblockstate2 = Blocks.GRASS_PATH.getDefaultState();
            playerIn.playSound(stack, worldIn, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0f, 1.0f);
            if (!playerIn.isRemote) {
                playerIn.setBlockState(worldIn, iblockstate2, 11);
                itemstack.damageItem(1, stack);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
