// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.text;

interface RBNFPostProcessor
{
    void init(final RuleBasedNumberFormat p0, final String p1);
    
    void process(final StringBuffer p0, final NFRuleSet p1);
}
