// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;

public final class ProjectileHelper
{
    public static RayTraceResult forwardsRaycast(final Entity p_188802_0_, final boolean includeEntities, final boolean p_188802_2_, final Entity excludedEntity) {
        final double d0 = p_188802_0_.posX;
        final double d2 = p_188802_0_.posY;
        final double d3 = p_188802_0_.posZ;
        final double d4 = p_188802_0_.motionX;
        final double d5 = p_188802_0_.motionY;
        final double d6 = p_188802_0_.motionZ;
        final World world = p_188802_0_.world;
        final Vec3d vec3d = new Vec3d(d0, d2, d3);
        Vec3d vec3d2 = new Vec3d(d0 + d4, d2 + d5, d3 + d6);
        RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d2, false, true, false);
        if (includeEntities) {
            if (raytraceresult != null) {
                vec3d2 = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
            }
            Entity entity = null;
            final List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(p_188802_0_, p_188802_0_.getEntityBoundingBox().addCoord(d4, d5, d6).expandXyz(1.0));
            double d7 = 0.0;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                if (entity2.canBeCollidedWith() && (p_188802_2_ || !entity2.isEntityEqual(excludedEntity)) && !entity2.noClip) {
                    final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expandXyz(0.30000001192092896);
                    final RayTraceResult raytraceresult2 = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                    if (raytraceresult2 != null) {
                        final double d8 = vec3d.squareDistanceTo(raytraceresult2.hitVec);
                        if (d8 < d7 || d7 == 0.0) {
                            entity = entity2;
                            d7 = d8;
                        }
                    }
                }
            }
            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }
        }
        return raytraceresult;
    }
    
    public static final void rotateTowardsMovement(final Entity p_188803_0_, final float p_188803_1_) {
        final double d0 = p_188803_0_.motionX;
        final double d2 = p_188803_0_.motionY;
        final double d3 = p_188803_0_.motionZ;
        final float f = MathHelper.sqrt(d0 * d0 + d3 * d3);
        p_188803_0_.rotationYaw = (float)(MathHelper.atan2(d3, d0) * 57.29577951308232) + 90.0f;
        p_188803_0_.rotationPitch = (float)(MathHelper.atan2(f, d2) * 57.29577951308232) - 90.0f;
        while (p_188803_0_.rotationPitch - p_188803_0_.prevRotationPitch < -180.0f) {
            p_188803_0_.prevRotationPitch -= 360.0f;
        }
        while (p_188803_0_.rotationPitch - p_188803_0_.prevRotationPitch >= 180.0f) {
            p_188803_0_.prevRotationPitch += 360.0f;
        }
        while (p_188803_0_.rotationYaw - p_188803_0_.prevRotationYaw < -180.0f) {
            p_188803_0_.prevRotationYaw -= 360.0f;
        }
        while (p_188803_0_.rotationYaw - p_188803_0_.prevRotationYaw >= 180.0f) {
            p_188803_0_.prevRotationYaw += 360.0f;
        }
        p_188803_0_.rotationPitch = p_188803_0_.prevRotationPitch + (p_188803_0_.rotationPitch - p_188803_0_.prevRotationPitch) * p_188803_1_;
        p_188803_0_.rotationYaw = p_188803_0_.prevRotationYaw + (p_188803_0_.rotationYaw - p_188803_0_.prevRotationYaw) * p_188803_1_;
    }
}
