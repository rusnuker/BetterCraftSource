// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.asm;

import java.lang.annotation.Annotation;

public interface IAnnotatedElement
{
    IAnnotationHandle getAnnotation(final Class<? extends Annotation> p0);
}
