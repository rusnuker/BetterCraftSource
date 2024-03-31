// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_16_4to1_17;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.storage.PlayerLastCursorItem;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import java.util.Iterator;
import com.viaversion.viaversion.rewriter.IdRewriteFunction;
import java.util.Map;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.storage.PingRequests;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import java.util.ArrayList;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.TagData;
import java.util.List;
import java.util.HashMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.packets.BlockItemPackets1_17;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.packets.EntityPackets1_17;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_16_4To1_17 extends BackwardsProtocol<ClientboundPackets1_17, ClientboundPackets1_16_2, ServerboundPackets1_17, ServerboundPackets1_16_2>
{
    public static final BackwardsMappings MAPPINGS;
    private static final int[] EMPTY_ARRAY;
    private final EntityPackets1_17 entityRewriter;
    private final BlockItemPackets1_17 blockItemPackets;
    private final TranslatableRewriter<ClientboundPackets1_17> translatableRewriter;
    
    public Protocol1_16_4To1_17() {
        super(ClientboundPackets1_17.class, ClientboundPackets1_16_2.class, ServerboundPackets1_17.class, ServerboundPackets1_16_2.class);
        this.entityRewriter = new EntityPackets1_17(this);
        this.blockItemPackets = new BlockItemPackets1_17(this);
        this.translatableRewriter = new TranslatableRewriter<ClientboundPackets1_17>(this);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_17.CHAT_MESSAGE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_17.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_17.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_17.TAB_LIST);
        this.translatableRewriter.registerOpenWindow(ClientboundPackets1_17.OPEN_WINDOW);
        this.translatableRewriter.registerPing();
        final SoundRewriter<ClientboundPackets1_17> soundRewriter = new SoundRewriter<ClientboundPackets1_17>(this);
        soundRewriter.registerSound(ClientboundPackets1_17.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_17.ENTITY_SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_17.NAMED_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_17.STOP_SOUND);
        final TagRewriter<ClientboundPackets1_17> tagRewriter = new TagRewriter<ClientboundPackets1_17>(this);
        ((AbstractProtocol<ClientboundPackets1_17, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_17.TAGS, wrapper -> {
            final Map<String, List<TagData>> tags = new HashMap<String, List<TagData>>();
            for (int length = wrapper.read((Type<Integer>)Type.VAR_INT), i = 0; i < length; ++i) {
                final String resourceKey = Key.stripMinecraftNamespace(wrapper.read(Type.STRING));
                final List<TagData> tagList = new ArrayList<TagData>();
                tags.put(resourceKey, tagList);
                for (int tagLength = wrapper.read((Type<Integer>)Type.VAR_INT), j = 0; j < tagLength; ++j) {
                    final String identifier = wrapper.read(Type.STRING);
                    final int[] entries = wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                    tagList.add(new TagData(identifier, entries));
                }
            }
            RegistryType.getValues();
            final RegistryType[] array;
            final int length2 = array.length;
            int k = 0;
            while (k < length2) {
                final RegistryType type = array[k];
                final List<TagData> tagList2 = tags.get(type.resourceLocation());
                final IdRewriteFunction rewriter = tagRewriter.getRewriter(type);
                wrapper.write(Type.VAR_INT, tagList2.size());
                tagList2.iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final TagData tagData = iterator.next();
                    int[] entries2 = tagData.entries();
                    if (rewriter != null) {
                        final IntList idList = new IntArrayList(entries2.length);
                        final int[] array2;
                        int l = 0;
                        for (int length3 = array2.length; l < length3; ++l) {
                            final int id = array2[l];
                            final int mappedId = rewriter.rewrite(id);
                            if (mappedId != -1) {
                                idList.add(mappedId);
                            }
                        }
                        entries2 = idList.toArray(Protocol1_16_4To1_17.EMPTY_ARRAY);
                    }
                    wrapper.write(Type.STRING, tagData.identifier());
                    wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, entries2);
                }
                if (type == RegistryType.ENTITY) {
                    break;
                }
                else {
                    ++k;
                }
            }
            return;
        });
        new StatisticsRewriter<ClientboundPackets1_17>(this).register(ClientboundPackets1_17.STATISTICS);
        ((AbstractProtocol<ClientboundPackets1_17, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_17.RESOURCE_PACK, wrapper -> {
            wrapper.passthrough(Type.STRING);
            wrapper.passthrough(Type.STRING);
            wrapper.read((Type<Object>)Type.BOOLEAN);
            wrapper.read(Type.OPTIONAL_COMPONENT);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_17, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_17.EXPLOSION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.handler(wrapper -> wrapper.write((Type<Object>)Type.INT, wrapper.read((Type<T>)Type.VAR_INT)));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_17, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_17.SPAWN_POSITION, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION1_14);
                this.handler(wrapper -> wrapper.read((Type<Object>)Type.FLOAT));
            }
        });
        ((Protocol<ClientboundPackets1_17, ClientboundPackets1_16_2, SM, SU>)this).registerClientbound(ClientboundPackets1_17.PING, null, wrapper -> {
            wrapper.cancel();
            final int id2 = wrapper.read((Type<Integer>)Type.INT);
            final short shortId = (short)id2;
            if (id2 == shortId && ViaBackwards.getConfig().handlePingsAsInvAcknowledgements()) {
                wrapper.user().get(PingRequests.class).addId(shortId);
                final PacketWrapper acknowledgementPacket = wrapper.create(ClientboundPackets1_16_2.WINDOW_CONFIRMATION);
                acknowledgementPacket.write(Type.UNSIGNED_BYTE, (Short)0);
                acknowledgementPacket.write(Type.SHORT, shortId);
                acknowledgementPacket.write(Type.BOOLEAN, false);
                acknowledgementPacket.send(Protocol1_16_4To1_17.class);
                return;
            }
            else {
                final PacketWrapper pongPacket = wrapper.create(ServerboundPackets1_17.PONG);
                pongPacket.write(Type.INT, id2);
                pongPacket.sendToServer(Protocol1_16_4To1_17.class);
                return;
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_16_2>)this).registerServerbound(ServerboundPackets1_16_2.CLIENT_SETTINGS, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> wrapper.write(Type.BOOLEAN, false));
            }
        });
        this.mergePacket(ClientboundPackets1_17.TITLE_TEXT, ClientboundPackets1_16_2.TITLE, 0);
        this.mergePacket(ClientboundPackets1_17.TITLE_SUBTITLE, ClientboundPackets1_16_2.TITLE, 1);
        this.mergePacket(ClientboundPackets1_17.ACTIONBAR, ClientboundPackets1_16_2.TITLE, 2);
        this.mergePacket(ClientboundPackets1_17.TITLE_TIMES, ClientboundPackets1_16_2.TITLE, 3);
        ((Protocol<ClientboundPackets1_17, ClientboundPackets1_16_2, SM, SU>)this).registerClientbound(ClientboundPackets1_17.CLEAR_TITLES, ClientboundPackets1_16_2.TITLE, wrapper -> {
            if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                wrapper.write(Type.VAR_INT, 5);
            }
            else {
                wrapper.write(Type.VAR_INT, 4);
            }
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_17, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_17.ADD_VIBRATION_SIGNAL);
    }
    
    @Override
    public void init(final UserConnection user) {
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_17Types.PLAYER));
        user.put(new PingRequests());
        user.put(new PlayerLastCursorItem());
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_16_4To1_17.MAPPINGS;
    }
    
    @Override
    public TranslatableRewriter<ClientboundPackets1_17> getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    public void mergePacket(final ClientboundPackets1_17 newPacketType, final ClientboundPackets1_16_2 oldPacketType, final int type) {
        ((Protocol<ClientboundPackets1_17, ClientboundPackets1_16_2, SM, SU>)this).registerClientbound(newPacketType, oldPacketType, wrapper -> wrapper.write(Type.VAR_INT, type));
    }
    
    @Override
    public EntityPackets1_17 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public BlockItemPackets1_17 getItemRewriter() {
        return this.blockItemPackets;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.17", "1.16.2", Protocol1_17To1_16_4.class);
        EMPTY_ARRAY = new int[0];
    }
}
