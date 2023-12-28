// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft;

public class Position
{
    private final int x;
    private final int y;
    private final int z;
    
    public Position(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Position(final int x, final short y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Position(final Position toCopy) {
        this(toCopy.getX(), toCopy.getY(), toCopy.getZ());
    }
    
    public Position getRelative(final BlockFace face) {
        return new Position(this.x + face.getModX(), (short)(this.y + face.getModY()), this.z + face.getModZ());
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Position position = (Position)o;
        return this.x == position.x && this.y == position.y && this.z == position.z;
    }
    
    @Override
    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }
    
    @Override
    public String toString() {
        return "Position{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }
}
