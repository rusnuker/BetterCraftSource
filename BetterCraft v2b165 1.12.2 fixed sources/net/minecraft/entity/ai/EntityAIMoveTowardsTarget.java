// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveTowardsTarget extends EntityAIBase
{
    private final EntityCreature theEntity;
    private EntityLivingBase targetEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double speed;
    private final float maxTargetDistance;
    
    public EntityAIMoveTowardsTarget(final EntityCreature creature, final double speedIn, final float targetMaxDistance) {
        this.theEntity = creature;
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        this.targetEntity = this.theEntity.getAttackTarget();
        if (this.targetEntity == null) {
            return false;
        }
        if (this.targetEntity.getDistanceSqToEntity(this.theEntity) > this.maxTargetDistance * this.maxTargetDistance) {
            return false;
        }
        final Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3d(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
        if (vec3d == null) {
            return false;
        }
        this.movePosX = vec3d.xCoord;
        this.movePosY = vec3d.yCoord;
        this.movePosZ = vec3d.zCoord;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSqToEntity(this.theEntity) < this.maxTargetDistance * this.maxTargetDistance;
    }
    
    @Override
    public void resetTask() {
        this.targetEntity = null;
    }
    
    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
    }
}
