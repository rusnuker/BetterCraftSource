// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.chars.AbstractCharIterator;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractFloat2CharSortedMap extends AbstractFloat2CharMap implements Float2CharSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2CharSortedMap() {
    }
    
    @Deprecated
    @Override
    public Float2CharSortedMap headMap(final Float to) {
        return this.headMap((float)to);
    }
    
    @Deprecated
    @Override
    public Float2CharSortedMap tailMap(final Float from) {
        return this.tailMap((float)from);
    }
    
    @Deprecated
    @Override
    public Float2CharSortedMap subMap(final Float from, final Float to) {
        return this.subMap((float)from, (float)to);
    }
    
    @Deprecated
    @Override
    public Float firstKey() {
        return this.firstFloatKey();
    }
    
    @Deprecated
    @Override
    public Float lastKey() {
        return this.lastFloatKey();
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Float, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Float, Character>>)this.float2CharEntrySet();
    }
    
    protected class KeySet extends AbstractFloatSortedSet
    {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2CharSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractFloat2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractFloat2CharSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2CharSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2CharSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2CharSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Float, Character>>)AbstractFloat2CharSortedMap.this.entrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractFloat2CharSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractFloatBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Float, Character>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Float, Character>> i) {
            this.i = i;
        }
        
        @Override
        public float nextFloat() {
            return (float)this.i.next().getKey();
        }
        
        @Override
        public float previousFloat() {
            return (float)this.i.previous().getKey();
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
    
    protected class ValuesCollection extends AbstractCharCollection
    {
        @Override
        public CharIterator iterator() {
            return new ValuesIterator(AbstractFloat2CharSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractFloat2CharSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractFloat2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractFloat2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractCharIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Float, Character>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Float, Character>> i) {
            this.i = i;
        }
        
        @Override
        public char nextChar() {
            return (char)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
