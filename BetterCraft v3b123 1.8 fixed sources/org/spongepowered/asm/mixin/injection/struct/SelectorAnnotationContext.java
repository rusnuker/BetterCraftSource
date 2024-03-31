// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;

public class SelectorAnnotationContext implements ISelectorContext
{
    private final ISelectorContext parent;
    private final IAnnotationHandle selectorAnnotation;
    private final String selectorCoordinate;
    
    public SelectorAnnotationContext(final ISelectorContext parent, final IAnnotationHandle selectorAnnotation, final String selectorCoordinate) {
        this.parent = parent;
        this.selectorAnnotation = selectorAnnotation;
        this.selectorCoordinate = selectorCoordinate;
    }
    
    @Override
    public ISelectorContext getParent() {
        return this.parent;
    }
    
    @Override
    public IMixinContext getMixin() {
        return this.parent.getMixin();
    }
    
    @Override
    public Object getMethod() {
        return this.parent.getMethod();
    }
    
    @Override
    public IAnnotationHandle getAnnotation() {
        return this.parent.getAnnotation();
    }
    
    @Override
    public IAnnotationHandle getSelectorAnnotation() {
        return this.selectorAnnotation;
    }
    
    @Override
    public String getSelectorCoordinate(final boolean leaf) {
        return this.selectorCoordinate;
    }
    
    @Override
    public String remap(final String reference) {
        return this.parent.remap(reference);
    }
}
