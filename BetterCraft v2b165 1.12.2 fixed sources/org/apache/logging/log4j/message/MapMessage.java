// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.EnglishEnums;
import java.util.Iterator;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.IndexedStringMap;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@AsynchronouslyFormattable
@PerformanceSensitive({ "allocation" })
public class MapMessage implements MultiformatMessage, StringBuilderFormattable
{
    private static final long serialVersionUID = -5031471831131487120L;
    private final IndexedStringMap data;
    
    public MapMessage() {
        this.data = new SortedArrayStringMap();
    }
    
    public MapMessage(final Map<String, String> map) {
        this.data = new SortedArrayStringMap(map);
    }
    
    @Override
    public String[] getFormats() {
        return MapFormat.names();
    }
    
    @Override
    public Object[] getParameters() {
        final Object[] result = new Object[this.data.size()];
        for (int i = 0; i < this.data.size(); ++i) {
            result[i] = this.data.getValueAt(i);
        }
        return result;
    }
    
    @Override
    public String getFormat() {
        return "";
    }
    
    public Map<String, String> getData() {
        final TreeMap<String, String> result = new TreeMap<String, String>();
        for (int i = 0; i < this.data.size(); ++i) {
            result.put(this.data.getKeyAt(i), this.data.getValueAt(i));
        }
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)result);
    }
    
    public IndexedReadOnlyStringMap getIndexedReadOnlyStringMap() {
        return this.data;
    }
    
    public void clear() {
        this.data.clear();
    }
    
    public MapMessage with(final String key, final String value) {
        this.put(key, value);
        return this;
    }
    
    public void put(final String key, final String value) {
        if (value == null) {
            throw new IllegalArgumentException("No value provided for key " + key);
        }
        this.validate(key, value);
        this.data.putValue(key, value);
    }
    
    protected void validate(final String key, final String value) {
    }
    
    public void putAll(final Map<String, String> map) {
        for (final Map.Entry<String, ?> entry : map.entrySet()) {
            this.data.putValue(entry.getKey(), entry.getValue());
        }
    }
    
    public String get(final String key) {
        return this.data.getValue(key);
    }
    
    public String remove(final String key) {
        final String result = this.data.getValue(key);
        this.data.remove(key);
        return result;
    }
    
    public String asString() {
        return this.format(null, new StringBuilder()).toString();
    }
    
    public String asString(final String format) {
        try {
            return this.format(EnglishEnums.valueOf(MapFormat.class, format), new StringBuilder()).toString();
        }
        catch (final IllegalArgumentException ex) {
            return this.asString();
        }
    }
    
    private StringBuilder format(final MapFormat format, final StringBuilder sb) {
        if (format == null) {
            this.appendMap(sb);
        }
        else {
            switch (format) {
                case XML: {
                    this.asXml(sb);
                    break;
                }
                case JSON: {
                    this.asJson(sb);
                    break;
                }
                case JAVA: {
                    this.asJava(sb);
                    break;
                }
                default: {
                    this.appendMap(sb);
                    break;
                }
            }
        }
        return sb;
    }
    
    public void asXml(final StringBuilder sb) {
        sb.append("<Map>\n");
        for (int i = 0; i < this.data.size(); ++i) {
            sb.append("  <Entry key=\"").append(this.data.getKeyAt(i)).append("\">").append(this.data.getValueAt(i)).append("</Entry>\n");
        }
        sb.append("</Map>");
    }
    
    @Override
    public String getFormattedMessage() {
        return this.asString();
    }
    
    @Override
    public String getFormattedMessage(final String[] formats) {
        if (formats == null || formats.length == 0) {
            return this.asString();
        }
        for (int i = 0; i < formats.length; ++i) {
            final MapFormat mapFormat = MapFormat.lookupIgnoreCase(formats[i]);
            if (mapFormat != null) {
                return this.format(mapFormat, new StringBuilder()).toString();
            }
        }
        return this.asString();
    }
    
    protected void appendMap(final StringBuilder sb) {
        for (int i = 0; i < this.data.size(); ++i) {
            if (i > 0) {
                sb.append(' ');
            }
            StringBuilders.appendKeyDqValue(sb, this.data.getKeyAt(i), this.data.getValueAt(i));
        }
    }
    
    protected void asJson(final StringBuilder sb) {
        sb.append('{');
        for (int i = 0; i < this.data.size(); ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            StringBuilders.appendDqValue(sb, this.data.getKeyAt(i)).append(':');
            StringBuilders.appendDqValue(sb, this.data.getValueAt(i));
        }
        sb.append('}');
    }
    
    protected void asJava(final StringBuilder sb) {
        sb.append('{');
        for (int i = 0; i < this.data.size(); ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            StringBuilders.appendKeyDqValue(sb, this.data.getKeyAt(i), this.data.getValueAt(i));
        }
        sb.append('}');
    }
    
    public MapMessage newInstance(final Map<String, String> map) {
        return new MapMessage(map);
    }
    
    @Override
    public String toString() {
        return this.asString();
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        this.format(null, buffer);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MapMessage that = (MapMessage)o;
        return this.data.equals(that.data);
    }
    
    @Override
    public int hashCode() {
        return this.data.hashCode();
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    public enum MapFormat
    {
        XML, 
        JSON, 
        JAVA;
        
        public static MapFormat lookupIgnoreCase(final String format) {
            return MapFormat.XML.name().equalsIgnoreCase(format) ? MapFormat.XML : (MapFormat.JSON.name().equalsIgnoreCase(format) ? MapFormat.JSON : (MapFormat.JAVA.name().equalsIgnoreCase(format) ? MapFormat.JAVA : null));
        }
        
        public static String[] names() {
            return new String[] { MapFormat.XML.name(), MapFormat.JSON.name(), MapFormat.JAVA.name() };
        }
    }
}
