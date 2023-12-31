// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.util.AsciiString;
import java.util.TimeZone;
import io.netty.util.internal.ObjectUtil;
import java.util.Date;
import java.util.GregorianCalendar;
import io.netty.util.concurrent.FastThreadLocal;
import java.util.BitSet;

public final class DateFormatter
{
    private static final BitSet DELIMITERS;
    private static final String[] DAY_OF_WEEK_TO_SHORT_NAME;
    private static final String[] CALENDAR_MONTH_TO_SHORT_NAME;
    private static final FastThreadLocal<DateFormatter> INSTANCES;
    private final GregorianCalendar cal;
    private final StringBuilder sb;
    private boolean timeFound;
    private int hours;
    private int minutes;
    private int seconds;
    private boolean dayOfMonthFound;
    private int dayOfMonth;
    private boolean monthFound;
    private int month;
    private boolean yearFound;
    private int year;
    
    public static Date parseHttpDate(final CharSequence txt) {
        return parseHttpDate(txt, 0, txt.length());
    }
    
    public static Date parseHttpDate(final CharSequence txt, final int start, final int end) {
        final int length = end - start;
        if (length == 0) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException("Can't have end < start");
        }
        if (length > 64) {
            throw new IllegalArgumentException("Can't parse more than 64 chars,looks like a user error or a malformed header");
        }
        return formatter().parse0(ObjectUtil.checkNotNull(txt, "txt"), start, end);
    }
    
    public static String format(final Date date) {
        return formatter().format0(ObjectUtil.checkNotNull(date, "date"));
    }
    
    public static StringBuilder append(final Date date, final StringBuilder sb) {
        return formatter().append0(ObjectUtil.checkNotNull(date, "date"), ObjectUtil.checkNotNull(sb, "sb"));
    }
    
    private static DateFormatter formatter() {
        final DateFormatter formatter = DateFormatter.INSTANCES.get();
        formatter.reset();
        return formatter;
    }
    
    private static boolean isDelim(final char c) {
        return DateFormatter.DELIMITERS.get(c);
    }
    
    private static boolean isDigit(final char c) {
        return c >= '0' && c <= '9';
    }
    
    private static int getNumericalValue(final char c) {
        return c - '0';
    }
    
    private DateFormatter() {
        this.cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        this.sb = new StringBuilder(29);
        this.reset();
    }
    
    public void reset() {
        this.timeFound = false;
        this.hours = -1;
        this.minutes = -1;
        this.seconds = -1;
        this.dayOfMonthFound = false;
        this.dayOfMonth = -1;
        this.monthFound = false;
        this.month = -1;
        this.yearFound = false;
        this.year = -1;
        this.cal.clear();
        this.sb.setLength(0);
    }
    
    private boolean tryParseTime(final CharSequence txt, final int tokenStart, final int tokenEnd) {
        final int len = tokenEnd - tokenStart;
        if (len < 5 || len > 8) {
            return false;
        }
        int localHours = -1;
        int localMinutes = -1;
        int localSeconds = -1;
        int currentPartNumber = 0;
        int currentPartValue = 0;
        int numDigits = 0;
        for (int i = tokenStart; i < tokenEnd; ++i) {
            final char c = txt.charAt(i);
            if (isDigit(c)) {
                currentPartValue = currentPartValue * 10 + getNumericalValue(c);
                if (++numDigits > 2) {
                    return false;
                }
            }
            else {
                if (c != ':') {
                    return false;
                }
                if (numDigits == 0) {
                    return false;
                }
                switch (currentPartNumber) {
                    case 0: {
                        localHours = currentPartValue;
                        break;
                    }
                    case 1: {
                        localMinutes = currentPartValue;
                        break;
                    }
                    default: {
                        return false;
                    }
                }
                currentPartValue = 0;
                ++currentPartNumber;
                numDigits = 0;
            }
        }
        if (numDigits > 0) {
            localSeconds = currentPartValue;
        }
        if (localHours >= 0 && localMinutes >= 0 && localSeconds >= 0) {
            this.hours = localHours;
            this.minutes = localMinutes;
            this.seconds = localSeconds;
            return true;
        }
        return false;
    }
    
    private boolean tryParseDayOfMonth(final CharSequence txt, final int tokenStart, final int tokenEnd) {
        final int len = tokenEnd - tokenStart;
        if (len == 1) {
            final char c0 = txt.charAt(tokenStart);
            if (isDigit(c0)) {
                this.dayOfMonth = getNumericalValue(c0);
                return true;
            }
        }
        else if (len == 2) {
            final char c0 = txt.charAt(tokenStart);
            final char c2 = txt.charAt(tokenStart + 1);
            if (isDigit(c0) && isDigit(c2)) {
                this.dayOfMonth = getNumericalValue(c0) * 10 + getNumericalValue(c2);
                return true;
            }
        }
        return false;
    }
    
    private static boolean matchMonth(final String month, final CharSequence txt, final int tokenStart) {
        return AsciiString.regionMatchesAscii(month, true, 0, txt, tokenStart, 3);
    }
    
    private boolean tryParseMonth(final CharSequence txt, final int tokenStart, final int tokenEnd) {
        final int len = tokenEnd - tokenStart;
        if (len != 3) {
            return false;
        }
        if (matchMonth("Jan", txt, tokenStart)) {
            this.month = 0;
        }
        else if (matchMonth("Feb", txt, tokenStart)) {
            this.month = 1;
        }
        else if (matchMonth("Mar", txt, tokenStart)) {
            this.month = 2;
        }
        else if (matchMonth("Apr", txt, tokenStart)) {
            this.month = 3;
        }
        else if (matchMonth("May", txt, tokenStart)) {
            this.month = 4;
        }
        else if (matchMonth("Jun", txt, tokenStart)) {
            this.month = 5;
        }
        else if (matchMonth("Jul", txt, tokenStart)) {
            this.month = 6;
        }
        else if (matchMonth("Aug", txt, tokenStart)) {
            this.month = 7;
        }
        else if (matchMonth("Sep", txt, tokenStart)) {
            this.month = 8;
        }
        else if (matchMonth("Oct", txt, tokenStart)) {
            this.month = 9;
        }
        else if (matchMonth("Nov", txt, tokenStart)) {
            this.month = 10;
        }
        else {
            if (!matchMonth("Dec", txt, tokenStart)) {
                return false;
            }
            this.month = 11;
        }
        return true;
    }
    
    private boolean tryParseYear(final CharSequence txt, final int tokenStart, final int tokenEnd) {
        final int len = tokenEnd - tokenStart;
        if (len == 2) {
            final char c0 = txt.charAt(tokenStart);
            final char c2 = txt.charAt(tokenStart + 1);
            if (isDigit(c0) && isDigit(c2)) {
                this.year = getNumericalValue(c0) * 10 + getNumericalValue(c2);
                return true;
            }
        }
        else if (len == 4) {
            final char c0 = txt.charAt(tokenStart);
            final char c2 = txt.charAt(tokenStart + 1);
            final char c3 = txt.charAt(tokenStart + 2);
            final char c4 = txt.charAt(tokenStart + 3);
            if (isDigit(c0) && isDigit(c2) && isDigit(c3) && isDigit(c4)) {
                this.year = getNumericalValue(c0) * 1000 + getNumericalValue(c2) * 100 + getNumericalValue(c3) * 10 + getNumericalValue(c4);
                return true;
            }
        }
        return false;
    }
    
    private boolean parseToken(final CharSequence txt, final int tokenStart, final int tokenEnd) {
        if (!this.timeFound) {
            this.timeFound = this.tryParseTime(txt, tokenStart, tokenEnd);
            if (this.timeFound) {
                return this.dayOfMonthFound && this.monthFound && this.yearFound;
            }
        }
        if (!this.dayOfMonthFound) {
            this.dayOfMonthFound = this.tryParseDayOfMonth(txt, tokenStart, tokenEnd);
            if (this.dayOfMonthFound) {
                return this.timeFound && this.monthFound && this.yearFound;
            }
        }
        if (!this.monthFound) {
            this.monthFound = this.tryParseMonth(txt, tokenStart, tokenEnd);
            if (this.monthFound) {
                return this.timeFound && this.dayOfMonthFound && this.yearFound;
            }
        }
        if (!this.yearFound) {
            this.yearFound = this.tryParseYear(txt, tokenStart, tokenEnd);
        }
        return this.timeFound && this.dayOfMonthFound && this.monthFound && this.yearFound;
    }
    
    private Date parse0(final CharSequence txt, final int start, final int end) {
        final boolean allPartsFound = this.parse1(txt, start, end);
        return (allPartsFound && this.normalizeAndValidate()) ? this.computeDate() : null;
    }
    
    private boolean parse1(final CharSequence txt, final int start, final int end) {
        int tokenStart = -1;
        for (int i = start; i < end; ++i) {
            final char c = txt.charAt(i);
            if (isDelim(c)) {
                if (tokenStart != -1) {
                    if (this.parseToken(txt, tokenStart, i)) {
                        return true;
                    }
                    tokenStart = -1;
                }
            }
            else if (tokenStart == -1) {
                tokenStart = i;
            }
        }
        return tokenStart != -1 && this.parseToken(txt, tokenStart, txt.length());
    }
    
    private boolean normalizeAndValidate() {
        if (this.dayOfMonth < 1 || this.dayOfMonth > 31 || this.hours > 23 || this.minutes > 59 || this.seconds > 59) {
            return false;
        }
        if (this.year >= 70 && this.year <= 99) {
            this.year += 1900;
        }
        else if (this.year >= 0 && this.year < 70) {
            this.year += 2000;
        }
        else if (this.year < 1601) {
            return false;
        }
        return true;
    }
    
    private Date computeDate() {
        this.cal.set(5, this.dayOfMonth);
        this.cal.set(2, this.month);
        this.cal.set(1, this.year);
        this.cal.set(11, this.hours);
        this.cal.set(12, this.minutes);
        this.cal.set(13, this.seconds);
        return this.cal.getTime();
    }
    
    private String format0(final Date date) {
        this.append0(date, this.sb);
        return this.sb.toString();
    }
    
    private StringBuilder append0(final Date date, final StringBuilder sb) {
        this.cal.setTime(date);
        sb.append(DateFormatter.DAY_OF_WEEK_TO_SHORT_NAME[this.cal.get(7) - 1]).append(", ");
        sb.append(this.cal.get(5)).append(' ');
        sb.append(DateFormatter.CALENDAR_MONTH_TO_SHORT_NAME[this.cal.get(2)]).append(' ');
        sb.append(this.cal.get(1)).append(' ');
        appendZeroLeftPadded(this.cal.get(11), sb).append(':');
        appendZeroLeftPadded(this.cal.get(12), sb).append(':');
        return appendZeroLeftPadded(this.cal.get(13), sb).append(" GMT");
    }
    
    private static StringBuilder appendZeroLeftPadded(final int value, final StringBuilder sb) {
        if (value < 10) {
            sb.append('0');
        }
        return sb.append(value);
    }
    
    static {
        (DELIMITERS = new BitSet()).set(9);
        for (char c = ' '; c <= '/'; ++c) {
            DateFormatter.DELIMITERS.set(c);
        }
        for (char c = ';'; c <= '@'; ++c) {
            DateFormatter.DELIMITERS.set(c);
        }
        for (char c = '['; c <= '`'; ++c) {
            DateFormatter.DELIMITERS.set(c);
        }
        for (char c = '{'; c <= '~'; ++c) {
            DateFormatter.DELIMITERS.set(c);
        }
        DAY_OF_WEEK_TO_SHORT_NAME = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        CALENDAR_MONTH_TO_SHORT_NAME = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        INSTANCES = new FastThreadLocal<DateFormatter>() {
            @Override
            protected DateFormatter initialValue() {
                return new DateFormatter(null);
            }
        };
    }
}
