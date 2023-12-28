// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EntitySelectors;
import javax.annotation.Nullable;
import com.google.common.base.Predicates;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.Path;
import net.minecraft.entity.EntityCreature;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;
    protected EntityCreature theEntity;
    private final double farSpeed;
    private final double nearSpeed;
    protected T closestLivingEntity;
    private final float avoidDistance;
    private Path entityPathEntity;
    private final PathNavigate entityPathNavigate;
    private final Class<T> classToAvoid;
    private final Predicate<? super T> avoidTargetSelector;
    
    public EntityAIAvoidEntity(final EntityCreature theEntityIn, final Class<T> classToAvoidIn, final float avoidDistanceIn, final double farSpeedIn, final double nearSpeedIn) {
        this(theEntityIn, classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }
    
    public EntityAIAvoidEntity(final EntityCreature theEntityIn, final Class<T> classToAvoidIn, final Predicate<? super T> avoidTargetSelectorIn, final float avoidDistanceIn, final double farSpeedIn, final double nearSpeedIn) {
        this.canBeSeenSelector = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive() && EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(p_apply_1_) && !EntityAIAvoidEntity.this.theEntity.isOnSameTeam(p_apply_1_);
            }
        };
        this.theEntity = theEntityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.entityPathNavigate = theEntityIn.getNavigator();
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final List<T> list = this.theEntity.world.getEntitiesWithinAABB((Class<? extends T>)this.classToAvoid, this.theEntity.getEntityBoundingBox().expand(this.avoidDistance, 3.0, this.avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector));
        if (list.isEmpty()) {
            return false;
        }
        this.closestLivingEntity = list.get(0);
        final Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
        if (vec3d == null) {
            return false;
        }
        if (this.closestLivingEntity.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity)) {
            return false;
        }
        this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
        return this.entityPathEntity != null;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.entityPathNavigate.noPath();
    }
    
    @Override
    public void startExecuting() {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }
    
    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
    }
    
    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0) {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        }
        else {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}
