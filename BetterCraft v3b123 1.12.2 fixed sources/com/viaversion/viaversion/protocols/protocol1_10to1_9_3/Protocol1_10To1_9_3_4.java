// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_10to1_9_3;

import java.util.Iterator;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.protocols.protocol1_10to1_9_3.storage.ResourcePackTracker;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_10to1_9_3.packets.InventoryPackets;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_10To1_9_3_4 extends AbstractProtocol<ClientboundPackets1_9_3, ClientboundPackets1_9_3, ServerboundPackets1_9_3, ServerboundPackets1_9_3>
{
    public static final ValueTransformer<Short, Float> TO_NEW_PITCH;
    public static final ValueTransformer<List<Metadata>, List<Metadata>> TRANSFORM_METADATA;
    private final InventoryPackets itemRewriter;
    
    public Protocol1_10To1_9_3_4() {
        super(ClientboundPackets1_9_3.class, ClientboundPackets1_9_3.class, ServerboundPackets1_9_3.class, ServerboundPackets1_9_3.class);
        this.itemRewriter = new InventoryPackets(this);
    }
    
    @Override
    protected void registerPackets() {
        this.itemRewriter.register();
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.NAMED_SOUND, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE, Protocol1_10To1_9_3_4.TO_NEW_PITCH);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.SOUND, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE, Protocol1_10To1_9_3_4.TO_NEW_PITCH);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    wrapper.set(Type.VAR_INT, 0, Protocol1_10To1_9_3_4.this.getNewSoundId(id));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.ENTITY_METADATA, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Types1_9.METADATA_LIST, Protocol1_10To1_9_3_4.TRANSFORM_METADATA);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.SPAWN_MOB, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map(Types1_9.METADATA_LIST, Protocol1_10To1_9_3_4.TRANSFORM_METADATA);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Types1_9.METADATA_LIST, Protocol1_10To1_9_3_4.TRANSFORM_METADATA);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                    final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 1);
                    clientWorld.setEnvironment(dimensionId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.RESPAWN, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                    final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 0);
                    clientWorld.setEnvironment(dimensionId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.CHUNK_DATA, wrapper -> {
            final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
            final Chunk chunk = wrapper.passthrough((Type<Chunk>)new Chunk1_9_3_4Type(clientWorld));
            if (Via.getConfig().isReplacePistons()) {
                final int replacementId = Via.getConfig().getPistonReplacementId();
                chunk.getSections();
                final ChunkSection[] array;
                int i = 0;
                for (int length = array.length; i < length; ++i) {
                    final ChunkSection section = array[i];
                    if (section != null) {
                        section.palette(PaletteType.BLOCKS).replaceId(36, replacementId);
                    }
                }
            }
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.RESOURCE_PACK, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final ResourcePackTracker tracker = wrapper.user().get(ResourcePackTracker.class);
                    tracker.setLastHash(wrapper.get(Type.STRING, 1));
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9_3>)this).registerServerbound(ServerboundPackets1_9_3.RESOURCE_PACK_STATUS, new PacketHandlers() {
            public void register() {
                this.handler(wrapper -> {
                    final ResourcePackTracker tracker = wrapper.user().get(ResourcePackTracker.class);
                    wrapper.write(Type.STRING, tracker.getLastHash());
                    wrapper.write((Type<Object>)Type.VAR_INT, wrapper.read((Type<T>)Type.VAR_INT));
                });
            }
        });
    }
    
    public int getNewSoundId(final int id) {
        int newId = id;
        if (id >= 24) {
            ++newId;
        }
        if (id >= 248) {
            newId += 4;
        }
        if (id >= 296) {
            newId += 6;
        }
        if (id >= 354) {
            newId += 4;
        }
        if (id >= 372) {
            newId += 4;
        }
        return newId;
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.put(new ResourcePackTracker());
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }
    
    @Override
    public InventoryPackets getItemRewriter() {
        return this.itemRewriter;
    }
    
    static {
        TO_NEW_PITCH = new ValueTransformer<Short, Float>(Type.FLOAT) {
            @Override
            public Float transform(final PacketWrapper wrapper, final Short inputValue) throws Exception {
                return inputValue / 63.0f;
            }
        };
        TRANSFORM_METADATA = new ValueTransformer<List<Metadata>, List<Metadata>>(Types1_9.METADATA_LIST) {
            @Override
            public List<Metadata> transform(final PacketWrapper wrapper, final List<Metadata> inputValue) throws Exception {
                final List<Metadata> metaList = new CopyOnWriteArrayList<Metadata>(inputValue);
                for (final Metadata m : metaList) {
                    if (m.id() >= 5) {
                        m.setId(m.id() + 1);
                    }
                }
                return metaList;
            }
        };
    }
}
