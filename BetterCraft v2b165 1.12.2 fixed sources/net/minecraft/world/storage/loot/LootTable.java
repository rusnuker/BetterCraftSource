// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage.loot;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.Collections;
import net.minecraft.util.math.MathHelper;
import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootTable
{
    private static final Logger LOGGER;
    public static final LootTable EMPTY_LOOT_TABLE;
    private final LootPool[] pools;
    
    static {
        LOGGER = LogManager.getLogger();
        EMPTY_LOOT_TABLE = new LootTable(new LootPool[0]);
    }
    
    public LootTable(final LootPool[] poolsIn) {
        this.pools = poolsIn;
    }
    
    public List<ItemStack> generateLootForPools(final Random rand, final LootContext context) {
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        if (context.addLootTable(this)) {
            LootPool[] pools;
            for (int length = (pools = this.pools).length, i = 0; i < length; ++i) {
                final LootPool lootpool = pools[i];
                lootpool.generateLoot(list, rand, context);
            }
            context.removeLootTable(this);
        }
        else {
            LootTable.LOGGER.warn("Detected infinite loop in loot tables");
        }
        return list;
    }
    
    public void fillInventory(final IInventory inventory, final Random rand, final LootContext context) {
        final List<ItemStack> list = this.generateLootForPools(rand, context);
        final List<Integer> list2 = this.getEmptySlotsRandomized(inventory, rand);
        this.shuffleItems(list, list2.size(), rand);
        for (final ItemStack itemstack : list) {
            if (list2.isEmpty()) {
                LootTable.LOGGER.warn("Tried to over-fill a container");
                return;
            }
            if (itemstack.func_190926_b()) {
                inventory.setInventorySlotContents(list2.remove(list2.size() - 1), ItemStack.field_190927_a);
            }
            else {
                inventory.setInventorySlotContents(list2.remove(list2.size() - 1), itemstack);
            }
        }
    }
    
    private void shuffleItems(final List<ItemStack> stacks, int p_186463_2_, final Random rand) {
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        final Iterator<ItemStack> iterator = stacks.iterator();
        while (iterator.hasNext()) {
            final ItemStack itemstack = iterator.next();
            if (itemstack.func_190926_b()) {
                iterator.remove();
            }
            else {
                if (itemstack.func_190916_E() <= 1) {
                    continue;
                }
                list.add(itemstack);
                iterator.remove();
            }
        }
        p_186463_2_ -= stacks.size();
        while (p_186463_2_ > 0 && !list.isEmpty()) {
            final ItemStack itemstack2 = list.remove(MathHelper.getInt(rand, 0, list.size() - 1));
            final int i = MathHelper.getInt(rand, 1, itemstack2.func_190916_E() / 2);
            final ItemStack itemstack3 = itemstack2.splitStack(i);
            if (itemstack2.func_190916_E() > 1 && rand.nextBoolean()) {
                list.add(itemstack2);
            }
            else {
                stacks.add(itemstack2);
            }
            if (itemstack3.func_190916_E() > 1 && rand.nextBoolean()) {
                list.add(itemstack3);
            }
            else {
                stacks.add(itemstack3);
            }
        }
        stacks.addAll(list);
        Collections.shuffle(stacks, rand);
    }
    
    private List<Integer> getEmptySlotsRandomized(final IInventory inventory, final Random rand) {
        final List<Integer> list = (List<Integer>)Lists.newArrayList();
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            if (inventory.getStackInSlot(i).func_190926_b()) {
                list.add(i);
            }
        }
        Collections.shuffle(list, rand);
        return list;
    }
    
    public static class Serializer implements JsonDeserializer<LootTable>, JsonSerializer<LootTable>
    {
        @Override
        public LootTable deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "loot table");
            final LootPool[] alootpool = JsonUtils.deserializeClass(jsonobject, "pools", new LootPool[0], p_deserialize_3_, LootPool[].class);
            return new LootTable(alootpool);
        }
        
        @Override
        public JsonElement serialize(final LootTable p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            jsonobject.add("pools", p_serialize_3_.serialize(p_serialize_1_.pools));
            return jsonobject;
        }
    }
}
