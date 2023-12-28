// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import com.viaversion.viaversion.libs.gson.JsonArray;
import java.util.Iterator;
import java.io.IOException;
import com.google.common.io.CharStreams;
import java.nio.charset.StandardCharsets;
import java.io.Reader;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.IntArrayMappings;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import com.viaversion.viaversion.api.data.Mappings;
import com.google.common.collect.BiMap;
import java.util.Map;
import com.viaversion.viaversion.api.data.MappingDataBase;

public class MappingData extends MappingDataBase
{
    private final Map<String, Integer[]> blockTags;
    private final Map<String, Integer[]> itemTags;
    private final Map<String, Integer[]> fluidTags;
    private final BiMap<Short, String> oldEnchantmentsIds;
    private final Map<String, String> translateMapping;
    private final Map<String, String> mojangTranslation;
    private final BiMap<String, String> channelMappings;
    private Mappings enchantmentMappings;
    
    public MappingData() {
        super("1.12", "1.13");
        this.blockTags = new HashMap<String, Integer[]>();
        this.itemTags = new HashMap<String, Integer[]>();
        this.fluidTags = new HashMap<String, Integer[]>();
        this.oldEnchantmentsIds = (BiMap<Short, String>)HashBiMap.create();
        this.translateMapping = new HashMap<String, String>();
        this.mojangTranslation = new HashMap<String, String>();
        this.channelMappings = (BiMap<String, String>)HashBiMap.create();
    }
    
    public void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        this.loadTags(this.blockTags, newMappings.getAsJsonObject("block_tags"));
        this.loadTags(this.itemTags, newMappings.getAsJsonObject("item_tags"));
        this.loadTags(this.fluidTags, newMappings.getAsJsonObject("fluid_tags"));
        this.loadEnchantments(this.oldEnchantmentsIds, oldMappings.getAsJsonObject("enchantments"));
        this.enchantmentMappings = new IntArrayMappings(72, oldMappings.getAsJsonObject("enchantments"), newMappings.getAsJsonObject("enchantments"));
        if (Via.getConfig().isSnowCollisionFix()) {
            this.blockMappings.setNewId(1248, 3416);
        }
        if (Via.getConfig().isInfestedBlocksFix()) {
            this.blockMappings.setNewId(1552, 1);
            this.blockMappings.setNewId(1553, 14);
            this.blockMappings.setNewId(1554, 3983);
            this.blockMappings.setNewId(1555, 3984);
            this.blockMappings.setNewId(1556, 3985);
            this.blockMappings.setNewId(1557, 3986);
        }
        final JsonObject object = MappingDataLoader.loadFromDataDir("channelmappings-1.13.json");
        if (object != null) {
            for (final Map.Entry<String, JsonElement> entry : object.entrySet()) {
                final String oldChannel = entry.getKey();
                final String newChannel = entry.getValue().getAsString();
                if (!isValid1_13Channel(newChannel)) {
                    Via.getPlatform().getLogger().warning("Channel '" + newChannel + "' is not a valid 1.13 plugin channel, please check your configuration!");
                }
                else {
                    this.channelMappings.put(oldChannel, newChannel);
                }
            }
        }
        final Map<String, String> translateData = GsonUtil.getGson().fromJson(new InputStreamReader(MappingData.class.getClassLoader().getResourceAsStream("assets/viaversion/data/mapping-lang-1.12-1.13.json")), new TypeToken<Map<String, String>>() {}.getType());
        try {
            String[] lines;
            try (final Reader reader = new InputStreamReader(MappingData.class.getClassLoader().getResourceAsStream("assets/viaversion/data/en_US.properties"), StandardCharsets.UTF_8)) {
                lines = CharStreams.toString(reader).split("\n");
            }
            for (final String line : lines) {
                if (!line.isEmpty()) {
                    final String[] keyAndTranslation = line.split("=", 2);
                    if (keyAndTranslation.length == 2) {
                        final String key = keyAndTranslation[0];
                        if (!translateData.containsKey(key)) {
                            final String translation = keyAndTranslation[1].replaceAll("%(\\d\\$)?d", "%$1s");
                            this.mojangTranslation.put(key, translation);
                        }
                        else {
                            final String dataValue = translateData.get(key);
                            if (dataValue != null) {
                                this.translateMapping.put(key, dataValue);
                            }
                        }
                    }
                }
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings, final String key) {
        if (key.equals("blocks")) {
            return new IntArrayMappings(4084, oldMappings.getAsJsonObject("blocks"), newMappings.getAsJsonObject("blockstates"));
        }
        return super.loadFromObject(oldMappings, newMappings, diffMappings, key);
    }
    
    public static String validateNewChannel(String newId) {
        if (!isValid1_13Channel(newId)) {
            return null;
        }
        final int separatorIndex = newId.indexOf(58);
        if ((separatorIndex == -1 || separatorIndex == 0) && newId.length() <= 10) {
            newId = "minecraft:" + newId;
        }
        return newId;
    }
    
    public static boolean isValid1_13Channel(final String channelId) {
        return channelId.matches("([0-9a-z_.-]+):([0-9a-z_/.-]+)");
    }
    
    private void loadTags(final Map<String, Integer[]> output, final JsonObject newTags) {
        for (final Map.Entry<String, JsonElement> entry : newTags.entrySet()) {
            final JsonArray ids = entry.getValue().getAsJsonArray();
            final Integer[] idsArray = new Integer[ids.size()];
            for (int i = 0; i < ids.size(); ++i) {
                idsArray[i] = ids.get(i).getAsInt();
            }
            output.put(entry.getKey(), idsArray);
        }
    }
    
    private void loadEnchantments(final Map<Short, String> output, final JsonObject enchantments) {
        for (final Map.Entry<String, JsonElement> enchantment : enchantments.entrySet()) {
            output.put(Short.parseShort(enchantment.getKey()), enchantment.getValue().getAsString());
        }
    }
    
    public Map<String, Integer[]> getBlockTags() {
        return this.blockTags;
    }
    
    public Map<String, Integer[]> getItemTags() {
        return this.itemTags;
    }
    
    public Map<String, Integer[]> getFluidTags() {
        return this.fluidTags;
    }
    
    public BiMap<Short, String> getOldEnchantmentsIds() {
        return this.oldEnchantmentsIds;
    }
    
    public Map<String, String> getTranslateMapping() {
        return this.translateMapping;
    }
    
    public Map<String, String> getMojangTranslation() {
        return this.mojangTranslation;
    }
    
    public BiMap<String, String> getChannelMappings() {
        return this.channelMappings;
    }
    
    public Mappings getEnchantmentMappings() {
        return this.enchantmentMappings;
    }
}
