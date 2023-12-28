// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

public abstract class AbstractObjectBigListIterator<K> extends AbstractObjectBidirectionalIterator<K> implements ObjectBigListIterator<K>
{
    protected AbstractObjectBigListIterator() {
    }
    
    @Override
    public void set(final K k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final K k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.next();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previous();
        }
        return n - i - 1L;
    }
}
