// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public interface IRecipe
{
    boolean matches(final InventoryCrafting p0, final World p1);
    
    ItemStack getCraftingResult(final InventoryCrafting p0);
    
    boolean func_194133_a(final int p0, final int p1);
    
    ItemStack getRecipeOutput();
    
    NonNullList<ItemStack> getRemainingItems(final InventoryCrafting p0);
    
    default NonNullList<Ingredient> func_192400_c() {
        return NonNullList.func_191196_a();
    }
    
    default boolean func_192399_d() {
        return false;
    }
    
    default String func_193358_e() {
        return "";
    }
}
