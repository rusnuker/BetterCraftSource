// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.util;

public class EscapeUtil
{
    public static String escape(final String string) {
        return string.replace("\\", "\\\\").replace("/", "\\/").replace(" ", "\\s").replace("|", "\\p");
    }
    
    public static String unescape(final String string) {
        return string.replace("\\\\", "\\").replace("\\/", "/").replace("\\s", " ").replace("\\p", "|");
    }
}
