// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_20to1_19_4;

import com.viaversion.viaversion.api.data.MappingDataBase;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
import com.viaversion.viaversion.api.type.types.version.Types1_20;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.rewriter.SoundRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.protocols.protocol1_20to1_19_4.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_20to1_19_4.packets.EntityPackets;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public final class Protocol1_20To1_19_4 extends AbstractProtocol<ClientboundPackets1_19_4, ClientboundPackets1_19_4, ServerboundPackets1_19_4, ServerboundPackets1_19_4>
{
    public static final MappingData MAPPINGS;
    private final EntityPackets entityRewriter;
    private final InventoryPackets itemRewriter;
    
    public Protocol1_20To1_19_4() {
        super(ClientboundPackets1_19_4.class, ClientboundPackets1_19_4.class, ServerboundPackets1_19_4.class, ServerboundPackets1_19_4.class);
        this.entityRewriter = new EntityPackets(this);
        this.itemRewriter = new InventoryPackets(this);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        final TagRewriter<ClientboundPackets1_19_4> tagRewriter = new TagRewriter<ClientboundPackets1_19_4>(this);
        tagRewriter.registerGeneric(ClientboundPackets1_19_4.TAGS);
        final SoundRewriter<ClientboundPackets1_19_4> soundRewriter = new SoundRewriter<ClientboundPackets1_19_4>(this);
        soundRewriter.register1_19_3Sound(ClientboundPackets1_19_4.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_19_4.ENTITY_SOUND);
        new StatisticsRewriter<ClientboundPackets1_19_4>(this).register(ClientboundPackets1_19_4.STATISTICS);
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_4.COMBAT_END, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.read((Type<Object>)Type.INT);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_19_4.COMBAT_KILL, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.read((Type<Object>)Type.INT);
        });
    }
    
    @Override
    protected void onMappingDataLoaded() {
        super.onMappingDataLoaded();
        Types1_20.PARTICLE.filler(this).reader("block", ParticleType.Readers.BLOCK).reader("block_marker", ParticleType.Readers.BLOCK).reader("dust", ParticleType.Readers.DUST).reader("falling_dust", ParticleType.Readers.BLOCK).reader("dust_color_transition", ParticleType.Readers.DUST_TRANSITION).reader("item", ParticleType.Readers.VAR_INT_ITEM).reader("vibration", ParticleType.Readers.VIBRATION).reader("sculk_charge", ParticleType.Readers.SCULK_CHARGE).reader("shriek", ParticleType.Readers.SHRIEK);
    }
    
    @Override
    public void init(final UserConnection user) {
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_19_4Types.PLAYER));
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_20To1_19_4.MAPPINGS;
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
        MAPPINGS = new MappingDataBase("1.19.4", "1.20");
    }
}
