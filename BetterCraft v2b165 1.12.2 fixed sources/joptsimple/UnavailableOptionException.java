// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

import java.util.Collection;
import java.util.List;

class UnavailableOptionException extends OptionException
{
    private static final long serialVersionUID = -1L;
    
    UnavailableOptionException(final List<? extends OptionSpec<?>> forbiddenOptions) {
        super(forbiddenOptions);
    }
    
    @Override
    Object[] messageArguments() {
        return new Object[] { this.multipleOptionString() };
    }
}
