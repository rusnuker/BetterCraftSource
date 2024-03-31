// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.hud;

public enum Region
{
    TOP_LEFT("TOP_LEFT", 0), 
    TOP_CENTER("TOP_CENTER", 1), 
    TOP_RIGHT("TOP_RIGHT", 2), 
    MIDDLE_LEFT("MIDDLE_LEFT", 3), 
    MIDDLE_CENTER("MIDDLE_CENTER", 4), 
    MIDDLE_RIGHT("MIDDLE_RIGHT", 5), 
    BOTTOM_LEFT("BOTTOM_LEFT", 6), 
    BOTTOM_CENTER("BOTTOM_CENTER", 7), 
    BOTTOM_RIGHT("BOTTOM_RIGHT", 8);
    
    int x;
    int y;
    int widht;
    int height;
    
    private Region(final String s, final int n) {
    }
}
