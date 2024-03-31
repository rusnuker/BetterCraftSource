// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.Validate;
import java.util.List;
import java.util.AbstractList;

public class NonNullList<E> extends AbstractList<E>
{
    private final List<E> field_191198_a;
    private final E field_191199_b;
    
    public static <E> NonNullList<E> func_191196_a() {
        return new NonNullList<E>();
    }
    
    public static <E> NonNullList<E> func_191197_a(final int p_191197_0_, final E p_191197_1_) {
        Validate.notNull(p_191197_1_);
        final Object[] aobject = new Object[p_191197_0_];
        Arrays.fill(aobject, p_191197_1_);
        return new NonNullList<E>(Arrays.asList((E[])aobject), p_191197_1_);
    }
    
    public static <E> NonNullList<E> func_193580_a(final E p_193580_0_, final E... p_193580_1_) {
        return new NonNullList<E>(Arrays.asList(p_193580_1_), p_193580_0_);
    }
    
    protected NonNullList() {
        this(new ArrayList<Object>(), null);
    }
    
    protected NonNullList(final List<E> p_i47327_1_, @Nullable final E p_i47327_2_) {
        this.field_191198_a = p_i47327_1_;
        this.field_191199_b = p_i47327_2_;
    }
    
    @Nonnull
    @Override
    public E get(final int p_get_1_) {
        return this.field_191198_a.get(p_get_1_);
    }
    
    @Override
    public E set(final int p_set_1_, final E p_set_2_) {
        Validate.notNull(p_set_2_);
        return this.field_191198_a.set(p_set_1_, p_set_2_);
    }
    
    @Override
    public void add(final int p_add_1_, final E p_add_2_) {
        Validate.notNull(p_add_2_);
        this.field_191198_a.add(p_add_1_, p_add_2_);
    }
    
    @Override
    public E remove(final int p_remove_1_) {
        return this.field_191198_a.remove(p_remove_1_);
    }
    
    @Override
    public int size() {
        return this.field_191198_a.size();
    }
    
    @Override
    public void clear() {
        if (this.field_191199_b == null) {
            super.clear();
        }
        else {
            for (int i = 0; i < this.size(); ++i) {
                this.set(i, this.field_191199_b);
            }
        }
    }
}
