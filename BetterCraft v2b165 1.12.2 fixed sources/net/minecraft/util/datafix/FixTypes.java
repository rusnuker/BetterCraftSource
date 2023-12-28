// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.datafix;

public enum FixTypes implements IFixType
{
    LEVEL("LEVEL", 0), 
    PLAYER("PLAYER", 1), 
    CHUNK("CHUNK", 2), 
    BLOCK_ENTITY("BLOCK_ENTITY", 3), 
    ENTITY("ENTITY", 4), 
    ITEM_INSTANCE("ITEM_INSTANCE", 5), 
    OPTIONS("OPTIONS", 6), 
    STRUCTURE("STRUCTURE", 7);
    
    private FixTypes(final String s, final int n) {
    }
}
