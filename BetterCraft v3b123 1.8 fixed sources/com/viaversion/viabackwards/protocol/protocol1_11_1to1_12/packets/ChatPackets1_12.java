// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.data.AdvancementTranslations;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import com.viaversion.viaversion.rewriter.ComponentRewriter;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import com.viaversion.viaversion.api.rewriter.RewriterBase;

public class ChatPackets1_12 extends RewriterBase<Protocol1_11_1To1_12>
{
    public static final ComponentRewriter<ClientboundPackets1_12> COMPONENT_REWRITER;
    
    public ChatPackets1_12(final Protocol1_11_1To1_12 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_12, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_12.CHAT_MESSAGE, wrapper -> {
            final JsonElement element = wrapper.passthrough(Type.COMPONENT);
            ChatPackets1_12.COMPONENT_REWRITER.processText(element);
        });
    }
    
    static {
        COMPONENT_REWRITER = new ComponentRewriter<ClientboundPackets1_12>() {
            @Override
            public void processText(final JsonElement element) {
                super.processText(element);
                if (element == null || !element.isJsonObject()) {
                    return;
                }
                final JsonObject object = element.getAsJsonObject();
                final JsonElement keybind = object.remove("keybind");
                if (keybind == null) {
                    return;
                }
                object.addProperty("text", keybind.getAsString());
            }
            
            @Override
            protected void handleTranslate(final JsonObject object, final String translate) {
                final String text = AdvancementTranslations.get(translate);
                if (text != null) {
                    object.addProperty("translate", text);
                }
            }
        };
    }
}
