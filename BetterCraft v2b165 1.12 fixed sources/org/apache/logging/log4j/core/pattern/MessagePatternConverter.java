// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.util.Loader;
import java.util.Locale;
import org.apache.logging.log4j.core.util.ArrayUtils;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MessagePatternConverter", category = "Converter")
@ConverterKeys({ "m", "msg", "message" })
@PerformanceSensitive({ "allocation" })
public final class MessagePatternConverter extends LogEventPatternConverter
{
    private static final String NOLOOKUPS = "nolookups";
    private final String[] formats;
    private final Configuration config;
    private final TextRenderer textRenderer;
    private final boolean noLookups;
    
    private MessagePatternConverter(final Configuration config, final String[] options) {
        super("Message", "message");
        this.formats = options;
        this.config = config;
        final int noLookupsIdx = this.loadNoLookups(options);
        this.noLookups = (noLookupsIdx >= 0);
        this.textRenderer = this.loadMessageRenderer((noLookupsIdx >= 0) ? ArrayUtils.remove(options, noLookupsIdx) : options);
    }
    
    private int loadNoLookups(final String[] options) {
        if (options != null) {
            for (int i = 0; i < options.length; ++i) {
                final String option = options[i];
                if ("nolookups".equalsIgnoreCase(option)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private TextRenderer loadMessageRenderer(final String[] options) {
        if (options != null) {
            final String[] arr$ = options;
            final int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                final String option = arr$[i$];
                final String upperCase = option.toUpperCase(Locale.ROOT);
                switch (upperCase) {
                    case "ANSI": {
                        if (Loader.isJansiAvailable()) {
                            return new JAnsiTextRenderer(options, JAnsiTextRenderer.DefaultMessageStyleMap);
                        }
                        StatusLogger.getLogger().warn("You requested ANSI message rendering but JANSI is not on the classpath.");
                        return null;
                    }
                    case "HTML": {
                        return new HtmlTextRenderer(options);
                    }
                    default: {
                        ++i$;
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    public static MessagePatternConverter newInstance(final Configuration config, final String[] options) {
        return new MessagePatternConverter(config, options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final Message msg = event.getMessage();
        if (msg instanceof StringBuilderFormattable) {
            final boolean doRender = this.textRenderer != null;
            final StringBuilder workingBuilder = doRender ? new StringBuilder(80) : toAppendTo;
            final StringBuilderFormattable stringBuilderFormattable = (StringBuilderFormattable)msg;
            final int offset = workingBuilder.length();
            stringBuilderFormattable.formatTo(workingBuilder);
            if (this.config != null && !this.noLookups) {
                for (int i = offset; i < workingBuilder.length() - 1; ++i) {
                    if (workingBuilder.charAt(i) == '$' && workingBuilder.charAt(i + 1) == '{') {
                        final String value = workingBuilder.substring(offset, workingBuilder.length());
                        workingBuilder.setLength(offset);
                        workingBuilder.append(this.config.getStrSubstitutor().replace(event, value));
                    }
                }
            }
            if (doRender) {
                this.textRenderer.render(workingBuilder, toAppendTo);
            }
            return;
        }
        if (msg != null) {
            String result;
            if (msg instanceof MultiformatMessage) {
                result = ((MultiformatMessage)msg).getFormattedMessage(this.formats);
            }
            else {
                result = msg.getFormattedMessage();
            }
            if (result != null) {
                toAppendTo.append((this.config != null && result.contains("${")) ? this.config.getStrSubstitutor().replace(event, result) : result);
            }
            else {
                toAppendTo.append("null");
            }
        }
    }
}
