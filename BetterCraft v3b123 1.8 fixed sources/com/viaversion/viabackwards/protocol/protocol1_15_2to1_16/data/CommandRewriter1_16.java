// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.rewriter.CommandRewriter;

public class CommandRewriter1_16 extends CommandRewriter<ClientboundPackets1_16>
{
    public CommandRewriter1_16(final Protocol1_15_2To1_16 protocol) {
        super(protocol);
    }
    
    @Override
    public String handleArgumentType(final String argumentType) {
        if (argumentType.equals("minecraft:uuid")) {
            return "minecraft:game_profile";
        }
        return super.handleArgumentType(argumentType);
    }
}
