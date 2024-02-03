// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_12_1to1_12_2;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_12_1To1_12_2 extends BackwardsProtocol<ClientboundPackets1_12_1, ClientboundPackets1_12_1, ServerboundPackets1_12_1, ServerboundPackets1_12_1>
{
    public Protocol1_12_1To1_12_2() {
        super(ClientboundPackets1_12_1.class, ClientboundPackets1_12_1.class, ServerboundPackets1_12_1.class, ServerboundPackets1_12_1.class);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_12_1, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_12_1.KEEP_ALIVE, new PacketHandlers() {
            public void register() {
                this.handler(packetWrapper -> {
                    final Long keepAlive = packetWrapper.read((Type<Long>)Type.LONG);
                    packetWrapper.user().get(KeepAliveTracker.class).setKeepAlive(keepAlive);
                    packetWrapper.write(Type.VAR_INT, keepAlive.hashCode());
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_12_1>)this).registerServerbound(ServerboundPackets1_12_1.KEEP_ALIVE, new PacketHandlers() {
            public void register() {
                this.handler(packetWrapper -> {
                    final int keepAlive = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                    final long realKeepAlive = packetWrapper.user().get(KeepAliveTracker.class).getKeepAlive();
                    if (keepAlive != Long.hashCode(realKeepAlive)) {
                        packetWrapper.cancel();
                    }
                    else {
                        packetWrapper.write(Type.LONG, realKeepAlive);
                        packetWrapper.user().get(KeepAliveTracker.class).setKeepAlive(2147483647L);
                    }
                });
            }
        });
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.put(new KeepAliveTracker());
    }
}
