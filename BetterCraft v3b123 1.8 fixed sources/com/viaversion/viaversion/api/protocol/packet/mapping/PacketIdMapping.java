// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.mapping;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;

final class PacketIdMapping implements PacketMapping
{
    private final int mappedPacketId;
    private final PacketHandler handler;
    
    PacketIdMapping(final int mappedPacketId, final PacketHandler handler) {
        this.mappedPacketId = mappedPacketId;
        this.handler = handler;
    }
    
    @Override
    public void applyType(final PacketWrapper wrapper) {
        wrapper.setId(this.mappedPacketId);
    }
    
    @Override
    public PacketHandler handler() {
        return this.handler;
    }
}
