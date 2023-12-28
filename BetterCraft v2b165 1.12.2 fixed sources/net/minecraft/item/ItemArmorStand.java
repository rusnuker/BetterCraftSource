// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.util.math.Rotations;
import java.util.Random;
import java.util.List;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemArmorStand extends Item
{
    public ItemArmorStand() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        if (hand == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        }
        final boolean flag = playerIn.getBlockState(worldIn).getBlock().isReplaceable(playerIn, worldIn);
        final BlockPos blockpos = flag ? worldIn : worldIn.offset(hand);
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(blockpos, hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final BlockPos blockpos2 = blockpos.up();
        boolean flag2 = !playerIn.isAirBlock(blockpos) && !playerIn.getBlockState(blockpos).getBlock().isReplaceable(playerIn, blockpos);
        flag2 |= (!playerIn.isAirBlock(blockpos2) && !playerIn.getBlockState(blockpos2).getBlock().isReplaceable(playerIn, blockpos2));
        if (flag2) {
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
            playerIn.setBlockToAir(blockpos);
            playerIn.setBlockToAir(blockpos2);
            final EntityArmorStand entityarmorstand = new EntityArmorStand(playerIn, d0 + 0.5, d2, d3 + 0.5);
            final float f = MathHelper.floor((MathHelper.wrapDegrees(stack.rotationYaw - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            entityarmorstand.setLocationAndAngles(d0 + 0.5, d2, d3 + 0.5, f, 0.0f);
            this.applyRandomRotations(entityarmorstand, playerIn.rand);
            ItemMonsterPlacer.applyItemEntityDataToEntity(playerIn, stack, itemstack, entityarmorstand);
            playerIn.spawnEntityInWorld(entityarmorstand);
            playerIn.playSound(null, entityarmorstand.posX, entityarmorstand.posY, entityarmorstand.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f);
        }
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }
    
    private void applyRandomRotations(final EntityArmorStand armorStand, final Random rand) {
        Rotations rotations = armorStand.getHeadRotation();
        float f = rand.nextFloat() * 5.0f;
        final float f2 = rand.nextFloat() * 20.0f - 10.0f;
        Rotations rotations2 = new Rotations(rotations.getX() + f, rotations.getY() + f2, rotations.getZ());
        armorStand.setHeadRotation(rotations2);
        rotations = armorStand.getBodyRotation();
        f = rand.nextFloat() * 10.0f - 5.0f;
        rotations2 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        armorStand.setBodyRotation(rotations2);
    }
}
