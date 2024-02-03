// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.mapping;

import java.util.Arrays;
import com.viaversion.viaversion.api.protocol.packet.State;

final class PacketArrayMappings implements PacketMappings
{
    private final PacketMapping[][] packets;
    
    PacketArrayMappings() {
        this.packets = new PacketMapping[State.values().length][];
    }
    
    @Override
    public PacketMapping mappedPacket(final State state, final int unmappedId) {
        final PacketMapping[] packets = this.packets[state.ordinal()];
        if (packets != null && unmappedId >= 0 && unmappedId < packets.length) {
            return packets[unmappedId];
        }
        return null;
    }
    
    @Override
    public void addMapping(final State state, final int unmappedId, final PacketMapping mapping) {
        final int ordinal = state.ordinal();
        PacketMapping[] packets = this.packets[ordinal];
        if (packets == null) {
            packets = new PacketMapping[unmappedId + 8];
            this.packets[ordinal] = packets;
        }
        else if (unmappedId >= packets.length) {
            packets = Arrays.copyOf(packets, unmappedId + 32);
            this.packets[ordinal] = packets;
        }
        packets[unmappedId] = mapping;
    }
}
