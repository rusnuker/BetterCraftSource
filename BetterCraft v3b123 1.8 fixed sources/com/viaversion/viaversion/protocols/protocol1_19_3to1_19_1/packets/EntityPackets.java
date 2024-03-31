// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.BitSet;
import java.util.UUID;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.types.version.Types1_19_3;
import com.viaversion.viaversion.api.type.types.version.Types1_19;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_3Types;
import com.viaversion.viaversion.api.type.types.BitSetType;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.Protocol1_19_3To1_19_1;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ClientboundPackets1_19_1;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public final class EntityPackets extends EntityRewriter<ClientboundPackets1_19_1, Protocol1_19_3To1_19_1>
{
    private static final BitSetType PROFILE_ACTIONS_ENUM_TYPE;
    
    public EntityPackets(final Protocol1_19_3To1_19_1 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((EntityRewriter<ClientboundPackets1_19_1, T>)this).registerTrackerWithData1_19(ClientboundPackets1_19_1.SPAWN_ENTITY, Entity1_19_3Types.FALLING_BLOCK);
        ((EntityRewriter<ClientboundPackets1_19_1, T>)this).registerMetadataRewriter(ClientboundPackets1_19_1.ENTITY_METADATA, Types1_19.METADATA_LIST, Types1_19_3.METADATA_LIST);
        ((EntityRewriter<ClientboundPackets1_19_1, T>)this).registerRemoveEntities(ClientboundPackets1_19_1.REMOVE_ENTITIES);
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_1.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(EntityPackets.this.dimensionDataHandler());
                this.handler(EntityPackets.this.biomeSizeTracker());
                this.handler(EntityPackets.this.worldDataTrackerHandlerByKey());
                this.handler(wrapper -> {
                    final PacketWrapper enableFeaturesPacket = wrapper.create(ClientboundPackets1_19_3.UPDATE_ENABLED_FEATURES);
                    enableFeaturesPacket.write(Type.VAR_INT, 1);
                    enableFeaturesPacket.write(Type.STRING, "minecraft:vanilla");
                    enableFeaturesPacket.scheduleSend(Protocol1_19_3To1_19_1.class);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_1.RESPAWN, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.map((Type<Object>)Type.LONG);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(EntityPackets.this.worldDataTrackerHandlerByKey());
                this.handler(wrapper -> {
                    final boolean keepAttributes = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    byte keepDataMask = 2;
                    if (keepAttributes) {
                        keepDataMask |= 0x1;
                    }
                    wrapper.write(Type.BYTE, keepDataMask);
                });
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_19_1.PLAYER_INFO, ClientboundPackets1_19_3.PLAYER_INFO_UPDATE, wrapper -> {
            final int action = wrapper.read((Type<Integer>)Type.VAR_INT);
            if (action == 4) {
                final int entries = wrapper.read((Type<Integer>)Type.VAR_INT);
                final UUID[] uuidsToRemove = new UUID[entries];
                for (int i = 0; i < entries; ++i) {
                    uuidsToRemove[i] = wrapper.read(Type.UUID);
                }
                wrapper.write(Type.UUID_ARRAY, uuidsToRemove);
                wrapper.setPacketType(ClientboundPackets1_19_3.PLAYER_INFO_REMOVE);
            }
            else {
                final BitSet set = new BitSet(6);
                if (action == 0) {
                    set.set(0, 6);
                }
                else {
                    set.set((action == 1) ? (action + 1) : (action + 2));
                }
                wrapper.write(EntityPackets.PROFILE_ACTIONS_ENUM_TYPE, set);
                for (int entries2 = wrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < entries2; ++j) {
                    wrapper.passthrough(Type.UUID);
                    if (action == 0) {
                        wrapper.passthrough(Type.STRING);
                        for (int properties = wrapper.passthrough((Type<Integer>)Type.VAR_INT), k = 0; k < properties; ++k) {
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.OPTIONAL_STRING);
                        }
                        final int gamemode = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final int ping = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final JsonElement displayName = wrapper.read(Type.OPTIONAL_COMPONENT);
                        wrapper.read(Type.OPTIONAL_PROFILE_KEY);
                        wrapper.write(Type.BOOLEAN, false);
                        wrapper.write(Type.VAR_INT, gamemode);
                        wrapper.write(Type.BOOLEAN, true);
                        wrapper.write(Type.VAR_INT, ping);
                        wrapper.write(Type.OPTIONAL_COMPONENT, displayName);
                    }
                    else if (action == 1 || action == 2) {
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    }
                    else if (action == 3) {
                        wrapper.passthrough(Type.OPTIONAL_COMPONENT);
                    }
                }
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.filter().handler((event, meta) -> {
            final int id = meta.metaType().typeId();
            meta.setMetaType(Types1_19_3.META_TYPES.byId((id >= 2) ? (id + 1) : id));
            return;
        });
        this.registerMetaTypeHandler(Types1_19_3.META_TYPES.itemType, Types1_19_3.META_TYPES.blockStateType, null, Types1_19_3.META_TYPES.particleType);
        this.filter().index(6).handler((event, meta) -> {
            final int pose = meta.value();
            if (pose >= 10) {
                meta.setValue(pose + 1);
            }
            return;
        });
        this.filter().filterFamily(Entity1_19_3Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
            final int data = (int)meta.getValue();
            meta.setValue(((Protocol1_19_3To1_19_1)this.protocol).getMappingData().getNewBlockStateId(data));
        });
    }
    
    @Override
    public void onMappingDataLoaded() {
        this.mapTypes();
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_19_3Types.getTypeFromId(type);
    }
    
    static {
        PROFILE_ACTIONS_ENUM_TYPE = new BitSetType(6);
    }
}
