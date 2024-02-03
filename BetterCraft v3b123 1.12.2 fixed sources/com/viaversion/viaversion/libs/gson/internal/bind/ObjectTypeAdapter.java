// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.ArrayDeque;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.ToNumberPolicy;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.ToNumberStrategy;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

public final class ObjectTypeAdapter extends TypeAdapter<Object>
{
    private static final TypeAdapterFactory DOUBLE_FACTORY;
    private final Gson gson;
    private final ToNumberStrategy toNumberStrategy;
    
    private ObjectTypeAdapter(final Gson gson, final ToNumberStrategy toNumberStrategy) {
        this.gson = gson;
        this.toNumberStrategy = toNumberStrategy;
    }
    
    private static TypeAdapterFactory newFactory(final ToNumberStrategy toNumberStrategy) {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
                if (type.getRawType() == Object.class) {
                    return (TypeAdapter<T>)new ObjectTypeAdapter(gson, toNumberStrategy, null);
                }
                return null;
            }
        };
    }
    
    public static TypeAdapterFactory getFactory(final ToNumberStrategy toNumberStrategy) {
        if (toNumberStrategy == ToNumberPolicy.DOUBLE) {
            return ObjectTypeAdapter.DOUBLE_FACTORY;
        }
        return newFactory(toNumberStrategy);
    }
    
    private Object tryBeginNesting(final JsonReader in, final JsonToken peeked) throws IOException {
        switch (peeked) {
            case BEGIN_ARRAY: {
                in.beginArray();
                return new ArrayList();
            }
            case BEGIN_OBJECT: {
                in.beginObject();
                return new LinkedTreeMap();
            }
            default: {
                return null;
            }
        }
    }
    
    private Object readTerminal(final JsonReader in, final JsonToken peeked) throws IOException {
        switch (peeked) {
            case STRING: {
                return in.nextString();
            }
            case NUMBER: {
                return this.toNumberStrategy.readNumber(in);
            }
            case BOOLEAN: {
                return in.nextBoolean();
            }
            case NULL: {
                in.nextNull();
                return null;
            }
            default: {
                throw new IllegalStateException("Unexpected token: " + peeked);
            }
        }
    }
    
    @Override
    public Object read(final JsonReader in) throws IOException {
        JsonToken peeked = in.peek();
        Object current = this.tryBeginNesting(in, peeked);
        if (current == null) {
            return this.readTerminal(in, peeked);
        }
        final Deque<Object> stack = new ArrayDeque<Object>();
        while (true) {
            if (in.hasNext()) {
                String name = null;
                if (current instanceof Map) {
                    name = in.nextName();
                }
                peeked = in.peek();
                Object value = this.tryBeginNesting(in, peeked);
                final boolean isNesting = value != null;
                if (value == null) {
                    value = this.readTerminal(in, peeked);
                }
                if (current instanceof List) {
                    final List<Object> list = (List<Object>)current;
                    list.add(value);
                }
                else {
                    final Map<String, Object> map = (Map<String, Object>)current;
                    map.put(name, value);
                }
                if (!isNesting) {
                    continue;
                }
                stack.addLast(current);
                current = value;
            }
            else {
                if (current instanceof List) {
                    in.endArray();
                }
                else {
                    in.endObject();
                }
                if (stack.isEmpty()) {
                    break;
                }
                current = stack.removeLast();
            }
        }
        return current;
    }
    
    @Override
    public void write(final JsonWriter out, final Object value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        final TypeAdapter<Object> typeAdapter = this.gson.getAdapter(value.getClass());
        if (typeAdapter instanceof ObjectTypeAdapter) {
            out.beginObject();
            out.endObject();
            return;
        }
        typeAdapter.write(out, value);
    }
    
    static {
        DOUBLE_FACTORY = newFactory(ToNumberPolicy.DOUBLE);
    }
}
