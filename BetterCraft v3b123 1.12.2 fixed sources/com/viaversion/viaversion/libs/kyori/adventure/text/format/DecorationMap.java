// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.AbstractCollection;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Set;
import java.util.Arrays;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import org.jetbrains.annotations.Unmodifiable;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import java.util.AbstractMap;

@Unmodifiable
final class DecorationMap extends AbstractMap<TextDecoration, TextDecoration.State> implements Examinable
{
    static final TextDecoration[] DECORATIONS;
    private static final TextDecoration.State[] STATES;
    private static final int MAP_SIZE;
    private static final TextDecoration.State[] EMPTY_STATE_ARRAY;
    static final DecorationMap EMPTY;
    private static final KeySet KEY_SET;
    private final int bitSet;
    private volatile EntrySet entrySet;
    private volatile Values values;
    
    static DecorationMap fromMap(final Map<TextDecoration, TextDecoration.State> decorationMap) {
        if (decorationMap instanceof DecorationMap) {
            return (DecorationMap)decorationMap;
        }
        int bitSet = 0;
        for (final TextDecoration decoration : DecorationMap.DECORATIONS) {
            bitSet |= decorationMap.getOrDefault(decoration, TextDecoration.State.NOT_SET).ordinal() * offset(decoration);
        }
        return withBitSet(bitSet);
    }
    
    static DecorationMap merge(final Map<TextDecoration, TextDecoration.State> first, final Map<TextDecoration, TextDecoration.State> second) {
        int bitSet = 0;
        for (final TextDecoration decoration : DecorationMap.DECORATIONS) {
            bitSet |= first.getOrDefault(decoration, second.getOrDefault(decoration, TextDecoration.State.NOT_SET)).ordinal() * offset(decoration);
        }
        return withBitSet(bitSet);
    }
    
    private static DecorationMap withBitSet(final int bitSet) {
        return (bitSet == 0) ? DecorationMap.EMPTY : new DecorationMap(bitSet);
    }
    
    private static int offset(final TextDecoration decoration) {
        return 1 << decoration.ordinal() * 2;
    }
    
    private DecorationMap(final int bitSet) {
        this.entrySet = null;
        this.values = null;
        this.bitSet = bitSet;
    }
    
    @NotNull
    public DecorationMap with(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(decoration, "decoration");
        final int offset = offset(decoration);
        return withBitSet((this.bitSet & ~(3 * offset)) | state.ordinal() * offset);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Arrays.stream(DecorationMap.DECORATIONS).map(decoration -> ExaminableProperty.of(decoration.toString(), this.get(decoration)));
    }
    
    @Override
    public TextDecoration.State get(final Object o) {
        if (o instanceof TextDecoration) {
            return DecorationMap.STATES[this.bitSet >> ((TextDecoration)o).ordinal() * 2 & 0x3];
        }
        return null;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return key instanceof TextDecoration;
    }
    
