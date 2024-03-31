// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;

public class ShapelessRecipes implements IRecipe
{
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final String field_194138_c;
    
    public ShapelessRecipes(final String p_i47500_1_, final ItemStack p_i47500_2_, final NonNullList<Ingredient> p_i47500_3_) {
        this.field_194138_c = p_i47500_1_;
        this.recipeOutput = p_i47500_2_;
        this.recipeItems = p_i47500_3_;
    }
    
    @Override
    public String func_193358_e() {
        return this.field_194138_c;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }
    
    @Override
    public NonNullList<Ingredient> func_192400_c() {
        return this.recipeItems;
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
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        final List<Ingredient> list = (List<Ingredient>)Lists.newArrayList((Iterable<?>)this.recipeItems);
        for (int i = 0; i < inv.getHeight(); ++i) {
            for (int j = 0; j < inv.getWidth(); ++j) {
                final ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (!itemstack.func_190926_b()) {
                    boolean flag = false;
                    for (final Ingredient ingredient : list) {
                        if (ingredient.apply(itemstack)) {
                            flag = true;
                            list.remove(ingredient);
                            break;
                        }
                    }
                    if (!flag) {
                        return false;
                    }
                }
            }
        }
        return list.isEmpty();
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.recipeOutput.copy();
    }
    
    public static ShapelessRecipes func_193363_a(final JsonObject p_193363_0_) {
        final String s = JsonUtils.getString(p_193363_0_, "group", "");
        final NonNullList<Ingredient> nonnulllist = func_193364_a(JsonUtils.getJsonArray(p_193363_0_, "ingredients"));
        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        }
        if (nonnulllist.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        }
        final ItemStack itemstack = ShapedRecipes.func_192405_a(JsonUtils.getJsonObject(p_193363_0_, "result"), true);
        return new ShapelessRecipes(s, itemstack, nonnulllist);
    }
    
    private static NonNullList<Ingredient> func_193364_a(final JsonArray p_193364_0_) {
        final NonNullList<Ingredient> nonnulllist = NonNullList.func_191196_a();
        for (int i = 0; i < p_193364_0_.size(); ++i) {
            final Ingredient ingredient = ShapedRecipes.func_193361_a(p_193364_0_.get(i));
            if (ingredient != Ingredient.field_193370_a) {
                nonnulllist.add(ingredient);
            }
        }
        return nonnulllist;
    }
    
    @Override
    public boolean func_194133_a(final int p_194133_1_, final int p_194133_2_) {
        return p_194133_1_ * p_194133_2_ >= this.recipeItems.size();
    }
}
