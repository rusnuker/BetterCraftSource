// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.rewriter.RecipeRewriter1_19_3;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.Protocol1_19_3To1_19_4;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ServerboundPackets1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public final class BlockItemPackets1_19_4 extends ItemRewriter<ClientboundPackets1_19_4, ServerboundPackets1_19_3, Protocol1_19_3To1_19_4>
{
    public BlockItemPackets1_19_4(final Protocol1_19_3To1_19_4 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        final BlockRewriter<ClientboundPackets1_19_4> blockRewriter = new BlockRewriter<ClientboundPackets1_19_4>(this.protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_19_4.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_19_4.BLOCK_CHANGE);
        blockRewriter.registerVarLongMultiBlockChange(ClientboundPackets1_19_4.MULTI_BLOCK_CHANGE);
        blockRewriter.registerEffect(ClientboundPackets1_19_4.EFFECT, 1010, 2001);
        blockRewriter.registerChunkData1_19(ClientboundPackets1_19_4.CHUNK_DATA, Chunk1_18Type::new);
        blockRewriter.registerBlockEntityData(ClientboundPackets1_19_4.BLOCK_ENTITY_DATA);
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.OPEN_WINDOW, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.COMPONENT);
                this.handler(wrapper -> {
                    final int windowType = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (windowType == 21) {
                        wrapper.cancel();
                    }
                    else if (windowType > 21) {
                        wrapper.set(Type.VAR_INT, 1, windowType - 1);
                    }
                    ((Protocol1_19_3To1_19_4)BlockItemPackets1_19_4.this.protocol).getTranslatableRewriter().processText(wrapper.get(Type.COMPONENT, 0));
                });
            }
        });
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerSetCooldown(ClientboundPackets1_19_4.COOLDOWN);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerWindowItems1_17_1(ClientboundPackets1_19_4.WINDOW_ITEMS);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerSetSlot1_17_1(ClientboundPackets1_19_4.SET_SLOT);
        ((ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerAdvancements(ClientboundPackets1_19_4.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_19_4.ENTITY_EQUIPMENT);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_19_3, T>)this).registerClickWindow1_17_1(ServerboundPackets1_19_3.CLICK_WINDOW);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerTradeList1_19(ClientboundPackets1_19_4.TRADE_LIST);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<C, ServerboundPackets1_19_3, T>)this).registerCreativeInvAction(ServerboundPackets1_19_3.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerWindowPropertyEnchantmentHandler(ClientboundPackets1_19_4.WINDOW_PROPERTY);
        ((com.viaversion.viaversion.rewriter.ItemRewriter<ClientboundPackets1_19_4, S, T>)this).registerSpawnParticle1_19(ClientboundPackets1_19_4.SPAWN_PARTICLE);
        final RecipeRewriter1_19_3<ClientboundPackets1_19_4> recipeRewriter = new RecipeRewriter1_19_3<ClientboundPackets1_19_4>(this.protocol) {
            @Override
            public void handleCraftingShaped(final PacketWrapper wrapper) throws Exception {
                final int ingredients = wrapper.passthrough((Type<Integer>)Type.VAR_INT) * wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                wrapper.passthrough(Type.STRING);
                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                for (int i = 0; i < ingredients; ++i) {
                    this.handleIngredient(wrapper);
                }
                this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                wrapper.read((Type<Object>)Type.BOOLEAN);
            }
        };
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.DECLARE_RECIPES, wrapper -> {
            int newSize;
            for (int size = newSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                final String type = wrapper.read(Type.STRING);
                final String cutType = Key.stripMinecraftNamespace(type);
                if (cutType.equals("smithing_transform") || cutType.equals("smithing_trim")) {
                    --newSize;
                    wrapper.read(Type.STRING);
                    wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                    wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                    wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                    if (cutType.equals("smithing_transform")) {
                        wrapper.read(Type.FLAT_VAR_INT_ITEM);
                    }
                }
                else if (cutType.equals("crafting_decorated_pot")) {
                    --newSize;
                    wrapper.read(Type.STRING);
                    wrapper.read((Type<Object>)Type.VAR_INT);
                }
                else {
                    wrapper.write(Type.STRING, type);
                    wrapper.passthrough(Type.STRING);
                    recipeRewriter.handleRecipeType(wrapper, cutType);
                }
            }
            wrapper.set(Type.VAR_INT, 0, newSize);
        });
    }
}
