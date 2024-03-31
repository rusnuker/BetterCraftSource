// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import java.util.HashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import com.viaversion.viaversion.libs.gson.JsonArray;
import java.util.Iterator;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.google.common.annotations.Beta;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import java.util.Arrays;
import com.viaversion.viaversion.libs.opennbt.NBTIO;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.viaversion.viaversion.libs.gson.JsonIOException;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import java.io.Reader;
import com.viaversion.viaversion.util.GsonUtil;
import java.io.FileReader;
import java.io.File;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.Map;

public final class MappingDataLoader
{
    private static final byte DIRECT_ID = 0;
    private static final byte SHIFTS_ID = 1;
    private static final byte CHANGES_ID = 2;
    private static final byte IDENTITY_ID = 3;
    private static final Map<String, CompoundTag> MAPPINGS_CACHE;
    private static boolean cacheValid;
    
    @Deprecated
    public static void enableMappingsCache() {
    }
    
    public static void clearCache() {
        MappingDataLoader.MAPPINGS_CACHE.clear();
        MappingDataLoader.cacheValid = false;
    }
    
    public static JsonObject loadFromDataDir(final String name) {
        final File file = new File(Via.getPlatform().getDataFolder(), name);
        if (!file.exists()) {
            return loadData(name);
        }
        try (final FileReader reader = new FileReader(file)) {
            return GsonUtil.getGson().fromJson(reader, JsonObject.class);
        }
        catch (final JsonSyntaxException e) {
            Via.getPlatform().getLogger().warning(name + " is badly formatted!");
            throw new RuntimeException(e);
        }
        catch (final IOException | JsonIOException e2) {
            throw new RuntimeException(e2);
        }
    }
    
