// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockConnectionStorage;
import com.viaversion.viaversion.api.connection.UserConnection;

public class PacketBlockConnectionProvider extends BlockConnectionProvider
{
    @Override
    public void storeBlock(final UserConnection connection, final int x, final int y, final int z, final int blockState) {
        connection.get(BlockConnectionStorage.class).store(x, y, z, blockState);
    }
    
    @Override
    public void removeBlock(final UserConnection connection, final int x, final int y, final int z) {
        connection.get(BlockConnectionStorage.class).remove(x, y, z);
    }
    
    @Override
    public int getBlockData(final UserConnection connection, final int x, final int y, final int z) {
        return connection.get(BlockConnectionStorage.class).get(x, y, z);
    }
    
    @Override
    public void clearStorage(final UserConnection connection) {
        connection.get(BlockConnectionStorage.class).clear();
    }
    
    @Override
    public void unloadChunk(final UserConnection connection, final int x, final int z) {
        connection.get(BlockConnectionStorage.class).unloadChunk(x, z);
    }
    
    @Override
    public void unloadChunkSection(final UserConnection connection, final int chunkX, final int chunkY, final int chunkZ) {
        connection.get(BlockConnectionStorage.class).unloadSection(chunkX, chunkY, chunkZ);
    }
    
    @Override
    public boolean storesBlocks() {
        return true;
    }
    
    @Override
    public UserBlockData forUser(final UserConnection connection) {
        final BlockConnectionStorage storage = connection.get(BlockConnectionStorage.class);
        return (x, y, z) -> storage.get(x, y, z);
    }
}
