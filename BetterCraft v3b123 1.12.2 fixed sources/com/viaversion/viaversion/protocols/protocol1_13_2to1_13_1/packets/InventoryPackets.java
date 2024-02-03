// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.Protocol1_13_2To1_13_1;

public class InventoryPackets
{
    public static void register(final Protocol1_13_2To1_13_1 protocol) {
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SET_SLOT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map(Type.FLAT_ITEM, Type.FLAT_VAR_INT_ITEM);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.WINDOW_ITEMS, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.FLAT_ITEM_ARRAY, Type.FLAT_VAR_INT_ITEM_ARRAY);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final String channel = wrapper.get(Type.STRING, 0);
                    if (channel.equals("minecraft:trader_list") || channel.equals("trader_list")) {
                        wrapper.passthrough((Type<Object>)Type.INT);
                        for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                            final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (secondItem) {
                                wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                            }
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.INT);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.ENTITY_EQUIPMENT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.FLAT_ITEM, Type.FLAT_VAR_INT_ITEM);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.DECLARE_RECIPES, wrapper -> {
            for (int recipesNo = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < recipesNo; ++i) {
                wrapper.passthrough(Type.STRING);
                final String type = wrapper.passthrough(Type.STRING);
                if (type.equals("crafting_shapeless")) {
                    wrapper.passthrough(Type.STRING);
                    for (int ingredientsNo = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i2 = 0; i2 < ingredientsNo; ++i2) {
                        wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_ITEM_ARRAY_VAR_INT));
                    }
                    wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                }
                else if (type.equals("crafting_shaped")) {
                    final int ingredientsNo2 = wrapper.passthrough((Type<Integer>)Type.VAR_INT) * wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    wrapper.passthrough(Type.STRING);
                    for (int i3 = 0; i3 < ingredientsNo2; ++i3) {
                        wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_ITEM_ARRAY_VAR_INT));
                    }
                    wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                }
                else if (type.equals("smelting")) {
                    wrapper.passthrough(Type.STRING);
                    wrapper.write(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, (Item[])(Object)wrapper.read((Type<T>)Type.FLAT_ITEM_ARRAY_VAR_INT));
                    wrapper.write(Type.FLAT_VAR_INT_ITEM, (Item)wrapper.read((Type<T>)Type.FLAT_ITEM));
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                }
            }
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_13>)protocol).registerServerbound(ServerboundPackets1_13.CLICK_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.FLAT_VAR_INT_ITEM, Type.FLAT_ITEM);
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_13>)protocol).registerServerbound(ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.SHORT);
                this.map(Type.FLAT_VAR_INT_ITEM, Type.FLAT_ITEM);
            }
        });
    }
}
