// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Table<R, C, V>
{
    boolean contains(@Nullable @CompatibleWith("R") final Object p0, @Nullable @CompatibleWith("C") final Object p1);
    
    boolean containsRow(@Nullable @CompatibleWith("R") final Object p0);
    
    boolean containsColumn(@Nullable @CompatibleWith("C") final Object p0);
    
    boolean containsValue(@Nullable @CompatibleWith("V") final Object p0);
    
    V get(@Nullable @CompatibleWith("R") final Object p0, @Nullable @CompatibleWith("C") final Object p1);
    
    boolean isEmpty();
    
    int size();
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
    
    void clear();
    
    @Nullable
    @CanIgnoreReturnValue
    V put(final R p0, final C p1, final V p2);
    
    void putAll(final Table<? extends R, ? extends C, ? extends V> p0);
    
    @Nullable
    @CanIgnoreReturnValue
    V remove(@Nullable @CompatibleWith("R") final Object p0, @Nullable @CompatibleWith("C") final Object p1);
    
    Map<C, V> row(final R p0);
    
    Map<R, V> column(final C p0);
    
    Set<Cell<R, C, V>> cellSet();
    
    Set<R> rowKeySet();
    
    Set<C> columnKeySet();
    
    Collection<V> values();
    
    Map<R, Map<C, V>> rowMap();
    
    Map<C, Map<R, V>> columnMap();
    
    public interface Cell<R, C, V>
    {
        @Nullable
        R getRowKey();
        
        @Nullable
        C getColumnKey();
        
        @Nullable
        V getValue();
        
        boolean equals(@Nullable final Object p0);
        
        int hashCode();
    }
}
