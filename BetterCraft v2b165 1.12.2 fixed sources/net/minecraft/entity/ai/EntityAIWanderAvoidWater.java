// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityCreature;

public class EntityAIWanderAvoidWater extends EntityAIWander
{
    protected final float field_190865_h;
    
    public EntityAIWanderAvoidWater(final EntityCreature p_i47301_1_, final double p_i47301_2_) {
        this(p_i47301_1_, p_i47301_2_, 0.001f);
    }
    
    public EntityAIWanderAvoidWater(final EntityCreature p_i47302_1_, final double p_i47302_2_, final float p_i47302_4_) {
        super(p_i47302_1_, p_i47302_2_);
        this.field_190865_h = p_i47302_4_;
    }
    
    @Nullable
    @Override
    protected Vec3d func_190864_f() {
        if (this.entity.isInWater()) {
            final Vec3d vec3d = RandomPositionGenerator.func_191377_b(this.entity, 15, 7);
            return (vec3d == null) ? super.func_190864_f() : vec3d;
        }
        return (this.entity.getRNG().nextFloat() >= this.field_190865_h) ? RandomPositionGenerator.func_191377_b(this.entity, 10, 7) : super.func_190864_f();
    }
}
