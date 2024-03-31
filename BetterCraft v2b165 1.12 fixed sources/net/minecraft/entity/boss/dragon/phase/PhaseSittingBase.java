// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;

public abstract class PhaseSittingBase extends PhaseBase
{
    public PhaseSittingBase(final EntityDragon p_i46794_1_) {
        super(p_i46794_1_);
    }
    
    @Override
    public boolean getIsStationary() {
        return true;
    }
    
    @Override
    public float getAdjustedDamage(final MultiPartEntityPart pt, final DamageSource src, final float damage) {
        if (src.getSourceOfDamage() instanceof EntityArrow) {
            src.getSourceOfDamage().setFire(1);
            return 0.0f;
        }
        return super.getAdjustedDamage(pt, src, damage);
    }
}
