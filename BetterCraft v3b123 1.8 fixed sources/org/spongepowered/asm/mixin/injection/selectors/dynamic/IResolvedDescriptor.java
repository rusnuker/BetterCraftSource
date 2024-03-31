// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors.dynamic;

import java.util.List;
import org.spongepowered.asm.util.Quantifier;
import org.objectweb.asm.Type;
import org.spongepowered.asm.util.asm.IAnnotationHandle;

public interface IResolvedDescriptor
{
    boolean isResolved();
    
    IAnnotationHandle getAnnotation();
    
    String getResolutionInfo();
    
    String getId();
    
    Type getOwner();
    
    String getName();
    
    Type[] getArgs();
    
    Type getReturnType();
    
    Quantifier getMatches();
    
    List<IAnnotationHandle> getNext();
}
