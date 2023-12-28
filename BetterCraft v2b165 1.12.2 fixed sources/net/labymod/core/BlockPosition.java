// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

public class BlockPosition
{
    private int x;
    private int y;
    private int z;
    
    public BlockPosition(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setZ(final int z) {
        this.z = z;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BlockPosition)) {
            return false;
        }
        final BlockPosition blockposition = (BlockPosition)obj;
        return blockposition.getX() == this.x && blockposition.getY() == this.y && blockposition.getZ() == this.z;
    }
}
