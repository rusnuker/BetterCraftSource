// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson;

import com.viaversion.viaversion.libs.gson.internal.NonNullElementWrapperList;
import java.util.List;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

public final class JsonArray extends JsonElement implements Iterable<JsonElement>
{
    private final ArrayList<JsonElement> elements;
    
    public JsonArray() {
        this.elements = new ArrayList<JsonElement>();
    }
    
    public JsonArray(final int capacity) {
        this.elements = new ArrayList<JsonElement>(capacity);
    }
    
    @Override
    public JsonArray deepCopy() {
        if (!this.elements.isEmpty()) {
            final JsonArray result = new JsonArray(this.elements.size());
            for (final JsonElement element : this.elements) {
                result.add(element.deepCopy());
            }
            return result;
        }
        return new JsonArray();
    }
    
    public void add(final Boolean bool) {
        this.elements.add((bool == null) ? JsonNull.INSTANCE : new JsonPrimitive(bool));
    }
    
    public void add(final Character character) {
        this.elements.add((character == null) ? JsonNull.INSTANCE : new JsonPrimitive(character));
    }
    
    public void add(final Number number) {
        this.elements.add((number == null) ? JsonNull.INSTANCE : new JsonPrimitive(number));
    }
    
    public void add(final String string) {
        this.elements.add((string == null) ? JsonNull.INSTANCE : new JsonPrimitive(string));
    }
    
    public void add(JsonElement element) {
        if (element == null) {
            element = JsonNull.INSTANCE;
        }
        this.elements.add(element);
    }
    
    public void addAll(final JsonArray array) {
        this.elements.addAll(array.elements);
    }
    
    public JsonElement set(final int index, final JsonElement element) {
        return this.elements.set(index, (element == null) ? JsonNull.INSTANCE : element);
    }
    
    public boolean remove(final JsonElement element) {
        return this.elements.remove(element);
    }
    
    public JsonElement remove(final int index) {
        return this.elements.remove(index);
    }
    
    public boolean contains(final JsonElement element) {
        return this.elements.contains(element);
    }
    
    public int size() {
        return this.elements.size();
    }
    
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }
    
    @Override
    public Iterator<JsonElement> iterator() {
        return this.elements.iterator();
    }
    
    public JsonElement get(final int i) {
        return this.elements.get(i);
    }
    
    private JsonElement getAsSingleElement() {
        final int size = this.elements.size();
        if (size == 1) {
            return this.elements.get(0);
        }
        throw new IllegalStateException("Array must have size 1, but has size " + size);
    }
    
    @Override
    public Number getAsNumber() {
        return this.getAsSingleElement().getAsNumber();
    }
    
    @Override
    public String getAsString() {
        return this.getAsSingleElement().getAsString();
    }
    
    @Override
    public double getAsDouble() {
        return this.getAsSingleElement().getAsDouble();
    }
    
    @Override
    public BigDecimal getAsBigDecimal() {
        return this.getAsSingleElement().getAsBigDecimal();
    }
    
    @Override
    public BigInteger getAsBigInteger() {
        return this.getAsSingleElement().getAsBigInteger();
    }
    
    @Override
    public float getAsFloat() {
        return this.getAsSingleElement().getAsFloat();
    }
    
    @Override
    public long getAsLong() {
        return this.getAsSingleElement().getAsLong();
    }
    
    @Override
    public int getAsInt() {
        return this.getAsSingleElement().getAsInt();
    }
    
    @Override
    public byte getAsByte() {
        return this.getAsSingleElement().getAsByte();
    }
    
    @Deprecated
    @Override
    public char getAsCharacter() {
        return this.getAsSingleElement().getAsCharacter();
    }
    
    @Override
    public short getAsShort() {
        return this.getAsSingleElement().getAsShort();
    }
    
    @Override
    public boolean getAsBoolean() {
        return this.getAsSingleElement().getAsBoolean();
    }
    
    public List<JsonElement> asList() {
        return new NonNullElementWrapperList<JsonElement>(this.elements);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof JsonArray && ((JsonArray)o).elements.equals(this.elements));
    }
    
    @Override
    public int hashCode() {
        return this.elements.hashCode();
    }
}
