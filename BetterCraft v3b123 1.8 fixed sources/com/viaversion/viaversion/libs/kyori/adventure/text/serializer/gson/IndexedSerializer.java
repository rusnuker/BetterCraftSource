// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class IndexedSerializer<E> extends TypeAdapter<E>
{
    private final String name;
    private final Index<String, E> map;
    private final boolean throwOnUnknownKey;
    
    public static <E> TypeAdapter<E> strict(final String name, final Index<String, E> map) {
        return new IndexedSerializer<E>(name, map, true).nullSafe();
    }
    
    public static <E> TypeAdapter<E> lenient(final String name, final Index<String, E> map) {
        return new IndexedSerializer<E>(name, map, false).nullSafe();
    }
    
    private IndexedSerializer(final String name, final Index<String, E> map, final boolean throwOnUnknownKey) {
        this.name = name;
        this.map = map;
        this.throwOnUnknownKey = throwOnUnknownKey;
    }
    
    @Override
    public void write(final JsonWriter out, final E value) throws IOException {
        out.value(this.map.key(value));
    }
    
    @Override
    public E read(final JsonReader in) throws IOException {
        final String string = in.nextString();
        final E value = this.map.value(string);
        if (value != null) {
            return value;
        }
        if (this.throwOnUnknownKey) {
            throw new JsonParseException("invalid " + this.name + ":  " + string);
        }
        return null;
    }
}