    @Override
    public int size() {
        return DecorationMap.MAP_SIZE;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @NotNull
    @Override
    public Set<Map.Entry<TextDecoration, TextDecoration.State>> entrySet() {
        if (this.entrySet == null) {
            synchronized (this) {
                if (this.entrySet == null) {
                    this.entrySet = new EntrySet();
                }
            }
        }
        return this.entrySet;
    }
    
    @NotNull
    @Override
    public Set<TextDecoration> keySet() {
        return DecorationMap.KEY_SET;
    }
    
    @NotNull
    @Override
    public Collection<TextDecoration.State> values() {
        if (this.values == null) {
            synchronized (this) {
                if (this.values == null) {
                    this.values = new Values();
                }
            }
        }
        return this.values;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other == this || (other != null && other.getClass() == DecorationMap.class && this.bitSet == ((DecorationMap)other).bitSet);
    }
    
    @Override
    public int hashCode() {
        return this.bitSet;
    }
    
    static {
        DECORATIONS = TextDecoration.values();
        STATES = TextDecoration.State.values();
        MAP_SIZE = DecorationMap.DECORATIONS.length;
        EMPTY_STATE_ARRAY = new TextDecoration.State[0];
        EMPTY = new DecorationMap(0);
        KEY_SET = new KeySet();
    }
    
    final class EntrySet extends AbstractSet<Map.Entry<TextDecoration, TextDecoration.State>>
    {
        @NotNull
        @Override
        public Iterator<Map.Entry<TextDecoration, TextDecoration.State>> iterator() {
            return new Iterator<Map.Entry<TextDecoration, TextDecoration.State>>() {
                private final Iterator<TextDecoration> decorations = DecorationMap.KEY_SET.iterator();
                private final Iterator<TextDecoration.State> states = DecorationMap.this.values().iterator();
                
                @Override
                public boolean hasNext() {
                    return this.decorations.hasNext() && this.states.hasNext();
                }
                
                @Override
                public Map.Entry<TextDecoration, TextDecoration.State> next() {
                    if (this.hasNext()) {
                        return new SimpleImmutableEntry<TextDecoration, TextDecoration.State>(this.decorations.next(), this.states.next());
                    }
                    throw new NoSuchElementException();
                }
            };
        }
        
        @Override
        public int size() {
            return DecorationMap.MAP_SIZE;
        }
    }
    
    final class Values extends AbstractCollection<TextDecoration.State>
    {
        @NotNull
        @Override
        public Iterator<TextDecoration.State> iterator() {
            return Spliterators.iterator((Spliterator<? extends TextDecoration.State>)Arrays.spliterator(this.toArray((T[])DecorationMap.EMPTY_STATE_ARRAY)));
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            final TextDecoration.State[] states = new TextDecoration.State[DecorationMap.MAP_SIZE];
            for (int i = 0; i < DecorationMap.MAP_SIZE; ++i) {
                states[i] = DecorationMap.this.get(DecorationMap.DECORATIONS[i]);
            }
            return states;
        }
        
        @Override
        public <T> T[] toArray(final T[] dest) {
            if (dest.length < DecorationMap.MAP_SIZE) {
                return Arrays.copyOf(this.toArray(), DecorationMap.MAP_SIZE, (Class<? extends T[]>)dest.getClass());
            }
            System.arraycopy(this.toArray(), 0, dest, 0, DecorationMap.MAP_SIZE);
            if (dest.length > DecorationMap.MAP_SIZE) {
                dest[DecorationMap.MAP_SIZE] = null;
            }
            return dest;
        }
        
        @Override
        public boolean contains(final Object o) {
            return o instanceof TextDecoration.State && super.contains(o);
        }
        
        @Override
        public int size() {
            return DecorationMap.MAP_SIZE;
        }
    }
    
    static final class KeySet extends AbstractSet<TextDecoration>
    {
        @Override
        public boolean contains(final Object o) {
            return o instanceof TextDecoration;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return DecorationMap.DECORATIONS.clone();
        }
        
        @Override
        public <T> T[] toArray(final T[] dest) {
            if (dest.length < DecorationMap.MAP_SIZE) {
                return Arrays.copyOf(DecorationMap.DECORATIONS, DecorationMap.MAP_SIZE, (Class<? extends T[]>)dest.getClass());
            }
            System.arraycopy(DecorationMap.DECORATIONS, 0, dest, 0, DecorationMap.MAP_SIZE);
            if (dest.length > DecorationMap.MAP_SIZE) {
                dest[DecorationMap.MAP_SIZE] = null;
            }
            return dest;
        }
        
        @NotNull
        @Override
        public Iterator<TextDecoration> iterator() {
            return Spliterators.iterator((Spliterator<? extends TextDecoration>)Arrays.spliterator(DecorationMap.DECORATIONS));
        }
        
        @Override
        public int size() {
            return DecorationMap.MAP_SIZE;
        }
    }
}
