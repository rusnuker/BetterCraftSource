// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.InventoryTracker;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;

public class InventoryPackets
{
    public static void register(final Protocol1_9To1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.WINDOW_PROPERTY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.handler(wrapper -> {
                    final short windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final short property = wrapper.get((Type<Short>)Type.SHORT, 0);
                    final short value = wrapper.get((Type<Short>)Type.SHORT, 1);
                    final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    if (inventoryTracker.getInventory() != null && inventoryTracker.getInventory().equalsIgnoreCase("minecraft:enchanting_table") && property > 3 && property < 7) {
                        final short level = (short)(value >> 8);
                        final short enchantID = (short)(value & 0xFF);
                        wrapper.create(wrapper.getId(), propertyPacket -> {
                            propertyPacket.write(Type.UNSIGNED_BYTE, windowId);
                            propertyPacket.write(Type.SHORT, property);
                            propertyPacket.write(Type.SHORT, enchantID);
                            return;
                        }).scheduleSend(Protocol1_9To1_8.class);
                        wrapper.set(Type.SHORT, 0, (short)(property + 3));
                        wrapper.set(Type.SHORT, 1, level);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.OPEN_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final String inventory = wrapper.get(Type.STRING, 0);
                    final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    inventoryTracker.setInventory(inventory);
                    return;
                });
                this.handler(wrapper -> {
                    final String inventory2 = wrapper.get(Type.STRING, 0);
                    if (inventory2.equals("minecraft:brewing_stand")) {
                        wrapper.set(Type.UNSIGNED_BYTE, 1, (short)(wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 1) + 1));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.SET_SLOT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map(Type.ITEM);
                this.handler(wrapper -> {
                    final Item stack = wrapper.get(Type.ITEM, 0);
                    final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                    if (showShieldWhenSwordInHand) {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        final short slotID = wrapper.get((Type<Short>)Type.SHORT, 0);
                        final byte windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0).byteValue();
                        inventoryTracker.setItemId(windowId, slotID, (stack == null) ? 0 : stack.identifier());
                        entityTracker.syncShieldWithSword();
                    }
                    ItemRewriter.toClient(stack);
                    return;
                });
                this.handler(wrapper -> {
                    final InventoryTracker inventoryTracker2 = wrapper.user().get(InventoryTracker.class);
                    final short slotID2 = wrapper.get((Type<Short>)Type.SHORT, 0);
                    if (inventoryTracker2.getInventory() != null && inventoryTracker2.getInventory().equals("minecraft:brewing_stand") && slotID2 >= 4) {
                        wrapper.set(Type.SHORT, 0, (short)(slotID2 + 1));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.WINDOW_ITEMS, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.ITEM_ARRAY);
                this.handler(wrapper -> {
                    final Item[] stacks = wrapper.get(Type.ITEM_ARRAY, 0);
                    final Short windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                    for (short i = 0; i < stacks.length; ++i) {
                        final Item stack = stacks[i];
                        if (showShieldWhenSwordInHand) {
                            inventoryTracker.setItemId(windowId, i, (stack == null) ? 0 : stack.identifier());
                        }
                        ItemRewriter.toClient(stack);
                    }
                    if (showShieldWhenSwordInHand) {
                        entityTracker.syncShieldWithSword();
                    }
                    return;
                });
                this.handler(wrapper -> {
                    final InventoryTracker inventoryTracker2 = wrapper.user().get(InventoryTracker.class);
                    if (inventoryTracker2.getInventory() != null && inventoryTracker2.getInventory().equals("minecraft:brewing_stand")) {
                        final Item[] oldStack = wrapper.get(Type.ITEM_ARRAY, 0);
                        final Item[] newStack = new Item[oldStack.length + 1];
                        for (int j = 0; j < newStack.length; ++j) {
                            if (j > 4) {
                                newStack[j] = oldStack[j - 1];
                            }
                            else if (j != 4) {
                                newStack[j] = oldStack[j];
                            }
                        }
                        wrapper.set(Type.ITEM_ARRAY, 0, newStack);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.CLOSE_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    inventoryTracker.setInventory(null);
                    inventoryTracker.resetInventory(wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.MAP_DATA, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BYTE);
                this.handler(wrapper -> wrapper.write(Type.BOOLEAN, true));
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.CREATIVE_INVENTORY_ACTION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.SHORT);
                this.map(Type.ITEM);
                this.handler(wrapper -> {
                    final Item stack = wrapper.get(Type.ITEM, 0);
                    final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                    if (showShieldWhenSwordInHand) {
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        final short slotID = wrapper.get((Type<Short>)Type.SHORT, 0);
                        inventoryTracker.setItemId((short)0, slotID, (stack == null) ? 0 : stack.identifier());
                        entityTracker.syncShieldWithSword();
                    }
                    ItemRewriter.toServer(stack);
                    return;
                });
                this.handler(wrapper -> {
                    final short slot = wrapper.get((Type<Short>)Type.SHORT, 0);
                    final boolean throwItem = slot == 45;
                    if (throwItem) {
                        wrapper.create(ClientboundPackets1_9.SET_SLOT, new PacketHandler() {
                            final /* synthetic */ short val$slot;
                            
                            @Override
                            public void handle(final PacketWrapper wrapper) throws Exception {
                                wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
                                wrapper.write(Type.SHORT, this.val$slot);
                                wrapper.write(Type.ITEM, null);
                            }
                        }).send(Protocol1_9To1_8.class);
                        wrapper.set(Type.SHORT, 0, (Short)(-999));
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.CLICK_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map(Type.VAR_INT, Type.BYTE);
                this.map(Type.ITEM);
                this.handler(wrapper -> {
                    final Item stack = wrapper.get(Type.ITEM, 0);
                    if (Via.getConfig().isShowShieldWhenSwordInHand()) {
                        final Short windowId = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                        final byte mode = wrapper.get((Type<Byte>)Type.BYTE, 1);
                        final short hoverSlot = wrapper.get((Type<Short>)Type.SHORT, 0);
                        final byte button = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                        inventoryTracker.handleWindowClick(wrapper.user(), windowId, mode, hoverSlot, button);
                    }
                    ItemRewriter.toServer(stack);
                    return;
                });
                this.handler(wrapper -> {
                    final short windowID = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final short slot = wrapper.get((Type<Short>)Type.SHORT, 0);
                    boolean throwItem = slot == 45 && windowID == 0;
                    final InventoryTracker inventoryTracker2 = wrapper.user().get(InventoryTracker.class);
                    if (inventoryTracker2.getInventory() != null && inventoryTracker2.getInventory().equals("minecraft:brewing_stand")) {
                        if (slot == 4) {
                            throwItem = true;
                        }
                        if (slot > 4) {
                            wrapper.set(Type.SHORT, 0, (short)(slot - 1));
                        }
                    }
                    if (throwItem) {
                        wrapper.create(ClientboundPackets1_9.SET_SLOT, new PacketHandler() {
                            final /* synthetic */ short val$windowID;
                            final /* synthetic */ short val$slot;
                            
                            @Override
                            public void handle(final PacketWrapper wrapper) throws Exception {
                                wrapper.write(Type.UNSIGNED_BYTE, this.val$windowID);
                                wrapper.write(Type.SHORT, this.val$slot);
                                wrapper.write(Type.ITEM, null);
                            }
                        }).scheduleSend(Protocol1_9To1_8.class);
                        wrapper.set(Type.BYTE, 0, (Byte)0);
                        wrapper.set(Type.BYTE, 1, (Byte)0);
                        wrapper.set(Type.SHORT, 0, (Short)(-999));
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.CLOSE_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final InventoryTracker inventoryTracker = wrapper.user().get(InventoryTracker.class);
                    inventoryTracker.setInventory(null);
                    inventoryTracker.resetInventory(wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0));
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.HELD_ITEM_CHANGE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.SHORT);
                this.handler(wrapper -> {
                    final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand() && Via.getConfig().isShieldBlocking();
                    final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    if (entityTracker.isBlocking()) {
                        entityTracker.setBlocking(false);
                        if (!showShieldWhenSwordInHand) {
                            entityTracker.setSecondHand(null);
                        }
                    }
                    if (showShieldWhenSwordInHand) {
                        entityTracker.setHeldItemSlot(wrapper.get((Type<Short>)Type.SHORT, 0));
                        entityTracker.syncShieldWithSword();
                    }
                });
            }
        });
    }
}
