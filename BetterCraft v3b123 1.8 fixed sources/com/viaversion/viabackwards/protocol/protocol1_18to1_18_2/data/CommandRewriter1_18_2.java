// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18to1_18_2.data;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.protocol.protocol1_18to1_18_2.Protocol1_18To1_18_2;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.rewriter.CommandRewriter;

public final class CommandRewriter1_18_2 extends CommandRewriter<ClientboundPackets1_18>
{
    public CommandRewriter1_18_2(final Protocol1_18To1_18_2 protocol) {
        super(protocol);
        this.parserHandlers.put("minecraft:resource", wrapper -> {
            wrapper.read(Type.STRING);
            wrapper.write(Type.VAR_INT, 1);
            return;
        });
        this.parserHandlers.put("minecraft:resource_or_tag", wrapper -> {
            wrapper.read(Type.STRING);
            wrapper.write(Type.VAR_INT, 1);
        });
    }
    
    @Override
    public String handleArgumentType(final String argumentType) {
        if (argumentType.equals("minecraft:resource") || argumentType.equals("minecraft:resource_or_tag")) {
            return "brigadier:string";
        }
        return super.handleArgumentType(argumentType);
    }
}
