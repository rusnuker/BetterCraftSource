// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_18to1_17_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.Protocol1_18To1_17_1;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.ClientboundPackets1_17_1;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public final class InventoryPackets extends ItemRewriter<ClientboundPackets1_17_1, ServerboundPackets1_17, Protocol1_18To1_17_1>
{
    public InventoryPackets(final Protocol1_18To1_17_1 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((ItemRewriter<ClientboundPackets1_17_1, S, T>)this).registerSetCooldown(ClientboundPackets1_17_1.COOLDOWN);
        ((ItemRewriter<ClientboundPackets1_17_1, S, T>)this).registerWindowItems1_17_1(ClientboundPackets1_17_1.WINDOW_ITEMS);
        ((ItemRewriter<ClientboundPackets1_17_1, S, T>)this).registerTradeList(ClientboundPackets1_17_1.TRADE_LIST);
        ((ItemRewriter<ClientboundPackets1_17_1, S, T>)this).registerSetSlot1_17_1(ClientboundPackets1_17_1.SET_SLOT);
        ((ItemRewriter<ClientboundPackets1_17_1, S, T>)this).registerAdvancements(ClientboundPackets1_17_1.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_17_1, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_17_1.ENTITY_EQUIPMENT);
        ((AbstractProtocol<ClientboundPackets1_17_1, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_17_1.EFFECT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map(Type.POSITION1_14);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                    if (id == 1010) {
                        wrapper.set(Type.INT, 1, ((Protocol1_18To1_17_1)InventoryPackets.this.protocol).getMappingData().getNewItemId(data));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17_1, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_17_1.SPAWN_PARTICLE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    if (id == 2) {
                        wrapper.set(Type.INT, 0, 3);
                        wrapper.write(Type.VAR_INT, 7754);
                    }
                    else if (id == 3) {
                        wrapper.write(Type.VAR_INT, 7786);
                    }
                    else {
                        final ParticleMappings mappings = ((Protocol1_18To1_17_1)InventoryPackets.this.protocol).getMappingData().getParticleMappings();
                        if (mappings.isBlockParticle(id)) {
                            final int data = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                            wrapper.set(Type.VAR_INT, 0, ((Protocol1_18To1_17_1)InventoryPackets.this.protocol).getMappingData().getNewBlockStateId(data));
                        }
                        else if (mappings.isItemParticle(id)) {
                            InventoryPackets.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        }
                        final int newId = ((Protocol1_18To1_17_1)InventoryPackets.this.protocol).getMappingData().getNewParticleId(id);
                        if (newId != id) {
                            wrapper.set(Type.INT, 0, newId);
                        }
                    }
                });
            }
        });
        new RecipeRewriter<ClientboundPackets1_17_1>(this.protocol).register(ClientboundPackets1_17_1.DECLARE_RECIPES);
        ((ItemRewriter<C, ServerboundPackets1_17, T>)this).registerClickWindow1_17_1(ServerboundPackets1_17.CLICK_WINDOW);
        ((ItemRewriter<C, ServerboundPackets1_17, T>)this).registerCreativeInvAction(ServerboundPackets1_17.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
    }
}
