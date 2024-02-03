// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.packet.provider;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import java.util.Map;
import java.util.HashMap;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.util.Collection;

public interface PacketTypeMap<P>
{
    P typeByName(final String p0);
    
    P typeById(final int p0);
    
    Collection<P> types();
    
    default <S extends PacketType, T extends S> PacketTypeMap<S> of(final Class<T> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("Given class is not an enum");
        }
        final S[] types = enumClass.getEnumConstants();
        final Map<String, S> byName = new HashMap<String, S>(types.length);
        for (final S type : types) {
            byName.put(type.getName(), type);
        }
        return of(byName, types);
    }
    
    default <T> PacketTypeMap<T> of(final Map<String, T> packetsByName, final Int2ObjectMap<T> packetsById) {
        return new PacketTypeMapMap<T>(packetsByName, packetsById);
    }
    
    default <T> PacketTypeMap<T> of(final Map<String, T> packetsByName, final T[] packets) {
        return new PacketTypeArrayMap<T>(packetsByName, packets);
    }
}
