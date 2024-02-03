// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2;

import com.google.common.collect.Sets;
import java.util.HashMap;
import com.viaversion.viaversion.api.minecraft.Position;
import java.util.Iterator;
import java.util.List;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.StatisticMappings;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.StatisticData;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.util.ChatColorUtil;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.PlayerLookTargetProvider;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.PaintingProvider;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockStorage;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.TabCompleteTracker;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockConnectionStorage;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.PacketBlockConnectionProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.BlockIdData;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.RecipeData;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.ComponentRewriter1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.metadata.MetadataRewriter1_13To1_12_2;
import java.util.Set;
import java.util.Map;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.MappingData;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_13To1_12_2 extends AbstractProtocol<ClientboundPackets1_12_1, ClientboundPackets1_13, ServerboundPackets1_12_1, ServerboundPackets1_13>
{
    public static final MappingData MAPPINGS;
    private static final Map<Character, Character> SCOREBOARD_TEAM_NAME_REWRITE;
    private static final Set<Character> FORMATTING_CODES;
    private final MetadataRewriter1_13To1_12_2 entityRewriter;
    private final InventoryPackets itemRewriter;
    private final ComponentRewriter1_13<ClientboundPackets1_12_1> componentRewriter;
    public static final PacketHandler POS_TO_3_INT;
    public static final PacketHandler SEND_DECLARE_COMMANDS_AND_TAGS;
    
    public Protocol1_13To1_12_2() {
        super(ClientboundPackets1_12_1.class, ClientboundPackets1_13.class, ServerboundPackets1_12_1.class, ServerboundPackets1_13.class);
        this.entityRewriter = new MetadataRewriter1_13To1_12_2(this);
        this.itemRewriter = new InventoryPackets(this);
        this.componentRewriter = new ComponentRewriter1_13<ClientboundPackets1_12_1>(this);
    }
    
    @Override
    protected void registerPackets() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.entityRewriter:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/metadata/MetadataRewriter1_13To1_12_2;
        //     4: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/metadata/MetadataRewriter1_13To1_12_2.register:()V
        //     7: aload_0         /* this */
        //     8: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.itemRewriter:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/packets/InventoryPackets;
        //    11: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/packets/InventoryPackets.register:()V
        //    14: aload_0         /* this */
        //    15: invokestatic    com/viaversion/viaversion/protocols/protocol1_13to1_12_2/packets/EntityPackets.register:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //    18: aload_0         /* this */
        //    19: invokestatic    com/viaversion/viaversion/protocols/protocol1_13to1_12_2/packets/WorldPackets.register:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //    22: aload_0         /* this */
        //    23: getstatic       com/viaversion/viaversion/api/protocol/packet/State.LOGIN:Lcom/viaversion/viaversion/api/protocol/packet/State;
        //    26: iconst_0       
        //    27: iconst_0       
        //    28: aload_0         /* this */
        //    29: invokedynamic   BootstrapMethod #0, handle:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //    34: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/State;IILcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    37: aload_0         /* this */
        //    38: getstatic       com/viaversion/viaversion/api/protocol/packet/State.STATUS:Lcom/viaversion/viaversion/api/protocol/packet/State;
        //    41: iconst_0       
        //    42: iconst_0       
        //    43: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$1;
        //    46: dup            
        //    47: aload_0         /* this */
        //    48: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$1.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //    51: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/State;IILcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    54: aload_0         /* this */
        //    55: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.STATISTICS:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //    58: invokedynamic   BootstrapMethod #1, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //    63: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    66: aload_0         /* this */
        //    67: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.componentRewriter:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13;
        //    70: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.BOSSBAR:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //    73: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13.registerBossBar:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    76: aload_0         /* this */
        //    77: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.componentRewriter:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13;
        //    80: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.CHAT_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //    83: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13.registerComponentPacket:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    86: aload_0         /* this */
        //    87: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.TAB_COMPLETE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //    90: invokedynamic   BootstrapMethod #2, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //    95: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    98: aload_0         /* this */
        //    99: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.OPEN_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   102: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$2;
        //   105: dup            
        //   106: aload_0         /* this */
        //   107: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$2.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   110: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   113: aload_0         /* this */
        //   114: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.COOLDOWN:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   117: invokedynamic   BootstrapMethod #3, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   122: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   125: aload_0         /* this */
        //   126: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.componentRewriter:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13;
        //   129: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.DISCONNECT:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   132: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13.registerComponentPacket:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   135: aload_0         /* this */
        //   136: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.EFFECT:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   139: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$3;
        //   142: dup            
        //   143: aload_0         /* this */
        //   144: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$3.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   147: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   150: aload_0         /* this */
        //   151: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.CRAFT_RECIPE_RESPONSE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   154: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$4;
        //   157: dup            
        //   158: aload_0         /* this */
        //   159: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$4.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   162: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   165: aload_0         /* this */
        //   166: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.componentRewriter:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13;
        //   169: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.COMBAT_EVENT:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   172: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13.registerCombatEvent:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   175: aload_0         /* this */
        //   176: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.MAP_DATA:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   179: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$5;
        //   182: dup            
        //   183: aload_0         /* this */
        //   184: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$5.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   187: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   190: aload_0         /* this */
        //   191: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.UNLOCK_RECIPES:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   194: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$6;
        //   197: dup            
        //   198: aload_0         /* this */
        //   199: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$6.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   202: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   205: aload_0         /* this */
        //   206: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.RESPAWN:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   209: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$7;
        //   212: dup            
        //   213: aload_0         /* this */
        //   214: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$7.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   217: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   220: aload_0         /* this */
        //   221: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.SCOREBOARD_OBJECTIVE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   224: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$8;
        //   227: dup            
        //   228: aload_0         /* this */
        //   229: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$8.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   232: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   235: aload_0         /* this */
        //   236: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.TEAMS:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   239: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$9;
        //   242: dup            
        //   243: aload_0         /* this */
        //   244: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$9.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   247: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   250: aload_0         /* this */
        //   251: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.UPDATE_SCORE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   254: aload_0         /* this */
        //   255: invokedynamic   BootstrapMethod #4, handle:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   260: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   263: aload_0         /* this */
        //   264: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.componentRewriter:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13;
        //   267: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.TITLE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   270: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/ComponentRewriter1_13.registerTitle:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   273: new             Lcom/viaversion/viaversion/rewriter/SoundRewriter;
        //   276: dup            
        //   277: aload_0         /* this */
        //   278: invokespecial   com/viaversion/viaversion/rewriter/SoundRewriter.<init>:(Lcom/viaversion/viaversion/api/protocol/Protocol;)V
        //   281: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.SOUND:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   284: invokevirtual   com/viaversion/viaversion/rewriter/SoundRewriter.registerSound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   287: aload_0         /* this */
        //   288: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.TAB_LIST:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   291: aload_0         /* this */
        //   292: invokedynamic   BootstrapMethod #5, handle:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   297: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   300: aload_0         /* this */
        //   301: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1.ADVANCEMENTS:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ClientboundPackets1_12_1;
        //   304: aload_0         /* this */
        //   305: invokedynamic   BootstrapMethod #6, handle:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   310: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   313: aload_0         /* this */
        //   314: getstatic       com/viaversion/viaversion/api/protocol/packet/State.LOGIN:Lcom/viaversion/viaversion/api/protocol/packet/State;
        //   317: iconst_2       
        //   318: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.cancelServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/State;I)V
        //   321: aload_0         /* this */
        //   322: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.QUERY_BLOCK_NBT:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   325: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.cancelServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;)V
        //   328: aload_0         /* this */
        //   329: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.TAB_COMPLETE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   332: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$10;
        //   335: dup            
        //   336: aload_0         /* this */
        //   337: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$10.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   340: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   343: aload_0         /* this */
        //   344: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.EDIT_BOOK:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   347: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   350: aload_0         /* this */
        //   351: invokedynamic   BootstrapMethod #7, handle:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   356: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   359: aload_0         /* this */
        //   360: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.ENTITY_NBT_REQUEST:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   363: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.cancelServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;)V
        //   366: aload_0         /* this */
        //   367: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.PICK_ITEM:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   370: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   373: invokedynamic   BootstrapMethod #8, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   378: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   381: aload_0         /* this */
        //   382: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.CRAFT_RECIPE_REQUEST:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   385: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$11;
        //   388: dup            
        //   389: aload_0         /* this */
        //   390: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$11.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   393: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   396: aload_0         /* this */
        //   397: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.RECIPE_BOOK_DATA:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   400: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$12;
        //   403: dup            
        //   404: aload_0         /* this */
        //   405: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$12.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   408: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   411: aload_0         /* this */
        //   412: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.RENAME_ITEM:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   415: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   418: invokedynamic   BootstrapMethod #9, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   423: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   426: aload_0         /* this */
        //   427: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.SELECT_TRADE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   430: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   433: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$13;
        //   436: dup            
        //   437: aload_0         /* this */
        //   438: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$13.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   441: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   444: aload_0         /* this */
        //   445: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.SET_BEACON_EFFECT:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   448: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   451: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$14;
        //   454: dup            
        //   455: aload_0         /* this */
        //   456: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$14.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   459: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   462: aload_0         /* this */
        //   463: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.UPDATE_COMMAND_BLOCK:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   466: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   469: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$15;
        //   472: dup            
        //   473: aload_0         /* this */
        //   474: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$15.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   477: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   480: aload_0         /* this */
        //   481: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.UPDATE_COMMAND_BLOCK_MINECART:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   484: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   487: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$16;
        //   490: dup            
        //   491: aload_0         /* this */
        //   492: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$16.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   495: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   498: aload_0         /* this */
        //   499: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.UPDATE_STRUCTURE_BLOCK:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   502: getstatic       com/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_12_1to1_12/ServerboundPackets1_12_1;
        //   505: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$17;
        //   508: dup            
        //   509: aload_0         /* this */
        //   510: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2$17.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2;)V
        //   513: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/Protocol1_13To1_12_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   516: return         
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
    
    @Override
    protected void onMappingDataLoaded() {
        ConnectionData.init();
        RecipeData.init();
        BlockIdData.init();
        Types1_13.PARTICLE.filler(this).reader(3, ParticleType.Readers.BLOCK).reader(20, ParticleType.Readers.DUST).reader(11, ParticleType.Readers.DUST).reader(27, ParticleType.Readers.ITEM);
        if (Via.getConfig().isServersideBlockConnections() && Via.getManager().getProviders().get(BlockConnectionProvider.class) instanceof PacketBlockConnectionProvider) {
            BlockConnectionStorage.init();
        }
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.addEntityTracker(this.getClass(), new EntityTrackerBase(userConnection, Entity1_13Types.EntityType.PLAYER));
        userConnection.put(new TabCompleteTracker());
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
        userConnection.put(new BlockStorage());
        if (Via.getConfig().isServersideBlockConnections() && Via.getManager().getProviders().get(BlockConnectionProvider.class) instanceof PacketBlockConnectionProvider) {
            userConnection.put(new BlockConnectionStorage());
        }
    }
    
    @Override
    public void register(final ViaProviders providers) {
        providers.register(BlockEntityProvider.class, new BlockEntityProvider());
        providers.register(PaintingProvider.class, new PaintingProvider());
        providers.register(PlayerLookTargetProvider.class, new PlayerLookTargetProvider());
    }
    
    public char getLastColorChar(final String input) {
        final int length = input.length();
        for (int index = length - 1; index > -1; --index) {
            final char section = input.charAt(index);
            if (section == '§' && index < length - 1) {
                final char c = input.charAt(index + 1);
                if (ChatColorUtil.isColorCode(c) && !Protocol1_13To1_12_2.FORMATTING_CODES.contains(c)) {
                    return c;
                }
            }
        }
        return 'r';
    }
    
    protected String rewriteTeamMemberName(String name) {
        if (ChatColorUtil.stripColor(name).isEmpty()) {
            final StringBuilder newName = new StringBuilder();
            for (int i = 1; i < name.length(); i += 2) {
                final char colorChar = name.charAt(i);
                Character rewrite = Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.get(colorChar);
                if (rewrite == null) {
                    rewrite = colorChar;
                }
                newName.append('§').append(rewrite);
            }
            name = newName.toString();
        }
        return name;
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_13To1_12_2.MAPPINGS;
    }
    
    @Override
    public MetadataRewriter1_13To1_12_2 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public InventoryPackets getItemRewriter() {
        return this.itemRewriter;
    }
    
    public ComponentRewriter1_13 getComponentRewriter() {
        return this.componentRewriter;
    }
    
    static {
        MAPPINGS = new MappingData();
        SCOREBOARD_TEAM_NAME_REWRITE = new HashMap<Character, Character>();
        FORMATTING_CODES = Sets.newHashSet('k', 'l', 'm', 'n', 'o', 'r');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('0', 'g');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('1', 'h');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('2', 'i');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('3', 'j');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('4', 'p');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('5', 'q');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('6', 's');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('7', 't');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('8', 'u');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('9', 'v');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('a', 'w');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('b', 'x');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('c', 'y');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('d', 'z');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('e', '!');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('f', '?');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('k', '#');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('l', '(');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('m', ')');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('n', ':');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('o', ';');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put('r', '/');
        POS_TO_3_INT = (wrapper -> {
            final Position position = wrapper.read(Type.POSITION);
            wrapper.write(Type.INT, position.x());
            wrapper.write(Type.INT, position.y());
            wrapper.write(Type.INT, position.z());
            return;
        });
        SEND_DECLARE_COMMANDS_AND_TAGS = (w -> {
            w.create(ClientboundPackets1_13.DECLARE_COMMANDS, wrapper -> {
                wrapper.write(Type.VAR_INT, 2);
                wrapper.write(Type.BYTE, (Byte)0);
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { 1 });
                wrapper.write(Type.BYTE, (Byte)22);
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[0]);
                wrapper.write(Type.STRING, "args");
                wrapper.write(Type.STRING, "brigadier:string");
                wrapper.write(Type.VAR_INT, 2);
                wrapper.write(Type.STRING, "minecraft:ask_server");
                wrapper.write(Type.VAR_INT, 0);
                return;
            }).scheduleSend(Protocol1_13To1_12_2.class);
            w.create(ClientboundPackets1_13.TAGS, wrapper -> {
                wrapper.write(Type.VAR_INT, Protocol1_13To1_12_2.MAPPINGS.getBlockTags().size());
                Protocol1_13To1_12_2.MAPPINGS.getBlockTags().entrySet().iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final Map.Entry<String, int[]> tag = iterator.next();
                    wrapper.write(Type.STRING, tag.getKey());
                    wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, tag.getValue());
                }
                wrapper.write(Type.VAR_INT, Protocol1_13To1_12_2.MAPPINGS.getItemTags().size());
                Protocol1_13To1_12_2.MAPPINGS.getItemTags().entrySet().iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final Map.Entry<String, int[]> tag2 = iterator2.next();
                    wrapper.write(Type.STRING, tag2.getKey());
                    wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, tag2.getValue());
                }
                wrapper.write(Type.VAR_INT, Protocol1_13To1_12_2.MAPPINGS.getFluidTags().size());
                Protocol1_13To1_12_2.MAPPINGS.getFluidTags().entrySet().iterator();
                final Iterator iterator3;
                while (iterator3.hasNext()) {
                    final Map.Entry<String, int[]> tag3 = iterator3.next();
                    wrapper.write(Type.STRING, tag3.getKey());
                    wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, tag3.getValue());
                }
            }).scheduleSend(Protocol1_13To1_12_2.class);
        });
    }
}
