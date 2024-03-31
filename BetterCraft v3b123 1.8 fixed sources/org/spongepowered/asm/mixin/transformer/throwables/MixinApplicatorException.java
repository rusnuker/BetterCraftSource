// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.throwables;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.extensibility.IActivityContext;
import org.spongepowered.asm.mixin.transformer.ActivityStack;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinApplicatorException extends InvalidMixinException
{
    private static final long serialVersionUID = 1L;
    
    public MixinApplicatorException(final IMixinInfo context, final String message) {
        super(context, message, (IActivityContext)null);
    }
    
    public MixinApplicatorException(final IMixinInfo context, final String message, final IActivityContext activityContext) {
        super(context, message, activityContext);
    }
    
    public MixinApplicatorException(final IMixinContext context, final String message) {
        super(context, message, (IActivityContext)null);
    }
    
    public MixinApplicatorException(final IMixinContext context, final String message, final IActivityContext activityContext) {
        super(context, message, activityContext);
    }
    
    public MixinApplicatorException(final IMixinInfo mixin, final String message, final Throwable cause) {
        super(mixin, message, cause, null);
    }
    
    public MixinApplicatorException(final IMixinInfo mixin, final String message, final Throwable cause, final IActivityContext activityContext) {
        super(mixin, message, cause, activityContext);
    }
    
    public MixinApplicatorException(final IMixinContext mixin, final String message, final Throwable cause) {
        super(mixin, message, cause, null);
    }
    
    public MixinApplicatorException(final IMixinContext mixin, final String message, final Throwable cause, final IActivityContext activityContext) {
        super(mixin, message, cause, activityContext);
    }
    
    public MixinApplicatorException(final IMixinInfo mixin, final Throwable cause) {
        super(mixin, cause, null);
    }
    
    public MixinApplicatorException(final IMixinInfo mixin, final Throwable cause, final IActivityContext activityContext) {
        super(mixin, cause, activityContext);
    }
    
    public MixinApplicatorException(final IMixinContext mixin, final Throwable cause) {
        super(mixin, cause, null);
    }
    
    public MixinApplicatorException(final IMixinContext mixin, final Throwable cause, final IActivityContext activityContext) {
        super(mixin, cause, activityContext);
    }
}
