// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import java.lang.reflect.TypeVariable;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T>
{
    private final Gson context;
    private final TypeAdapter<T> delegate;
    private final Type type;
    
    TypeAdapterRuntimeTypeWrapper(final Gson context, final TypeAdapter<T> delegate, final Type type) {
        this.context = context;
        this.delegate = delegate;
        this.type = type;
    }
    
    @Override
    public T read(final JsonReader in) throws IOException {
        return this.delegate.read(in);
    }
    
    @Override
    public void write(final JsonWriter out, final T value) throws IOException {
        TypeAdapter<T> chosen = this.delegate;
        final Type runtimeType = getRuntimeTypeIfMoreSpecific(this.type, value);
        if (runtimeType != this.type) {
            final TypeAdapter<T> runtimeTypeAdapter = this.context.getAdapter(TypeToken.get(runtimeType));
            if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                chosen = runtimeTypeAdapter;
            }
            else if (!isReflective(this.delegate)) {
                chosen = this.delegate;
            }
            else {
                chosen = runtimeTypeAdapter;
            }
        }
        chosen.write(out, value);
    }
    
    private static boolean isReflective(TypeAdapter<?> typeAdapter) {
        while (typeAdapter instanceof SerializationDelegatingTypeAdapter) {
            final TypeAdapter<?> delegate = ((SerializationDelegatingTypeAdapter)typeAdapter).getSerializationDelegate();
            if (delegate == typeAdapter) {
                break;
            }
            typeAdapter = delegate;
        }
        return typeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter;
    }
    
    private static Type getRuntimeTypeIfMoreSpecific(Type type, final Object value) {
        if (value != null && (type instanceof Class || type instanceof TypeVariable)) {
            type = value.getClass();
        }
        return type;
    }
}
