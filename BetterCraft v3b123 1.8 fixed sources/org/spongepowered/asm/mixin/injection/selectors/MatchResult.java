// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

public enum MatchResult
{
    NONE, 
    WEAK, 
    MATCH, 
    EXACT_MATCH;
    
    public boolean isAtLeast(final MatchResult other) {
        return other == null || other.ordinal() <= this.ordinal();
    }
    
    public boolean isMatch() {
        return this.ordinal() >= MatchResult.MATCH.ordinal();
    }
    
    public boolean isExactMatch() {
        return this == MatchResult.EXACT_MATCH;
    }
}
