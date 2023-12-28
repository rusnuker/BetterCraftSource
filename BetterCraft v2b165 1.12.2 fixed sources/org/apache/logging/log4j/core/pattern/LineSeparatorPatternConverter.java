// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LineSeparatorPatternConverter", category = "Converter")
@ConverterKeys({ "n" })
@PerformanceSensitive({ "allocation" })
public final class LineSeparatorPatternConverter extends LogEventPatternConverter
{
    private static final LineSeparatorPatternConverter INSTANCE;
    private final String lineSep;
    
    private LineSeparatorPatternConverter() {
        super("Line Sep", "lineSep");
        this.lineSep = Strings.LINE_SEPARATOR;
    }
    
    public static LineSeparatorPatternConverter newInstance(final String[] options) {
        return LineSeparatorPatternConverter.INSTANCE;
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(this.lineSep);
    }
    
    static {
        INSTANCE = new LineSeparatorPatternConverter();
    }
}
