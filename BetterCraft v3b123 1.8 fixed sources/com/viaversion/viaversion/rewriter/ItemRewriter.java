// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.rewriter.RewriterBase;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public abstract class ItemRewriter<C extends ClientboundPacketType, S extends ServerboundPacketType, T extends Protocol<C, ?, ?, S>> extends RewriterBase<T> implements com.viaversion.viaversion.api.rewriter.ItemRewriter<T>
{
    protected ItemRewriter(final T protocol) {
        super(protocol);
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        if (this.protocol.getMappingData() != null && this.protocol.getMappingData().getItemMappings() != null) {
            item.setIdentifier(this.protocol.getMappingData().getNewItemId(item.identifier()));
        }
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        if (this.protocol.getMappingData() != null && this.protocol.getMappingData().getItemMappings() != null) {
            item.setIdentifier(this.protocol.getMappingData().getOldItemId(item.identifier()));
        }
        return item;
    }
    
    public void registerWindowItems(final C packetType, final Type<Item[]> type) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)type);
                this.handler(ItemRewriter.this.itemArrayHandler(type));
            }
        });
    }
    
    public void registerWindowItems1_17_1(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final Item[] array;
                    final Item[] items = array = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final Item item = array[i];
                        ItemRewriter.this.handleItemToClient(item);
                    }
                    ItemRewriter.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                });
            }
        });
    }
    
    public void registerOpenWindow(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int windowType = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final int mappedId = ItemRewriter.this.protocol.getMappingData().getMenuMappings().getNewId(windowType);
                    if (mappedId == -1) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.write(Type.VAR_INT, mappedId);
                    }
                });
            }
        });
    }
    
    public void registerSetSlot(final C packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)type);
                this.handler(ItemRewriter.this.itemToClientHandler(type));
            }
        });
    }
    
    public void registerSetSlot1_17_1(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.SHORT);
                this.map(Type.FLAT_VAR_INT_ITEM);
                this.handler(ItemRewriter.this.itemToClientHandler(Type.FLAT_VAR_INT_ITEM));
            }
        });
    }
    
    public void registerEntityEquipment(final C packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)type);
                this.handler(ItemRewriter.this.itemToClientHandler(type));
            }
        });
    }
    
    public void registerEntityEquipmentArray(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    byte slot;
                    do {
                        slot = wrapper.passthrough((Type<Byte>)Type.BYTE);
                        ItemRewriter.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                    } while ((slot & 0xFFFFFF80) != 0x0);
                });
            }
        });
    }
    
    public void registerCreativeInvAction(final S packetType, final Type<Item> type) {
        this.protocol.registerServerbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)type);
                this.handler(ItemRewriter.this.itemToServerHandler(type));
            }
        });
    }
    
    public void registerClickWindow(final S packetType, final Type<Item> type) {
        this.protocol.registerServerbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)type);
                this.handler(ItemRewriter.this.itemToServerHandler(type));
            }
        });
    }
    
    public void registerClickWindow1_17_1(final S packetType) {
        this.protocol.registerServerbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    for (int length = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < length; ++i) {
                        wrapper.passthrough((Type<Object>)Type.SHORT);
                        ItemRewriter.this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                    }
                    ItemRewriter.this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                });
            }
        });
    }
    
    public void registerSetCooldown(final C packetType) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            final int itemId = wrapper.read((Type<Integer>)Type.VAR_INT);
            wrapper.write(Type.VAR_INT, this.protocol.getMappingData().getNewItemId(itemId));
        });
    }
    
    public void registerTradeList(final C packetType) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                    this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                }
                wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.FLOAT);
                wrapper.passthrough((Type<Object>)Type.INT);
            }
        });
    }
    
    public void registerTradeList1_19(final C packetType) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.FLOAT);
                wrapper.passthrough((Type<Object>)Type.INT);
            }
        });
    }
    
    public void registerAdvancements(final C packetType, final Type<Item> type) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                wrapper.passthrough(Type.STRING);
                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                    wrapper.passthrough(Type.STRING);
                }
                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                    wrapper.passthrough(Type.COMPONENT);
                    wrapper.passthrough(Type.COMPONENT);
                    this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
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
        });
    }
    
    public void registerWindowPropertyEnchantmentHandler(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final Mappings mappings = ItemRewriter.this.protocol.getMappingData().getEnchantmentMappings();
                    if (mappings != null) {
                        final short property = wrapper.passthrough((Type<Short>)Type.SHORT);
                        if (property >= 4 && property <= 6) {
                            final short enchantmentId = (short)mappings.getNewId(wrapper.read((Type<Short>)Type.SHORT));
                            wrapper.write(Type.SHORT, enchantmentId);
                        }
                    }
                });
            }
        });
    }
    
    public void registerSpawnParticle(final C packetType, final Type<Item> itemType, final Type<?> coordType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map(coordType);
                this.map(coordType);
                this.map(coordType);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.INT);
                this.handler(ItemRewriter.this.getSpawnParticleHandler(itemType));
            }
        });
    }
    
    public void registerSpawnParticle1_19(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.INT);
                this.handler(ItemRewriter.this.getSpawnParticleHandler(Type.VAR_INT, Type.FLAT_VAR_INT_ITEM));
            }
        });
    }
    
    public PacketHandler getSpawnParticleHandler(final Type<Item> itemType) {
        return this.getSpawnParticleHandler(Type.INT, itemType);
    }
    
    public PacketHandler getSpawnParticleHandler(final Type<Integer> idType, final Type<Item> itemType) {
        return wrapper -> {
            final int id = wrapper.get((Type<Integer>)idType, 0);
            if (id != -1) {
                final ParticleMappings mappings = this.protocol.getMappingData().getParticleMappings();
                if (mappings.isBlockParticle(id)) {
                    final int data = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.VAR_INT, this.protocol.getMappingData().getNewBlockStateId(data));
                }
                else if (mappings.isItemParticle(id)) {
                    this.handleItemToClient(wrapper.passthrough((Type<Item>)itemType));
                }
                final int newId = this.protocol.getMappingData().getNewParticleId(id);
                if (newId != id) {
                    wrapper.set(idType, 0, newId);
                }
            }
        };
    }
    
    public PacketHandler itemArrayHandler(final Type<Item[]> type) {
        return wrapper -> {
            final Item[] array;
            final Item[] items = array = wrapper.get((Type<Item[]>)type, 0);
            int i = 0;
            for (int length = array.length; i < length; ++i) {
                final Item item = array[i];
                this.handleItemToClient(item);
            }
        };
    }
    
    public PacketHandler itemToClientHandler(final Type<Item> type) {
        return wrapper -> this.handleItemToClient(wrapper.get((Type<Item>)type, 0));
    }
    
    public PacketHandler itemToServerHandler(final Type<Item> type) {
        return wrapper -> this.handleItemToServer(wrapper.get((Type<Item>)type, 0));
    }
}
