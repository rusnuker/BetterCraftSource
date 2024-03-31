// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.lang3.time;

import java.util.GregorianCalendar;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.util.Calendar;
import java.lang.reflect.Method;

class CalendarReflection
{
    private static final Method IS_WEEK_DATE_SUPPORTED;
    private static final Method GET_WEEK_YEAR;
    
    private static Method getCalendarMethod(final String methodName, final Class<?>... argTypes) {
        try {
            final Method m = Calendar.class.getMethod(methodName, argTypes);
            return m;
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    static boolean isWeekDateSupported(final Calendar calendar) {
        try {
            return CalendarReflection.IS_WEEK_DATE_SUPPORTED != null && (boolean)CalendarReflection.IS_WEEK_DATE_SUPPORTED.invoke(calendar, new Object[0]);
        }
        catch (final Exception e) {
            return ExceptionUtils.rethrow(e);
        }
    }
    
    public static int getWeekYear(final Calendar calendar) {
        try {
            if (isWeekDateSupported(calendar)) {
                return (int)CalendarReflection.GET_WEEK_YEAR.invoke(calendar, new Object[0]);
            }
        }
        catch (final Exception e) {
            return ExceptionUtils.rethrow(e);
        }
        int year = calendar.get(1);
        if (CalendarReflection.IS_WEEK_DATE_SUPPORTED == null && calendar instanceof GregorianCalendar) {
            switch (calendar.get(2)) {
                case 0: {
                    if (calendar.get(3) >= 52) {
                        --year;
                        break;
                    }
                    break;
                }
                case 11: {
                    if (calendar.get(3) == 1) {
                        ++year;
                        break;
                    }
                    break;
                }
            }
        }
        return year;
    }
    
    static {
        IS_WEEK_DATE_SUPPORTED = getCalendarMethod("isWeekDateSupported", (Class<?>[])new Class[0]);
        GET_WEEK_YEAR = getCalendarMethod("getWeekYear", (Class<?>[])new Class[0]);
    }
}
