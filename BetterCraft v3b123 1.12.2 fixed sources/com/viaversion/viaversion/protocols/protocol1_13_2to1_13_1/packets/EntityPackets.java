// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.packets;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.protocol.Protocol;

public class EntityPackets
{
    public static void register(final Protocol protocol) {
        final PacketHandler metaTypeHandler = wrapper -> {
            wrapper.get(Types1_13_2.METADATA_LIST, 0).iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final Metadata metadata = iterator.next();
                metadata.setMetaType(Types1_13_2.META_TYPES.byId(metadata.metaType().typeId()));
            }
            return;
        };
        protocol.registerClientbound(ClientboundPackets1_13.SPAWN_MOB, new PacketHandlers() {
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
                this.map(Types1_13.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(metaTypeHandler);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_13.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Types1_13.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(metaTypeHandler);
            }
        });
        protocol.registerClientbound(ClientboundPackets1_13.ENTITY_METADATA, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Types1_13.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(metaTypeHandler);
            }
        });
    }
}
