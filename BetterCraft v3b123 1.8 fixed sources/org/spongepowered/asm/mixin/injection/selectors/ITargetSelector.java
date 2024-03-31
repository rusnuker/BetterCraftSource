// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

public interface ITargetSelector
{
    ITargetSelector next();
    
    ITargetSelector configure(final Configure p0, final String... p1);
    
    ITargetSelector validate() throws InvalidSelectorException;
    
    ITargetSelector attach(final ISelectorContext p0) throws InvalidSelectorException;
    
    int getMinMatchCount();
    
    int getMaxMatchCount();
    
     <TNode> MatchResult match(final ElementNode<TNode> p0);
    
    public enum Configure
    {
        SELECT_MEMBER(0), 
        SELECT_INSTRUCTION(0), 
        MOVE(1), 
        ORPHAN(0), 
        TRANSFORM(1), 
        PERMISSIVE(0), 
        CLEAR_LIMITS(0);
        
        private int requiredArgs;
        
        private Configure(final int requiredArgs) {
            this.requiredArgs = requiredArgs;
        }
        
        public void checkArgs(final String... args) throws IllegalArgumentException {
            final int argc = (args == null) ? 0 : args.length;
            if (argc < this.requiredArgs) {
                throw new IllegalArgumentException("Insufficient arguments for " + this.name() + " mutation. Required " + this.requiredArgs + " but received " + argc);
            }
        }
    }
}
