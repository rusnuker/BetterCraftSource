// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage.loot.functions;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.RandomValueRange;

public class LootingEnchantBonus extends LootFunction
{
    private final RandomValueRange count;
    private final int limit;
    
    public LootingEnchantBonus(final LootCondition[] p_i47145_1_, final RandomValueRange p_i47145_2_, final int p_i47145_3_) {
        super(p_i47145_1_);
        this.count = p_i47145_2_;
        this.limit = p_i47145_3_;
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        final Entity entity = context.getKiller();
        if (entity instanceof EntityLivingBase) {
            final int i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
            if (i == 0) {
                return stack;
            }
            final float f = i * this.count.generateFloat(rand);
            stack.func_190917_f(Math.round(f));
            if (this.limit != 0 && stack.func_190916_E() > this.limit) {
                stack.func_190920_e(this.limit);
            }
        }
        return stack;
    }
    
    public static class Serializer extends LootFunction.Serializer<LootingEnchantBonus>
    {
        protected Serializer() {
            super(new ResourceLocation("looting_enchant"), LootingEnchantBonus.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final LootingEnchantBonus functionClazz, final JsonSerializationContext serializationContext) {
            object.add("count", serializationContext.serialize(functionClazz.count));
            if (functionClazz.limit > 0) {
                object.add("limit", serializationContext.serialize(functionClazz.limit));
            }
        }
        
        @Override
        public LootingEnchantBonus deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            final int i = JsonUtils.getInt(object, "limit", 0);
            return new LootingEnchantBonus(conditionsIn, JsonUtils.deserializeClass(object, "count", deserializationContext, (Class<? extends RandomValueRange>)RandomValueRange.class), i);
        }
    }
}
