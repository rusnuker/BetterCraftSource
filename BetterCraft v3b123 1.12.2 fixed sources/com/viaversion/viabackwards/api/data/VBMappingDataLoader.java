// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.data;

import com.viaversion.viaversion.libs.gson.JsonIOException;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import java.io.FileReader;
import java.io.Reader;
import java.io.InputStreamReader;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Map;
import java.io.File;
import com.viaversion.viabackwards.ViaBackwards;
import java.io.InputStream;
import java.io.IOException;
import com.viaversion.viaversion.libs.opennbt.NBTIO;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public final class VBMappingDataLoader
{
    public static CompoundTag loadNBT(final String name) {
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
    
    public static CompoundTag loadNBTFromDir(final String name) {
        final CompoundTag packedData = loadNBT(name);
        final File file = new File(ViaBackwards.getPlatform().getDataFolder(), name);
        if (!file.exists()) {
            return packedData;
        }
        ViaBackwards.getPlatform().getLogger().info("Loading " + name + " from plugin folder");
        try {
            final CompoundTag fileData = NBTIO.readFile(file, false, false);
            return mergeTags(packedData, fileData);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static CompoundTag mergeTags(final CompoundTag original, final CompoundTag extra) {
        for (final Map.Entry<String, Tag> entry : extra.entrySet()) {
            if (entry.getValue() instanceof CompoundTag) {
                final CompoundTag originalEntry = original.get(entry.getKey());
                if (originalEntry != null) {
                    mergeTags(originalEntry, entry.getValue());
                    continue;
                }
            }
            original.put(entry.getKey(), entry.getValue());
        }
        return original;
    }
    
    public static JsonObject loadData(final String name) {
        try (final InputStream stream = getResource(name)) {
            if (stream == null) {
                final JsonObject jsonObject = null;
                if (stream != null) {
                    stream.close();
                }
                return jsonObject;
            }
            return GsonUtil.getGson().fromJson(new InputStreamReader(stream), JsonObject.class);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
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
    
    public static InputStream getResource(final String name) {
        return VBMappingDataLoader.class.getClassLoader().getResourceAsStream("assets/viabackwards/data/" + name);
    }
}
