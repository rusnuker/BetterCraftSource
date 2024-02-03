// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Collection;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import javax.lang.model.element.TypeElement;

public interface IMixinValidator
{
    boolean validate(final ValidationPass p0, final TypeElement p1, final IAnnotationHandle p2, final Collection<TypeHandle> p3);
    
    public enum ValidationPass
    {
        EARLY, 
        LATE, 
        FINAL;
    }
}
