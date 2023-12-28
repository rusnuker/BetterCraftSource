// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.stats;

import net.minecraft.item.crafting.CraftingManager;
import javax.annotation.Nullable;
import net.minecraft.item.crafting.IRecipe;
import java.util.BitSet;

public class RecipeBook
{
    protected final BitSet field_194077_a;
    protected final BitSet field_194078_b;
    protected boolean field_192818_b;
    protected boolean field_192819_c;
    
    public RecipeBook() {
        this.field_194077_a = new BitSet();
        this.field_194078_b = new BitSet();
    }
    
    public void func_193824_a(final RecipeBook p_193824_1_) {
        this.field_194077_a.clear();
        this.field_194078_b.clear();
        this.field_194077_a.or(p_193824_1_.field_194077_a);
        this.field_194078_b.or(p_193824_1_.field_194078_b);
    }
    
    public void func_194073_a(final IRecipe p_194073_1_) {
        if (!p_194073_1_.func_192399_d()) {
            this.field_194077_a.set(func_194075_d(p_194073_1_));
        }
    }
    
    public boolean func_193830_f(@Nullable final IRecipe p_193830_1_) {
        return this.field_194077_a.get(func_194075_d(p_193830_1_));
    }
    
    public void func_193831_b(final IRecipe p_193831_1_) {
        final int i = func_194075_d(p_193831_1_);
        this.field_194077_a.clear(i);
        this.field_194078_b.clear(i);
    }
    
    protected static int func_194075_d(@Nullable final IRecipe p_194075_0_) {
        return CraftingManager.field_193380_a.getIDForObject(p_194075_0_);
    }
    
    public boolean func_194076_e(final IRecipe p_194076_1_) {
        return this.field_194078_b.get(func_194075_d(p_194076_1_));
    }
    
    public void func_194074_f(final IRecipe p_194074_1_) {
        this.field_194078_b.clear(func_194075_d(p_194074_1_));
    }
    
    public void func_193825_e(final IRecipe p_193825_1_) {
        this.field_194078_b.set(func_194075_d(p_193825_1_));
    }
    
    public boolean func_192812_b() {
        return this.field_192818_b;
    }
    
    public void func_192813_a(final boolean p_192813_1_) {
        this.field_192818_b = p_192813_1_;
    }
    
    public boolean func_192815_c() {
        return this.field_192819_c;
    }
    
    public void func_192810_b(final boolean p_192810_1_) {
        this.field_192819_c = p_192810_1_;
    }
}
