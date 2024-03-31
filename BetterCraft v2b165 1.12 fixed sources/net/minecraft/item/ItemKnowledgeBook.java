// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.item.crafting.IRecipe;
import java.util.List;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemKnowledgeBook extends Item
{
    private static final Logger field_194126_a;
    
    static {
        field_194126_a = LogManager.getLogger();
    }
    
    public ItemKnowledgeBook() {
        this.setMaxStackSize(1);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World itemStackIn, final EntityPlayer worldIn, final EnumHand playerIn) {
        final ItemStack itemstack = worldIn.getHeldItem(playerIn);
        final NBTTagCompound nbttagcompound = itemstack.getTagCompound();
        if (!worldIn.capabilities.isCreativeMode) {
            worldIn.setHeldItem(playerIn, ItemStack.field_190927_a);
        }
        if (nbttagcompound != null && nbttagcompound.hasKey("Recipes", 9)) {
            if (!itemStackIn.isRemote) {
                final NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 8);
                final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final String s = nbttaglist.getStringTagAt(i);
                    final IRecipe irecipe = CraftingManager.func_193373_a(new ResourceLocation(s));
                    if (irecipe == null) {
                        ItemKnowledgeBook.field_194126_a.error("Invalid recipe: " + s);
                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                    }
                    list.add(irecipe);
                }
                worldIn.func_192021_a(list);
                worldIn.addStat(StatList.getObjectUseStats(this));
            }
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        ItemKnowledgeBook.field_194126_a.error("Tag not valid: " + nbttagcompound);
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
}
