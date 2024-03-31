// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;

public enum BlockDir
{
    DOWN("DOWN", 0, EnumFacing.DOWN), 
    UP("UP", 1, EnumFacing.UP), 
    NORTH("NORTH", 2, EnumFacing.NORTH), 
    SOUTH("SOUTH", 3, EnumFacing.SOUTH), 
    WEST("WEST", 4, EnumFacing.WEST), 
    EAST("EAST", 5, EnumFacing.EAST), 
    NORTH_WEST("NORTH_WEST", 6, EnumFacing.NORTH, EnumFacing.WEST), 
    NORTH_EAST("NORTH_EAST", 7, EnumFacing.NORTH, EnumFacing.EAST), 
    SOUTH_WEST("SOUTH_WEST", 8, EnumFacing.SOUTH, EnumFacing.WEST), 
    SOUTH_EAST("SOUTH_EAST", 9, EnumFacing.SOUTH, EnumFacing.EAST), 
    DOWN_NORTH("DOWN_NORTH", 10, EnumFacing.DOWN, EnumFacing.NORTH), 
    DOWN_SOUTH("DOWN_SOUTH", 11, EnumFacing.DOWN, EnumFacing.SOUTH), 
    UP_NORTH("UP_NORTH", 12, EnumFacing.UP, EnumFacing.NORTH), 
    UP_SOUTH("UP_SOUTH", 13, EnumFacing.UP, EnumFacing.SOUTH), 
    DOWN_WEST("DOWN_WEST", 14, EnumFacing.DOWN, EnumFacing.WEST), 
    DOWN_EAST("DOWN_EAST", 15, EnumFacing.DOWN, EnumFacing.EAST), 
    UP_WEST("UP_WEST", 16, EnumFacing.UP, EnumFacing.WEST), 
    UP_EAST("UP_EAST", 17, EnumFacing.UP, EnumFacing.EAST);
    
    private EnumFacing facing1;
    private EnumFacing facing2;
    
    private BlockDir(final String s, final int n, final EnumFacing p_i12_3_) {
        this.facing1 = p_i12_3_;
    }
    
    private BlockDir(final String s, final int n, final EnumFacing p_i13_3_, final EnumFacing p_i13_4_) {
        this.facing1 = p_i13_3_;
        this.facing2 = p_i13_4_;
    }
    
    public EnumFacing getFacing1() {
        return this.facing1;
    }
    
    public EnumFacing getFacing2() {
        return this.facing2;
    }
    
    BlockPos offset(BlockPos p_offset_1_) {
        p_offset_1_ = p_offset_1_.offset(this.facing1, 1);
        if (this.facing2 != null) {
            p_offset_1_ = p_offset_1_.offset(this.facing2, 1);
        }
        return p_offset_1_;
    }
    
    public int getOffsetX() {
        int i = this.facing1.getFrontOffsetX();
        if (this.facing2 != null) {
            i += this.facing2.getFrontOffsetX();
        }
        return i;
    }
    
    public int getOffsetY() {
        int i = this.facing1.getFrontOffsetY();
        if (this.facing2 != null) {
            i += this.facing2.getFrontOffsetY();
        }
        return i;
    }
    
    public int getOffsetZ() {
        int i = this.facing1.getFrontOffsetZ();
        if (this.facing2 != null) {
            i += this.facing2.getFrontOffsetZ();
        }
        return i;
    }
    
    public boolean isDouble() {
        return this.facing2 != null;
    }
}
