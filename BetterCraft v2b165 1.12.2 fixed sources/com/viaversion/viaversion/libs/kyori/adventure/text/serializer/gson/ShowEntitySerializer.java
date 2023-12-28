// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.JsonSerializationContext;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.util.UUID;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.gson.JsonDeserializer;

final class ShowEntitySerializer implements JsonDeserializer<HoverEvent.ShowEntity>, JsonSerializer<HoverEvent.ShowEntity>
{
    static final String TYPE = "type";
    static final String ID = "id";
    static final String NAME = "name";
    
    @Override
    public HoverEvent.ShowEntity deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = json.getAsJsonObject();
        if (!object.has("type") || !object.has("id")) {
            throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
        }
        final Key type = context.deserialize(object.getAsJsonPrimitive("type"), Key.class);
        final UUID id = UUID.fromString(object.getAsJsonPrimitive("id").getAsString());
        Component name = null;
        if (object.has("name")) {
            name = context.deserialize(object.get("name"), Component.class);
        }
        return HoverEvent.ShowEntity.of(type, id, name);
    }
    
    @Override
    public JsonElement serialize(final HoverEvent.ShowEntity src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject json = new JsonObject();
        json.add("type", context.serialize(src.type()));
        json.addProperty("id", src.id().toString());
        final Component name = src.name();
        if (name != null) {
            json.add("name", context.serialize(name));
        }
        return json;
    }
}
