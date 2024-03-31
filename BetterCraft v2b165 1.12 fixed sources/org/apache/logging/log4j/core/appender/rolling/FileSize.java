// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.regex.Matcher;
import java.text.ParseException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public final class FileSize
{
    private static final Logger LOGGER;
    private static final long KB = 1024L;
    private static final long MB = 1048576L;
    private static final long GB = 1073741824L;
    private static final Pattern VALUE_PATTERN;
    
    private FileSize() {
    }
    
    public static long parse(final String string, final long defaultValue) {
        final Matcher matcher = FileSize.VALUE_PATTERN.matcher(string);
        if (matcher.matches()) {
            try {
                final long value = NumberFormat.getNumberInstance(Locale.getDefault()).parse(matcher.group(1)).longValue();
                final String units = matcher.group(3);
                if (units.isEmpty()) {
                    return value;
                }
                if (units.equalsIgnoreCase("K")) {
                    return value * 1024L;
                }
                if (units.equalsIgnoreCase("M")) {
                    return value * 1048576L;
                }
                if (units.equalsIgnoreCase("G")) {
                    return value * 1073741824L;
                }
                FileSize.LOGGER.error("FileSize units not recognized: " + string);
                return defaultValue;
            }
            catch (final ParseException e) {
                FileSize.LOGGER.error("FileSize unable to parse numeric part: " + string, e);
                return defaultValue;
            }
        }
        FileSize.LOGGER.error("FileSize unable to parse bytes: " + string);
        return defaultValue;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
    }
}
