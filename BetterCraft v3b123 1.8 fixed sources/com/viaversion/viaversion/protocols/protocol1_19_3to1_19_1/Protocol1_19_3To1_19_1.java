// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1;

import com.viaversion.viaversion.api.data.MappingDataBase;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_3Types;
import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
import com.viaversion.viaversion.api.type.types.version.Types1_19_3;
import com.viaversion.viaversion.api.minecraft.ProfileKey;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.storage.ReceivedMessagesStorage;
import com.viaversion.viaversion.api.minecraft.PlayerMessageSignature;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.rewriter.SoundRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.packets.EntityPackets;
import java.util.UUID;
import com.viaversion.viaversion.api.type.types.BitSetType;
import com.viaversion.viaversion.api.type.types.ByteArrayType;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ServerboundPackets1_19_1;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ClientboundPackets1_19_1;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public final class Protocol1_19_3To1_19_1 extends AbstractProtocol<ClientboundPackets1_19_1, ClientboundPackets1_19_3, ServerboundPackets1_19_1, ServerboundPackets1_19_3>
{
    public static final MappingData MAPPINGS;
    private static final ByteArrayType.OptionalByteArrayType OPTIONAL_MESSAGE_SIGNATURE_BYTES_TYPE;
    private static final ByteArrayType MESSAGE_SIGNATURE_BYTES_TYPE;
    private static final BitSetType ACKNOWLEDGED_BIT_SET_TYPE;
    private static final UUID ZERO_UUID;
    private static final byte[] EMPTY_BYTES;
    private final EntityPackets entityRewriter;
    private final InventoryPackets itemRewriter;
    
    public Protocol1_19_3To1_19_1() {
        super(ClientboundPackets1_19_1.class, ClientboundPackets1_19_3.class, ServerboundPackets1_19_1.class, ServerboundPackets1_19_3.class);
        this.entityRewriter = new EntityPackets(this);
        this.itemRewriter = new InventoryPackets(this);
    }
    
    @Override
    protected void registerPackets() {
        final TagRewriter<ClientboundPackets1_19_1> tagRewriter = new TagRewriter<ClientboundPackets1_19_1>(this);
        tagRewriter.addTagRaw(RegistryType.ITEM, "minecraft:creeper_igniters", 733);
        tagRewriter.addEmptyTags(RegistryType.ITEM, "minecraft:bookshelf_books", "minecraft:hanging_signs", "minecraft:stripped_logs");
        tagRewriter.addEmptyTags(RegistryType.BLOCK, "minecraft:all_hanging_signs", "minecraft:ceiling_hanging_signs", "minecraft:invalid_spawn_inside", "minecraft:stripped_logs", "minecraft:wall_hanging_signs");
        tagRewriter.registerGeneric(ClientboundPackets1_19_1.TAGS);
        this.entityRewriter.register();
        this.itemRewriter.register();
        final SoundRewriter<ClientboundPackets1_19_1> soundRewriter = new SoundRewriter<ClientboundPackets1_19_1>(this);
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_1.ENTITY_SOUND, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(soundRewriter.getSoundHandler());
                this.handler(wrapper -> {
                    final int soundId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    wrapper.set(Type.VAR_INT, 0, soundId + 1);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_1.SOUND, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(soundRewriter.getSoundHandler());
                this.handler(wrapper -> {
                    final int soundId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    wrapper.set(Type.VAR_INT, 0, soundId + 1);
                });
            }
        });
        ((Protocol<ClientboundPackets1_19_1, ClientboundPackets1_19_3, SM, SU>)this).registerClientbound(ClientboundPackets1_19_1.NAMED_SOUND, ClientboundPackets1_19_3.SOUND, wrapper -> {
            wrapper.write(Type.VAR_INT, 0);
            wrapper.passthrough(Type.STRING);
            wrapper.write((Type<Object>)Type.OPTIONAL_FLOAT, null);
            return;
        });
        new StatisticsRewriter<ClientboundPackets1_19_1>(this).register(ClientboundPackets1_19_1.STATISTICS);
        final CommandRewriter<ClientboundPackets1_19_1> commandRewriter = new CommandRewriter<ClientboundPackets1_19_1>(this) {
            @Override
            public void handleArgument(final PacketWrapper wrapper, final String argumentType) throws Exception {
                switch (argumentType) {
                    case "minecraft:item_enchantment": {
                        wrapper.write(Type.STRING, "minecraft:enchantment");
                        break;
                    }
                    case "minecraft:mob_effect": {
                        wrapper.write(Type.STRING, "minecraft:mob_effect");
                        break;
                    }
                    case "minecraft:entity_summon": {
                        wrapper.write(Type.STRING, "minecraft:entity_type");
                        break;
                    }
                    default: {
                        super.handleArgument(wrapper, argumentType);
                        break;
                    }
                }
            }
            
            @Override
            public String handleArgumentType(final String argumentType) {
                switch (argumentType) {
                    case "minecraft:resource": {
                        return "minecraft:resource_key";
                    }
                    case "minecraft:resource_or_tag": {
                        return "minecraft:resource_or_tag_key";
                    }
                    case "minecraft:entity_summon":
                    case "minecraft:item_enchantment":
                    case "minecraft:mob_effect": {
                        return "minecraft:resource";
                    }
                    default: {
                        return argumentType;
                    }
                }
            }
        };
        commandRewriter.registerDeclareCommands1_19(ClientboundPackets1_19_1.DECLARE_COMMANDS);
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_1.SERVER_DATA, new PacketHandlers() {
            public void register() {
                this.map(Type.OPTIONAL_COMPONENT);
                this.map(Type.OPTIONAL_STRING);
                this.read(Type.BOOLEAN);
            }
        });
        ((Protocol<ClientboundPackets1_19_1, ClientboundPackets1_19_3, SM, SU>)this).registerClientbound(ClientboundPackets1_19_1.PLAYER_CHAT, ClientboundPackets1_19_3.DISGUISED_CHAT, new PacketHandlers() {
            public void register() {
                this.read(Type.OPTIONAL_BYTE_ARRAY_PRIMITIVE);
                this.handler(wrapper -> {
                    final PlayerMessageSignature signature = wrapper.read(Type.PLAYER_MESSAGE_SIGNATURE);
                    if (!signature.uuid().equals(Protocol1_19_3To1_19_1.ZERO_UUID) && signature.signatureBytes().length != 0) {
                        final ReceivedMessagesStorage messagesStorage = wrapper.user().get(ReceivedMessagesStorage.class);
                        messagesStorage.add(signature);
                        if (messagesStorage.tickUnacknowledged() > 64) {
                            messagesStorage.resetUnacknowledgedCount();
                            final PacketWrapper chatAckPacket = wrapper.create(ServerboundPackets1_19_1.CHAT_ACK);
                            chatAckPacket.write(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY, messagesStorage.lastSignatures());
                            chatAckPacket.write(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE, null);
                            chatAckPacket.sendToServer(Protocol1_19_3To1_19_1.class);
                        }
                    }
                    final String plainMessage = wrapper.read(Type.STRING);
                    JsonElement decoratedMessage = wrapper.read(Type.OPTIONAL_COMPONENT);
                    wrapper.read((Type<Object>)Type.LONG);
                    wrapper.read((Type<Object>)Type.LONG);
                    wrapper.read(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY);
                    final JsonElement unsignedMessage = wrapper.read(Type.OPTIONAL_COMPONENT);
                    if (unsignedMessage != null) {
                        decoratedMessage = unsignedMessage;
                    }
                    if (decoratedMessage == null) {
                        decoratedMessage = GsonComponentSerializer.gson().serializeToTree(Component.text(plainMessage));
                    }
                    final int filterMaskType = wrapper.read((Type<Integer>)Type.VAR_INT);
                    if (filterMaskType == 2) {
                        wrapper.read(Type.LONG_ARRAY_PRIMITIVE);
                    }
                    wrapper.write(Type.COMPONENT, decoratedMessage);
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_3>)this).registerServerbound(ServerboundPackets1_19_3.CHAT_COMMAND, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.LONG);
                this.map((Type<Object>)Type.LONG);
                this.handler(wrapper -> {
                    final int signatures = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT, 0);
                    for (int i = 0; i < signatures; ++i) {
                        wrapper.read(Type.STRING);
                        wrapper.read((Type<Object>)Protocol1_19_3To1_19_1.MESSAGE_SIGNATURE_BYTES_TYPE);
                    }
                    wrapper.write(Type.BOOLEAN, false);
                    final ReceivedMessagesStorage messagesStorage = wrapper.user().get(ReceivedMessagesStorage.class);
                    messagesStorage.resetUnacknowledgedCount();
                    wrapper.write(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY, messagesStorage.lastSignatures());
                    wrapper.write(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE, null);
                    return;
                });
                this.read(Type.VAR_INT);
                this.read(Protocol1_19_3To1_19_1.ACKNOWLEDGED_BIT_SET_TYPE);
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_3>)this).registerServerbound(ServerboundPackets1_19_3.CHAT_MESSAGE, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.LONG);
                this.read(Type.LONG);
                this.create(Type.LONG, 0L);
                this.handler(wrapper -> {
                    wrapper.read((Type<Object>)Protocol1_19_3To1_19_1.OPTIONAL_MESSAGE_SIGNATURE_BYTES_TYPE);
                    wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, Protocol1_19_3To1_19_1.EMPTY_BYTES);
                    wrapper.write(Type.BOOLEAN, false);
                    final ReceivedMessagesStorage messagesStorage = wrapper.user().get(ReceivedMessagesStorage.class);
                    messagesStorage.resetUnacknowledgedCount();
                    wrapper.write(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY, messagesStorage.lastSignatures());
                    wrapper.write(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE, null);
                    return;
                });
                this.read(Type.VAR_INT);
                this.read(Protocol1_19_3To1_19_1.ACKNOWLEDGED_BIT_SET_TYPE);
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.create(Type.OPTIONAL_PROFILE_KEY, null);
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.ENCRYPTION_KEY.getId(), ServerboundLoginPackets.ENCRYPTION_KEY.getId(), new PacketHandlers() {
            public void register() {
                this.map(Type.BYTE_ARRAY_PRIMITIVE);
                this.create(Type.BOOLEAN, true);
                this.map(Type.BYTE_ARRAY_PRIMITIVE);
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_3>)this).cancelServerbound(ServerboundPackets1_19_3.CHAT_SESSION_UPDATE);
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_19_1.DELETE_CHAT_MESSAGE);
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_19_1.PLAYER_CHAT_HEADER);
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_19_1.CHAT_PREVIEW);
        ((AbstractProtocol<ClientboundPackets1_19_1, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_19_1.SET_DISPLAY_CHAT_PREVIEW);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_3>)this).cancelServerbound(ServerboundPackets1_19_3.CHAT_ACK);
    }
    
    @Override
    protected void onMappingDataLoaded() {
        super.onMappingDataLoaded();
        Types1_19_3.PARTICLE.filler(this).reader("block", ParticleType.Readers.BLOCK).reader("block_marker", ParticleType.Readers.BLOCK).reader("dust", ParticleType.Readers.DUST).reader("falling_dust", ParticleType.Readers.BLOCK).reader("dust_color_transition", ParticleType.Readers.DUST_TRANSITION).reader("item", ParticleType.Readers.VAR_INT_ITEM).reader("vibration", ParticleType.Readers.VIBRATION).reader("sculk_charge", ParticleType.Readers.SCULK_CHARGE).reader("shriek", ParticleType.Readers.SHRIEK);
        Entity1_19_3Types.initialize(this);
    }
    
    @Override
    public void init(final UserConnection user) {
        user.put(new ReceivedMessagesStorage());
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_19_3Types.PLAYER));
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_19_3To1_19_1.MAPPINGS;
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
        MAPPINGS = new MappingDataBase("1.19", "1.19.3");
        OPTIONAL_MESSAGE_SIGNATURE_BYTES_TYPE = new ByteArrayType.OptionalByteArrayType(256);
        MESSAGE_SIGNATURE_BYTES_TYPE = new ByteArrayType(256);
        ACKNOWLEDGED_BIT_SET_TYPE = new BitSetType(20);
        ZERO_UUID = new UUID(0L, 0L);
        EMPTY_BYTES = new byte[0];
    }
}
