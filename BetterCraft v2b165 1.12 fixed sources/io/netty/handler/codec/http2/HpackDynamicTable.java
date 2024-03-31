// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

final class HpackDynamicTable
{
    HpackHeaderField[] hpackHeaderFields;
    int head;
    int tail;
    private long size;
    private long capacity;
    
    HpackDynamicTable(final long initialCapacity) {
        this.capacity = -1L;
        this.setCapacity(initialCapacity);
    }
    
    public int length() {
        int length;
        if (this.head < this.tail) {
            length = this.hpackHeaderFields.length - this.tail + this.head;
        }
        else {
            length = this.head - this.tail;
        }
        return length;
    }
    
    public long size() {
        return this.size;
    }
    
    public long capacity() {
        return this.capacity;
    }
    
    public HpackHeaderField getEntry(final int index) {
        if (index <= 0 || index > this.length()) {
            throw new IndexOutOfBoundsException();
        }
        final int i = this.head - index;
        if (i < 0) {
            return this.hpackHeaderFields[i + this.hpackHeaderFields.length];
        }
        return this.hpackHeaderFields[i];
    }
    
    public void add(final HpackHeaderField header) {
        final int headerSize = header.size();
        if (headerSize > this.capacity) {
            this.clear();
            return;
        }
        while (this.capacity - this.size < headerSize) {
            this.remove();
        }
        this.hpackHeaderFields[this.head++] = header;
        this.size += header.size();
        if (this.head == this.hpackHeaderFields.length) {
            this.head = 0;
        }
    }
    
    public HpackHeaderField remove() {
        final HpackHeaderField removed = this.hpackHeaderFields[this.tail];
        if (removed == null) {
            return null;
        }
        this.size -= removed.size();
        this.hpackHeaderFields[this.tail++] = null;
        if (this.tail == this.hpackHeaderFields.length) {
            this.tail = 0;
        }
        return removed;
    }
    
    public void clear() {
        while (this.tail != this.head) {
            this.hpackHeaderFields[this.tail++] = null;
            if (this.tail == this.hpackHeaderFields.length) {
                this.tail = 0;
            }
        }
        this.head = 0;
        this.tail = 0;
        this.size = 0L;
    }
    
    public void setCapacity(final long capacity) {
        if (capacity < 0L || capacity > 4294967295L) {
            throw new IllegalArgumentException("capacity is invalid: " + capacity);
        }
        if (this.capacity == capacity) {
            return;
        }
        this.capacity = capacity;
        if (capacity == 0L) {
            this.clear();
        }
        else {
            while (this.size > capacity) {
                this.remove();
            }
        }
        int maxEntries = (int)(capacity / 32L);
        if (capacity % 32L != 0L) {
            ++maxEntries;
        }
        if (this.hpackHeaderFields != null && this.hpackHeaderFields.length == maxEntries) {
            return;
        }
        final HpackHeaderField[] tmp = new HpackHeaderField[maxEntries];
        final int len = this.length();
        int cursor = this.tail;
        for (int i = 0; i < len; ++i) {
            final HpackHeaderField entry = this.hpackHeaderFields[cursor++];
            tmp[i] = entry;
            if (cursor == this.hpackHeaderFields.length) {
                cursor = 0;
            }
        }
        this.tail = 0;
        this.head = this.tail + len;
        this.hpackHeaderFields = tmp;
    }
}
