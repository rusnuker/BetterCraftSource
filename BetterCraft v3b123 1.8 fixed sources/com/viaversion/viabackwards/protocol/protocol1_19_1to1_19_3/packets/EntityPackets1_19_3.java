// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.minecraft.ProfileKey;
import java.util.UUID;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.util.BitSet;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ClientboundPackets1_19_1;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.storage.ChatTypeStorage1_19_3;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_3Types;
import com.viaversion.viaversion.api.type.types.version.Types1_19_3;
import com.viaversion.viaversion.api.type.types.version.Types1_19;
import com.viaversion.viaversion.api.type.types.BitSetType;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.Protocol1_19_1To1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public final class EntityPackets1_19_3 extends EntityRewriter<ClientboundPackets1_19_3, Protocol1_19_1To1_19_3>
{
    private static final BitSetType PROFILE_ACTIONS_ENUM_TYPE;
    private static final int[] PROFILE_ACTIONS;
    private static final int ADD_PLAYER = 0;
    private static final int INITIALIZE_CHAT = 1;
    private static final int UPDATE_GAMEMODE = 2;
    private static final int UPDATE_LISTED = 3;
    private static final int UPDATE_LATENCY = 4;
    private static final int UPDATE_DISPLAYNAME = 5;
    
    public EntityPackets1_19_3(final Protocol1_19_1To1_19_3 protocol) {
        super(protocol, Types1_19.META_TYPES.optionalComponentType, Types1_19.META_TYPES.booleanType);
    }
    
    @Override
    protected void registerPackets() {
        ((com.viaversion.viaversion.rewriter.EntityRewriter<ClientboundPackets1_19_3, T>)this).registerMetadataRewriter(ClientboundPackets1_19_3.ENTITY_METADATA, Types1_19_3.METADATA_LIST, Types1_19.METADATA_LIST);
        ((com.viaversion.viaversion.rewriter.EntityRewriter<ClientboundPackets1_19_3, T>)this).registerRemoveEntities(ClientboundPackets1_19_3.REMOVE_ENTITIES);
        ((EntityRewriter<ClientboundPackets1_19_3, T>)this).registerTrackerWithData1_19(ClientboundPackets1_19_3.SPAWN_ENTITY, Entity1_19_3Types.FALLING_BLOCK);
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(EntityPackets1_19_3.this.dimensionDataHandler());
                this.handler(EntityPackets1_19_3.this.biomeSizeTracker());
                this.handler(EntityPackets1_19_3.this.worldDataTrackerHandlerByKey());
                this.handler(wrapper -> {
                    final ChatTypeStorage1_19_3 chatTypeStorage = wrapper.user().get(ChatTypeStorage1_19_3.class);
                    chatTypeStorage.clear();
                    final CompoundTag registry = wrapper.get(Type.NBT, 0);
                    final ListTag chatTypes = registry.get("minecraft:chat_type").get("value");
                    chatTypes.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag chatType = iterator.next();
                        final CompoundTag chatTypeCompound = (CompoundTag)chatType;
                        final NumberTag idTag = chatTypeCompound.get("id");
                        chatTypeStorage.addChatType(idTag.asInt(), chatTypeCompound);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.RESPAWN, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.map((Type<Object>)Type.LONG);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(EntityPackets1_19_3.this.worldDataTrackerHandlerByKey());
                this.handler(wrapper -> {
                    final byte keepDataMask = wrapper.read((Type<Byte>)Type.BYTE);
                    wrapper.write(Type.BOOLEAN, (keepDataMask & 0x1) != 0x0);
                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_19_3.PLAYER_INFO_UPDATE, ClientboundPackets1_19_1.PLAYER_INFO, wrapper -> {
            wrapper.cancel();
            final BitSet actions = wrapper.read((Type<BitSet>)EntityPackets1_19_3.PROFILE_ACTIONS_ENUM_TYPE);
            final int entries = wrapper.read((Type<Integer>)Type.VAR_INT);
            if (actions.get(0)) {
                final PacketWrapper playerInfoPacket = wrapper.create(ClientboundPackets1_19_1.PLAYER_INFO);
                playerInfoPacket.write(Type.VAR_INT, 0);
                playerInfoPacket.write(Type.VAR_INT, entries);
                for (int i = 0; i < entries; ++i) {
                    playerInfoPacket.write(Type.UUID, (UUID)wrapper.read((Type<T>)Type.UUID));
                    playerInfoPacket.write(Type.STRING, (String)wrapper.read((Type<T>)Type.STRING));
                    final int properties = wrapper.read((Type<Integer>)Type.VAR_INT);
                    playerInfoPacket.write(Type.VAR_INT, properties);
                    for (int j = 0; j < properties; ++j) {
                        playerInfoPacket.write(Type.STRING, (String)wrapper.read((Type<T>)Type.STRING));
                        playerInfoPacket.write(Type.STRING, (String)wrapper.read((Type<T>)Type.STRING));
                        playerInfoPacket.write(Type.OPTIONAL_STRING, (String)wrapper.read((Type<T>)Type.OPTIONAL_STRING));
                    }
                    ProfileKey profileKey;
                    if (actions.get(1) && wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        wrapper.read(Type.UUID);
                        profileKey = wrapper.read(Type.PROFILE_KEY);
                    }
                    else {
                        profileKey = null;
                    }
                    final int gamemode = actions.get(2) ? wrapper.read((Type<Integer>)Type.VAR_INT) : 0;
                    if (actions.get(3)) {
                        wrapper.read((Type<Object>)Type.BOOLEAN);
                    }
                    final int latency = actions.get(4) ? wrapper.read((Type<Integer>)Type.VAR_INT) : 0;
                    final JsonElement displayName = actions.get(5) ? wrapper.read(Type.OPTIONAL_COMPONENT) : null;
                    playerInfoPacket.write(Type.VAR_INT, gamemode);
                    playerInfoPacket.write(Type.VAR_INT, latency);
                    playerInfoPacket.write(Type.OPTIONAL_COMPONENT, displayName);
                    playerInfoPacket.write(Type.OPTIONAL_PROFILE_KEY, profileKey);
                }
                playerInfoPacket.send(Protocol1_19_1To1_19_3.class);
                return;
            }
            else {
                final PlayerProfileUpdate[] updates = new PlayerProfileUpdate[entries];
                for (int k = 0; k < entries; ++k) {
                    final UUID uuid = wrapper.read(Type.UUID);
                    int gamemode2 = 0;
                    int latency2 = 0;
                    JsonElement displayName2 = null;
                    final int[] profile_ACTIONS = EntityPackets1_19_3.PROFILE_ACTIONS;
                    int l = 0;
                    for (int length = profile_ACTIONS.length; l < length; ++l) {
                        final int action = profile_ACTIONS[l];
                        if (!(!actions.get(action))) {
                            switch (action) {
                                case 2: {
                                    gamemode2 = wrapper.read((Type<Integer>)Type.VAR_INT);
                                    break;
                                }
                                case 4: {
                                    latency2 = wrapper.read((Type<Integer>)Type.VAR_INT);
                                    break;
                                }
                                case 5: {
                                    displayName2 = wrapper.read(Type.OPTIONAL_COMPONENT);
                                    break;
                                }
                            }
                        }
                    }
                    updates[k] = new PlayerProfileUpdate(uuid, gamemode2, latency2, displayName2);
                }
                if (actions.get(2)) {
                    this.sendPlayerProfileUpdate(wrapper.user(), 1, updates);
                }
                else if (actions.get(4)) {
                    this.sendPlayerProfileUpdate(wrapper.user(), 2, updates);
                }
                else if (actions.get(5)) {
                    this.sendPlayerProfileUpdate(wrapper.user(), 3, updates);
                }
                return;
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_19_3.PLAYER_INFO_REMOVE, ClientboundPackets1_19_1.PLAYER_INFO, wrapper -> {
            final UUID[] uuids = wrapper.read(Type.UUID_ARRAY);
            wrapper.write(Type.VAR_INT, 4);
            wrapper.write(Type.VAR_INT, uuids.length);
            final UUID[] array;
            int n = 0;
            for (int length2 = array.length; n < length2; ++n) {
                final UUID uuid2 = array[n];
                wrapper.write(Type.UUID, uuid2);
            }
        });
    }
    
    private void sendPlayerProfileUpdate(final UserConnection connection, final int action, final PlayerProfileUpdate[] updates) throws Exception {
        final PacketWrapper playerInfoPacket = PacketWrapper.create(ClientboundPackets1_19_1.PLAYER_INFO, connection);
        playerInfoPacket.write(Type.VAR_INT, action);
        playerInfoPacket.write(Type.VAR_INT, updates.length);
        for (final PlayerProfileUpdate update : updates) {
            playerInfoPacket.write(Type.UUID, update.uuid());
            if (action == 1) {
                playerInfoPacket.write(Type.VAR_INT, update.gamemode());
            }
            else if (action == 2) {
                playerInfoPacket.write(Type.VAR_INT, update.latency());
            }
            else {
                if (action != 3) {
                    throw new IllegalArgumentException("Invalid action: " + action);
                }
                playerInfoPacket.write(Type.OPTIONAL_COMPONENT, update.displayName());
            }
        }
        playerInfoPacket.send(Protocol1_19_1To1_19_3.class);
    }
    
    public void registerRewrites() {
        this.filter().handler((event, meta) -> {
            final int id = meta.metaType().typeId();
            if (id > 2) {
                meta.setMetaType(Types1_19.META_TYPES.byId(id - 1));
            }
            else if (id != 2) {
                meta.setMetaType(Types1_19.META_TYPES.byId(id));
            }
            return;
        });
        this.registerMetaTypeHandler(Types1_19.META_TYPES.itemType, Types1_19.META_TYPES.blockStateType, null, Types1_19.META_TYPES.particleType, Types1_19.META_TYPES.componentType, Types1_19.META_TYPES.optionalComponentType);
        this.filter().index(6).handler((event, meta) -> {
            final int pose = meta.value();
            if (pose == 10) {
                meta.setValue(0);
            }
            else if (pose > 10) {
                meta.setValue(pose - 1);
            }
            return;
        });
        this.filter().filterFamily(Entity1_19_3Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
            final int data = (int)meta.getValue();
            meta.setValue(((Protocol1_19_1To1_19_3)this.protocol).getMappingData().getNewBlockStateId(data));
            return;
        });
        this.filter().type(Entity1_19_3Types.CAMEL).cancel(19);
        this.filter().type(Entity1_19_3Types.CAMEL).cancel(20);
    }
    
    @Override
    public void onMappingDataLoaded() {
        this.mapTypes();
        this.mapEntityTypeWithData(Entity1_19_3Types.CAMEL, Entity1_19_3Types.DONKEY).jsonName();
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_19_3Types.getTypeFromId(typeId);
    }
    
    static {
        PROFILE_ACTIONS_ENUM_TYPE = new BitSetType(6);
        PROFILE_ACTIONS = new int[] { 2, 4, 5 };
    }
    
    private static final class PlayerProfileUpdate
    {
        private final UUID uuid;
        private final int gamemode;
        private final int latency;
        private final JsonElement displayName;
        
        private PlayerProfileUpdate(final UUID uuid, final int gamemode, final int latency, final JsonElement displayName) {
            this.uuid = uuid;
            this.gamemode = gamemode;
            this.latency = latency;
            this.displayName = displayName;
        }
        
        public UUID uuid() {
            return this.uuid;
        }
        
        public int gamemode() {
            return this.gamemode;
        }
        
        public int latency() {
            return this.latency;
        }
        
        public JsonElement displayName() {
            return this.displayName;
        }
    }
}
