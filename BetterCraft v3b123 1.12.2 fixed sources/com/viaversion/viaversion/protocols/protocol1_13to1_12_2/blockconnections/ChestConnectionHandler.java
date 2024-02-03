// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Locale;
import java.util.Arrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

class ChestConnectionHandler extends ConnectionHandler
{
    private static final Int2ObjectMap<BlockFace> CHEST_FACINGS;
    private static final int[] CONNECTED_STATES;
    private static final IntSet TRAPPED_CHESTS;
    
    static ConnectionData.ConnectorInitAction init() {
        Arrays.fill(ChestConnectionHandler.CONNECTED_STATES, -1);
        final ChestConnectionHandler connectionHandler = new ChestConnectionHandler();
        return blockData -> {
            if (blockData.getMinecraftKey().equals("minecraft:chest") || blockData.getMinecraftKey().equals("minecraft:trapped_chest")) {
                if (!blockData.getValue("waterlogged").equals("true")) {
                    ChestConnectionHandler.CHEST_FACINGS.put(blockData.getSavedBlockStateId(), BlockFace.valueOf(blockData.getValue("facing").toUpperCase(Locale.ROOT)));
                    if (blockData.getMinecraftKey().equalsIgnoreCase("minecraft:trapped_chest")) {
                        ChestConnectionHandler.TRAPPED_CHESTS.add(blockData.getSavedBlockStateId());
                    }
                    ChestConnectionHandler.CONNECTED_STATES[getStates(blockData)] = blockData.getSavedBlockStateId();
                    ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), connectionHandler);
                }
            }
        };
    }
    
    private static Byte getStates(final WrappedBlockData blockData) {
        byte states = 0;
        final String type = blockData.getValue("type");
        if (type.equals("left")) {
            states |= 0x1;
        }
        if (type.equals("right")) {
            states |= 0x2;
        }
        states |= (byte)(BlockFace.valueOf(blockData.getValue("facing").toUpperCase(Locale.ROOT)).ordinal() << 2);
        if (blockData.getMinecraftKey().equals("minecraft:trapped_chest")) {
            states |= 0x10;
        }
        return states;
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final BlockFace facing = ChestConnectionHandler.CHEST_FACINGS.get(blockState);
        byte states = 0;
        states |= (byte)(facing.ordinal() << 2);
        final boolean trapped = ChestConnectionHandler.TRAPPED_CHESTS.contains(blockState);
        if (trapped) {
            states |= 0x10;
        }
        int relative;
        if (ChestConnectionHandler.CHEST_FACINGS.containsKey(relative = this.getBlockData(user, position.getRelative(BlockFace.NORTH))) && trapped == ChestConnectionHandler.TRAPPED_CHESTS.contains(relative)) {
            states |= (byte)((facing == BlockFace.WEST) ? 1 : 2);
        }
        else if (ChestConnectionHandler.CHEST_FACINGS.containsKey(relative = this.getBlockData(user, position.getRelative(BlockFace.SOUTH))) && trapped == ChestConnectionHandler.TRAPPED_CHESTS.contains(relative)) {
            states |= (byte)((facing == BlockFace.EAST) ? 1 : 2);
        }
        else if (ChestConnectionHandler.CHEST_FACINGS.containsKey(relative = this.getBlockData(user, position.getRelative(BlockFace.WEST))) && trapped == ChestConnectionHandler.TRAPPED_CHESTS.contains(relative)) {
            states |= (byte)((facing == BlockFace.NORTH) ? 2 : 1);
        }
        else if (ChestConnectionHandler.CHEST_FACINGS.containsKey(relative = this.getBlockData(user, position.getRelative(BlockFace.EAST))) && trapped == ChestConnectionHandler.TRAPPED_CHESTS.contains(relative)) {
            states |= (byte)((facing == BlockFace.SOUTH) ? 2 : 1);
        }
        final int newBlockState = ChestConnectionHandler.CONNECTED_STATES[states];
        return (newBlockState == -1) ? blockState : newBlockState;
    }
    
    static {
        CHEST_FACINGS = new Int2ObjectOpenHashMap<BlockFace>();
        CONNECTED_STATES = new int[32];
        TRAPPED_CHESTS = new IntOpenHashSet();
    }
}
