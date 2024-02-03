// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.throwables;

public class MixinApplyError extends MixinError
{
    private static final long serialVersionUID = 1L;
    
    public MixinApplyError(final String message) {
        super(message);
    }
    
    public MixinApplyError(final Throwable cause) {
        super(cause);
    }
    
    public MixinApplyError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
