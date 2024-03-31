// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.booleans.AbstractBooleanIterator;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractByte2BooleanSortedMap extends AbstractByte2BooleanMap implements Byte2BooleanSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2BooleanSortedMap() {
    }
    
    @Deprecated
    @Override
    public Byte2BooleanSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    @Override
    public Byte2BooleanSortedMap tailMap(final Byte from) {
        return this.tailMap((byte)from);
    }
    
    @Deprecated
    @Override
    public Byte2BooleanSortedMap subMap(final Byte from, final Byte to) {
        return this.subMap((byte)from, (byte)to);
    }
    
    @Deprecated
    @Override
    public Byte firstKey() {
        return this.firstByteKey();
    }
    
    @Deprecated
    @Override
    public Byte lastKey() {
        return this.lastByteKey();
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Byte, Boolean>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Boolean>>)this.byte2BooleanEntrySet();
    }
    
    protected class KeySet extends AbstractByteSortedSet
    {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2BooleanSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2BooleanSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2BooleanSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2BooleanSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Byte, Boolean>>)AbstractByte2BooleanSortedMap.this.entrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractByte2BooleanSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractByteBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Boolean>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Boolean>> i) {
            this.i = i;
        }
        
        @Override
        public byte nextByte() {
            return (byte)this.i.next().getKey();
        }
        
        @Override
        public byte previousByte() {
            return (byte)this.i.previous().getKey();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }
    
    protected class ValuesCollection extends AbstractBooleanCollection
    {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(AbstractByte2BooleanSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractByte2BooleanSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractBooleanIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Boolean>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Boolean>> i) {
            this.i = i;
        }
        
        @Override
        public boolean nextBoolean() {
            return (boolean)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
