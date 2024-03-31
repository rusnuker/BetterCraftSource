// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.data;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.rewriter.CommandRewriter;

public class CommandRewriter1_14 extends CommandRewriter<ClientboundPackets1_14>
{
    public CommandRewriter1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol);
        this.parserHandlers.put("minecraft:nbt_tag", wrapper -> wrapper.write(Type.VAR_INT, 2));
        this.parserHandlers.put("minecraft:time", wrapper -> {
            wrapper.write(Type.BYTE, (Byte)1);
            wrapper.write(Type.INT, 0);
        });
    }
    
    @Override
    public String handleArgumentType(final String argumentType) {
        switch (argumentType) {
            case "minecraft:nbt_compound_tag": {
                return "minecraft:nbt";
            }
            case "minecraft:nbt_tag": {
                return "brigadier:string";
            }
            case "minecraft:time": {
                return "brigadier:integer";
            }
            default: {
                return super.handleArgumentType(argumentType);
            }
        }
    }
}
