// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.inventory;

public enum ClickType
{
    PICKUP("PICKUP", 0), 
    QUICK_MOVE("QUICK_MOVE", 1), 
    SWAP("SWAP", 2), 
    CLONE("CLONE", 3), 
    THROW("THROW", 4), 
    QUICK_CRAFT("QUICK_CRAFT", 5), 
    PICKUP_ALL("PICKUP_ALL", 6);
    
    private ClickType(final String s, final int n) {
    }
}
