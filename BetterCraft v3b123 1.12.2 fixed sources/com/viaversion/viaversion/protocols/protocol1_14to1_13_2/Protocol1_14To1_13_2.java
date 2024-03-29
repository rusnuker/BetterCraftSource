// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
import com.viaversion.viaversion.rewriter.ComponentRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.data.ComponentRewriter1_14;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.SoundRewriter;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.PlayerPackets;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.WorldPackets;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.EntityPackets;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.metadata.MetadataRewriter1_14To1_13_2;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.data.MappingData;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_14To1_13_2 extends AbstractProtocol<ClientboundPackets1_13, ClientboundPackets1_14, ServerboundPackets1_13, ServerboundPackets1_14>
{
    public static final MappingData MAPPINGS;
    private final MetadataRewriter1_14To1_13_2 metadataRewriter;
    private final InventoryPackets itemRewriter;
    
    public Protocol1_14To1_13_2() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_14.class, ServerboundPackets1_13.class, ServerboundPackets1_14.class);
        this.metadataRewriter = new MetadataRewriter1_14To1_13_2(this);
        this.itemRewriter = new InventoryPackets(this);
    }
    
    @Override
    protected void registerPackets() {
        this.metadataRewriter.register();
        this.itemRewriter.register();
        EntityPackets.register(this);
        WorldPackets.register(this);
        PlayerPackets.register(this);
        new SoundRewriter<ClientboundPackets1_13>(this).registerSound(ClientboundPackets1_13.SOUND);
        new StatisticsRewriter<ClientboundPackets1_13>(this).register(ClientboundPackets1_13.STATISTICS);
        final ComponentRewriter<ClientboundPackets1_13> componentRewriter = new ComponentRewriter1_14<ClientboundPackets1_13>(this);
        componentRewriter.registerComponentPacket(ClientboundPackets1_13.CHAT_MESSAGE);
        final CommandRewriter<ClientboundPackets1_13> commandRewriter = new CommandRewriter<ClientboundPackets1_13>(this) {
            @Override
            public String handleArgumentType(final String argumentType) {
                if (argumentType.equals("minecraft:nbt")) {
                    return "minecraft:nbt_compound_tag";
                }
                return super.handleArgumentType(argumentType);
            }
        };
        commandRewriter.registerDeclareCommands(ClientboundPackets1_13.DECLARE_COMMANDS);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_13.TAGS, wrapper -> {
            final int blockTagsSize = wrapper.read((Type<Integer>)Type.VAR_INT);
            wrapper.write(Type.VAR_INT, blockTagsSize + 6);
            for (int i = 0; i < blockTagsSize; ++i) {
                wrapper.passthrough(Type.STRING);
                final int[] blockIds = wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                for (int j = 0; j < blockIds.length; ++j) {
                    blockIds[j] = Protocol1_14To1_13_2.MAPPINGS.getNewBlockId(blockIds[j]);
                }
            }
            wrapper.write(Type.STRING, "minecraft:signs");
            wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { Protocol1_14To1_13_2.MAPPINGS.getNewBlockId(150), Protocol1_14To1_13_2.MAPPINGS.getNewBlockId(155) });
            wrapper.write(Type.STRING, "minecraft:wall_signs");
            wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { Protocol1_14To1_13_2.MAPPINGS.getNewBlockId(155) });
            wrapper.write(Type.STRING, "minecraft:standing_signs");
            wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { Protocol1_14To1_13_2.MAPPINGS.getNewBlockId(150) });
            wrapper.write(Type.STRING, "minecraft:fences");
            wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { 189, 248, 472, 473, 474, 475 });
            wrapper.write(Type.STRING, "minecraft:walls");
            wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { 271, 272 });
            wrapper.write(Type.STRING, "minecraft:wooden_fences");
            wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { 189, 472, 473, 474, 475 });
            final int itemTagsSize = wrapper.read((Type<Integer>)Type.VAR_INT);
            wrapper.write(Type.VAR_INT, itemTagsSize + 2);
            for (int k = 0; k < itemTagsSize; ++k) {
                wrapper.passthrough(Type.STRING);
                final int[] itemIds = wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                for (int l = 0; l < itemIds.length; ++l) {
                    itemIds[l] = Protocol1_14To1_13_2.MAPPINGS.getNewItemId(itemIds[l]);
                }
            }
            wrapper.write(Type.STRING, "minecraft:signs");
            wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { Protocol1_14To1_13_2.MAPPINGS.getNewItemId(541) });
            wrapper.write(Type.STRING, "minecraft:arrows");
            wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { 526, 825, 826 });
            for (int fluidTagsSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), m = 0; m < fluidTagsSize; ++m) {
                wrapper.passthrough(Type.STRING);
                wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
            }
            wrapper.write(Type.VAR_INT, 0);
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this).cancelServerbound(ServerboundPackets1_14.SET_DIFFICULTY);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this).cancelServerbound(ServerboundPackets1_14.LOCK_DIFFICULTY);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this).cancelServerbound(ServerboundPackets1_14.UPDATE_JIGSAW_BLOCK);
    }
    
    @Override
    protected void onMappingDataLoaded() {
        WorldPackets.air = Protocol1_14To1_13_2.MAPPINGS.getBlockStateMappings().getNewId(0);
        WorldPackets.voidAir = Protocol1_14To1_13_2.MAPPINGS.getBlockStateMappings().getNewId(8591);
        WorldPackets.caveAir = Protocol1_14To1_13_2.MAPPINGS.getBlockStateMappings().getNewId(8592);
        Types1_13_2.PARTICLE.filler(this, false).reader("block", ParticleType.Readers.BLOCK).reader("dust", ParticleType.Readers.DUST).reader("falling_dust", ParticleType.Readers.BLOCK).reader("item", ParticleType.Readers.VAR_INT_ITEM);
        Types1_14.PARTICLE.filler(this).reader("block", ParticleType.Readers.BLOCK).reader("dust", ParticleType.Readers.DUST).reader("falling_dust", ParticleType.Readers.BLOCK).reader("item", ParticleType.Readers.VAR_INT_ITEM);
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.addEntityTracker(this.getClass(), new EntityTracker1_14(userConnection));
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_14To1_13_2.MAPPINGS;
    }
    
    @Override
    public MetadataRewriter1_14To1_13_2 getEntityRewriter() {
        return this.metadataRewriter;
    }
    
    @Override
    public InventoryPackets getItemRewriter() {
        return this.itemRewriter;
    }
    
    static {
        MAPPINGS = new MappingData();
    }
}
