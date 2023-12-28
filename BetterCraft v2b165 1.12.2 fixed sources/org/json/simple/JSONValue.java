// 
// Decompiled by Procyon v0.6.0
// 

package org.json.simple;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import java.io.Writer;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.StringReader;
import org.json.simple.parser.JSONParser;
import java.io.Reader;

public class JSONValue
{
    @Deprecated
    public static Object parse(final Reader in) {
        try {
            final JSONParser parser = new JSONParser();
            return parser.parse(in);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    @Deprecated
    public static Object parse(final String s) {
        final StringReader in = new StringReader(s);
        return parse(in);
    }
    
    public static Object parseWithException(final Reader in) throws IOException, ParseException {
        final JSONParser parser = new JSONParser();
        return parser.parse(in);
    }
    
    public static Object parseWithException(final String s) throws ParseException {
        final JSONParser parser = new JSONParser();
        return parser.parse(s);
    }
    
    public static void writeJSONString(final Object value, final Writer out) throws IOException {
        if (value == null) {
            out.write("null");
            return;
        }
        if (value instanceof String) {
            out.write(34);
            out.write(escape((String)value));
            out.write(34);
            return;
        }
        if (value instanceof Double) {
            if (((Double)value).isInfinite() || ((Double)value).isNaN()) {
                out.write("null");
            }
            else {
                out.write(value.toString());
            }
            return;
        }
        if (value instanceof Float) {
            if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
                out.write("null");
            }
            else {
                out.write(value.toString());
            }
            return;
        }
        if (value instanceof Number) {
            out.write(value.toString());
            return;
        }
        if (value instanceof Boolean) {
            out.write(value.toString());
            return;
        }
        if (value instanceof JSONStreamAware) {
            ((JSONStreamAware)value).writeJSONString(out);
            return;
        }
        if (value instanceof JSONAware) {
            out.write(((JSONAware)value).toJSONString());
            return;
        }
        if (value instanceof Map) {
            JSONObject.writeJSONString((Map)value, out);
            return;
        }
        if (value instanceof Collection) {
            JSONArray.writeJSONString((Collection)value, out);
            return;
        }
        if (value instanceof byte[]) {
            JSONArray.writeJSONString((byte[])value, out);
            return;
        }
        if (value instanceof short[]) {
            JSONArray.writeJSONString((short[])value, out);
            return;
        }
        if (value instanceof int[]) {
            JSONArray.writeJSONString((int[])value, out);
            return;
        }
        if (value instanceof long[]) {
            JSONArray.writeJSONString((long[])value, out);
            return;
        }
        if (value instanceof float[]) {
            JSONArray.writeJSONString((float[])value, out);
            return;
        }
        if (value instanceof double[]) {
            JSONArray.writeJSONString((double[])value, out);
            return;
        }
        if (value instanceof boolean[]) {
            JSONArray.writeJSONString((boolean[])value, out);
            return;
        }
        if (value instanceof char[]) {
            JSONArray.writeJSONString((char[])value, out);
            return;
        }
        if (value instanceof Object[]) {
            JSONArray.writeJSONString((Object[])value, out);
            return;
        }
        out.write(value.toString());
    }
    
    public static String toJSONString(final Object value) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(value, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String escape(final String s) {
        if (s == null) {
            return null;
        }
        final StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }
    
    static void escape(final String s, final StringBuffer sb) {
        for (int len = s.length(), i = 0; i < len; ++i) {
            final char ch = s.charAt(i);
            switch (ch) {
                case '\"': {
                    sb.append("\\\"");
                    break;
                }
                case '\\': {
                    sb.append("\\\\");
                    break;
                }
                case '\b': {
                    sb.append("\\b");
                    break;
                }
                case '\f': {
                    sb.append("\\f");
                    break;
                }
                case '\n': {
                    sb.append("\\n");
                    break;
                }
                case '\r': {
                    sb.append("\\r");
                    break;
                }
                case '\t': {
                    sb.append("\\t");
                    break;
                }
                case '/': {
                    sb.append("\\/");
                    break;
                }
                default: {
                    if ((ch >= '\0' && ch <= '\u001f') || (ch >= '\u007f' && ch <= '\u009f') || (ch >= '\u2000' && ch <= '\u20ff')) {
                        final String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); ++k) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                        break;
                    }
                    sb.append(ch);
                    break;
                }
            }
        }
    }
}
