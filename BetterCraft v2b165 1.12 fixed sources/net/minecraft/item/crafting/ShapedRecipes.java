// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import com.google.gson.JsonSyntaxException;
import com.google.common.collect.Sets;
import java.util.Map;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ShapedRecipes implements IRecipe
{
    private final int recipeWidth;
    private final int recipeHeight;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack recipeOutput;
    private final String field_194137_e;
    
    public ShapedRecipes(final String p_i47501_1_, final int p_i47501_2_, final int p_i47501_3_, final NonNullList<Ingredient> p_i47501_4_, final ItemStack p_i47501_5_) {
        this.field_194137_e = p_i47501_1_;
        this.recipeWidth = p_i47501_2_;
        this.recipeHeight = p_i47501_3_;
        this.recipeItems = p_i47501_4_;
        this.recipeOutput = p_i47501_5_;
    }
    
    @Override
    public String func_193358_e() {
        return this.field_194137_e;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
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
    public NonNullList<Ingredient> func_192400_c() {
        return this.recipeItems;
    }
    
    @Override
    public boolean func_194133_a(final int p_194133_1_, final int p_194133_2_) {
        return p_194133_1_ >= this.recipeWidth && p_194133_2_ >= this.recipeHeight;
    }
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        for (int i = 0; i <= 3 - this.recipeWidth; ++i) {
            for (int j = 0; j <= 3 - this.recipeHeight; ++j) {
                if (this.checkMatch(inv, i, j, true)) {
                    return true;
                }
                if (this.checkMatch(inv, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkMatch(final InventoryCrafting p_77573_1_, final int p_77573_2_, final int p_77573_3_, final boolean p_77573_4_) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                final int k = i - p_77573_2_;
                final int l = j - p_77573_3_;
                Ingredient ingredient = Ingredient.field_193370_a;
                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
                    if (p_77573_4_) {
                        ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
                    }
                    else {
                        ingredient = this.recipeItems.get(k + l * this.recipeWidth);
                    }
                }
                if (!ingredient.apply(p_77573_1_.getStackInRowAndColumn(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.getRecipeOutput().copy();
    }
    
    public int func_192403_f() {
        return this.recipeWidth;
    }
    
    public int func_192404_g() {
        return this.recipeHeight;
    }
    
    public static ShapedRecipes func_193362_a(final JsonObject p_193362_0_) {
        final String s = JsonUtils.getString(p_193362_0_, "group", "");
        final Map<String, Ingredient> map = func_192408_a(JsonUtils.getJsonObject(p_193362_0_, "key"));
        final String[] astring = func_194134_a(func_192407_a(JsonUtils.getJsonArray(p_193362_0_, "pattern")));
        final int i = astring[0].length();
        final int j = astring.length;
        final NonNullList<Ingredient> nonnulllist = func_192402_a(astring, map, i, j);
        final ItemStack itemstack = func_192405_a(JsonUtils.getJsonObject(p_193362_0_, "result"), true);
        return new ShapedRecipes(s, i, j, nonnulllist, itemstack);
    }
    
    private static NonNullList<Ingredient> func_192402_a(final String[] p_192402_0_, final Map<String, Ingredient> p_192402_1_, final int p_192402_2_, final int p_192402_3_) {
        final NonNullList<Ingredient> nonnulllist = NonNullList.func_191197_a(p_192402_2_ * p_192402_3_, Ingredient.field_193370_a);
        final Set<String> set = (Set<String>)Sets.newHashSet((Iterable<?>)p_192402_1_.keySet());
        set.remove(" ");
        for (int i = 0; i < p_192402_0_.length; ++i) {
            for (int j = 0; j < p_192402_0_[i].length(); ++j) {
                final String s = p_192402_0_[i].substring(j, j + 1);
                final Ingredient ingredient = p_192402_1_.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }
                set.remove(s);
                nonnulllist.set(j + p_192402_2_ * i, ingredient);
            }
        }
        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        }
        return nonnulllist;
    }
    
    @VisibleForTesting
    static String[] func_194134_a(final String... p_194134_0_) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;
        for (int i2 = 0; i2 < p_194134_0_.length; ++i2) {
            final String s = p_194134_0_[i2];
            i = Math.min(i, func_194135_a(s));
            final int j2 = func_194136_b(s);
            j = Math.max(j, j2);
            if (j2 < 0) {
                if (k == i2) {
                    ++k;
                }
                ++l;
            }
            else {
                l = 0;
            }
        }
        if (p_194134_0_.length == l) {
            return new String[0];
        }
        final String[] astring = new String[p_194134_0_.length - l - k];
        for (int k2 = 0; k2 < astring.length; ++k2) {
            astring[k2] = p_194134_0_[k2 + k].substring(i, j + 1);
        }
        return astring;
    }
    
    private static int func_194135_a(final String p_194135_0_) {
        int i;
        for (i = 0; i < p_194135_0_.length() && p_194135_0_.charAt(i) == ' '; ++i) {}
        return i;
    }
    
    private static int func_194136_b(final String p_194136_0_) {
        int i;
        for (i = p_194136_0_.length() - 1; i >= 0 && p_194136_0_.charAt(i) == ' '; --i) {}
        return i;
    }
    
    private static String[] func_192407_a(final JsonArray p_192407_0_) {
        final String[] astring = new String[p_192407_0_.size()];
        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        }
        if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        }
        for (int i = 0; i < astring.length; ++i) {
            final String s = JsonUtils.getString(p_192407_0_.get(i), "pattern[" + i + "]");
            if (s.length() > 3) {
                throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
            }
            if (i > 0 && astring[0].length() != s.length()) {
                throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
            }
            astring[i] = s;
        }
        return astring;
    }
    
    private static Map<String, Ingredient> func_192408_a(final JsonObject p_192408_0_) {
        final Map<String, Ingredient> map = (Map<String, Ingredient>)Maps.newHashMap();
        for (final Map.Entry<String, JsonElement> entry : p_192408_0_.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }
            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }
            map.put(entry.getKey(), func_193361_a(entry.getValue()));
        }
        map.put(" ", Ingredient.field_193370_a);
        return map;
    }
    
    public static Ingredient func_193361_a(@Nullable final JsonElement p_193361_0_) {
        if (p_193361_0_ == null || p_193361_0_.isJsonNull()) {
            throw new JsonSyntaxException("Item cannot be null");
        }
        if (p_193361_0_.isJsonObject()) {
            return Ingredient.func_193369_a(func_192405_a(p_193361_0_.getAsJsonObject(), false));
        }
        if (!p_193361_0_.isJsonArray()) {
            throw new JsonSyntaxException("Expected item to be object or array of objects");
        }
        final JsonArray jsonarray = p_193361_0_.getAsJsonArray();
        if (jsonarray.size() == 0) {
            throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
        }
        final ItemStack[] aitemstack = new ItemStack[jsonarray.size()];
        for (int i = 0; i < jsonarray.size(); ++i) {
            aitemstack[i] = func_192405_a(JsonUtils.getJsonObject(jsonarray.get(i), "item"), false);
        }
        return Ingredient.func_193369_a(aitemstack);
    }
    
    public static ItemStack func_192405_a(final JsonObject p_192405_0_, final boolean p_192405_1_) {
        final String s = JsonUtils.getString(p_192405_0_, "item");
        final Item item = Item.REGISTRY.getObject(new ResourceLocation(s));
        if (item == null) {
            throw new JsonSyntaxException("Unknown item '" + s + "'");
        }
        if (item.getHasSubtypes() && !p_192405_0_.has("data")) {
            throw new JsonParseException("Missing data for item '" + s + "'");
        }
        final int i = JsonUtils.getInt(p_192405_0_, "data", 0);
        final int j = p_192405_1_ ? JsonUtils.getInt(p_192405_0_, "count", 1) : 1;
        return new ItemStack(item, j, i);
    }
}
