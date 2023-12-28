// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityLlama;

public class EntityAILlamaFollowCaravan extends EntityAIBase
{
    public EntityLlama field_190859_a;
    private double field_190860_b;
    private int field_190861_c;
    
    public EntityAILlamaFollowCaravan(final EntityLlama p_i47305_1_, final double p_i47305_2_) {
        this.field_190859_a = p_i47305_1_;
        this.field_190860_b = p_i47305_2_;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.field_190859_a.getLeashed() || this.field_190859_a.func_190718_dR()) {
            return false;
        }
        final List<EntityLlama> list = this.field_190859_a.world.getEntitiesWithinAABB(this.field_190859_a.getClass(), this.field_190859_a.getEntityBoundingBox().expand(9.0, 4.0, 9.0));
        EntityLlama entityllama = null;
        double d0 = Double.MAX_VALUE;
        for (final EntityLlama entityllama2 : list) {
            if (entityllama2.func_190718_dR() && !entityllama2.func_190712_dQ()) {
                final double d2 = this.field_190859_a.getDistanceSqToEntity(entityllama2);
                if (d2 > d0) {
                    continue;
                }
                d0 = d2;
                entityllama = entityllama2;
            }
        }
        if (entityllama == null) {
            for (final EntityLlama entityllama3 : list) {
                if (entityllama3.getLeashed() && !entityllama3.func_190712_dQ()) {
                    final double d3 = this.field_190859_a.getDistanceSqToEntity(entityllama3);
                    if (d3 > d0) {
                        continue;
                    }
                    d0 = d3;
                    entityllama = entityllama3;
                }
            }
        }
        if (entityllama == null) {
            return false;
        }
        if (d0 < 4.0) {
            return false;
        }
        if (!entityllama.getLeashed() && !this.func_190858_a(entityllama, 1)) {
            return false;
        }
        this.field_190859_a.func_190715_a(entityllama);
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        if (this.field_190859_a.func_190718_dR() && this.field_190859_a.func_190716_dS().isEntityAlive() && this.func_190858_a(this.field_190859_a, 0)) {
            final double d0 = this.field_190859_a.getDistanceSqToEntity(this.field_190859_a.func_190716_dS());
            if (d0 > 676.0) {
                if (this.field_190860_b <= 3.0) {
                    this.field_190860_b *= 1.2;
                    this.field_190861_c = 40;
                    return true;
                }
                if (this.field_190861_c == 0) {
                    return false;
                }
            }
            if (this.field_190861_c > 0) {
                --this.field_190861_c;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void resetTask() {
        this.field_190859_a.func_190709_dP();
        this.field_190860_b = 2.1;
    }
    
    @Override
    public void updateTask() {
        if (this.field_190859_a.func_190718_dR()) {
            final EntityLlama entityllama = this.field_190859_a.func_190716_dS();
            final double d0 = this.field_190859_a.getDistanceToEntity(entityllama);
            final float f = 2.0f;
            final Vec3d vec3d = new Vec3d(entityllama.posX - this.field_190859_a.posX, entityllama.posY - this.field_190859_a.posY, entityllama.posZ - this.field_190859_a.posZ).normalize().scale(Math.max(d0 - 2.0, 0.0));
            this.field_190859_a.getNavigator().tryMoveToXYZ(this.field_190859_a.posX + vec3d.xCoord, this.field_190859_a.posY + vec3d.yCoord, this.field_190859_a.posZ + vec3d.zCoord, this.field_190860_b);
        }
    }
    
    private boolean func_190858_a(final EntityLlama p_190858_1_, int p_190858_2_) {
        if (p_190858_2_ > 8) {
            return false;
        }
        if (!p_190858_1_.func_190718_dR()) {
            return false;
        }
        if (p_190858_1_.func_190716_dS().getLeashed()) {
            return true;
        }
        final EntityLlama entityllama = p_190858_1_.func_190716_dS();
        ++p_190858_2_;
        return this.func_190858_a(entityllama, p_190858_2_);
    }
}
