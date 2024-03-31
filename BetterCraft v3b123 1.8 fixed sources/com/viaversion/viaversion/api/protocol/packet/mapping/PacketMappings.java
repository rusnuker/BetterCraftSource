// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.mapping;

import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.State;

public interface PacketMappings
{
    PacketMapping mappedPacket(final State p0, final int p1);
    
    default boolean hasMapping(final PacketType packetType) {
        return this.mappedPacket(packetType.state(), packetType.getId()) != null;
    }
    
    default boolean hasMapping(final State state, final int unmappedId) {
        return this.mappedPacket(state, unmappedId) != null;
    }
    
    default void addMapping(final PacketType packetType, final PacketMapping mapping) {
        this.addMapping(packetType.state(), packetType.getId(), mapping);
    }
    
    void addMapping(final State p0, final int p1, final PacketMapping p2);
    
    default PacketMappings arrayMappings() {
        return new PacketArrayMappings();
    }
}
