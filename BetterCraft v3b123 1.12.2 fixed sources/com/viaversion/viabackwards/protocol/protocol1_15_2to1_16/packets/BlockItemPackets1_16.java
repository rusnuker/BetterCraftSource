// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets.InventoryPackets;
import java.util.UUID;
import java.util.Map;
import com.viaversion.viaversion.api.type.types.UUIDIntArrayType;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import java.util.List;
import com.viaversion.viabackwards.api.rewriters.MapColorRewriter;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.MapColorRewrites;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.types.ShortType;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.storage.BiomeStorage;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.util.CompactArrayUtil;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.ArrayList;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.api.rewriters.EnchantmentRewriter;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_16 extends ItemRewriter<ClientboundPackets1_16, ServerboundPackets1_14, Protocol1_15_2To1_16>
{
    private EnchantmentRewriter enchantmentRewriter;
    
    public BlockItemPackets1_16(final Protocol1_15_2To1_16 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final BlockRewriter<ClientboundPackets1_16> blockRewriter = new BlockRewriter<ClientboundPackets1_16>(this.protocol, Type.POSITION1_14);
        final RecipeRewriter<ClientboundPackets1_16> recipeRewriter = new RecipeRewriter<ClientboundPackets1_16>(this.protocol);
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16.DECLARE_RECIPES, wrapper -> {
            int newSize;
            for (int size = newSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                final String originalType = wrapper.read(Type.STRING);
                final String type = Key.stripMinecraftNamespace(originalType);
                if (type.equals("smithing")) {
                    --newSize;
                    wrapper.read(Type.STRING);
                    wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                    wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                    wrapper.read(Type.FLAT_VAR_INT_ITEM);
                }
                else {
                    wrapper.write(Type.STRING, originalType);
                    wrapper.passthrough(Type.STRING);
                    recipeRewriter.handleRecipeType(wrapper, type);
                }
            }
            wrapper.set(Type.VAR_INT, 0, newSize);
            return;
        });
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16, S, T>)this).registerSetCooldown(ClientboundPackets1_16.COOLDOWN);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16, S, T>)this).registerWindowItems(ClientboundPackets1_16.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16, S, T>)this).registerSetSlot(ClientboundPackets1_16.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16, S, T>)this).registerTradeList(ClientboundPackets1_16.TRADE_LIST);
        ((ItemRewriter<ClientboundPackets1_16, S, T>)this).registerAdvancements(ClientboundPackets1_16.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_16.ACKNOWLEDGE_PLAYER_DIGGING);
        blockRewriter.registerBlockAction(ClientboundPackets1_16.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_16.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_16.MULTI_BLOCK_CHANGE);
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16.ENTITY_EQUIPMENT, wrapper -> {
            final int entityId = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
            final List<EquipmentData> equipmentData = new ArrayList<EquipmentData>();
            byte slot;
            do {
                slot = wrapper.read((Type<Byte>)Type.BYTE);
                final Item item = this.handleItemToClient(wrapper.read(Type.FLAT_VAR_INT_ITEM));
                final int rawSlot = slot & 0x7F;
                equipmentData.add(new EquipmentData(rawSlot, item));
            } while ((slot & 0xFFFFFF80) != 0x0);
            final EquipmentData firstData = equipmentData.get(0);
            wrapper.write(Type.VAR_INT, firstData.slot);
            wrapper.write(Type.FLAT_VAR_INT_ITEM, firstData.item);
            for (int j = 1; j < equipmentData.size(); ++j) {
                final PacketWrapper equipmentPacket = wrapper.create(ClientboundPackets1_15.ENTITY_EQUIPMENT);
                final EquipmentData data = equipmentData.get(j);
                equipmentPacket.write(Type.VAR_INT, entityId);
                equipmentPacket.write(Type.VAR_INT, data.slot);
                equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, data.item);
                equipmentPacket.send(Protocol1_15_2To1_16.class);
            }
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16.UPDATE_LIGHT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16.CHUNK_DATA, wrapper -> {
            final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_16Type());
            wrapper.write(new Chunk1_15Type(), chunk);
            for (int k = 0; k < chunk.getSections().length; ++k) {
                final ChunkSection section = chunk.getSections()[k];
                if (section != null) {
                    final DataPalette palette = section.palette(PaletteType.BLOCKS);
                    for (int l = 0; l < palette.size(); ++l) {
                        final int mappedBlockStateId = ((Protocol1_15_2To1_16)this.protocol).getMappingData().getNewBlockStateId(palette.idByIndex(l));
                        palette.setIdByIndex(l, mappedBlockStateId);
                    }
                }
            }
            final CompoundTag heightMaps = chunk.getHeightMap();
            heightMaps.values().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final Tag heightMapTag = iterator.next();
                final LongArrayTag heightMap = (LongArrayTag)heightMapTag;
                final int[] heightMapData = new int[256];
                CompactArrayUtil.iterateCompactArrayWithPadding(9, heightMapData.length, heightMap.getValue(), (i, v) -> heightMapData[i] = v);
                heightMap.setValue(CompactArrayUtil.createCompactArray(9, heightMapData.length, i -> heightMapData[i]));
            }
            if (chunk.isBiomeData()) {
                if (wrapper.user().getProtocolInfo().getServerProtocolVersion() >= ProtocolVersion.v1_16_2.getVersion()) {
                    final BiomeStorage biomeStorage = wrapper.user().get(BiomeStorage.class);
                    for (int m = 0; m < 1024; ++m) {
                        final int biome = chunk.getBiomeData()[m];
                        int legacyBiome = biomeStorage.legacyBiome(biome);
                        if (legacyBiome == -1) {
                            ViaBackwards.getPlatform().getLogger().warning("Biome sent that does not exist in the biome registry: " + biome);
                            legacyBiome = 1;
                        }
                        chunk.getBiomeData()[m] = legacyBiome;
                    }
                }
                else {
                    for (int i2 = 0; i2 < 1024; ++i2) {
                        final int biome2 = chunk.getBiomeData()[i2];
                        switch (biome2) {
                            case 170:
                            case 171:
                            case 172:
                            case 173: {
                                chunk.getBiomeData()[i2] = 8;
                                break;
                            }
                        }
                    }
                }
            }
            if (chunk.getBlockEntities() == null) {
                return;
            }
            else {
                chunk.getBlockEntities().iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final CompoundTag blockEntity = iterator2.next();
                    this.handleBlockEntity(blockEntity);
                }
                return;
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_16.EFFECT, 1010, 2001);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_16, S, T>)this).registerSpawnParticle(ClientboundPackets1_16.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16.WINDOW_PROPERTY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.handler(wrapper -> {
                    final short property = wrapper.get((Type<Short>)Type.SHORT, 0);
                    if (property >= 4 && property <= 6) {
                        final short enchantmentId = wrapper.get((Type<Short>)Type.SHORT, 1);
                        if (enchantmentId > 11) {
                            final ShortType short1 = Type.SHORT;
                            final short enchantmentId2 = (short)(enchantmentId - 1);
                            final int n;
                            wrapper.set(short1, n, enchantmentId2);
                        }
                        else if (enchantmentId == 11) {
                            wrapper.set(Type.SHORT, 1, (Short)9);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16.MAP_DATA, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(MapColorRewriter.getRewriteHandler(MapColorRewrites::getMappedColor));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16.BLOCK_ENTITY_DATA, wrapper -> {
            wrapper.passthrough(Type.POSITION1_14);
            wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
            final CompoundTag tag = wrapper.passthrough(Type.NBT);
            this.handleBlockEntity(tag);
            return;
        });
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_14, T>)this).registerClickWindow(ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_14, T>)this).registerCreativeInvAction(ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_14>)this.protocol).registerServerbound(ServerboundPackets1_14.EDIT_BOOK, wrapper -> this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
    }
    
    private void handleBlockEntity(final CompoundTag tag) {
        final StringTag idTag = tag.get("id");
        if (idTag == null) {
            return;
        }
        final String id = idTag.getValue();
        if (id.equals("minecraft:conduit")) {
            final Tag targetUuidTag = tag.remove("Target");
            if (!(targetUuidTag instanceof IntArrayTag)) {
                return;
            }
            final UUID targetUuid = UUIDIntArrayType.uuidFromIntArray((int[])targetUuidTag.getValue());
            tag.put("target_uuid", new StringTag(targetUuid.toString()));
        }
        else if (id.equals("minecraft:skull")) {
            final Tag skullOwnerTag = tag.remove("SkullOwner");
            if (!(skullOwnerTag instanceof CompoundTag)) {
                return;
            }
            final CompoundTag skullOwnerCompoundTag = (CompoundTag)skullOwnerTag;
            final Tag ownerUuidTag = skullOwnerCompoundTag.remove("Id");
            if (ownerUuidTag instanceof IntArrayTag) {
                final UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])ownerUuidTag.getValue());
                skullOwnerCompoundTag.put("Id", new StringTag(ownerUuid.toString()));
            }
            final CompoundTag ownerTag = new CompoundTag();
            for (final Map.Entry<String, Tag> entry : skullOwnerCompoundTag) {
                ownerTag.put(entry.getKey(), entry.getValue());
            }
            tag.put("Owner", ownerTag);
        }
    }
    
    @Override
    protected void registerRewrites() {
        (this.enchantmentRewriter = new EnchantmentRewriter(this)).registerEnchantment("minecraft:soul_speed", "§7Soul Speed");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        final CompoundTag tag = item.tag();
        if (item.identifier() == 771 && tag != null) {
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof IntArrayTag) {
                    final UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])idTag.getValue());
                    ownerCompundTag.put("Id", new StringTag(ownerUuid.toString()));
                }
            }
        }
        InventoryPackets.newToOldAttributes(item);
        this.enchantmentRewriter.handleToClient(item);
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        final int identifier = item.identifier();
        super.handleItemToServer(item);
        final CompoundTag tag = item.tag();
        if (identifier == 771 && tag != null) {
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof StringTag) {
                    final UUID ownerUuid = UUID.fromString((String)idTag.getValue());
                    ownerCompundTag.put("Id", new IntArrayTag(UUIDIntArrayType.uuidToIntArray(ownerUuid)));
                }
            }
        }
        InventoryPackets.oldToNewAttributes(item);
        this.enchantmentRewriter.handleToServer(item);
        return item;
    }
    
    private static final class EquipmentData
    {
        private final int slot;
        private final Item item;
        
        private EquipmentData(final int slot, final Item item) {
            this.slot = slot;
            this.item = item;
        }
    }
}
