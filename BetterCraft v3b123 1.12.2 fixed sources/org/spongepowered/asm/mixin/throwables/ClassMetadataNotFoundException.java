// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.throwables;

import org.spongepowered.asm.mixin.extensibility.IActivityContext;

public class ClassMetadataNotFoundException extends MixinException
{
    private static final long serialVersionUID = 1L;
    
    public ClassMetadataNotFoundException(final String message) {
        super(message);
    }
    
    public ClassMetadataNotFoundException(final String message, final IActivityContext context) {
        super(message, context);
    }
    
    public ClassMetadataNotFoundException(final Throwable cause) {
        super(cause);
    }
    
    public ClassMetadataNotFoundException(final Throwable cause, final IActivityContext context) {
        super(cause, context);
    }
    
    public ClassMetadataNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ClassMetadataNotFoundException(final String message, final Throwable cause, final IActivityContext context) {
        super(message, cause, context);
    }
}
