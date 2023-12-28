// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.type.PartialType;
import java.util.Optional;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.CustomByteType;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.types.Chunk1_9to1_8Type;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.sounds.SoundEffect;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.sounds.Effect;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.api.protocol.Protocol;

public class WorldPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerClientbound(ClientboundPackets1_8.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        int id = wrapper.get((Type<Integer>)Type.INT, 0);
                        id = Effect.getNewId(id);
                        wrapper.set(Type.INT, 0, id);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                        if (id == 2002) {
                            final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                            final int newData = ItemRewriter.getNewEffectID(data);
                            wrapper.set(Type.INT, 1, newData);
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
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
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientChunks clientChunks = wrapper.user().get(ClientChunks.class);
                        final Chunk1_9to1_8Type type = new Chunk1_9to1_8Type(clientChunks);
                        final Chunk1_8 chunk = wrapper.read((Type<Chunk1_8>)type);
                        if (chunk.isUnloadPacket()) {
                            wrapper.setId(ClientboundPackets1_9.UNLOAD_CHUNK);
                            wrapper.write(Type.INT, chunk.getX());
                            wrapper.write(Type.INT, chunk.getZ());
                            final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                            provider.unloadChunk(wrapper.user(), chunk.getX(), chunk.getZ());
                        }
                        else {
                            wrapper.write((Type<Chunk1_8>)type, chunk);
                        }
                        wrapper.read(Type.REMAINING_BYTES);
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.MAP_BULK_CHUNK, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.cancel();
                    final boolean skyLight = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    final int count = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final ChunkBulkSection[] chunks = new ChunkBulkSection[count];
                    for (int i = 0; i < count; ++i) {
                        chunks[i] = new ChunkBulkSection(wrapper, skyLight);
                    }
                    final ClientChunks clientChunks = wrapper.user().get(ClientChunks.class);
                    final ChunkBulkSection[] array;
                    int j = 0;
                    for (int length = array.length; j < length; ++j) {
                        final ChunkBulkSection chunk = array[j];
                        final CustomByteType customByteType = new CustomByteType(chunk.getLength());
                        chunk.setData(wrapper.read((Type<byte[]>)customByteType));
                        clientChunks.getBulkChunks().add(ClientChunks.toLong(chunk.getX(), chunk.getZ()));
                        ByteBuf buffer = null;
                        try {
                            buffer = wrapper.user().getChannel().alloc().buffer();
                            Type.INT.write(buffer, chunk.getX());
                            Type.INT.write(buffer, chunk.getZ());
                            Type.BOOLEAN.write(buffer, true);
                            Type.UNSIGNED_SHORT.write(buffer, chunk.getBitMask());
                            Type.VAR_INT.writePrimitive(buffer, chunk.getLength());
                            ((PartialType<byte[], X>)customByteType).write(buffer, chunk.getData());
                            final PacketWrapper chunkPacket = PacketWrapper.create(ClientboundPackets1_8.CHUNK_DATA, buffer, wrapper.user());
                            chunkPacket.send(Protocol1_9To1_8.class, false);
                        }
                        finally {
                            if (buffer != null) {
                                buffer.release();
                            }
                        }
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
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
                    }
                });
            }
        });
        protocol.registerClientbound(ClientboundPackets1_8.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.PLAYER_DIGGING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.map(Type.POSITION);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int status = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                        if (status == 6) {
                            wrapper.cancel();
                        }
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int status = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                        if (status == 5 || status == 4 || status == 3) {
                            final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                            if (entityTracker.isBlocking()) {
                                entityTracker.setBlocking(false);
                                if (!Via.getConfig().isShowShieldWhenSwordInHand()) {
                                    entityTracker.setSecondHand(null);
                                }
                            }
                        }
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.USE_ITEM, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        wrapper.clearInputBuffer();
                        wrapper.setId(8);
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
                    }
                });
            }
        });
        protocol.registerServerbound(ServerboundPackets1_9.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        if (hand != 0) {
                            wrapper.cancel();
                        }
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item item = Protocol1_9To1_8.getHandItem(wrapper.user());
                        wrapper.write(Type.ITEM, item);
                    }
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int face = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                        if (face == 255) {
                            return;
                        }
                        final Position p = wrapper.get(Type.POSITION, 0);
                        int x = p.getX();
                        int y = p.getY();
                        int z = p.getZ();
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
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                        final Position pos = wrapper.get(Type.POSITION, 0);
                        final Optional<CompoundTag> tag = provider.get(wrapper.user(), pos);
                        if (tag.isPresent()) {
                            final PacketWrapper updateBlockEntity = PacketWrapper.create(9, null, wrapper.user());
                            updateBlockEntity.write(Type.POSITION, pos);
                            updateBlockEntity.write(Type.UNSIGNED_BYTE, (Short)2);
                            updateBlockEntity.write(Type.NBT, tag.get());
                            updateBlockEntity.scheduleSend(Protocol1_9To1_8.class);
                        }
                    }
                });
            }
        });
    }
    
    public static final class ChunkBulkSection
    {
        private final int x;
        private final int z;
        private final int bitMask;
        private final int length;
        private byte[] data;
        
        public ChunkBulkSection(final PacketWrapper wrapper, final boolean skylight) throws Exception {
            this.x = wrapper.read((Type<Integer>)Type.INT);
            this.z = wrapper.read((Type<Integer>)Type.INT);
            this.bitMask = wrapper.read((Type<Integer>)Type.UNSIGNED_SHORT);
            final int bitCount = Integer.bitCount(this.bitMask);
            this.length = bitCount * 10240 + (skylight ? (bitCount * 2048) : 0) + 256;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getZ() {
            return this.z;
        }
        
        public int getBitMask() {
            return this.bitMask;
        }
        
        public int getLength() {
            return this.length;
        }
        
        public byte[] getData() {
            return this.data;
        }
        
        public void setData(final byte[] data) {
            this.data = data;
        }
    }
}
