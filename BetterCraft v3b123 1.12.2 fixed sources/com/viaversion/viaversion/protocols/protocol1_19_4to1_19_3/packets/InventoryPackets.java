// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.rewriter.RecipeRewriter1_19_3;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.Protocol1_19_4To1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public final class InventoryPackets extends ItemRewriter<ClientboundPackets1_19_3, ServerboundPackets1_19_4, Protocol1_19_4To1_19_3>
{
    public InventoryPackets(final Protocol1_19_4To1_19_3 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        final BlockRewriter<ClientboundPackets1_19_3> blockRewriter = new BlockRewriter<ClientboundPackets1_19_3>(this.protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_19_3.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_19_3.BLOCK_CHANGE);
        blockRewriter.registerVarLongMultiBlockChange(ClientboundPackets1_19_3.MULTI_BLOCK_CHANGE);
        blockRewriter.registerChunkData1_19(ClientboundPackets1_19_3.CHUNK_DATA, Chunk1_18Type::new);
        blockRewriter.registerBlockEntityData(ClientboundPackets1_19_3.BLOCK_ENTITY_DATA);
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.EFFECT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map(Type.POSITION1_14);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                    if (id == 1010) {
                        if (data >= 1092 && data <= 1106) {
                            wrapper.set(Type.INT, 1, ((Protocol1_19_4To1_19_3)InventoryPackets.this.protocol).getMappingData().getNewItemId(data));
                        }
                        else {
                            wrapper.set(Type.INT, 0, 1011);
                            wrapper.set(Type.INT, 1, 0);
                        }
                    }
                    else if (id == 2001) {
                        wrapper.set(Type.INT, 1, ((Protocol1_19_4To1_19_3)InventoryPackets.this.protocol).getMappingData().getNewBlockStateId(data));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.OPEN_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.COMPONENT);
                this.handler(wrapper -> {
                    final int windowType = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (windowType >= 21) {
                        wrapper.set(Type.VAR_INT, 1, windowType + 1);
                    }
                });
            }
        });
        ((ItemRewriter<ClientboundPackets1_19_3, S, T>)this).registerSetCooldown(ClientboundPackets1_19_3.COOLDOWN);
        ((ItemRewriter<ClientboundPackets1_19_3, S, T>)this).registerWindowItems1_17_1(ClientboundPackets1_19_3.WINDOW_ITEMS);
        ((ItemRewriter<ClientboundPackets1_19_3, S, T>)this).registerSetSlot1_17_1(ClientboundPackets1_19_3.SET_SLOT);
        ((ItemRewriter<ClientboundPackets1_19_3, S, T>)this).registerAdvancements(ClientboundPackets1_19_3.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_19_3, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_19_3.ENTITY_EQUIPMENT);
        ((ItemRewriter<ClientboundPackets1_19_3, S, T>)this).registerTradeList1_19(ClientboundPackets1_19_3.TRADE_LIST);
        ((ItemRewriter<ClientboundPackets1_19_3, S, T>)this).registerWindowPropertyEnchantmentHandler(ClientboundPackets1_19_3.WINDOW_PROPERTY);
        ((ItemRewriter<ClientboundPackets1_19_3, S, T>)this).registerSpawnParticle1_19(ClientboundPackets1_19_3.SPAWN_PARTICLE);
        ((ItemRewriter<C, ServerboundPackets1_19_4, T>)this).registerCreativeInvAction(ServerboundPackets1_19_4.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<C, ServerboundPackets1_19_4, T>)this).registerClickWindow1_17_1(ServerboundPackets1_19_4.CLICK_WINDOW);
        new RecipeRewriter1_19_3<ClientboundPackets1_19_3>(this.protocol) {
            @Override
            public void handleCraftingShaped(final PacketWrapper wrapper) throws Exception {
                super.handleCraftingShaped(wrapper);
                wrapper.write(Type.BOOLEAN, true);
            }
        }.register(ClientboundPackets1_19_3.DECLARE_RECIPES);
    }
}
