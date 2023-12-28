// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

public interface IAttribute
{
    String getAttributeUnlocalizedName();
    
    double clampValue(final double p0);
    
    double getDefaultValue();
    
    boolean getShouldWatch();
    
    @Nullable
    IAttribute getParent();
}
