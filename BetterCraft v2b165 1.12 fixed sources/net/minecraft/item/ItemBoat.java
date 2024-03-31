// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import net.minecraft.stats.StatList;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityBoat;

public class ItemBoat extends Item
{
    private final EntityBoat.Type type;
    
    public ItemBoat(final EntityBoat.Type typeIn) {
        this.type = typeIn;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setUnlocalizedName("boat." + typeIn.getName());
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World itemStackIn, final EntityPlayer worldIn, final EnumHand playerIn) {
        final ItemStack itemstack = worldIn.getHeldItem(playerIn);
        final float f = 1.0f;
        final float f2 = worldIn.prevRotationPitch + (worldIn.rotationPitch - worldIn.prevRotationPitch) * 1.0f;
        final float f3 = worldIn.prevRotationYaw + (worldIn.rotationYaw - worldIn.prevRotationYaw) * 1.0f;
        final double d0 = worldIn.prevPosX + (worldIn.posX - worldIn.prevPosX) * 1.0;
        final double d2 = worldIn.prevPosY + (worldIn.posY - worldIn.prevPosY) * 1.0 + worldIn.getEyeHeight();
        final double d3 = worldIn.prevPosZ + (worldIn.posZ - worldIn.prevPosZ) * 1.0;
        final Vec3d vec3d = new Vec3d(d0, d2, d3);
        final float f4 = MathHelper.cos(-f3 * 0.017453292f - 3.1415927f);
        final float f5 = MathHelper.sin(-f3 * 0.017453292f - 3.1415927f);
        final float f6 = -MathHelper.cos(-f2 * 0.017453292f);
        final float f7 = MathHelper.sin(-f2 * 0.017453292f);
        final float f8 = f5 * f6;
        final float f9 = f4 * f6;
        final double d4 = 5.0;
        final Vec3d vec3d2 = vec3d.addVector(f8 * 5.0, f7 * 5.0, f9 * 5.0);
        final RayTraceResult raytraceresult = itemStackIn.rayTraceBlocks(vec3d, vec3d2, true);
        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        final Vec3d vec3d3 = worldIn.getLook(1.0f);
        boolean flag = false;
        final List<Entity> list = itemStackIn.getEntitiesWithinAABBExcludingEntity(worldIn, worldIn.getEntityBoundingBox().addCoord(vec3d3.xCoord * 5.0, vec3d3.yCoord * 5.0, vec3d3.zCoord * 5.0).expandXyz(1.0));
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = list.get(i);
            if (entity.canBeCollidedWith()) {
                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expandXyz(entity.getCollisionBorderSize());
                if (axisalignedbb.isVecInside(vec3d)) {
                    flag = true;
                }
            }
        }
        if (flag) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        final Block block = itemStackIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
        final boolean flag2 = block == Blocks.WATER || block == Blocks.FLOWING_WATER;
        final EntityBoat entityboat = new EntityBoat(itemStackIn, raytraceresult.hitVec.xCoord, flag2 ? (raytraceresult.hitVec.yCoord - 0.12) : raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
        entityboat.setBoatType(this.type);
        entityboat.rotationYaw = worldIn.rotationYaw;
        if (!itemStackIn.getCollisionBoxes(entityboat, entityboat.getEntityBoundingBox().expandXyz(-0.1)).isEmpty()) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        if (!itemStackIn.isRemote) {
            itemStackIn.spawnEntityInWorld(entityboat);
        }
        if (!worldIn.capabilities.isCreativeMode) {
            itemstack.func_190918_g(1);
        }
        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
