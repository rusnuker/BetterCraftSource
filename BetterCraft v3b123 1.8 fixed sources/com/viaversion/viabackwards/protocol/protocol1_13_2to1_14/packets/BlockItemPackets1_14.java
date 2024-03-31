// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import java.util.Set;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionLight;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.Environment;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionLightImpl;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viabackwards.api.rewriters.EnchantmentRewriter;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_14 extends ItemRewriter<ClientboundPackets1_14, ServerboundPackets1_13, Protocol1_13_2To1_14>
{
    private EnchantmentRewriter enchantmentRewriter;
    
    public BlockItemPackets1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //     4: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //     7: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.EDIT_BOOK:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //    10: aload_0         /* this */
        //    11: invokedynamic   BootstrapMethod #0, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //    16: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    19: aload_0         /* this */
        //    20: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    23: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //    26: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.OPEN_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //    29: invokedynamic   BootstrapMethod #1, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //    34: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    37: aload_0         /* this */
        //    38: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    41: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //    44: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.OPEN_HORSE_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //    47: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.OPEN_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    50: invokedynamic   BootstrapMethod #2, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //    55: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    58: new             Lcom/viaversion/viaversion/rewriter/BlockRewriter;
        //    61: dup            
        //    62: aload_0         /* this */
        //    63: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    66: getstatic       com/viaversion/viaversion/api/type/Type.POSITION:Lcom/viaversion/viaversion/api/type/Type;
        //    69: invokespecial   com/viaversion/viaversion/rewriter/BlockRewriter.<init>:(Lcom/viaversion/viaversion/api/protocol/Protocol;Lcom/viaversion/viaversion/api/type/Type;)V
        //    72: astore_1        /* blockRewriter */
        //    73: aload_0         /* this */
        //    74: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.COOLDOWN:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //    77: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.registerSetCooldown:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    80: aload_0         /* this */
        //    81: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.WINDOW_ITEMS:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //    84: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM_ARRAY:Lcom/viaversion/viaversion/api/type/Type;
        //    87: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.registerWindowItems:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //    90: aload_0         /* this */
        //    91: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.SET_SLOT:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //    94: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //    97: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.registerSetSlot:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   100: aload_0         /* this */
        //   101: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.ADVANCEMENTS:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   104: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   107: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.registerAdvancements:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   110: aload_0         /* this */
        //   111: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   114: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   117: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.TRADE_LIST:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   120: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //   123: aload_0         /* this */
        //   124: invokedynamic   BootstrapMethod #3, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   129: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   132: aload_0         /* this */
        //   133: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   136: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   139: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.OPEN_BOOK:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   142: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //   145: invokedynamic   BootstrapMethod #4, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   150: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   153: aload_0         /* this */
        //   154: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   157: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   160: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.ENTITY_EQUIPMENT:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   163: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$1;
        //   166: dup            
        //   167: aload_0         /* this */
        //   168: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$1.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   171: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   174: new             Lcom/viaversion/viaversion/rewriter/RecipeRewriter;
        //   177: dup            
        //   178: aload_0         /* this */
        //   179: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   182: invokespecial   com/viaversion/viaversion/rewriter/RecipeRewriter.<init>:(Lcom/viaversion/viaversion/api/protocol/Protocol;)V
        //   185: astore_2        /* recipeHandler */
        //   186: ldc             "crafting_special_suspiciousstew"
        //   188: ldc             "blasting"
        //   190: ldc             "smoking"
        //   192: ldc             "campfire_cooking"
        //   194: ldc             "stonecutting"
        //   196: invokestatic    com/google/common/collect/ImmutableSet.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;
        //   199: astore_3        /* removedTypes */
        //   200: aload_0         /* this */
        //   201: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   204: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   207: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.DECLARE_RECIPES:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   210: aload_3         /* removedTypes */
        //   211: aload_2         /* recipeHandler */
        //   212: invokedynamic   BootstrapMethod #5, handle:(Ljava/util/Set;Lcom/viaversion/viaversion/rewriter/RecipeRewriter;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   217: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   220: aload_0         /* this */
        //   221: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.CLICK_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   224: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   227: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.registerClickWindow:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   230: aload_0         /* this */
        //   231: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ServerboundPackets1_13;
        //   234: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   237: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.registerCreativeInvAction:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   240: aload_0         /* this */
        //   241: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   244: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   247: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.BLOCK_BREAK_ANIMATION:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   250: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$2;
        //   253: dup            
        //   254: aload_0         /* this */
        //   255: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$2.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   258: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   261: aload_0         /* this */
        //   262: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   265: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   268: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.BLOCK_ENTITY_DATA:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   271: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$3;
        //   274: dup            
        //   275: aload_0         /* this */
        //   276: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$3.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   279: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   282: aload_0         /* this */
        //   283: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   286: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   289: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.BLOCK_ACTION:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   292: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$4;
        //   295: dup            
        //   296: aload_0         /* this */
        //   297: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$4.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   300: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   303: aload_0         /* this */
        //   304: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   307: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   310: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.BLOCK_CHANGE:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   313: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$5;
        //   316: dup            
        //   317: aload_0         /* this */
        //   318: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$5.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   321: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   324: aload_1         /* blockRewriter */
        //   325: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.MULTI_BLOCK_CHANGE:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   328: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerMultiBlockChange:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   331: aload_0         /* this */
        //   332: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   335: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   338: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.EXPLOSION:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   341: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$6;
        //   344: dup            
        //   345: aload_0         /* this */
        //   346: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$6.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   349: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   352: aload_0         /* this */
        //   353: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   356: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   359: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.CHUNK_DATA:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   362: aload_0         /* this */
        //   363: invokedynamic   BootstrapMethod #6, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   368: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   371: aload_0         /* this */
        //   372: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   375: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   378: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.UNLOAD_CHUNK:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   381: invokedynamic   BootstrapMethod #7, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   386: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   389: aload_0         /* this */
        //   390: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   393: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   396: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.EFFECT:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   399: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$7;
        //   402: dup            
        //   403: aload_0         /* this */
        //   404: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$7.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   407: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   410: aload_0         /* this */
        //   411: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.SPAWN_PARTICLE:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   414: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   417: getstatic       com/viaversion/viaversion/api/type/Type.FLOAT:Lcom/viaversion/viaversion/api/type/types/FloatType;
        //   420: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.registerSpawnParticle:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;Lcom/viaversion/viaversion/api/type/Type;)V
        //   423: aload_0         /* this */
        //   424: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   427: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   430: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.MAP_DATA:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   433: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$8;
        //   436: dup            
        //   437: aload_0         /* this */
        //   438: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$8.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   441: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   444: aload_0         /* this */
        //   445: getfield        com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   448: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14;
        //   451: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14.SPAWN_POSITION:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ClientboundPackets1_14;
        //   454: new             Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$9;
        //   457: dup            
        //   458: aload_0         /* this */
        //   459: invokespecial   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14$9.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/BlockItemPackets1_14;)V
        //   462: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/Protocol1_13_2To1_14.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   465: return         
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
    protected void registerRewrites() {
        (this.enchantmentRewriter = new EnchantmentRewriter(this, false)).registerEnchantment("minecraft:multishot", "§7Multishot");
        this.enchantmentRewriter.registerEnchantment("minecraft:quick_charge", "§7Quick Charge");
        this.enchantmentRewriter.registerEnchantment("minecraft:piercing", "§7Piercing");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        final CompoundTag tag = item.tag();
        final CompoundTag display;
        if (tag != null && (display = tag.get("display")) != null) {
            final ListTag lore = display.get("Lore");
            if (lore != null) {
                this.saveListTag(display, lore, "Lore");
                for (final Tag loreEntry : lore) {
                    if (!(loreEntry instanceof StringTag)) {
                        continue;
                    }
                    final StringTag loreEntryTag = (StringTag)loreEntry;
                    final String value = loreEntryTag.getValue();
                    if (value == null || value.isEmpty()) {
                        continue;
                    }
                    loreEntryTag.setValue(ChatRewriter.jsonToLegacyText(value));
                }
            }
        }
        this.enchantmentRewriter.handleToClient(item);
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        final CompoundTag tag = item.tag();
        final CompoundTag display;
        if (tag != null && (display = tag.get("display")) != null) {
            final ListTag lore = display.get("Lore");
            if (lore != null && !this.hasBackupTag(display, "Lore")) {
                for (final Tag loreEntry : lore) {
                    if (loreEntry instanceof StringTag) {
                        final StringTag loreEntryTag = (StringTag)loreEntry;
                        loreEntryTag.setValue(ChatRewriter.legacyTextToJsonString(loreEntryTag.getValue()));
                    }
                }
            }
        }
        this.enchantmentRewriter.handleToServer(item);
        super.handleItemToServer(item);
        return item;
    }
}
