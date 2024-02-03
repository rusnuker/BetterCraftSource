// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_14_1to1_14.packets;

import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14_1to1_14.metadata.MetadataRewriter1_14_1To1_14;
import com.viaversion.viaversion.protocols.protocol1_14_1to1_14.Protocol1_14_1To1_14;

public class EntityPackets
{
    public static void register(final Protocol1_14_1To1_14 protocol) {
        final MetadataRewriter1_14_1To1_14 metadataRewriter = protocol.get(MetadataRewriter1_14_1To1_14.class);
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
                this.map(Types1_14.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_14.METADATA_LIST));
            }
        });
        ((EntityRewriter<ClientboundPackets1_14, T>)metadataRewriter).registerRemoveEntities(ClientboundPackets1_14.DESTROY_ENTITIES);
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_14.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Types1_14.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_14.METADATA_LIST, Entity1_14Types.PLAYER));
            }
        });
        ((EntityRewriter<ClientboundPackets1_14, T>)metadataRewriter).registerMetadataRewriter(ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST);
    }
}
