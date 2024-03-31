// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.util.prediction;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.labymod.core.LabyModCore;
import net.minecraft.client.entity.EntityPlayerSP;

public class JumpPrediction
{
    public static boolean isJumpPredicted(final EntityPlayerSP player) {
        final float moveX = (float)(player.posX - player.prevPosX);
        final float moveZ = (float)(player.posZ - player.prevPosZ);
        if (player.onGround && !player.isSneaking() && !player.isRiding()) {
            final World world = LabyModCore.getMinecraft().getWorld();
            final JumpVec2f vec2f = new JumpVec2f(player.movementInput.moveStrafe, player.movementInput.moveForward);
            if (vec2f.x != 0.0f || vec2f.y != 0.0f) {
                final JumpVec3d vec3d = new JumpVec3d(player.posX, player.getEntityBoundingBox().minY, player.posZ);
                final double d0 = player.posX + moveX;
                final double d2 = player.posZ + moveZ;
                final JumpVec3d vec3d2 = new JumpVec3d(d0, player.getEntityBoundingBox().minY, d2);
                JumpVec3d vec3d3 = new JumpVec3d(moveX, 0.0, moveZ);
                final float f = player.getAIMoveSpeed();
                float f2 = (float)vec3d3.lengthSquared();
                if (f2 <= 0.001f) {
                    final float f3 = f * vec2f.x;
                    final float f4 = f * vec2f.y;
                    final float f5 = MathHelper.sin(player.rotationYaw * 0.017453292f);
                    final float f6 = MathHelper.cos(player.rotationYaw * 0.017453292f);
                    vec3d3 = new JumpVec3d(f3 * f6 - f4 * f5, vec3d3.y, f4 * f6 + f3 * f5);
                    f2 = (float)vec3d3.lengthSquared();
                    if (f2 <= 0.001f) {
                        return false;
                    }
                }
                final float f7 = (float)fastInvSqrt(f2);
                final JumpVec3d vec3d4 = scale(vec3d3, f7);
                final JumpVec3d vec3d5 = getForward(player);
                final float f8 = (float)(vec3d5.x * vec3d4.x + vec3d5.z * vec3d4.z);
                if (f8 >= -0.15f) {
                    BlockPos blockpos = new BlockPos(player.posX, player.getEntityBoundingBox().maxY, player.posZ);
                    final IBlockState iblockstate = world.getBlockState(blockpos);
                    if (iblockstate.getBlock().getCollisionBoundingBox(world, blockpos, iblockstate) == null) {
                        blockpos = blockpos.up();
                        final IBlockState iblockstate2 = world.getBlockState(blockpos);
                        if (iblockstate2.getBlock().getCollisionBoundingBox(world, blockpos, iblockstate2) == null) {
                            float f9 = 1.2f;
                            if (player.isPotionActive(Potion.jump)) {
                                f9 += (player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.75f;
                            }
                            final float f10 = Math.max(f * 7.0f, 1.0f / f7);
                            JumpVec3d vec3d6 = add(vec3d2, scale(vec3d4, f10));
                            final float f11 = player.width;
                            final float f12 = player.height;
                            final AxisAlignedBB axisalignedbb = axisAlignedBB(vec3d, addVector(vec3d6, 0.0, f12, 0.0)).expand(f11, 0.0, f11);
                            final JumpVec3d lvt_19_1_ = addVector(vec3d, 0.0, 0.5099999904632568, 0.0);
                            vec3d6 = addVector(vec3d6, 0.0, 0.5099999904632568, 0.0);
                            final JumpVec3d vec3d7 = crossProduct(vec3d4, new JumpVec3d(0.0, 1.0, 0.0));
                            final JumpVec3d vec3d8 = scale(vec3d7, f11 * 0.5f);
                            final JumpVec3d vec3d9 = subtract(lvt_19_1_, vec3d8);
                            final JumpVec3d vec3d10 = subtract(vec3d6, vec3d8);
                            final JumpVec3d vec3d11 = add(lvt_19_1_, vec3d8);
                            final JumpVec3d vec3d12 = add(vec3d6, vec3d8);
                            final List<AxisAlignedBB> list = world.getCollidingBoundingBoxes(player, axisalignedbb);
                            list.isEmpty();
                            float f13 = Float.MIN_VALUE;
                            for (final AxisAlignedBB axisalignedbb2 : list) {
                                if (intersects(axisalignedbb2, vec3d9, vec3d10) || intersects(axisalignedbb2, vec3d11, vec3d12)) {
                                    f13 = (float)axisalignedbb2.maxY;
                                    final JumpVec3d vec3d13 = getCenter(axisalignedbb2);
                                    final BlockPos blockpos2 = new BlockPos(vec3d13.x, vec3d13.y, vec3d13.z);
                                    for (int i = 1; i < f9; ++i) {
                                        final BlockPos blockpos3 = blockpos2.up(i);
                                        final IBlockState iblockstate3 = world.getBlockState(blockpos3);
                                        final AxisAlignedBB axisalignedbb3;
                                        if ((axisalignedbb3 = iblockstate3.getBlock().getCollisionBoundingBox(world, blockpos3, iblockstate3)) != null) {
                                            f13 = (float)axisalignedbb3.maxY + blockpos3.getY();
                                            if (f13 - player.getEntityBoundingBox().minY > f9) {
                                                return false;
                                            }
                                        }
                                        if (i > 1) {
                                            blockpos = blockpos.up();
                                            final IBlockState iblockstate4 = world.getBlockState(blockpos);
                                            if (iblockstate4.getBlock().getCollisionBoundingBox(world, blockpos, iblockstate4) != null) {
                                                return false;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            if (f13 != Float.MIN_VALUE) {
                                final float f14 = (float)(f13 - player.getEntityBoundingBox().minY);
                                if (f14 > 0.5f && f14 <= f9) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static JumpVec3d getCenter(final AxisAlignedBB ax) {
        return new JumpVec3d(ax.minX + (ax.maxX - ax.minX) * 0.5, ax.minY + (ax.maxY - ax.minY) * 0.5, ax.minZ + (ax.maxZ - ax.minZ) * 0.5);
    }
    
    public static boolean intersects(final AxisAlignedBB ax, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        return ax.minX < x2 && ax.maxX > x1 && ax.minY < y2 && ax.maxY > y1 && ax.minZ < z2 && ax.maxZ > z1;
    }
    
    public static boolean intersects(final AxisAlignedBB ax, final JumpVec3d min, final JumpVec3d max) {
        return intersects(ax, Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z), Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));
    }
    
    public static JumpVec3d subtract(final JumpVec3d vec, final JumpVec3d vec2) {
        return subtract(vec2, vec.x, vec.y, vec.z);
    }
    
    public static JumpVec3d subtract(final JumpVec3d vec, final double x, final double y, final double z) {
        return addVector(vec, -x, -y, -z);
    }
    
    public static JumpVec3d crossProduct(final JumpVec3d vec, final JumpVec3d vec2) {
        return new JumpVec3d(vec2.y * vec.z - vec2.z * vec.y, vec2.z * vec.x - vec2.x * vec.z, vec2.x * vec.y - vec2.y * vec.x);
    }
    
    private static AxisAlignedBB axisAlignedBB(final JumpVec3d a, final JumpVec3d b) {
        return new AxisAlignedBB(a.x, a.y, a.z, b.x, b.y, b.z);
    }
    
    public static JumpVec3d add(final JumpVec3d vec1, final JumpVec3d vec) {
        return addVector(vec1, vec.x, vec.y, vec.z);
    }
    
    public static JumpVec3d addVector(final JumpVec3d vec, final double x, final double y, final double z) {
        return new JumpVec3d(vec.x + x, vec.y + y, vec.z + z);
    }
    
    public static JumpVec2f getPitchYaw(final EntityPlayerSP player) {
        return new JumpVec2f(player.rotationPitch, player.rotationYaw);
    }
    
    public static JumpVec3d getForward(final EntityPlayerSP player) {
        return fromPitchYawVector(getPitchYaw(player));
    }
    
    public static JumpVec3d fromPitchYawVector(final JumpVec2f p_189984_0_) {
        return fromPitchYaw(p_189984_0_.x, p_189984_0_.y);
    }
    
    public static JumpVec3d fromPitchYaw(final float p_189986_0_, final float p_189986_1_) {
        final float f = MathHelper.cos(-p_189986_1_ * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-p_189986_1_ * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-p_189986_0_ * 0.017453292f);
        final float f4 = MathHelper.sin(-p_189986_0_ * 0.017453292f);
        return new JumpVec3d(f2 * f3, f4, f * f3);
    }
    
    public static JumpVec3d scale(final JumpVec3d vec, final double factor) {
        return new JumpVec3d(vec.x * factor, vec.y * factor, vec.z * factor);
    }
    
    public static double fastInvSqrt(double p_181161_0_) {
        final double d0 = 0.5 * p_181161_0_;
        long i = Double.doubleToRawLongBits(p_181161_0_);
        i = 6910469410427058090L - (i >> 1);
        p_181161_0_ = Double.longBitsToDouble(i);
        p_181161_0_ *= 1.5 - d0 * p_181161_0_ * p_181161_0_;
        return p_181161_0_;
    }
}
