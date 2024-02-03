// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

@Deprecated
public class Debug
{
    @Deprecated
    public static void log(final EnumDebugMode debugMode, final String message) {
        net.labymod.support.util.Debug.log(net.labymod.support.util.Debug.EnumDebugMode.ADDON, message);
    }
    
    @Deprecated
    public enum EnumDebugMode
    {
        @Deprecated
        ADDON("ADDON", 0);
        
        private EnumDebugMode(final String s, final int n) {
        }
    }
}
