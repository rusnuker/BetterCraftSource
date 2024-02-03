// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

public class ServiceInitialisationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public ServiceInitialisationException() {
    }
    
    public ServiceInitialisationException(final String message) {
        super(message);
    }
    
    public ServiceInitialisationException(final Throwable cause) {
        super(cause);
    }
    
    public ServiceInitialisationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
