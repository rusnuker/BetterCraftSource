// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockHorizontal extends Block
{
    public static final PropertyDirection FACING;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }
    
    protected BlockHorizontal(final Material materialIn) {
        super(materialIn);
    }
    
    protected BlockHorizontal(final Material materialIn, final MapColor colorIn) {
        super(materialIn, colorIn);
    }
}
