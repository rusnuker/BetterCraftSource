// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import me.amkgre.bettercraft.client.mods.teamspeak.util.EscapeUtil;

public class ValueParameter extends Parameter
{
    private String key;
    private String value;
    
    ValueParameter(final String key, final Object value) {
        this.key = key;
        this.value = convertValue(value);
    }
    
    private static String convertValue(final Object value) {
        if (value instanceof Boolean) {
            return value ? "1" : "0";
        }
        return String.valueOf(value);
    }
    
    public String getKey() {
        return this.key;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    @Override
    public String serialize() {
        return String.valueOf(this.getKey()) + "=" + EscapeUtil.escape(String.valueOf(this.getValue()));
    }
}
