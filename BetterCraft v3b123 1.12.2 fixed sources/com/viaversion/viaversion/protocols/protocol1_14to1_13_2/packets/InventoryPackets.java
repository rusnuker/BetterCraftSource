// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets;

import com.viaversion.viaversion.libs.gson.JsonObject;
import com.google.common.collect.Sets;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import java.util.concurrent.ThreadLocalRandom;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.rewriter.ComponentRewriter;
import java.util.Set;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.rewriter.ItemRewriter;

public class InventoryPackets extends ItemRewriter<ClientboundPackets1_13, ServerboundPackets1_14, Protocol1_14To1_13_2>
{
    private static final String NBT_TAG_NAME;
    private static final Set<String> REMOVED_RECIPE_TYPES;
    private static final ComponentRewriter<ClientboundPackets1_13> COMPONENT_REWRITER;
    
    public InventoryPackets(final Protocol1_14To1_13_2 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.COOLDOWN:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //     4: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.registerSetCooldown:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;)V
        //     7: aload_0         /* this */
        //     8: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.ADVANCEMENTS:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    11: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //    14: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.registerAdvancements:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //    17: aload_0         /* this */
        //    18: getfield        com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    21: checkcast       Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/Protocol1_14To1_13_2;
        //    24: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.OPEN_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    27: aconst_null    
        //    28: invokedynamic   BootstrapMethod #0, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //    33: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/Protocol1_14To1_13_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    36: aload_0         /* this */
        //    37: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.WINDOW_ITEMS:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    40: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM_ARRAY:Lcom/viaversion/viaversion/api/type/Type;
        //    43: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.registerWindowItems:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //    46: aload_0         /* this */
        //    47: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.SET_SLOT:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    50: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //    53: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.registerSetSlot:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //    56: aload_0         /* this */
        //    57: getfield        com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    60: checkcast       Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/Protocol1_14To1_13_2;
        //    63: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.PLUGIN_MESSAGE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    66: new             Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets$2;
        //    69: dup            
        //    70: aload_0         /* this */
        //    71: invokespecial   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets$2.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets;)V
        //    74: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/Protocol1_14To1_13_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //    77: aload_0         /* this */
        //    78: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.ENTITY_EQUIPMENT:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //    81: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //    84: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.registerEntityEquipment:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //    87: new             Lcom/viaversion/viaversion/rewriter/RecipeRewriter;
        //    90: dup            
        //    91: aload_0         /* this */
        //    92: getfield        com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //    95: invokespecial   com/viaversion/viaversion/rewriter/RecipeRewriter.<init>:(Lcom/viaversion/viaversion/api/protocol/Protocol;)V
        //    98: astore_1        /* recipeRewriter */
        //    99: aload_0         /* this */
        //   100: getfield        com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   103: checkcast       Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/Protocol1_14To1_13_2;
        //   106: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.DECLARE_RECIPES:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //   109: aload_1         /* recipeRewriter */
        //   110: invokedynamic   BootstrapMethod #1, handle:(Lcom/viaversion/viaversion/rewriter/RecipeRewriter;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   115: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/Protocol1_14To1_13_2.registerClientbound:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   118: aload_0         /* this */
        //   119: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ServerboundPackets1_14.CLICK_WINDOW:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ServerboundPackets1_14;
        //   122: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   125: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.registerClickWindow:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   128: aload_0         /* this */
        //   129: getfield        com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.protocol:Lcom/viaversion/viaversion/api/protocol/Protocol;
        //   132: checkcast       Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/Protocol1_14To1_13_2;
        //   135: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ServerboundPackets1_14.SELECT_TRADE:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ServerboundPackets1_14;
        //   138: invokedynamic   BootstrapMethod #2, handle:()Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
        //   143: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/Protocol1_14To1_13_2.registerServerbound:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
        //   146: aload_0         /* this */
        //   147: getstatic       com/viaversion/viaversion/protocols/protocol1_14to1_13_2/ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION:Lcom/viaversion/viaversion/protocols/protocol1_14to1_13_2/ServerboundPackets1_14;
        //   150: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   153: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.registerCreativeInvAction:(Lcom/viaversion/viaversion/api/protocol/packet/ServerboundPacketType;Lcom/viaversion/viaversion/api/type/Type;)V
        //   156: aload_0         /* this */
        //   157: getstatic       com/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13.SPAWN_PARTICLE:Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/ClientboundPackets1_13;
        //   160: getstatic       com/viaversion/viaversion/api/type/Type.FLAT_VAR_INT_ITEM:Lcom/viaversion/viaversion/api/type/Type;
        //   163: getstatic       com/viaversion/viaversion/api/type/Type.FLOAT:Lcom/viaversion/viaversion/api/type/types/FloatType;
        //   166: invokevirtual   com/viaversion/viaversion/protocols/protocol1_14to1_13_2/packets/InventoryPackets.registerSpawnParticle:(Lcom/viaversion/viaversion/api/protocol/packet/ClientboundPacketType;Lcom/viaversion/viaversion/api/type/Type;Lcom/viaversion/viaversion/api/type/Type;)V
        //   169: return         
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
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        item.setIdentifier(Protocol1_14To1_13_2.MAPPINGS.getNewItemId(item.identifier()));
        if (item.tag() == null) {
            return item;
        }
        final Tag displayTag = item.tag().get("display");
        if (displayTag instanceof CompoundTag) {
            final CompoundTag display = (CompoundTag)displayTag;
            final Tag loreTag = display.get("Lore");
            if (loreTag instanceof ListTag) {
                final ListTag lore = (ListTag)loreTag;
                display.put(InventoryPackets.NBT_TAG_NAME + "|Lore", new ListTag(lore.clone().getValue()));
                for (final Tag loreEntry : lore) {
                    if (loreEntry instanceof StringTag) {
                        final String jsonText = ChatRewriter.legacyTextToJsonString(((StringTag)loreEntry).getValue(), true);
                        ((StringTag)loreEntry).setValue(jsonText);
                    }
                }
            }
        }
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        item.setIdentifier(Protocol1_14To1_13_2.MAPPINGS.getOldItemId(item.identifier()));
        if (item.tag() == null) {
            return item;
        }
        final Tag displayTag = item.tag().get("display");
        if (displayTag instanceof CompoundTag) {
            final CompoundTag display = (CompoundTag)displayTag;
            final Tag loreTag = display.get("Lore");
            if (loreTag instanceof ListTag) {
                final ListTag lore = (ListTag)loreTag;
                final ListTag savedLore = display.remove(InventoryPackets.NBT_TAG_NAME + "|Lore");
                if (savedLore != null) {
                    display.put("Lore", new ListTag(savedLore.getValue()));
                }
                else {
                    for (final Tag loreEntry : lore) {
                        if (loreEntry instanceof StringTag) {
                            ((StringTag)loreEntry).setValue(ChatRewriter.jsonToLegacyText(((StringTag)loreEntry).getValue()));
                        }
                    }
                }
            }
        }
        return item;
    }
    
    static {
        NBT_TAG_NAME = "ViaVersion|" + Protocol1_14To1_13_2.class.getSimpleName();
        REMOVED_RECIPE_TYPES = Sets.newHashSet("crafting_special_banneraddpattern", "crafting_special_repairitem");
        COMPONENT_REWRITER = new ComponentRewriter<ClientboundPackets1_13>() {
            @Override
            protected void handleTranslate(final JsonObject object, final String translate) {
                super.handleTranslate(object, translate);
                if (translate.startsWith("block.") && translate.endsWith(".name")) {
                    object.addProperty("translate", translate.substring(0, translate.length() - 5));
                }
            }
        };
    }
}
