// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class DefaultAttributeMap implements AttributeMap
{
    private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> updater;
    private static final int BUCKET_SIZE = 4;
    private static final int MASK = 3;
    private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;
    
    @Override
    public <T> Attribute<T> attr(final AttributeKey<T> key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            attributes = new AtomicReferenceArray<DefaultAttribute<?>>(4);
            if (!DefaultAttributeMap.updater.compareAndSet(this, null, attributes)) {
                attributes = this.attributes;
            }
        }
        final int i = index(key);
        DefaultAttribute<?> head = attributes.get(i);
        if (head == null) {
            head = new DefaultAttribute<Object>();
            final DefaultAttribute<T> attr = new DefaultAttribute<T>(head, key);
            ((DefaultAttribute<Object>)head).next = attr;
            ((DefaultAttribute<Object>)attr).prev = head;
            if (attributes.compareAndSet(i, null, head)) {
                return attr;
            }
            head = attributes.get(i);
        }
        synchronized (head) {
            DefaultAttribute<?> curr = head;
            while (true) {
                final DefaultAttribute<?> next = ((DefaultAttribute<Object>)head).next;
                if (next == null) {
                    final DefaultAttribute<T> attr2 = new DefaultAttribute<T>(head, key);
                    ((DefaultAttribute<Object>)head).next = attr2;
                    ((DefaultAttribute<Object>)attr2).prev = head;
                    return attr2;
                }
                if (((DefaultAttribute<Object>)next).key == key && !((DefaultAttribute<Object>)next).removed) {
                    return (Attribute<T>)next;
                }
                curr = next;
            }
        }
    }
    
    @Override
    public <T> boolean hasAttr(final AttributeKey<T> key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        final AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            return false;
        }
        final int i = index(key);
        final DefaultAttribute<?> head = attributes.get(i);
        if (head == null) {
            return false;
        }
        synchronized (head) {
            for (DefaultAttribute<?> curr = ((DefaultAttribute<Object>)head).next; curr != null; curr = ((DefaultAttribute<Object>)curr).next) {
                if (((DefaultAttribute<Object>)curr).key == key && !((DefaultAttribute<Object>)curr).removed) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private static int index(final AttributeKey<?> key) {
        return key.id() & 0x3;
    }
    
    static {
        updater = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, AtomicReferenceArray.class, "attributes");
    }
    
    private static final class DefaultAttribute<T> extends AtomicReference<T> implements Attribute<T>
    {
        private static final long serialVersionUID = -2661411462200283011L;
        private final DefaultAttribute<?> head;
        private final AttributeKey<T> key;
        private DefaultAttribute<?> prev;
        private DefaultAttribute<?> next;
        private volatile boolean removed;
        
        DefaultAttribute(final DefaultAttribute<?> head, final AttributeKey<T> key) {
            this.head = head;
            this.key = key;
        }
        
        DefaultAttribute() {
            this.head = this;
            this.key = null;
        }
        
        @Override
        public AttributeKey<T> key() {
            return this.key;
        }
        
        @Override
        public T setIfAbsent(final T value) {
            while (!this.compareAndSet(null, value)) {
                final T old = this.get();
                if (old != null) {
                    return old;
                }
            }
            return null;
        }
        
        @Override
        public T getAndRemove() {
            this.removed = true;
            final T oldValue = this.getAndSet(null);
            this.remove0();
            return oldValue;
        }
        
        @Override
        public void remove() {
            this.removed = true;
            this.set(null);
            this.remove0();
        }
        
        private void remove0() {
            synchronized (this.head) {
                if (this.prev == null) {
                    return;
                }
                this.prev.next = this.next;
                if (this.next != null) {
                    this.next.prev = this.prev;
                }
                this.prev = null;
                this.next = null;
            }
        }
    }
}
