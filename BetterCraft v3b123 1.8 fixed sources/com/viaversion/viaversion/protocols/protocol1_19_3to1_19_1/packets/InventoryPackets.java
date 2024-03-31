// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.packets;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.Protocol1_19_3To1_19_1;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ServerboundPackets1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ClientboundPackets1_19_1;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public final class InventoryPackets extends ItemRewriter<ClientboundPackets1_19_1, ServerboundPackets1_19_3, Protocol1_19_3To1_19_1>
{
    private static final int MISC_CRAFTING_BOOK_CATEGORY = 0;
    
    public InventoryPackets(final Protocol1_19_3To1_19_1 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* this */
        //     5: getfield        com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //     8: getstatic       com/viaversion/viaversion/api/type/Type.POSITION1_14:Lcom/viaversion/viaversion/api/type/Type;
        //    11: invokespecial   com/viaversion/viaversion/rewriter/BlockRewriter.<init>:(Lcom/viaversion/viaversion/api/protocol/Protocol;Lcom/viaversion/viaversion/api/type/Type;)V
        //    14: astore_1        /* blockRewriter */
        //    15: aload_1         /* blockRewriter */
        //    16: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.BLOCK_ACTION:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    19: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerBlockAction:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    22: aload_1         /* blockRewriter */
        //    23: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.BLOCK_CHANGE:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    26: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerBlockChange:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    29: aload_1         /* blockRewriter */
        //    30: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.MULTI_BLOCK_CHANGE:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    33: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerVarLongMultiBlockChange:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    36: aload_1         /* blockRewriter */
        //    37: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.EFFECT:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    40: sipush          1010
        //    43: sipush          2001
        //    46: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerEffect:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;II)V
        //    49: aload_1         /* blockRewriter */
        //    50: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.CHUNK_DATA:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    53: invokedynamic   BootstrapMethod #0, supply:()Lcom/viaversion/viaversion/rewriter/BlockRewriter$ChunkTypeSupplier;
        //    58: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerChunkData1_19:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/rewriter/BlockRewriter$ChunkTypeSupplier;)V
        //    61: aload_1         /* blockRewriter */
        //    62: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.BLOCK_ENTITY_DATA:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    65: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerBlockEntityData:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    68: aload_0         /* this */
        //    69: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.COOLDOWN:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    72: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerSetCooldown:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    75: aload_0         /* this */
        //    76: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.WINDOW_ITEMS:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    79: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerWindowItems1_17_1:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    82: aload_0         /* this */
        //    83: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.SET_SLOT:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    86: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerSetSlot1_17_1:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    89: aload_0         /* this */
        //    90: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.ADVANCEMENTS:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //    93: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //    96: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerAdvancements:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //    99: aload_0         /* this */
        //   100: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.ENTITY_EQUIPMENT:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //   103: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerEntityEquipmentArray:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   106: aload_0         /* this */
        //   107: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ServerboundPackets1_19_3.CLICK_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ServerboundPackets1_19_3;
        //   110: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerClickWindow1_17_1:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;)V
        //   113: aload_0         /* this */
        //   114: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.TRADE_LIST:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //   117: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerTradeList1_19:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   120: aload_0         /* this */
        //   121: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ServerboundPackets1_19_3.CREATIVE_INVENTORY_ACTION:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ServerboundPackets1_19_3;
        //   124: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   127: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerCreativeInvAction:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   130: aload_0         /* this */
        //   131: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.WINDOW_PROPERTY:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //   134: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerWindowPropertyEnchantmentHandler:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   137: aload_0         /* this */
        //   138: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.SPAWN_PARTICLE:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //   141: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.registerSpawnParticle1_19:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   144: new             Lcom/viaversion/viaversion/rewriter/RecipeRewriter;
        //   147: dup            
        //   148: aload_0         /* this */
        //   149: getfield        com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   152: invokespecial   com/viaversion/viaversion/rewriter/RecipeRewriter.<init>:(Lcom/viaversion/viaversion/api/protocol/Protocol;)V
        //   155: astore_2        /* recipeRewriter */
        //   156: aload_0         /* this */
        //   157: getfield        com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   160: checkcast       Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/Protocol1_19_3To1_19_1;
        //   163: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.DECLARE_RECIPES:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //   166: aload_0         /* this */
        //   167: aload_2         /* recipeRewriter */
        //   168: invokedynamic   BootstrapMethod #1, handle:(Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets;Lcom/viaversion/viaversion/rewriter/RecipeRewriter;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   173: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/Protocol1_19_3To1_19_1.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   176: aload_0         /* this */
        //   177: getfield        com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   180: checkcast       Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/Protocol1_19_3To1_19_1;
        //   183: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1.EXPLOSION:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ClientboundPackets1_19_1;
        //   186: new             Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets$1;
        //   189: dup            
        //   190: aload_0         /* this */
        //   191: invokespecial   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets$1.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/packets/InventoryPackets;)V
        //   194: invokevirtual   com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/Protocol1_19_3To1_19_1.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   197: return         
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
}
