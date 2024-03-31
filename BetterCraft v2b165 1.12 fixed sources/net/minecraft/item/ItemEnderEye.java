// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ActionResult;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEnderEye extends Item
{
    public ItemEnderEye() {
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack) || iblockstate.getBlock() != Blocks.END_PORTAL_FRAME || iblockstate.getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE)) {
            return EnumActionResult.FAIL;
        }
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        playerIn.setBlockState(worldIn, iblockstate.withProperty((IProperty<Comparable>)BlockEndPortalFrame.EYE, true), 2);
        playerIn.updateComparatorOutputLevel(worldIn, Blocks.END_PORTAL_FRAME);
        itemstack.func_190918_g(1);
        for (int i = 0; i < 16; ++i) {
            final double d0 = worldIn.getX() + (5.0f + ItemEnderEye.itemRand.nextFloat() * 6.0f) / 16.0f;
            final double d2 = worldIn.getY() + 0.8125f;
            final double d3 = worldIn.getZ() + (5.0f + ItemEnderEye.itemRand.nextFloat() * 6.0f) / 16.0f;
            final double d4 = 0.0;
            final double d5 = 0.0;
            final double d6 = 0.0;
            playerIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
        playerIn.playSound(null, worldIn, SoundEvents.field_193781_bp, SoundCategory.BLOCKS, 1.0f, 1.0f);
        final BlockPattern.PatternHelper blockpattern$patternhelper = BlockEndPortalFrame.getOrCreatePortalShape().match(playerIn, worldIn);
        if (blockpattern$patternhelper != null) {
            final BlockPos blockpos = blockpattern$patternhelper.getFrontTopLeft().add(-3, 0, -3);
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 3; ++k) {
                    playerIn.setBlockState(blockpos.add(j, 0, k), Blocks.END_PORTAL.getDefaultState(), 2);
                }
            }
            playerIn.playBroadcastSound(1038, blockpos.add(1, 0, 1), 0);
        }
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World itemStackIn, final EntityPlayer worldIn, final EnumHand playerIn) {
        final ItemStack itemstack = worldIn.getHeldItem(playerIn);
        final RayTraceResult raytraceresult = this.rayTrace(itemStackIn, worldIn, false);
        if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && itemStackIn.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        worldIn.setActiveHand(playerIn);
        if (!itemStackIn.isRemote) {
            final BlockPos blockpos = ((WorldServer)itemStackIn).getChunkProvider().getStrongholdGen(itemStackIn, "Stronghold", new BlockPos(worldIn), false);
            if (blockpos != null) {
                final EntityEnderEye entityendereye = new EntityEnderEye(itemStackIn, worldIn.posX, worldIn.posY + worldIn.height / 2.0f, worldIn.posZ);
                entityendereye.moveTowards(blockpos);
                itemStackIn.spawnEntityInWorld(entityendereye);
                if (worldIn instanceof EntityPlayerMP) {
                    CriteriaTriggers.field_192132_l.func_192239_a((EntityPlayerMP)worldIn, blockpos);
                }
                itemStackIn.playSound(null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 0.5f, 0.4f / (ItemEnderEye.itemRand.nextFloat() * 0.4f + 0.8f));
                itemStackIn.playEvent(null, 1003, new BlockPos(worldIn), 0);
                if (!worldIn.capabilities.isCreativeMode) {
                    itemstack.func_190918_g(1);
                }
                worldIn.addStat(StatList.getObjectUseStats(this));
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
