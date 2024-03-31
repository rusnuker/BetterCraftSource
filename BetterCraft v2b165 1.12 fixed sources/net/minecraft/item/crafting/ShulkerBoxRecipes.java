// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class ShulkerBoxRecipes
{
    public static class ShulkerBoxColoring implements IRecipe
    {
        @Override
        public boolean matches(final InventoryCrafting inv, final World worldIn) {
            int i = 0;
            int j = 0;
            for (int k = 0; k < inv.getSizeInventory(); ++k) {
                final ItemStack itemstack = inv.getStackInSlot(k);
                if (!itemstack.func_190926_b()) {
                    if (Block.getBlockFromItem(itemstack.getItem()) instanceof BlockShulkerBox) {
                        ++i;
                    }
                    else {
                        if (itemstack.getItem() != Items.DYE) {
                            return false;
                        }
                        ++j;
                    }
                    if (j > 1 || i > 1) {
                        return false;
                    }
                }
            }
            return i == 1 && j == 1;
        }
        
        @Override
        public ItemStack getCraftingResult(final InventoryCrafting inv) {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack2 = ItemStack.field_190927_a;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack3 = inv.getStackInSlot(i);
                if (!itemstack3.func_190926_b()) {
                    if (Block.getBlockFromItem(itemstack3.getItem()) instanceof BlockShulkerBox) {
                        itemstack = itemstack3;
                    }
                    else if (itemstack3.getItem() == Items.DYE) {
                        itemstack2 = itemstack3;
                    }
                }
            }
            final ItemStack itemstack4 = BlockShulkerBox.func_190953_b(EnumDyeColor.byDyeDamage(itemstack2.getMetadata()));
            if (itemstack.hasTagCompound()) {
                itemstack4.setTagCompound(itemstack.getTagCompound().copy());
            }
            return itemstack4;
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
