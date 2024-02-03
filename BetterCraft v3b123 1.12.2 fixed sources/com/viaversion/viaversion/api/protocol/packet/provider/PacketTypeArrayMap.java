// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.provider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

final class PacketTypeArrayMap<P> implements PacketTypeMap<P>
{
    private final Map<String, P> packetsByName;
    private final P[] packets;
    
    PacketTypeArrayMap(final Map<String, P> packetsByName, final P[] packets) {
        this.packetsByName = packetsByName;
        this.packets = packets;
    }
    
    @Override
    public P typeByName(final String packetTypeName) {
        return this.packetsByName.get(packetTypeName);
    }
    
    @Override
    public P typeById(final int packetTypeId) {
        return (packetTypeId >= 0 && packetTypeId < this.packets.length) ? this.packets[packetTypeId] : null;
    }
    
    @Override
    public Collection<P> types() {
        return Arrays.asList(this.packets);
    }
}
