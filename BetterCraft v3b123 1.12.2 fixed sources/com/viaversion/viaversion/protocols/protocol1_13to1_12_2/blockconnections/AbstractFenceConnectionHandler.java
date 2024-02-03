// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Arrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;

public abstract class AbstractFenceConnectionHandler extends ConnectionHandler
{
    private static final StairConnectionHandler STAIR_CONNECTION_HANDLER;
    private final IntSet blockStates;
    private final int[] connectedBlockStates;
    private final int blockConnectionsTypeId;
    
    protected AbstractFenceConnectionHandler(final String blockConnections) {
        this.blockStates = new IntOpenHashSet();
        this.connectedBlockStates = new int[this.statesSize()];
        this.blockConnectionsTypeId = ((blockConnections != null) ? BlockData.connectionTypeId(blockConnections) : -1);
        Arrays.fill(this.connectedBlockStates, -1);
    }
    
    public ConnectionData.ConnectorInitAction getInitAction(final String key) {
        final AbstractFenceConnectionHandler handler = this;
        return blockData -> {
            if (key.equals(blockData.getMinecraftKey())) {
                if (!blockData.hasData("waterlogged") || !blockData.getValue("waterlogged").equals("true")) {
                    this.blockStates.add(blockData.getSavedBlockStateId());
                    ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), handler);
                    final byte internalStateId = this.getStates(blockData);
                    this.connectedBlockStates[internalStateId] = blockData.getSavedBlockStateId();
                }
            }
        };
    }
    
    protected byte getStates(final WrappedBlockData blockData) {
        byte states = 0;
        if (blockData.getValue("east").equals("true")) {
            states |= 0x1;
        }
        if (blockData.getValue("north").equals("true")) {
            states |= 0x2;
        }
        if (blockData.getValue("south").equals("true")) {
            states |= 0x4;
        }
        if (blockData.getValue("west").equals("true")) {
            states |= 0x8;
        }
        return states;
    }
    
    protected byte getStates(final UserConnection user, final Position position, final int blockState) {
        byte states = 0;
        final boolean pre1_12 = user.getProtocolInfo().getServerProtocolVersion() < ProtocolVersion.v1_12.getVersion();
        if (this.connects(BlockFace.EAST, this.getBlockData(user, position.getRelative(BlockFace.EAST)), pre1_12)) {
            states |= 0x1;
        }
        if (this.connects(BlockFace.NORTH, this.getBlockData(user, position.getRelative(BlockFace.NORTH)), pre1_12)) {
            states |= 0x2;
        }
        if (this.connects(BlockFace.SOUTH, this.getBlockData(user, position.getRelative(BlockFace.SOUTH)), pre1_12)) {
            states |= 0x4;
        }
        if (this.connects(BlockFace.WEST, this.getBlockData(user, position.getRelative(BlockFace.WEST)), pre1_12)) {
            states |= 0x8;
        }
        return states;
    }
    
    protected byte statesSize() {
        return 16;
    }
    
    @Override
    public int getBlockData(final UserConnection user, final Position position) {
        return AbstractFenceConnectionHandler.STAIR_CONNECTION_HANDLER.connect(user, position, super.getBlockData(user, position));
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final int newBlockState = this.connectedBlockStates[this.getStates(user, position, blockState)];
        return (newBlockState == -1) ? blockState : newBlockState;
    }
    
    protected boolean connects(final BlockFace side, final int blockState, final boolean pre1_12) {
        if (this.blockStates.contains(blockState)) {
            return true;
        }
        if (this.blockConnectionsTypeId == -1) {
            return false;
        }
        final BlockData blockData = ConnectionData.blockConnectionData.get(blockState);
        return blockData != null && blockData.connectsTo(this.blockConnectionsTypeId, side.opposite(), pre1_12);
    }
    
    public IntSet getBlockStates() {
        return this.blockStates;
    }
    
    static {
        STAIR_CONNECTION_HANDLER = new StairConnectionHandler();
    }
}
