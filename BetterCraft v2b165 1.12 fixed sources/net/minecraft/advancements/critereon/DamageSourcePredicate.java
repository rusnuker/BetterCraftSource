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

public class DamageSourcePredicate
{
    public static DamageSourcePredicate field_192449_a;
    private final Boolean field_192450_b;
    private final Boolean field_192451_c;
    private final Boolean field_192452_d;
    private final Boolean field_192453_e;
    private final Boolean field_192454_f;
    private final Boolean field_192455_g;
    private final Boolean field_192456_h;
    private final EntityPredicate field_193419_i;
    private final EntityPredicate field_193420_j;
    
    static {
        DamageSourcePredicate.field_192449_a = new DamageSourcePredicate();
    }
    
    public DamageSourcePredicate() {
        this.field_192450_b = null;
        this.field_192451_c = null;
        this.field_192452_d = null;
        this.field_192453_e = null;
        this.field_192454_f = null;
        this.field_192455_g = null;
        this.field_192456_h = null;
        this.field_193419_i = EntityPredicate.field_192483_a;
        this.field_193420_j = EntityPredicate.field_192483_a;
    }
    
    public DamageSourcePredicate(@Nullable final Boolean p_i47543_1_, @Nullable final Boolean p_i47543_2_, @Nullable final Boolean p_i47543_3_, @Nullable final Boolean p_i47543_4_, @Nullable final Boolean p_i47543_5_, @Nullable final Boolean p_i47543_6_, @Nullable final Boolean p_i47543_7_, final EntityPredicate p_i47543_8_, final EntityPredicate p_i47543_9_) {
        this.field_192450_b = p_i47543_1_;
        this.field_192451_c = p_i47543_2_;
        this.field_192452_d = p_i47543_3_;
        this.field_192453_e = p_i47543_4_;
        this.field_192454_f = p_i47543_5_;
        this.field_192455_g = p_i47543_6_;
        this.field_192456_h = p_i47543_7_;
        this.field_193419_i = p_i47543_8_;
        this.field_193420_j = p_i47543_9_;
    }
    
    public boolean func_193418_a(final EntityPlayerMP p_193418_1_, final DamageSource p_193418_2_) {
        return this == DamageSourcePredicate.field_192449_a || ((this.field_192450_b == null || this.field_192450_b == p_193418_2_.isProjectile()) && (this.field_192451_c == null || this.field_192451_c == p_193418_2_.isExplosion()) && (this.field_192452_d == null || this.field_192452_d == p_193418_2_.isUnblockable()) && (this.field_192453_e == null || this.field_192453_e == p_193418_2_.canHarmInCreative()) && (this.field_192454_f == null || this.field_192454_f == p_193418_2_.isDamageAbsolute()) && (this.field_192455_g == null || this.field_192455_g == p_193418_2_.isFireDamage()) && (this.field_192456_h == null || this.field_192456_h == p_193418_2_.isMagicDamage()) && this.field_193419_i.func_192482_a(p_193418_1_, p_193418_2_.getSourceOfDamage()) && this.field_193420_j.func_192482_a(p_193418_1_, p_193418_2_.getEntity()));
    }
    
    public static DamageSourcePredicate func_192447_a(@Nullable final JsonElement p_192447_0_) {
        if (p_192447_0_ != null && !p_192447_0_.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_192447_0_, "damage type");
            final Boolean obool = func_192448_a(jsonobject, "is_projectile");
            final Boolean obool2 = func_192448_a(jsonobject, "is_explosion");
            final Boolean obool3 = func_192448_a(jsonobject, "bypasses_armor");
            final Boolean obool4 = func_192448_a(jsonobject, "bypasses_invulnerability");
            final Boolean obool5 = func_192448_a(jsonobject, "bypasses_magic");
            final Boolean obool6 = func_192448_a(jsonobject, "is_fire");
            final Boolean obool7 = func_192448_a(jsonobject, "is_magic");
            final EntityPredicate entitypredicate = EntityPredicate.func_192481_a(jsonobject.get("direct_entity"));
            final EntityPredicate entitypredicate2 = EntityPredicate.func_192481_a(jsonobject.get("source_entity"));
            return new DamageSourcePredicate(obool, obool2, obool3, obool4, obool5, obool6, obool7, entitypredicate, entitypredicate2);
        }
        return DamageSourcePredicate.field_192449_a;
    }
    
    @Nullable
    private static Boolean func_192448_a(final JsonObject p_192448_0_, final String p_192448_1_) {
        return p_192448_0_.has(p_192448_1_) ? Boolean.valueOf(JsonUtils.getBoolean(p_192448_0_, p_192448_1_)) : null;
    }
}
