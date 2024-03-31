// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.throwables;

import org.spongepowered.asm.mixin.throwables.MixinError;

public class InjectionError extends MixinError
{
    private static final long serialVersionUID = 1L;
    
    public InjectionError() {
    }
    
    public InjectionError(final String message) {
        super(message);
    }
    
    public InjectionError(final Throwable cause) {
        super(cause);
    }
    
    public InjectionError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
