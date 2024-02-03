// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.Map;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.api.type.types.UUIDIntArrayType;
import java.util.UUID;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.util.CompactArrayUtil;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;

public class WorldPackets
{
    public static void register(final Protocol1_16To1_15_2 protocol) {
        final BlockRewriter<ClientboundPackets1_15> blockRewriter = new BlockRewriter<ClientboundPackets1_15>(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_15.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_15.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_15.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_15.ACKNOWLEDGE_PLAYER_DIGGING);
        ((AbstractProtocol<ClientboundPackets1_15, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_15.UPDATE_LIGHT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> wrapper.write(Type.BOOLEAN, true));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_15.CHUNK_DATA, wrapper -> {
            final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_15Type());
            wrapper.write(new Chunk1_16Type(), chunk);
            chunk.setIgnoreOldLightData(chunk.isFullChunk());
            int i = 0;
            for (int s = 0; s < chunk.getSections().length; ++s) {
                final ChunkSection section = chunk.getSections()[s];
                if (section != null) {
                    DataPalette palette;
                    int mappedBlockStateId;
                    for (palette = section.palette(PaletteType.BLOCKS), i = 0; i < palette.size(); ++i) {
                        mappedBlockStateId = protocol.getMappingData().getNewBlockStateId(palette.idByIndex(i));
                        palette.setIdByIndex(i, mappedBlockStateId);
                    }
                }
            }
            final CompoundTag heightMaps = chunk.getHeightMap();
            heightMaps.values().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final Tag heightMapTag = iterator.next();
                final LongArrayTag heightMap = (LongArrayTag)heightMapTag;
                final int[] heightMapData = new int[256];
                CompactArrayUtil.iterateCompactArray(9, heightMapData.length, heightMap.getValue(), (i, v) -> heightMapData[i] = v);
                heightMap.setValue(CompactArrayUtil.createCompactArrayWithPadding(9, heightMapData.length, i -> heightMapData[i]));
            }
            if (chunk.getBlockEntities() == null) {
                return;
            }
            else {
                chunk.getBlockEntities().iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final CompoundTag blockEntity = iterator2.next();
                    handleBlockEntity(protocol, blockEntity);
                }
                return;
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_15.BLOCK_ENTITY_DATA, wrapper -> {
            wrapper.passthrough(Type.POSITION1_14);
            wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
            final CompoundTag tag = wrapper.passthrough(Type.NBT);
            handleBlockEntity(protocol, tag);
            return;
        });
        blockRewriter.registerEffect(ClientboundPackets1_15.EFFECT, 1010, 2001);
    }
    
    private static void handleBlockEntity(final Protocol1_16To1_15_2 protocol, final CompoundTag compoundTag) {
        final StringTag idTag = compoundTag.get("id");
        if (idTag == null) {
            return;
        }
        final String id = idTag.getValue();
        if (id.equals("minecraft:conduit")) {
            final Tag targetUuidTag = compoundTag.remove("target_uuid");
            if (!(targetUuidTag instanceof StringTag)) {
                return;
            }
            final UUID targetUuid = UUID.fromString((String)targetUuidTag.getValue());
            compoundTag.put("Target", new IntArrayTag(UUIDIntArrayType.uuidToIntArray(targetUuid)));
        }
        else if (id.equals("minecraft:skull") && compoundTag.get("Owner") instanceof CompoundTag) {
            final CompoundTag ownerTag = compoundTag.remove("Owner");
            final StringTag ownerUuidTag = ownerTag.remove("Id");
            if (ownerUuidTag != null) {
                final UUID ownerUuid = UUID.fromString(ownerUuidTag.getValue());
                ownerTag.put("Id", new IntArrayTag(UUIDIntArrayType.uuidToIntArray(ownerUuid)));
            }
            final CompoundTag skullOwnerTag = new CompoundTag();
            for (final Map.Entry<String, Tag> entry : ownerTag.entrySet()) {
                skullOwnerTag.put(entry.getKey(), entry.getValue());
            }
            compoundTag.put("SkullOwner", skullOwnerTag);
        }
        else if (id.equals("minecraft:sign")) {
            for (int i = 1; i <= 4; ++i) {
                final Tag line = compoundTag.get("Text" + i);
                if (line instanceof StringTag) {
                    final JsonElement text = protocol.getComponentRewriter().processText(((StringTag)line).getValue());
                    compoundTag.put("Text" + i, new StringTag(text.toString()));
                }
            }
        }
        else if (id.equals("minecraft:mob_spawner")) {
            final Tag spawnDataTag = compoundTag.get("SpawnData");
            if (spawnDataTag instanceof CompoundTag) {
                final Tag spawnDataIdTag = ((CompoundTag)spawnDataTag).get("id");
                if (spawnDataIdTag instanceof StringTag) {
                    final StringTag spawnDataIdStringTag = (StringTag)spawnDataIdTag;
                    if (spawnDataIdStringTag.getValue().equals("minecraft:zombie_pigman")) {
                        spawnDataIdStringTag.setValue("minecraft:zombified_piglin");
                    }
                }
            }
        }
    }
}
