// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_13to1_13_1;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.data.CommandRewriter1_13_1;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets.WorldPackets1_13_1;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets.InventoryPackets1_13_1;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets.EntityPackets1_13_1;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_13To1_13_1 extends BackwardsProtocol<ClientboundPackets1_13, ClientboundPackets1_13, ServerboundPackets1_13, ServerboundPackets1_13>
{
    public static final BackwardsMappings MAPPINGS;
    private final EntityPackets1_13_1 entityRewriter;
    private final InventoryPackets1_13_1 itemRewriter;
    private final TranslatableRewriter<ClientboundPackets1_13> translatableRewriter;
    
    public Protocol1_13To1_13_1() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_13.class, ServerboundPackets1_13.class, ServerboundPackets1_13.class);
        this.entityRewriter = new EntityPackets1_13_1(this);
        this.itemRewriter = new InventoryPackets1_13_1(this);
        this.translatableRewriter = new TranslatableRewriter<ClientboundPackets1_13>(this);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        WorldPackets1_13_1.register(this);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_13.CHAT_MESSAGE);
        this.translatableRewriter.registerCombatEvent(ClientboundPackets1_13.COMBAT_EVENT);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_13.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_13.TAB_LIST);
        this.translatableRewriter.registerTitle(ClientboundPackets1_13.TITLE);
        this.translatableRewriter.registerPing();
        new CommandRewriter1_13_1(this).registerDeclareCommands(ClientboundPackets1_13.DECLARE_COMMANDS);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_13>)this).registerServerbound(ServerboundPackets1_13.TAB_COMPLETE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.STRING, (ValueTransformer<String, Object>)new ValueTransformer<String, String>(Type.STRING) {
                    @Override
                    public String transform(final PacketWrapper wrapper, final String inputValue) {
                        return inputValue.startsWith("/") ? inputValue : ("/" + inputValue);
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_13>)this).registerServerbound(ServerboundPackets1_13.EDIT_BOOK, new PacketHandlers() {
            public void register() {
                this.map(Type.FLAT_ITEM);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(wrapper -> {
                    Protocol1_13To1_13_1.this.itemRewriter.handleItemToServer(wrapper.get(Type.FLAT_ITEM, 0));
                    wrapper.write(Type.VAR_INT, 0);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_13.OPEN_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final JsonElement title = wrapper.passthrough(Type.COMPONENT);
                    Protocol1_13To1_13_1.this.translatableRewriter.processText(title);
                    if (ViaBackwards.getConfig().fix1_13FormattedInventoryTitle()) {
                        if (!title.isJsonObject() || title.getAsJsonObject().size() != 1 || !title.getAsJsonObject().has("translate")) {
                            final JsonObject legacyComponent = new JsonObject();
                            legacyComponent.addProperty("text", ChatRewriter.jsonToLegacyText(title.toString()));
                            wrapper.set(Type.COMPONENT, 0, legacyComponent);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_13.TAB_COMPLETE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int start = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    wrapper.set(Type.VAR_INT, 1, start - 1);
                    for (int count = wrapper.get((Type<Integer>)Type.VAR_INT, 3), i = 0; i < count; ++i) {
                        wrapper.passthrough(Type.STRING);
                        final boolean hasTooltip = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                        if (hasTooltip) {
                            wrapper.passthrough(Type.STRING);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_13.BOSSBAR, new PacketHandlers() {
            public void register() {
                this.map(Type.UUID);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (action == 0 || action == 3) {
                        Protocol1_13To1_13_1.this.translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT));
                        if (action == 0) {
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            short flags = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                            if ((flags & 0x4) != 0x0) {
                                flags |= 0x2;
                            }
                            wrapper.write(Type.UNSIGNED_BYTE, flags);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_13.ADVANCEMENTS, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                wrapper.passthrough(Type.STRING);
                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                    wrapper.passthrough(Type.STRING);
                }
                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                    wrapper.passthrough(Type.COMPONENT);
                    wrapper.passthrough(Type.COMPONENT);
                    final Item icon = wrapper.passthrough(Type.FLAT_ITEM);
                    this.itemRewriter.handleItemToClient(icon);
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    final int flags = wrapper.passthrough((Type<Integer>)Type.INT);
                    if ((flags & 0x1) != 0x0) {
                        wrapper.passthrough(Type.STRING);
                    }
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                    wrapper.passthrough((Type<Object>)Type.FLOAT);
                }
                wrapper.passthrough(Type.STRING_ARRAY);
                for (int arrayLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                    wrapper.passthrough(Type.STRING_ARRAY);
                }
            }
            return;
        });
        new TagRewriter<ClientboundPackets1_13>(this).register(ClientboundPackets1_13.TAGS, RegistryType.ITEM);
        new StatisticsRewriter<ClientboundPackets1_13>(this).register(ClientboundPackets1_13.STATISTICS);
    }
    
    @Override
    public void init(final UserConnection user) {
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_13Types.EntityType.PLAYER));
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_13To1_13_1.MAPPINGS;
    }
    
    @Override
    public EntityPackets1_13_1 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public InventoryPackets1_13_1 getItemRewriter() {
        return this.itemRewriter;
    }
    
    public TranslatableRewriter<ClientboundPackets1_13> translatableRewriter() {
        return this.translatableRewriter;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.13.2", "1.13", Protocol1_13_1To1_13.class);
    }
}
