// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import java.util.List;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import javax.annotation.Nullable;
import net.minecraft.pathfinding.PathNavigate;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;

public class EntityAIFollow extends EntityAIBase
{
    private final EntityLiving field_192372_a;
    private final Predicate<EntityLiving> field_192373_b;
    private EntityLiving field_192374_c;
    private final double field_192375_d;
    private final PathNavigate field_192376_e;
    private int field_192377_f;
    private final float field_192378_g;
    private float field_192379_h;
    private final float field_192380_i;
    
    public EntityAIFollow(final EntityLiving p_i47417_1_, final double p_i47417_2_, final float p_i47417_4_, final float p_i47417_5_) {
        this.field_192372_a = p_i47417_1_;
        this.field_192373_b = new Predicate<EntityLiving>() {
            @Override
            public boolean apply(@Nullable final EntityLiving p_apply_1_) {
                return p_apply_1_ != null && p_i47417_1_.getClass() != p_apply_1_.getClass();
            }
        };
        this.field_192375_d = p_i47417_2_;
        this.field_192376_e = p_i47417_1_.getNavigator();
        this.field_192378_g = p_i47417_4_;
        this.field_192380_i = p_i47417_5_;
        this.setMutexBits(3);
        if (!(p_i47417_1_.getNavigator() instanceof PathNavigateGround) && !(p_i47417_1_.getNavigator() instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        final List<EntityLiving> list = this.field_192372_a.world.getEntitiesWithinAABB((Class<? extends EntityLiving>)EntityLiving.class, this.field_192372_a.getEntityBoundingBox().expandXyz(this.field_192380_i), (Predicate<? super EntityLiving>)this.field_192373_b);
        if (!list.isEmpty()) {
            for (final EntityLiving entityliving : list) {
                if (!entityliving.isInvisible()) {
                    this.field_192374_c = entityliving;
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.field_192374_c != null && !this.field_192376_e.noPath() && this.field_192372_a.getDistanceSqToEntity(this.field_192374_c) > this.field_192378_g * this.field_192378_g;
    }
    
    @Override
    public void startExecuting() {
        this.field_192377_f = 0;
        this.field_192379_h = this.field_192372_a.getPathPriority(PathNodeType.WATER);
        this.field_192372_a.setPathPriority(PathNodeType.WATER, 0.0f);
    }
    
    @Override
    public void resetTask() {
        this.field_192374_c = null;
        this.field_192376_e.clearPathEntity();
        this.field_192372_a.setPathPriority(PathNodeType.WATER, this.field_192379_h);
    }
    
    @Override
    public void updateTask() {
        if (this.field_192374_c != null && !this.field_192372_a.getLeashed()) {
            this.field_192372_a.getLookHelper().setLookPositionWithEntity(this.field_192374_c, 10.0f, (float)this.field_192372_a.getVerticalFaceSpeed());
            if (--this.field_192377_f <= 0) {
                this.field_192377_f = 10;
                final double d0 = this.field_192372_a.posX - this.field_192374_c.posX;
                final double d2 = this.field_192372_a.posY - this.field_192374_c.posY;
                final double d3 = this.field_192372_a.posZ - this.field_192374_c.posZ;
                final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (d4 > this.field_192378_g * this.field_192378_g) {
                    this.field_192376_e.tryMoveToEntityLiving(this.field_192374_c, this.field_192375_d);
                }
                else {
                    this.field_192376_e.clearPathEntity();
                    final EntityLookHelper entitylookhelper = this.field_192374_c.getLookHelper();
                    if (d4 <= this.field_192378_g || (entitylookhelper.getLookPosX() == this.field_192372_a.posX && entitylookhelper.getLookPosY() == this.field_192372_a.posY && entitylookhelper.getLookPosZ() == this.field_192372_a.posZ)) {
                        final double d5 = this.field_192374_c.posX - this.field_192372_a.posX;
                        final double d6 = this.field_192374_c.posZ - this.field_192372_a.posZ;
                        this.field_192376_e.tryMoveToXYZ(this.field_192372_a.posX - d5, this.field_192372_a.posY, this.field_192372_a.posZ - d6, this.field_192375_d);
                    }
                }
            }
        }
    }
}
