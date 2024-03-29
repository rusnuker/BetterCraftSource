// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Set;
import java.util.HashSet;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public class FlowerConnectionHandler extends ConnectionHandler
{
    private static final Int2IntMap FLOWERS;
    
    static ConnectionData.ConnectorInitAction init() {
        final Set<String> baseFlower = new HashSet<String>();
        baseFlower.add("minecraft:rose_bush");
        baseFlower.add("minecraft:sunflower");
        baseFlower.add("minecraft:peony");
        baseFlower.add("minecraft:tall_grass");
        baseFlower.add("minecraft:large_fern");
        baseFlower.add("minecraft:lilac");
        final FlowerConnectionHandler handler = new FlowerConnectionHandler();
        return blockData -> {
            if (baseFlower.contains(blockData.getMinecraftKey())) {
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), handler);
                if (blockData.getValue("half").equals("lower")) {
                    blockData.set("half", "upper");
                    FlowerConnectionHandler.FLOWERS.put(blockData.getSavedBlockStateId(), blockData.getBlockStateId());
                }
            }
        };
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final int blockBelowId = this.getBlockData(user, position.getRelative(BlockFace.BOTTOM));
        final int connectBelow = FlowerConnectionHandler.FLOWERS.get(blockBelowId);
        if (connectBelow != 0) {
            final int blockAboveId = this.getBlockData(user, position.getRelative(BlockFace.TOP));
            if (Via.getConfig().isStemWhenBlockAbove()) {
                if (blockAboveId == 0) {
                    return connectBelow;
                }
            }
            else if (!FlowerConnectionHandler.FLOWERS.containsKey(blockAboveId)) {
                return connectBelow;
            }
        }
        return blockState;
    }
    
    static {
        FLOWERS = new Int2IntOpenHashMap();
    }
}
