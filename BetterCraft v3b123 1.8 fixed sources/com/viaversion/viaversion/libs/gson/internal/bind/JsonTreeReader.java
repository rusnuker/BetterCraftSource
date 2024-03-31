// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import java.util.Arrays;
import java.util.Map;
import com.viaversion.viaversion.libs.gson.stream.MalformedJsonException;
import com.viaversion.viaversion.libs.gson.JsonNull;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.io.Reader;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;

public final class JsonTreeReader extends JsonReader
{
    private static final Reader UNREADABLE_READER;
    private static final Object SENTINEL_CLOSED;
    private Object[] stack;
    private int stackSize;
    private String[] pathNames;
    private int[] pathIndices;
    
    public JsonTreeReader(final JsonElement element) {
        super(JsonTreeReader.UNREADABLE_READER);
        this.stack = new Object[32];
        this.stackSize = 0;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        this.push(element);
    }
    
    @Override
    public void beginArray() throws IOException {
        this.expect(JsonToken.BEGIN_ARRAY);
        final JsonArray array = (JsonArray)this.peekStack();
        this.push(array.iterator());
        this.pathIndices[this.stackSize - 1] = 0;
    }
    
    @Override
    public void endArray() throws IOException {
        this.expect(JsonToken.END_ARRAY);
        this.popStack();
        this.popStack();
        if (this.stackSize > 0) {
            final int[] pathIndices = this.pathIndices;
            final int n = this.stackSize - 1;
            ++pathIndices[n];
        }
    }
    
    @Override
    public void beginObject() throws IOException {
        this.expect(JsonToken.BEGIN_OBJECT);
        final JsonObject object = (JsonObject)this.peekStack();
        this.push(object.entrySet().iterator());
    }
    
    @Override
    public void endObject() throws IOException {
        this.expect(JsonToken.END_OBJECT);
        this.pathNames[this.stackSize - 1] = null;
        this.popStack();
        this.popStack();
        if (this.stackSize > 0) {
            final int[] pathIndices = this.pathIndices;
            final int n = this.stackSize - 1;
            ++pathIndices[n];
        }
    }
    
    @Override
    public boolean hasNext() throws IOException {
        final JsonToken token = this.peek();
        return token != JsonToken.END_OBJECT && token != JsonToken.END_ARRAY && token != JsonToken.END_DOCUMENT;
    }
    
    @Override
    public JsonToken peek() throws IOException {
        if (this.stackSize == 0) {
            return JsonToken.END_DOCUMENT;
        }
        final Object o = this.peekStack();
        if (o instanceof Iterator) {
            final boolean isObject = this.stack[this.stackSize - 2] instanceof JsonObject;
            final Iterator<?> iterator = (Iterator<?>)o;
            if (!iterator.hasNext()) {
                return isObject ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
            }
            if (isObject) {
                return JsonToken.NAME;
            }
            this.push(iterator.next());
            return this.peek();
        }
        else {
            if (o instanceof JsonObject) {
                return JsonToken.BEGIN_OBJECT;
            }
            if (o instanceof JsonArray) {
                return JsonToken.BEGIN_ARRAY;
            }
            if (o instanceof JsonPrimitive) {
                final JsonPrimitive primitive = (JsonPrimitive)o;
                if (primitive.isString()) {
                    return JsonToken.STRING;
                }
                if (primitive.isBoolean()) {
                    return JsonToken.BOOLEAN;
                }
                if (primitive.isNumber()) {
                    return JsonToken.NUMBER;
                }
                throw new AssertionError();
            }
            else {
                if (o instanceof JsonNull) {
                    return JsonToken.NULL;
                }
                if (o == JsonTreeReader.SENTINEL_CLOSED) {
                    throw new IllegalStateException("JsonReader is closed");
                }
                throw new MalformedJsonException("Custom JsonElement subclass " + o.getClass().getName() + " is not supported");
            }
        }
    }
    
    private Object peekStack() {
        return this.stack[this.stackSize - 1];
    }
    
    private Object popStack() {
        final Object[] stack = this.stack;
        final int stackSize = this.stackSize - 1;
        this.stackSize = stackSize;
        final Object result = stack[stackSize];
        this.stack[this.stackSize] = null;
        return result;
    }
    
    private void expect(final JsonToken expected) throws IOException {
        if (this.peek() != expected) {
            throw new IllegalStateException("Expected " + expected + " but was " + this.peek() + this.locationString());
        }
    }
    
