// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.internal;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;

public class AbbreviationMap<V> implements OptionNameMap<V>
{
    private final Map<Character, AbbreviationMap<V>> children;
    private String key;
    private V value;
    private int keysBeyond;
    
    public AbbreviationMap() {
        this.children = new TreeMap<Character, AbbreviationMap<V>>();
    }
    
    @Override
    public boolean contains(final String key) {
        return this.get(key) != null;
    }
    
    @Override
    public V get(final String key) {
        final char[] chars = charsOf(key);
        AbbreviationMap<V> child = this;
        for (final char each : chars) {
            child = child.children.get(each);
            if (child == null) {
                return null;
            }
        }
        return child.value;
    }
    
    @Override
    public void put(final String key, final V newValue) {
        if (newValue == null) {
            throw new NullPointerException();
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException();
        }
        final char[] chars = charsOf(key);
        this.add(chars, newValue, 0, chars.length);
    }
    
    @Override
    public void putAll(final Iterable<String> keys, final V newValue) {
        for (final String each : keys) {
            this.put(each, newValue);
        }
    }
    
    private boolean add(final char[] chars, final V newValue, final int offset, final int length) {
        if (offset == length) {
            this.value = newValue;
            final boolean wasAlreadyAKey = this.key != null;
            this.key = new String(chars);
            return !wasAlreadyAKey;
        }
        final char nextChar = chars[offset];
        AbbreviationMap<V> child = this.children.get(nextChar);
        if (child == null) {
            child = new AbbreviationMap<V>();
            this.children.put(nextChar, child);
        }
        final boolean newKeyAdded = child.add(chars, newValue, offset + 1, length);
        if (newKeyAdded) {
            ++this.keysBeyond;
        }
        if (this.key == null) {
            this.value = ((this.keysBeyond > 1) ? null : newValue);
        }
        return newKeyAdded;
    }
    
    @Override
    public void remove(final String key) {
        if (key.length() == 0) {
            throw new IllegalArgumentException();
        }
        final char[] keyChars = charsOf(key);
        this.remove(keyChars, 0, keyChars.length);
    }
    
    private boolean remove(final char[] aKey, final int offset, final int length) {
        if (offset == length) {
            return this.removeAtEndOfKey();
        }
        final char nextChar = aKey[offset];
        final AbbreviationMap<V> child = this.children.get(nextChar);
        if (child == null || !child.remove(aKey, offset + 1, length)) {
            return false;
        }
        --this.keysBeyond;
        if (child.keysBeyond == 0) {
            this.children.remove(nextChar);
        }
        if (this.keysBeyond == 1 && this.key == null) {
            this.setValueToThatOfOnlyChild();
        }
        return true;
    }
    
    private void setValueToThatOfOnlyChild() {
        final Map.Entry<Character, AbbreviationMap<V>> entry = this.children.entrySet().iterator().next();
        final AbbreviationMap<V> onlyChild = entry.getValue();
        this.value = onlyChild.value;
    }
    
    private boolean removeAtEndOfKey() {
        if (this.key == null) {
            return false;
        }
        this.key = null;
        if (this.keysBeyond == 1) {
            this.setValueToThatOfOnlyChild();
        }
        else {
            this.value = null;
        }
        return true;
    }
    
    @Override
    public Map<String, V> toJavaUtilMap() {
        final Map<String, V> mappings = new TreeMap<String, V>();
        this.addToMappings(mappings);
        return mappings;
    }
    
    private void addToMappings(final Map<String, V> mappings) {
        if (this.key != null) {
            mappings.put(this.key, this.value);
        }
        for (final AbbreviationMap<V> each : this.children.values()) {
            each.addToMappings(mappings);
        }
    }
    
    private static char[] charsOf(final String aKey) {
        final char[] chars = new char[aKey.length()];
        aKey.getChars(0, aKey.length(), chars, 0);
        return chars;
    }
}
