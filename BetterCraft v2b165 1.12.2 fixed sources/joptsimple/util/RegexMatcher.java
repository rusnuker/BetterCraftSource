// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.util;

import joptsimple.ValueConversionException;
import joptsimple.internal.Messages;
import java.util.Locale;
import java.util.regex.Pattern;
import joptsimple.ValueConverter;

public class RegexMatcher implements ValueConverter<String>
{
    private final Pattern pattern;
    
    public RegexMatcher(final String pattern, final int flags) {
        this.pattern = Pattern.compile(pattern, flags);
    }
    
    public static ValueConverter<String> regex(final String pattern) {
        return new RegexMatcher(pattern, 0);
    }
    
    @Override
    public String convert(final String value) {
        if (!this.pattern.matcher(value).matches()) {
            this.raiseValueConversionFailure(value);
        }
        return value;
    }
    
    @Override
    public Class<String> valueType() {
        return String.class;
    }
    
    @Override
    public String valuePattern() {
        return this.pattern.pattern();
    }
    
    private void raiseValueConversionFailure(final String value) {
        final String message = Messages.message(Locale.getDefault(), "joptsimple.ExceptionMessages", RegexMatcher.class, "message", value, this.pattern.pattern());
        throw new ValueConversionException(message);
    }
}
