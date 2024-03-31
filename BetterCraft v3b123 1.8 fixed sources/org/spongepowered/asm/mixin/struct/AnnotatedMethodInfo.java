// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.struct;

import javax.tools.Diagnostic;
import org.spongepowered.asm.util.logging.MessageRouter;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Locale;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;

public class AnnotatedMethodInfo implements IInjectionPointContext
{
    private final IMixinContext context;
    protected final MethodNode method;
    protected final AnnotationNode annotation;
    
    public AnnotatedMethodInfo(final IMixinContext mixin, final MethodNode method, final AnnotationNode annotation) {
        this.context = mixin;
        this.method = method;
        this.annotation = annotation;
    }
    
    @Override
    public String remap(final String reference) {
        if (this.context != null) {
            final IReferenceMapper referenceMapper = this.context.getReferenceMapper();
            return (referenceMapper != null) ? referenceMapper.remap(this.context.getClassRef(), reference) : reference;
        }
        return reference;
    }
    
    @Override
    public ISelectorContext getParent() {
        return null;
    }
    
    @Override
    public final IMixinContext getMixin() {
        return this.context;
    }
    
    @Override
    public final MethodNode getMethod() {
        return this.method;
    }
    
    public String getMethodName() {
        return this.method.name;
    }
    
    @Override
    public AnnotationNode getAnnotationNode() {
        return this.annotation;
    }
    
    @Override
    public final IAnnotationHandle getAnnotation() {
        return Annotations.handleOf(this.annotation);
    }
    
    @Override
    public IAnnotationHandle getSelectorAnnotation() {
        return Annotations.handleOf(this.annotation);
    }
    
    @Override
    public String getSelectorCoordinate(final boolean leaf) {
        return leaf ? "method" : this.getMethodName().toLowerCase(Locale.ROOT);
    }
    
    @Override
    public void addMessage(final String format, final Object... args) {
        if (this.context.getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            MessageRouter.getMessager().printMessage(Diagnostic.Kind.WARNING, String.format(format, args));
        }
    }
}
