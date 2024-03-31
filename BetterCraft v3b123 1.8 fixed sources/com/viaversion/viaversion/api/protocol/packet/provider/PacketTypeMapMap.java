// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.provider;

import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import java.util.Map;

final class PacketTypeMapMap<P> implements PacketTypeMap<P>
{
    private final Map<String, P> packetsByName;
    private final Int2ObjectMap<P> packetsById;
    
    PacketTypeMapMap(final Map<String, P> packetsByName, final Int2ObjectMap<P> packetsById) {
        this.packetsByName = packetsByName;
        this.packetsById = packetsById;
    }
    
    @Override
    public P typeByName(final String packetTypeName) {
        return this.packetsByName.get(packetTypeName);
    }
    
    @Override
    public P typeById(final int packetTypeId) {
        return this.packetsById.get(packetTypeId);
    }
    
    @Override
    public Collection<P> types() {
        return this.packetsById.values();
    }
}
