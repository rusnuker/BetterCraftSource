// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_8;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.Protocol1_16_1To1_16_2;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_16_2 extends ItemRewriter<ClientboundPackets1_16_2, ServerboundPackets1_16, Protocol1_16_1To1_16_2>
{
    public BlockItemPackets1_16_2(final Protocol1_16_1To1_16_2 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final BlockRewriter<ClientboundPackets1_16_2> blockRewriter = new BlockRewriter<ClientboundPackets1_16_2>(this.protocol, Type.POSITION1_14);
        new RecipeRewriter<ClientboundPackets1_16_2>(this.protocol).register(ClientboundPackets1_16_2.DECLARE_RECIPES);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerSetCooldown(ClientboundPackets1_16_2.COOLDOWN);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerWindowItems(ClientboundPackets1_16_2.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerSetSlot(ClientboundPackets1_16_2.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_16_2.ENTITY_EQUIPMENT);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerTradeList(ClientboundPackets1_16_2.TRADE_LIST);
        ((ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerAdvancements(ClientboundPackets1_16_2.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16_2.UNLOCK_RECIPES, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.read((Type<Object>)Type.BOOLEAN);
            wrapper.read((Type<Object>)Type.BOOLEAN);
            wrapper.read((Type<Object>)Type.BOOLEAN);
            wrapper.read((Type<Object>)Type.BOOLEAN);
            return;
        });
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_16_2.ACKNOWLEDGE_PLAYER_DIGGING);
        blockRewriter.registerBlockAction(ClientboundPackets1_16_2.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_16_2.BLOCK_CHANGE);
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16_2.CHUNK_DATA, wrapper -> {
            final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_16_2Type());
            wrapper.write(new Chunk1_16Type(), chunk);
            chunk.setIgnoreOldLightData(true);
            for (int i = 0; i < chunk.getSections().length; ++i) {
                final ChunkSection section = chunk.getSections()[i];
                if (section != null) {
                    final DataPalette palette = section.palette(PaletteType.BLOCKS);
                    for (int j = 0; j < palette.size(); ++j) {
                        final int mappedBlockStateId = ((Protocol1_16_1To1_16_2)this.protocol).getMappingData().getNewBlockStateId(palette.idByIndex(j));
                        palette.setIdByIndex(j, mappedBlockStateId);
                    }
                }
            }
            chunk.getBlockEntities().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final CompoundTag blockEntity = iterator.next();
                if (blockEntity != null) {
                    this.handleBlockEntity(blockEntity);
                }
            }
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16_2.BLOCK_ENTITY_DATA, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION1_14);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(wrapper -> BlockItemPackets1_16_2.this.handleBlockEntity(wrapper.passthrough(Type.NBT)));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE, wrapper -> {
            final long chunkPosition = wrapper.read((Type<Long>)Type.LONG);
            wrapper.read((Type<Object>)Type.BOOLEAN);
            final int chunkX = (int)(chunkPosition >> 42);
            final int chunkY = (int)(chunkPosition << 44 >> 44);
            final int chunkZ = (int)(chunkPosition << 22 >> 42);
            wrapper.write(Type.INT, chunkX);
            wrapper.write(Type.INT, chunkZ);
            final BlockChangeRecord[] blockChangeRecord = wrapper.read(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY);
            wrapper.write(Type.BLOCK_CHANGE_RECORD_ARRAY, blockChangeRecord);
            for (int k = 0; k < blockChangeRecord.length; ++k) {
                final BlockChangeRecord record = blockChangeRecord[k];
                final int blockId = ((Protocol1_16_1To1_16_2)this.protocol).getMappingData().getNewBlockStateId(record.getBlockId());
                blockChangeRecord[k] = new BlockChangeRecord1_8(record.getSectionX(), record.getY(chunkY), record.getSectionZ(), blockId);
            }
            return;
        });
        blockRewriter.registerEffect(ClientboundPackets1_16_2.EFFECT, 1010, 2001);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16_2, S, T>)this).registerSpawnParticle(ClientboundPackets1_16_2.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_16, T>)this).registerClickWindow(ServerboundPackets1_16.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_16, T>)this).registerCreativeInvAction(ServerboundPackets1_16.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_16>)this.protocol).registerServerbound(ServerboundPackets1_16.EDIT_BOOK, wrapper -> this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
    }
    
    private void handleBlockEntity(final CompoundTag tag) {
        final StringTag idTag = tag.get("id");
        if (idTag == null) {
            return;
        }
        if (idTag.getValue().equals("minecraft:skull")) {
            final Tag skullOwnerTag = tag.get("SkullOwner");
            if (!(skullOwnerTag instanceof CompoundTag)) {
                return;
            }
            final CompoundTag skullOwnerCompoundTag = (CompoundTag)skullOwnerTag;
            if (!skullOwnerCompoundTag.contains("Id")) {
                return;
            }
            final CompoundTag properties = skullOwnerCompoundTag.get("Properties");
            if (properties == null) {
                return;
            }
            final ListTag textures = properties.get("textures");
            if (textures == null) {
                return;
            }
            final CompoundTag first = (textures.size() > 0) ? textures.get(0) : null;
            if (first == null) {
                return;
            }
            final int hashCode = first.get("Value").getValue().hashCode();
            final int[] uuidIntArray = { hashCode, 0, 0, 0 };
            skullOwnerCompoundTag.put("Id", new IntArrayTag(uuidIntArray));
        }
    }
}
