// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.provider;

import com.viaversion.viaversion.api.protocol.packet.State;
import java.util.Map;
import com.google.common.annotations.Beta;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

@Beta
public final class SimplePacketTypesProvider<CU extends ClientboundPacketType, CM extends ClientboundPacketType, SM extends ServerboundPacketType, SU extends ServerboundPacketType> implements PacketTypesProvider<CU, CM, SM, SU>
{
    private final Map<State, PacketTypeMap<CU>> unmappedClientboundPacketTypes;
    private final Map<State, PacketTypeMap<CM>> mappedClientboundPacketTypes;
    private final Map<State, PacketTypeMap<SM>> mappedServerboundPacketTypes;
    private final Map<State, PacketTypeMap<SU>> unmappedServerboundPacketTypes;
    
    public SimplePacketTypesProvider(final Map<State, PacketTypeMap<CU>> unmappedClientboundPacketTypes, final Map<State, PacketTypeMap<CM>> mappedClientboundPacketTypes, final Map<State, PacketTypeMap<SM>> mappedServerboundPacketTypes, final Map<State, PacketTypeMap<SU>> unmappedServerboundPacketTypes) {
        this.unmappedClientboundPacketTypes = unmappedClientboundPacketTypes;
        this.mappedClientboundPacketTypes = mappedClientboundPacketTypes;
        this.mappedServerboundPacketTypes = mappedServerboundPacketTypes;
        this.unmappedServerboundPacketTypes = unmappedServerboundPacketTypes;
    }
    
    @Override
    public Map<State, PacketTypeMap<CU>> unmappedClientboundPacketTypes() {
        return this.unmappedClientboundPacketTypes;
    }
    
    @Override
    public Map<State, PacketTypeMap<CM>> mappedClientboundPacketTypes() {
        return this.mappedClientboundPacketTypes;
    }
    
    @Override
    public Map<State, PacketTypeMap<SM>> mappedServerboundPacketTypes() {
        return this.mappedServerboundPacketTypes;
    }
    
    @Override
    public Map<State, PacketTypeMap<SU>> unmappedServerboundPacketTypes() {
        return this.unmappedServerboundPacketTypes;
    }
}
