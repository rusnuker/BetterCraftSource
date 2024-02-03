// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage.TabCompleteStorage;
import java.util.List;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import com.viaversion.viaversion.api.rewriter.RewriterBase;

public class PlayerPacket1_13 extends RewriterBase<Protocol1_12_2To1_13>
{
    private final CommandRewriter<ClientboundPackets1_13> commandRewriter;
    
    public PlayerPacket1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol);
        this.commandRewriter = new CommandRewriter<ClientboundPackets1_13>(this.protocol);
    }
    
    @Override
    protected void registerPackets() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //     4: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //     7: getstatic       com/viaversion/viaversion/api/protocol/packet/State.LOGIN:Lcom/viaversion/viaversion/api/protocol/packet/State;
        //    10: iconst_4       
        //    11: iconst_m1      
        //    12: new             Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$1;
        //    15: dup            
        //    16: aload_0         /* this */
        //    17: invokespecial   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$1.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)V
        //    20: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/State;IILcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    23: aload_0         /* this */
        //    24: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    27: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //    30: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    33: aload_0         /* this */
        //    34: invokedynamic   BootstrapMethod #0, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //    39: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    42: aload_0         /* this */
        //    43: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    46: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //    49: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.SPAWN_PARTICLE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    52: new             Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$2;
        //    55: dup            
        //    56: aload_0         /* this */
        //    57: invokespecial   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$2.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)V
        //    60: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    63: aload_0         /* this */
        //    64: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    67: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //    70: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.PLAYER_INFO:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    73: new             Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$3;
        //    76: dup            
        //    77: aload_0         /* this */
        //    78: invokespecial   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$3.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)V
        //    81: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    84: aload_0         /* this */
        //    85: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    88: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //    91: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.SCOREBOARD_OBJECTIVE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    94: new             Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$4;
        //    97: dup            
        //    98: aload_0         /* this */
        //    99: invokespecial   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$4.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)V
        //   102: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   105: aload_0         /* this */
        //   106: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   109: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //   112: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.TEAMS:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //   115: new             Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$5;
        //   118: dup            
        //   119: aload_0         /* this */
        //   120: invokespecial   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$5.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)V
        //   123: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   126: aload_0         /* this */
        //   127: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   130: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //   133: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.DECLARE_COMMANDS:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //   136: aconst_null    
        //   137: aload_0         /* this */
        //   138: invokedynamic   BootstrapMethod #1, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   143: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   146: aload_0         /* this */
        //   147: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   150: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //   153: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.TAB_COMPLETE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //   156: invokedynamic   BootstrapMethod #2, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   161: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   164: aload_0         /* this */
        //   165: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   168: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //   171: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.TAB_COMPLETE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   174: invokedynamic   BootstrapMethod #3, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   179: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   182: aload_0         /* this */
        //   183: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   186: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //   189: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   192: aload_0         /* this */
        //   193: invokedynamic   BootstrapMethod #4, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   198: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   201: aload_0         /* this */
        //   202: getfield        com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   205: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13;
        //   208: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.STATISTICS:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //   211: new             Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$6;
        //   214: dup            
        //   215: aload_0         /* this */
        //   216: invokespecial   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$6.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13;)V
        //   219: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/Protocol1_12_2To1_13.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   222: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:252)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:185)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.nameVariables(AstMethodBodyBuilder.java:1482)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.populateVariables(AstMethodBodyBuilder.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static boolean startsWithIgnoreCase(final String string, final String prefix) {
        return string.length() >= prefix.length() && string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
