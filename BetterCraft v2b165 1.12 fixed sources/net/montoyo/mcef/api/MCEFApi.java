// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.api;

public class MCEFApi
{
    public static API getAPI() {
        try {
            final Class cls = Class.forName("net.montoyo.mcef.MCEF");
            return (API)cls.getField("PROXY_CLIENT").get(null);
        }
        catch (final Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    
    public static boolean isMCEFLoaded() {
        return true;
    }
}
