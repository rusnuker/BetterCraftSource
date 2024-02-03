// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.throwables;

import org.spongepowered.asm.mixin.throwables.MixinError;

public class LVTGeneratorError extends MixinError
{
    private static final long serialVersionUID = 1L;
    
    public LVTGeneratorError(final String message) {
        super(message);
    }
    
    public LVTGeneratorError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
