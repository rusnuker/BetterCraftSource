// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

import org.spongepowered.asm.mixin.throwables.MixinException;

public class InvalidSelectorException extends MixinException
{
    private static final long serialVersionUID = 1L;
    
    public InvalidSelectorException(final String message) {
        super(message);
    }
    
    public InvalidSelectorException(final Throwable cause) {
        super(cause);
    }
    
    public InvalidSelectorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
