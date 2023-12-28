// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.pathfinding;

import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.IBlockAccess;
import com.google.common.collect.Sets;
import java.util.Set;

public class PathFinder
{
    private final PathHeap path;
    private final Set<PathPoint> closedSet;
    private final PathPoint[] pathOptions;
    private final NodeProcessor nodeProcessor;
    
    public PathFinder(final NodeProcessor processor) {
        this.path = new PathHeap();
        this.closedSet = (Set<PathPoint>)Sets.newHashSet();
        this.pathOptions = new PathPoint[32];
        this.nodeProcessor = processor;
    }
    
    @Nullable
    public Path findPath(final IBlockAccess worldIn, final EntityLiving p_186333_2_, final Entity p_186333_3_, final float p_186333_4_) {
        return this.findPath(worldIn, p_186333_2_, p_186333_3_.posX, p_186333_3_.getEntityBoundingBox().minY, p_186333_3_.posZ, p_186333_4_);
    }
    
    @Nullable
    public Path findPath(final IBlockAccess worldIn, final EntityLiving p_186336_2_, final BlockPos p_186336_3_, final float p_186336_4_) {
        return this.findPath(worldIn, p_186336_2_, p_186336_3_.getX() + 0.5f, p_186336_3_.getY() + 0.5f, p_186336_3_.getZ() + 0.5f, p_186336_4_);
    }
    
    @Nullable
    private Path findPath(final IBlockAccess worldIn, final EntityLiving p_186334_2_, final double p_186334_3_, final double p_186334_5_, final double p_186334_7_, final float p_186334_9_) {
        this.path.clearPath();
        this.nodeProcessor.initProcessor(worldIn, p_186334_2_);
        final PathPoint pathpoint = this.nodeProcessor.getStart();
        final PathPoint pathpoint2 = this.nodeProcessor.getPathPointToCoords(p_186334_3_, p_186334_5_, p_186334_7_);
        final Path path = this.findPath(pathpoint, pathpoint2, p_186334_9_);
        this.nodeProcessor.postProcess();
        return path;
    }
    
    @Nullable
    private Path findPath(final PathPoint p_186335_1_, final PathPoint p_186335_2_, final float p_186335_3_) {
        p_186335_1_.totalPathDistance = 0.0f;
        p_186335_1_.distanceToNext = p_186335_1_.distanceManhattan(p_186335_2_);
        p_186335_1_.distanceToTarget = p_186335_1_.distanceToNext;
        this.path.clearPath();
        this.closedSet.clear();
        this.path.addPoint(p_186335_1_);
        PathPoint pathpoint = p_186335_1_;
        int i = 0;
        while (!this.path.isPathEmpty()) {
            if (++i >= 200) {
                break;
            }
            final PathPoint pathpoint2 = this.path.dequeue();
            if (pathpoint2.equals(p_186335_2_)) {
                pathpoint = p_186335_2_;
                break;
            }
            if (pathpoint2.distanceManhattan(p_186335_2_) < pathpoint.distanceManhattan(p_186335_2_)) {
                pathpoint = pathpoint2;
            }
            pathpoint2.visited = true;
            for (int j = this.nodeProcessor.findPathOptions(this.pathOptions, pathpoint2, p_186335_2_, p_186335_3_), k = 0; k < j; ++k) {
                final PathPoint pathpoint3 = this.pathOptions[k];
                final float f = pathpoint2.distanceManhattan(pathpoint3);
                pathpoint3.distanceFromOrigin = pathpoint2.distanceFromOrigin + f;
                pathpoint3.cost = f + pathpoint3.costMalus;
                final float f2 = pathpoint2.totalPathDistance + pathpoint3.cost;
                if (pathpoint3.distanceFromOrigin < p_186335_3_ && (!pathpoint3.isAssigned() || f2 < pathpoint3.totalPathDistance)) {
                    pathpoint3.previous = pathpoint2;
                    pathpoint3.totalPathDistance = f2;
                    pathpoint3.distanceToNext = pathpoint3.distanceManhattan(p_186335_2_) + pathpoint3.costMalus;
                    if (pathpoint3.isAssigned()) {
                        this.path.changeDistance(pathpoint3, pathpoint3.totalPathDistance + pathpoint3.distanceToNext);
                    }
                    else {
                        pathpoint3.distanceToTarget = pathpoint3.totalPathDistance + pathpoint3.distanceToNext;
                        this.path.addPoint(pathpoint3);
                    }
                }
            }
        }
        if (pathpoint == p_186335_1_) {
            return null;
        }
        final Path path = this.createEntityPath(p_186335_1_, pathpoint);
        return path;
    }
    
    private Path createEntityPath(final PathPoint start, final PathPoint end) {
        int i = 1;
        for (PathPoint pathpoint = end; pathpoint.previous != null; pathpoint = pathpoint.previous) {
            ++i;
        }
        final PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint2 = end;
        --i;
        apathpoint[i] = end;
        while (pathpoint2.previous != null) {
            pathpoint2 = pathpoint2.previous;
            --i;
            apathpoint[i] = pathpoint2;
        }
        return new Path(apathpoint);
    }
}
