// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.api.util.enums;

public enum EnumFeedType
{
    TRENDING("TRENDING", 0, "trending"), 
    RESOURCEPACK_OF_THE_WEEK("RESOURCEPACK_OF_THE_WEEK", 1, "resourcepack-of-the-week"), 
    PROMOTION("PROMOTION", 2, "promotion");
    
    private String key;
    
    public String getKey() {
        return this.key;
    }
    
    private EnumFeedType(final String s, final int n, final String key) {
        this.key = key;
    }
}
