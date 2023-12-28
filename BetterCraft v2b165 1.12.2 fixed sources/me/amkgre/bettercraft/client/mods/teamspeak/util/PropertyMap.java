// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.util;

import java.util.Map;

public class PropertyMap
{
    private Map<String, String> properties;
    
    public PropertyMap(final Map<String, String> properties) {
        this.properties = properties;
    }
    
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public boolean contains(final String key) {
        return this.properties.containsKey(key);
    }
    
    public String get(final String key) {
        return this.properties.get(key);
    }
    
    public String get(final String key, final String fallback) {
        return this.contains(key) ? this.properties.get(key) : fallback;
    }
    
    public boolean getBool(final String key) {
        return "1".equals(this.properties.get(key));
    }
    
    public boolean getBool(final String key, final boolean fallback) {
        return this.contains(key) ? "1".equals(this.properties.get(key)) : fallback;
    }
    
    public int getInt(final String key) {
        return this.getInt(key, 0);
    }
    
    public int getInt(final String key, final int fallback) {
        final String value = this.properties.get(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch (final NumberFormatException ex) {}
        }
        return fallback;
    }
    
    public long getLong(final String key) {
        return this.getLong(key, 0L);
    }
    
    public long getLong(final String key, final long fallback) {
        final String value = this.properties.get(key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            }
            catch (final NumberFormatException ex) {}
        }
        return fallback;
    }
    
    public float getFloat(final String key) {
        return this.getFloat(key, 0.0f);
    }
    
    public float getFloat(final String key, final float fallback) {
        final String value = this.properties.get(key);
        if (value != null) {
            try {
                return Float.parseFloat(value);
            }
            catch (final NumberFormatException ex) {}
        }
        return fallback;
    }
}
