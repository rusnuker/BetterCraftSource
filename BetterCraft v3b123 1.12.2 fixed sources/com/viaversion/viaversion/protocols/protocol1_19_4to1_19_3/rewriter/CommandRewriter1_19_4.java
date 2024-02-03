// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.rewriter;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class CommandRewriter1_19_4<C extends ClientboundPacketType> extends CommandRewriter<C>
{
    public CommandRewriter1_19_4(final Protocol<C, ?, ?, ?> protocol) {
        super(protocol);
        this.parserHandlers.put("minecraft:time", wrapper -> {
            final Integer n = wrapper.passthrough((Type<Integer>)Type.INT);
        });
    }
}
