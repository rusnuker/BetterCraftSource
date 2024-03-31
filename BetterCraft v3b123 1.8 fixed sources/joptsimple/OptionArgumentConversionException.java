// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

import java.util.Collection;

class OptionArgumentConversionException extends OptionException
{
    private static final long serialVersionUID = -1L;
    private final String argument;
    
    OptionArgumentConversionException(final Collection<String> options, final String argument, final Throwable cause) {
        super(options, cause);
        this.argument = argument;
    }
    
    @Override
    public String getMessage() {
        return "Cannot parse argument '" + this.argument + "' of option " + this.multipleOptionMessage();
    }
}
