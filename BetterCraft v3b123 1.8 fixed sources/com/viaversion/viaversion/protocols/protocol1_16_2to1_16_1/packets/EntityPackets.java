// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16_2Types;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.metadata.MetadataRewriter1_16_2To1_16_1;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;

public class EntityPackets
{
    public static void register(final Protocol1_16_2To1_16_1 protocol) {
        final MetadataRewriter1_16_2To1_16_1 metadataRewriter = protocol.get(MetadataRewriter1_16_2To1_16_1.class);
        ((EntityRewriter<ClientboundPackets1_16, T>)metadataRewriter).registerTrackerWithData(ClientboundPackets1_16.SPAWN_ENTITY, Entity1_16_2Types.FALLING_BLOCK);
        ((EntityRewriter<ClientboundPackets1_16, T>)metadataRewriter).registerTracker(ClientboundPackets1_16.SPAWN_MOB);
        ((EntityRewriter<ClientboundPackets1_16, T>)metadataRewriter).registerTracker(ClientboundPackets1_16.SPAWN_PLAYER, Entity1_16_2Types.PLAYER);
        ((EntityRewriter<ClientboundPackets1_16, T>)metadataRewriter).registerMetadataRewriter(ClientboundPackets1_16.ENTITY_METADATA, Types1_16.METADATA_LIST);
        ((EntityRewriter<ClientboundPackets1_16, T>)metadataRewriter).registerRemoveEntities(ClientboundPackets1_16.DESTROY_ENTITIES);
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_16.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final short gamemode = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    wrapper.write(Type.BOOLEAN, (gamemode & 0x8) != 0x0);
                    final short gamemode2 = (short)(gamemode & 0xFFFFFFF7);
                    wrapper.write(Type.UNSIGNED_BYTE, gamemode2);
                    return;
                });
                this.map((Type<Object>)Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    wrapper.read(Type.NBT);
                    wrapper.write(Type.NBT, protocol.getMappingData().getDimensionRegistry());
                    final String dimensionType = wrapper.read(Type.STRING);
                    wrapper.write(Type.NBT, EntityPackets.getDimensionData(dimensionType));
                    return;
                });
                this.map(Type.STRING);
                this.map((Type<Object>)Type.LONG);
                this.map(Type.UNSIGNED_BYTE, Type.VAR_INT);
                this.handler(metadataRewriter.playerTrackerHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_16.RESPAWN, wrapper -> {
            final String dimensionType = wrapper.read(Type.STRING);
            wrapper.write(Type.NBT, getDimensionData(dimensionType));
        });
    }
    
    public static CompoundTag getDimensionData(final String dimensionType) {
        final CompoundTag tag = Protocol1_16_2To1_16_1.MAPPINGS.getDimensionDataMap().get(dimensionType);
        if (tag == null) {
            Via.getPlatform().getLogger().severe("Could not get dimension data of " + dimensionType);
            throw new NullPointerException("Dimension data for " + dimensionType + " is null!");
        }
        return tag.clone();
    }
}
