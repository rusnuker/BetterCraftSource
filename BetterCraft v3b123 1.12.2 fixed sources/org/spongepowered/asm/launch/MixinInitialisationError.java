// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import org.spongepowered.asm.mixin.throwables.MixinError;

public class MixinInitialisationError extends MixinError
{
    private static final long serialVersionUID = 1L;
    
    public MixinInitialisationError() {
    }
    
    public MixinInitialisationError(final String message) {
        super(message);
    }
    
    public MixinInitialisationError(final Throwable cause) {
        super(cause);
    }
    
    public MixinInitialisationError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
