// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

import org.spongepowered.asm.util.asm.IAnnotationHandle;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public interface ISelectorContext
{
    ISelectorContext getParent();
    
    IMixinContext getMixin();
    
    Object getMethod();
    
    IAnnotationHandle getAnnotation();
    
    IAnnotationHandle getSelectorAnnotation();
    
    String getSelectorCoordinate(final boolean p0);
    
    String remap(final String p0);
}
