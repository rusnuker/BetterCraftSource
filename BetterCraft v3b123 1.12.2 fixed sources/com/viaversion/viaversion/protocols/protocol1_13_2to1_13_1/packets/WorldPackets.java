// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.Protocol1_13_2To1_13_1;

public class WorldPackets
{
    public static void register(final Protocol1_13_2To1_13_1 protocol) {
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PARTICLE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    if (id == 27) {
                        wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                    }
                });
            }
        });
    }
}
