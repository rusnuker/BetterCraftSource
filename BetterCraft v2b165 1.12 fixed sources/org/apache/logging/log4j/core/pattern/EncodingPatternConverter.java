// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.List;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "encode", category = "Converter")
@ConverterKeys({ "enc", "encode" })
@PerformanceSensitive({ "allocation" })
public final class EncodingPatternConverter extends LogEventPatternConverter
{
    private final List<PatternFormatter> formatters;
    
    private EncodingPatternConverter(final List<PatternFormatter> formatters) {
        super("encode", "encode");
        this.formatters = formatters;
    }
    
    public static EncodingPatternConverter newInstance(final Configuration config, final String[] options) {
        if (options.length != 1) {
            EncodingPatternConverter.LOGGER.error("Incorrect number of options on escape. Expected 1, received " + options.length);
            return null;
        }
        if (options[0] == null) {
            EncodingPatternConverter.LOGGER.error("No pattern supplied on escape");
            return null;
        }
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        return new EncodingPatternConverter(formatters);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final int start = toAppendTo.length();
        for (int i = 0; i < this.formatters.size(); ++i) {
            this.formatters.get(i).format(event, toAppendTo);
        }
        for (int i = toAppendTo.length() - 1; i >= start; --i) {
            final char c = toAppendTo.charAt(i);
            switch (c) {
                case '\r': {
                    toAppendTo.setCharAt(i, '\\');
                    toAppendTo.insert(i + 1, 'r');
                    break;
                }
                case '\n': {
                    toAppendTo.setCharAt(i, '\\');
                    toAppendTo.insert(i + 1, 'n');
                    break;
                }
                case '&': {
                    toAppendTo.setCharAt(i, '&');
                    toAppendTo.insert(i + 1, "amp;");
                    break;
                }
                case '<': {
                    toAppendTo.setCharAt(i, '&');
                    toAppendTo.insert(i + 1, "lt;");
                    break;
                }
                case '>': {
                    toAppendTo.setCharAt(i, '&');
                    toAppendTo.insert(i + 1, "gt;");
                    break;
                }
                case '\"': {
                    toAppendTo.setCharAt(i, '&');
                    toAppendTo.insert(i + 1, "quot;");
                    break;
                }
                case '\'': {
                    toAppendTo.setCharAt(i, '&');
                    toAppendTo.insert(i + 1, "apos;");
                    break;
                }
                case '/': {
                    toAppendTo.setCharAt(i, '&');
                    toAppendTo.insert(i + 1, "#x2F;");
                    break;
                }
            }
        }
    }
}
