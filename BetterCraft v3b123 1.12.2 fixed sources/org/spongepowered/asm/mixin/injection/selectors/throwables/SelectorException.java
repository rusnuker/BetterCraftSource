// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors.throwables;

import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import org.spongepowered.asm.mixin.throwables.MixinException;

public class SelectorException extends MixinException
{
    private static final long serialVersionUID = 1L;
    private final ITargetSelector selector;
    
    public SelectorException(final ITargetSelector selector, final String message) {
        super(message);
        this.selector = selector;
    }
    
    public SelectorException(final ITargetSelector selector, final Throwable cause) {
        super(cause);
        this.selector = selector;
    }
    
    public SelectorException(final ITargetSelector selector, final String message, final Throwable cause) {
        super(message, cause);
        this.selector = selector;
    }
    
    public ITargetSelector getSelector() {
        return this.selector;
    }
}
