// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.culling;

import net.minecraft.util.math.AxisAlignedBB;

public interface ICamera
{
    boolean isBoundingBoxInFrustum(final AxisAlignedBB p0);
    
    void setPosition(final double p0, final double p1, final double p2);
}
