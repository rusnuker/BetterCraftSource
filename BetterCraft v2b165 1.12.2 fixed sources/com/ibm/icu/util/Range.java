// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.util;

import java.util.Date;

class Range
{
    public Date start;
    public DateRule rule;
    
    public Range(final Date start, final DateRule rule) {
        this.start = start;
        this.rule = rule;
    }
}
