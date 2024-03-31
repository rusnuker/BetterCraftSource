// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage;

import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import java.util.concurrent.ConcurrentHashMap;
import com.viaversion.viaversion.api.minecraft.Position;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.api.connection.StorableObject;

public class BlockStorage implements StorableObject
{
    private static final IntSet WHITELIST;
    private final Map<Position, ReplacementData> blocks;
    
    public BlockStorage() {
        this.blocks = new ConcurrentHashMap<Position, ReplacementData>();
    }
    
    public void store(final Position position, final int block) {
        this.store(position, block, -1);
    }
    
    public void store(final Position position, final int block, final int replacementId) {
        if (!BlockStorage.WHITELIST.contains(block)) {
            return;
        }
        this.blocks.put(position, new ReplacementData(block, replacementId));
    }
    
    public boolean isWelcome(final int block) {
        return BlockStorage.WHITELIST.contains(block);
    }
    
    public boolean contains(final Position position) {
        return this.blocks.containsKey(position);
    }
    
    public ReplacementData get(final Position position) {
        return this.blocks.get(position);
    }
    
    public ReplacementData remove(final Position position) {
        return this.blocks.remove(position);
    }
    
    static {
        (WHITELIST = new IntOpenHashSet(46, 1.0f)).add(5266);
        for (int i = 0; i < 16; ++i) {
            BlockStorage.WHITELIST.add(972 + i);
        }
        for (int i = 0; i < 20; ++i) {
            BlockStorage.WHITELIST.add(6854 + i);
        }
        for (int i = 0; i < 4; ++i) {
            BlockStorage.WHITELIST.add(7110 + i);
        }
        for (int i = 0; i < 5; ++i) {
            BlockStorage.WHITELIST.add(5447 + i);
        }
    }
    
    public static class ReplacementData
    {
        private int original;
        private int replacement;
        
        public ReplacementData(final int original, final int replacement) {
            this.original = original;
            this.replacement = replacement;
        }
        
        public int getOriginal() {
            return this.original;
        }
        
        public void setOriginal(final int original) {
            this.original = original;
        }
        
        public int getReplacement() {
            return this.replacement;
        }
        
        public void setReplacement(final int replacement) {
            this.replacement = replacement;
        }
    }
}
