// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import java.util.Locale;
import org.apache.logging.log4j.core.util.Patterns;
import java.util.HashMap;
import org.apache.logging.log4j.Level;
import java.util.Map;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LevelPatternConverter", category = "Converter")
@ConverterKeys({ "p", "level" })
@PerformanceSensitive({ "allocation" })
public final class LevelPatternConverter extends LogEventPatternConverter
{
    private static final String OPTION_LENGTH = "length";
    private static final String OPTION_LOWER = "lowerCase";
    private static final LevelPatternConverter INSTANCE;
    private final Map<Level, String> levelMap;
    
    private LevelPatternConverter(final Map<Level, String> map) {
        super("Level", "level");
        this.levelMap = map;
    }
    
    public static LevelPatternConverter newInstance(final String[] options) {
        if (options == null || options.length == 0) {
            return LevelPatternConverter.INSTANCE;
        }
        final Map<Level, String> levelMap = new HashMap<Level, String>();
        int length = Integer.MAX_VALUE;
        boolean lowerCase = false;
        final String[] arr$;
        final String[] definitions = arr$ = options[0].split(Patterns.COMMA_SEPARATOR);
        for (final String def : arr$) {
            final String[] pair = def.split("=");
            if (pair == null || pair.length != 2) {
                LevelPatternConverter.LOGGER.error("Invalid option {}", def);
            }
            else {
                final String key = pair[0].trim();
                final String value = pair[1].trim();
                if ("length".equalsIgnoreCase(key)) {
                    length = Integer.parseInt(value);
                }
                else if ("lowerCase".equalsIgnoreCase(key)) {
                    lowerCase = Boolean.parseBoolean(value);
                }
                else {
                    final Level level = Level.toLevel(key, null);
                    if (level == null) {
                        LevelPatternConverter.LOGGER.error("Invalid Level {}", key);
                    }
                    else {
                        levelMap.put(level, value);
                    }
                }
            }
        }
        if (levelMap.isEmpty() && length == Integer.MAX_VALUE && !lowerCase) {
            return LevelPatternConverter.INSTANCE;
        }
        for (final Level level2 : Level.values()) {
            if (!levelMap.containsKey(level2)) {
                final String left = left(level2, length);
                levelMap.put(level2, lowerCase ? left.toLowerCase(Locale.US) : left);
            }
        }
        return new LevelPatternConverter(levelMap);
    }
    
    private static String left(final Level level, final int length) {
        final String string = level.toString();
        if (length >= string.length()) {
            return string;
        }
        return string.substring(0, length);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
        output.append((this.levelMap == null) ? event.getLevel().toString() : this.levelMap.get(event.getLevel()));
    }
    
    @Override
    public String getStyleClass(final Object e) {
        if (e instanceof LogEvent) {
            return "level " + ((LogEvent)e).getLevel().name().toLowerCase(Locale.ENGLISH);
        }
        return "level";
    }
    
    static {
        INSTANCE = new LevelPatternConverter(null);
    }
}
