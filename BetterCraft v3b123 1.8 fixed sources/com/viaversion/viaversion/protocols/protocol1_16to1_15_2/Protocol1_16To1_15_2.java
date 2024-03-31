// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_16to1_15_2;

import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.storage.InventoryTracker1_16;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.provider.PlayerAbilitiesProvider;
import java.util.List;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.rewriter.SoundRewriter;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets.WorldPackets;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets.EntityPackets;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.data.TranslationMappings;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.metadata.MetadataRewriter1_16To1_15_2;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.data.MappingData;
import java.util.UUID;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_16To1_15_2 extends AbstractProtocol<ClientboundPackets1_15, ClientboundPackets1_16, ServerboundPackets1_14, ServerboundPackets1_16>
{
    private static final UUID ZERO_UUID;
    public static final MappingData MAPPINGS;
    private final MetadataRewriter1_16To1_15_2 metadataRewriter;
    private final InventoryPackets itemRewriter;
    private final TranslationMappings componentRewriter;
    private TagRewriter<ClientboundPackets1_15> tagRewriter;
    
    public Protocol1_16To1_15_2() {
        super(ClientboundPackets1_15.class, ClientboundPackets1_16.class, ServerboundPackets1_14.class, ServerboundPackets1_16.class);
        this.metadataRewriter = new MetadataRewriter1_16To1_15_2(this);
        this.itemRewriter = new InventoryPackets(this);
        this.componentRewriter = new TranslationMappings(this);
    }
    
    @Override
    protected void registerPackets() {
        this.metadataRewriter.register();
        this.itemRewriter.register();
        EntityPackets.register(this);
        WorldPackets.register(this);
        (this.tagRewriter = new TagRewriter<ClientboundPackets1_15>(this)).register(ClientboundPackets1_15.TAGS, RegistryType.ENTITY);
        new StatisticsRewriter<ClientboundPackets1_15>(this).register(ClientboundPackets1_15.STATISTICS);
        this.registerClientbound(State.LOGIN, 2, 2, wrapper -> {
            final UUID uuid = UUID.fromString(wrapper.read(Type.STRING));
            wrapper.write(Type.UUID, uuid);
            return;
        });
        this.registerClientbound(State.STATUS, 0, 0, wrapper -> {
            final String original = wrapper.passthrough(Type.STRING);
            final JsonObject object = GsonUtil.getGson().fromJson(original, JsonObject.class);
            final JsonObject players = object.getAsJsonObject("players");
            if (players == null) {
                return;
            }
            else {
                final JsonArray sample = players.getAsJsonArray("sample");
                if (sample == null) {
                    return;
                }
                else {
                    final JsonArray splitSamples = new JsonArray();
                    sample.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final JsonElement element = iterator.next();
                        final JsonObject playerInfo = element.getAsJsonObject();
                        final String name = playerInfo.getAsJsonPrimitive("name").getAsString();
                        if (name.indexOf(10) == -1) {
                            splitSamples.add(playerInfo);
                        }
                        else {
                            final String id = playerInfo.getAsJsonPrimitive("id").getAsString();
                            name.split("\n");
                            final String[] array;
                            int i = 0;
                            for (int length = array.length; i < length; ++i) {
                                final String s = array[i];
                                final JsonObject newSample = new JsonObject();
                                newSample.addProperty("name", s);
                                newSample.addProperty("id", id);
                                splitSamples.add(newSample);
                            }
                        }
                    }
                    if (splitSamples.size() != sample.size()) {
                        players.add("sample", splitSamples);
                        wrapper.set(Type.STRING, 0, object.toString());
                    }
                    return;
                }
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_15.CHAT_MESSAGE, new PacketHandlers() {
            public void register() {
                this.map(Type.COMPONENT);
                this.map((Type<Object>)Type.BYTE);
                this.handler(wrapper -> {
                    Protocol1_16To1_15_2.this.componentRewriter.processText(wrapper.get(Type.COMPONENT, 0));
                    wrapper.write(Type.UUID, Protocol1_16To1_15_2.ZERO_UUID);
                });
            }
        });
        this.componentRewriter.registerBossBar(ClientboundPackets1_15.BOSSBAR);
        this.componentRewriter.registerTitle(ClientboundPackets1_15.TITLE);
        this.componentRewriter.registerCombatEvent(ClientboundPackets1_15.COMBAT_EVENT);
        final SoundRewriter<ClientboundPackets1_15> soundRewriter = new SoundRewriter<ClientboundPackets1_15>(this);
        soundRewriter.registerSound(ClientboundPackets1_15.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_15.ENTITY_SOUND);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_16>)this).registerServerbound(ServerboundPackets1_16.INTERACT_ENTITY, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            final int action = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
            if (action == 0 || action == 2) {
                if (action == 2) {
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                }
                wrapper.passthrough((Type<Object>)Type.VAR_INT);
            }
            wrapper.read((Type<Object>)Type.BOOLEAN);
            return;
        });
        if (Via.getConfig().isIgnoreLong1_16ChannelNames()) {
            ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_16>)this).registerServerbound(ServerboundPackets1_16.PLUGIN_MESSAGE, new PacketHandlers() {
                public void register() {
                    this.handler(wrapper -> {
                        final String channel = wrapper.passthrough(Type.STRING);
                        if (channel.length() > 32) {
                            if (!Via.getConfig().isSuppressConversionWarnings()) {
                                Via.getPlatform().getLogger().warning("Ignoring incoming plugin channel, as it is longer than 32 characters: " + channel);
                            }
                            wrapper.cancel();
                        }
                        else if (channel.equals("minecraft:register") || channel.equals("minecraft:unregister")) {
                            final String[] channels = new String(wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8).split("\u0000");
                            final ArrayList checkedChannels = new ArrayList<String>(channels.length);
                            final String[] array;
                            int i = 0;
                            for (int length = array.length; i < length; ++i) {
                                final String registeredChannel = array[i];
                                if (registeredChannel.length() > 32) {
                                    if (!Via.getConfig().isSuppressConversionWarnings()) {
                                        Via.getPlatform().getLogger().warning("Ignoring incoming plugin channel register of '" + registeredChannel + "', as it is longer than 32 characters");
                                    }
                                }
                                else {
                                    checkedChannels.add(registeredChannel);
                                }
                            }
                            if (checkedChannels.isEmpty()) {
                                wrapper.cancel();
                            }
                            else {
                                wrapper.write(Type.REMAINING_BYTES, Joiner.on('\0').join(checkedChannels).getBytes(StandardCharsets.UTF_8));
                            }
                        }
                    });
                }
            });
        }
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_16>)this).registerServerbound(ServerboundPackets1_16.PLAYER_ABILITIES, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.BYTE);
            final PlayerAbilitiesProvider playerAbilities = Via.getManager().getProviders().get(PlayerAbilitiesProvider.class);
            wrapper.write(Type.FLOAT, playerAbilities.getFlyingSpeed(wrapper.user()));
            wrapper.write(Type.FLOAT, playerAbilities.getWalkingSpeed(wrapper.user()));
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_16>)this).cancelServerbound(ServerboundPackets1_16.GENERATE_JIGSAW);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_16>)this).cancelServerbound(ServerboundPackets1_16.UPDATE_JIGSAW_BLOCK);
    }
    
    @Override
    protected void onMappingDataLoaded() {
        final int[] wallPostOverrideTag = new int[47];
        int arrayIndex = 0;
        wallPostOverrideTag[arrayIndex++] = 140;
        wallPostOverrideTag[arrayIndex++] = 179;
        wallPostOverrideTag[arrayIndex++] = 264;
        for (int i = 153; i <= 158; ++i) {
            wallPostOverrideTag[arrayIndex++] = i;
        }
        for (int i = 163; i <= 168; ++i) {
            wallPostOverrideTag[arrayIndex++] = i;
        }
        for (int i = 408; i <= 439; ++i) {
            wallPostOverrideTag[arrayIndex++] = i;
        }
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:wall_post_override", wallPostOverrideTag);
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:beacon_base_blocks", 133, 134, 148, 265);
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:climbable", 160, 241, 658);
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:fire", 142);
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:campfires", 679);
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:fence_gates", 242, 467, 468, 469, 470, 471);
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:unstable_bottom_center", 242, 467, 468, 469, 470, 471);
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:wooden_trapdoors", 193, 194, 195, 196, 197, 198);
        this.tagRewriter.addTag(RegistryType.ITEM, "minecraft:wooden_trapdoors", 215, 216, 217, 218, 219, 220);
        this.tagRewriter.addTag(RegistryType.ITEM, "minecraft:beacon_payment_items", 529, 530, 531, 760);
        this.tagRewriter.addTag(RegistryType.ENTITY, "minecraft:impact_projectiles", 2, 72, 71, 37, 69, 79, 83, 15, 93);
        this.tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:guarded_by_piglins");
        this.tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:soul_speed_blocks");
        this.tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:soul_fire_base_blocks");
        this.tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:non_flammable_wood");
        this.tagRewriter.addEmptyTag(RegistryType.ITEM, "minecraft:non_flammable_wood");
        this.tagRewriter.addEmptyTags(RegistryType.BLOCK, "minecraft:bamboo_plantable_on", "minecraft:beds", "minecraft:bee_growables", "minecraft:beehives", "minecraft:coral_plants", "minecraft:crops", "minecraft:dragon_immune", "minecraft:flowers", "minecraft:portals", "minecraft:shulker_boxes", "minecraft:small_flowers", "minecraft:tall_flowers", "minecraft:trapdoors", "minecraft:underwater_bonemeals", "minecraft:wither_immune", "minecraft:wooden_fences", "minecraft:wooden_trapdoors");
        this.tagRewriter.addEmptyTags(RegistryType.ENTITY, "minecraft:arrows", "minecraft:beehive_inhabitors", "minecraft:raiders", "minecraft:skeletons");
        this.tagRewriter.addEmptyTags(RegistryType.ITEM, "minecraft:beds", "minecraft:coals", "minecraft:fences", "minecraft:flowers", "minecraft:lectern_books", "minecraft:music_discs", "minecraft:small_flowers", "minecraft:tall_flowers", "minecraft:trapdoors", "minecraft:walls", "minecraft:wooden_fences");
        Types1_16.PARTICLE.filler(this).reader("block", ParticleType.Readers.BLOCK).reader("dust", ParticleType.Readers.DUST).reader("falling_dust", ParticleType.Readers.BLOCK).reader("item", ParticleType.Readers.VAR_INT_ITEM);
    }
    
    @Override
    public void register(final ViaProviders providers) {
        providers.register(PlayerAbilitiesProvider.class, new PlayerAbilitiesProvider());
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.addEntityTracker(this.getClass(), new EntityTrackerBase(userConnection, Entity1_16Types.PLAYER));
        userConnection.put(new InventoryTracker1_16());
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_16To1_15_2.MAPPINGS;
    }
    
    @Override
    public MetadataRewriter1_16To1_15_2 getEntityRewriter() {
        return this.metadataRewriter;
    }
    
    @Override
    public InventoryPackets getItemRewriter() {
        return this.itemRewriter;
    }
    
    public TranslationMappings getComponentRewriter() {
        return this.componentRewriter;
    }
    
    static {
        ZERO_UUID = new UUID(0L, 0L);
        MAPPINGS = new MappingData();
    }
}
