// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.throwables;

import org.spongepowered.asm.mixin.extensibility.IActivityContext;
import org.spongepowered.asm.mixin.throwables.MixinException;

public class MixinPreProcessorException extends MixinException
{
    private static final long serialVersionUID = 1L;
    
    public MixinPreProcessorException(final String message, final IActivityContext context) {
        super(message, context);
    }
    
    public MixinPreProcessorException(final String message, final Throwable cause, final IActivityContext context) {
        super(message, cause, context);
    }
}
