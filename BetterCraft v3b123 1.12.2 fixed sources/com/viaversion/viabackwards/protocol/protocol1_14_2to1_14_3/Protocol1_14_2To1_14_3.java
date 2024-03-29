// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_14_2to1_14_3;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_14_2To1_14_3 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14>
{
    public Protocol1_14_2To1_14_3() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_14.TRADE_LIST, wrapper -> {
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                    wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                }
                wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.INT);
                wrapper.passthrough((Type<Object>)Type.FLOAT);
            }
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.passthrough((Type<Object>)Type.VAR_INT);
            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
            wrapper.read((Type<Object>)Type.BOOLEAN);
            return;
        });
        final RecipeRewriter<ClientboundPackets1_14> recipeHandler = new RecipeRewriter<ClientboundPackets1_14>(this);
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)this).registerClientbound(ClientboundPackets1_14.DECLARE_RECIPES, wrapper -> {
            final int size2 = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
            int deleted = 0;
            for (int j = 0; j < size2; ++j) {
                final String fullType = wrapper.read(Type.STRING);
                final String type = fullType.replace("minecraft:", "");
                final String id = wrapper.read(Type.STRING);
                if (type.equals("crafting_special_repairitem")) {
                    ++deleted;
                }
                else {
                    wrapper.write(Type.STRING, fullType);
                    wrapper.write(Type.STRING, id);
                    recipeHandler.handleRecipeType(wrapper, type);
                }
            }
            wrapper.set(Type.VAR_INT, 0, size2 - deleted);
        });
    }
}
