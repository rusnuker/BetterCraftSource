// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import java.io.StringReader;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import javax.annotation.Nullable;
import com.google.gson.JsonArray;
import net.minecraft.item.Item;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils
{
    public static boolean isString(final JsonObject json, final String memberName) {
        return isJsonPrimitive(json, memberName) && json.getAsJsonPrimitive(memberName).isString();
    }
    
    public static boolean isString(final JsonElement json) {
        return json.isJsonPrimitive() && json.getAsJsonPrimitive().isString();
    }
    
    public static boolean isNumber(final JsonElement json) {
        return json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber();
    }
    
    public static boolean isBoolean(final JsonObject json, final String memberName) {
        return isJsonPrimitive(json, memberName) && json.getAsJsonPrimitive(memberName).isBoolean();
    }
    
    public static boolean isJsonArray(final JsonObject json, final String memberName) {
        return hasField(json, memberName) && json.get(memberName).isJsonArray();
    }
    
    public static boolean isJsonPrimitive(final JsonObject json, final String memberName) {
        return hasField(json, memberName) && json.get(memberName).isJsonPrimitive();
    }
    
    public static boolean hasField(final JsonObject json, final String memberName) {
        return json != null && json.get(memberName) != null;
    }
    
    public static String getString(final JsonElement json, final String memberName) {
        if (json.isJsonPrimitive()) {
            return json.getAsString();
        }
        throw new JsonSyntaxException("Expected " + memberName + " to be a string, was " + toString(json));
    }
    
    public static String getString(final JsonObject json, final String memberName) {
        if (json.has(memberName)) {
            return getString(json.get(memberName), memberName);
        }
        throw new JsonSyntaxException("Missing " + memberName + ", expected to find a string");
    }
    
    public static String getString(final JsonObject json, final String memberName, final String fallback) {
        return json.has(memberName) ? getString(json.get(memberName), memberName) : fallback;
    }
    
    public static Item getItem(final JsonElement json, final String memberName) {
        if (!json.isJsonPrimitive()) {
            throw new JsonSyntaxException("Expected " + memberName + " to be an item, was " + toString(json));
        }
        final String s = json.getAsString();
        final Item item = Item.getByNameOrId(s);
        if (item == null) {
            throw new JsonSyntaxException("Expected " + memberName + " to be an item, was unknown string '" + s + "'");
        }
        return item;
    }
    
    public static Item getItem(final JsonObject json, final String memberName) {
        if (json.has(memberName)) {
            return getItem(json.get(memberName), memberName);
        }
        throw new JsonSyntaxException("Missing " + memberName + ", expected to find an item");
    }
    
    public static boolean getBoolean(final JsonElement json, final String memberName) {
        if (json.isJsonPrimitive()) {
            return json.getAsBoolean();
        }
        throw new JsonSyntaxException("Expected " + memberName + " to be a Boolean, was " + toString(json));
    }
    
    public static boolean getBoolean(final JsonObject json, final String memberName) {
        if (json.has(memberName)) {
            return getBoolean(json.get(memberName), memberName);
        }
        throw new JsonSyntaxException("Missing " + memberName + ", expected to find a Boolean");
    }
    
    public static boolean getBoolean(final JsonObject json, final String memberName, final boolean fallback) {
        return json.has(memberName) ? getBoolean(json.get(memberName), memberName) : fallback;
    }
    
    public static float getFloat(final JsonElement json, final String memberName) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            return json.getAsFloat();
        }
        throw new JsonSyntaxException("Expected " + memberName + " to be a Float, was " + toString(json));
    }
    
    public static float getFloat(final JsonObject json, final String memberName) {
        if (json.has(memberName)) {
            return getFloat(json.get(memberName), memberName);
        }
        throw new JsonSyntaxException("Missing " + memberName + ", expected to find a Float");
    }
    
    public static float getFloat(final JsonObject json, final String memberName, final float fallback) {
        return json.has(memberName) ? getFloat(json.get(memberName), memberName) : fallback;
    }
    
    public static int getInt(final JsonElement json, final String memberName) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            return json.getAsInt();
        }
        throw new JsonSyntaxException("Expected " + memberName + " to be a Int, was " + toString(json));
    }
    
    public static int getInt(final JsonObject json, final String memberName) {
        if (json.has(memberName)) {
            return getInt(json.get(memberName), memberName);
        }
        throw new JsonSyntaxException("Missing " + memberName + ", expected to find a Int");
    }
    
    public static int getInt(final JsonObject json, final String memberName, final int fallback) {
        return json.has(memberName) ? getInt(json.get(memberName), memberName) : fallback;
    }
    
    public static JsonObject getJsonObject(final JsonElement json, final String memberName) {
        if (json.isJsonObject()) {
            return json.getAsJsonObject();
        }
        throw new JsonSyntaxException("Expected " + memberName + " to be a JsonObject, was " + toString(json));
    }
    
    public static JsonObject getJsonObject(final JsonObject json, final String memberName) {
        if (json.has(memberName)) {
            return getJsonObject(json.get(memberName), memberName);
        }
        throw new JsonSyntaxException("Missing " + memberName + ", expected to find a JsonObject");
    }
    
    public static JsonObject getJsonObject(final JsonObject json, final String memberName, final JsonObject fallback) {
        return json.has(memberName) ? getJsonObject(json.get(memberName), memberName) : fallback;
    }
    
    public static JsonArray getJsonArray(final JsonElement json, final String memberName) {
        if (json.isJsonArray()) {
            return json.getAsJsonArray();
        }
        throw new JsonSyntaxException("Expected " + memberName + " to be a JsonArray, was " + toString(json));
    }
    
    public static JsonArray getJsonArray(final JsonObject json, final String memberName) {
        if (json.has(memberName)) {
            return getJsonArray(json.get(memberName), memberName);
        }
        throw new JsonSyntaxException("Missing " + memberName + ", expected to find a JsonArray");
    }
    
    public static JsonArray getJsonArray(final JsonObject json, final String memberName, @Nullable final JsonArray fallback) {
        return json.has(memberName) ? getJsonArray(json.get(memberName), memberName) : fallback;
    }
    
    public static <T> T deserializeClass(@Nullable final JsonElement json, final String memberName, final JsonDeserializationContext context, final Class<? extends T> adapter) {
        if (json != null) {
            return context.deserialize(json, adapter);
        }
        throw new JsonSyntaxException("Missing " + memberName);
    }
    
    public static <T> T deserializeClass(final JsonObject json, final String memberName, final JsonDeserializationContext context, final Class<? extends T> adapter) {
        if (json.has(memberName)) {
            return deserializeClass(json.get(memberName), memberName, context, adapter);
        }
        throw new JsonSyntaxException("Missing " + memberName);
    }
    
    public static <T> T deserializeClass(final JsonObject json, final String memberName, final T fallback, final JsonDeserializationContext context, final Class<? extends T> adapter) {
        return json.has(memberName) ? deserializeClass(json.get(memberName), memberName, context, adapter) : fallback;
    }
    
    public static String toString(final JsonElement json) {
        final String s = StringUtils.abbreviateMiddle(String.valueOf(json), "...", 10);
        if (json == null) {
            return "null (missing)";
        }
        if (json.isJsonNull()) {
            return "null (json)";
        }
        if (json.isJsonArray()) {
            return "an array (" + s + ")";
        }
        if (json.isJsonObject()) {
            return "an object (" + s + ")";
        }
        if (json.isJsonPrimitive()) {
            final JsonPrimitive jsonprimitive = json.getAsJsonPrimitive();
            if (jsonprimitive.isNumber()) {
                return "a number (" + s + ")";
            }
            if (jsonprimitive.isBoolean()) {
                return "a boolean (" + s + ")";
            }
        }
        return s;
    }
    
    @Nullable
    public static <T> T gsonDeserialize(final Gson gsonIn, final Reader readerIn, final Class<T> adapter, final boolean lenient) {
        try {
            final JsonReader jsonreader = new JsonReader(readerIn);
            jsonreader.setLenient(lenient);
            return gsonIn.getAdapter(adapter).read(jsonreader);
        }
        catch (final IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }
    
    @Nullable
    public static <T> T func_193838_a(final Gson p_193838_0_, final Reader p_193838_1_, final Type p_193838_2_, final boolean p_193838_3_) {
        try {
            final JsonReader jsonreader = new JsonReader(p_193838_1_);
            jsonreader.setLenient(p_193838_3_);
            return (T)p_193838_0_.getAdapter(TypeToken.get(p_193838_2_)).read(jsonreader);
        }
        catch (final IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }
    
    @Nullable
    public static <T> T func_193837_a(final Gson p_193837_0_, final String p_193837_1_, final Type p_193837_2_, final boolean p_193837_3_) {
        return func_193838_a(p_193837_0_, new StringReader(p_193837_1_), p_193837_2_, p_193837_3_);
    }
    
    @Nullable
    public static <T> T gsonDeserialize(final Gson gsonIn, final String json, final Class<T> adapter, final boolean lenient) {
        return gsonDeserialize(gsonIn, new StringReader(json), adapter, lenient);
    }
    
    @Nullable
    public static <T> T func_193841_a(final Gson p_193841_0_, final Reader p_193841_1_, final Type p_193841_2_) {
        return func_193838_a(p_193841_0_, p_193841_1_, p_193841_2_, false);
    }
    
    @Nullable
    public static <T> T func_193840_a(final Gson p_193840_0_, final String p_193840_1_, final Type p_193840_2_) {
        return func_193837_a(p_193840_0_, p_193840_1_, p_193840_2_, false);
    }
    
    @Nullable
    public static <T> T func_193839_a(final Gson p_193839_0_, final Reader p_193839_1_, final Class<T> p_193839_2_) {
        return gsonDeserialize(p_193839_0_, p_193839_1_, p_193839_2_, false);
    }
    
    @Nullable
    public static <T> T gsonDeserialize(final Gson gsonIn, final String json, final Class<T> adapter) {
        return gsonDeserialize(gsonIn, json, adapter, false);
    }
}
