// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_12to1_11_1;

import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_12Types;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.providers.InventoryQuickMoveProvider;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.rewriter.SoundRewriter;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.type.types.version.Types1_12;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.metadata.MetadataRewriter1_12To1_11_1;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_12To1_11_1 extends AbstractProtocol<ClientboundPackets1_9_3, ClientboundPackets1_12, ServerboundPackets1_9_3, ServerboundPackets1_12>
{
    private final MetadataRewriter1_12To1_11_1 metadataRewriter;
    private final InventoryPackets itemRewriter;
    
    public Protocol1_12To1_11_1() {
        super(ClientboundPackets1_9_3.class, ClientboundPackets1_12.class, ServerboundPackets1_9_3.class, ServerboundPackets1_12.class);
        this.metadataRewriter = new MetadataRewriter1_12To1_11_1(this);
        this.itemRewriter = new InventoryPackets(this);
    }
    
    @Override
    protected void registerPackets() {
        this.metadataRewriter.register();
        this.itemRewriter.register();
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.SPAWN_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.BYTE);
                this.handler(Protocol1_12To1_11_1.this.metadataRewriter.objectTrackerHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.SPAWN_MOB, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map(Types1_12.METADATA_LIST);
                this.handler(Protocol1_12To1_11_1.this.metadataRewriter.trackerAndRewriterHandler(Types1_12.METADATA_LIST));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.CHAT_MESSAGE, wrapper -> {
            if (!Via.getConfig().is1_12NBTArrayFix()) {
                return;
            }
            else {
                try {
                    final JsonElement obj = Protocol1_9To1_8.FIX_JSON.transform(null, wrapper.passthrough(Type.COMPONENT).toString());
                    TranslateRewriter.toClient(obj, wrapper.user());
                    ChatItemRewriter.toClient(obj, wrapper.user());
                    wrapper.set(Type.COMPONENT, 0, obj);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        });
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.CHUNK_DATA, wrapper -> {
            final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
            final Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
            final Chunk chunk = wrapper.passthrough((Type<Chunk>)type);
            for (int s = 0; s < chunk.getSections().length; ++s) {
                final ChunkSection section = chunk.getSections()[s];
                if (section != null) {
                    final DataPalette blocks = section.palette(PaletteType.BLOCKS);
                    for (int idx = 0; idx < 4096; ++idx) {
                        final int id = blocks.idAt(idx) >> 4;
                        if (id == 26) {
                            final CompoundTag tag = new CompoundTag();
                            tag.put("color", new IntTag(14));
                            tag.put("x", new IntTag(ChunkSection.xFromIndex(idx) + (chunk.getX() << 4)));
                            tag.put("y", new IntTag(ChunkSection.yFromIndex(idx) + (s << 4)));
                            tag.put("z", new IntTag(ChunkSection.zFromIndex(idx) + (chunk.getZ() << 4)));
                            tag.put("id", new StringTag("minecraft:bed"));
                            chunk.getBlockEntities().add(tag);
                        }
                    }
                }
            }
            return;
        });
        ((com.viaversion.viaversion.rewriter.EntityRewriter<ClientboundPackets1_9_3, T>)this.metadataRewriter).registerRemoveEntities(ClientboundPackets1_9_3.DESTROY_ENTITIES);
        ((com.viaversion.viaversion.rewriter.EntityRewriter<ClientboundPackets1_9_3, T>)this.metadataRewriter).registerMetadataRewriter(ClientboundPackets1_9_3.ENTITY_METADATA, Types1_12.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_9_3.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final UserConnection user = wrapper.user();
                    final ClientWorld clientChunks = user.get(ClientWorld.class);
                    final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 1);
                    clientChunks.setEnvironment(dimensionId);
                    if (user.getProtocolInfo().getProtocolVersion() >= ProtocolVersion.v1_13.getVersion()) {
                        wrapper.create(ClientboundPackets1_13.DECLARE_RECIPES, packetWrapper -> packetWrapper.write(Type.VAR_INT, 0)).scheduleSend(Protocol1_13To1_12_2.class);
                    }
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
        new SoundRewriter<ClientboundPackets1_9_3>(this, this::getNewSoundId).registerSound(ClientboundPackets1_9_3.SOUND);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_12>)this).cancelServerbound(ServerboundPackets1_12.PREPARE_CRAFTING_GRID);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_12>)this).registerServerbound(ServerboundPackets1_12.CLIENT_SETTINGS, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final String locale = wrapper.get(Type.STRING, 0);
                    if (locale.length() > 7) {
                        wrapper.set(Type.STRING, 0, locale.substring(0, 7));
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_12>)this).cancelServerbound(ServerboundPackets1_12.RECIPE_BOOK_DATA);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_12>)this).cancelServerbound(ServerboundPackets1_12.ADVANCEMENT_TAB);
    }
    
    private int getNewSoundId(final int id) {
        int newId = id;
        if (id >= 26) {
            newId += 2;
        }
        if (id >= 70) {
            newId += 4;
        }
        if (id >= 74) {
            ++newId;
        }
        if (id >= 143) {
            newId += 3;
        }
        if (id >= 185) {
            ++newId;
        }
        if (id >= 263) {
            newId += 7;
        }
        if (id >= 301) {
            newId += 33;
        }
        if (id >= 317) {
            newId += 2;
        }
        if (id >= 491) {
            newId += 3;
        }
        return newId;
    }
    
    @Override
    public void register(final ViaProviders providers) {
        providers.register(InventoryQuickMoveProvider.class, new InventoryQuickMoveProvider());
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.addEntityTracker(this.getClass(), new EntityTrackerBase(userConnection, Entity1_12Types.EntityType.PLAYER));
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }
    
    @Override
    public MetadataRewriter1_12To1_11_1 getEntityRewriter() {
        return this.metadataRewriter;
    }
    
    @Override
    public InventoryPackets getItemRewriter() {
        return this.itemRewriter;
    }
}
