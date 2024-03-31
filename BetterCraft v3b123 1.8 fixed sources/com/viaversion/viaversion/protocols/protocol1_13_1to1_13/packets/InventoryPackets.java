// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13_1to1_13.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<ClientboundPackets1_13, ServerboundPackets1_13, Protocol1_13_1To1_13>
{
    public InventoryPackets(final Protocol1_13_1To1_13 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerSetSlot(ClientboundPackets1_13.SET_SLOT, Type.FLAT_ITEM);
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerWindowItems(ClientboundPackets1_13.WINDOW_ITEMS, Type.FLAT_ITEM_ARRAY);
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerAdvancements(ClientboundPackets1_13.ADVANCEMENTS, Type.FLAT_ITEM);
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerSetCooldown(ClientboundPackets1_13.COOLDOWN);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final String channel = wrapper.get(Type.STRING, 0);
                    if (channel.equals("minecraft:trader_list") || channel.equals("trader_list")) {
                        wrapper.passthrough((Type<Object>)Type.INT);
                        for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_ITEM));
                            InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_ITEM));
                            final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (secondItem) {
                                InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_ITEM));
                            }
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.INT);
                            wrapper.passthrough((Type<Object>)Type.INT);
                        }
                    }
                });
            }
        });
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerEntityEquipment(ClientboundPackets1_13.ENTITY_EQUIPMENT, Type.FLAT_ITEM);
        final RecipeRewriter<ClientboundPackets1_13> recipeRewriter = new RecipeRewriter<ClientboundPackets1_13>(this.protocol);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.DECLARE_RECIPES, wrapper -> {
            for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                wrapper.passthrough(Type.STRING);
                final String type = wrapper.passthrough(Type.STRING).replace("minecraft:", "");
                recipeRewriter.handleRecipeType(wrapper, type);
            }
            return;
        });
        ((ItemRewriter<C, ServerboundPackets1_13, T>)this).registerClickWindow(ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_ITEM);
        ((ItemRewriter<C, ServerboundPackets1_13, T>)this).registerCreativeInvAction(ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_ITEM);
        ((ItemRewriter<ClientboundPackets1_13, S, T>)this).registerSpawnParticle(ClientboundPackets1_13.SPAWN_PARTICLE, Type.FLAT_ITEM, Type.FLOAT);
    }
}
