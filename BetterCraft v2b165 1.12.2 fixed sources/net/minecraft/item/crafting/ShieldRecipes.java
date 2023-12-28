// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class ShieldRecipes
{
    public static class Decoration implements IRecipe
    {
        @Override
        public boolean matches(final InventoryCrafting inv, final World worldIn) {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack2 = ItemStack.field_190927_a;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack3 = inv.getStackInSlot(i);
                if (!itemstack3.func_190926_b()) {
                    if (itemstack3.getItem() == Items.BANNER) {
                        if (!itemstack2.func_190926_b()) {
                            return false;
                        }
                        itemstack2 = itemstack3;
                    }
                    else {
                        if (itemstack3.getItem() != Items.SHIELD) {
                            return false;
                        }
                        if (!itemstack.func_190926_b()) {
                            return false;
                        }
                        if (itemstack3.getSubCompound("BlockEntityTag") != null) {
                            return false;
                        }
                        itemstack = itemstack3;
                    }
                }
            }
            return !itemstack.func_190926_b() && !itemstack2.func_190926_b();
        }
        
        @Override
        public ItemStack getCraftingResult(final InventoryCrafting inv) {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack2 = ItemStack.field_190927_a;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack3 = inv.getStackInSlot(i);
                if (!itemstack3.func_190926_b()) {
                    if (itemstack3.getItem() == Items.BANNER) {
                        itemstack = itemstack3;
                    }
                    else if (itemstack3.getItem() == Items.SHIELD) {
                        itemstack2 = itemstack3.copy();
                    }
                }
            }
            if (itemstack2.func_190926_b()) {
                return itemstack2;
            }
            final NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");
            final NBTTagCompound nbttagcompound2 = (nbttagcompound == null) ? new NBTTagCompound() : nbttagcompound.copy();
            nbttagcompound2.setInteger("Base", itemstack.getMetadata() & 0xF);
            itemstack2.setTagInfo("BlockEntityTag", nbttagcompound2);
            return itemstack2;
        }
        
        @Override
        public ItemStack getRecipeOutput() {
            return ItemStack.field_190927_a;
        }
        
        @Override
        public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
            final NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack.getItem().hasContainerItem()) {
                    nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                }
            }
            return nonnulllist;
        }
        
        @Override
        public boolean func_192399_d() {
            return true;
        }
        
        @Override
        public boolean func_194133_a(final int p_194133_1_, final int p_194133_2_) {
            return p_194133_1_ * p_194133_2_ >= 2;
        }
    }
}
