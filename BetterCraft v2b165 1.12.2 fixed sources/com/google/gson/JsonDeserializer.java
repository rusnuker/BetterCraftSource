// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializer<T>
{
    T deserialize(final JsonElement p0, final Type p1, final JsonDeserializationContext p2) throws JsonParseException;
}
