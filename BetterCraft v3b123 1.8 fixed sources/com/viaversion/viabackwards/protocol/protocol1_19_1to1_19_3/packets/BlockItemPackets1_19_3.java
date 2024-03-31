// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.packets;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.Protocol1_19_1To1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ServerboundPackets1_19_1;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public final class BlockItemPackets1_19_3 extends ItemRewriter<ClientboundPackets1_19_3, ServerboundPackets1_19_1, Protocol1_19_1To1_19_3>
{
    public BlockItemPackets1_19_3(final Protocol1_19_1To1_19_3 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* this */
        //     5: getfield        com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //     8: getstatic       com/viaversion/viaversion/api/type/Type.POSITION1_14:Lcom/viaversion/viaversion/api/type/Type;
        //    11: invokespecial   com/viaversion/viaversion/rewriter/BlockRewriter.<init>:(Lcom/viaversion/viaversion/api/protocol/Protocol;Lcom/viaversion/viaversion/api/type/Type;)V
        //    14: astore_1        /* blockRewriter */
        //    15: aload_1         /* blockRewriter */
        //    16: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.BLOCK_ACTION:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    19: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerBlockAction:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    22: aload_1         /* blockRewriter */
        //    23: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.BLOCK_CHANGE:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    26: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerBlockChange:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    29: aload_1         /* blockRewriter */
        //    30: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.MULTI_BLOCK_CHANGE:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    33: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerVarLongMultiBlockChange:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    36: aload_1         /* blockRewriter */
        //    37: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.EFFECT:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    40: sipush          1010
        //    43: sipush          2001
        //    46: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerEffect:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;II)V
        //    49: aload_1         /* blockRewriter */
        //    50: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.CHUNK_DATA:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    53: invokedynamic   BootstrapMethod #0, supply:()Lcom/viaversion/viaversion/rewriter/BlockRewriter$ChunkTypeSupplier;
        //    58: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerChunkData1_19:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/rewriter/BlockRewriter$ChunkTypeSupplier;)V
        //    61: aload_1         /* blockRewriter */
        //    62: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.BLOCK_ENTITY_DATA:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    65: invokevirtual   com/viaversion/viaversion/rewriter/BlockRewriter.registerBlockEntityData:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    68: aload_0         /* this */
        //    69: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.COOLDOWN:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    72: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerSetCooldown:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    75: aload_0         /* this */
        //    76: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.WINDOW_ITEMS:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    79: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerWindowItems1_17_1:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    82: aload_0         /* this */
        //    83: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.SET_SLOT:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    86: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerSetSlot1_17_1:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    89: aload_0         /* this */
        //    90: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.ENTITY_EQUIPMENT:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //    93: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerEntityEquipmentArray:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //    96: aload_0         /* this */
        //    97: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.ADVANCEMENTS:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //   100: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   103: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerAdvancements:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   106: aload_0         /* this */
        //   107: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ServerboundPackets1_19_1.CLICK_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ServerboundPackets1_19_1;
        //   110: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerClickWindow1_17_1:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;)V
        //   113: aload_0         /* this */
        //   114: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.TRADE_LIST:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //   117: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerTradeList1_19:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   120: aload_0         /* this */
        //   121: getstatic       com/viaversion/viaversion/protocols/protocol1_19_1to1_19/ServerboundPackets1_19_1.CREATIVE_INVENTORY_ACTION:Lcom/viaversion/viaversion/protocols/protocol1_19_1to1_19/ServerboundPackets1_19_1;
        //   124: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   127: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerCreativeInvAction:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   130: aload_0         /* this */
        //   131: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.WINDOW_PROPERTY:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //   134: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerWindowPropertyEnchantmentHandler:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   137: aload_0         /* this */
        //   138: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.SPAWN_PARTICLE:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //   141: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.registerSpawnParticle1_19:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //   144: aload_0         /* this */
        //   145: getfield        com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   148: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/Protocol1_19_1To1_19_3;
        //   151: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.EXPLOSION:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //   154: new             Lcom/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3$1;
        //   157: dup            
        //   158: aload_0         /* this */
        //   159: invokespecial   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3$1.<init>:(Lcom/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3;)V
        //   162: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/Protocol1_19_1To1_19_3.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   165: new             Lcom/viaversion/viaversion/rewriter/RecipeRewriter;
        //   168: dup            
        //   169: aload_0         /* this */
        //   170: getfield        com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   173: invokespecial   com/viaversion/viaversion/rewriter/RecipeRewriter.<init>:(Lcom/viaversion/viaversion/api/protocol/Protocol;)V
        //   176: astore_2        /* recipeRewriter */
        //   177: aload_0         /* this */
        //   178: getfield        com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   181: checkcast       Lcom/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/Protocol1_19_1To1_19_3;
        //   184: getstatic       com/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3.DECLARE_RECIPES:Lcom/viaversion/viaversion/protocols/protocol1_19_3to1_19_1/ClientboundPackets1_19_3;
        //   187: aload_0         /* this */
        //   188: aload_2         /* recipeRewriter */
        //   189: invokedynamic   BootstrapMethod #1, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/packets/BlockItemPackets1_19_3;Lcom/viaversion/viaversion/rewriter/RecipeRewriter;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   194: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_19_1to1_19_3/Protocol1_19_1To1_19_3.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
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
