// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.extensibility;

import org.spongepowered.asm.logging.Level;

public interface IMixinErrorHandler
{
    ErrorAction onPrepareError(final IMixinConfig p0, final Throwable p1, final IMixinInfo p2, final ErrorAction p3);
    
    ErrorAction onApplyError(final String p0, final Throwable p1, final IMixinInfo p2, final ErrorAction p3);
    
    public enum ErrorAction
    {
        NONE(Level.INFO), 
        WARN(Level.WARN), 
        ERROR(Level.FATAL);
        
        public final Level logLevel;
        
        private ErrorAction(final Level logLevel) {
            this.logLevel = logLevel;
        }
    }
}
