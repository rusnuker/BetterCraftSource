// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.text;

class AbsoluteValueSubstitution extends NFSubstitution
{
    AbsoluteValueSubstitution(final int pos, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        super(pos, ruleSet, formatter, description);
    }
    
    @Override
    public long transformNumber(final long number) {
        return Math.abs(number);
    }
    
    @Override
    public double transformNumber(final double number) {
        return Math.abs(number);
    }
    
    @Override
    public double composeRuleValue(final double newRuleValue, final double oldRuleValue) {
        return -newRuleValue;
    }
    
    @Override
    public double calcUpperBound(final double oldUpperBound) {
        return Double.MAX_VALUE;
    }
    
    @Override
    char tokenChar() {
        return '>';
    }
}
