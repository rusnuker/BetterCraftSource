// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.Iterator;
import java.util.Map;
import com.viaversion.viaversion.util.Key;
import java.util.LinkedHashMap;

public final class WrappedBlockData
{
    private final LinkedHashMap<String, String> blockData;
    private final String minecraftKey;
    private final int savedBlockStateId;
    
    public static WrappedBlockData fromString(final String s) {
        final String[] array = s.split("\\[");
        final String key = array[0];
        final WrappedBlockData wrappedBlockdata = new WrappedBlockData(key, ConnectionData.getId(s));
        if (array.length > 1) {
            String blockData = array[1];
            blockData = blockData.replace("]", "");
            final String[] split;
            final String[] data = split = blockData.split(",");
            for (final String d : split) {
                final String[] a = d.split("=");
                wrappedBlockdata.blockData.put(a[0], a[1]);
            }
        }
        return wrappedBlockdata;
    }
    
    private WrappedBlockData(final String minecraftKey, final int savedBlockStateId) {
        this.blockData = new LinkedHashMap<String, String>();
        this.minecraftKey = Key.namespaced(minecraftKey);
        this.savedBlockStateId = savedBlockStateId;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.minecraftKey + "[");
        for (final Map.Entry<String, String> entry : this.blockData.entrySet()) {
            sb.append(entry.getKey()).append('=').append(entry.getValue()).append(',');
        }
        return sb.substring(0, sb.length() - 1) + "]";
    }
    
    public String getMinecraftKey() {
        return this.minecraftKey;
    }
    
    public int getSavedBlockStateId() {
        return this.savedBlockStateId;
    }
    
    public int getBlockStateId() {
        return ConnectionData.getId(this.toString());
    }
    
    public WrappedBlockData set(final String data, final Object value) {
        if (!this.hasData(data)) {
            throw new UnsupportedOperationException("No blockdata found for " + data + " at " + this.minecraftKey);
        }
        this.blockData.put(data, value.toString());
        return this;
    }
    
    public String getValue(final String data) {
        return this.blockData.get(data);
    }
    
    public boolean hasData(final String key) {
        return this.blockData.containsKey(key);
    }
}
