// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.provider.AckSequenceProvider;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ServerboundPackets1_19;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public final class InventoryPackets extends ItemRewriter<ClientboundPackets1_18, ServerboundPackets1_19, Protocol1_19To1_18_2>
{
    public InventoryPackets(final Protocol1_19To1_18_2 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((ItemRewriter<ClientboundPackets1_18, S, T>)this).registerSetCooldown(ClientboundPackets1_18.COOLDOWN);
        ((ItemRewriter<ClientboundPackets1_18, S, T>)this).registerWindowItems1_17_1(ClientboundPackets1_18.WINDOW_ITEMS);
        ((ItemRewriter<ClientboundPackets1_18, S, T>)this).registerSetSlot1_17_1(ClientboundPackets1_18.SET_SLOT);
        ((ItemRewriter<ClientboundPackets1_18, S, T>)this).registerAdvancements(ClientboundPackets1_18.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_18, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_18.ENTITY_EQUIPMENT);
        ((AbstractProtocol<ClientboundPackets1_18, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_18.SPAWN_PARTICLE, new PacketHandlers() {
            public void register() {
                this.map(Type.INT, Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.INT);
                this.handler(InventoryPackets.this.getSpawnParticleHandler(Type.VAR_INT, Type.FLAT_VAR_INT_ITEM));
            }
        });
        ((ItemRewriter<C, ServerboundPackets1_19, T>)this).registerClickWindow1_17_1(ServerboundPackets1_19.CLICK_WINDOW);
        ((ItemRewriter<C, ServerboundPackets1_19, T>)this).registerCreativeInvAction(ServerboundPackets1_19.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_18, S, T>)this).registerWindowPropertyEnchantmentHandler(ClientboundPackets1_18.WINDOW_PROPERTY);
        ((AbstractProtocol<ClientboundPackets1_18, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_18.TRADE_LIST, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int size = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    wrapper.write(Type.VAR_INT, size);
                    for (int i = 0; i < size; ++i) {
                        InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                            InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        }
                        else {
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, null);
                        }
                        wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        wrapper.passthrough((Type<Object>)Type.FLOAT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19>)this.protocol).registerServerbound(ServerboundPackets1_19.PLAYER_DIGGING, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.POSITION1_14);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(InventoryPackets.this.sequenceHandler());
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19>)this.protocol).registerServerbound(ServerboundPackets1_19.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.POSITION1_14);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(InventoryPackets.this.sequenceHandler());
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19>)this.protocol).registerServerbound(ServerboundPackets1_19.USE_ITEM, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(InventoryPackets.this.sequenceHandler());
            }
        });
        new RecipeRewriter<ClientboundPackets1_18>(this.protocol).register(ClientboundPackets1_18.DECLARE_RECIPES);
    }
    
    private PacketHandler sequenceHandler() {
        return wrapper -> {
            final int sequence = wrapper.read((Type<Integer>)Type.VAR_INT);
            final AckSequenceProvider provider = Via.getManager().getProviders().get(AckSequenceProvider.class);
            provider.handleSequence(wrapper.user(), sequence);
        };
    }
}
