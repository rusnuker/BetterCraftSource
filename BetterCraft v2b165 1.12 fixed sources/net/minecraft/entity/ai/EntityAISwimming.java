// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.EntityLiving;

public class EntityAISwimming extends EntityAIBase
{
    private final EntityLiving theEntity;
    
    public EntityAISwimming(final EntityLiving entitylivingIn) {
        this.theEntity = entitylivingIn;
        this.setMutexBits(4);
        if (entitylivingIn.getNavigator() instanceof PathNavigateGround) {
            ((PathNavigateGround)entitylivingIn.getNavigator()).setCanSwim(true);
        }
        else if (entitylivingIn.getNavigator() instanceof PathNavigateFlying) {
            ((PathNavigateFlying)entitylivingIn.getNavigator()).func_192877_c(true);
        }
    }
    
    @Override
    public boolean shouldExecute() {
        return this.theEntity.isInWater() || this.theEntity.isInLava();
    }
    
    @Override
    public void updateTask() {
        if (this.theEntity.getRNG().nextFloat() < 0.8f) {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
