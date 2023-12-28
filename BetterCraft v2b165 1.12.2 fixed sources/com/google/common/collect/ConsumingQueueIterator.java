// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayDeque;
import java.util.Queue;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class ConsumingQueueIterator<T> extends AbstractIterator<T>
{
    private final Queue<T> queue;
    
    ConsumingQueueIterator(final T... elements) {
        Collections.addAll(this.queue = new ArrayDeque<T>(elements.length), elements);
    }
    
    ConsumingQueueIterator(final Queue<T> queue) {
        this.queue = Preconditions.checkNotNull(queue);
    }
    
    public T computeNext() {
        return this.queue.isEmpty() ? this.endOfData() : this.queue.remove();
    }
}
