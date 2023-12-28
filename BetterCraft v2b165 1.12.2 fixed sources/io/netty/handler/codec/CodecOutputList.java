// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.Recycler;
import java.util.RandomAccess;
import java.util.AbstractList;

final class CodecOutputList extends AbstractList<Object> implements RandomAccess
{
    private static final Recycler<CodecOutputList> RECYCLER;
    private final Recycler.Handle<CodecOutputList> handle;
    private int size;
    private Object[] array;
    private boolean insertSinceRecycled;
    
    static CodecOutputList newInstance() {
        return CodecOutputList.RECYCLER.get();
    }
    
    private CodecOutputList(final Recycler.Handle<CodecOutputList> handle) {
        this.array = new Object[16];
        this.handle = handle;
    }
    
    @Override
    public Object get(final int index) {
        this.checkIndex(index);
        return this.array[index];
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean add(final Object element) {
        ObjectUtil.checkNotNull(element, "element");
        try {
            this.insert(this.size, element);
        }
        catch (final IndexOutOfBoundsException ignore) {
            this.expandArray();
            this.insert(this.size, element);
        }
        ++this.size;
        return true;
    }
    
    @Override
    public Object set(final int index, final Object element) {
        ObjectUtil.checkNotNull(element, "element");
        this.checkIndex(index);
        final Object old = this.array[index];
        this.insert(index, element);
        return old;
    }
    
    @Override
    public void add(final int index, final Object element) {
        ObjectUtil.checkNotNull(element, "element");
        this.checkIndex(index);
        if (this.size == this.array.length) {
            this.expandArray();
        }
        if (index != this.size - 1) {
            System.arraycopy(this.array, index, this.array, index + 1, this.size - index);
        }
        this.insert(index, element);
        ++this.size;
    }
    
    @Override
    public Object remove(final int index) {
        this.checkIndex(index);
        final Object old = this.array[index];
        final int len = this.size - index - 1;
        if (len > 0) {
            System.arraycopy(this.array, index + 1, this.array, index, len);
        }
        this.array[--this.size] = null;
        return old;
    }
    
    @Override
    public void clear() {
        this.size = 0;
    }
    
    boolean insertSinceRecycled() {
        return this.insertSinceRecycled;
    }
    
    void recycle() {
        for (int i = 0; i < this.size; ++i) {
            this.array[i] = null;
        }
        this.clear();
        this.insertSinceRecycled = false;
        this.handle.recycle(this);
    }
    
    Object getUnsafe(final int index) {
        return this.array[index];
    }
    
    private void checkIndex(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    private void insert(final int index, final Object element) {
        this.array[index] = element;
        this.insertSinceRecycled = true;
    }
    
    private void expandArray() {
        final int newCapacity = this.array.length << 1;
        if (newCapacity < 0) {
            throw new OutOfMemoryError();
        }
        final Object[] newArray = new Object[newCapacity];
        System.arraycopy(this.array, 0, newArray, 0, this.array.length);
        this.array = newArray;
    }
    
    static {
        RECYCLER = new Recycler<CodecOutputList>() {
            @Override
            protected CodecOutputList newObject(final Handle<CodecOutputList> handle) {
                return new CodecOutputList(handle, null);
            }
        };
    }
}
