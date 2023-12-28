// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.JsonSerializationContext;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.kyori.adventure.nbt.api.BinaryTagHolder;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.gson.JsonDeserializer;

final class ShowItemSerializer implements JsonDeserializer<HoverEvent.ShowItem>, JsonSerializer<HoverEvent.ShowItem>
{
    static final String ID = "id";
    static final String COUNT = "count";
    static final String TAG = "tag";
    
    @Override
    public HoverEvent.ShowItem deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = json.getAsJsonObject();
        if (!object.has("id")) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
        }
        final Key id = context.deserialize(object.getAsJsonPrimitive("id"), Key.class);
        int count = 1;
        if (object.has("count")) {
            count = object.get("count").getAsInt();
        }
        BinaryTagHolder nbt = null;
        if (object.has("tag")) {
            final JsonElement tag = object.get("tag");
            if (tag.isJsonPrimitive()) {
                nbt = BinaryTagHolder.of(tag.getAsString());
            }
            else if (!tag.isJsonNull()) {
                throw new JsonParseException("Expected tag to be a string");
            }
        }
        return HoverEvent.ShowItem.of(id, count, nbt);
    }
    
    @Override
    public JsonElement serialize(final HoverEvent.ShowItem src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject json = new JsonObject();
        json.add("id", context.serialize(src.item()));
        final int count = src.count();
        if (count != 1) {
            json.addProperty("count", count);
        }
        final BinaryTagHolder nbt = src.nbt();
        if (nbt != null) {
            json.addProperty("tag", nbt.string());
        }
        return json;
    }
}
