// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.injection.selectors.InvalidSelectorException;

public class InvalidMemberDescriptorException extends InvalidSelectorException
{
    private static final long serialVersionUID = 1L;
    private final String input;
    
    public InvalidMemberDescriptorException(final String input, final String message) {
        super(message);
        this.input = input;
    }
    
    public InvalidMemberDescriptorException(final String input, final Throwable cause) {
        super(cause);
        this.input = input;
    }
    
    public InvalidMemberDescriptorException(final String input, final String message, final Throwable cause) {
        super(message, cause);
        this.input = input;
    }
    
    public String getInput() {
        return this.input;
    }
}
