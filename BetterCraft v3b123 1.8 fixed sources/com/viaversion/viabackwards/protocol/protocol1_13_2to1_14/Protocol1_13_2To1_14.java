// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.DifficultyStorage;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets.SoundPackets1_14;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets.PlayerPackets1_14;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.data.CommandRewriter1_14;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets.BlockItemPackets1_14;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets.EntityPackets1_14;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_13_2To1_14 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets1_13, ServerboundPackets1_14, ServerboundPackets1_13>
{
    public static final BackwardsMappings MAPPINGS;
    private final EntityPackets1_14 entityRewriter;
    private final BlockItemPackets1_14 blockItemPackets;
    private final TranslatableRewriter<ClientboundPackets1_14> translatableRewriter;
    
    public Protocol1_13_2To1_14() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_13.class, ServerboundPackets1_14.class, ServerboundPackets1_13.class);
        this.entityRewriter = new EntityPackets1_14(this);
        this.blockItemPackets = new BlockItemPackets1_14(this);
        this.translatableRewriter = new TranslatableRewriter<ClientboundPackets1_14>(this);
    }
    
    @Override
    protected void registerPackets() {
        super.registerPackets();
        this.translatableRewriter.registerBossBar(ClientboundPackets1_14.BOSSBAR);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_14.CHAT_MESSAGE);
        this.translatableRewriter.registerCombatEvent(ClientboundPackets1_14.COMBAT_EVENT);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_14.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_14.TAB_LIST);
        this.translatableRewriter.registerTitle(ClientboundPackets1_14.TITLE);
        this.translatableRewriter.registerPing();
        new CommandRewriter1_14(this).registerDeclareCommands(ClientboundPackets1_14.DECLARE_COMMANDS);
        new PlayerPackets1_14(this).register();
        new SoundPackets1_14(this).register();
        new StatisticsRewriter<ClientboundPackets1_14>(this).register(ClientboundPackets1_14.STATISTICS);
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_14.UPDATE_VIEW_POSITION);
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_14.UPDATE_VIEW_DISTANCE);
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)this).cancelClientbound(ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING);
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_14.TAGS, wrapper -> {
            for (int blockTagsSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < blockTagsSize; ++i) {
                wrapper.passthrough(Type.STRING);
                final int[] blockIds = wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                for (int j = 0; j < blockIds.length; ++j) {
                    final int id = blockIds[j];
                    final int blockId = Protocol1_13_2To1_14.MAPPINGS.getNewBlockId(id);
                    blockIds[j] = blockId;
                }
            }
            for (int itemTagsSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), k = 0; k < itemTagsSize; ++k) {
                wrapper.passthrough(Type.STRING);
                final int[] itemIds = wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                for (int l = 0; l < itemIds.length; ++l) {
                    final int itemId = itemIds[l];
                    final int oldId = Protocol1_13_2To1_14.MAPPINGS.getItemMappings().getNewId(itemId);
                    itemIds[l] = oldId;
                }
            }
            for (int fluidTagsSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), m = 0; m < fluidTagsSize; ++m) {
                wrapper.passthrough(Type.STRING);
                wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
            }
            for (int entityTagsSize = wrapper.read((Type<Integer>)Type.VAR_INT), i2 = 0; i2 < entityTagsSize; ++i2) {
                wrapper.read(Type.STRING);
                wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
            }
            return;
        });
        ((Protocol<ClientboundPackets1_14, ClientboundPackets1_13, SM, SU>)this).registerClientbound(ClientboundPackets1_14.UPDATE_LIGHT, null, wrapper -> {
            final int x = wrapper.read((Type<Integer>)Type.VAR_INT);
            final int z = wrapper.read((Type<Integer>)Type.VAR_INT);
            final int skyLightMask = wrapper.read((Type<Integer>)Type.VAR_INT);
            final int blockLightMask = wrapper.read((Type<Integer>)Type.VAR_INT);
            final int emptySkyLightMask = wrapper.read((Type<Integer>)Type.VAR_INT);
            final int emptyBlockLightMask = wrapper.read((Type<Integer>)Type.VAR_INT);
            final byte[][] skyLight = new byte[16][];
            if (isSet(skyLightMask, 0)) {
                wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
            }
            for (int i3 = 0; i3 < 16; ++i3) {
                if (isSet(skyLightMask, i3 + 1)) {
                    skyLight[i3] = wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                }
                else if (isSet(emptySkyLightMask, i3 + 1)) {
                    skyLight[i3] = ChunkLightStorage.EMPTY_LIGHT;
                }
            }
            if (isSet(skyLightMask, 17)) {
                wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
            }
            final byte[][] blockLight = new byte[16][];
            if (isSet(blockLightMask, 0)) {
                wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
            }
            for (int i4 = 0; i4 < 16; ++i4) {
                if (isSet(blockLightMask, i4 + 1)) {
                    blockLight[i4] = wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                }
                else if (isSet(emptyBlockLightMask, i4 + 1)) {
                    blockLight[i4] = ChunkLightStorage.EMPTY_LIGHT;
                }
            }
            if (isSet(blockLightMask, 17)) {
                wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
            }
            wrapper.user().get(ChunkLightStorage.class).setStoredLight(skyLight, blockLight, x, z);
            wrapper.cancel();
        });
    }
    
    private static boolean isSet(final int mask, final int i) {
        return (mask & 1 << i) != 0x0;
    }
    
    @Override
    public void init(final UserConnection user) {
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_14Types.PLAYER));
        if (!user.has(ChunkLightStorage.class)) {
            user.put(new ChunkLightStorage(user));
        }
        user.put(new DifficultyStorage(user));
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_13_2To1_14.MAPPINGS;
    }
    
    @Override
    public EntityPackets1_14 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public BlockItemPackets1_14 getItemRewriter() {
        return this.blockItemPackets;
    }
    
    @Override
    public TranslatableRewriter<ClientboundPackets1_14> getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.14", "1.13.2", Protocol1_14To1_13_2.class);
    }
}
