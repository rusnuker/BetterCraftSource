// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_18to1_17_1;

import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.storage.ChunkLightStorage;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
import com.viaversion.viaversion.api.type.types.version.Types1_18;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.SoundRewriter;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.packets.WorldPackets;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.packets.EntityPackets;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.data.MappingData;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.ClientboundPackets1_17_1;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public final class Protocol1_18To1_17_1 extends AbstractProtocol<ClientboundPackets1_17_1, ClientboundPackets1_18, ServerboundPackets1_17, ServerboundPackets1_17>
{
    public static final MappingData MAPPINGS;
    private final EntityPackets entityRewriter;
    private final InventoryPackets itemRewriter;
    
    public Protocol1_18To1_17_1() {
        super(ClientboundPackets1_17_1.class, ClientboundPackets1_18.class, ServerboundPackets1_17.class, ServerboundPackets1_17.class);
        this.entityRewriter = new EntityPackets(this);
        this.itemRewriter = new InventoryPackets(this);
    }
    
    @Override
    protected void registerPackets() {
        this.entityRewriter.register();
        this.itemRewriter.register();
        WorldPackets.register(this);
        final SoundRewriter<ClientboundPackets1_17_1> soundRewriter = new SoundRewriter<ClientboundPackets1_17_1>(this);
        soundRewriter.registerSound(ClientboundPackets1_17_1.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_17_1.ENTITY_SOUND);
        final TagRewriter<ClientboundPackets1_17_1> tagRewriter = new TagRewriter<ClientboundPackets1_17_1>(this);
        tagRewriter.registerGeneric(ClientboundPackets1_17_1.TAGS);
        tagRewriter.addEmptyTags(RegistryType.BLOCK, "minecraft:lava_pool_stone_cannot_replace", "minecraft:big_dripleaf_placeable", "minecraft:wolves_spawnable_on", "minecraft:rabbits_spawnable_on", "minecraft:polar_bears_spawnable_on_in_frozen_ocean", "minecraft:parrots_spawnable_on", "minecraft:mooshrooms_spawnable_on", "minecraft:goats_spawnable_on", "minecraft:foxes_spawnable_on", "minecraft:axolotls_spawnable_on", "minecraft:animals_spawnable_on", "minecraft:azalea_grows_on", "minecraft:azalea_root_replaceable", "minecraft:replaceable_plants", "minecraft:terracotta");
        tagRewriter.addEmptyTags(RegistryType.ITEM, "minecraft:dirt", "minecraft:terracotta");
        new StatisticsRewriter<ClientboundPackets1_17_1>(this).register(ClientboundPackets1_17_1.STATISTICS);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CLIENT_SETTINGS, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.read(Type.BOOLEAN);
            }
        });
    }
    
    @Override
    protected void onMappingDataLoaded() {
        Types1_18.PARTICLE.filler(this).reader("block", ParticleType.Readers.BLOCK).reader("block_marker", ParticleType.Readers.BLOCK).reader("dust", ParticleType.Readers.DUST).reader("falling_dust", ParticleType.Readers.BLOCK).reader("dust_color_transition", ParticleType.Readers.DUST_TRANSITION).reader("item", ParticleType.Readers.VAR_INT_ITEM).reader("vibration", ParticleType.Readers.VIBRATION);
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_18To1_17_1.MAPPINGS;
    }
    
    @Override
    public void init(final UserConnection connection) {
        this.addEntityTracker(connection, new EntityTrackerBase(connection, Entity1_17Types.PLAYER));
        connection.put(new ChunkLightStorage());
    }
    
    @Override
    public EntityPackets getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public InventoryPackets getItemRewriter() {
        return this.itemRewriter;
    }
    
    static {
        MAPPINGS = new MappingData();
    }
}
