// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.throwables;

public class IllegalClassLoadError extends MixinTransformerError
{
    private static final long serialVersionUID = 1L;
    
    public IllegalClassLoadError(final String message) {
        super(message);
    }
    
    public IllegalClassLoadError(final Throwable cause) {
        super(cause);
    }
    
    public IllegalClassLoadError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
