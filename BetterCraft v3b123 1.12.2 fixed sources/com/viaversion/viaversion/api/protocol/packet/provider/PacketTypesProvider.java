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
public interface PacketTypesProvider<CU extends ClientboundPacketType, CM extends ClientboundPacketType, SM extends ServerboundPacketType, SU extends ServerboundPacketType>
{
    Map<State, PacketTypeMap<CU>> unmappedClientboundPacketTypes();
    
    Map<State, PacketTypeMap<SU>> unmappedServerboundPacketTypes();
    
    Map<State, PacketTypeMap<CM>> mappedClientboundPacketTypes();
    
    Map<State, PacketTypeMap<SM>> mappedServerboundPacketTypes();
}
