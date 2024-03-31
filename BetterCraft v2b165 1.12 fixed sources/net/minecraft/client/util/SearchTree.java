// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.util;

import com.google.common.collect.AbstractIterator;
import java.util.Locale;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import java.util.function.Function;

public class SearchTree<T> implements ISearchTree<T>
{
    protected SuffixArray<T> field_194044_a;
    protected SuffixArray<T> field_194045_b;
    private final Function<T, Iterable<String>> field_194046_c;
    private final Function<T, Iterable<ResourceLocation>> field_194047_d;
    private final List<T> field_194048_e;
    private Object2IntMap<T> field_194049_f;
    
    public SearchTree(final Function<T, Iterable<String>> p_i47612_1_, final Function<T, Iterable<ResourceLocation>> p_i47612_2_) {
        this.field_194044_a = new SuffixArray<T>();
        this.field_194045_b = new SuffixArray<T>();
        this.field_194048_e = (List<T>)Lists.newArrayList();
        this.field_194049_f = new Object2IntOpenHashMap<T>();
        this.field_194046_c = p_i47612_1_;
        this.field_194047_d = p_i47612_2_;
    }
    
    public void func_194040_a() {
        this.field_194044_a = new SuffixArray<T>();
        this.field_194045_b = new SuffixArray<T>();
        for (final T t : this.field_194048_e) {
            this.func_194042_b(t);
        }
        this.field_194044_a.func_194058_a();
        this.field_194045_b.func_194058_a();
    }
    
    public void func_194043_a(final T p_194043_1_) {
        this.field_194049_f.put(p_194043_1_, this.field_194048_e.size());
        this.field_194048_e.add(p_194043_1_);
        this.func_194042_b(p_194043_1_);
    }
    
    private void func_194042_b(final T p_194042_1_) {
        this.field_194047_d.apply(p_194042_1_).forEach(p_194039_2_ -> this.field_194045_b.func_194057_a((T)p_194057_1_, p_194039_2_.toString().toLowerCase(Locale.ROOT)));
        this.field_194046_c.apply(p_194042_1_).forEach(p_194041_2_ -> this.field_194044_a.func_194057_a((T)p_194057_1_2, p_194041_2_.toLowerCase(Locale.ROOT)));
    }
    
    @Override
    public List<T> func_194038_a(final String p_194038_1_) {
        final List<T> list = this.field_194044_a.func_194055_a(p_194038_1_);
        if (p_194038_1_.indexOf(58) < 0) {
            return list;
        }
        final List<T> list2 = this.field_194045_b.func_194055_a(p_194038_1_);
        return (List<T>)(list2.isEmpty() ? list : Lists.newArrayList((Iterator<?>)new MergingIterator<Object>(list.iterator(), list2.iterator(), this.field_194049_f)));
    }
    
    static class MergingIterator<T> extends AbstractIterator<T>
    {
        private final Iterator<T> field_194033_a;
        private final Iterator<T> field_194034_b;
        private final Object2IntMap<T> field_194035_c;
        private T field_194036_d;
        private T field_194037_e;
        
        public MergingIterator(final Iterator<T> p_i47606_1_, final Iterator<T> p_i47606_2_, final Object2IntMap<T> p_i47606_3_) {
            this.field_194033_a = p_i47606_1_;
            this.field_194034_b = p_i47606_2_;
            this.field_194035_c = p_i47606_3_;
            this.field_194036_d = (p_i47606_1_.hasNext() ? p_i47606_1_.next() : null);
            this.field_194037_e = (p_i47606_2_.hasNext() ? p_i47606_2_.next() : null);
        }
        
        @Override
        protected T computeNext() {
            if (this.field_194036_d == null && this.field_194037_e == null) {
                return this.endOfData();
            }
            int i;
            if (this.field_194036_d == this.field_194037_e) {
                i = 0;
            }
            else if (this.field_194036_d == null) {
                i = 1;
            }
            else if (this.field_194037_e == null) {
                i = -1;
            }
            else {
                i = Integer.compare(this.field_194035_c.getInt(this.field_194036_d), this.field_194035_c.getInt(this.field_194037_e));
            }
            final T t = (i <= 0) ? this.field_194036_d : this.field_194037_e;
            if (i <= 0) {
                this.field_194036_d = (this.field_194033_a.hasNext() ? this.field_194033_a.next() : null);
            }
            if (i >= 0) {
                this.field_194037_e = (this.field_194034_b.hasNext() ? this.field_194034_b.next() : null);
            }
            return t;
        }
    }
}
