// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIPlay extends EntityAIBase
{
    private final EntityVillager villagerObj;
    private EntityLivingBase targetVillager;
    private final double speed;
    private int playTime;
    
    public EntityAIPlay(final EntityVillager villagerObjIn, final double speedIn) {
        this.villagerObj = villagerObjIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.villagerObj.getGrowingAge() >= 0) {
            return false;
        }
        if (this.villagerObj.getRNG().nextInt(400) != 0) {
            return false;
        }
        final List<EntityVillager> list = this.villagerObj.world.getEntitiesWithinAABB((Class<? extends EntityVillager>)EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(6.0, 3.0, 6.0));
        double d0 = Double.MAX_VALUE;
        for (final EntityVillager entityvillager : list) {
            if (entityvillager != this.villagerObj && !entityvillager.isPlaying() && entityvillager.getGrowingAge() < 0) {
                final double d2 = entityvillager.getDistanceSqToEntity(this.villagerObj);
                if (d2 > d0) {
                    continue;
                }
                d0 = d2;
                this.targetVillager = entityvillager;
            }
        }
        if (this.targetVillager == null) {
            final Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
            if (vec3d == null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.playTime > 0;
    }
    
    @Override
    public void startExecuting() {
        if (this.targetVillager != null) {
            this.villagerObj.setPlaying(true);
        }
        this.playTime = 1000;
    }
    
    @Override
    public void resetTask() {
        this.villagerObj.setPlaying(false);
        this.targetVillager = null;
    }
    
    @Override
    public void updateTask() {
        --this.playTime;
        if (this.targetVillager != null) {
            if (this.villagerObj.getDistanceSqToEntity(this.targetVillager) > 4.0) {
                this.villagerObj.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
            }
        }
        else if (this.villagerObj.getNavigator().noPath()) {
            final Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
            if (vec3d == null) {
                return;
            }
            this.villagerObj.getNavigator().tryMoveToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, this.speed);
        }
    }
}
