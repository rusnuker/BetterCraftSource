// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.util;

import java.util.Iterator;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.item.ItemStack;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class SearchTreeManager implements IResourceManagerReloadListener
{
    public static final Key<ItemStack> field_194011_a;
    public static final Key<RecipeList> field_194012_b;
    private final Map<Key<?>, SearchTree<?>> field_194013_c;
    
    static {
        field_194011_a = new Key<ItemStack>();
        field_194012_b = new Key<RecipeList>();
    }
    
    public SearchTreeManager() {
        this.field_194013_c = (Map<Key<?>, SearchTree<?>>)Maps.newHashMap();
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        for (final SearchTree<?> searchtree : this.field_194013_c.values()) {
            searchtree.func_194040_a();
        }
    }
    
    public <T> void func_194009_a(final Key<T> p_194009_1_, final SearchTree<T> p_194009_2_) {
        this.field_194013_c.put(p_194009_1_, p_194009_2_);
    }
    
    public <T> ISearchTree<T> func_194010_a(final Key<T> p_194010_1_) {
        return (ISearchTree)this.field_194013_c.get(p_194010_1_);
    }
    
    public static class Key<T>
    {
    }
}
