// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_10to1_11.packets;

import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.ComponentSerializer;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;

public class PlayerPackets1_11
{
    private static final ValueTransformer<Short, Float> TO_NEW_FLOAT;
    
    public void register(final Protocol1_10To1_11 protocol) {
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_9_3.TITLE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (action == 2) {
                        final JsonElement message = wrapper.read(Type.COMPONENT);
                        wrapper.clearPacket();
                        wrapper.setPacketType(ClientboundPackets1_9_3.CHAT_MESSAGE);
                        final String legacy = LegacyComponentSerializer.legacySection().serialize(((ComponentSerializer<I, Component, String>)GsonComponentSerializer.gson()).deserialize(message.toString()));
                        final JsonElement message2 = new JsonObject();
                        message2.getAsJsonObject().addProperty("text", legacy);
                        wrapper.write(Type.COMPONENT, message2);
                        wrapper.write(Type.BYTE, (Byte)2);
                    }
                    else if (action > 2) {
                        wrapper.set(Type.VAR_INT, 0, action - 1);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_9_3.COLLECT_ITEM, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> wrapper.read((Type<Object>)Type.VAR_INT));
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9_3>)protocol).registerServerbound(ServerboundPackets1_9_3.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE, (ValueTransformer<Object, Object>)PlayerPackets1_11.TO_NEW_FLOAT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE, (ValueTransformer<Object, Object>)PlayerPackets1_11.TO_NEW_FLOAT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE, (ValueTransformer<Object, Object>)PlayerPackets1_11.TO_NEW_FLOAT);
            }
        });
    }
    
    static {
        TO_NEW_FLOAT = new ValueTransformer<Short, Float>() {
            @Override
            public Float transform(final PacketWrapper wrapper, final Short inputValue) throws Exception {
                return inputValue / 16.0f;
            }
        };
    }
}
