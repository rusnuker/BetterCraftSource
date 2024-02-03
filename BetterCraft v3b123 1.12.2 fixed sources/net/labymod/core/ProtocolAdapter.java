// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

import net.labymod.api.protocol.shadow.ShadowProtocol;
import net.labymod.api.protocol.chunk.Extracted;
import net.labymod.api.protocol.chunk.ChunkCachingProtocol;

public interface ProtocolAdapter
{
    void onReceiveChunkPacket(final Object p0, final Object p1);
    
    void loadChunk(final ChunkCachingProtocol p0, final Extracted p1, final int p2, final int p3, final boolean p4);
    
    void loadChunkBulk(final ChunkCachingProtocol p0, final Extracted[] p1, final int[] p2, final int[] p3);
    
    boolean handleOutgoingPacket(final Object p0, final ShadowProtocol p1);
}
