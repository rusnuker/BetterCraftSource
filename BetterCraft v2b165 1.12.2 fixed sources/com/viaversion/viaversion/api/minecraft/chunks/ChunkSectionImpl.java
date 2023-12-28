// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;

public class ChunkSectionImpl implements ChunkSection
{
    private final IntList palette;
    private final Int2IntMap inversePalette;
    private final int[] blocks;
    private ChunkSectionLight light;
    private int nonAirBlocksCount;
    
    public ChunkSectionImpl(final boolean holdsLight) {
        this.blocks = new int[4096];
        this.palette = new IntArrayList();
        (this.inversePalette = new Int2IntOpenHashMap()).defaultReturnValue(-1);
        if (holdsLight) {
            this.light = new ChunkSectionLightImpl();
        }
    }
    
    public ChunkSectionImpl(final boolean holdsLight, final int expectedPaletteLength) {
        this.blocks = new int[4096];
        if (holdsLight) {
            this.light = new ChunkSectionLightImpl();
        }
        this.palette = new IntArrayList(expectedPaletteLength);
        (this.inversePalette = new Int2IntOpenHashMap(expectedPaletteLength)).defaultReturnValue(-1);
    }
    
    @Override
    public int getFlatBlock(final int idx) {
        final int index = this.blocks[idx];
        return this.palette.getInt(index);
    }
    
    @Override
    public void setFlatBlock(final int idx, final int id) {
        int index = this.inversePalette.get(id);
        if (index == -1) {
            index = this.palette.size();
            this.palette.add(id);
            this.inversePalette.put(id, index);
        }
        this.blocks[idx] = index;
    }
    
    @Override
    public int getPaletteIndex(final int idx) {
        return this.blocks[idx];
    }
    
    @Override
    public void setPaletteIndex(final int idx, final int index) {
        this.blocks[idx] = index;
    }
    
    @Override
    public int getPaletteSize() {
        return this.palette.size();
    }
    
    @Override
    public int getPaletteEntry(final int index) {
        return this.palette.getInt(index);
    }
    
    @Override
    public void setPaletteEntry(final int index, final int id) {
        final int oldId = this.palette.set(index, id);
        if (oldId == id) {
            return;
        }
        this.inversePalette.put(id, index);
        if (this.inversePalette.get(oldId) == index) {
            this.inversePalette.remove(oldId);
            for (int i = 0; i < this.palette.size(); ++i) {
                if (this.palette.getInt(i) == oldId) {
                    this.inversePalette.put(oldId, i);
                    break;
                }
            }
        }
    }
    
    @Override
    public void replacePaletteEntry(final int oldId, final int newId) {
        final int index = this.inversePalette.remove(oldId);
        if (index == -1) {
            return;
        }
        this.inversePalette.put(newId, index);
        for (int i = 0; i < this.palette.size(); ++i) {
            if (this.palette.getInt(i) == oldId) {
                this.palette.set(i, newId);
            }
        }
    }
    
    @Override
    public void addPaletteEntry(final int id) {
        this.inversePalette.put(id, this.palette.size());
        this.palette.add(id);
    }
    
    @Override
    public void clearPalette() {
        this.palette.clear();
        this.inversePalette.clear();
    }
    
    @Override
    public int getNonAirBlocksCount() {
        return this.nonAirBlocksCount;
    }
    
    @Override
    public void setNonAirBlocksCount(final int nonAirBlocksCount) {
        this.nonAirBlocksCount = nonAirBlocksCount;
    }
    
    @Override
    public ChunkSectionLight getLight() {
        return this.light;
    }
    
    @Override
    public void setLight(final ChunkSectionLight light) {
        this.light = light;
    }
}
