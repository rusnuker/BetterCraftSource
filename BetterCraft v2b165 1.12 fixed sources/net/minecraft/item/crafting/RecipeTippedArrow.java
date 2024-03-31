// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.potion.PotionEffect;
import java.util.Collection;
import net.minecraft.potion.PotionUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipeTippedArrow implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        if (inv.getWidth() == 3 && inv.getHeight() == 3) {
            for (int i = 0; i < inv.getWidth(); ++i) {
                for (int j = 0; j < inv.getHeight(); ++j) {
                    final ItemStack itemstack = inv.getStackInRowAndColumn(i, j);
                    if (itemstack.func_190926_b()) {
                        return false;
                    }
                    final Item item = itemstack.getItem();
                    if (i == 1 && j == 1) {
                        if (item != Items.LINGERING_POTION) {
                            return false;
                        }
                    }
                    else if (item != Items.ARROW) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        final ItemStack itemstack = inv.getStackInRowAndColumn(1, 1);
        if (itemstack.getItem() != Items.LINGERING_POTION) {
            return ItemStack.field_190927_a;
        }
        final ItemStack itemstack2 = new ItemStack(Items.TIPPED_ARROW, 8);
        PotionUtils.addPotionToItemStack(itemstack2, PotionUtils.getPotionFromItem(itemstack));
        PotionUtils.appendEffects(itemstack2, PotionUtils.getFullEffectsFromItem(itemstack));
        return itemstack2;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.field_190927_a;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
        return NonNullList.func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);
    }
    
    @Override
    public boolean func_192399_d() {
        return true;
    }
    
    @Override
    public boolean func_194133_a(final int p_194133_1_, final int p_194133_2_) {
        return p_194133_1_ >= 2 && p_194133_2_ >= 2;
    }
}