    public static JsonObject loadData(final String name) {
        final InputStream stream = getResource(name);
        if (stream == null) {
            return null;
        }
        try (final InputStreamReader reader = new InputStreamReader(stream)) {
            return GsonUtil.getGson().fromJson(reader, JsonObject.class);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static CompoundTag loadNBT(final String name, final boolean cache) {
        if (!MappingDataLoader.cacheValid) {
            return loadNBTFromFile(name);
        }
        CompoundTag data = MappingDataLoader.MAPPINGS_CACHE.get(name);
        if (data != null) {
            return data;
        }
        data = loadNBTFromFile(name);
        if (cache && data != null) {
            MappingDataLoader.MAPPINGS_CACHE.put(name, data);
        }
        return data;
    }
    
    public static CompoundTag loadNBT(final String name) {
        return loadNBT(name, false);
    }
    
    private static CompoundTag loadNBTFromFile(final String name) {
        final InputStream resource = getResource(name);
        if (resource == null) {
            return null;
        }
        try (final InputStream stream = resource) {
            return NBTIO.readTag(stream);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Mappings loadMappings(final CompoundTag mappingsTag, final String key) {
        return loadMappings(mappingsTag, key, size -> {
            final int[] array = new int[size];
            Arrays.fill(array, -1);
            return array;
        }, (array, id, mappedId) -> array[id] = mappedId, IntArrayMappings::of);
    }
    
    @Beta
    public static <M extends Mappings, V> Mappings loadMappings(final CompoundTag mappingsTag, final String key, final MappingHolderSupplier<V> holderSupplier, final AddConsumer<V> addConsumer, final MappingsSupplier<M, V> mappingsSupplier) {
        final CompoundTag tag = mappingsTag.get(key);
        if (tag == null) {
            return null;
        }
        final ByteTag serializationStragetyTag = tag.get("id");
        final IntTag mappedSizeTag = tag.get("mappedSize");
        final byte strategy = serializationStragetyTag.asByte();
        if (strategy == 0) {
            final IntArrayTag valuesTag = tag.get("val");
            return IntArrayMappings.of(valuesTag.getValue(), mappedSizeTag.asInt());
        }
        V mappings;
        if (strategy == 1) {
            final IntArrayTag shiftsAtTag = tag.get("at");
            final IntArrayTag shiftsTag = tag.get("to");
            final IntTag sizeTag = tag.get("size");
            final int[] shiftsAt = shiftsAtTag.getValue();
            final int[] shiftsTo = shiftsTag.getValue();
            final int size = sizeTag.asInt();
            mappings = holderSupplier.get(size);
            if (shiftsAt[0] != 0) {
                for (int to = shiftsAt[0], id = 0; id < to; ++id) {
                    addConsumer.addTo(mappings, id, id);
                }
            }
            for (int i = 0; i < shiftsAt.length; ++i) {
                final int from = shiftsAt[i];
                final int to2 = (i == shiftsAt.length - 1) ? size : shiftsAt[i + 1];
                int mappedId = shiftsTo[i];
                for (int id2 = from; id2 < to2; ++id2) {
                    addConsumer.addTo(mappings, id2, mappedId++);
                }
            }
        }
        else if (strategy == 2) {
            final IntArrayTag changesAtTag = tag.get("at");
            final IntArrayTag valuesTag2 = tag.get("val");
            final IntTag sizeTag = tag.get("size");
            final boolean fillBetween = tag.get("nofill") == null;
            final int[] changesAt = changesAtTag.getValue();
            final int[] values = valuesTag2.getValue();
            mappings = holderSupplier.get(sizeTag.asInt());
            for (int i = 0; i < changesAt.length; ++i) {
                final int id = changesAt[i];
                if (fillBetween) {
                    int identity;
                    for (int previousId = identity = ((i != 0) ? (changesAt[i - 1] + 1) : 0); identity < id; ++identity) {
                        addConsumer.addTo(mappings, identity, identity);
                    }
                }
                addConsumer.addTo(mappings, id, values[i]);
            }
        }
        else {
            if (strategy == 3) {
                final IntTag sizeTag2 = tag.get("size");
                return new IdentityMappings(sizeTag2.asInt(), mappedSizeTag.asInt());
            }
            throw new IllegalArgumentException("Unknown serialization strategy: " + strategy);
        }
        return mappingsSupplier.create(mappings, mappedSizeTag.asInt());
    }
    
    public static FullMappings loadFullMappings(final CompoundTag mappingsTag, final CompoundTag unmappedIdentifiers, final CompoundTag mappedIdentifiers, final String key) {
        final ListTag unmappedElements = unmappedIdentifiers.get(key);
        final ListTag mappedElements = mappedIdentifiers.get(key);
        if (unmappedElements == null || mappedElements == null) {
            return null;
        }
        Mappings mappings = loadMappings(mappingsTag, key);
        if (mappings == null) {
            mappings = new IdentityMappings(unmappedElements.size(), mappedElements.size());
        }
        return new FullMappingsBase((List<String>)unmappedElements.getValue().stream().map(t -> t.getValue()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), (List<String>)mappedElements.getValue().stream().map(t -> t.getValue()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), mappings);
    }
    
    @Deprecated
    public static void mapIdentifiers(final int[] output, final JsonObject unmappedIdentifiers, final JsonObject mappedIdentifiers, final JsonObject diffIdentifiers, final boolean warnOnMissing) {
        final Object2IntMap<String> newIdentifierMap = indexedObjectToMap(mappedIdentifiers);
        for (final Map.Entry<String, JsonElement> entry : unmappedIdentifiers.entrySet()) {
            final int id = Integer.parseInt(entry.getKey());
            final int mappedId = mapIdentifierEntry(id, entry.getValue().getAsString(), newIdentifierMap, diffIdentifiers, warnOnMissing);
            if (mappedId != -1) {
                output[id] = mappedId;
            }
        }
    }
    
    private static int mapIdentifierEntry(final int id, final String val, final Object2IntMap<String> mappedIdentifiers, final JsonObject diffIdentifiers, final boolean warnOnMissing) {
        int mappedId = mappedIdentifiers.getInt(val);
        if (mappedId == -1) {
            if (diffIdentifiers != null) {
                JsonElement diffElement = diffIdentifiers.get(val);
                if (diffElement != null || (diffElement = diffIdentifiers.get(Integer.toString(id))) != null) {
                    final String mappedName = diffElement.getAsString();
                    if (mappedName.isEmpty()) {
                        return -1;
                    }
                    mappedId = mappedIdentifiers.getInt(mappedName);
                }
            }
            if (mappedId == -1) {
                if ((warnOnMissing && !Via.getConfig().isSuppressConversionWarnings()) || Via.getManager().isDebug()) {
                    Via.getPlatform().getLogger().warning("No key for " + val + " :( ");
                }
                return -1;
            }
        }
        return mappedId;
    }
    
    @Deprecated
    public static void mapIdentifiers(final int[] output, final JsonArray unmappedIdentifiers, final JsonArray mappedIdentifiers, final JsonObject diffIdentifiers, final boolean warnOnMissing) {
        final Object2IntMap<String> newIdentifierMap = arrayToMap(mappedIdentifiers);
        for (int id = 0; id < unmappedIdentifiers.size(); ++id) {
            final JsonElement unmappedIdentifier = unmappedIdentifiers.get(id);
            final int mappedId = mapIdentifierEntry(id, unmappedIdentifier.getAsString(), newIdentifierMap, diffIdentifiers, warnOnMissing);
            if (mappedId != -1) {
                output[id] = mappedId;
            }
        }
    }
    
    public static Object2IntMap<String> indexedObjectToMap(final JsonObject object) {
        final Object2IntMap<String> map = new Object2IntOpenHashMap<String>(object.size(), 0.99f);
        map.defaultReturnValue(-1);
        for (final Map.Entry<String, JsonElement> entry : object.entrySet()) {
            map.put(entry.getValue().getAsString(), Integer.parseInt(entry.getKey()));
        }
        return map;
    }
    
    public static Object2IntMap<String> arrayToMap(final JsonArray array) {
        final Object2IntMap<String> map = new Object2IntOpenHashMap<String>(array.size(), 0.99f);
        map.defaultReturnValue(-1);
        for (int i = 0; i < array.size(); ++i) {
            map.put(array.get(i).getAsString(), i);
        }
        return map;
    }
    
    public static InputStream getResource(final String name) {
        return MappingDataLoader.class.getClassLoader().getResourceAsStream("assets/viaversion/data/" + name);
    }
    
    static {
        MAPPINGS_CACHE = new HashMap<String, CompoundTag>();
        MappingDataLoader.cacheValid = true;
    }
    
    @FunctionalInterface
    public interface MappingsSupplier<T extends Mappings, V>
    {
        T create(final V p0, final int p1);
    }
    
    @FunctionalInterface
    public interface MappingHolderSupplier<T>
    {
        T get(final int p0);
    }
    
    @FunctionalInterface
    public interface AddConsumer<T>
    {
        void addTo(final T p0, final int p1, final int p2);
    }
}
