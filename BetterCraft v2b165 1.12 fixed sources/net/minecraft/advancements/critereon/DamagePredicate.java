// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayerMP;
import javax.annotation.Nullable;

public class DamagePredicate
{
    public static DamagePredicate field_192366_a;
    private final MinMaxBounds field_192367_b;
    private final MinMaxBounds field_192368_c;
    private final EntityPredicate field_192369_d;
    private final Boolean field_192370_e;
    private final DamageSourcePredicate field_192371_f;
    
    static {
        DamagePredicate.field_192366_a = new DamagePredicate();
    }
    
    public DamagePredicate() {
        this.field_192367_b = MinMaxBounds.field_192516_a;
        this.field_192368_c = MinMaxBounds.field_192516_a;
        this.field_192369_d = EntityPredicate.field_192483_a;
        this.field_192370_e = null;
        this.field_192371_f = DamageSourcePredicate.field_192449_a;
    }
    
    public DamagePredicate(final MinMaxBounds p_i47464_1_, final MinMaxBounds p_i47464_2_, final EntityPredicate p_i47464_3_, @Nullable final Boolean p_i47464_4_, final DamageSourcePredicate p_i47464_5_) {
        this.field_192367_b = p_i47464_1_;
        this.field_192368_c = p_i47464_2_;
        this.field_192369_d = p_i47464_3_;
        this.field_192370_e = p_i47464_4_;
        this.field_192371_f = p_i47464_5_;
    }
    
    public boolean func_192365_a(final EntityPlayerMP p_192365_1_, final DamageSource p_192365_2_, final float p_192365_3_, final float p_192365_4_, final boolean p_192365_5_) {
        return this == DamagePredicate.field_192366_a || (this.field_192367_b.func_192514_a(p_192365_3_) && this.field_192368_c.func_192514_a(p_192365_4_) && this.field_192369_d.func_192482_a(p_192365_1_, p_192365_2_.getEntity()) && (this.field_192370_e == null || this.field_192370_e == p_192365_5_) && this.field_192371_f.func_193418_a(p_192365_1_, p_192365_2_));
    }
    
    public static DamagePredicate func_192364_a(@Nullable final JsonElement p_192364_0_) {
        if (p_192364_0_ != null && !p_192364_0_.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_192364_0_, "damage");
            final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("dealt"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(jsonobject.get("taken"));
            final Boolean obool = jsonobject.has("blocked") ? Boolean.valueOf(JsonUtils.getBoolean(jsonobject, "blocked")) : null;
            final EntityPredicate entitypredicate = EntityPredicate.func_192481_a(jsonobject.get("source_entity"));
            final DamageSourcePredicate damagesourcepredicate = DamageSourcePredicate.func_192447_a(jsonobject.get("type"));
            return new DamagePredicate(minmaxbounds, minmaxbounds2, entitypredicate, obool, damagesourcepredicate);
        }
        return DamagePredicate.field_192366_a;
    }
}
