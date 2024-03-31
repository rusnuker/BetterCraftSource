// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors.throwables;

import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;

public class SelectorConstraintException extends SelectorException
{
    private static final long serialVersionUID = 1L;
    
    public SelectorConstraintException(final ITargetSelector selector, final String message) {
        super(selector, message);
    }
    
    public SelectorConstraintException(final ITargetSelector selector, final Throwable cause) {
        super(selector, cause);
    }
    
    public SelectorConstraintException(final ITargetSelector selector, final String message, final Throwable cause) {
        super(selector, message, cause);
    }
}
