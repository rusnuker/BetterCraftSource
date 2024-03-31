// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.throwables;

import org.spongepowered.asm.mixin.extensibility.IActivityContext;

public class MixinException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private String activityDescriptor;
    
    public MixinException(final String message) {
        super(message);
    }
    
    public MixinException(final String message, final IActivityContext context) {
        super(message);
        this.activityDescriptor = ((context != null) ? context.toString() : null);
    }
    
    public MixinException(final Throwable cause) {
        super(cause);
    }
    
    public MixinException(final Throwable cause, final IActivityContext context) {
        super(cause);
        this.activityDescriptor = ((context != null) ? context.toString() : null);
    }
    
    public MixinException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MixinException(final String message, final Throwable cause, final IActivityContext context) {
        super(message, cause);
        this.activityDescriptor = ((context != null) ? context.toString() : null);
    }
    
    public void prepend(final IActivityContext upstreamContext) {
        final String strContext = upstreamContext.toString();
        this.activityDescriptor = ((this.activityDescriptor != null) ? (strContext + " -> " + this.activityDescriptor) : (" -> " + strContext));
    }
    
    @Override
    public String getMessage() {
        final String message = super.getMessage();
        return (this.activityDescriptor != null) ? (message + " [" + this.activityDescriptor + "]") : message;
    }
}
