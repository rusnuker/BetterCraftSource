// 
// Decompiled by Procyon v0.6.0
// 

package org.json.data;

import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.io.Reader;
import java.util.HashMap;

public class JSONObject
{
    private static final int keyPoolSize = 100;
    private static HashMap<String, Object> keyPool;
    private final HashMap<String, Object> map;
    public static final Object NULL;
    
    static {
        JSONObject.keyPool = new HashMap<String, Object>(100);
        NULL = new Null(null);
    }
    
    public JSONObject() {
        this.map = new HashMap<String, Object>();
    }
    
    public JSONObject(final Reader reader) {
        this(new JSONTokener(reader));
    }
    
    protected JSONObject(final JSONTokener x) {
        this();
        if (x.nextClean() != '{') {
            throw new RuntimeException("A JSONObject text must begin with '{'");
        }
        while (true) {
            char c = x.nextClean();
            switch (c) {
                case '\0': {
                    throw new RuntimeException("A JSONObject text must end with '}'");
                }
                case '}': {
                    return;
                }
                default: {
                    x.back();
                    final String key = x.nextValue().toString();
                    c = x.nextClean();
                    if (c == '=') {
                        if (x.next() != '>') {
                            x.back();
                        }
                    }
                    else if (c != ':') {
                        throw new RuntimeException("Expected a ':' after a key");
                    }
                    this.putOnce(key, x.nextValue());
                    switch (x.nextClean()) {
                        case ',':
                        case ';': {
                            if (x.nextClean() == '}') {
                                return;
                            }
                            x.back();
                            continue;
                        }
                        case '}': {
                            return;
                        }
                        default: {
                            throw new RuntimeException("Expected a ',' or '}'");
                        }
                    }
                    break;
                }
            }
        }
    }
    
    protected JSONObject(final HashMap<String, Object> map) {
        this.map = new HashMap<String, Object>();
        if (map != null) {
            for (final Map.Entry e : map.entrySet()) {
                final Object value = e.getValue();
                if (value != null) {
                    map.put(e.getKey(), wrap(value));
                }
            }
        }
    }
    
    public JSONObject(final Object bean) {
        this();
        this.populateMap(bean);
    }
    
    public static JSONObject parse(final String source) {
        return new JSONObject(new JSONTokener(source));
    }
    
