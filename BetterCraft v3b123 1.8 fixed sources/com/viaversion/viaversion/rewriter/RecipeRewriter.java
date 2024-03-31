// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class RecipeRewriter<C extends ClientboundPacketType>
{
    protected final Protocol<C, ?, ?, ?> protocol;
    protected final Map<String, RecipeConsumer> recipeHandlers;
    
    public RecipeRewriter(final Protocol<C, ?, ?, ?> protocol) {
        this.recipeHandlers = new HashMap<String, RecipeConsumer>();
        this.protocol = protocol;
        this.recipeHandlers.put("crafting_shapeless", this::handleCraftingShapeless);
        this.recipeHandlers.put("crafting_shaped", this::handleCraftingShaped);
        this.recipeHandlers.put("smelting", this::handleSmelting);
        this.recipeHandlers.put("blasting", this::handleSmelting);
        this.recipeHandlers.put("smoking", this::handleSmelting);
        this.recipeHandlers.put("campfire_cooking", this::handleSmelting);
        this.recipeHandlers.put("stonecutting", this::handleStonecutting);
        this.recipeHandlers.put("smithing", this::handleSmithing);
        this.recipeHandlers.put("smithing_transform", this::handleSmithingTransform);
        this.recipeHandlers.put("smithing_trim", this::handleSmithingTrim);
        this.recipeHandlers.put("crafting_decorated_pot", this::handleSimpleRecipe);
    }
    
    public void handleRecipeType(final PacketWrapper wrapper, final String type) throws Exception {
        final RecipeConsumer handler = this.recipeHandlers.get(type);
        if (handler != null) {
            handler.accept(wrapper);
        }
    }
    
    public void register(final C packetType) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                final String type = wrapper.passthrough(Type.STRING);
                wrapper.passthrough(Type.STRING);
                this.handleRecipeType(wrapper, Key.stripMinecraftNamespace(type));
            }
        });
    }
    
    public void handleCraftingShaped(final PacketWrapper wrapper) throws Exception {
        final int ingredientsNo = wrapper.passthrough((Type<Integer>)Type.VAR_INT) * wrapper.passthrough((Type<Integer>)Type.VAR_INT);
        wrapper.passthrough(Type.STRING);
        for (int i = 0; i < ingredientsNo; ++i) {
            this.handleIngredient(wrapper);
        }
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
    
    public void handleCraftingShapeless(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough(Type.STRING);
        this.handleIngredients(wrapper);
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
    
    public void handleSmelting(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough(Type.STRING);
        this.handleIngredient(wrapper);
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
        wrapper.passthrough((Type<Object>)Type.FLOAT);
        wrapper.passthrough((Type<Object>)Type.VAR_INT);
    }
    
    public void handleStonecutting(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough(Type.STRING);
        this.handleIngredient(wrapper);
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
    
    public void handleSmithing(final PacketWrapper wrapper) throws Exception {
        this.handleIngredient(wrapper);
        this.handleIngredient(wrapper);
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
    
    public void handleSimpleRecipe(final PacketWrapper wrapper) throws Exception {
        wrapper.passthrough((Type<Object>)Type.VAR_INT);
    }
    
    public void handleSmithingTransform(final PacketWrapper wrapper) throws Exception {
        this.handleIngredient(wrapper);
        this.handleIngredient(wrapper);
        this.handleIngredient(wrapper);
        this.rewrite(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
    }
    
    public void handleSmithingTrim(final PacketWrapper wrapper) throws Exception {
        this.handleIngredient(wrapper);
        this.handleIngredient(wrapper);
        this.handleIngredient(wrapper);
    }
    
    protected void rewrite(final Item item) {
        if (this.protocol.getItemRewriter() != null) {
            this.protocol.getItemRewriter().handleItemToClient(item);
        }
    }
    
    protected void handleIngredient(final PacketWrapper wrapper) throws Exception {
        final Item[] array;
        final Item[] items = array = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
        for (final Item item : array) {
            this.rewrite(item);
        }
    }
    
    protected void handleIngredients(final PacketWrapper wrapper) throws Exception {
        for (int ingredients = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < ingredients; ++i) {
            this.handleIngredient(wrapper);
        }
    }
    
    @FunctionalInterface
    public interface RecipeConsumer
    {
        void accept(final PacketWrapper p0) throws Exception;
    }
}
