// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.throwables;

public class ReEntrantTransformerError extends MixinTransformerError
{
    private static final long serialVersionUID = 7073583236491579255L;
    
    public ReEntrantTransformerError(final String message) {
        super(message);
    }
    
    public ReEntrantTransformerError(final Throwable cause) {
        super(cause);
    }
    
    public ReEntrantTransformerError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
