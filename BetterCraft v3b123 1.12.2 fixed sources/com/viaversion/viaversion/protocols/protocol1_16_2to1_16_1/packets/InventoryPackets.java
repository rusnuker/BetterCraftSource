// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<ClientboundPackets1_16, ServerboundPackets1_16_2, Protocol1_16_2To1_16_1>
{
    public InventoryPackets(final Protocol1_16_2To1_16_1 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((ItemRewriter<ClientboundPackets1_16, S, T>)this).registerSetCooldown(ClientboundPackets1_16.COOLDOWN);
        ((ItemRewriter<ClientboundPackets1_16, S, T>)this).registerWindowItems(ClientboundPackets1_16.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        ((ItemRewriter<ClientboundPackets1_16, S, T>)this).registerTradeList(ClientboundPackets1_16.TRADE_LIST);
        ((ItemRewriter<ClientboundPackets1_16, S, T>)this).registerSetSlot(ClientboundPackets1_16.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_16, S, T>)this).registerEntityEquipmentArray(ClientboundPackets1_16.ENTITY_EQUIPMENT);
        ((ItemRewriter<ClientboundPackets1_16, S, T>)this).registerAdvancements(ClientboundPackets1_16.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_16, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_16.UNLOCK_RECIPES, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.write(Type.BOOLEAN, false);
            wrapper.write(Type.BOOLEAN, false);
            wrapper.write(Type.BOOLEAN, false);
            wrapper.write(Type.BOOLEAN, false);
            return;
        });
        new RecipeRewriter<ClientboundPackets1_16>(this.protocol).register(ClientboundPackets1_16.DECLARE_RECIPES);
        ((ItemRewriter<C, ServerboundPackets1_16_2, T>)this).registerClickWindow(ServerboundPackets1_16_2.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<C, ServerboundPackets1_16_2, T>)this).registerCreativeInvAction(ServerboundPackets1_16_2.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_16_2>)this.protocol).registerServerbound(ServerboundPackets1_16_2.EDIT_BOOK, wrapper -> this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
        ((ItemRewriter<ClientboundPackets1_16, S, T>)this).registerSpawnParticle(ClientboundPackets1_16.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
    }
}
