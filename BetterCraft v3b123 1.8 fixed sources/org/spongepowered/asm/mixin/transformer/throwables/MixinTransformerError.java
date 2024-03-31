// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.throwables;

import org.spongepowered.asm.mixin.throwables.MixinError;

public class MixinTransformerError extends MixinError
{
    private static final long serialVersionUID = 1L;
    
    public MixinTransformerError(final String message) {
        super(message);
    }
    
    public MixinTransformerError(final Throwable cause) {
        super(cause);
    }
    
    public MixinTransformerError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
