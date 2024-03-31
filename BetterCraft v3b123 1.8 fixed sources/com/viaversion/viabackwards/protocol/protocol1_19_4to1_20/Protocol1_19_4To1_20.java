// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_4to1_20;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_20to1_19_4.Protocol1_20To1_19_4;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.packets.BlockItemPackets1_20;
import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.packets.EntityPackets1_20;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_19_4To1_20 extends BackwardsProtocol<ClientboundPackets1_19_4, ClientboundPackets1_19_4, ServerboundPackets1_19_4, ServerboundPackets1_19_4>
{
    public static final BackwardsMappings MAPPINGS;
    private final TranslatableRewriter<ClientboundPackets1_19_4> translatableRewriter;
    private final EntityPackets1_20 entityRewriter;
    private final BlockItemPackets1_20 itemRewriter;
    
    public Protocol1_19_4To1_20() {
        super(ClientboundPackets1_19_4.class, ClientboundPackets1_19_4.class, ServerboundPackets1_19_4.class, ServerboundPackets1_19_4.class);
        this.translatableRewriter = new TranslatableRewriter<ClientboundPackets1_19_4>(this);
        this.entityRewriter = new EntityPackets1_20(this);
        this.itemRewriter = new BlockItemPackets1_20(this);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        final TagRewriter<ClientboundPackets1_19_4> tagRewriter = new TagRewriter<ClientboundPackets1_19_4>(this);
        tagRewriter.registerGeneric(ClientboundPackets1_19_4.TAGS);
        final SoundRewriter<ClientboundPackets1_19_4> soundRewriter = new SoundRewriter<ClientboundPackets1_19_4>(this);
        soundRewriter.registerStopSound(ClientboundPackets1_19_4.STOP_SOUND);
        soundRewriter.register1_19_3Sound(ClientboundPackets1_19_4.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_19_4.ENTITY_SOUND);
        new StatisticsRewriter<ClientboundPackets1_19_4>(this).register(ClientboundPackets1_19_4.STATISTICS);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.ACTIONBAR);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.TITLE_TEXT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.TITLE_SUBTITLE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_19_4.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_19_4.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_19_4.TAB_LIST);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.SYSTEM_CHAT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_4.DISGUISED_CHAT);
        this.translatableRewriter.registerPing();
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_4.COMBAT_END, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.write(Type.INT, -1);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_4.COMBAT_KILL, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.write(Type.INT, -1);
            this.translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT));
        });
    }
    
    @Override
    public void init(final UserConnection user) {
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_19_4Types.PLAYER));
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_19_4To1_20.MAPPINGS;
    }
    
    @Override
    public EntityPackets1_20 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public BlockItemPackets1_20 getItemRewriter() {
        return this.itemRewriter;
    }
    
    @Override
    public TranslatableRewriter<ClientboundPackets1_19_4> getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.20", "1.19.4", Protocol1_20To1_19_4.class);
    }
}
