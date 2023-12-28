// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.util;

import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.common.collect.Table;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.CraftingManager;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.recipebook.RecipeList;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import java.util.Map;
import net.minecraft.stats.RecipeBook;

public class RecipeBookClient extends RecipeBook
{
    public static final Map<CreativeTabs, List<RecipeList>> field_194086_e;
    public static final List<RecipeList> field_194087_f;
    
    static {
        field_194086_e = Maps.newHashMap();
        field_194087_f = Lists.newArrayList();
        final Table<CreativeTabs, String, RecipeList> table = (Table<CreativeTabs, String, RecipeList>)HashBasedTable.create();
        for (final IRecipe irecipe : CraftingManager.field_193380_a) {
            if (!irecipe.func_192399_d()) {
                final CreativeTabs creativetabs = func_194084_a(irecipe.getRecipeOutput());
                final String s = irecipe.func_193358_e();
                RecipeList recipelist1;
                if (s.isEmpty()) {
                    recipelist1 = func_194082_a(creativetabs);
                }
                else {
                    recipelist1 = table.get(creativetabs, s);
                    if (recipelist1 == null) {
                        recipelist1 = func_194082_a(creativetabs);
                        table.put(creativetabs, s, recipelist1);
                    }
                }
                recipelist1.func_192709_a(irecipe);
            }
        }
    }
    
    private static RecipeList func_194082_a(final CreativeTabs p_194082_0_) {
        final RecipeList recipelist = new RecipeList();
        RecipeBookClient.field_194087_f.add(recipelist);
        RecipeBookClient.field_194086_e.computeIfAbsent(p_194082_0_, p_194085_0_ -> new ArrayList()).add(recipelist);
        RecipeBookClient.field_194086_e.computeIfAbsent(CreativeTabs.SEARCH, p_194083_0_ -> new ArrayList()).add(recipelist);
        return recipelist;
    }
    
    private static CreativeTabs func_194084_a(final ItemStack p_194084_0_) {
        final CreativeTabs creativetabs = p_194084_0_.getItem().getCreativeTab();
        if (creativetabs != CreativeTabs.BUILDING_BLOCKS && creativetabs != CreativeTabs.TOOLS && creativetabs != CreativeTabs.REDSTONE) {
            return (creativetabs == CreativeTabs.COMBAT) ? CreativeTabs.TOOLS : CreativeTabs.MISC;
        }
        return creativetabs;
    }
}
