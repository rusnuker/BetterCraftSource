// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MapPatternConverter", category = "Converter")
@ConverterKeys({ "K", "map", "MAP" })
public final class MapPatternConverter extends LogEventPatternConverter
{
    private final String key;
    
    private MapPatternConverter(final String[] options) {
        super((options != null && options.length > 0) ? ("MAP{" + options[0] + '}') : "MAP", "map");
        this.key = ((options != null && options.length > 0) ? options[0] : null);
    }
    
    public static MapPatternConverter newInstance(final String[] options) {
        return new MapPatternConverter(options);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        if (event.getMessage() instanceof MapMessage) {
            final MapMessage msg = (MapMessage)event.getMessage();
            final IndexedReadOnlyStringMap sortedMap = msg.getIndexedReadOnlyStringMap();
            if (this.key == null) {
                if (sortedMap.isEmpty()) {
                    toAppendTo.append("{}");
                    return;
                }
                toAppendTo.append("{");
                for (int i = 0; i < sortedMap.size(); ++i) {
                    if (i > 0) {
                        toAppendTo.append(", ");
                    }
                    toAppendTo.append(sortedMap.getKeyAt(i)).append('=').append(sortedMap.getValueAt(i));
                }
                toAppendTo.append('}');
            }
            else {
                final String val = sortedMap.getValue(this.key);
                if (val != null) {
                    toAppendTo.append(val);
                }
            }
        }
    }
}