    private String nextName(final boolean skipName) throws IOException {
        this.expect(JsonToken.NAME);
        final Iterator<?> i = (Iterator<?>)this.peekStack();
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)i.next();
        final String result = (String)entry.getKey();
        this.pathNames[this.stackSize - 1] = (skipName ? "<skipped>" : result);
        this.push(entry.getValue());
        return result;
    }
    
    @Override
    public String nextName() throws IOException {
        return this.nextName(false);
    }
    
    @Override
    public String nextString() throws IOException {
        final JsonToken token = this.peek();
        if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + token + this.locationString());
        }
        final String result = ((JsonPrimitive)this.popStack()).getAsString();
        if (this.stackSize > 0) {
            final int[] pathIndices = this.pathIndices;
            final int n = this.stackSize - 1;
            ++pathIndices[n];
        }
        return result;
    }
    
    @Override
    public boolean nextBoolean() throws IOException {
        this.expect(JsonToken.BOOLEAN);
        final boolean result = ((JsonPrimitive)this.popStack()).getAsBoolean();
        if (this.stackSize > 0) {
            final int[] pathIndices = this.pathIndices;
            final int n = this.stackSize - 1;
            ++pathIndices[n];
        }
        return result;
    }
    
    @Override
    public void nextNull() throws IOException {
        this.expect(JsonToken.NULL);
        this.popStack();
        if (this.stackSize > 0) {
            final int[] pathIndices = this.pathIndices;
            final int n = this.stackSize - 1;
            ++pathIndices[n];
        }
    }
    
    @Override
    public double nextDouble() throws IOException {
        final JsonToken token = this.peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token + this.locationString());
        }
        final double result = ((JsonPrimitive)this.peekStack()).getAsDouble();
        if (!this.isLenient() && (Double.isNaN(result) || Double.isInfinite(result))) {
            throw new MalformedJsonException("JSON forbids NaN and infinities: " + result);
        }
        this.popStack();
        if (this.stackSize > 0) {
            final int[] pathIndices = this.pathIndices;
            final int n = this.stackSize - 1;
            ++pathIndices[n];
        }
        return result;
    }
    
    @Override
    public long nextLong() throws IOException {
        final JsonToken token = this.peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token + this.locationString());
        }
        final long result = ((JsonPrimitive)this.peekStack()).getAsLong();
        this.popStack();
        if (this.stackSize > 0) {
            final int[] pathIndices = this.pathIndices;
            final int n = this.stackSize - 1;
            ++pathIndices[n];
        }
        return result;
    }
    
    @Override
    public int nextInt() throws IOException {
        final JsonToken token = this.peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token + this.locationString());
        }
        final int result = ((JsonPrimitive)this.peekStack()).getAsInt();
        this.popStack();
        if (this.stackSize > 0) {
            final int[] pathIndices = this.pathIndices;
            final int n = this.stackSize - 1;
            ++pathIndices[n];
        }
        return result;
    }
    
    JsonElement nextJsonElement() throws IOException {
        final JsonToken peeked = this.peek();
        if (peeked == JsonToken.NAME || peeked == JsonToken.END_ARRAY || peeked == JsonToken.END_OBJECT || peeked == JsonToken.END_DOCUMENT) {
            throw new IllegalStateException("Unexpected " + peeked + " when reading a JsonElement.");
        }
        final JsonElement element = (JsonElement)this.peekStack();
        this.skipValue();
        return element;
    }
    
    @Override
    public void close() throws IOException {
        this.stack = new Object[] { JsonTreeReader.SENTINEL_CLOSED };
        this.stackSize = 1;
    }
    
    @Override
    public void skipValue() throws IOException {
        final JsonToken peeked = this.peek();
        switch (peeked) {
            case NAME: {
                final String unused = this.nextName(true);
                break;
            }
            case END_ARRAY: {
                this.endArray();
                break;
            }
            case END_OBJECT: {
                this.endObject();
                break;
            }
            case END_DOCUMENT: {
                break;
            }
            default: {
                this.popStack();
                if (this.stackSize > 0) {
                    final int[] pathIndices = this.pathIndices;
                    final int n = this.stackSize - 1;
                    ++pathIndices[n];
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + this.locationString();
    }
    
    public void promoteNameToValue() throws IOException {
        this.expect(JsonToken.NAME);
        final Iterator<?> i = (Iterator<?>)this.peekStack();
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)i.next();
        this.push(entry.getValue());
        this.push(new JsonPrimitive((String)entry.getKey()));
    }
    
    private void push(final Object newTop) {
        if (this.stackSize == this.stack.length) {
            final int newLength = this.stackSize * 2;
            this.stack = Arrays.copyOf(this.stack, newLength);
            this.pathIndices = Arrays.copyOf(this.pathIndices, newLength);
            this.pathNames = Arrays.copyOf(this.pathNames, newLength);
        }
        this.stack[this.stackSize++] = newTop;
    }
    
    private String getPath(final boolean usePreviousPath) {
        final StringBuilder result = new StringBuilder().append('$');
        for (int i = 0; i < this.stackSize; ++i) {
            if (this.stack[i] instanceof JsonArray) {
                if (++i < this.stackSize && this.stack[i] instanceof Iterator) {
                    int pathIndex = this.pathIndices[i];
                    if (usePreviousPath && pathIndex > 0 && (i == this.stackSize - 1 || i == this.stackSize - 2)) {
                        --pathIndex;
                    }
                    result.append('[').append(pathIndex).append(']');
                }
            }
            else if (this.stack[i] instanceof JsonObject && ++i < this.stackSize && this.stack[i] instanceof Iterator) {
                result.append('.');
                if (this.pathNames[i] != null) {
                    result.append(this.pathNames[i]);
                }
            }
        }
        return result.toString();
    }
    
    @Override
    public String getPreviousPath() {
        return this.getPath(true);
    }
    
    @Override
    public String getPath() {
        return this.getPath(false);
    }
    
    private String locationString() {
        return " at path " + this.getPath();
    }
    
    static {
        UNREADABLE_READER = new Reader() {
            @Override
            public int read(final char[] buffer, final int offset, final int count) {
                throw new AssertionError();
            }
            
            @Override
            public void close() {
                throw new AssertionError();
            }
        };
        SENTINEL_CLOSED = new Object();
    }
}
