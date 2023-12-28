// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipeBookCloning implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        int i = 0;
        ItemStack itemstack = ItemStack.field_190927_a;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            final ItemStack itemstack2 = inv.getStackInSlot(j);
            if (!itemstack2.func_190926_b()) {
                if (itemstack2.getItem() == Items.WRITTEN_BOOK) {
                    if (!itemstack.func_190926_b()) {
                        return false;
                    }
                    itemstack = itemstack2;
                }
                else {
                    if (itemstack2.getItem() != Items.WRITABLE_BOOK) {
                        return false;
                    }
                    ++i;
                }
            }
        }
        return !itemstack.func_190926_b() && itemstack.hasTagCompound() && i > 0;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        int i = 0;
        ItemStack itemstack = ItemStack.field_190927_a;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            final ItemStack itemstack2 = inv.getStackInSlot(j);
            if (!itemstack2.func_190926_b()) {
                if (itemstack2.getItem() == Items.WRITTEN_BOOK) {
                    if (!itemstack.func_190926_b()) {
                        return ItemStack.field_190927_a;
                    }
                    itemstack = itemstack2;
                }
                else {
                    if (itemstack2.getItem() != Items.WRITABLE_BOOK) {
                        return ItemStack.field_190927_a;
                    }
                    ++i;
                }
            }
        }
        if (!itemstack.func_190926_b() && itemstack.hasTagCompound() && i >= 1 && ItemWrittenBook.getGeneration(itemstack) < 2) {
            final ItemStack itemstack3 = new ItemStack(Items.WRITTEN_BOOK, i);
            itemstack3.setTagCompound(itemstack.getTagCompound().copy());
            itemstack3.getTagCompound().setInteger("generation", ItemWrittenBook.getGeneration(itemstack) + 1);
            if (itemstack.hasDisplayName()) {
                itemstack3.setStackDisplayName(itemstack.getDisplayName());
            }
            return itemstack3;
        }
        return ItemStack.field_190927_a;
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
            if (itemstack.getItem() instanceof ItemWrittenBook) {
                final ItemStack itemstack2 = itemstack.copy();
                itemstack2.func_190920_e(1);
                nonnulllist.set(i, itemstack2);
                break;
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
        return p_194133_1_ >= 3 && p_194133_2_ >= 3;
    }
}
