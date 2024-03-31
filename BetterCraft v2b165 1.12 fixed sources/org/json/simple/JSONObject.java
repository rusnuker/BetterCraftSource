// 
// Decompiled by Procyon v0.6.0
// 

package org.json.simple;

import java.io.StringWriter;
import java.io.IOException;
import java.util.Iterator;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;

public class JSONObject extends HashMap implements Map, JSONAware, JSONStreamAware
{
    private static final long serialVersionUID = -503443796854799292L;
    
    public JSONObject() {
    }
    
    public JSONObject(final Map map) {
        super(map);
    }
    
    public static void writeJSONString(final Map map, final Writer out) throws IOException {
        if (map == null) {
            out.write("null");
            return;
        }
        boolean first = true;
        final Iterator iter = map.entrySet().iterator();
        out.write(123);
        while (iter.hasNext()) {
            if (first) {
                first = false;
            }
            else {
                out.write(44);
            }
            final Entry entry = iter.next();
            out.write(34);
            out.write(escape(String.valueOf(entry.getKey())));
            out.write(34);
            out.write(58);
            JSONValue.writeJSONString(entry.getValue(), out);
        }
        out.write(125);
    }
    
    @Override
    public void writeJSONString(final Writer out) throws IOException {
        writeJSONString(this, out);
    }
    
    public static String toJSONString(final Map map) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(map, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String toJSONString() {
        return toJSONString(this);
    }
    
    @Override
    public String toString() {
        return this.toJSONString();
    }
    
    public static String toString(final String key, final Object value) {
        final StringBuffer sb = new StringBuffer();
        sb.append('\"');
        if (key == null) {
            sb.append("null");
        }
        else {
            JSONValue.escape(key, sb);
        }
        sb.append('\"').append(':');
        sb.append(JSONValue.toJSONString(value));
        return sb.toString();
    }
    
    public static String escape(final String s) {
        return JSONValue.escape(s);
    }
}
