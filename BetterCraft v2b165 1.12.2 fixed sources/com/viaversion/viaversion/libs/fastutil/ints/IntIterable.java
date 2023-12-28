// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.IntConsumer;

public interface IntIterable extends Iterable<Integer>
{
    IntIterator iterator();
    
    default void forEach(final IntConsumer action) {
        Objects.requireNonNull(action);
        final IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.nextInt());
        }
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEach(action::accept);
    }
}
