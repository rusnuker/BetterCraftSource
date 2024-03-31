// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.rewriter.RecipeRewriter1_19_4;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.storage.BackSignEditStorage;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.Protocol1_19_4To1_20;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ServerboundPackets1_19_4;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public final class BlockItemPackets1_20 extends ItemRewriter<ClientboundPackets1_19_4, ServerboundPackets1_19_4, Protocol1_19_4To1_20>
{
    public BlockItemPackets1_20(final Protocol1_19_4To1_20 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        final BlockRewriter<ClientboundPackets1_19_4> blockRewriter = new BlockRewriter<ClientboundPackets1_19_4>(this.protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_19_4.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_19_4.BLOCK_CHANGE);
        blockRewriter.registerEffect(ClientboundPackets1_19_4.EFFECT, 1010, 2001);
        blockRewriter.registerBlockEntityData(ClientboundPackets1_19_4.BLOCK_ENTITY_DATA, this::handleBlockEntity);
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.CHUNK_DATA, new PacketHandlers() {
            @Override
            protected void register() {
                this.handler(blockRewriter.chunkDataHandler1_19(Chunk1_18Type::new, x$0 -> {
                    final Object this$0 = BlockItemPackets1_20.this;
                    rec$.handleBlockEntity(x$0);
                    return;
                }));
                this.create(Type.BOOLEAN, true);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.UPDATE_LIGHT, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.write(Type.BOOLEAN, true);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.MULTI_BLOCK_CHANGE, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.LONG);
                this.create(Type.BOOLEAN, false);
                this.handler(wrapper -> {
                    final BlockChangeRecord[] array = wrapper.passthrough(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY);
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final BlockChangeRecord record = array[i];
                        record.setBlockId(((Protocol1_19_4To1_20)BlockItemPackets1_20.this.protocol).getMappingData().getNewBlockStateId(record.getBlockId()));
                    }
                });
            }
        });
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerOpenWindow(ClientboundPackets1_19_4.OPEN_WINDOW);
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerSetCooldown(ClientboundPackets1_19_4.COOLDOWN);
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerWindowItems1_17_1(ClientboundPackets1_19_4.WINDOW_ITEMS);
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerSetSlot1_17_1(ClientboundPackets1_19_4.SET_SLOT);
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_19_4.ENTITY_EQUIPMENT);
        ((ItemRewriter<C, ServerboundPackets1_19_4, T>)this).registerClickWindow1_17_1(ServerboundPackets1_19_4.CLICK_WINDOW);
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerTradeList1_19(ClientboundPackets1_19_4.TRADE_LIST);
        ((ItemRewriter<C, ServerboundPackets1_19_4, T>)this).registerCreativeInvAction(ServerboundPackets1_19_4.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerWindowPropertyEnchantmentHandler(ClientboundPackets1_19_4.WINDOW_PROPERTY);
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerSpawnParticle1_19(ClientboundPackets1_19_4.SPAWN_PARTICLE);
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.ADVANCEMENTS, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                wrapper.passthrough(Type.STRING);
                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                    wrapper.passthrough(Type.STRING);
                }
                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                    wrapper.passthrough(Type.COMPONENT);
                    wrapper.passthrough(Type.COMPONENT);
                    this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
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
                wrapper.read((Type<Object>)Type.BOOLEAN);
            }
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.OPEN_SIGN_EDITOR, wrapper -> {
            final Position position = wrapper.passthrough(Type.POSITION1_14);
            final boolean frontSide = wrapper.read((Type<Boolean>)Type.BOOLEAN);
            if (frontSide) {
                wrapper.user().remove(BackSignEditStorage.class);
            }
            else {
                wrapper.user().put(new BackSignEditStorage(position));
            }
            return;
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_19_4>)this.protocol).registerServerbound(ServerboundPackets1_19_4.UPDATE_SIGN, wrapper -> {
            final Position position2 = wrapper.passthrough(Type.POSITION1_14);
            final BackSignEditStorage backSignEditStorage = wrapper.user().remove(BackSignEditStorage.class);
            final boolean frontSide2 = backSignEditStorage == null || !backSignEditStorage.position().equals(position2);
            wrapper.write(Type.BOOLEAN, frontSide2);
            return;
        });
        new RecipeRewriter1_19_4<ClientboundPackets1_19_4>(this.protocol).register(ClientboundPackets1_19_4.DECLARE_RECIPES);
    }
    
    private void handleBlockEntity(final BlockEntity blockEntity) {
        if (blockEntity.typeId() != 7 && blockEntity.typeId() != 8) {
            return;
        }
        final CompoundTag tag = blockEntity.tag();
        final CompoundTag frontText = tag.remove("front_text");
        tag.remove("back_text");
        if (frontText != null) {
            this.writeMessages(frontText, tag, false);
            this.writeMessages(frontText, tag, true);
            final Tag color = frontText.remove("color");
            if (color != null) {
                tag.put("Color", color);
            }
            final Tag glowing = frontText.remove("has_glowing_text");
            if (glowing != null) {
                tag.put("GlowingText", glowing);
            }
        }
    }
    
    private void writeMessages(final CompoundTag frontText, final CompoundTag tag, final boolean filtered) {
        final ListTag messages = frontText.get(filtered ? "filtered_messages" : "messages");
        if (messages == null) {
            return;
        }
        int i = 0;
        for (final Tag message : messages) {
            tag.put((filtered ? "FilteredText" : "Text") + ++i, message);
        }
    }
}
