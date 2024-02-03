// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_15to1_14_4;

import com.viaversion.viaversion.api.data.MappingDataBase;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_15Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.SoundRewriter;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.packets.WorldPackets;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.packets.EntityPackets;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.metadata.MetadataRewriter1_15To1_14_4;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_15To1_14_4 extends AbstractProtocol<ClientboundPackets1_14, ClientboundPackets1_15, ServerboundPackets1_14, ServerboundPackets1_14>
{
    public static final MappingData MAPPINGS;
    private final MetadataRewriter1_15To1_14_4 metadataRewriter;
    private final InventoryPackets itemRewriter;
    private TagRewriter<ClientboundPackets1_14> tagRewriter;
    
    public Protocol1_15To1_14_4() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_15.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
        this.metadataRewriter = new MetadataRewriter1_15To1_14_4(this);
        this.itemRewriter = new InventoryPackets(this);
    }
    
    @Override
    protected void registerPackets() {
        this.metadataRewriter.register();
        this.itemRewriter.register();
        EntityPackets.register(this);
        WorldPackets.register(this);
        final SoundRewriter<ClientboundPackets1_14> soundRewriter = new SoundRewriter<ClientboundPackets1_14>(this);
        soundRewriter.registerSound(ClientboundPackets1_14.ENTITY_SOUND);
        soundRewriter.registerSound(ClientboundPackets1_14.SOUND);
        new StatisticsRewriter<ClientboundPackets1_14>(this).register(ClientboundPackets1_14.STATISTICS);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this).registerServerbound(ServerboundPackets1_14.EDIT_BOOK, wrapper -> this.itemRewriter.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
        (this.tagRewriter = new TagRewriter<ClientboundPackets1_14>(this)).register(ClientboundPackets1_14.TAGS, RegistryType.ENTITY);
    }
    
    @Override
    protected void onMappingDataLoaded() {
        final int[] shulkerBoxes = new int[17];
        final int shulkerBoxOffset = 501;
        for (int i = 0; i < 17; ++i) {
            shulkerBoxes[i] = shulkerBoxOffset + i;
        }
        this.tagRewriter.addTag(RegistryType.BLOCK, "minecraft:shulker_boxes", shulkerBoxes);
    }
    
    @Override
    public void init(final UserConnection connection) {
        this.addEntityTracker(connection, new EntityTrackerBase(connection, Entity1_15Types.PLAYER));
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_15To1_14_4.MAPPINGS;
    }
    
    @Override
    public MetadataRewriter1_15To1_14_4 getEntityRewriter() {
        return this.metadataRewriter;
    }
    
    @Override
    public InventoryPackets getItemRewriter() {
        return this.itemRewriter;
    }
    
    static {
        MAPPINGS = new MappingDataBase("1.14", "1.15");
    }
}
