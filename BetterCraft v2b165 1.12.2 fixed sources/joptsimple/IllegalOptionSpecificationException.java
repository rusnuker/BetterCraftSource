// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

import java.util.Collections;

class IllegalOptionSpecificationException extends OptionException
{
    private static final long serialVersionUID = -1L;
    
    IllegalOptionSpecificationException(final String option) {
        super(Collections.singletonList(option));
    }
    
    @Override
    Object[] messageArguments() {
        return new Object[] { this.singleOptionString() };
    }
}
