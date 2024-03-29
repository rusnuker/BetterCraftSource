// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_17to1_16_4;

import com.viaversion.viaversion.api.data.MappingDataBase;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.storage.InventoryAcknowledgements;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
import com.viaversion.viaversion.api.type.types.version.Types1_17;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.rewriter.SoundRewriter;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.packets.WorldPackets;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.packets.EntityPackets;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public final class Protocol1_17To1_16_4 extends AbstractProtocol<ClientboundPackets1_16_2, ClientboundPackets1_17, ServerboundPackets1_16_2, ServerboundPackets1_17>
{
    public static final MappingData MAPPINGS;
    private static final String[] NEW_GAME_EVENT_TAGS;
    private final EntityPackets entityRewriter;
    private final InventoryPackets itemRewriter;
    private final TagRewriter<ClientboundPackets1_16_2> tagRewriter;
    
    public Protocol1_17To1_16_4() {
        super(ClientboundPackets1_16_2.class, ClientboundPackets1_17.class, ServerboundPackets1_16_2.class, ServerboundPackets1_17.class);
        this.entityRewriter = new EntityPackets(this);
        this.itemRewriter = new InventoryPackets(this);
        this.tagRewriter = new TagRewriter<ClientboundPackets1_16_2>(this);
    }
    
    @Override
    protected void registerPackets() {
        this.entityRewriter.register();
        this.itemRewriter.register();
        WorldPackets.register(this);
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_16_2.TAGS, wrapper -> {
            wrapper.write(Type.VAR_INT, 5);
            RegistryType.getValues();
            final RegistryType[] array;
            final int length = array.length;
            int i = 0;
            while (i < length) {
                final RegistryType type = array[i];
                wrapper.write(Type.STRING, type.resourceLocation());
                this.tagRewriter.handle(wrapper, this.tagRewriter.getRewriter(type), this.tagRewriter.getNewTags(type));
                if (type == RegistryType.ENTITY) {
                    break;
                }
                else {
                    ++i;
                }
            }
            wrapper.write(Type.STRING, RegistryType.GAME_EVENT.resourceLocation());
            wrapper.write(Type.VAR_INT, Protocol1_17To1_16_4.NEW_GAME_EVENT_TAGS.length);
            final String[] new_GAME_EVENT_TAGS = Protocol1_17To1_16_4.NEW_GAME_EVENT_TAGS;
            int j = 0;
            for (int length2 = new_GAME_EVENT_TAGS.length; j < length2; ++j) {
                final String tag = new_GAME_EVENT_TAGS[j];
                wrapper.write(Type.STRING, tag);
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[0]);
            }
            return;
        });
        new StatisticsRewriter<ClientboundPackets1_16_2>(this).register(ClientboundPackets1_16_2.STATISTICS);
        final SoundRewriter<ClientboundPackets1_16_2> soundRewriter = new SoundRewriter<ClientboundPackets1_16_2>(this);
        soundRewriter.registerSound(ClientboundPackets1_16_2.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_16_2.ENTITY_SOUND);
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_16_2.RESOURCE_PACK, wrapper -> {
            wrapper.passthrough(Type.STRING);
            wrapper.passthrough(Type.STRING);
            wrapper.write(Type.BOOLEAN, Via.getConfig().isForcedUse1_17ResourcePack());
            wrapper.write(Type.OPTIONAL_COMPONENT, Via.getConfig().get1_17ResourcePackPrompt());
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_16_2.MAP_DATA, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.passthrough((Type<Object>)Type.BYTE);
            wrapper.read((Type<Object>)Type.BOOLEAN);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            final int size = wrapper.read((Type<Integer>)Type.VAR_INT);
            if (size != 0) {
                wrapper.write(Type.BOOLEAN, true);
                wrapper.write(Type.VAR_INT, size);
            }
            else {
                wrapper.write(Type.BOOLEAN, false);
            }
            return;
        });
        ((Protocol<ClientboundPackets1_16_2, ClientboundPackets1_17, SM, SU>)this).registerClientbound(ClientboundPackets1_16_2.TITLE, null, wrapper -> {
            final int type2 = wrapper.read((Type<Integer>)Type.VAR_INT);
            ClientboundPacketType packetType = null;
            switch (type2) {
                case 0: {
                    packetType = ClientboundPackets1_17.TITLE_TEXT;
                    break;
                }
                case 1: {
                    packetType = ClientboundPackets1_17.TITLE_SUBTITLE;
                    break;
                }
                case 2: {
                    packetType = ClientboundPackets1_17.ACTIONBAR;
                    break;
                }
                case 3: {
                    packetType = ClientboundPackets1_17.TITLE_TIMES;
                    break;
                }
                case 4: {
                    packetType = ClientboundPackets1_17.CLEAR_TITLES;
                    wrapper.write(Type.BOOLEAN, false);
                    break;
                }
                case 5: {
                    packetType = ClientboundPackets1_17.CLEAR_TITLES;
                    wrapper.write(Type.BOOLEAN, true);
                    break;
                }
                default: {
                    new IllegalArgumentException("Invalid title type received: " + type2);
                    throw;
                }
            }
            wrapper.setPacketType(packetType);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_16_2.EXPLOSION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.handler(wrapper -> wrapper.write((Type<Object>)Type.VAR_INT, wrapper.read((Type<T>)Type.INT)));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_16_2.SPAWN_POSITION, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION1_14);
                this.handler(wrapper -> wrapper.write(Type.FLOAT, 0.0f));
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CLIENT_SETTINGS, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> wrapper.read((Type<Object>)Type.BOOLEAN));
            }
        });
    }
    
    @Override
    protected void onMappingDataLoaded() {
        this.tagRewriter.loadFromMappingData();
        this.tagRewriter.addEmptyTags(RegistryType.ITEM, "minecraft:candles", "minecraft:ignored_by_piglin_babies", "minecraft:piglin_food", "minecraft:freeze_immune_wearables", "minecraft:axolotl_tempt_items", "minecraft:occludes_vibration_signals", "minecraft:fox_food", "minecraft:diamond_ores", "minecraft:iron_ores", "minecraft:lapis_ores", "minecraft:redstone_ores", "minecraft:coal_ores", "minecraft:copper_ores", "minecraft:emerald_ores", "minecraft:cluster_max_harvestables");
        this.tagRewriter.addEmptyTags(RegistryType.BLOCK, "minecraft:crystal_sound_blocks", "minecraft:candle_cakes", "minecraft:candles", "minecraft:snow_step_sound_blocks", "minecraft:inside_step_sound_blocks", "minecraft:occludes_vibration_signals", "minecraft:dripstone_replaceable_blocks", "minecraft:cave_vines", "minecraft:moss_replaceable", "minecraft:deepslate_ore_replaceables", "minecraft:lush_ground_replaceable", "minecraft:diamond_ores", "minecraft:iron_ores", "minecraft:lapis_ores", "minecraft:redstone_ores", "minecraft:stone_ore_replaceables", "minecraft:coal_ores", "minecraft:copper_ores", "minecraft:emerald_ores", "minecraft:snow", "minecraft:small_dripleaf_placeable", "minecraft:features_cannot_replace", "minecraft:lava_pool_stone_replaceables", "minecraft:geode_invalid_blocks");
        this.tagRewriter.addEmptyTags(RegistryType.ENTITY, "minecraft:powder_snow_walkable_mobs", "minecraft:axolotl_always_hostiles", "minecraft:axolotl_tempted_hostiles", "minecraft:axolotl_hunt_targets", "minecraft:freeze_hurts_extra_types", "minecraft:freeze_immune_entity_types");
        Types1_17.PARTICLE.filler(this).reader("block", ParticleType.Readers.BLOCK).reader("dust", ParticleType.Readers.DUST).reader("falling_dust", ParticleType.Readers.BLOCK).reader("dust_color_transition", ParticleType.Readers.DUST_TRANSITION).reader("item", ParticleType.Readers.VAR_INT_ITEM).reader("vibration", ParticleType.Readers.VIBRATION);
    }
    
    @Override
    public void init(final UserConnection user) {
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_17Types.PLAYER));
        user.put(new InventoryAcknowledgements());
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_17To1_16_4.MAPPINGS;
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
        MAPPINGS = new MappingDataBase("1.16.2", "1.17");
        NEW_GAME_EVENT_TAGS = new String[] { "minecraft:ignore_vibrations_sneaking", "minecraft:vibrations" };
    }
}
