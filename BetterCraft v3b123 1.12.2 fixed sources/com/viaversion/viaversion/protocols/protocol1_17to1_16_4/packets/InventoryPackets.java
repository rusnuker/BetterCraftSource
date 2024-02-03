// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_17to1_16_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.storage.InventoryAcknowledgements;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public final class InventoryPackets extends ItemRewriter<ClientboundPackets1_16_2, ServerboundPackets1_17, Protocol1_17To1_16_4>
{
    public InventoryPackets(final Protocol1_17To1_16_4 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerSetCooldown(ClientboundPackets1_16_2.COOLDOWN);
        ((ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerWindowItems(ClientboundPackets1_16_2.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        ((ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerTradeList(ClientboundPackets1_16_2.TRADE_LIST);
        ((ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerSetSlot(ClientboundPackets1_16_2.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerAdvancements(ClientboundPackets1_16_2.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_16_2.ENTITY_EQUIPMENT);
        ((ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerSpawnParticle(ClientboundPackets1_16_2.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
        new RecipeRewriter<ClientboundPackets1_16_2>(this.protocol).register(ClientboundPackets1_16_2.DECLARE_RECIPES);
        ((ItemRewriter<C, ServerboundPackets1_17, T>)this).registerCreativeInvAction(ServerboundPackets1_17.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this.protocol).registerServerbound(ServerboundPackets1_17.EDIT_BOOK, wrapper -> this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this.protocol).registerServerbound(ServerboundPackets1_17.CLICK_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.BYTE);
                this.handler(wrapper -> wrapper.write(Type.SHORT, (Short)0));
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    for (int length = wrapper.read((Type<Integer>)Type.VAR_INT), i = 0; i < length; ++i) {
                        wrapper.read((Type<Object>)Type.SHORT);
                        wrapper.read(Type.FLAT_VAR_INT_ITEM);
                    }
                    Item item = wrapper.read(Type.FLAT_VAR_INT_ITEM);
                    final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (action == 5 || action == 1) {
                        item = null;
                    }
                    else {
                        InventoryPackets.this.handleItemToServer(item);
                    }
                    wrapper.write(Type.FLAT_VAR_INT_ITEM, item);
                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_16_2.WINDOW_CONFIRMATION, null, wrapper -> {
            final short inventoryId = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
            final short confirmationId = wrapper.read((Type<Short>)Type.SHORT);
            final boolean accepted = wrapper.read((Type<Boolean>)Type.BOOLEAN);
            if (!accepted) {
                final int id = 0x40000000 | inventoryId << 16 | (confirmationId & 0xFFFF);
                wrapper.user().get(InventoryAcknowledgements.class).addId(id);
                final PacketWrapper pingPacket = wrapper.create(ClientboundPackets1_17.PING);
                pingPacket.write(Type.INT, id);
                pingPacket.send(Protocol1_17To1_16_4.class);
            }
            wrapper.cancel();
            return;
        });
        this.protocol.registerServerbound(ServerboundPackets1_17.PONG, null, wrapper -> {
            final int id2 = wrapper.read((Type<Integer>)Type.INT);
            if ((id2 & 0x40000000) != 0x0 && wrapper.user().get(InventoryAcknowledgements.class).removeId(id2)) {
                final short inventoryId2 = (short)(id2 >> 16 & 0xFF);
                final short confirmationId2 = (short)(id2 & 0xFFFF);
                final PacketWrapper packet = wrapper.create(ServerboundPackets1_16_2.WINDOW_CONFIRMATION);
                packet.write(Type.UNSIGNED_BYTE, inventoryId2);
                packet.write(Type.SHORT, confirmationId2);
                packet.write(Type.BOOLEAN, true);
                packet.sendToServer(Protocol1_17To1_16_4.class);
            }
            wrapper.cancel();
        });
    }
}
