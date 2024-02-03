// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets1_13_1 extends ItemRewriter<ClientboundPackets1_13, ServerboundPackets1_13, Protocol1_13To1_13_1>
{
    public InventoryPackets1_13_1(final Protocol1_13To1_13_1 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerSetCooldown(ClientboundPackets1_13.COOLDOWN);
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerWindowItems(ClientboundPackets1_13.WINDOW_ITEMS, Type.FLAT_ITEM_ARRAY);
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerSetSlot(ClientboundPackets1_13.SET_SLOT, Type.FLAT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.PLUGIN_MESSAGE, wrapper -> {
            final String channel = wrapper.passthrough(Type.STRING);
            if (channel.equals("minecraft:trader_list")) {
                wrapper.passthrough((Type<Object>)Type.INT);
                for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                    final Item input = wrapper.passthrough(Type.FLAT_ITEM);
                    this.handleItemToClient(input);
                    final Item output = wrapper.passthrough(Type.FLAT_ITEM);
                    this.handleItemToClient(output);
                    final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                    if (secondItem) {
                        final Item second = wrapper.passthrough(Type.FLAT_ITEM);
                        this.handleItemToClient(second);
                    }
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.passthrough((Type<Object>)Type.INT);
                    wrapper.passthrough((Type<Object>)Type.INT);
                }
            }
            return;
        });
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerEntityEquipment(ClientboundPackets1_13.ENTITY_EQUIPMENT, Type.FLAT_ITEM);
        ((ItemRewriter<C, ServerboundPackets1_13, T>)this).registerClickWindow(ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_ITEM);
        ((ItemRewriter<C, ServerboundPackets1_13, T>)this).registerCreativeInvAction(ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_ITEM);
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerSpawnParticle(ClientboundPackets1_13.SPAWN_PARTICLE, Type.FLAT_ITEM, Type.FLOAT);
    }
}
