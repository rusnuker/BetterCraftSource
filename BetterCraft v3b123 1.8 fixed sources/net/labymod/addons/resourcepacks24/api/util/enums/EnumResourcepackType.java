// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.api.util.enums;

public enum EnumResourcepackType
{
    NEW("NEW", 0, "new"), 
    RANDOM("RANDOM", 1, "random");
    
    private String key;
    
    public String getKey() {
        return this.key;
    }
    
    private EnumResourcepackType(final String s, final int n, final String key) {
        this.key = key;
    }
}
