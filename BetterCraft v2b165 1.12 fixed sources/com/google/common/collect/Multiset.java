// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.collect;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.Iterator;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.function.ObjIntConsumer;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;

@GwtCompatible
public interface Multiset<E> extends Collection<E>
{
    int size();
    
    int count(@Nullable @CompatibleWith("E") final Object p0);
    
    @CanIgnoreReturnValue
    int add(@Nullable final E p0, final int p1);
    
    @CanIgnoreReturnValue
    int remove(@Nullable @CompatibleWith("E") final Object p0, final int p1);
    
    @CanIgnoreReturnValue
    int setCount(final E p0, final int p1);
    
    @CanIgnoreReturnValue
    boolean setCount(final E p0, final int p1, final int p2);
    
    Set<E> elementSet();
    
    Set<Entry<E>> entrySet();
    
    @Beta
    default void forEachEntry(final ObjIntConsumer<? super E> action) {
        Preconditions.checkNotNull(action);
        this.entrySet().forEach(entry -> action.accept(entry.getElement(), entry.getCount()));
    }
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
    
    String toString();
    
    Iterator<E> iterator();
    
    boolean contains(@Nullable final Object p0);
    
    boolean containsAll(final Collection<?> p0);
    
    @CanIgnoreReturnValue
    boolean add(final E p0);
    
    @CanIgnoreReturnValue
    boolean remove(@Nullable final Object p0);
    
    @CanIgnoreReturnValue
    boolean removeAll(final Collection<?> p0);
    
    @CanIgnoreReturnValue
    boolean retainAll(final Collection<?> p0);
    
    default void forEach(final Consumer<? super E> action) {
        Preconditions.checkNotNull(action);
        this.entrySet().forEach(entry -> {
            final E elem = entry.getElement();
            for (int count = entry.getCount(), i = 0; i < count; ++i) {
                action.accept(elem);
            }
        });
    }
    
    default Spliterator<E> spliterator() {
        return Multisets.spliteratorImpl(this);
    }
    
    public interface Entry<E>
    {
        E getElement();
        
        int getCount();
        
        boolean equals(final Object p0);
        
        int hashCode();
        
        String toString();
    }
}
