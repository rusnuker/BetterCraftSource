// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.mapping;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketType;

final class PacketTypeMapping implements PacketMapping
{
    private final PacketType mappedPacketType;
    private final PacketHandler handler;
    
    PacketTypeMapping(final PacketType mappedPacketType, final PacketHandler handler) {
        this.mappedPacketType = mappedPacketType;
        this.handler = handler;
    }
    
    @Override
    public void applyType(final PacketWrapper wrapper) {
        if (this.mappedPacketType != null) {
            wrapper.setPacketType(this.mappedPacketType);
        }
    }
    
    @Override
    public PacketHandler handler() {
        return this.handler;
    }
}
