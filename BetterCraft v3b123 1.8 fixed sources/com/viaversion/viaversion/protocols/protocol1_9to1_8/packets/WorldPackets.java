// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.Optional;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.types.ChunkBulk1_8Type;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.types.Chunk1_9_1_2Type;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.types.Chunk1_8Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.sounds.SoundEffect;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.sounds.Effect;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;

public class WorldPackets
{
    public static void register(final Protocol1_9To1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.UPDATE_SIGN, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.EFFECT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map(Type.POSITION);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int id2 = Effect.getNewId(id);
                    wrapper.set(Type.INT, 0, id2);
                    return;
                });
                this.handler(wrapper -> {
                    final int id3 = wrapper.get((Type<Integer>)Type.INT, 0);
                    if (id3 == 2002) {
                        final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                        final int newData = ItemRewriter.getNewEffectID(data);
                        wrapper.set(Type.INT, 1, newData);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.NAMED_SOUND, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final String name = wrapper.get(Type.STRING, 0);
                    final SoundEffect effect = SoundEffect.getByName(name);
                    int catid = 0;
                    String newname = name;
                    if (effect != null) {
                        catid = effect.getCategory().getId();
                        newname = effect.getNewName();
                    }
                    wrapper.set(Type.STRING, 0, newname);
                    wrapper.write(Type.VAR_INT, catid);
                    if (effect != null && effect.isBreaksound()) {
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        final int x = wrapper.passthrough((Type<Integer>)Type.INT);
                        final int y = wrapper.passthrough((Type<Integer>)Type.INT);
                        final int z = wrapper.passthrough((Type<Integer>)Type.INT);
                        if (tracker.interactedBlockRecently((int)Math.floor(x / 8.0), (int)Math.floor(y / 8.0), (int)Math.floor(z / 8.0))) {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.CHUNK_DATA, wrapper -> {
            final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
            final ClientChunks clientChunks = wrapper.user().get(ClientChunks.class);
            final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_8Type(clientWorld));
            final long chunkHash = ClientChunks.toLong(chunk.getX(), chunk.getZ());
            if (chunk.isFullChunk() && chunk.getBitmask() == 0) {
                wrapper.setPacketType(ClientboundPackets1_9.UNLOAD_CHUNK);
                wrapper.write(Type.INT, chunk.getX());
                wrapper.write(Type.INT, chunk.getZ());
                final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                provider.unloadChunk(wrapper.user(), chunk.getX(), chunk.getZ());
                clientChunks.getLoadedChunks().remove(chunkHash);
                if (Via.getConfig().isChunkBorderFix()) {
                    final BlockFace[] horizontal = BlockFace.HORIZONTAL;
                    int i = 0;
                    for (int length = horizontal.length; i < length; ++i) {
                        final BlockFace face = horizontal[i];
                        final int chunkX = chunk.getX() + face.modX();
                        final int chunkZ = chunk.getZ() + face.modZ();
                        if (!clientChunks.getLoadedChunks().contains(ClientChunks.toLong(chunkX, chunkZ))) {
                            final PacketWrapper unloadChunk = wrapper.create(ClientboundPackets1_9.UNLOAD_CHUNK);
                            unloadChunk.write(Type.INT, chunkX);
                            unloadChunk.write(Type.INT, chunkZ);
                            unloadChunk.send(Protocol1_9To1_8.class);
                        }
                    }
                }
            }
            else {
                final Chunk1_9_1_2Type chunkType = new Chunk1_9_1_2Type(clientWorld);
                wrapper.write((Type<Chunk>)chunkType, chunk);
                clientChunks.getLoadedChunks().add(chunkHash);
                if (Via.getConfig().isChunkBorderFix()) {
                    final BlockFace[] horizontal2 = BlockFace.HORIZONTAL;
                    int j = 0;
                    for (int length2 = horizontal2.length; j < length2; ++j) {
                        final BlockFace face2 = horizontal2[j];
                        final int chunkX2 = chunk.getX() + face2.modX();
                        final int chunkZ2 = chunk.getZ() + face2.modZ();
                        if (!clientChunks.getLoadedChunks().contains(ClientChunks.toLong(chunkX2, chunkZ2))) {
                            final PacketWrapper emptyChunk = wrapper.create(ClientboundPackets1_9.CHUNK_DATA);
                            new BaseChunk(chunkX2, chunkZ2, true, false, 0, new ChunkSection[16], new int[256], new ArrayList<CompoundTag>());
                            final BaseChunk baseChunk;
                            final Chunk c = baseChunk;
                            emptyChunk.write((Type<Chunk>)chunkType, c);
                            emptyChunk.send(Protocol1_9To1_8.class);
                        }
                    }
                }
            }
            return;
        });
        ((Protocol<ClientboundPackets1_8, ClientboundPackets1_9, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.MAP_BULK_CHUNK, null, wrapper -> {
            wrapper.cancel();
            final ClientWorld clientWorld2 = wrapper.user().get(ClientWorld.class);
            final ClientChunks clientChunks2 = wrapper.user().get(ClientChunks.class);
            final Chunk[] chunks = wrapper.read((Type<Chunk[]>)new ChunkBulk1_8Type(clientWorld2));
            final Chunk1_9_1_2Type chunkType2 = new Chunk1_9_1_2Type(clientWorld2);
            final Chunk[] array;
            int k = 0;
            for (int length3 = array.length; k < length3; ++k) {
                final Chunk chunk2 = array[k];
                final PacketWrapper chunkData = wrapper.create(ClientboundPackets1_9.CHUNK_DATA);
                chunkData.write((Type<Chunk>)chunkType2, chunk2);
                chunkData.send(Protocol1_9To1_8.class);
                clientChunks2.getLoadedChunks().add(ClientChunks.toLong(chunk2.getX(), chunk2.getZ()));
                if (Via.getConfig().isChunkBorderFix()) {
                    final BlockFace[] horizontal3 = BlockFace.HORIZONTAL;
                    int l = 0;
                    for (int length4 = horizontal3.length; l < length4; ++l) {
                        final BlockFace face3 = horizontal3[l];
                        final int chunkX3 = chunk2.getX() + face3.modX();
                        final int chunkZ3 = chunk2.getZ() + face3.modZ();
                        if (!clientChunks2.getLoadedChunks().contains(ClientChunks.toLong(chunkX3, chunkZ3))) {
                            final PacketWrapper emptyChunk2 = wrapper.create(ClientboundPackets1_9.CHUNK_DATA);
                            new BaseChunk(chunkX3, chunkZ3, true, false, 0, new ChunkSection[16], new int[256], new ArrayList<CompoundTag>());
                            final BaseChunk baseChunk2;
                            final Chunk c2 = baseChunk2;
                            emptyChunk2.write((Type<Chunk>)chunkType2, c2);
                            emptyChunk2.send(Protocol1_9To1_8.class);
                        }
                    }
                }
            }
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.BLOCK_ENTITY_DATA, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(wrapper -> {
                    final int action = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (action == 1) {
                        final CompoundTag tag = wrapper.get(Type.NBT, 0);
                        if (tag != null) {
                            if (tag.contains("EntityId")) {
                                final String entity = (String)tag.get("EntityId").getValue();
                                final CompoundTag spawn = new CompoundTag();
                                spawn.put("id", new StringTag(entity));
                                tag.put("SpawnData", spawn);
                            }
                            else {
                                final CompoundTag spawn2 = new CompoundTag();
                                spawn2.put("id", new StringTag("AreaEffectCloud"));
                                tag.put("SpawnData", spawn2);
                            }
                        }
                    }
                    if (action == 2) {
                        final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                        provider.addOrUpdateBlock(wrapper.user(), wrapper.get(Type.POSITION, 0), wrapper.get(Type.NBT, 0));
                        wrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.UPDATE_SIGN, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.PLAYER_DIGGING, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.POSITION);
                this.handler(wrapper -> {
                    final int status = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (status == 6) {
                        wrapper.cancel();
                    }
                    return;
                });
                this.handler(wrapper -> {
                    final int status2 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (status2 == 5 || status2 == 4 || status2 == 3) {
                        final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        if (entityTracker.isBlocking()) {
                            entityTracker.setBlocking(false);
                            if (!Via.getConfig().isShowShieldWhenSwordInHand()) {
                                entityTracker.setSecondHand(null);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<CU, CM, ServerboundPackets1_8, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.USE_ITEM, null, wrapper -> {
            final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
            wrapper.clearInputBuffer();
            wrapper.setPacketType(ServerboundPackets1_8.PLAYER_BLOCK_PLACEMENT);
            wrapper.write(Type.POSITION, new Position(-1, (short)(-1), -1));
            wrapper.write(Type.UNSIGNED_BYTE, (Short)255);
            final Item item = Protocol1_9To1_8.getHandItem(wrapper.user());
            if (Via.getConfig().isShieldBlocking()) {
                final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                final boolean showShieldWhenSwordInHand = Via.getConfig().isShowShieldWhenSwordInHand();
                final boolean isSword = showShieldWhenSwordInHand ? tracker.hasSwordInHand() : (item != null && Protocol1_9To1_8.isSword(item.identifier()));
                if (isSword) {
                    if (hand == 0 && !tracker.isBlocking()) {
                        tracker.setBlocking(true);
                        if (!showShieldWhenSwordInHand && tracker.getItemInSecondHand() == null) {
                            final Item shield = new DataItem(442, (byte)1, (short)0, null);
                            tracker.setSecondHand(shield);
                        }
                    }
                    final boolean blockUsingMainHand = Via.getConfig().isNoDelayShieldBlocking() && !showShieldWhenSwordInHand;
                    if ((blockUsingMainHand && hand == 1) || (!blockUsingMainHand && hand == 0)) {
                        wrapper.cancel();
                    }
                }
                else {
                    if (!showShieldWhenSwordInHand) {
                        tracker.setSecondHand(null);
                    }
                    tracker.setBlocking(false);
                }
            }
            wrapper.write(Type.ITEM, item);
            wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
            wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
            wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.PLAYER_BLOCK_PLACEMENT, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                    if (hand != 0) {
                        wrapper.cancel();
                    }
                    return;
                });
                this.handler(wrapper -> {
                    final Item item = Protocol1_9To1_8.getHandItem(wrapper.user());
                    wrapper.write(Type.ITEM, item);
                    return;
                });
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final int face = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    if (face == 255) {
                        return;
                    }
                    else {
                        final Position p = wrapper.get(Type.POSITION, 0);
                        int x = p.x();
                        int y = p.y();
                        int z = p.z();
                        switch (face) {
                            case 0: {
                                --y;
                                break;
                            }
                            case 1: {
                                ++y;
                                break;
                            }
                            case 2: {
                                --z;
                                break;
                            }
                            case 3: {
                                ++z;
                                break;
                            }
                            case 4: {
                                --x;
                                break;
                            }
                            case 5: {
                                ++x;
                                break;
                            }
                        }
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        tracker.addBlockInteraction(new Position(x, y, z));
                        return;
                    }
                });
                this.handler(wrapper -> {
                    final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                    final Position pos = wrapper.get(Type.POSITION, 0);
                    final Optional<CompoundTag> tag = provider.get(wrapper.user(), pos);
                    if (tag.isPresent()) {
                        final PacketWrapper updateBlockEntity = PacketWrapper.create(ClientboundPackets1_9.BLOCK_ENTITY_DATA, null, wrapper.user());
                        updateBlockEntity.write(Type.POSITION, pos);
                        updateBlockEntity.write(Type.UNSIGNED_BYTE, (Short)2);
                        updateBlockEntity.write(Type.NBT, tag.get());
                        updateBlockEntity.scheduleSend(Protocol1_9To1_8.class);
                    }
                });
            }
        });
    }
}
