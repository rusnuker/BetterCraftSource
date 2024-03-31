// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage.loot;

import java.net.URL;
import com.google.common.io.Resources;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import java.io.IOException;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import net.minecraft.util.ResourceLocation;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class LootTableManager
{
    private static final Logger LOGGER;
    private static final Gson GSON_INSTANCE;
    private final LoadingCache<ResourceLocation, LootTable> registeredLootTables;
    private final File baseFolder;
    
    static {
        LOGGER = LogManager.getLogger();
        GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer()).registerTypeAdapter(LootPool.class, new LootPool.Serializer()).registerTypeAdapter(LootTable.class, new LootTable.Serializer()).registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer()).registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer()).registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.Serializer()).registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer()).create();
    }
    
    public LootTableManager(@Nullable final File folder) {
        this.registeredLootTables = CacheBuilder.newBuilder().build((CacheLoader<? super ResourceLocation, LootTable>)new Loader((Loader)null));
        this.baseFolder = folder;
        this.reloadLootTables();
    }
    
    public LootTable getLootTableFromLocation(final ResourceLocation ressources) {
        return this.registeredLootTables.getUnchecked(ressources);
    }
    
    public void reloadLootTables() {
        this.registeredLootTables.invalidateAll();
        for (final ResourceLocation resourcelocation : LootTableList.getAll()) {
            this.getLootTableFromLocation(resourcelocation);
        }
    }
    
    class Loader extends CacheLoader<ResourceLocation, LootTable>
    {
        private Loader() {
        }
        
        @Override
        public LootTable load(final ResourceLocation p_load_1_) throws Exception {
            if (p_load_1_.getResourcePath().contains(".")) {
                LootTableManager.LOGGER.debug("Invalid loot table name '{}' (can't contain periods)", p_load_1_);
                return LootTable.EMPTY_LOOT_TABLE;
            }
            LootTable loottable = this.loadLootTable(p_load_1_);
            if (loottable == null) {
                loottable = this.loadBuiltinLootTable(p_load_1_);
            }
            if (loottable == null) {
                loottable = LootTable.EMPTY_LOOT_TABLE;
                LootTableManager.LOGGER.warn("Couldn't find resource table {}", p_load_1_);
            }
            return loottable;
        }
        
        @Nullable
        private LootTable loadLootTable(final ResourceLocation resource) {
            if (LootTableManager.this.baseFolder == null) {
                return null;
            }
            final File file1 = new File(new File(LootTableManager.this.baseFolder, resource.getResourceDomain()), String.valueOf(resource.getResourcePath()) + ".json");
            if (file1.exists()) {
                if (file1.isFile()) {
                    String s;
                    try {
                        s = Files.toString(file1, StandardCharsets.UTF_8);
                    }
                    catch (final IOException ioexception) {
                        LootTableManager.LOGGER.warn("Couldn't load loot table {} from {}", resource, file1, ioexception);
                        return LootTable.EMPTY_LOOT_TABLE;
                    }
                    try {
                        return JsonUtils.gsonDeserialize(LootTableManager.GSON_INSTANCE, s, LootTable.class);
                    }
                    catch (final IllegalArgumentException | JsonParseException jsonparseexception) {
                        LootTableManager.LOGGER.error("Couldn't load loot table {} from {}", resource, file1, jsonparseexception);
                        return LootTable.EMPTY_LOOT_TABLE;
                    }
                }
                LootTableManager.LOGGER.warn("Expected to find loot table {} at {} but it was a folder.", resource, file1);
                return LootTable.EMPTY_LOOT_TABLE;
            }
            return null;
        }
        
        @Nullable
        private LootTable loadBuiltinLootTable(final ResourceLocation resource) {
            final URL url = LootTableManager.class.getResource("/assets/" + resource.getResourceDomain() + "/loot_tables/" + resource.getResourcePath() + ".json");
            if (url != null) {
                String s;
                try {
                    s = Resources.toString(url, StandardCharsets.UTF_8);
                }
                catch (final IOException ioexception) {
                    LootTableManager.LOGGER.warn("Couldn't load loot table {} from {}", resource, url, ioexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }
                try {
                    return JsonUtils.gsonDeserialize(LootTableManager.GSON_INSTANCE, s, LootTable.class);
                }
                catch (final JsonParseException jsonparseexception) {
                    LootTableManager.LOGGER.error("Couldn't load loot table {} from {}", resource, url, jsonparseexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }
            }
            return null;
        }
    }
}
