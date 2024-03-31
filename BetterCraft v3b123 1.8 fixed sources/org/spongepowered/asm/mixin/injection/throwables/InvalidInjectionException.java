// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.throwables;

import org.spongepowered.asm.mixin.extensibility.IActivityContext;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;

public class InvalidInjectionException extends InvalidMixinException
{
    private static final long serialVersionUID = 2L;
    private final ISelectorContext selectorContext;
    
    public InvalidInjectionException(final IMixinContext context, final String message) {
        super(context, message);
        this.selectorContext = null;
    }
    
    public InvalidInjectionException(final IMixinContext context, final String message, final IActivityContext activityContext) {
        super(context, message, activityContext);
        this.selectorContext = null;
    }
    
    public InvalidInjectionException(final ISelectorContext selectorContext, final String message) {
        super(selectorContext.getMixin(), message);
        this.selectorContext = selectorContext;
    }
    
    public InvalidInjectionException(final ISelectorContext selectorContext, final String message, final IActivityContext activityContext) {
        super(selectorContext.getMixin(), message, activityContext);
        this.selectorContext = selectorContext;
    }
    
    public InvalidInjectionException(final IMixinContext context, final Throwable cause) {
        super(context, cause);
        this.selectorContext = null;
    }
    
    public InvalidInjectionException(final IMixinContext context, final Throwable cause, final IActivityContext activityContext) {
        super(context, cause, activityContext);
        this.selectorContext = null;
    }
    
    public InvalidInjectionException(final ISelectorContext selectorContext, final Throwable cause) {
        super(selectorContext.getMixin(), cause);
        this.selectorContext = selectorContext;
    }
    
    public InvalidInjectionException(final ISelectorContext selectorContext, final Throwable cause, final IActivityContext activityContext) {
        super(selectorContext.getMixin(), cause, activityContext);
        this.selectorContext = selectorContext;
    }
    
    public InvalidInjectionException(final IMixinContext context, final String message, final Throwable cause) {
        super(context, message, cause);
        this.selectorContext = null;
    }
    
    public InvalidInjectionException(final IMixinContext context, final String message, final Throwable cause, final IActivityContext activityContext) {
        super(context, message, cause, activityContext);
        this.selectorContext = null;
    }
    
    public InvalidInjectionException(final ISelectorContext selectorContext, final String message, final Throwable cause) {
        super(selectorContext.getMixin(), message, cause);
        this.selectorContext = selectorContext;
    }
    
    public InvalidInjectionException(final ISelectorContext selectorContext, final String message, final Throwable cause, final IActivityContext activityContext) {
        super(selectorContext.getMixin(), message, cause, activityContext);
        this.selectorContext = selectorContext;
    }
    
    public ISelectorContext getContext() {
        return this.selectorContext;
    }
}
