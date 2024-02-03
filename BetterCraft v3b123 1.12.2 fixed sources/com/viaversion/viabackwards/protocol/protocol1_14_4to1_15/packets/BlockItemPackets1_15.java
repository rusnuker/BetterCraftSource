// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_14_4to1_15.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.protocol.protocol1_14_4to1_15.Protocol1_14_4To1_15;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_15 extends ItemRewriter<ClientboundPackets1_15, ServerboundPackets1_14, Protocol1_14_4To1_15>
{
    public BlockItemPackets1_15(final Protocol1_14_4To1_15 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final BlockRewriter<ClientboundPackets1_15> blockRewriter = new BlockRewriter<ClientboundPackets1_15>(this.protocol, Type.POSITION1_14);
        new RecipeRewriter<ClientboundPackets1_15>(this.protocol).register(ClientboundPackets1_15.DECLARE_RECIPES);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this.protocol).registerServerbound(ServerboundPackets1_14.EDIT_BOOK, wrapper -> this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_15, S, T>)this).registerSetCooldown(ClientboundPackets1_15.COOLDOWN);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_15, S, T>)this).registerWindowItems(ClientboundPackets1_15.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_15, S, T>)this).registerSetSlot(ClientboundPackets1_15.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_15, S, T>)this).registerTradeList(ClientboundPackets1_15.TRADE_LIST);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_15, S, T>)this).registerEntityEquipment(ClientboundPackets1_15.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_15, S, T>)this).registerAdvancements(ClientboundPackets1_15.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_14, T>)this).registerClickWindow(ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_14, T>)this).registerCreativeInvAction(ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_15.ACKNOWLEDGE_PLAYER_DIGGING);
        blockRewriter.registerBlockAction(ClientboundPackets1_15.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_15.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_15.MULTI_BLOCK_CHANGE);
        ((AbstractProtocol<ClientboundPackets1_15, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_15.CHUNK_DATA, wrapper -> {
            final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_15Type());
            wrapper.write(new Chunk1_14Type(), chunk);
            if (chunk.isFullChunk()) {
                final int[] biomeData = chunk.getBiomeData();
                final int[] newBiomeData = new int[256];
                for (int i = 0; i < 4; ++i) {
                    for (int j = 0; j < 4; ++j) {
                        final int x = j << 2;
                        final int z = i << 2;
                        final int newIndex = z << 4 | x;
                        final int oldIndex = i << 2 | j;
                        final int biome = biomeData[oldIndex];
                        for (int k = 0; k < 4; ++k) {
                            final int offX = newIndex + (k << 4);
                            for (int l = 0; l < 4; ++l) {
                                newBiomeData[offX + l] = biome;
                            }
                        }
                    }
                }
                chunk.setBiomeData(newBiomeData);
            }
            for (int m = 0; m < chunk.getSections().length; ++m) {
                final ChunkSection section = chunk.getSections()[m];
                if (section != null) {
                    final DataPalette palette = section.palette(PaletteType.BLOCKS);
                    for (int j2 = 0; j2 < palette.size(); ++j2) {
                        final int mappedBlockStateId = ((Protocol1_14_4To1_15)this.protocol).getMappingData().getNewBlockStateId(palette.idByIndex(j2));
                        palette.setIdByIndex(j2, mappedBlockStateId);
                    }
                }
            }
            return;
        });
        blockRewriter.registerEffect(ClientboundPackets1_15.EFFECT, 1010, 2001);
        ((AbstractProtocol<ClientboundPackets1_15, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_15.SPAWN_PARTICLE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map(Type.DOUBLE, Type.FLOAT);
                this.map(Type.DOUBLE, Type.FLOAT);
                this.map(Type.DOUBLE, Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    if (id == 3 || id == 23) {
                        final int data = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                        wrapper.set(Type.VAR_INT, 0, ((Protocol1_14_4To1_15)BlockItemPackets1_15.this.protocol).getMappingData().getNewBlockStateId(data));
                    }
                    else if (id == 32) {
                        final Item item = BlockItemPackets1_15.this.handleItemToClient(wrapper.read(Type.FLAT_VAR_INT_ITEM));
                        wrapper.write(Type.FLAT_VAR_INT_ITEM, item);
                    }
                    final int mappedId = ((Protocol1_14_4To1_15)BlockItemPackets1_15.this.protocol).getMappingData().getNewParticleId(id);
                    if (id != mappedId) {
                        wrapper.set(Type.INT, 0, mappedId);
                    }
                });
            }
        });
    }
}
