// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import java.util.Iterator;
import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemEnchantedBook extends Item
{
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }
    
    @Override
    public boolean isItemTool(final ItemStack stack) {
        return false;
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return getEnchantments(stack).hasNoTags() ? super.getRarity(stack) : EnumRarity.UNCOMMON;
    }
    
    public static NBTTagList getEnchantments(final ItemStack p_92110_0_) {
        final NBTTagCompound nbttagcompound = p_92110_0_.getTagCompound();
        return (nbttagcompound != null) ? nbttagcompound.getTagList("StoredEnchantments", 10) : new NBTTagList();
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World playerIn, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        final NBTTagList nbttaglist = getEnchantments(stack);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final int j = nbttagcompound.getShort("id");
            final Enchantment enchantment = Enchantment.getEnchantmentByID(j);
            if (enchantment != null) {
                tooltip.add(enchantment.getTranslatedName(nbttagcompound.getShort("lvl")));
            }
        }
    }
    
    public static void addEnchantment(final ItemStack p_92115_0_, final EnchantmentData stack) {
        final NBTTagList nbttaglist = getEnchantments(p_92115_0_);
        boolean flag = true;
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            if (Enchantment.getEnchantmentByID(nbttagcompound.getShort("id")) == stack.enchantmentobj) {
                if (nbttagcompound.getShort("lvl") < stack.enchantmentLevel) {
                    nbttagcompound.setShort("lvl", (short)stack.enchantmentLevel);
                }
                flag = false;
                break;
            }
        }
        if (flag) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setShort("id", (short)Enchantment.getEnchantmentID(stack.enchantmentobj));
            nbttagcompound2.setShort("lvl", (short)stack.enchantmentLevel);
            nbttaglist.appendTag(nbttagcompound2);
        }
        if (!p_92115_0_.hasTagCompound()) {
            p_92115_0_.setTagCompound(new NBTTagCompound());
        }
        p_92115_0_.getTagCompound().setTag("StoredEnchantments", nbttaglist);
    }
    
    public static ItemStack getEnchantedItemStack(final EnchantmentData p_92111_0_) {
        final ItemStack itemstack = new ItemStack(Items.ENCHANTED_BOOK);
        addEnchantment(itemstack, p_92111_0_);
        return itemstack;
    }
    
    @Override
    public void getSubItems(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        if (itemIn == CreativeTabs.SEARCH) {
            for (final Enchantment enchantment : Enchantment.REGISTRY) {
                if (enchantment.type != null) {
                    for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                        tab.add(getEnchantedItemStack(new EnchantmentData(enchantment, i)));
                    }
                }
            }
        }
        else if (itemIn.getRelevantEnchantmentTypes().length != 0) {
            for (final Enchantment enchantment2 : Enchantment.REGISTRY) {
                if (itemIn.hasRelevantEnchantmentType(enchantment2.type)) {
                    tab.add(getEnchantedItemStack(new EnchantmentData(enchantment2, enchantment2.getMaxLevel())));
                }
            }
        }
    }
}
