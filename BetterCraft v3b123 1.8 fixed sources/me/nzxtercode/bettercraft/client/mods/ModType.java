// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.mods;

public enum ModType
{
    RENDER("RENDER", 0, "Render"), 
    INGAME("INGAME", 1, "Ingame");
    
    private String name;
    
    private ModType(final String s, final int n, final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
