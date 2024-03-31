// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.enchantment.Enchantment;
import java.util.Map;
import net.minecraft.potion.PotionUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.potion.PotionType;
import net.minecraft.item.Item;

public class ItemPredicate
{
    public static final ItemPredicate field_192495_a;
    private final Item field_192496_b;
    private final Integer field_192497_c;
    private final MinMaxBounds field_192498_d;
    private final MinMaxBounds field_193444_e;
    private final EnchantmentPredicate[] field_192499_e;
    private final PotionType field_192500_f;
    private final NBTPredicate field_193445_h;
    
    static {
        field_192495_a = new ItemPredicate();
    }
    
    public ItemPredicate() {
        this.field_192496_b = null;
        this.field_192497_c = null;
        this.field_192500_f = null;
        this.field_192498_d = MinMaxBounds.field_192516_a;
        this.field_193444_e = MinMaxBounds.field_192516_a;
        this.field_192499_e = new EnchantmentPredicate[0];
        this.field_193445_h = NBTPredicate.field_193479_a;
    }
    
    public ItemPredicate(@Nullable final Item p_i47540_1_, @Nullable final Integer p_i47540_2_, final MinMaxBounds p_i47540_3_, final MinMaxBounds p_i47540_4_, final EnchantmentPredicate[] p_i47540_5_, @Nullable final PotionType p_i47540_6_, final NBTPredicate p_i47540_7_) {
        this.field_192496_b = p_i47540_1_;
        this.field_192497_c = p_i47540_2_;
        this.field_192498_d = p_i47540_3_;
        this.field_193444_e = p_i47540_4_;
        this.field_192499_e = p_i47540_5_;
        this.field_192500_f = p_i47540_6_;
        this.field_193445_h = p_i47540_7_;
    }
    
    public boolean func_192493_a(final ItemStack p_192493_1_) {
        if (this.field_192496_b != null && p_192493_1_.getItem() != this.field_192496_b) {
            return false;
        }
        if (this.field_192497_c != null && p_192493_1_.getMetadata() != this.field_192497_c) {
            return false;
        }
        if (!this.field_192498_d.func_192514_a((float)p_192493_1_.func_190916_E())) {
            return false;
        }
        if (this.field_193444_e != MinMaxBounds.field_192516_a && !p_192493_1_.isItemStackDamageable()) {
            return false;
        }
        if (!this.field_193444_e.func_192514_a((float)(p_192493_1_.getMaxDamage() - p_192493_1_.getItemDamage()))) {
            return false;
        }
        if (!this.field_193445_h.func_193478_a(p_192493_1_)) {
            return false;
        }
        final Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(p_192493_1_);
        for (int i = 0; i < this.field_192499_e.length; ++i) {
            if (!this.field_192499_e[i].func_192463_a(map)) {
                return false;
            }
        }
        final PotionType potiontype = PotionUtils.getPotionFromItem(p_192493_1_);
        return this.field_192500_f == null || this.field_192500_f == potiontype;
    }
    
    public static ItemPredicate func_192492_a(@Nullable final JsonElement p_192492_0_) {
        if (p_192492_0_ != null && !p_192492_0_.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_192492_0_, "item");
            final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("count"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(jsonobject.get("durability"));
            final Integer integer = jsonobject.has("data") ? Integer.valueOf(JsonUtils.getInt(jsonobject, "data")) : null;
            final NBTPredicate nbtpredicate = NBTPredicate.func_193476_a(jsonobject.get("nbt"));
            Item item = null;
            if (jsonobject.has("item")) {
                final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "item"));
                item = Item.REGISTRY.getObject(resourcelocation);
                if (item == null) {
                    throw new JsonSyntaxException("Unknown item id '" + resourcelocation + "'");
                }
            }
            final EnchantmentPredicate[] aenchantmentpredicate = EnchantmentPredicate.func_192465_b(jsonobject.get("enchantments"));
            PotionType potiontype = null;
            if (jsonobject.has("potion")) {
                final ResourceLocation resourcelocation2 = new ResourceLocation(JsonUtils.getString(jsonobject, "potion"));
                if (!PotionType.REGISTRY.containsKey(resourcelocation2)) {
                    throw new JsonSyntaxException("Unknown potion '" + resourcelocation2 + "'");
                }
                potiontype = PotionType.REGISTRY.getObject(resourcelocation2);
            }
            return new ItemPredicate(item, integer, minmaxbounds, minmaxbounds2, aenchantmentpredicate, potiontype, nbtpredicate);
        }
        return ItemPredicate.field_192495_a;
    }
    
    public static ItemPredicate[] func_192494_b(@Nullable final JsonElement p_192494_0_) {
        if (p_192494_0_ != null && !p_192494_0_.isJsonNull()) {
            final JsonArray jsonarray = JsonUtils.getJsonArray(p_192494_0_, "items");
            final ItemPredicate[] aitempredicate = new ItemPredicate[jsonarray.size()];
            for (int i = 0; i < aitempredicate.length; ++i) {
                aitempredicate[i] = func_192492_a(jsonarray.get(i));
            }
            return aitempredicate;
        }
        return new ItemPredicate[0];
    }
}
