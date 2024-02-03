// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.api.data.MappedItem;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class ItemRewriter<C extends ClientboundPacketType, S extends ServerboundPacketType, T extends BackwardsProtocol<C, ?, ?, S>> extends ItemRewriterBase<C, S, T>
{
    public ItemRewriter(final T protocol) {
        super(protocol, true);
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag display = (item.tag() != null) ? item.tag().get("display") : null;
        if (((BackwardsProtocol)this.protocol).getTranslatableRewriter() != null && display != null) {
            final Tag name = display.get("Name");
            if (name instanceof StringTag) {
                final StringTag nameStringTag = (StringTag)name;
                final String newValue = ((BackwardsProtocol)this.protocol).getTranslatableRewriter().processText(nameStringTag.getValue()).toString();
                if (!newValue.equals(name.getValue())) {
                    this.saveStringTag(display, nameStringTag, "Name");
                }
                nameStringTag.setValue(newValue);
            }
            final Tag lore = display.get("Lore");
            if (lore instanceof ListTag) {
                final ListTag loreListTag = (ListTag)lore;
                boolean changed = false;
                for (final Tag loreEntryTag : loreListTag) {
                    if (!(loreEntryTag instanceof StringTag)) {
                        continue;
                    }
                    final StringTag loreEntry = (StringTag)loreEntryTag;
                    final String newValue2 = ((BackwardsProtocol)this.protocol).getTranslatableRewriter().processText(loreEntry.getValue()).toString();
                    if (!changed && !newValue2.equals(loreEntry.getValue())) {
                        changed = true;
                        this.saveListTag(display, loreListTag, "Lore");
                    }
                    loreEntry.setValue(newValue2);
                }
            }
        }
        final MappedItem data = ((BackwardsProtocol)this.protocol).getMappingData().getMappedItem(item.identifier());
        if (data == null) {
            return super.handleItemToClient(item);
        }
        if (item.tag() == null) {
            item.setTag(new CompoundTag());
        }
        item.tag().put(this.nbtTagName + "|id", new IntTag(item.identifier()));
        item.setIdentifier(data.getId());
        if (data.customModelData() != null && !item.tag().contains("CustomModelData")) {
            item.tag().put("CustomModelData", new IntTag(data.customModelData()));
        }
        if (display == null) {
            item.tag().put("display", display = new CompoundTag());
        }
        if (!display.contains("Name")) {
            display.put("Name", new StringTag(data.getJsonName()));
            display.put(this.nbtTagName + "|customName", new ByteTag());
        }
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToServer(item);
        if (item.tag() != null) {
            final IntTag originalId = item.tag().remove(this.nbtTagName + "|id");
            if (originalId != null) {
                item.setIdentifier(originalId.asInt());
            }
        }
        return item;
    }
    
    @Override
    public void registerAdvancements(final C packetType, final Type<Item> type) {
        ((BackwardsProtocol)this.protocol).registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.handler(wrapper -> {
                    final Object val$type = type;
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        wrapper.passthrough(Type.STRING);
                        if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                            final JsonElement title = wrapper.passthrough(Type.COMPONENT);
                            final JsonElement description = wrapper.passthrough(Type.COMPONENT);
                            final TranslatableRewriter<C> translatableRewriter = ((BackwardsProtocol)ItemRewriter.this.protocol).getTranslatableRewriter();
                            if (translatableRewriter != null) {
                                translatableRewriter.processText(title);
                                translatableRewriter.processText(description);
                            }
                            ItemRewriter.this.handleItemToClient(wrapper.passthrough((Type<Item>)type));
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
        });
    }
}
