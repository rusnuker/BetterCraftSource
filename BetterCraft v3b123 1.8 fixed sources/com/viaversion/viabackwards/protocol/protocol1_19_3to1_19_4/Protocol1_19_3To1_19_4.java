// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.Protocol1_19_4To1_19_3;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.packets.BlockItemPackets1_19_4;
import com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.packets.EntityPackets1_19_4;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ServerboundPackets1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_19_3To1_19_4 extends BackwardsProtocol<ClientboundPackets1_19_4, ClientboundPackets1_19_3, ServerboundPackets1_19_4, ServerboundPackets1_19_3>
{
    public static final BackwardsMappings MAPPINGS;
    private final EntityPackets1_19_4 entityRewriter;
    private final BlockItemPackets1_19_4 itemRewriter;
    private final TranslatableRewriter<ClientboundPackets1_19_4> translatableRewriter;
    
    public Protocol1_19_3To1_19_4() {
        super(ClientboundPackets1_19_4.class, ClientboundPackets1_19_3.class, ServerboundPackets1_19_4.class, ServerboundPackets1_19_3.class);
        this.entityRewriter = new EntityPackets1_19_4(this);
        this.itemRewriter = new BlockItemPackets1_19_4(this);
        this.translatableRewriter = new TranslatableRewriter<ClientboundPackets1_19_4>(this);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        final SoundRewriter<ClientboundPackets1_19_4> soundRewriter = new SoundRewriter<ClientboundPackets1_19_4>(this);
        soundRewriter.registerStopSound(ClientboundPackets1_19_4.STOP_SOUND);
        soundRewriter.register1_19_3Sound(ClientboundPackets1_19_4.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_19_4.ENTITY_SOUND);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.ACTIONBAR);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.TITLE_TEXT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.TITLE_SUBTITLE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_19_4.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_19_4.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_19_4.TAB_LIST);
        this.translatableRewriter.registerCombatKill(ClientboundPackets1_19_4.COMBAT_KILL);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.SYSTEM_CHAT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.DISGUISED_CHAT);
        this.translatableRewriter.registerPing();
        new CommandRewriter<ClientboundPackets1_19_4>(this) {
            @Override
            public void handleArgument(final PacketWrapper wrapper, final String argumentType) throws Exception {
                switch (argumentType) {
                    case "minecraft:heightmap": {
                        wrapper.write(Type.VAR_INT, 0);
                        break;
                    }
                    case "minecraft:time": {
                        wrapper.read((Type<Object>)Type.INT);
                        break;
                    }
                    case "minecraft:resource":
                    case "minecraft:resource_or_tag": {
                        final String resource = wrapper.read(Type.STRING);
                        wrapper.write(Type.STRING, resource.equals("minecraft:damage_type") ? "minecraft:mob_effect" : resource);
                        break;
                    }
                    default: {
                        super.handleArgument(wrapper, argumentType);
                        break;
                    }
                }
            }
        }.registerDeclareCommands1_19(ClientboundPackets1_19_4.DECLARE_COMMANDS);
        final TagRewriter<ClientboundPackets1_19_4> tagRewriter = new TagRewriter<ClientboundPackets1_19_4>(this);
        tagRewriter.removeTags("minecraft:damage_type");
        tagRewriter.registerGeneric(ClientboundPackets1_19_4.TAGS);
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_4.SERVER_DATA, wrapper -> {
            final JsonElement element = wrapper.read(Type.COMPONENT);
            wrapper.write(Type.OPTIONAL_COMPONENT, element);
            final byte[] iconBytes = wrapper.read(Type.OPTIONAL_BYTE_ARRAY_PRIMITIVE);
            String string;
            if (iconBytes != null) {
                string = "data:image/png;base64," + new String(Base64.getEncoder().encode(iconBytes), StandardCharsets.UTF_8);
            }
            else {
                string = null;
            }
            final String iconBase64 = string;
            wrapper.write(Type.OPTIONAL_STRING, iconBase64);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_19_4.BUNDLE);
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_19_4.CHUNK_BIOMES);
    }
    
    @Override
    public void init(final UserConnection user) {
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_19_4Types.PLAYER));
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_19_3To1_19_4.MAPPINGS;
    }
    
    @Override
    public BlockItemPackets1_19_4 getItemRewriter() {
        return this.itemRewriter;
    }
    
    @Override
    public EntityPackets1_19_4 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public TranslatableRewriter<ClientboundPackets1_19_4> getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.19.4", "1.19.3", Protocol1_19_4To1_19_3.class);
    }
}
