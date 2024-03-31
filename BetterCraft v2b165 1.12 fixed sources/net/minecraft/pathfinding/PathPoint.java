// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.pathfinding;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;

public class PathPoint
{
    public final int xCoord;
    public final int yCoord;
    public final int zCoord;
    private final int hash;
    public int index;
    public float totalPathDistance;
    public float distanceToNext;
    public float distanceToTarget;
    public PathPoint previous;
    public boolean visited;
    public float distanceFromOrigin;
    public float cost;
    public float costMalus;
    public PathNodeType nodeType;
    
    public PathPoint(final int x, final int y, final int z) {
        this.index = -1;
        this.nodeType = PathNodeType.BLOCKED;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.hash = makeHash(x, y, z);
    }
    
    public PathPoint cloneMove(final int x, final int y, final int z) {
        final PathPoint pathpoint = new PathPoint(x, y, z);
        pathpoint.index = this.index;
        pathpoint.totalPathDistance = this.totalPathDistance;
        pathpoint.distanceToNext = this.distanceToNext;
        pathpoint.distanceToTarget = this.distanceToTarget;
        pathpoint.previous = this.previous;
        pathpoint.visited = this.visited;
        pathpoint.distanceFromOrigin = this.distanceFromOrigin;
        pathpoint.cost = this.cost;
        pathpoint.costMalus = this.costMalus;
        pathpoint.nodeType = this.nodeType;
        return pathpoint;
    }
    
    public static int makeHash(final int x, final int y, final int z) {
        return (y & 0xFF) | (x & 0x7FFF) << 8 | (z & 0x7FFF) << 24 | ((x < 0) ? Integer.MIN_VALUE : 0) | ((z < 0) ? 32768 : 0);
    }
    
    public float distanceTo(final PathPoint pathpointIn) {
        final float f = (float)(pathpointIn.xCoord - this.xCoord);
        final float f2 = (float)(pathpointIn.yCoord - this.yCoord);
        final float f3 = (float)(pathpointIn.zCoord - this.zCoord);
        return MathHelper.sqrt(f * f + f2 * f2 + f3 * f3);
    }
    
    public float distanceToSquared(final PathPoint pathpointIn) {
        final float f = (float)(pathpointIn.xCoord - this.xCoord);
        final float f2 = (float)(pathpointIn.yCoord - this.yCoord);
        final float f3 = (float)(pathpointIn.zCoord - this.zCoord);
        return f * f + f2 * f2 + f3 * f3;
    }
    
    public float distanceManhattan(final PathPoint p_186281_1_) {
        final float f = (float)Math.abs(p_186281_1_.xCoord - this.xCoord);
        final float f2 = (float)Math.abs(p_186281_1_.yCoord - this.yCoord);
        final float f3 = (float)Math.abs(p_186281_1_.zCoord - this.zCoord);
        return f + f2 + f3;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!(p_equals_1_ instanceof PathPoint)) {
            return false;
        }
        final PathPoint pathpoint = (PathPoint)p_equals_1_;
        return this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord && this.yCoord == pathpoint.yCoord && this.zCoord == pathpoint.zCoord;
    }
    
    @Override
    public int hashCode() {
        return this.hash;
    }
    
    public boolean isAssigned() {
        return this.index >= 0;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.xCoord) + ", " + this.yCoord + ", " + this.zCoord;
    }
    
    public static PathPoint createFromBuffer(final PacketBuffer buf) {
        final PathPoint pathpoint = new PathPoint(buf.readInt(), buf.readInt(), buf.readInt());
        pathpoint.distanceFromOrigin = buf.readFloat();
        pathpoint.cost = buf.readFloat();
        pathpoint.costMalus = buf.readFloat();
        pathpoint.visited = buf.readBoolean();
        pathpoint.nodeType = PathNodeType.values()[buf.readInt()];
        pathpoint.distanceToTarget = buf.readFloat();
        return pathpoint;
    }
}
