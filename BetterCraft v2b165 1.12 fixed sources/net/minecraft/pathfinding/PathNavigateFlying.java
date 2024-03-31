// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.pathfinding;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public class PathNavigateFlying extends PathNavigate
{
    public PathNavigateFlying(final EntityLiving p_i47412_1_, final World p_i47412_2_) {
        super(p_i47412_1_, p_i47412_2_);
    }
    
    @Override
    protected PathFinder getPathFinder() {
        (this.nodeProcessor = new FlyingNodeProcessor()).setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }
    
    @Override
    protected boolean canNavigate() {
        return (this.func_192880_g() && this.isInLiquid()) || !this.theEntity.isRiding();
    }
    
    @Override
    protected Vec3d getEntityPosition() {
        return new Vec3d(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
    }
    
    @Override
    public Path getPathToEntityLiving(final Entity entityIn) {
        return this.getPathToPos(new BlockPos(entityIn));
    }
    
    @Override
    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (this.tryUpdatePath) {
            this.updatePath();
        }
        if (!this.noPath()) {
            if (this.canNavigate()) {
                this.pathFollow();
            }
            else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                final Vec3d vec3d = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
                if (MathHelper.floor(this.theEntity.posX) == MathHelper.floor(vec3d.xCoord) && MathHelper.floor(this.theEntity.posY) == MathHelper.floor(vec3d.yCoord) && MathHelper.floor(this.theEntity.posZ) == MathHelper.floor(vec3d.zCoord)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            this.func_192876_m();
            if (!this.noPath()) {
                final Vec3d vec3d2 = this.currentPath.getPosition(this.theEntity);
                this.theEntity.getMoveHelper().setMoveTo(vec3d2.xCoord, vec3d2.yCoord, vec3d2.zCoord, this.speed);
            }
        }
    }
    
    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3d posVec31, final Vec3d posVec32, final int sizeX, final int sizeY, final int sizeZ) {
        int i = MathHelper.floor(posVec31.xCoord);
        int j = MathHelper.floor(posVec31.yCoord);
        int k = MathHelper.floor(posVec31.zCoord);
        double d0 = posVec32.xCoord - posVec31.xCoord;
        double d2 = posVec32.yCoord - posVec31.yCoord;
        double d3 = posVec32.zCoord - posVec31.zCoord;
        final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        if (d4 < 1.0E-8) {
            return false;
        }
        final double d5 = 1.0 / Math.sqrt(d4);
        d0 *= d5;
        d2 *= d5;
        d3 *= d5;
        final double d6 = 1.0 / Math.abs(d0);
        final double d7 = 1.0 / Math.abs(d2);
        final double d8 = 1.0 / Math.abs(d3);
        double d9 = i - posVec31.xCoord;
        double d10 = j - posVec31.yCoord;
        double d11 = k - posVec31.zCoord;
        if (d0 >= 0.0) {
            ++d9;
        }
        if (d2 >= 0.0) {
            ++d10;
        }
        if (d3 >= 0.0) {
            ++d11;
        }
        d9 /= d0;
        d10 /= d2;
        d11 /= d3;
        final int l = (d0 < 0.0) ? -1 : 1;
        final int i2 = (d2 < 0.0) ? -1 : 1;
        final int j2 = (d3 < 0.0) ? -1 : 1;
        final int k2 = MathHelper.floor(posVec32.xCoord);
        final int l2 = MathHelper.floor(posVec32.yCoord);
        final int i3 = MathHelper.floor(posVec32.zCoord);
        int j3 = k2 - i;
        int k3 = l2 - j;
        int l3 = i3 - k;
        while (j3 * l > 0 || k3 * i2 > 0 || l3 * j2 > 0) {
            if (d9 < d11 && d9 <= d10) {
                d9 += d6;
                i += l;
                j3 = k2 - i;
            }
            else if (d10 < d9 && d10 <= d11) {
                d10 += d7;
                j += i2;
                k3 = l2 - j;
            }
            else {
                d11 += d8;
                k += j2;
                l3 = i3 - k;
            }
        }
        return true;
    }
    
    public void func_192879_a(final boolean p_192879_1_) {
        this.nodeProcessor.setCanBreakDoors(p_192879_1_);
    }
    
    public void func_192878_b(final boolean p_192878_1_) {
        this.nodeProcessor.setCanEnterDoors(p_192878_1_);
    }
    
    public void func_192877_c(final boolean p_192877_1_) {
        this.nodeProcessor.setCanSwim(p_192877_1_);
    }
    
    public boolean func_192880_g() {
        return this.nodeProcessor.getCanSwim();
    }
    
    @Override
    public boolean canEntityStandOnPos(final BlockPos pos) {
        return this.worldObj.getBlockState(pos).isFullyOpaque();
    }
}
