// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.injection.selectors.InvalidSelectorException;

public class TargetNotSupportedException extends InvalidSelectorException
{
    private static final long serialVersionUID = 1L;
    
    public TargetNotSupportedException(final String message) {
        super(message);
    }
    
    public TargetNotSupportedException(final Throwable cause) {
        super(cause);
    }
    
    public TargetNotSupportedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
