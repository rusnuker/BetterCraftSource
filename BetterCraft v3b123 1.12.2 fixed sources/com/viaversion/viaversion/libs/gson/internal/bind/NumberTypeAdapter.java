// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.ToNumberPolicy;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.ToNumberStrategy;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

public final class NumberTypeAdapter extends TypeAdapter<Number>
{
    private static final TypeAdapterFactory LAZILY_PARSED_NUMBER_FACTORY;
    private final ToNumberStrategy toNumberStrategy;
    
    private NumberTypeAdapter(final ToNumberStrategy toNumberStrategy) {
        this.toNumberStrategy = toNumberStrategy;
    }
    
    private static TypeAdapterFactory newFactory(final ToNumberStrategy toNumberStrategy) {
        final NumberTypeAdapter adapter = new NumberTypeAdapter(toNumberStrategy);
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
                return (TypeAdapter<T>)((type.getRawType() == Number.class) ? adapter : null);
            }
        };
    }
    
    public static TypeAdapterFactory getFactory(final ToNumberStrategy toNumberStrategy) {
        if (toNumberStrategy == ToNumberPolicy.LAZILY_PARSED_NUMBER) {
            return NumberTypeAdapter.LAZILY_PARSED_NUMBER_FACTORY;
        }
        return newFactory(toNumberStrategy);
    }
    
    @Override
    public Number read(final JsonReader in) throws IOException {
        final JsonToken jsonToken = in.peek();
        switch (jsonToken) {
            case NULL: {
                in.nextNull();
                return null;
            }
            case NUMBER:
            case STRING: {
                return this.toNumberStrategy.readNumber(in);
            }
            default: {
                throw new JsonSyntaxException("Expecting number, got: " + jsonToken + "; at path " + in.getPath());
            }
        }
    }
    
    @Override
    public void write(final JsonWriter out, final Number value) throws IOException {
        out.value(value);
    }
    
    static {
        LAZILY_PARSED_NUMBER_FACTORY = newFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    }
}
