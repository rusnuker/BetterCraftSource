// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16Types;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.WorldNameTracker;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.storage.PlayerSneakStorage;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import java.util.UUID;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.CommandRewriter1_16;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.chat.TranslatableRewriter1_16;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.packets.BlockItemPackets1_16;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.packets.EntityPackets1_16;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_15_2To1_16 extends BackwardsProtocol<ClientboundPackets1_16, ClientboundPackets1_15, ServerboundPackets1_16, ServerboundPackets1_14>
{
    public static final BackwardsMappings MAPPINGS;
    private final EntityPackets1_16 entityRewriter;
    private final BlockItemPackets1_16 blockItemPackets;
    private final TranslatableRewriter1_16 translatableRewriter;
    
    public Protocol1_15_2To1_16() {
        super(ClientboundPackets1_16.class, ClientboundPackets1_15.class, ServerboundPackets1_16.class, ServerboundPackets1_14.class);
        this.entityRewriter = new EntityPackets1_16(this);
        this.blockItemPackets = new BlockItemPackets1_16(this);
        this.translatableRewriter = new TranslatableRewriter1_16(this);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        this.translatableRewriter.registerBossBar(ClientboundPackets1_16.BOSSBAR);
        this.translatableRewriter.registerCombatEvent(ClientboundPackets1_16.COMBAT_EVENT);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_16.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_16.TAB_LIST);
        this.translatableRewriter.registerTitle(ClientboundPackets1_16.TITLE);
        this.translatableRewriter.registerPing();
        new CommandRewriter1_16(this).registerDeclareCommands(ClientboundPackets1_16.DECLARE_COMMANDS);
        this.registerClientbound(State.STATUS, 0, 0, wrapper -> {
            final String original = wrapper.passthrough(Type.STRING);
            final JsonObject object = GsonUtil.getGson().fromJson(original, JsonObject.class);
            final JsonElement description = object.get("description");
            if (description == null) {
                return;
            }
            else {
                this.translatableRewriter.processText(description);
                wrapper.set(Type.STRING, 0, object.toString());
                return;
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_16.CHAT_MESSAGE, new PacketHandlers() {
            public void register() {
                this.handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT)));
                this.map((Type<Object>)Type.BYTE);
                this.map(Type.UUID, Type.NOTHING);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_16.OPEN_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT)));
                this.handler(wrapper -> {
                    int windowType = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (windowType == 20) {
                        wrapper.set(Type.VAR_INT, 1, 7);
                    }
                    else if (windowType > 20) {
                        wrapper.set(Type.VAR_INT, 1, --windowType);
                    }
                });
            }
        });
        final SoundRewriter<ClientboundPackets1_16> soundRewriter = new SoundRewriter<ClientboundPackets1_16>(this);
        soundRewriter.registerSound(ClientboundPackets1_16.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_16.ENTITY_SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_16.NAMED_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_16.STOP_SOUND);
        this.registerClientbound(State.LOGIN, 2, 2, wrapper -> {
            final UUID uuid = wrapper.read(Type.UUID);
            wrapper.write(Type.STRING, uuid.toString());
            return;
        });
        new TagRewriter<ClientboundPackets1_16>(this).register(ClientboundPackets1_16.TAGS, RegistryType.ENTITY);
        new StatisticsRewriter<ClientboundPackets1_16>(this).register(ClientboundPackets1_16.STATISTICS);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this).registerServerbound(ServerboundPackets1_14.ENTITY_ACTION, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            final int action = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
            if (action == 0) {
                wrapper.user().get(PlayerSneakStorage.class).setSneaking(true);
            }
            else if (action == 1) {
                wrapper.user().get(PlayerSneakStorage.class).setSneaking(false);
            }
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this).registerServerbound(ServerboundPackets1_14.INTERACT_ENTITY, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            final int action2 = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
            if (action2 == 0 || action2 == 2) {
                if (action2 == 2) {
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                }
                wrapper.passthrough((Type<Object>)Type.VAR_INT);
            }
            wrapper.write(Type.BOOLEAN, wrapper.user().get(PlayerSneakStorage.class).isSneaking());
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this).registerServerbound(ServerboundPackets1_14.PLAYER_ABILITIES, wrapper -> {
            final byte flags = wrapper.read((Type<Byte>)Type.BYTE);
            final byte flags2 = (byte)(flags & 0x2);
            wrapper.write(Type.BYTE, flags2);
            wrapper.read((Type<Object>)Type.FLOAT);
            wrapper.read((Type<Object>)Type.FLOAT);
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this).cancelServerbound(ServerboundPackets1_14.UPDATE_JIGSAW_BLOCK);
    }
    
    @Override
    public void init(final UserConnection user) {
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
        user.put(new PlayerSneakStorage());
        user.put(new WorldNameTracker());
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_16Types.PLAYER));
    }
    
    @Override
    public TranslatableRewriter1_16 getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_15_2To1_16.MAPPINGS;
    }
    
    @Override
    public EntityPackets1_16 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public BlockItemPackets1_16 getItemRewriter() {
        return this.blockItemPackets;
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
    }
}
