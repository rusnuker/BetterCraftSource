// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.util.prediction;

public class JumpVec3d
{
    public static final JumpVec3d ZERO;
    public final double x;
    public final double y;
    public final double z;
    
    static {
        ZERO = new JumpVec3d(0.0, 0.0, 0.0);
    }
    
    public JumpVec3d(double xIn, double yIn, double zIn) {
        if (xIn == -0.0) {
            xIn = 0.0;
        }
        if (yIn == -0.0) {
            yIn = 0.0;
        }
        if (zIn == -0.0) {
            zIn = 0.0;
        }
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }
    
    public JumpVec3d subtractReverse(final JumpVec3d vec) {
        return new JumpVec3d(vec.x - this.x, vec.y - this.y, vec.z - this.z);
    }
    
    public JumpVec3d normalize() {
        final double d0 = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return (d0 < 1.0E-4) ? JumpVec3d.ZERO : new JumpVec3d(this.x / d0, this.y / d0, this.z / d0);
    }
    
    public double dotProduct(final JumpVec3d vec) {
        return this.x * vec.x + this.y * vec.y + this.z * vec.z;
    }
    
    public JumpVec3d crossProduct(final JumpVec3d vec) {
        return new JumpVec3d(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x);
    }
    
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
}
