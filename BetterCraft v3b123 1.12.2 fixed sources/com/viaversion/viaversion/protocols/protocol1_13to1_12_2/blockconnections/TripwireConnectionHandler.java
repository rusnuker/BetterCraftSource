// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Locale;
import java.util.Arrays;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class TripwireConnectionHandler extends ConnectionHandler
{
    private static final Int2ObjectMap<TripwireData> TRIPWIRE_DATA_MAP;
    private static final Int2ObjectMap<BlockFace> TRIPWIRE_HOOKS;
    private static final int[] CONNECTED_BLOCKS;
    
    static ConnectionData.ConnectorInitAction init() {
        Arrays.fill(TripwireConnectionHandler.CONNECTED_BLOCKS, -1);
        final TripwireConnectionHandler connectionHandler = new TripwireConnectionHandler();
        return blockData -> {
            if (blockData.getMinecraftKey().equals("minecraft:tripwire_hook")) {
                TripwireConnectionHandler.TRIPWIRE_HOOKS.put(blockData.getSavedBlockStateId(), BlockFace.valueOf(blockData.getValue("facing").toUpperCase(Locale.ROOT)));
            }
            else if (blockData.getMinecraftKey().equals("minecraft:tripwire")) {
                final TripwireData tripwireData = new TripwireData(blockData.getValue("attached").equals("true"), blockData.getValue("disarmed").equals("true"), blockData.getValue("powered").equals("true"));
                TripwireConnectionHandler.TRIPWIRE_DATA_MAP.put(blockData.getSavedBlockStateId(), tripwireData);
                TripwireConnectionHandler.CONNECTED_BLOCKS[getStates(blockData)] = blockData.getSavedBlockStateId();
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), connectionHandler);
            }
        };
    }
    
    private static byte getStates(final WrappedBlockData blockData) {
        byte b = 0;
        if (blockData.getValue("attached").equals("true")) {
            b |= 0x1;
        }
        if (blockData.getValue("disarmed").equals("true")) {
            b |= 0x2;
        }
        if (blockData.getValue("powered").equals("true")) {
            b |= 0x4;
        }
        if (blockData.getValue("east").equals("true")) {
            b |= 0x8;
        }
        if (blockData.getValue("north").equals("true")) {
            b |= 0x10;
        }
        if (blockData.getValue("south").equals("true")) {
            b |= 0x20;
        }
        if (blockData.getValue("west").equals("true")) {
            b |= 0x40;
        }
        return b;
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final TripwireData tripwireData = TripwireConnectionHandler.TRIPWIRE_DATA_MAP.get(blockState);
        if (tripwireData == null) {
            return blockState;
        }
        byte b = 0;
        if (tripwireData.isAttached()) {
            b |= 0x1;
        }
        if (tripwireData.isDisarmed()) {
            b |= 0x2;
        }
        if (tripwireData.isPowered()) {
            b |= 0x4;
        }
        final int east = this.getBlockData(user, position.getRelative(BlockFace.EAST));
        final int north = this.getBlockData(user, position.getRelative(BlockFace.NORTH));
        final int south = this.getBlockData(user, position.getRelative(BlockFace.SOUTH));
        final int west = this.getBlockData(user, position.getRelative(BlockFace.WEST));
        if (TripwireConnectionHandler.TRIPWIRE_DATA_MAP.containsKey(east) || TripwireConnectionHandler.TRIPWIRE_HOOKS.get(east) == BlockFace.WEST) {
            b |= 0x8;
        }
        if (TripwireConnectionHandler.TRIPWIRE_DATA_MAP.containsKey(north) || TripwireConnectionHandler.TRIPWIRE_HOOKS.get(north) == BlockFace.SOUTH) {
            b |= 0x10;
        }
        if (TripwireConnectionHandler.TRIPWIRE_DATA_MAP.containsKey(south) || TripwireConnectionHandler.TRIPWIRE_HOOKS.get(south) == BlockFace.NORTH) {
            b |= 0x20;
        }
        if (TripwireConnectionHandler.TRIPWIRE_DATA_MAP.containsKey(west) || TripwireConnectionHandler.TRIPWIRE_HOOKS.get(west) == BlockFace.EAST) {
            b |= 0x40;
        }
        final int newBlockState = TripwireConnectionHandler.CONNECTED_BLOCKS[b];
        return (newBlockState == -1) ? blockState : newBlockState;
    }
    
    static {
        TRIPWIRE_DATA_MAP = new Int2ObjectOpenHashMap<TripwireData>();
        TRIPWIRE_HOOKS = new Int2ObjectArrayMap<BlockFace>();
        CONNECTED_BLOCKS = new int[128];
    }
    
    private static final class TripwireData
    {
        private final boolean attached;
        private final boolean disarmed;
        private final boolean powered;
        
        private TripwireData(final boolean attached, final boolean disarmed, final boolean powered) {
            this.attached = attached;
            this.disarmed = disarmed;
            this.powered = powered;
        }
        
        public boolean isAttached() {
            return this.attached;
        }
        
        public boolean isDisarmed() {
            return this.disarmed;
        }
        
        public boolean isPowered() {
            return this.powered;
        }
    }
}
