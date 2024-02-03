// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
import com.viaversion.viaversion.util.MathUtil;
import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.Protocol1_18_2To1_19;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public final class BlockItemPackets1_19 extends ItemRewriter<ClientboundPackets1_19, ServerboundPackets1_17, Protocol1_18_2To1_19>
{
    public BlockItemPackets1_19(final Protocol1_18_2To1_19 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final BlockRewriter<ClientboundPackets1_19> blockRewriter = new BlockRewriter<ClientboundPackets1_19>(this.protocol, Type.POSITION1_14);
        new RecipeRewriter<ClientboundPackets1_19>(this.protocol).register(ClientboundPackets1_19.DECLARE_RECIPES);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19, S, T>)this).registerSetCooldown(ClientboundPackets1_19.COOLDOWN);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19, S, T>)this).registerWindowItems1_17_1(ClientboundPackets1_19.WINDOW_ITEMS);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19, S, T>)this).registerSetSlot1_17_1(ClientboundPackets1_19.SET_SLOT);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_19.ENTITY_EQUIPMENT);
        ((ItemRewriter<ClientboundPackets1_19, S, T>)this).registerAdvancements(ClientboundPackets1_19.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_17, T>)this).registerClickWindow1_17_1(ServerboundPackets1_17.CLICK_WINDOW);
        blockRewriter.registerBlockAction(ClientboundPackets1_19.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_19.BLOCK_CHANGE);
        blockRewriter.registerVarLongMultiBlockChange(ClientboundPackets1_19.MULTI_BLOCK_CHANGE);
        blockRewriter.registerEffect(ClientboundPackets1_19.EFFECT, 1010, 2001);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_17, T>)this).registerCreativeInvAction(ServerboundPackets1_17.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_19, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19.TRADE_LIST, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int size = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.UNSIGNED_BYTE, (short)size);
                    for (int i = 0; i < size; ++i) {
                        BlockItemPackets1_19.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        BlockItemPackets1_19.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        final Item secondItem = wrapper.read(Type.FLAT_VAR_INT_ITEM);
                        if (secondItem != null) {
                            BlockItemPackets1_19.this.handleItemToClient(secondItem);
                            wrapper.write(Type.BOOLEAN, true);
                            wrapper.write(Type.FLAT_VAR_INT_ITEM, secondItem);
                        }
                        else {
                            wrapper.write(Type.BOOLEAN, false);
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
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19, S, T>)this).registerWindowPropertyEnchantmentHandler(ClientboundPackets1_19.WINDOW_PROPERTY);
        this.protocol.registerClientbound(ClientboundPackets1_19.BLOCK_CHANGED_ACK, null, new PacketHandlers() {
            public void register() {
                this.read(Type.VAR_INT);
                this.handler(PacketWrapper::cancel);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19.SPAWN_PARTICLE, new PacketHandlers() {
            public void register() {
                this.map(Type.VAR_INT, Type.INT);
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
                    final ParticleMappings particleMappings = ((Protocol1_18_2To1_19)BlockItemPackets1_19.this.protocol).getMappingData().getParticleMappings();
                    if (id == particleMappings.id("sculk_charge")) {
                        wrapper.set(Type.INT, 0, -1);
                        wrapper.cancel();
                    }
                    else if (id == particleMappings.id("shriek")) {
                        wrapper.set(Type.INT, 0, -1);
                        wrapper.cancel();
                    }
                    else if (id == particleMappings.id("vibration")) {
                        wrapper.set(Type.INT, 0, -1);
                        wrapper.cancel();
                    }
                    return;
                });
                this.handler(BlockItemPackets1_19.this.getSpawnParticleHandler(Type.FLAT_VAR_INT_ITEM));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19.CHUNK_DATA, wrapper -> {
            final EntityTracker tracker = ((Protocol1_18_2To1_19)this.protocol).getEntityRewriter().tracker(wrapper.user());
            final Chunk1_18Type chunkType = new Chunk1_18Type(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(((Protocol1_18_2To1_19)this.protocol).getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent()));
            final Chunk chunk = wrapper.passthrough((Type<Chunk>)chunkType);
            chunk.getSections();
            final ChunkSection[] array;
            int j = 0;
            for (int length = array.length; j < length; ++j) {
                final ChunkSection section = array[j];
                final DataPalette blockPalette = section.palette(PaletteType.BLOCKS);
                for (int i = 0; i < blockPalette.size(); ++i) {
                    final int id = blockPalette.idByIndex(i);
                    blockPalette.setIdByIndex(i, ((Protocol1_18_2To1_19)this.protocol).getMappingData().getNewBlockStateId(id));
                }
            }
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this.protocol).registerServerbound(ServerboundPackets1_17.PLAYER_DIGGING, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.POSITION1_14);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.create(Type.VAR_INT, 0);
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this.protocol).registerServerbound(ServerboundPackets1_17.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.POSITION1_14);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.create(Type.VAR_INT, 0);
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this.protocol).registerServerbound(ServerboundPackets1_17.USE_ITEM, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.create(Type.VAR_INT, 0);
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this.protocol).registerServerbound(ServerboundPackets1_17.SET_BEACON_EFFECT, wrapper -> {
            final int primaryEffect = wrapper.read((Type<Integer>)Type.VAR_INT);
            if (primaryEffect != -1) {
                wrapper.write(Type.BOOLEAN, true);
                wrapper.write(Type.VAR_INT, primaryEffect);
            }
            else {
                wrapper.write(Type.BOOLEAN, false);
            }
            final int secondaryEffect = wrapper.read((Type<Integer>)Type.VAR_INT);
            if (secondaryEffect != -1) {
                wrapper.write(Type.BOOLEAN, true);
                wrapper.write(Type.VAR_INT, secondaryEffect);
            }
            else {
                wrapper.write(Type.BOOLEAN, false);
            }
        });
    }
}
