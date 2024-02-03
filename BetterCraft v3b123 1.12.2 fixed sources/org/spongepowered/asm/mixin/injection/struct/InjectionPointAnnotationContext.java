// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.util.Annotations;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;

public class InjectionPointAnnotationContext extends SelectorAnnotationContext implements IInjectionPointContext
{
    private IInjectionPointContext parentContext;
    
    public InjectionPointAnnotationContext(final IInjectionPointContext parent, final IAnnotationHandle selectorAnnotation, final String selectorCoordinate) {
        super(parent, selectorAnnotation, selectorCoordinate);
        this.parentContext = parent;
    }
    
    public InjectionPointAnnotationContext(final IInjectionPointContext parent, final AnnotationNode selectorAnnotation, final String selectorCoordinate) {
        super(parent, Annotations.handleOf(selectorAnnotation), selectorCoordinate);
        this.parentContext = parent;
    }
    
    @Override
    public void addMessage(final String format, final Object... args) {
        this.parentContext.addMessage(format, args);
    }
    
    @Override
    public MethodNode getMethod() {
        return this.parentContext.getMethod();
    }
    
    @Override
    public AnnotationNode getAnnotationNode() {
        return this.parentContext.getAnnotationNode();
    }
    
    @Override
    public IAnnotationHandle getAnnotation() {
        return this.parentContext.getAnnotation();
    }
    
    @Override
    public String toString() {
        return String.format("%s->%s(%s)", this.parentContext, this.getSelectorAnnotation(), this.getSelectorCoordinate(false));
    }
}
