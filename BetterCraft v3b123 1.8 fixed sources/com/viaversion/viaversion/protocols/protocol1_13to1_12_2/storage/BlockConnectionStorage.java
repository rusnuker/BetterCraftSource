// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage;

import com.viaversion.viaversion.api.Via;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.lang.reflect.Constructor;
import com.viaversion.viaversion.api.connection.StorableObject;

public class BlockConnectionStorage implements StorableObject
{
    private static Constructor<?> fastUtilLongObjectHashMap;
    private final Map<Long, SectionData> blockStorage;
    private Long lastIndex;
    private SectionData lastSection;
    
    public BlockConnectionStorage() {
        this.blockStorage = this.createLongObjectMap();
    }
    
    public static void init() {
    }
    
    public void store(final int x, final int y, final int z, final int blockState) {
        final long index = getChunkSectionIndex(x, y, z);
        SectionData section = this.getSection(index);
        if (section == null) {
            if (blockState == 0) {
                return;
            }
            this.blockStorage.put(index, section = new SectionData());
            this.lastSection = section;
            this.lastIndex = index;
        }
        section.setBlockAt(x, y, z, blockState);
    }
    
    public int get(final int x, final int y, final int z) {
        final long pair = getChunkSectionIndex(x, y, z);
        final SectionData section = this.getSection(pair);
        if (section == null) {
            return 0;
        }
        return section.blockAt(x, y, z);
    }
    
    public void remove(final int x, final int y, final int z) {
        final long index = getChunkSectionIndex(x, y, z);
        final SectionData section = this.getSection(index);
        if (section == null) {
            return;
        }
        section.setBlockAt(x, y, z, 0);
        if (section.nonEmptyBlocks() == 0) {
            this.removeSection(index);
        }
    }
    
    public void clear() {
        this.blockStorage.clear();
        this.lastSection = null;
        this.lastIndex = null;
    }
    
    public void unloadChunk(final int x, final int z) {
        for (int y = 0; y < 16; ++y) {
            this.unloadSection(x, y, z);
        }
    }
    
    public void unloadSection(final int x, final int y, final int z) {
        this.removeSection(getChunkSectionIndex(x << 4, y << 4, z << 4));
    }
    
    private SectionData getSection(final long index) {
        if (this.lastIndex != null && this.lastIndex == index) {
            return this.lastSection;
        }
        this.lastIndex = index;
        return this.lastSection = this.blockStorage.get(index);
    }
    
    private void removeSection(final long index) {
        this.blockStorage.remove(index);
        if (this.lastIndex != null && this.lastIndex == index) {
            this.lastIndex = null;
            this.lastSection = null;
        }
    }
    
    private static long getChunkSectionIndex(final int x, final int y, final int z) {
        return ((long)(x >> 4) & 0x3FFFFFFL) << 38 | ((long)(y >> 4) & 0xFFFL) << 26 | ((long)(z >> 4) & 0x3FFFFFFL);
    }
    
    private <T> Map<Long, T> createLongObjectMap() {
        if (BlockConnectionStorage.fastUtilLongObjectHashMap != null) {
            try {
                return (Map)BlockConnectionStorage.fastUtilLongObjectHashMap.newInstance(new Object[0]);
            }
            catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<Long, T>();
    }
    
    static {
        try {
            final String className = "it" + ".unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap";
            BlockConnectionStorage.fastUtilLongObjectHashMap = Class.forName(className).getConstructor((Class<?>[])new Class[0]);
            Via.getPlatform().getLogger().info("Using FastUtil Long2ObjectOpenHashMap for block connections");
        }
        catch (final ClassNotFoundException | NoSuchMethodException ex) {}
    }
    
    private static final class SectionData
    {
        private final short[] blockStates;
        private short nonEmptyBlocks;
        
        private SectionData() {
            this.blockStates = new short[4096];
        }
        
        public int blockAt(final int x, final int y, final int z) {
            return this.blockStates[encodeBlockPos(x, y, z)];
        }
        
        public void setBlockAt(final int x, final int y, final int z, final int blockState) {
            final int index = encodeBlockPos(x, y, z);
            if (blockState == this.blockStates[index]) {
                return;
            }
            this.blockStates[index] = (short)blockState;
            if (blockState == 0) {
                --this.nonEmptyBlocks;
            }
            else {
                ++this.nonEmptyBlocks;
            }
        }
        
        public short nonEmptyBlocks() {
            return this.nonEmptyBlocks;
        }
        
        private static int encodeBlockPos(final int x, final int y, final int z) {
            return (y & 0xF) << 8 | (x & 0xF) << 4 | (z & 0xF);
        }
    }
}
