// 
// Decompiled by Procyon v0.6.0
// 

package org.javapluginapi.team.api;

public class PluginException extends RuntimeException
{
    private static final long serialVersionUID = -8161437637562938254L;
    
    public PluginException(final String whatHappened) {
        super(whatHappened);
    }
    
    public PluginException(final String whatHappened, final Throwable cause) {
        super(whatHappened, cause);
    }
}
