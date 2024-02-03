// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.mojang.inventory.scale;

public enum EnumGuiScale
{
    DEFAULT("DEFAULT", 0, "Default"), 
    SMALL("SMALL", 1, "Small"), 
    NORMAL("NORMAL", 2, "Normal"), 
    LARGE("LARGE", 3, "Large"), 
    AUTO("AUTO", 4, "Auto");
    
    private String displayName;
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    private EnumGuiScale(final String s, final int n, final String displayName) {
        this.displayName = displayName;
    }
}
