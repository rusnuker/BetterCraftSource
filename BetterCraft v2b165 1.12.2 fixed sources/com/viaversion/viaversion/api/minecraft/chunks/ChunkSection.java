// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft.chunks;

public interface ChunkSection
{
    public static final int SIZE = 4096;
    
    default int index(final int x, final int y, final int z) {
        return y << 8 | z << 4 | x;
    }
    
    int getFlatBlock(final int p0);
    
    default int getFlatBlock(final int x, final int y, final int z) {
        return this.getFlatBlock(index(x, y, z));
    }
    
    void setFlatBlock(final int p0, final int p1);
    
    default void setFlatBlock(final int x, final int y, final int z, final int id) {
        this.setFlatBlock(index(x, y, z), id);
    }
    
    default int getBlockWithoutData(final int x, final int y, final int z) {
        return this.getFlatBlock(x, y, z) >> 4;
    }
    
    default int getBlockData(final int x, final int y, final int z) {
        return this.getFlatBlock(x, y, z) & 0xF;
    }
    
    default void setBlockWithData(final int x, final int y, final int z, final int type, final int data) {
        this.setFlatBlock(index(x, y, z), type << 4 | (data & 0xF));
    }
    
    default void setBlockWithData(final int idx, final int type, final int data) {
        this.setFlatBlock(idx, type << 4 | (data & 0xF));
    }
    
    void setPaletteIndex(final int p0, final int p1);
    
    int getPaletteIndex(final int p0);
    
    int getPaletteSize();
    
    int getPaletteEntry(final int p0);
    
    void setPaletteEntry(final int p0, final int p1);
    
    void replacePaletteEntry(final int p0, final int p1);
    
    void addPaletteEntry(final int p0);
    
    void clearPalette();
    
    int getNonAirBlocksCount();
    
    void setNonAirBlocksCount(final int p0);
    
    default boolean hasLight() {
        return this.getLight() != null;
    }
    
    ChunkSectionLight getLight();
    
    void setLight(final ChunkSectionLight p0);
}
