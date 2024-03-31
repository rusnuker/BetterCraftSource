// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.util;

public class TimeUnitAmount extends Measure
{
    public TimeUnitAmount(final Number number, final TimeUnit unit) {
        super(number, unit);
    }
    
    public TimeUnitAmount(final double number, final TimeUnit unit) {
        super(new Double(number), unit);
    }
    
    public TimeUnit getTimeUnit() {
        return (TimeUnit)this.getUnit();
    }
}
