// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_15to1_14_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_15Types;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.metadata.MetadataRewriter1_15To1_14_4;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;

public class EntityPackets
{
    public static void register(final Protocol1_15To1_14_4 protocol) {
        final MetadataRewriter1_15To1_14_4 metadataRewriter = protocol.get(MetadataRewriter1_15To1_14_4.class);
        ((EntityRewriter<ClientboundPackets1_14, T>)metadataRewriter).registerTrackerWithData(ClientboundPackets1_14.SPAWN_ENTITY, Entity1_15Types.FALLING_BLOCK);
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_14.SPAWN_MOB, new PacketHandlers() {
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
                this.handler(metadataRewriter.trackerHandler());
                this.handler(wrapper -> {
                    final Object val$metadataRewriter = metadataRewriter;
                    sendMetadataPacket(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), metadataRewriter);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_14.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.handler(wrapper -> {
                    final Object val$metadataRewriter = metadataRewriter;
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    wrapper.user().getEntityTracker(Protocol1_15To1_14_4.class).addEntity(entityId, Entity1_15Types.PLAYER);
                    sendMetadataPacket(wrapper, entityId, metadataRewriter);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_14.RESPAWN, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> wrapper.write(Type.LONG, 0L));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_14.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(metadataRewriter.playerTrackerHandler());
                this.handler(wrapper -> wrapper.write(Type.LONG, 0L));
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(wrapper -> wrapper.write(Type.BOOLEAN, !Via.getConfig().is1_15InstantRespawn()));
            }
        });
        ((EntityRewriter<ClientboundPackets1_14, T>)metadataRewriter).registerMetadataRewriter(ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST);
        ((EntityRewriter<ClientboundPackets1_14, T>)metadataRewriter).registerRemoveEntities(ClientboundPackets1_14.DESTROY_ENTITIES);
    }
    
    private static void sendMetadataPacket(final PacketWrapper wrapper, final int entityId, final EntityRewriter<?, ?> rewriter) throws Exception {
        final List<Metadata> metadata = wrapper.read(Types1_14.METADATA_LIST);
        if (metadata.isEmpty()) {
            return;
        }
        wrapper.send(Protocol1_15To1_14_4.class);
        wrapper.cancel();
        rewriter.handleMetadata(entityId, metadata, wrapper.user());
        final PacketWrapper metadataPacket = PacketWrapper.create(ClientboundPackets1_15.ENTITY_METADATA, wrapper.user());
        metadataPacket.write(Type.VAR_INT, entityId);
        metadataPacket.write(Types1_14.METADATA_LIST, metadata);
        metadataPacket.send(Protocol1_15To1_14_4.class);
    }
    
    public static int getNewEntityId(final int oldId) {
        return (oldId >= 4) ? (oldId + 1) : oldId;
    }
}
