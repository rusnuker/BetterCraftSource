// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntityImpl;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.List;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.util.MathUtil;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
import java.util.function.Consumer;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class BlockRewriter<C extends ClientboundPacketType>
{
    private final Protocol<C, ?, ?, ?> protocol;
    private final Type<Position> positionType;
    
    public BlockRewriter(final Protocol<C, ?, ?, ?> protocol, final Type<Position> positionType) {
        this.protocol = protocol;
        this.positionType = positionType;
    }
    
    public void registerBlockAction(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)BlockRewriter.this.positionType);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    if (BlockRewriter.this.protocol.getMappingData().getBlockMappings() != null) {
                        final int id = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final int mappedId = BlockRewriter.this.protocol.getMappingData().getNewBlockId(id);
                        if (mappedId == -1) {
                            wrapper.cancel();
                        }
                        else {
                            wrapper.set(Type.VAR_INT, 0, mappedId);
                        }
                    }
                });
            }
        });
    }
    
    public void registerBlockChange(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)BlockRewriter.this.positionType);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> wrapper.set(Type.VAR_INT, 0, BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(wrapper.get((Type<Integer>)Type.VAR_INT, 0))));
            }
        });
    }
    
    public void registerMultiBlockChange(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final BlockChangeRecord[] array = wrapper.passthrough(Type.BLOCK_CHANGE_RECORD_ARRAY);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        record.setBlockId(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(record.getBlockId()));
                    }
                });
            }
        });
    }
    
    public void registerVarLongMultiBlockChange(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.LONG);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(wrapper -> {
                    final BlockChangeRecord[] array = wrapper.passthrough(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        record.setBlockId(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(record.getBlockId()));
                    }
                });
            }
        });
    }
    
    public void registerVarLongMultiBlockChange1_20(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.LONG);
                this.handler(wrapper -> {
                    final BlockChangeRecord[] array = wrapper.passthrough(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        record.setBlockId(BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(record.getBlockId()));
                    }
                });
            }
        });
    }
    
    public void registerAcknowledgePlayerDigging(final C packetType) {
        this.registerBlockChange(packetType);
    }
    
    public void registerEffect(final C packetType, final int playRecordId, final int blockBreakId) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)BlockRewriter.this.positionType);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final Object val$playRecordId = playRecordId;
                    final Object val$blockBreakId = blockBreakId;
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                    if (id == playRecordId) {
                        wrapper.set(Type.INT, 1, BlockRewriter.this.protocol.getMappingData().getNewItemId(data));
                    }
                    else if (id == blockBreakId) {
                        wrapper.set(Type.INT, 1, BlockRewriter.this.protocol.getMappingData().getNewBlockStateId(data));
                    }
                });
            }
        });
    }
    
    public void registerChunkData1_19(final C packetType, final ChunkTypeSupplier chunkTypeSupplier) {
        this.registerChunkData1_19(packetType, chunkTypeSupplier, null);
    }
    
    public void registerChunkData1_19(final C packetType, final ChunkTypeSupplier chunkTypeSupplier, final Consumer<BlockEntity> blockEntityHandler) {
        this.protocol.registerClientbound(packetType, this.chunkDataHandler1_19(chunkTypeSupplier, blockEntityHandler));
    }
    
    public PacketHandler chunkDataHandler1_19(final ChunkTypeSupplier chunkTypeSupplier, final Consumer<BlockEntity> blockEntityHandler) {
        return wrapper -> {
            final EntityTracker tracker = this.protocol.getEntityRewriter().tracker(wrapper.user());
            Preconditions.checkArgument(tracker.biomesSent() != 0, (Object)"Biome count not set");
            Preconditions.checkArgument(tracker.currentWorldSectionHeight() != 0, (Object)"Section height not set");
            final Type<Chunk> chunkType = chunkTypeSupplier.supply(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(this.protocol.getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent()));
            final Chunk chunk = wrapper.passthrough(chunkType);
            chunk.getSections();
            final ChunkSection[] array;
            int k = 0;
            for (int length = array.length; k < length; ++k) {
                final ChunkSection section = array[k];
                final DataPalette blockPalette = section.palette(PaletteType.BLOCKS);
                for (int i = 0; i < blockPalette.size(); ++i) {
                    final int id = blockPalette.idByIndex(i);
                    blockPalette.setIdByIndex(i, this.protocol.getMappingData().getNewBlockStateId(id));
                }
            }
            final Mappings blockEntityMappings = this.protocol.getMappingData().getBlockEntityMappings();
            if (blockEntityMappings != null || blockEntityHandler != null) {
                final List<BlockEntity> blockEntities = chunk.blockEntities();
                for (int j = 0; j < blockEntities.size(); ++j) {
                    final BlockEntity blockEntity = blockEntities.get(j);
                    if (blockEntityMappings != null) {
                        blockEntities.set(j, blockEntity.withTypeId(blockEntityMappings.getNewIdOrDefault(blockEntity.typeId(), blockEntity.typeId())));
                    }
                    if (blockEntityHandler != null && blockEntity.tag() != null) {
                        blockEntityHandler.accept(blockEntity);
                    }
                }
            }
        };
    }
    
    public void registerBlockEntityData(final C packetType) {
        this.registerBlockEntityData(packetType, null);
    }
    
    public void registerBlockEntityData(final C packetType, final Consumer<BlockEntity> blockEntityHandler) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            final Position position = wrapper.passthrough(Type.POSITION1_14);
            final int blockEntityId = wrapper.read((Type<Integer>)Type.VAR_INT);
            final Mappings mappings = this.protocol.getMappingData().getBlockEntityMappings();
            if (mappings != null) {
                wrapper.write(Type.VAR_INT, mappings.getNewIdOrDefault(blockEntityId, blockEntityId));
            }
            else {
                wrapper.write(Type.VAR_INT, blockEntityId);
            }
            final CompoundTag tag;
            if (blockEntityHandler != null && (tag = wrapper.passthrough(Type.NBT)) != null) {
                final BlockEntity blockEntity = new BlockEntityImpl(BlockEntity.pack(position.x(), position.z()), (short)position.y(), blockEntityId, tag);
                blockEntityHandler.accept(blockEntity);
            }
        });
    }
    
    @FunctionalInterface
    public interface ChunkTypeSupplier
    {
        Type<Chunk> supply(final int p0, final int p1, final int p2);
    }
}
