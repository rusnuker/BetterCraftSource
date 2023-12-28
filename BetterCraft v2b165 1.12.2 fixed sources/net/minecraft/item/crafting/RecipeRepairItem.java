// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.item.Item;
import java.util.List;
import net.minecraft.item.ItemStack;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipeRepairItem implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.func_190926_b()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    final ItemStack itemstack2 = list.get(0);
                    if (itemstack.getItem() != itemstack2.getItem() || itemstack2.func_190916_E() != 1 || itemstack.func_190916_E() != 1 || !itemstack2.getItem().isDamageable()) {
                        return false;
                    }
                }
            }
        }
        return list.size() == 2;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.func_190926_b()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    final ItemStack itemstack2 = list.get(0);
                    if (itemstack.getItem() != itemstack2.getItem() || itemstack2.func_190916_E() != 1 || itemstack.func_190916_E() != 1 || !itemstack2.getItem().isDamageable()) {
                        return ItemStack.field_190927_a;
                    }
                }
            }
        }
        if (list.size() == 2) {
            final ItemStack itemstack3 = list.get(0);
            final ItemStack itemstack4 = list.get(1);
            if (itemstack3.getItem() == itemstack4.getItem() && itemstack3.func_190916_E() == 1 && itemstack4.func_190916_E() == 1 && itemstack3.getItem().isDamageable()) {
                final Item item = itemstack3.getItem();
                final int j = item.getMaxDamage() - itemstack3.getItemDamage();
                final int k = item.getMaxDamage() - itemstack4.getItemDamage();
                final int l = j + k + item.getMaxDamage() * 5 / 100;
                int i2 = item.getMaxDamage() - l;
                if (i2 < 0) {
                    i2 = 0;
                }
                return new ItemStack(itemstack3.getItem(), 1, i2);
            }
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
