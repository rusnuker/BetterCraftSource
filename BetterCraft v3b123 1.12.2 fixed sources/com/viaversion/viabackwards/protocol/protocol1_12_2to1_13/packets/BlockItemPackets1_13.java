// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.google.common.primitives.Ints;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import java.util.List;
import java.util.Collection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import java.util.Locale;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viabackwards.api.rewriters.EnchantmentRewriter;
import java.util.ArrayList;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.BlockIdData;
import com.viaversion.viaversion.libs.opennbt.conversion.ConverterRegistry;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import java.util.Optional;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Iterator;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.FlowerPotHandler;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.SpawnEggRewriter;
import com.viaversion.viaversion.api.type.Type;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_13 extends ItemRewriter<ClientboundPackets1_13, ServerboundPackets1_12_1, Protocol1_12_2To1_13>
{
    private final Map<String, String> enchantmentMappings;
    private final String extraNbtTag;
    
    public BlockItemPackets1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol);
        this.enchantmentMappings = new HashMap<String, String>();
        this.extraNbtTag = "VB|" + protocol.getClass().getSimpleName() + "|2";
    }
    
    public static boolean isDamageable(final int id) {
        return (id >= 256 && id <= 259) || id == 261 || (id >= 267 && id <= 279) || (id >= 283 && id <= 286) || (id >= 290 && id <= 294) || (id >= 298 && id <= 317) || id == 346 || id == 359 || id == 398 || id == 442 || id == 443;
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.COOLDOWN, wrapper -> {
            final int itemId = wrapper.read((Type<Integer>)Type.VAR_INT);
            final int oldId = ((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().getNewId(itemId);
            if (oldId == -1) {
                wrapper.cancel();
                return;
            }
            else if (SpawnEggRewriter.getEntityId(oldId).isPresent()) {
                wrapper.write(Type.VAR_INT, 6128);
                return;
            }
            else {
                wrapper.write(Type.VAR_INT, oldId >> 4);
                return;
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.BLOCK_ACTION, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    int blockId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (blockId == 73) {
                        blockId = 25;
                    }
                    else if (blockId == 99) {
                        blockId = 33;
                    }
                    else if (blockId == 92) {
                        blockId = 29;
                    }
                    else if (blockId == 142) {
                        blockId = 54;
                    }
                    else if (blockId == 305) {
                        blockId = 146;
                    }
                    else if (blockId == 249) {
                        blockId = 130;
                    }
                    else if (blockId == 257) {
                        blockId = 138;
                    }
                    else if (blockId == 140) {
                        blockId = 52;
                    }
                    else if (blockId == 472) {
                        blockId = 209;
                    }
                    else if (blockId >= 483 && blockId <= 498) {
                        blockId = blockId - 483 + 219;
                    }
                    wrapper.set(Type.VAR_INT, 0, blockId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.BLOCK_ENTITY_DATA, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(wrapper -> {
                    final BackwardsBlockEntityProvider provider = Via.getManager().getProviders().get(BackwardsBlockEntityProvider.class);
                    if (wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0) == 5) {
                        wrapper.cancel();
                    }
                    wrapper.set(Type.NBT, 0, provider.transform(wrapper.user(), wrapper.get(Type.POSITION, 0), wrapper.get(Type.NBT, 0)));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.UNLOAD_CHUNK, wrapper -> {
            final int chunkMinX = wrapper.passthrough((Type<Integer>)Type.INT) << 4;
            final int chunkMinZ = wrapper.passthrough((Type<Integer>)Type.INT) << 4;
            final int chunkMaxX = chunkMinX + 15;
            final int chunkMaxZ = chunkMinZ + 15;
            final BackwardsBlockStorage blockStorage = wrapper.user().get(BackwardsBlockStorage.class);
            blockStorage.getBlocks().entrySet().removeIf(entry -> {
                final Position position = entry.getKey();
                return position.x() >= chunkMinX && position.z() >= chunkMinZ && position.x() <= chunkMaxX && position.z() <= chunkMaxZ;
            });
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.BLOCK_CHANGE, new PacketHandlers() {
            public void register() {
                this.map(Type.POSITION);
                this.handler(wrapper -> {
                    final int blockState = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final Position position = wrapper.get(Type.POSITION, 0);
                    final BackwardsBlockStorage storage = wrapper.user().get(BackwardsBlockStorage.class);
                    storage.checkAndStore(position, blockState);
                    wrapper.write(Type.VAR_INT, ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(blockState));
                    flowerPotSpecialTreatment(wrapper.user(), blockState, position);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.MULTI_BLOCK_CHANGE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler(wrapper -> {
                    final BackwardsBlockStorage storage = wrapper.user().get(BackwardsBlockStorage.class);
                    final BlockChangeRecord[] array = wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        final int chunkX = wrapper.get((Type<Integer>)Type.INT, 0);
                        final int chunkZ = wrapper.get((Type<Integer>)Type.INT, 1);
                        final int block = record.getBlockId();
                        final Position position = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
                        storage.checkAndStore(position, block);
                        flowerPotSpecialTreatment(wrapper.user(), block, position);
                        record.setBlockId(((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(block));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.WINDOW_ITEMS, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.FLAT_ITEM_ARRAY, Type.ITEM_ARRAY);
                this.handler(BlockItemPackets1_13.this.itemArrayHandler(Type.ITEM_ARRAY));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.SET_SLOT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map(Type.FLAT_ITEM, Type.ITEM);
                this.handler(BlockItemPackets1_13.this.itemToClientHandler(Type.ITEM));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.CHUNK_DATA, wrapper -> {
            final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
            final Chunk1_9_3_4Type type_old = new Chunk1_9_3_4Type(clientWorld);
            final Chunk1_13Type type = new Chunk1_13Type(clientWorld);
            final Chunk chunk = wrapper.read((Type<Chunk>)type);
            final BackwardsBlockEntityProvider provider = Via.getManager().getProviders().get(BackwardsBlockEntityProvider.class);
            final BackwardsBlockStorage storage = wrapper.user().get(BackwardsBlockStorage.class);
            chunk.getBlockEntities().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final CompoundTag tag = iterator.next();
                final Tag idTag = tag.get("id");
                if (idTag == null) {
                    continue;
                }
                else {
                    final String id = (String)idTag.getValue();
                    if (!provider.isHandled(id)) {
                        continue;
                    }
                    else {
                        final int sectionIndex = tag.get("y").asInt() >> 4;
                        if (sectionIndex >= 0) {
                            if (sectionIndex > 15) {
                                continue;
                            }
                            else {
                                final ChunkSection section = chunk.getSections()[sectionIndex];
                                final int x = tag.get("x").asInt();
                                final int y = tag.get("y").asInt();
                                final int z = tag.get("z").asInt();
                                final Position position2 = new Position(x, (short)y, z);
                                final int block = section.palette(PaletteType.BLOCKS).idAt(x & 0xF, y & 0xF, z & 0xF);
                                storage.checkAndStore(position2, block);
                                provider.transform(wrapper.user(), position2, tag);
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
            for (int i = 0; i < chunk.getSections().length; ++i) {
                final ChunkSection section2 = chunk.getSections()[i];
                if (section2 != null) {
                    final DataPalette palette = section2.palette(PaletteType.BLOCKS);
                    for (int y2 = 0; y2 < 16; ++y2) {
                        for (int z2 = 0; z2 < 16; ++z2) {
                            for (int x2 = 0; x2 < 16; ++x2) {
                                final int block2 = palette.idAt(x2, y2, z2);
                                if (FlowerPotHandler.isFlowah(block2)) {
                                    final Position pos = new Position(x2 + (chunk.getX() << 4), (short)(y2 + (i << 4)), z2 + (chunk.getZ() << 4));
                                    storage.checkAndStore(pos, block2);
                                    final CompoundTag nbt = provider.transform(wrapper.user(), pos, "minecraft:flower_pot");
                                    chunk.getBlockEntities().add(nbt);
                                }
                            }
                        }
                    }
                    for (int j = 0; j < palette.size(); ++j) {
                        final int mappedBlockStateId = ((Protocol1_12_2To1_13)this.protocol).getMappingData().getNewBlockStateId(palette.idByIndex(j));
                        palette.setIdByIndex(j, mappedBlockStateId);
                    }
                }
            }
            if (chunk.isBiomeData()) {
                for (int k = 0; k < 256; ++k) {
                    final int biome = chunk.getBiomeData()[k];
                    int newId = -1;
                    switch (biome) {
                        case 40:
                        case 41:
                        case 42:
                        case 43: {
                            newId = 9;
                            break;
                        }
                        case 47:
                        case 48:
                        case 49: {
                            newId = 24;
                            break;
                        }
                        case 50: {
                            newId = 10;
                            break;
                        }
                        case 44:
                        case 45:
                        case 46: {
                            newId = 0;
                            break;
                        }
                    }
                    if (newId != -1) {
                        chunk.getBiomeData()[k] = newId;
                    }
                }
            }
            wrapper.write((Type<Chunk>)type_old, chunk);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.EFFECT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map(Type.POSITION);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                    if (id == 1010) {
                        wrapper.set(Type.INT, 1, ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getItemMappings().getNewId(data) >> 4);
                    }
                    else if (id == 2001) {
                        final int data2 = ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(data);
                        final int blockId = data2 >> 4;
                        final int blockData = data2 & 0xF;
                        wrapper.set(Type.INT, 1, (blockId & 0xFFF) | blockData << 12);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.MAP_DATA, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(wrapper -> {
                    for (int iconCount = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < iconCount; ++i) {
                        final int type = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final byte x = wrapper.read((Type<Byte>)Type.BYTE);
                        final byte z = wrapper.read((Type<Byte>)Type.BYTE);
                        final byte direction = wrapper.read((Type<Byte>)Type.BYTE);
                        if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.read(Type.COMPONENT);
                        }
                        if (type > 9) {
                            wrapper.set(Type.VAR_INT, 1, wrapper.get((Type<Integer>)Type.VAR_INT, 1) - 1);
                        }
                        else {
                            wrapper.write(Type.BYTE, (byte)(type << 4 | (direction & 0xF)));
                            wrapper.write(Type.BYTE, x);
                            wrapper.write(Type.BYTE, z);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.ENTITY_EQUIPMENT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.FLAT_ITEM, Type.ITEM);
                this.handler(BlockItemPackets1_13.this.itemToClientHandler(Type.ITEM));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.WINDOW_PROPERTY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.handler(wrapper -> {
                    final short property = wrapper.get((Type<Short>)Type.SHORT, 0);
                    if (property >= 4 && property <= 6) {
                        final short oldId = wrapper.get((Type<Short>)Type.SHORT, 1);
                        wrapper.set(Type.SHORT, 1, (short)((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getEnchantmentMappings().getNewId(oldId));
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_12_1>)this.protocol).registerServerbound(ServerboundPackets1_12_1.CREATIVE_INVENTORY_ACTION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.SHORT);
                this.map(Type.ITEM, Type.FLAT_ITEM);
                this.handler(BlockItemPackets1_13.this.itemToServerHandler(Type.FLAT_ITEM));
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_12_1>)this.protocol).registerServerbound(ServerboundPackets1_12_1.CLICK_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.ITEM, Type.FLAT_ITEM);
                this.handler(BlockItemPackets1_13.this.itemToServerHandler(Type.FLAT_ITEM));
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.enchantmentMappings.put("minecraft:loyalty", "�7Loyalty");
        this.enchantmentMappings.put("minecraft:impaling", "�7Impaling");
        this.enchantmentMappings.put("minecraft:riptide", "�7Riptide");
        this.enchantmentMappings.put("minecraft:channeling", "�7Channeling");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        final int originalId = item.identifier();
        Integer rawId = null;
        boolean gotRawIdFromTag = false;
        CompoundTag tag = item.tag();
        final Tag originalIdTag;
        if (tag != null && (originalIdTag = tag.remove(this.extraNbtTag)) != null) {
            rawId = ((NumberTag)originalIdTag).asInt();
            gotRawIdFromTag = true;
        }
        if (rawId == null) {
            super.handleItemToClient(item);
            if (item.identifier() == -1) {
                if (originalId == 362) {
                    rawId = 15007744;
                }
                else {
                    if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                        ViaBackwards.getPlatform().getLogger().warning("Failed to get 1.12 item for " + originalId);
                    }
                    rawId = 65536;
                }
            }
            else {
                if (tag == null) {
                    tag = item.tag();
                }
                rawId = this.itemIdToRaw(item.identifier(), item, tag);
            }
        }
        item.setIdentifier(rawId >> 16);
        item.setData((short)(rawId & 0xFFFF));
        if (tag != null) {
            if (isDamageable(item.identifier())) {
                final Tag damageTag = tag.remove("Damage");
                if (!gotRawIdFromTag && damageTag instanceof IntTag) {
                    item.setData((short)(int)damageTag.getValue());
                }
            }
            if (item.identifier() == 358) {
                final Tag mapTag = tag.remove("map");
                if (!gotRawIdFromTag && mapTag instanceof IntTag) {
                    item.setData((short)(int)mapTag.getValue());
                }
            }
            this.invertShieldAndBannerId(item, tag);
            final CompoundTag display = tag.get("display");
            if (display != null) {
                final StringTag name = display.get("Name");
                if (name != null) {
                    display.put(this.extraNbtTag + "|Name", new StringTag(name.getValue()));
                    name.setValue(((Protocol1_12_2To1_13)this.protocol).jsonToLegacy(name.getValue()));
                }
            }
            this.rewriteEnchantmentsToClient(tag, false);
            this.rewriteEnchantmentsToClient(tag, true);
            this.rewriteCanPlaceToClient(tag, "CanPlaceOn");
            this.rewriteCanPlaceToClient(tag, "CanDestroy");
        }
        return item;
    }
    
    private int itemIdToRaw(final int oldId, final Item item, CompoundTag tag) {
        final Optional<String> eggEntityId = SpawnEggRewriter.getEntityId(oldId);
        if (eggEntityId.isPresent()) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag());
            }
            if (!tag.contains("EntityTag")) {
                final CompoundTag entityTag = new CompoundTag();
                entityTag.put("id", new StringTag(eggEntityId.get()));
                tag.put("EntityTag", entityTag);
            }
            return 25100288;
        }
        return oldId >> 4 << 16 | (oldId & 0xF);
    }
    
    private void rewriteCanPlaceToClient(final CompoundTag tag, final String tagName) {
        if (!(tag.get(tagName) instanceof ListTag)) {
            return;
        }
        final ListTag blockTag = tag.get(tagName);
        if (blockTag == null) {
            return;
        }
        final ListTag newCanPlaceOn = new ListTag(StringTag.class);
        tag.put(this.extraNbtTag + "|" + tagName, ConverterRegistry.convertToTag(ConverterRegistry.convertToValue(blockTag)));
        for (final Tag oldTag : blockTag) {
            final Object value = oldTag.getValue();
            final String[] newValues = (value instanceof String) ? BlockIdData.fallbackReverseMapping.get(((String)value).replace("minecraft:", "")) : null;
            if (newValues != null) {
                for (final String newValue : newValues) {
                    newCanPlaceOn.add(new StringTag(newValue));
                }
            }
            else {
                newCanPlaceOn.add(oldTag);
            }
        }
        tag.put(tagName, newCanPlaceOn);
    }
    
    private void rewriteEnchantmentsToClient(final CompoundTag tag, final boolean storedEnch) {
        final String key = storedEnch ? "StoredEnchantments" : "Enchantments";
        final ListTag enchantments = tag.get(key);
        if (enchantments == null) {
            return;
        }
        final ListTag noMapped = new ListTag(CompoundTag.class);
        final ListTag newEnchantments = new ListTag(CompoundTag.class);
        final List<Tag> lore = new ArrayList<Tag>();
        boolean hasValidEnchants = false;
        for (final Tag enchantmentEntryTag : enchantments.clone()) {
            final CompoundTag enchantmentEntry = (CompoundTag)enchantmentEntryTag;
            final Tag idTag = enchantmentEntry.get("id");
            if (!(idTag instanceof StringTag)) {
                continue;
            }
            final String newId = (String)idTag.getValue();
            final NumberTag levelTag = enchantmentEntry.get("lvl");
            if (levelTag == null) {
                continue;
            }
            final int levelValue = levelTag.asInt();
            final short level = (short)((levelValue < 32767) ? ((short)levelValue) : 32767);
            final String mappedEnchantmentId = this.enchantmentMappings.get(newId);
            if (mappedEnchantmentId != null) {
                lore.add(new StringTag(mappedEnchantmentId + " " + EnchantmentRewriter.getRomanNumber(level)));
                noMapped.add(enchantmentEntry);
            }
            else {
                if (newId.isEmpty()) {
                    continue;
                }
                Short oldId = Protocol1_13To1_12_2.MAPPINGS.getOldEnchantmentsIds().inverse().get(Key.stripMinecraftNamespace(newId));
                if (oldId == null) {
                    if (!newId.startsWith("viaversion:legacy/")) {
                        noMapped.add(enchantmentEntry);
                        if (ViaBackwards.getConfig().addCustomEnchantsToLore()) {
                            String name = newId;
                            final int index = name.indexOf(58) + 1;
                            if (index != 0 && index != name.length()) {
                                name = name.substring(index);
                            }
                            name = "�7" + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase(Locale.ENGLISH);
                            lore.add(new StringTag(name + " " + EnchantmentRewriter.getRomanNumber(level)));
                        }
                        if (Via.getManager().isDebug()) {
                            ViaBackwards.getPlatform().getLogger().warning("Found unknown enchant: " + newId);
                            continue;
                        }
                        continue;
                    }
                    else {
                        oldId = Short.valueOf(newId.substring(18));
                    }
                }
                if (level != 0) {
                    hasValidEnchants = true;
                }
                final CompoundTag newEntry = new CompoundTag();
                newEntry.put("id", new ShortTag(oldId));
                newEntry.put("lvl", new ShortTag(level));
                newEnchantments.add(newEntry);
            }
        }
        if (!storedEnch && !hasValidEnchants) {
            IntTag hideFlags = tag.get("HideFlags");
            if (hideFlags == null) {
                hideFlags = new IntTag();
                tag.put(this.extraNbtTag + "|DummyEnchant", new ByteTag());
            }
            else {
                tag.put(this.extraNbtTag + "|OldHideFlags", new IntTag(hideFlags.asByte()));
            }
            if (newEnchantments.size() == 0) {
                final CompoundTag enchEntry = new CompoundTag();
                enchEntry.put("id", new ShortTag((short)0));
                enchEntry.put("lvl", new ShortTag((short)0));
                newEnchantments.add(enchEntry);
            }
            final int value = hideFlags.asByte() | 0x1;
            hideFlags.setValue(value);
            tag.put("HideFlags", hideFlags);
        }
        if (noMapped.size() != 0) {
            tag.put(this.extraNbtTag + "|" + key, noMapped);
            if (!lore.isEmpty()) {
                CompoundTag display = tag.get("display");
                if (display == null) {
                    tag.put("display", display = new CompoundTag());
                }
                ListTag loreTag = display.get("Lore");
                if (loreTag == null) {
                    display.put("Lore", loreTag = new ListTag(StringTag.class));
                    tag.put(this.extraNbtTag + "|DummyLore", new ByteTag());
                }
                else if (loreTag.size() != 0) {
                    final ListTag oldLore = new ListTag(StringTag.class);
                    for (final Tag value2 : loreTag) {
                        oldLore.add(value2.clone());
                    }
                    tag.put(this.extraNbtTag + "|OldLore", oldLore);
                    lore.addAll(loreTag.getValue());
                }
                loreTag.setValue(lore);
            }
        }
        tag.remove("Enchantments");
        tag.put(storedEnch ? key : "ench", newEnchantments);
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag tag = item.tag();
        final int originalId = item.identifier() << 16 | (item.data() & 0xFFFF);
        int rawId = item.identifier() << 4 | (item.data() & 0xF);
        if (isDamageable(item.identifier())) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag());
            }
            tag.put("Damage", new IntTag(item.data()));
        }
        if (item.identifier() == 358) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag());
            }
            tag.put("map", new IntTag(item.data()));
        }
        if (tag != null) {
            this.invertShieldAndBannerId(item, tag);
            final Tag display = tag.get("display");
            if (display instanceof CompoundTag) {
                final CompoundTag displayTag = (CompoundTag)display;
                final StringTag name = displayTag.get("Name");
                if (name != null) {
                    final StringTag via = displayTag.remove(this.extraNbtTag + "|Name");
                    name.setValue((via != null) ? via.getValue() : ChatRewriter.legacyTextToJsonString(name.getValue()));
                }
            }
            this.rewriteEnchantmentsToServer(tag, false);
            this.rewriteEnchantmentsToServer(tag, true);
            this.rewriteCanPlaceToServer(tag, "CanPlaceOn");
            this.rewriteCanPlaceToServer(tag, "CanDestroy");
            if (item.identifier() == 383) {
                final CompoundTag entityTag = tag.get("EntityTag");
                final StringTag identifier;
                if (entityTag != null && (identifier = entityTag.get("id")) != null) {
                    rawId = SpawnEggRewriter.getSpawnEggId(identifier.getValue());
                    if (rawId == -1) {
                        rawId = 25100288;
                    }
                    else {
                        entityTag.remove("id");
                        if (entityTag.isEmpty()) {
                            tag.remove("EntityTag");
                        }
                    }
                }
                else {
                    rawId = 25100288;
                }
            }
            if (tag.isEmpty()) {
                item.setTag(tag = null);
            }
        }
        final int identifier2 = item.identifier();
        item.setIdentifier(rawId);
        super.handleItemToServer(item);
        if (item.identifier() != rawId && item.identifier() != -1) {
            return item;
        }
        item.setIdentifier(identifier2);
        int newId = -1;
        if (((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().getNewId(rawId) == -1) {
            if (!isDamageable(item.identifier()) && item.identifier() != 358) {
                if (tag == null) {
                    item.setTag(tag = new CompoundTag());
                }
                tag.put(this.extraNbtTag, new IntTag(originalId));
            }
            if (item.identifier() == 229) {
                newId = 362;
            }
            else if (item.identifier() == 31 && item.data() == 0) {
                rawId = 512;
            }
            else if (((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().getNewId(rawId & 0xFFFFFFF0) != -1) {
                rawId &= 0xFFFFFFF0;
            }
            else {
                if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                    ViaBackwards.getPlatform().getLogger().warning("Failed to get 1.13 item for " + item.identifier());
                }
                rawId = 16;
            }
        }
        if (newId == -1) {
            newId = ((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().getNewId(rawId);
        }
        item.setIdentifier(newId);
        item.setData((short)0);
        return item;
    }
    
    private void rewriteCanPlaceToServer(final CompoundTag tag, final String tagName) {
        if (!(tag.get(tagName) instanceof ListTag)) {
            return;
        }
        ListTag blockTag = tag.remove(this.extraNbtTag + "|" + tagName);
        if (blockTag != null) {
            tag.put(tagName, ConverterRegistry.convertToTag(ConverterRegistry.convertToValue(blockTag)));
        }
        else if ((blockTag = tag.get(tagName)) != null) {
            final ListTag newCanPlaceOn = new ListTag(StringTag.class);
            for (final Tag oldTag : blockTag) {
                final Object value = oldTag.getValue();
                String oldId = value.toString().replace("minecraft:", "");
                final int key = Ints.tryParse(oldId);
                final String numberConverted = BlockIdData.numberIdToString.get(key);
                if (numberConverted != null) {
                    oldId = numberConverted;
                }
                final String lowerCaseId = oldId.toLowerCase(Locale.ROOT);
                final String[] newValues = BlockIdData.blockIdMapping.get(lowerCaseId);
                if (newValues != null) {
                    for (final String newValue : newValues) {
                        newCanPlaceOn.add(new StringTag(newValue));
                    }
                }
                else {
                    newCanPlaceOn.add(new StringTag(lowerCaseId));
                }
            }
            tag.put(tagName, newCanPlaceOn);
        }
    }
    
    private void rewriteEnchantmentsToServer(final CompoundTag tag, final boolean storedEnch) {
        final String key = storedEnch ? "StoredEnchantments" : "Enchantments";
        final ListTag enchantments = tag.get(storedEnch ? key : "ench");
        if (enchantments == null) {
            return;
        }
        final ListTag newEnchantments = new ListTag(CompoundTag.class);
        boolean dummyEnchant = false;
        if (!storedEnch) {
            final IntTag hideFlags = tag.remove(this.extraNbtTag + "|OldHideFlags");
            if (hideFlags != null) {
                tag.put("HideFlags", new IntTag(hideFlags.asByte()));
                dummyEnchant = true;
            }
            else if (tag.remove(this.extraNbtTag + "|DummyEnchant") != null) {
                tag.remove("HideFlags");
                dummyEnchant = true;
            }
        }
        for (final Tag enchEntry : enchantments) {
            final CompoundTag enchantmentEntry = new CompoundTag();
            final short oldId = ((CompoundTag)enchEntry).get("id").asShort();
            final short level = ((CompoundTag)enchEntry).get("lvl").asShort();
            if (dummyEnchant && oldId == 0 && level == 0) {
                continue;
            }
            String newId = Protocol1_13To1_12_2.MAPPINGS.getOldEnchantmentsIds().get(oldId);
            if (newId == null) {
                newId = "viaversion:legacy/" + oldId;
            }
            enchantmentEntry.put("id", new StringTag(newId));
            enchantmentEntry.put("lvl", new ShortTag(level));
            newEnchantments.add(enchantmentEntry);
        }
        final ListTag noMapped = tag.remove(this.extraNbtTag + "|Enchantments");
        if (noMapped != null) {
            for (final Tag value : noMapped) {
                newEnchantments.add(value);
            }
        }
        CompoundTag display = tag.get("display");
        if (display == null) {
            tag.put("display", display = new CompoundTag());
        }
        final ListTag oldLore = tag.remove(this.extraNbtTag + "|OldLore");
        if (oldLore != null) {
            ListTag lore = display.get("Lore");
            if (lore == null) {
                tag.put("Lore", lore = new ListTag());
            }
            lore.setValue(oldLore.getValue());
        }
        else if (tag.remove(this.extraNbtTag + "|DummyLore") != null) {
            display.remove("Lore");
            if (display.isEmpty()) {
                tag.remove("display");
            }
        }
        if (!storedEnch) {
            tag.remove("ench");
        }
        tag.put(key, newEnchantments);
    }
    
    private void invertShieldAndBannerId(final Item item, final CompoundTag tag) {
        if (item.identifier() != 442 && item.identifier() != 425) {
            return;
        }
        final Tag blockEntityTag = tag.get("BlockEntityTag");
        if (!(blockEntityTag instanceof CompoundTag)) {
            return;
        }
        final CompoundTag blockEntityCompoundTag = (CompoundTag)blockEntityTag;
        final Tag base = blockEntityCompoundTag.get("Base");
        if (base instanceof IntTag) {
            final IntTag baseTag = (IntTag)base;
            baseTag.setValue(15 - baseTag.asInt());
        }
        final Tag patterns = blockEntityCompoundTag.get("Patterns");
        if (patterns instanceof ListTag) {
            final ListTag patternsTag = (ListTag)patterns;
            for (final Tag pattern : patternsTag) {
                if (!(pattern instanceof CompoundTag)) {
                    continue;
                }
                final IntTag colorTag = ((CompoundTag)pattern).get("Color");
                colorTag.setValue(15 - colorTag.asInt());
            }
        }
    }
    
    private static void flowerPotSpecialTreatment(final UserConnection user, final int blockState, final Position position) throws Exception {
        if (FlowerPotHandler.isFlowah(blockState)) {
            final BackwardsBlockEntityProvider beProvider = Via.getManager().getProviders().get(BackwardsBlockEntityProvider.class);
            final CompoundTag nbt = beProvider.transform(user, position, "minecraft:flower_pot");
            final PacketWrapper blockUpdateRemove = PacketWrapper.create(ClientboundPackets1_12_1.BLOCK_CHANGE, user);
            blockUpdateRemove.write(Type.POSITION, position);
            blockUpdateRemove.write(Type.VAR_INT, 0);
            blockUpdateRemove.scheduleSend(Protocol1_12_2To1_13.class);
            final PacketWrapper blockCreate = PacketWrapper.create(ClientboundPackets1_12_1.BLOCK_CHANGE, user);
            blockCreate.write(Type.POSITION, position);
            blockCreate.write(Type.VAR_INT, Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(blockState));
            blockCreate.scheduleSend(Protocol1_12_2To1_13.class);
            final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_12_1.BLOCK_ENTITY_DATA, user);
            wrapper.write(Type.POSITION, position);
            wrapper.write(Type.UNSIGNED_BYTE, (Short)5);
            wrapper.write(Type.NBT, nbt);
            wrapper.scheduleSend(Protocol1_12_2To1_13.class);
        }
    }
}