    protected static String doubleToString(final double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "null";
        }
        String string = Double.toString(d);
        if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while (string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }
            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }
    
    public Object get(final String key) {
        if (key == null) {
            throw new RuntimeException("JSONObject.get(null) called");
        }
        final Object object = this.opt(key);
        return object;
    }
    
    public String getString(final String key) {
        final Object object = this.get(key);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String)object;
        }
        throw new RuntimeException("JSONObject[" + quote(key) + "] is not a string");
    }
    
    public String getString(final String key, final String defaultValue) {
        final Object object = this.opt(key);
        return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
    }
    
    public int getInt(final String key) {
        final Object object = this.get(key);
        if (object == null) {
            throw new RuntimeException("JSONObject[" + quote(key) + "] not found");
        }
        try {
            return (object instanceof Number) ? ((Number)object).intValue() : Integer.parseInt((String)object);
        }
        catch (final Exception e) {
            throw new RuntimeException("JSONObject[" + quote(key) + "] is not an int.");
        }
    }
    
    public int getInt(final String key, final int defaultValue) {
        try {
            return this.getInt(key);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public long getLong(final String key) {
        final Object object = this.get(key);
        try {
            return (object instanceof Number) ? ((Number)object).longValue() : Long.parseLong((String)object);
        }
        catch (final Exception e) {
            throw new RuntimeException("JSONObject[" + quote(key) + "] is not a long.", e);
        }
    }
    
    public long getLong(final String key, final long defaultValue) {
        try {
            return this.getLong(key);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public float getFloat(final String key) {
        return (float)this.getDouble(key);
    }
    
    public float getFloat(final String key, final float defaultValue) {
        try {
            return this.getFloat(key);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public double getDouble(final String key) {
        final Object object = this.get(key);
        try {
            return (object instanceof Number) ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        }
        catch (final Exception e) {
            throw new RuntimeException("JSONObject[" + quote(key) + "] is not a number.");
        }
    }
    
    public double getDouble(final String key, final double defaultValue) {
        try {
            return this.getDouble(key);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public boolean getBoolean(final String key) {
        final Object object = this.get(key);
        if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object).equalsIgnoreCase("false"))) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new RuntimeException("JSONObject[" + quote(key) + "] is not a Boolean.");
    }
    
    public boolean getBoolean(final String key, final boolean defaultValue) {
        try {
            return this.getBoolean(key);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public JSONArray getJSONArray(final String key) {
        final Object object = this.get(key);
        if (object == null) {
            return null;
        }
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        throw new RuntimeException("JSONObject[" + quote(key) + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final String key) {
        final Object object = this.get(key);
        if (object == null) {
            return null;
        }
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        }
        throw new RuntimeException("JSONObject[" + quote(key) + "] is not a JSONObject.");
    }
    
    public boolean hasKey(final String key) {
        return this.map.containsKey(key);
    }
    
    public boolean isNull(final String key) {
        return JSONObject.NULL.equals(this.opt(key));
    }
    
    public Iterator keyIterator() {
        return this.map.keySet().iterator();
    }
    
    public Set keys() {
        return this.map.keySet();
    }
    
    public int size() {
        return this.map.size();
    }
    
    private static String numberToString(final Number number) {
        if (number == null) {
            throw new RuntimeException("Null pointer");
        }
        testValidity(number);
        String string = number.toString();
        if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while (string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }
            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }
    
    private Object opt(final String key) {
        return (key == null) ? null : this.map.get(key);
    }
    
    private void populateMap(final Object bean) {
        final Class klass = bean.getClass();
        final boolean includeSuperClass = klass.getClassLoader() != null;
        final Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            try {
                final Method method = methods[i];
                if (Modifier.isPublic(method.getModifiers())) {
                    final String name = method.getName();
                    String key = "";
                    if (name.startsWith("get")) {
                        if ("getClass".equals(name) || "getDeclaringClass".equals(name)) {
                            key = "";
                        }
                        else {
                            key = name.substring(3);
                        }
                    }
                    else if (name.startsWith("is")) {
                        key = name.substring(2);
                    }
                    if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        }
                        else if (!Character.isUpperCase(key.charAt(1))) {
                            key = String.valueOf(key.substring(0, 1).toLowerCase()) + key.substring(1);
                        }
                        final Object result = method.invoke(bean, (Object[])null);
                        if (result != null) {
                            this.map.put(key, wrap(result));
                        }
                    }
                }
            }
            catch (final Exception ex) {}
        }
    }
    
    public JSONObject setString(final String key, final String value) {
        return this.put(key, value);
    }
    
    public JSONObject setInt(final String key, final int value) {
        this.put(key, value);
        return this;
    }
    
    public JSONObject setLong(final String key, final long value) {
        this.put(key, value);
        return this;
    }
    
    public JSONObject setFloat(final String key, final float value) {
        this.put(key, (double)value);
        return this;
    }
    
    public JSONObject setDouble(final String key, final double value) {
        this.put(key, value);
        return this;
    }
    
    public JSONObject setBoolean(final String key, final boolean value) {
        this.put(key, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public JSONObject setJSONObject(final String key, final JSONObject value) {
        return this.put(key, value);
    }
    
    public JSONObject setJSONArray(final String key, final JSONArray value) {
        return this.put(key, value);
    }
    
    public JSONObject put(String key, final Object value) {
        if (key == null) {
            throw new RuntimeException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            final String pooled = JSONObject.keyPool.get(key);
            if (pooled == null) {
                if (JSONObject.keyPool.size() >= 100) {
                    JSONObject.keyPool = new HashMap<String, Object>(100);
                }
                JSONObject.keyPool.put(key, key);
            }
            else {
                key = pooled;
            }
            this.map.put(key, value);
        }
        else {
            this.remove(key);
        }
        return this;
    }
    
    private JSONObject putOnce(final String key, final Object value) {
        if (key != null && value != null) {
            if (this.opt(key) != null) {
                throw new RuntimeException("Duplicate key \"" + key + "\"");
            }
            this.put(key, value);
        }
        return this;
    }
    
    public static String quote(final String string) {
        final StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            try {
                final String string2 = quote(string, sw).toString();
                monitorexit(sw.getBuffer());
                return string2;
            }
            catch (final IOException ignored) {
                monitorexit(sw.getBuffer());
                return "";
            }
        }
    }
    
    public static Writer quote(final String string, final Writer w) throws IOException {
        if (string == null || string.length() == 0) {
            w.write("\"\"");
            return w;
        }
        char c = '\0';
        final int len = string.length();
        w.write(34);
        for (int i = 0; i < len; ++i) {
            final char b = c;
            c = string.charAt(i);
            switch (c) {
                case '\"':
                case '\\': {
                    w.write(92);
                    w.write(c);
                    break;
                }
                case '/': {
                    if (b == '<') {
                        w.write(92);
                    }
                    w.write(c);
                    break;
                }
                case '\b': {
                    w.write("\\b");
                    break;
                }
                case '\t': {
                    w.write("\\t");
                    break;
                }
                case '\n': {
                    w.write("\\n");
                    break;
                }
                case '\f': {
                    w.write("\\f");
                    break;
                }
                case '\r': {
                    w.write("\\r");
                    break;
                }
                default: {
                    if (c < ' ' || (c >= '\u0080' && c < ' ') || (c >= '\u2000' && c < '\u2100')) {
                        w.write("\\u");
                        final String hhhh = Integer.toHexString(c);
                        w.write("0000", 0, 4 - hhhh.length());
                        w.write(hhhh);
                        break;
                    }
                    w.write(c);
                    break;
                }
            }
        }
        w.write(34);
        return w;
    }
    
    public Object remove(final String key) {
        return this.map.remove(key);
    }
    
    protected static Object stringToValue(final String string) {
        if (string.equals("")) {
            return string;
        }
        if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("null")) {
            return JSONObject.NULL;
        }
        final char b = string.charAt(0);
        if ((b < '0' || b > '9') && b != '.' && b != '-') {
            if (b != '+') {
                return string;
            }
        }
        try {
            if (string.indexOf(46) > -1 || string.indexOf(101) > -1 || string.indexOf(69) > -1) {
                final Double d = Double.valueOf(string);
                if (!d.isInfinite() && !d.isNaN()) {
                    return d;
                }
            }
            else {
                final Long myLong = Long.valueOf(string);
                if (myLong == myLong.intValue()) {
                    return myLong.intValue();
                }
                return myLong;
            }
        }
        catch (final Exception ex) {}
        return string;
    }
    
    protected static void testValidity(final Object o) {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
                    throw new RuntimeException("JSON does not allow non-finite numbers.");
                }
            }
            else if (o instanceof Float && (((Float)o).isInfinite() || ((Float)o).isNaN())) {
                throw new RuntimeException("JSON does not allow non-finite numbers.");
            }
        }
    }
    
    public boolean save(final File file, final String options) {
        final PrintWriter writer = createWriter(file);
        final boolean success = this.write(writer, options);
        writer.close();
        return success;
    }
    
    public boolean write(final PrintWriter output) {
        return this.write(output, null);
    }
    
    public boolean write(final PrintWriter output, final String options) {
        int indentFactor = 2;
        if (options != null) {
            final String[] opts = split(options, ',');
            String[] array;
            for (int length = (array = opts).length, i = 0; i < length; ++i) {
                final String opt = array[i];
                if (opt.equals("compact")) {
                    indentFactor = -1;
                }
                else if (opt.startsWith("indent=")) {
                    indentFactor = Integer.parseInt(opt.substring(7), -2);
                    if (indentFactor == -2) {
                        throw new IllegalArgumentException("Could not read a number from " + opt);
                    }
                }
                else {
                    System.err.println("Ignoring " + opt);
                }
            }
        }
        output.print(this.format(indentFactor));
        output.flush();
        return true;
    }
    
    @Override
    public String toString() {
        try {
            return this.format(2);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public String format(final int indentFactor) {
        final StringWriter w = new StringWriter();
        synchronized (w.getBuffer()) {
            final String string = this.writeInternal(w, indentFactor, 0).toString();
            monitorexit(w.getBuffer());
            return string;
        }
    }
    
    protected static String valueToString(final Object value) {
        if (value == null || value.equals(null)) {
            return "null";
        }
        if (value instanceof Number) {
            return numberToString((Number)value);
        }
        if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
            return value.toString();
        }
        if (value instanceof Map) {
            return new JSONObject(value).toString();
        }
        if (value instanceof Collection) {
            return new JSONArray(value).toString();
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value).toString();
        }
        return quote(value.toString());
    }
    
    protected static Object wrap(final Object object) {
        try {
            if (object == null) {
                return JSONObject.NULL;
            }
            if (object instanceof JSONObject || object instanceof JSONArray || JSONObject.NULL.equals(object) || object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Float || object instanceof Double || object instanceof String) {
                return object;
            }
            if (object instanceof Collection) {
                return new JSONArray(object);
            }
            if (object.getClass().isArray()) {
                return new JSONArray(object);
            }
            if (object instanceof Map) {
                return new JSONObject(object);
            }
            final Package objectPackage = object.getClass().getPackage();
            final String objectPackageName = (objectPackage != null) ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new JSONObject(object);
        }
        catch (final Exception exception) {
            return null;
        }
    }
    
    static final Writer writeValue(final Writer writer, final Object value, final int indentFactor, final int indent) throws IOException {
        if (value == null || value.equals(null)) {
            writer.write("null");
        }
        else if (value instanceof JSONObject) {
            ((JSONObject)value).writeInternal(writer, indentFactor, indent);
        }
        else if (value instanceof JSONArray) {
            ((JSONArray)value).writeInternal(writer, indentFactor, indent);
        }
        else if (value instanceof Map) {
            new JSONObject(value).writeInternal(writer, indentFactor, indent);
        }
        else if (value instanceof Collection) {
            new JSONArray(value).writeInternal(writer, indentFactor, indent);
        }
        else if (value.getClass().isArray()) {
            new JSONArray(value).writeInternal(writer, indentFactor, indent);
        }
        else if (value instanceof Number) {
            writer.write(numberToString((Number)value));
        }
        else if (value instanceof Boolean) {
            writer.write(value.toString());
        }
        else {
            quote(value.toString(), writer);
        }
        return writer;
    }
    
    static final void indent(final Writer writer, final int indent) throws IOException {
        for (int i = 0; i < indent; ++i) {
            writer.write(32);
        }
    }
    
    protected Writer writeInternal(final Writer writer, final int indentFactor, final int indent) {
        try {
            boolean commanate = false;
            final int length = this.size();
            final Iterator keys = this.keyIterator();
            writer.write(123);
            final int actualFactor = (indentFactor == -1) ? 0 : indentFactor;
            if (length == 1) {
                final Object key = keys.next();
                writer.write(quote(key.toString()));
                writer.write(58);
                if (actualFactor > 0) {
                    writer.write(32);
                }
                writeValue(writer, this.map.get(key), indentFactor, indent);
            }
            else if (length != 0) {
                final int newIndent = indent + actualFactor;
                while (keys.hasNext()) {
                    final Object key2 = keys.next();
                    if (commanate) {
                        writer.write(44);
                    }
                    if (indentFactor != -1) {
                        writer.write(10);
                    }
                    indent(writer, newIndent);
                    writer.write(quote(key2.toString()));
                    writer.write(58);
                    if (actualFactor > 0) {
                        writer.write(32);
                    }
                    writeValue(writer, this.map.get(key2), indentFactor, newIndent);
                    commanate = true;
                }
                if (indentFactor != -1) {
                    writer.write(10);
                }
                indent(writer, indent);
            }
            writer.write(125);
            return writer;
        }
        catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public static String[] split(final String value, final char delim) {
        if (value == null) {
            return null;
        }
        final char[] chars = value.toCharArray();
        int splitCount = 0;
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == delim) {
                ++splitCount;
            }
        }
        if (splitCount == 0) {
            final String[] splits = { value };
            return splits;
        }
        final String[] splits = new String[splitCount + 1];
        int splitIndex = 0;
        int startIndex = 0;
        for (int j = 0; j < chars.length; ++j) {
            if (chars[j] == delim) {
                splits[splitIndex++] = new String(chars, startIndex, j - startIndex);
                startIndex = j + 1;
            }
        }
        splits[splitIndex] = new String(chars, startIndex, chars.length - startIndex);
        return splits;
    }
    
    public static PrintWriter createWriter(final File file) {
        try {
            final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file), 8192);
            final OutputStreamWriter osw = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
            return new PrintWriter(osw);
        }
        catch (final FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static final class Null
    {
        @Override
        protected final Object clone() {
            return this;
        }
        
        @Override
        public boolean equals(final Object object) {
            return object == null || object == this;
        }
        
        @Override
        public String toString() {
            return "null";
        }
        
        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
