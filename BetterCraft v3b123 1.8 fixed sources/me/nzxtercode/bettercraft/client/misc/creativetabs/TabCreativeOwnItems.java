// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.creativetabs;

import net.minecraft.nbt.JsonToNBT;
import com.google.common.collect.Lists;
import java.util.List;
import com.google.gson.JsonArray;
import java.io.File;
import me.nzxtercode.bettercraft.client.Config;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

public class TabCreativeOwnItems
{
    public static void saveItemstack(final ItemStack stack) {
        final JsonArray jsonArray = getJsonArray();
        final JsonObject json = new JsonObject();
        json.add("id", new JsonPrimitive(Item.getIdFromItem(stack.getItem())));
        json.add("meta", new JsonPrimitive(stack.getMetadata()));
        json.add("amount", new JsonPrimitive(stack.stackSize));
        if (stack.hasTagCompound()) {
            json.add("nbt", new JsonPrimitive(stack.stackTagCompound.toString()));
        }
        jsonArray.add(json);
        Config.getInstance();
        Config.getInstance();
        Config.write(new File(Config.ROOT_DIR, "items.json"), jsonArray);
    }
    
    public static List<ItemStack> getItemstacks() {
        final List<ItemStack> itemStackList = (List<ItemStack>)Lists.newArrayList();
        getJsonArray().forEach(jsonElement -> {
            try {
                final JsonObject json = jsonElement.getAsJsonObject();
                final ItemStack stack = new ItemStack(Item.getItemById(json.get("id").getAsInt()), json.get("amount").getAsInt(), json.get("meta").getAsInt());
                if (json.has("nbt")) {
                    stack.stackTagCompound = JsonToNBT.getTagFromJson(json.get("nbt").getAsString());
                }
                list.add(stack);
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
            return;
        });
        return itemStackList;
    }
    
    private static final JsonArray getJsonArray() {
        Config.getInstance();
        final File file = new File(Config.ROOT_DIR, "items.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (final Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        Config.getInstance();
        final JsonElement jsonElement = Config.read(file);
        return jsonElement.isJsonNull() ? new JsonArray() : jsonElement.getAsJsonArray();
    }
}
