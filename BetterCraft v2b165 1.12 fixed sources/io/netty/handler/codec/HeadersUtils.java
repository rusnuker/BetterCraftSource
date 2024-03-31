// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.util.internal.ObjectUtil;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.AbstractList;
import java.util.List;

public final class HeadersUtils
{
    private HeadersUtils() {
    }
    
    public static <K, V> List<String> getAllAsString(final Headers<K, V, ?> headers, final K name) {
        final List<V> allNames = headers.getAll(name);
        return new AbstractList<String>() {
            @Override
            public String get(final int index) {
                final V value = allNames.get(index);
                return (value != null) ? value.toString() : null;
            }
            
            @Override
            public int size() {
                return allNames.size();
            }
        };
    }
    
    public static <K, V> String getAsString(final Headers<K, V, ?> headers, final K name) {
        final V orig = headers.get(name);
        return (orig != null) ? orig.toString() : null;
    }
    
    public static Iterator<Map.Entry<String, String>> iteratorAsString(final Iterable<Map.Entry<CharSequence, CharSequence>> headers) {
        return new StringEntryIterator(headers.iterator());
    }
    
    public static Set<String> namesAsString(final Headers<CharSequence, CharSequence, ?> headers) {
        return new CharSequenceDelegatingStringSet(headers.names());
    }
    
    private static final class StringEntryIterator implements Iterator<Map.Entry<String, String>>
    {
        private final Iterator<Map.Entry<CharSequence, CharSequence>> iter;
        
        public StringEntryIterator(final Iterator<Map.Entry<CharSequence, CharSequence>> iter) {
            this.iter = iter;
        }
        
        @Override
        public boolean hasNext() {
            return this.iter.hasNext();
        }
        
        @Override
        public Map.Entry<String, String> next() {
            return new StringEntry(this.iter.next());
        }
        
        @Override
        public void remove() {
            this.iter.remove();
        }
    }
    
    private static final class StringEntry implements Map.Entry<String, String>
    {
        private final Map.Entry<CharSequence, CharSequence> entry;
        private String name;
        private String value;
        
        StringEntry(final Map.Entry<CharSequence, CharSequence> entry) {
            this.entry = entry;
        }
        
        @Override
        public String getKey() {
            if (this.name == null) {
                this.name = this.entry.getKey().toString();
            }
            return this.name;
        }
        
        @Override
        public String getValue() {
            if (this.value == null && this.entry.getValue() != null) {
                this.value = this.entry.getValue().toString();
            }
            return this.value;
        }
        
        @Override
        public String setValue(final String value) {
            final String old = this.getValue();
            this.entry.setValue(value);
            return old;
        }
        
        @Override
        public String toString() {
            return this.entry.toString();
        }
    }
    
    private static final class StringIterator<T> implements Iterator<String>
    {
        private final Iterator<T> iter;
        
        public StringIterator(final Iterator<T> iter) {
            this.iter = iter;
        }
        
        @Override
        public boolean hasNext() {
            return this.iter.hasNext();
        }
        
        @Override
        public String next() {
            final T next = this.iter.next();
            return (next != null) ? next.toString() : null;
        }
        
        @Override
        public void remove() {
            this.iter.remove();
        }
    }
    
    private static final class CharSequenceDelegatingStringSet extends DelegatingStringSet<CharSequence>
    {
        public CharSequenceDelegatingStringSet(final Set<CharSequence> allNames) {
            super(allNames);
        }
        
        @Override
        public boolean add(final String e) {
            return this.allNames.add((T)e);
        }
        
        @Override
        public boolean addAll(final Collection<? extends String> c) {
            return this.allNames.addAll((Collection<? extends T>)c);
        }
    }
    
    private abstract static class DelegatingStringSet<T> implements Set<String>
    {
        protected final Set<T> allNames;
        
        public DelegatingStringSet(final Set<T> allNames) {
            this.allNames = ObjectUtil.checkNotNull(allNames, "allNames");
        }
        
        @Override
        public int size() {
            return this.allNames.size();
        }
        
        @Override
        public boolean isEmpty() {
            return this.allNames.isEmpty();
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.allNames.contains(o.toString());
        }
        
        @Override
        public Iterator<String> iterator() {
            return new StringIterator<Object>(this.allNames.iterator());
        }
        
        @Override
        public Object[] toArray() {
            final Object[] arr = new Object[this.size()];
            this.fillArray(arr);
            return arr;
        }
        
        @Override
        public <X> X[] toArray(final X[] a) {
            if (a == null || a.length < this.size()) {
                final X[] arr = (X[])new Object[this.size()];
                this.fillArray(arr);
                return arr;
            }
            this.fillArray(a);
            return a;
        }
        
        private void fillArray(final Object[] arr) {
            final Iterator<T> itr = this.allNames.iterator();
            for (int i = 0; i < this.size(); ++i) {
                arr[i] = itr.next();
            }
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.allNames.remove(o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            for (final Object o : c) {
                if (!this.contains(o)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            boolean modified = false;
            for (final Object o : c) {
                if (this.remove(o)) {
                    modified = true;
                }
            }
            return modified;
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            boolean modified = false;
            final Iterator<String> it = this.iterator();
            while (it.hasNext()) {
                if (!c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }
        
        @Override
        public void clear() {
            this.allNames.clear();
        }
    }
}
