// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Maps;
import net.minecraft.util.JsonUtils;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.util.Collections;
import net.minecraft.potion.Potion;
import java.util.Map;

public class MobEffectsPredicate
{
    public static final MobEffectsPredicate field_193473_a;
    private final Map<Potion, InstancePredicate> field_193474_b;
    
    static {
        field_193473_a = new MobEffectsPredicate(Collections.emptyMap());
    }
    
    public MobEffectsPredicate(final Map<Potion, InstancePredicate> p_i47538_1_) {
        this.field_193474_b = p_i47538_1_;
    }
    
    public boolean func_193469_a(final Entity p_193469_1_) {
        return this == MobEffectsPredicate.field_193473_a || (p_193469_1_ instanceof EntityLivingBase && this.func_193470_a(((EntityLivingBase)p_193469_1_).func_193076_bZ()));
    }
    
    public boolean func_193472_a(final EntityLivingBase p_193472_1_) {
        return this == MobEffectsPredicate.field_193473_a || this.func_193470_a(p_193472_1_.func_193076_bZ());
    }
    
    public boolean func_193470_a(final Map<Potion, PotionEffect> p_193470_1_) {
        if (this == MobEffectsPredicate.field_193473_a) {
            return true;
        }
        for (final Map.Entry<Potion, InstancePredicate> entry : this.field_193474_b.entrySet()) {
            final PotionEffect potioneffect = p_193470_1_.get(entry.getKey());
            if (!entry.getValue().func_193463_a(potioneffect)) {
                return false;
            }
        }
        return true;
    }
    
    public static MobEffectsPredicate func_193471_a(@Nullable final JsonElement p_193471_0_) {
        if (p_193471_0_ != null && !p_193471_0_.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_193471_0_, "effects");
            final Map<Potion, InstancePredicate> map = (Map<Potion, InstancePredicate>)Maps.newHashMap();
            for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                final ResourceLocation resourcelocation = new ResourceLocation(entry.getKey());
                final Potion potion = Potion.REGISTRY.getObject(resourcelocation);
                if (potion == null) {
                    throw new JsonSyntaxException("Unknown effect '" + resourcelocation + "'");
                }
                final InstancePredicate mobeffectspredicate$instancepredicate = InstancePredicate.func_193464_a(JsonUtils.getJsonObject(entry.getValue(), entry.getKey()));
                map.put(potion, mobeffectspredicate$instancepredicate);
            }
            return new MobEffectsPredicate(map);
        }
        return MobEffectsPredicate.field_193473_a;
    }
    
    public static class InstancePredicate
    {
        private final MinMaxBounds field_193465_a;
        private final MinMaxBounds field_193466_b;
        @Nullable
        private final Boolean field_193467_c;
        @Nullable
        private final Boolean field_193468_d;
        
        public InstancePredicate(final MinMaxBounds p_i47497_1_, final MinMaxBounds p_i47497_2_, @Nullable final Boolean p_i47497_3_, @Nullable final Boolean p_i47497_4_) {
            this.field_193465_a = p_i47497_1_;
            this.field_193466_b = p_i47497_2_;
            this.field_193467_c = p_i47497_3_;
            this.field_193468_d = p_i47497_4_;
        }
        
        public boolean func_193463_a(@Nullable final PotionEffect p_193463_1_) {
            return p_193463_1_ != null && this.field_193465_a.func_192514_a((float)p_193463_1_.getAmplifier()) && this.field_193466_b.func_192514_a((float)p_193463_1_.getDuration()) && (this.field_193467_c == null || this.field_193467_c == p_193463_1_.getIsAmbient()) && (this.field_193468_d == null || this.field_193468_d == p_193463_1_.doesShowParticles());
        }
        
        public static InstancePredicate func_193464_a(final JsonObject p_193464_0_) {
            final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_193464_0_.get("amplifier"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(p_193464_0_.get("duration"));
            final Boolean obool = p_193464_0_.has("ambient") ? Boolean.valueOf(JsonUtils.getBoolean(p_193464_0_, "ambient")) : null;
            final Boolean obool2 = p_193464_0_.has("visible") ? Boolean.valueOf(JsonUtils.getBoolean(p_193464_0_, "visible")) : null;
            return new InstancePredicate(minmaxbounds, minmaxbounds2, obool, obool2);
        }
    }
}
