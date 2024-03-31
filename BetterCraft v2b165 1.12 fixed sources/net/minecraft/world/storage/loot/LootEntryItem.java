// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage.loot;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.util.ResourceLocation;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import java.util.Random;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.item.Item;

public class LootEntryItem extends LootEntry
{
    protected final Item item;
    protected final LootFunction[] functions;
    
    public LootEntryItem(final Item itemIn, final int weightIn, final int qualityIn, final LootFunction[] functionsIn, final LootCondition[] conditionsIn) {
        super(weightIn, qualityIn, conditionsIn);
        this.item = itemIn;
        this.functions = functionsIn;
    }
    
    @Override
    public void addLoot(final Collection<ItemStack> stacks, final Random rand, final LootContext context) {
        ItemStack itemstack = new ItemStack(this.item);
        LootFunction[] functions;
        for (int length = (functions = this.functions).length, j = 0; j < length; ++j) {
            final LootFunction lootfunction = functions[j];
            if (LootConditionManager.testAllConditions(lootfunction.getConditions(), rand, context)) {
                itemstack = lootfunction.apply(itemstack, rand, context);
            }
        }
        if (!itemstack.func_190926_b()) {
            if (itemstack.func_190916_E() < this.item.getItemStackLimit()) {
                stacks.add(itemstack);
            }
            else {
                int i = itemstack.func_190916_E();
                while (i > 0) {
                    final ItemStack itemstack2 = itemstack.copy();
                    itemstack2.func_190920_e(Math.min(itemstack.getMaxStackSize(), i));
                    i -= itemstack2.func_190916_E();
                    stacks.add(itemstack2);
                }
            }
        }
    }
    
    @Override
    protected void serialize(final JsonObject json, final JsonSerializationContext context) {
        if (this.functions != null && this.functions.length > 0) {
            json.add("functions", context.serialize(this.functions));
        }
        final ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.item);
        if (resourcelocation == null) {
            throw new IllegalArgumentException("Can't serialize unknown item " + this.item);
        }
        json.addProperty("name", resourcelocation.toString());
    }
    
    public static LootEntryItem deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final int weightIn, final int qualityIn, final LootCondition[] conditionsIn) {
        final Item item = JsonUtils.getItem(object, "name");
        LootFunction[] alootfunction;
        if (object.has("functions")) {
            alootfunction = JsonUtils.deserializeClass(object, "functions", deserializationContext, (Class<? extends LootFunction[]>)LootFunction[].class);
        }
        else {
            alootfunction = new LootFunction[0];
        }
        return new LootEntryItem(item, weightIn, qualityIn, alootfunction, conditionsIn);
    }
}
