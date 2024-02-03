// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.rewriter;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.RecipeRewriter;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class RecipeRewriter1_19_3<C extends ClientboundPacketType> extends RecipeRewriter<C>
{
    public RecipeRewriter1_19_3(final Protocol<C, ?, ?, ?> protocol) {
        super(protocol);
        this.recipeHandlers.put("crafting_special_armordye", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_bookcloning", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_mapcloning", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_mapextending", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_firework_rocket", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_firework_star", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_firework_star_fade", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_tippedarrow", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_bannerduplicate", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_shielddecoration", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_shulkerboxcoloring", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_suspiciousstew", this::handleSimpleRecipe);
        this.recipeHandlers.put("crafting_special_repairitem", this::handleSimpleRecipe);
    }
    
    @Override
    public void handleCraftingShapeless(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough(Type.STRING);
        wrapper.passthrough((Type<Object>)Type.VAR_INT);
        this.handleIngredients(wrapper);
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
    
    @Override
    public void handleCraftingShaped(final PacketWrapper wrapper) throws Exception {
        final int ingredients = wrapper.passthrough((Type<Integer>)Type.VAR_INT) * wrapper.passthrough((Type<Integer>)Type.VAR_INT);
        wrapper.passthrough(Type.STRING);
        wrapper.passthrough((Type<Object>)Type.VAR_INT);
        for (int i = 0; i < ingredients; ++i) {
            this.handleIngredient(wrapper);
        }
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
    
    @Override
    public void handleSmelting(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough(Type.STRING);
        wrapper.passthrough((Type<Object>)Type.VAR_INT);
        this.handleIngredient(wrapper);
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
        wrapper.passthrough((Type<Object>)Type.FLOAT);
        wrapper.passthrough((Type<Object>)Type.VAR_INT);
    }
}
