// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_12to1_11_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.providers.InventoryQuickMoveProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.Protocol1_12To1_11_1;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<ClientboundPackets1_9_3, ServerboundPackets1_12, Protocol1_12To1_11_1>
{
    public InventoryPackets(final Protocol1_12To1_11_1 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((ItemRewriter<ClientboundPackets1_9_3, S, T>)this).registerSetSlot(ClientboundPackets1_9_3.SET_SLOT, Type.ITEM);
        ((ItemRewriter<ClientboundPackets1_9_3, S, T>)this).registerWindowItems(ClientboundPackets1_9_3.WINDOW_ITEMS, Type.ITEM_ARRAY);
        ((ItemRewriter<ClientboundPackets1_9_3, S, T>)this).registerEntityEquipment(ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_9_3.PLUGIN_MESSAGE, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    if (wrapper.get(Type.STRING, 0).equalsIgnoreCase("MC|TrList")) {
                        wrapper.passthrough((Type<Object>)Type.INT);
                        for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.ITEM));
                            InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.ITEM));
                            final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (secondItem) {
                                InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.ITEM));
                            }
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.INT);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_12>)this.protocol).registerServerbound(ServerboundPackets1_12.CLICK_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.ITEM);
                this.handler(wrapper -> {
                    final Item item = wrapper.get(Type.ITEM, 0);
                    if (!Via.getConfig().is1_12QuickMoveActionFix()) {
                        InventoryPackets.this.handleItemToServer(item);
                    }
                    else {
                        final byte button = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int mode = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (mode == 1 && button == 0 && item == null) {
                            final short windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                            final short slotId = wrapper.get((Type<Short>)Type.SHORT, 0);
                            final short actionId = wrapper.get((Type<Short>)Type.SHORT, 1);
                            final InventoryQuickMoveProvider provider = Via.getManager().getProviders().get(InventoryQuickMoveProvider.class);
                            final boolean succeed = provider.registerQuickMoveAction(windowId, slotId, actionId, wrapper.user());
                            if (succeed) {
                                wrapper.cancel();
                            }
                        }
                        else {
                            InventoryPackets.this.handleItemToServer(item);
                        }
                    }
                });
            }
        });
        ((ItemRewriter<C, ServerboundPackets1_12, T>)this).registerCreativeInvAction(ServerboundPackets1_12.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        if (item.identifier() == 355) {
            item.setData((short)0);
        }
        boolean newItem = item.identifier() >= 235 && item.identifier() <= 252;
        newItem |= (item.identifier() == 453);
        if (newItem) {
            item.setIdentifier(1);
            item.setData((short)0);
        }
        return item;
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        if (item.identifier() == 355) {
            item.setData((short)14);
        }
        return item;
    }
}
