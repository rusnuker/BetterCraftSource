// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_15to1_14_4.packets;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<ClientboundPackets1_14, ServerboundPackets1_14, Protocol1_15To1_14_4>
{
    public InventoryPackets(final Protocol1_15To1_14_4 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((ItemRewriter<ClientboundPackets1_14, S, T>)this).registerSetCooldown(ClientboundPackets1_14.COOLDOWN);
        ((ItemRewriter<ClientboundPackets1_14, S, T>)this).registerWindowItems(ClientboundPackets1_14.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        ((ItemRewriter<ClientboundPackets1_14, S, T>)this).registerTradeList(ClientboundPackets1_14.TRADE_LIST);
        ((ItemRewriter<ClientboundPackets1_14, S, T>)this).registerSetSlot(ClientboundPackets1_14.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_14, S, T>)this).registerEntityEquipment(ClientboundPackets1_14.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<ClientboundPackets1_14, S, T>)this).registerAdvancements(ClientboundPackets1_14.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        new RecipeRewriter<ClientboundPackets1_14>(this.protocol).register(ClientboundPackets1_14.DECLARE_RECIPES);
        ((ItemRewriter<C, ServerboundPackets1_14, T>)this).registerClickWindow(ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        ((ItemRewriter<C, ServerboundPackets1_14, T>)this).registerCreativeInvAction(ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
    }
}
