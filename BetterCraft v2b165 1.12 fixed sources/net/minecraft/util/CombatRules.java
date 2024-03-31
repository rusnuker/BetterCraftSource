// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import net.minecraft.util.math.MathHelper;

public class CombatRules
{
    public static float getDamageAfterAbsorb(final float damage, final float totalArmor, final float toughnessAttribute) {
        final float f = 2.0f + toughnessAttribute / 4.0f;
        final float f2 = MathHelper.clamp(totalArmor - damage / f, totalArmor * 0.2f, 20.0f);
        return damage * (1.0f - f2 / 25.0f);
    }
    
    public static float getDamageAfterMagicAbsorb(final float p_188401_0_, final float p_188401_1_) {
        final float f = MathHelper.clamp(p_188401_1_, 0.0f, 20.0f);
        return p_188401_0_ * (1.0f - f / 25.0f);
    }
}
