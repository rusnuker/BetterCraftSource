// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.throwables;

public class MixinError extends Error
{
    private static final long serialVersionUID = 1L;
    
    public MixinError() {
    }
    
    public MixinError(final String message) {
        super(message);
    }
    
    public MixinError(final Throwable cause) {
        super(cause);
    }
    
    public MixinError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
