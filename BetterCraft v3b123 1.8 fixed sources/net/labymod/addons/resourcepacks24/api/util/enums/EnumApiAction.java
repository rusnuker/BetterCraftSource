// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.api.util.enums;

public enum EnumApiAction
{
    SEARCH("SEARCH", 0, "search"), 
    DOWNLOAD("DOWNLOAD", 1, "download"), 
    FEED("FEED", 2, "feed"), 
    RESOURCEPACKS("RESOURCEPACKS", 3, "resourcepacks"), 
    RESOURCEPACK("RESOURCEPACK", 4, "resourcepack"), 
    CATEGORY("CATEGORY", 5, "category");
    
    private String key;
    
    public String getKey() {
        return this.key;
    }
    
    private EnumApiAction(final String s, final int n, final String key) {
        this.key = key;
    }
}
