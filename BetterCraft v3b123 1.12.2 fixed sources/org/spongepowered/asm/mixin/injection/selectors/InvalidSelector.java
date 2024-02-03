// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

public class InvalidSelector implements ITargetSelector
{
    private String input;
    private Throwable cause;
    
    public InvalidSelector(final Throwable cause) {
        this(cause, null);
    }
    
    public InvalidSelector(final String input) {
        this(null, input);
    }
    
    public InvalidSelector(final Throwable cause, final String input) {
        this.input = input;
        this.cause = cause;
    }
    
    @Override
    public String toString() {
        if (this.cause != null) {
            return String.format("%s: %s", this.cause.getClass().getName(), this.cause.getMessage());
        }
        return this.input;
    }
    
    @Override
    public ITargetSelector next() {
        return null;
    }
    
    @Override
    public ITargetSelector configure(final Configure request, final String... args) {
        return this;
    }
    
    @Override
    public ITargetSelector validate() throws InvalidSelectorException {
        if (this.cause instanceof InvalidSelectorException) {
            throw (InvalidSelectorException)this.cause;
        }
        String message = "Error parsing target selector";
        if (this.input != null) {
            message = message + ", the input was in an unexpected format: " + this.input;
        }
        if (this.cause != null) {
            throw new InvalidSelectorException(message, this.cause);
        }
        throw new InvalidSelectorException(message);
    }
    
    @Override
    public ITargetSelector attach(final ISelectorContext context) throws InvalidSelectorException {
        return this;
    }
    
    @Override
    public int getMinMatchCount() {
        return 0;
    }
    
    @Override
    public int getMaxMatchCount() {
        return 0;
    }
    
    @Override
    public <TNode> MatchResult match(final ElementNode<TNode> node) {
        this.validate();
        return MatchResult.NONE;
    }
}
