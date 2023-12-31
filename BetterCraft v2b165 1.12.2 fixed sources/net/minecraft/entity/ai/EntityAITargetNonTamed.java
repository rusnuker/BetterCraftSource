// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import com.google.common.base.Predicate;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.EntityLivingBase;

public class EntityAITargetNonTamed<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
{
    private final EntityTameable theTameable;
    
    public EntityAITargetNonTamed(final EntityTameable entityIn, final Class<T> classTarget, final boolean checkSight, final Predicate<? super T> targetSelector) {
        super(entityIn, classTarget, 10, checkSight, false, targetSelector);
        this.theTameable = entityIn;
    }
    
    @Override
    public boolean shouldExecute() {
        return !this.theTameable.isTamed() && super.shouldExecute();
    }
}
