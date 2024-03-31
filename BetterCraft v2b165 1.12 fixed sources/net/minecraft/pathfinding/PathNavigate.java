// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ChunkCache;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public abstract class PathNavigate
{
    protected EntityLiving theEntity;
    protected World worldObj;
    @Nullable
    protected Path currentPath;
    protected double speed;
    private final IAttributeInstance pathSearchRange;
    protected int totalTicks;
    private int ticksAtLastPos;
    private Vec3d lastPosCheck;
    private Vec3d timeoutCachedNode;
    private long timeoutTimer;
    private long lastTimeoutCheck;
    private double timeoutLimit;
    protected float maxDistanceToWaypoint;
    protected boolean tryUpdatePath;
    private long lastTimeUpdated;
    protected NodeProcessor nodeProcessor;
    private BlockPos targetPos;
    private final PathFinder pathFinder;
    
    public PathNavigate(final EntityLiving entitylivingIn, final World worldIn) {
        this.lastPosCheck = Vec3d.ZERO;
        this.timeoutCachedNode = Vec3d.ZERO;
        this.maxDistanceToWaypoint = 0.5f;
        this.theEntity = entitylivingIn;
        this.worldObj = worldIn;
        this.pathSearchRange = entitylivingIn.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        this.pathFinder = this.getPathFinder();
    }
    
    protected abstract PathFinder getPathFinder();
    
    public void setSpeed(final double speedIn) {
        this.speed = speedIn;
    }
    
    public float getPathSearchRange() {
        return (float)this.pathSearchRange.getAttributeValue();
    }
    
    public boolean canUpdatePathOnTimeout() {
        return this.tryUpdatePath;
    }
    
    public void updatePath() {
        if (this.worldObj.getTotalWorldTime() - this.lastTimeUpdated > 20L) {
            if (this.targetPos != null) {
                this.currentPath = null;
                this.currentPath = this.getPathToPos(this.targetPos);
                this.lastTimeUpdated = this.worldObj.getTotalWorldTime();
                this.tryUpdatePath = false;
            }
        }
        else {
            this.tryUpdatePath = true;
        }
    }
    
    @Nullable
    public final Path getPathToXYZ(final double x, final double y, final double z) {
        return this.getPathToPos(new BlockPos(x, y, z));
    }
    
    @Nullable
    public Path getPathToPos(final BlockPos pos) {
        if (!this.canNavigate()) {
            return null;
        }
        if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals(this.targetPos)) {
            return this.currentPath;
        }
        this.targetPos = pos;
        final float f = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        final BlockPos blockpos = new BlockPos(this.theEntity);
        final int i = (int)(f + 8.0f);
        final ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
        final Path path = this.pathFinder.findPath(chunkcache, this.theEntity, this.targetPos, f);
        this.worldObj.theProfiler.endSection();
        return path;
    }
    
    @Nullable
    public Path getPathToEntityLiving(final Entity entityIn) {
        if (!this.canNavigate()) {
            return null;
        }
        final BlockPos blockpos = new BlockPos(entityIn);
        if (this.currentPath != null && !this.currentPath.isFinished() && blockpos.equals(this.targetPos)) {
            return this.currentPath;
        }
        this.targetPos = blockpos;
        final float f = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        final BlockPos blockpos2 = new BlockPos(this.theEntity).up();
        final int i = (int)(f + 16.0f);
        final ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos2.add(-i, -i, -i), blockpos2.add(i, i, i), 0);
        final Path path = this.pathFinder.findPath(chunkcache, this.theEntity, entityIn, f);
        this.worldObj.theProfiler.endSection();
        return path;
    }
    
    public boolean tryMoveToXYZ(final double x, final double y, final double z, final double speedIn) {
        return this.setPath(this.getPathToXYZ(x, y, z), speedIn);
    }
    
    public boolean tryMoveToEntityLiving(final Entity entityIn, final double speedIn) {
        final Path path = this.getPathToEntityLiving(entityIn);
        return path != null && this.setPath(path, speedIn);
    }
    
    public boolean setPath(@Nullable final Path pathentityIn, final double speedIn) {
        if (pathentityIn == null) {
            this.currentPath = null;
            return false;
        }
        if (!pathentityIn.isSamePath(this.currentPath)) {
            this.currentPath = pathentityIn;
        }
        this.removeSunnyPath();
        if (this.currentPath.getCurrentPathLength() <= 0) {
            return false;
        }
        this.speed = speedIn;
        final Vec3d vec3d = this.getEntityPosition();
        this.ticksAtLastPos = this.totalTicks;
        this.lastPosCheck = vec3d;
        return true;
    }
    
    @Nullable
    public Path getPath() {
        return this.currentPath;
    }
    
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
                final Vec3d vec3d = this.getEntityPosition();
                final Vec3d vec3d2 = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
                if (vec3d.yCoord > vec3d2.yCoord && !this.theEntity.onGround && MathHelper.floor(vec3d.xCoord) == MathHelper.floor(vec3d2.xCoord) && MathHelper.floor(vec3d.zCoord) == MathHelper.floor(vec3d2.zCoord)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            this.func_192876_m();
            if (!this.noPath()) {
                Vec3d vec3d3 = this.currentPath.getPosition(this.theEntity);
                final BlockPos blockpos = new BlockPos(vec3d3).down();
                final AxisAlignedBB axisalignedbb = this.worldObj.getBlockState(blockpos).getBoundingBox(this.worldObj, blockpos);
                vec3d3 = vec3d3.subtract(0.0, 1.0 - axisalignedbb.maxY, 0.0);
                this.theEntity.getMoveHelper().setMoveTo(vec3d3.xCoord, vec3d3.yCoord, vec3d3.zCoord, this.speed);
            }
        }
    }
    
    protected void func_192876_m() {
    }
    
    protected void pathFollow() {
        final Vec3d vec3d = this.getEntityPosition();
        int i = this.currentPath.getCurrentPathLength();
        for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j) {
            if (this.currentPath.getPathPointFromIndex(j).yCoord != Math.floor(vec3d.yCoord)) {
                i = j;
                break;
            }
        }
        this.maxDistanceToWaypoint = ((this.theEntity.width > 0.75f) ? (this.theEntity.width / 2.0f) : (0.75f - this.theEntity.width / 2.0f));
        final Vec3d vec3d2 = this.currentPath.getCurrentPos();
        if (MathHelper.abs((float)(this.theEntity.posX - (vec3d2.xCoord + 0.5))) < this.maxDistanceToWaypoint && MathHelper.abs((float)(this.theEntity.posZ - (vec3d2.zCoord + 0.5))) < this.maxDistanceToWaypoint && Math.abs(this.theEntity.posY - vec3d2.yCoord) < 1.0) {
            this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
        }
        final int k = MathHelper.ceil(this.theEntity.width);
        final int l = MathHelper.ceil(this.theEntity.height);
        final int i2 = k;
        for (int j2 = i - 1; j2 >= this.currentPath.getCurrentPathIndex(); --j2) {
            if (this.isDirectPathBetweenPoints(vec3d, this.currentPath.getVectorFromIndex(this.theEntity, j2), k, l, i2)) {
                this.currentPath.setCurrentPathIndex(j2);
                break;
            }
        }
        this.checkForStuck(vec3d);
    }
    
    protected void checkForStuck(final Vec3d positionVec3) {
        if (this.totalTicks - this.ticksAtLastPos > 100) {
            if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25) {
                this.clearPathEntity();
            }
            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = positionVec3;
        }
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            final Vec3d vec3d = this.currentPath.getCurrentPos();
            if (vec3d.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += System.currentTimeMillis() - this.lastTimeoutCheck;
            }
            else {
                this.timeoutCachedNode = vec3d;
                final double d0 = positionVec3.distanceTo(this.timeoutCachedNode);
                this.timeoutLimit = ((this.theEntity.getAIMoveSpeed() > 0.0f) ? (d0 / this.theEntity.getAIMoveSpeed() * 1000.0) : 0.0);
            }
            if (this.timeoutLimit > 0.0 && this.timeoutTimer > this.timeoutLimit * 3.0) {
                this.timeoutCachedNode = Vec3d.ZERO;
                this.timeoutTimer = 0L;
                this.timeoutLimit = 0.0;
                this.clearPathEntity();
            }
            this.lastTimeoutCheck = System.currentTimeMillis();
        }
    }
    
    public boolean noPath() {
        return this.currentPath == null || this.currentPath.isFinished();
    }
    
    public void clearPathEntity() {
        this.currentPath = null;
    }
    
    protected abstract Vec3d getEntityPosition();
    
    protected abstract boolean canNavigate();
    
    protected boolean isInLiquid() {
        return this.theEntity.isInWater() || this.theEntity.isInLava();
    }
    
    protected void removeSunnyPath() {
        if (this.currentPath != null) {
            for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
                final PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
                final PathPoint pathpoint2 = (i + 1 < this.currentPath.getCurrentPathLength()) ? this.currentPath.getPathPointFromIndex(i + 1) : null;
                final IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord));
                final Block block = iblockstate.getBlock();
                if (block == Blocks.CAULDRON) {
                    this.currentPath.setPoint(i, pathpoint.cloneMove(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord));
                    if (pathpoint2 != null && pathpoint.yCoord >= pathpoint2.yCoord) {
                        this.currentPath.setPoint(i + 1, pathpoint2.cloneMove(pathpoint2.xCoord, pathpoint.yCoord + 1, pathpoint2.zCoord));
                    }
                }
            }
        }
    }
    
    protected abstract boolean isDirectPathBetweenPoints(final Vec3d p0, final Vec3d p1, final int p2, final int p3, final int p4);
    
    public boolean canEntityStandOnPos(final BlockPos pos) {
        return this.worldObj.getBlockState(pos.down()).isFullBlock();
    }
    
    public NodeProcessor getNodeProcessor() {
        return this.nodeProcessor;
    }
}
