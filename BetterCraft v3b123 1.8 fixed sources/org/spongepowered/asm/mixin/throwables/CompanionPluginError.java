// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.throwables;

public class CompanionPluginError extends LinkageError
{
    private static final long serialVersionUID = 1L;
    
    public CompanionPluginError() {
    }
    
    public CompanionPluginError(final String message) {
        super(message);
    }
    
    public CompanionPluginError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
