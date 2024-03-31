// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.gen.throwables;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;

public class InvalidAccessorException extends InvalidMixinException
{
    private static final long serialVersionUID = 2L;
    private final AccessorInfo info;
    
    public InvalidAccessorException(final IMixinContext context, final String message) {
        super(context, message);
        this.info = null;
    }
    
    public InvalidAccessorException(final AccessorInfo info, final String message) {
        super(info.getMixin(), message);
        this.info = info;
    }
    
    public InvalidAccessorException(final IMixinContext context, final Throwable cause) {
        super(context, cause);
        this.info = null;
    }
    
    public InvalidAccessorException(final AccessorInfo info, final Throwable cause) {
        super(info.getMixin(), cause);
        this.info = info;
    }
    
    public InvalidAccessorException(final IMixinContext context, final String message, final Throwable cause) {
        super(context, message, cause);
        this.info = null;
    }
    
    public InvalidAccessorException(final AccessorInfo info, final String message, final Throwable cause) {
        super(info.getMixin(), message, cause);
        this.info = info;
    }
    
    public AccessorInfo getAccessorInfo() {
        return this.info;
    }
}
