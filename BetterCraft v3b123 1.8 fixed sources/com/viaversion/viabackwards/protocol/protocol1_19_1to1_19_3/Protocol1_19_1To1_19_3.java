// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_3Types;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.storage.ChatSessionStorage;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.Protocol1_19To1_19_1;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.storage.ChatTypeStorage1_19_3;
import com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.storage.ChatRegistryStorage;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.types.BitSetType;
import java.util.BitSet;
import com.viaversion.viaversion.util.CipherUtil;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.storage.NonceStorage;
import com.viaversion.viaversion.api.minecraft.ProfileKey;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.packets.BlockItemPackets1_19_3;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.packets.EntityPackets1_19_3;
import com.viaversion.viaversion.api.type.types.ByteArrayType;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ServerboundPackets1_19_1;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ServerboundPackets1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ClientboundPackets1_19_1;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_19_1To1_19_3 extends BackwardsProtocol<ClientboundPackets1_19_3, ClientboundPackets1_19_1, ServerboundPackets1_19_3, ServerboundPackets1_19_1>
{
    public static final BackwardsMappings MAPPINGS;
    public static final ByteArrayType.OptionalByteArrayType OPTIONAL_SIGNATURE_BYTES_TYPE;
    public static final ByteArrayType SIGNATURE_BYTES_TYPE;
    private final EntityPackets1_19_3 entityRewriter;
    private final BlockItemPackets1_19_3 itemRewriter;
    private final TranslatableRewriter<ClientboundPackets1_19_3> translatableRewriter;
    
    public Protocol1_19_1To1_19_3() {
        super(ClientboundPackets1_19_3.class, ClientboundPackets1_19_1.class, ServerboundPackets1_19_3.class, ServerboundPackets1_19_1.class);
        this.entityRewriter = new EntityPackets1_19_3(this);
        this.itemRewriter = new BlockItemPackets1_19_3(this);
        this.translatableRewriter = new TranslatableRewriter<ClientboundPackets1_19_3>(this);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_3.SYSTEM_CHAT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_3.ACTIONBAR);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_3.TITLE_TEXT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_3.TITLE_SUBTITLE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_19_3.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_19_3.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_19_3.TAB_LIST);
        this.translatableRewriter.registerOpenWindow(ClientboundPackets1_19_3.OPEN_WINDOW);
        this.translatableRewriter.registerCombatKill(ClientboundPackets1_19_3.COMBAT_KILL);
        this.translatableRewriter.registerPing();
        final SoundRewriter<ClientboundPackets1_19_3> soundRewriter = new SoundRewriter<ClientboundPackets1_19_3>(this);
        soundRewriter.registerStopSound(ClientboundPackets1_19_3.STOP_SOUND);
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_3.SOUND, wrapper -> {
            final int soundId = wrapper.read((Type<Integer>)Type.VAR_INT) - 1;
            if (soundId != -1) {
                final int mappedId = Protocol1_19_1To1_19_3.MAPPINGS.getSoundMappings().getNewId(soundId);
                if (mappedId == -1) {
                    wrapper.cancel();
                    return;
                }
                else {
                    wrapper.write(Type.VAR_INT, mappedId);
                    return;
                }
            }
            else {
                String soundIdentifier = wrapper.read(Type.STRING);
                wrapper.read((Type<Object>)Type.OPTIONAL_FLOAT);
                final String mappedIdentifier = Protocol1_19_1To1_19_3.MAPPINGS.getMappedNamedSound(soundIdentifier);
                if (mappedIdentifier != null) {
                    if (mappedIdentifier.isEmpty()) {
                        wrapper.cancel();
                        return;
                    }
                    else {
                        soundIdentifier = mappedIdentifier;
                    }
                }
                wrapper.write(Type.STRING, soundIdentifier);
                wrapper.setPacketType(ClientboundPackets1_19_1.NAMED_SOUND);
                return;
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_3.ENTITY_SOUND, wrapper -> {
            final int soundId2 = wrapper.read((Type<Integer>)Type.VAR_INT) - 1;
            if (soundId2 != -1) {
                final int mappedId2 = Protocol1_19_1To1_19_3.MAPPINGS.getSoundMappings().getNewId(soundId2);
                if (mappedId2 == -1) {
                    wrapper.cancel();
                    return;
                }
                else {
                    wrapper.write(Type.VAR_INT, mappedId2);
                }
            }
            String soundIdentifier2 = wrapper.read(Type.STRING);
            wrapper.read((Type<Object>)Type.OPTIONAL_FLOAT);
            final String mappedIdentifier2 = Protocol1_19_1To1_19_3.MAPPINGS.getMappedNamedSound(soundIdentifier2);
            if (mappedIdentifier2 != null) {
                if (mappedIdentifier2.isEmpty()) {
                    wrapper.cancel();
                    return;
                }
                else {
                    soundIdentifier2 = mappedIdentifier2;
                }
            }
            final int mappedId3 = Protocol1_19_1To1_19_3.MAPPINGS.mappedSound(soundIdentifier2);
            if (mappedId3 == -1) {
                wrapper.cancel();
                return;
            }
            else {
                wrapper.write(Type.VAR_INT, mappedId3);
                return;
            }
        });
        final TagRewriter<ClientboundPackets1_19_3> tagRewriter = new TagRewriter<ClientboundPackets1_19_3>(this);
        tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:non_flammable_wood");
        tagRewriter.addEmptyTag(RegistryType.ITEM, "minecraft:overworld_natural_logs");
        tagRewriter.registerGeneric(ClientboundPackets1_19_3.TAGS);
        new StatisticsRewriter<ClientboundPackets1_19_3>(this).register(ClientboundPackets1_19_3.STATISTICS);
        final CommandRewriter<ClientboundPackets1_19_3> commandRewriter = new CommandRewriter<ClientboundPackets1_19_3>(this);
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_3.DECLARE_COMMANDS, wrapper -> {
            for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                final byte flags = wrapper.passthrough((Type<Byte>)Type.BYTE);
                wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                if ((flags & 0x8) != 0x0) {
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                }
                final int nodeType = flags & 0x3;
                if (nodeType == 1 || nodeType == 2) {
                    wrapper.passthrough(Type.STRING);
                }
                if (nodeType == 2) {
                    final int argumentTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final int mappedArgumentTypeId = Protocol1_19_1To1_19_3.MAPPINGS.getArgumentTypeMappings().mappings().getNewId(argumentTypeId);
                    Preconditions.checkArgument(mappedArgumentTypeId != -1, (Object)("Unknown command argument type id: " + argumentTypeId));
                    wrapper.write(Type.VAR_INT, mappedArgumentTypeId);
                    final String identifier = Protocol1_19_1To1_19_3.MAPPINGS.getArgumentTypeMappings().identifier(argumentTypeId);
                    commandRewriter.handleArgument(wrapper, identifier);
                    if (identifier.equals("minecraft:gamemode")) {
                        wrapper.write(Type.VAR_INT, 0);
                    }
                    if ((flags & 0x10) != 0x0) {
                        wrapper.passthrough(Type.STRING);
                    }
                }
            }
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_3.SERVER_DATA, new PacketHandlers() {
            public void register() {
                this.map(Type.OPTIONAL_COMPONENT);
                this.map(Type.OPTIONAL_STRING);
                this.create(Type.BOOLEAN, false);
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final ProfileKey profileKey = wrapper.read(Type.OPTIONAL_PROFILE_KEY);
                    if (profileKey == null) {
                        wrapper.user().put(new NonceStorage(null));
                    }
                });
            }
        });
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.HELLO.getId(), ClientboundLoginPackets.HELLO.getId(), new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    if (!wrapper.user().has(NonceStorage.class)) {
                        final byte[] publicKey = wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                        final byte[] nonce = wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                        wrapper.user().put(new NonceStorage(CipherUtil.encryptNonce(publicKey, nonce)));
                    }
                });
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.ENCRYPTION_KEY.getId(), ServerboundLoginPackets.ENCRYPTION_KEY.getId(), new PacketHandlers() {
            public void register() {
                this.map(Type.BYTE_ARRAY_PRIMITIVE);
                this.handler(wrapper -> {
                    final NonceStorage nonceStorage = wrapper.user().remove(NonceStorage.class);
                    final boolean isNonce = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    if (!isNonce) {
                        wrapper.read((Type<Object>)Type.LONG);
                        wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, (nonceStorage.nonce() != null) ? nonceStorage.nonce() : new byte[0]);
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_1>)this).registerServerbound(ServerboundPackets1_19_1.CHAT_MESSAGE, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.LONG);
                this.map((Type<Object>)Type.LONG);
                this.read(Type.BYTE_ARRAY_PRIMITIVE);
                this.create((Type<Object>)Protocol1_19_1To1_19_3.OPTIONAL_SIGNATURE_BYTES_TYPE, null);
                this.read(Type.BOOLEAN);
                this.read(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY);
                this.read(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE);
                this.handler(wrapper -> {
                    final int offset = 0;
                    final BitSet acknowledged = new BitSet(20);
                    wrapper.write(Type.VAR_INT, 0);
                    wrapper.write(new BitSetType(20), acknowledged);
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_1>)this).registerServerbound(ServerboundPackets1_19_1.CHAT_COMMAND, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map((Type<Object>)Type.LONG);
                this.map((Type<Object>)Type.LONG);
                this.handler(wrapper -> {
                    final int signatures = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT, 0);
                    for (int i = 0; i < signatures; ++i) {
                        wrapper.read(Type.STRING);
                        wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                    }
                    wrapper.read((Type<Object>)Type.BOOLEAN);
                    final int offset = 0;
                    final BitSet acknowledged = new BitSet(20);
                    wrapper.write(Type.VAR_INT, 0);
                    wrapper.write(new BitSetType(20), acknowledged);
                    return;
                });
                this.read(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY);
                this.read(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE);
            }
        });
        ((Protocol<ClientboundPackets1_19_3, ClientboundPackets1_19_1, SM, SU>)this).registerClientbound(ClientboundPackets1_19_3.PLAYER_CHAT, ClientboundPackets1_19_1.SYSTEM_CHAT, new PacketHandlers() {
            public void register() {
                this.read(Type.UUID);
                this.read(Type.VAR_INT);
                this.read(Protocol1_19_1To1_19_3.OPTIONAL_SIGNATURE_BYTES_TYPE);
                this.handler(wrapper -> {
                    final String plainContent = wrapper.read(Type.STRING);
                    wrapper.read((Type<Object>)Type.LONG);
                    wrapper.read((Type<Object>)Type.LONG);
                    for (int lastSeen = wrapper.read((Type<Integer>)Type.VAR_INT), i = 0; i < lastSeen; ++i) {
                        final int index = wrapper.read((Type<Integer>)Type.VAR_INT);
                        if (index == 0) {
                            wrapper.read((Type<Object>)Protocol1_19_1To1_19_3.SIGNATURE_BYTES_TYPE);
                        }
                    }
                    final JsonElement unsignedContent = wrapper.read(Type.OPTIONAL_COMPONENT);
                    final JsonElement content = (unsignedContent != null) ? unsignedContent : GsonComponentSerializer.gson().serializeToTree(Component.text(plainContent));
                    Protocol1_19_1To1_19_3.this.translatableRewriter.processText(content);
                    final int filterMaskType = wrapper.read((Type<Integer>)Type.VAR_INT);
                    if (filterMaskType == 2) {
                        wrapper.read(Type.LONG_ARRAY_PRIMITIVE);
                    }
                    final int chatTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final JsonElement senderName = wrapper.read(Type.COMPONENT);
                    final JsonElement targetName = wrapper.read(Type.OPTIONAL_COMPONENT);
                    final JsonElement result = Protocol1_19To1_19_1.decorateChatMessage(wrapper.user().get(ChatTypeStorage1_19_3.class), chatTypeId, senderName, targetName, content);
                    if (result == null) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.write(Type.COMPONENT, result);
                        wrapper.write(Type.BOOLEAN, false);
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_19_3, ClientboundPackets1_19_1, SM, SU>)this).registerClientbound(ClientboundPackets1_19_3.DISGUISED_CHAT, ClientboundPackets1_19_1.SYSTEM_CHAT, wrapper -> {
            final JsonElement content = wrapper.read(Type.COMPONENT);
            this.translatableRewriter.processText(content);
            final int chatTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
            final JsonElement senderName = wrapper.read(Type.COMPONENT);
            final JsonElement targetName = wrapper.read(Type.OPTIONAL_COMPONENT);
            final JsonElement result = Protocol1_19To1_19_1.decorateChatMessage(wrapper.user().get(ChatTypeStorage1_19_3.class), chatTypeId, senderName, targetName, content);
            if (result == null) {
                wrapper.cancel();
                return;
            }
            else {
                wrapper.write(Type.COMPONENT, result);
                wrapper.write(Type.BOOLEAN, false);
                return;
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_19_3.UPDATE_ENABLED_FEATURES);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_1>)this).cancelServerbound(ServerboundPackets1_19_1.CHAT_PREVIEW);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_1>)this).cancelServerbound(ServerboundPackets1_19_1.CHAT_ACK);
    }
    
    @Override
    public void init(final UserConnection user) {
        user.put(new ChatSessionStorage());
        user.put(new ChatTypeStorage1_19_3());
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_19_3Types.PLAYER));
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_19_1To1_19_3.MAPPINGS;
    }
    
    @Override
    public TranslatableRewriter<ClientboundPackets1_19_3> getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public BlockItemPackets1_19_3 getItemRewriter() {
        return this.itemRewriter;
    }
    
    @Override
    public EntityPackets1_19_3 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
        OPTIONAL_SIGNATURE_BYTES_TYPE = new ByteArrayType.OptionalByteArrayType(256);
        SIGNATURE_BYTES_TYPE = new ByteArrayType(256);
    }
}
