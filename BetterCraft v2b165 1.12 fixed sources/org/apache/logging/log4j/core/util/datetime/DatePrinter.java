// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util.datetime;

import java.text.FieldPosition;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;

public interface DatePrinter
{
    String format(final long p0);
    
    String format(final Date p0);
    
    String format(final Calendar p0);
    
     <B extends Appendable> B format(final long p0, final B p1);
    
     <B extends Appendable> B format(final Date p0, final B p1);
    
     <B extends Appendable> B format(final Calendar p0, final B p1);
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Locale getLocale();
    
    StringBuilder format(final Object p0, final StringBuilder p1, final FieldPosition p2);
}
