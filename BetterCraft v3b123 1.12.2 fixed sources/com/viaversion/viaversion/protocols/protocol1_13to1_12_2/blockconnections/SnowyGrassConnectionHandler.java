// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Set;
import java.util.HashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;

public class SnowyGrassConnectionHandler extends ConnectionHandler
{
    private static final Object2IntMap<GrassBlock> GRASS_BLOCKS;
    private static final IntSet SNOWY_GRASS_BLOCKS;
    
    static ConnectionData.ConnectorInitAction init() {
        final Set<String> snowyGrassBlocks = new HashSet<String>();
        snowyGrassBlocks.add("minecraft:grass_block");
        snowyGrassBlocks.add("minecraft:podzol");
        snowyGrassBlocks.add("minecraft:mycelium");
        SnowyGrassConnectionHandler.GRASS_BLOCKS.defaultReturnValue(-1);
        final SnowyGrassConnectionHandler handler = new SnowyGrassConnectionHandler();
        return blockData -> {
            if (snowyGrassBlocks.contains(blockData.getMinecraftKey())) {
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), handler);
                blockData.set("snowy", "true");
                SnowyGrassConnectionHandler.GRASS_BLOCKS.put(new GrassBlock(blockData.getSavedBlockStateId(), true), blockData.getBlockStateId());
                blockData.set("snowy", "false");
                SnowyGrassConnectionHandler.GRASS_BLOCKS.put(new GrassBlock(blockData.getSavedBlockStateId(), false), blockData.getBlockStateId());
            }
            if (blockData.getMinecraftKey().equals("minecraft:snow") || blockData.getMinecraftKey().equals("minecraft:snow_block")) {
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), handler);
                SnowyGrassConnectionHandler.SNOWY_GRASS_BLOCKS.add(blockData.getSavedBlockStateId());
            }
        };
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final int blockUpId = this.getBlockData(user, position.getRelative(BlockFace.TOP));
        final int newId = SnowyGrassConnectionHandler.GRASS_BLOCKS.getInt(new GrassBlock(blockState, SnowyGrassConnectionHandler.SNOWY_GRASS_BLOCKS.contains(blockUpId)));
        return (newId != -1) ? newId : blockState;
    }
    
    static {
        GRASS_BLOCKS = new Object2IntOpenHashMap<GrassBlock>();
        SNOWY_GRASS_BLOCKS = new IntOpenHashSet();
    }
    
    private static final class GrassBlock
    {
        private final int blockStateId;
        private final boolean snowy;
        
        private GrassBlock(final int blockStateId, final boolean snowy) {
            this.blockStateId = blockStateId;
            this.snowy = snowy;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final GrassBlock that = (GrassBlock)o;
            return this.blockStateId == that.blockStateId && this.snowy == that.snowy;
        }
        
        @Override
        public int hashCode() {
            int result = this.blockStateId;
            result = 31 * result + (this.snowy ? 1 : 0);
            return result;
        }
    }
}
