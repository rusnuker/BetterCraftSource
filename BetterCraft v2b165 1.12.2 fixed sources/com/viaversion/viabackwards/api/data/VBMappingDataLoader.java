// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.data;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import java.util.HashMap;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import java.util.Iterator;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.Map;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.viaversion.viaversion.libs.gson.JsonIOException;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import java.io.Reader;
import com.viaversion.viaversion.util.GsonUtil;
import java.io.FileReader;
import java.io.File;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.libs.gson.JsonObject;

public class VBMappingDataLoader
{
    public static JsonObject loadFromDataDir(final String name) {
        final File file = new File(ViaBackwards.getPlatform().getDataFolder(), name);
        if (!file.exists()) {
            return loadData(name);
        }
        try (final FileReader reader = new FileReader(file)) {
            return GsonUtil.getGson().fromJson(reader, JsonObject.class);
        }
        catch (final JsonSyntaxException e) {
            ViaBackwards.getPlatform().getLogger().warning(name + " is badly formatted!");
            e.printStackTrace();
            ViaBackwards.getPlatform().getLogger().warning("Falling back to resource's file!");
            return loadData(name);
        }
        catch (final IOException | JsonIOException e2) {
            e2.printStackTrace();
            return null;
        }
    }
    
    public static JsonObject loadData(final String name) {
        final InputStream stream = VBMappingDataLoader.class.getClassLoader().getResourceAsStream("assets/viabackwards/data/" + name);
        try (final InputStreamReader reader = new InputStreamReader(stream)) {
            return GsonUtil.getGson().fromJson(reader, JsonObject.class);
        }
        catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void mapIdentifiers(final int[] output, final JsonObject oldIdentifiers, final JsonObject newIdentifiers, final JsonObject diffIdentifiers) {
        mapIdentifiers(output, oldIdentifiers, newIdentifiers, diffIdentifiers, true);
    }
    
    public static void mapIdentifiers(final int[] output, final JsonObject oldIdentifiers, final JsonObject newIdentifiers, final JsonObject diffIdentifiers, final boolean warnOnMissing) {
        final Object2IntMap newIdentifierMap = MappingDataLoader.indexedObjectToMap(newIdentifiers);
        for (final Map.Entry<String, JsonElement> entry : oldIdentifiers.entrySet()) {
            final String key = entry.getValue().getAsString();
            int mappedId = newIdentifierMap.getInt(key);
            if (mappedId == -1) {
                if (diffIdentifiers != null) {
                    JsonPrimitive diffValueJson = diffIdentifiers.getAsJsonPrimitive(key);
                    String diffValue = (diffValueJson != null) ? diffValueJson.getAsString() : null;
                    final int dataIndex;
                    if (diffValue == null && (dataIndex = key.indexOf(91)) != -1 && (diffValueJson = diffIdentifiers.getAsJsonPrimitive(key.substring(0, dataIndex))) != null) {
                        diffValue = diffValueJson.getAsString();
                        if (diffValue.endsWith("[")) {
                            diffValue += key.substring(dataIndex + 1);
                        }
                    }
                    if (diffValue != null) {
                        mappedId = newIdentifierMap.getInt(diffValue);
                    }
                }
                if (mappedId == -1) {
                    if ((warnOnMissing && !Via.getConfig().isSuppressConversionWarnings()) || Via.getManager().isDebug()) {
                        ViaBackwards.getPlatform().getLogger().warning("No key for " + entry.getValue() + " :( ");
                        continue;
                    }
                    continue;
                }
            }
            output[Integer.parseInt(entry.getKey())] = mappedId;
        }
    }
    
    public static Map<String, String> objectToMap(final JsonObject object) {
        final Map<String, String> mappings = new HashMap<String, String>();
        for (final Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String key = entry.getKey();
            if (key.indexOf(58) == -1) {
                key = "minecraft:" + key;
            }
            String value = entry.getValue().getAsString();
            if (value.indexOf(58) == -1) {
                value = "minecraft:" + value;
            }
            mappings.put(key, value);
        }
        return mappings;
    }
    
    public static Int2ObjectMap<MappedItem> loadItemMappings(final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping) {
        return loadItemMappings(oldMapping, newMapping, diffMapping, false);
    }
    
    public static Int2ObjectMap<MappedItem> loadItemMappings(final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        final Int2ObjectMap<MappedItem> itemMapping = new Int2ObjectOpenHashMap<MappedItem>(diffMapping.size(), 1.0f);
        final Object2IntMap<String> newIdenfierMap = MappingDataLoader.indexedObjectToMap(newMapping);
        final Object2IntMap<String> oldIdenfierMap = MappingDataLoader.indexedObjectToMap(oldMapping);
        for (final Map.Entry<String, JsonElement> entry : diffMapping.entrySet()) {
            final JsonObject object = entry.getValue().getAsJsonObject();
            final String mappedIdName = object.getAsJsonPrimitive("id").getAsString();
            final int mappedId = newIdenfierMap.getInt(mappedIdName);
            if (mappedId == -1) {
                if (Via.getConfig().isSuppressConversionWarnings() && !Via.getManager().isDebug()) {
                    continue;
                }
                ViaBackwards.getPlatform().getLogger().warning("No key for " + mappedIdName + " :( ");
            }
            else {
                final int oldId = oldIdenfierMap.getInt(entry.getKey());
                if (oldId == -1) {
                    if (Via.getConfig().isSuppressConversionWarnings() && !Via.getManager().isDebug()) {
                        continue;
                    }
                    ViaBackwards.getPlatform().getLogger().warning("No old entry for " + mappedIdName + " :( ");
                }
                else {
                    final String name = object.getAsJsonPrimitive("name").getAsString();
                    itemMapping.put(oldId, new MappedItem(mappedId, name));
                }
            }
        }
        if (warnOnMissing && !Via.getConfig().isSuppressConversionWarnings()) {
            for (final Object2IntMap.Entry<String> entry2 : oldIdenfierMap.object2IntEntrySet()) {
                if (!newIdenfierMap.containsKey(entry2.getKey()) && !itemMapping.containsKey(entry2.getIntValue())) {
                    ViaBackwards.getPlatform().getLogger().warning("No item mapping for " + entry2.getKey() + " :( ");
                }
            }
        }
        return itemMapping;
    }
}
