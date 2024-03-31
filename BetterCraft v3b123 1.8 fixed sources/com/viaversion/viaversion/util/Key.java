// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

public final class Key
{
    public static String stripNamespace(final String identifier) {
        final int index = identifier.indexOf(58);
        if (index == -1) {
            return identifier;
        }
        return identifier.substring(index + 1);
    }
    
    public static String stripMinecraftNamespace(final String identifier) {
        if (identifier.startsWith("minecraft:")) {
            return identifier.substring(10);
        }
        return identifier;
    }
    
    public static String namespaced(final String identifier) {
        if (identifier.indexOf(58) == -1) {
            return "minecraft:" + identifier;
        }
        return identifier;
    }
}
