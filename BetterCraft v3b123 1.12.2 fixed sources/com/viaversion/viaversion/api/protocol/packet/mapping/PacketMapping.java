// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.mapping;

import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

public interface PacketMapping
{
    void applyType(final PacketWrapper p0);
    
    PacketHandler handler();
    
    default PacketMapping of(final int mappedPacketId, final PacketHandler handler) {
        return new PacketIdMapping(mappedPacketId, handler);
    }
    
    default PacketMapping of(final PacketType mappedPacketType, final PacketHandler handler) {
        return new PacketTypeMapping(mappedPacketType, handler);
    }
}
