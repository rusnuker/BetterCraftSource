// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

public class ItemFirework extends Item
{
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        if (!playerIn.isRemote) {
            final ItemStack itemstack = stack.getHeldItem(pos);
            final EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(playerIn, worldIn.getX() + facing, worldIn.getY() + hitX, worldIn.getZ() + hitY, itemstack);
            playerIn.spawnEntityInWorld(entityfireworkrocket);
            if (!stack.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World itemStackIn, final EntityPlayer worldIn, final EnumHand playerIn) {
        if (worldIn.isElytraFlying()) {
            final ItemStack itemstack = worldIn.getHeldItem(playerIn);
            if (!itemStackIn.isRemote) {
                final EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(itemStackIn, itemstack, worldIn);
                itemStackIn.spawnEntityInWorld(entityfireworkrocket);
                if (!worldIn.capabilities.isCreativeMode) {
                    itemstack.func_190918_g(1);
                }
            }
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, worldIn.getHeldItem(playerIn));
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, worldIn.getHeldItem(playerIn));
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World playerIn, final List<String> tooltip, final ITooltipFlag advanced) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("Fireworks");
        if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("Flight", 99)) {
                tooltip.add(String.valueOf(I18n.translateToLocal("item.fireworks.flight")) + " " + nbttagcompound.getByte("Flight"));
            }
            final NBTTagList nbttaglist = nbttagcompound.getTagList("Explosions", 10);
            if (!nbttaglist.hasNoTags()) {
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                    final List<String> list = (List<String>)Lists.newArrayList();
                    ItemFireworkCharge.addExplosionInfo(nbttagcompound2, list);
                    if (!list.isEmpty()) {
                        for (int j = 1; j < list.size(); ++j) {
                            list.set(j, "  " + list.get(j));
                        }
                        tooltip.addAll(list);
                    }
                }
            }
        }
    }
}
