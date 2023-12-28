// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol;

import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public interface SimpleProtocol extends Protocol<DummyPacketTypes, DummyPacketTypes, DummyPacketTypes, DummyPacketTypes>
{
    public enum DummyPacketTypes implements ClientboundPacketType, ServerboundPacketType
    {
        @Override
        public int getId() {
            return this.ordinal();
        }
        
        @Override
        public String getName() {
            return this.name();
        }
    }
}
