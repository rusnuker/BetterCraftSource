// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13_1to1_13.packets;

import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.metadata.MetadataRewriter1_13_1To1_13;
import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;

public class EntityPackets
{
    public static void register(final Protocol1_13_1To1_13 protocol) {
        final MetadataRewriter1_13_1To1_13 metadataRewriter = protocol.get(MetadataRewriter1_13_1To1_13.class);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final byte type = wrapper.get((Type<Byte>)Type.BYTE, 0);
                    final Entity1_13Types.EntityType entType = Entity1_13Types.getTypeFromId(type, true);
                    if (entType != null) {
                        if (entType.is(Entity1_13Types.EntityType.FALLING_BLOCK)) {
                            final int data = wrapper.get((Type<Integer>)Type.INT, 0);
                            wrapper.set(Type.INT, 0, protocol.getMappingData().getNewBlockStateId(data));
                        }
                        wrapper.user().getEntityTracker(Protocol1_13_1To1_13.class).addEntity(entityId, entType);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_MOB, new PacketHandlers() {
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
                this.map(Types1_13.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_13.METADATA_LIST));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Types1_13.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_13.METADATA_LIST, Entity1_13Types.EntityType.PLAYER));
            }
        });
        ((EntityRewriter<ClientboundPackets1_13, T>)metadataRewriter).registerRemoveEntities(ClientboundPackets1_13.DESTROY_ENTITIES);
        ((EntityRewriter<ClientboundPackets1_13, T>)metadataRewriter).registerMetadataRewriter(ClientboundPackets1_13.ENTITY_METADATA, Types1_13.METADATA_LIST);
    }
}
