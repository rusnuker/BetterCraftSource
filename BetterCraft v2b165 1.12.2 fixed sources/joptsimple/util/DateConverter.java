// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.util;

import joptsimple.internal.Messages;
import java.util.Locale;
import joptsimple.ValueConversionException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import joptsimple.ValueConverter;

public class DateConverter implements ValueConverter<Date>
{
    private final DateFormat formatter;
    
    public DateConverter(final DateFormat formatter) {
        if (formatter == null) {
            throw new NullPointerException("illegal null formatter");
        }
        this.formatter = formatter;
    }
    
    public static DateConverter datePattern(final String pattern) {
        final SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setLenient(false);
        return new DateConverter(formatter);
    }
    
    @Override
    public Date convert(final String value) {
        final ParsePosition position = new ParsePosition(0);
        final Date date = this.formatter.parse(value, position);
        if (position.getIndex() != value.length()) {
            throw new ValueConversionException(this.message(value));
        }
        return date;
    }
    
    @Override
    public Class<Date> valueType() {
        return Date.class;
    }
    
    @Override
    public String valuePattern() {
        return (this.formatter instanceof SimpleDateFormat) ? ((SimpleDateFormat)this.formatter).toPattern() : "";
    }
    
    private String message(final String value) {
        String key;
        Object[] arguments;
        if (this.formatter instanceof SimpleDateFormat) {
            key = "with.pattern.message";
            arguments = new Object[] { value, ((SimpleDateFormat)this.formatter).toPattern() };
        }
        else {
            key = "without.pattern.message";
            arguments = new Object[] { value };
        }
        return Messages.message(Locale.getDefault(), "joptsimple.ExceptionMessages", DateConverter.class, key, arguments);
    }
}
