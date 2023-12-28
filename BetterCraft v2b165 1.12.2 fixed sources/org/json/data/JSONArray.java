// 
// Decompiled by Procyon v0.6.0
// 

package org.json.data;

import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.lang.reflect.Array;
import java.io.Reader;
import java.util.ArrayList;

public class JSONArray
{
    private final ArrayList<Object> myArrayList;
    
    public JSONArray() {
        this.myArrayList = new ArrayList<Object>();
    }
    
    public JSONArray(final Reader reader) {
        this(new JSONTokener(reader));
    }
    
    protected JSONArray(final JSONTokener x) {
        this();
        if (x.nextClean() != '[') {
            throw new RuntimeException("A JSONArray text must start with '['");
        }
        if (x.nextClean() == ']') {
            return;
        }
        x.back();
        while (true) {
            if (x.nextClean() == ',') {
                x.back();
                this.myArrayList.add(JSONObject.NULL);
            }
            else {
                x.back();
                this.myArrayList.add(x.nextValue());
            }
            switch (x.nextClean()) {
                case ',':
                case ';': {
                    if (x.nextClean() == ']') {
                        return;
                    }
                    x.back();
                    continue;
                }
                case ']': {
                    return;
                }
                default: {
                    throw new RuntimeException("Expected a ',' or ']'");
                }
            }
        }
    }
    
    public static JSONArray parse(final String source) {
        try {
            return new JSONArray(new JSONTokener(source));
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    protected JSONArray(final Object array) {
        this();
        if (array.getClass().isArray()) {
            for (int length = Array.getLength(array), i = 0; i < length; ++i) {
                this.append(JSONObject.wrap(Array.get(array, i)));
            }
            return;
        }
        throw new RuntimeException("JSONArray initial value should be a string or collection or array.");
    }
    
    private Object opt(final int index) {
        if (index < 0 || index >= this.size()) {
            return null;
        }
        return this.myArrayList.get(index);
    }
    
    public Object get(final int index) {
        final Object object = this.opt(index);
        if (object == null) {
            throw new RuntimeException("JSONArray[" + index + "] not found.");
        }
        return object;
    }
    
    public String getString(final int index) {
        final Object object = this.get(index);
        if (object instanceof String) {
            return (String)object;
        }
        throw new RuntimeException("JSONArray[" + index + "] not a string.");
    }
    
    public String getString(final int index, final String defaultValue) {
        final Object object = this.opt(index);
        return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
    }
    
    public int getInt(final int index) {
        final Object object = this.get(index);
        try {
            return (object instanceof Number) ? ((Number)object).intValue() : Integer.parseInt((String)object);
        }
        catch (final Exception e) {
            throw new RuntimeException("JSONArray[" + index + "] is not a number.");
        }
    }
    
    public int getInt(final int index, final int defaultValue) {
        try {
            return this.getInt(index);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public long getLong(final int index) {
        final Object object = this.get(index);
        try {
            return (object instanceof Number) ? ((Number)object).longValue() : Long.parseLong((String)object);
        }
        catch (final Exception e) {
            throw new RuntimeException("JSONArray[" + index + "] is not a number.");
        }
    }
    
    public long getLong(final int index, final long defaultValue) {
        try {
            return this.getLong(index);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public float getFloat(final int index) {
        return (float)this.getDouble(index);
    }
    
    public float getFloat(final int index, final float defaultValue) {
        try {
            return this.getFloat(index);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public double getDouble(final int index) {
        final Object object = this.get(index);
        try {
            return (object instanceof Number) ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        }
        catch (final Exception e) {
            throw new RuntimeException("JSONArray[" + index + "] is not a number.");
        }
    }
    
    public double getDouble(final int index, final double defaultValue) {
        try {
            return this.getDouble(index);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public boolean getBoolean(final int index) {
        final Object object = this.get(index);
        if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object).equalsIgnoreCase("false"))) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new RuntimeException("JSONArray[" + index + "] is not a boolean.");
    }
    
    public boolean getBoolean(final int index, final boolean defaultValue) {
        try {
            return this.getBoolean(index);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public JSONArray getJSONArray(final int index) {
        final Object object = this.get(index);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        throw new RuntimeException("JSONArray[" + index + "] is not a JSONArray.");
    }
    
    public JSONArray getJSONArray(final int index, final JSONArray defaultValue) {
        try {
            return this.getJSONArray(index);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public JSONObject getJSONObject(final int index) {
        final Object object = this.get(index);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        }
        throw new RuntimeException("JSONArray[" + index + "] is not a JSONObject.");
    }
    
    public JSONObject getJSONObject(final int index, final JSONObject defaultValue) {
        try {
            return this.getJSONObject(index);
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public String[] getStringArray() {
        final String[] outgoing = new String[this.size()];
        for (int i = 0; i < this.size(); ++i) {
            outgoing[i] = this.getString(i);
        }
        return outgoing;
    }
    
    public int[] getIntArray() {
        final int[] outgoing = new int[this.size()];
        for (int i = 0; i < this.size(); ++i) {
            outgoing[i] = this.getInt(i);
        }
        return outgoing;
    }
    
    public long[] getLongArray() {
        final long[] outgoing = new long[this.size()];
        for (int i = 0; i < this.size(); ++i) {
            outgoing[i] = this.getLong(i);
        }
        return outgoing;
    }
    
    public float[] getFloatArray() {
        final float[] outgoing = new float[this.size()];
        for (int i = 0; i < this.size(); ++i) {
            outgoing[i] = this.getFloat(i);
        }
        return outgoing;
    }
    
    public double[] getDoubleArray() {
        final double[] outgoing = new double[this.size()];
        for (int i = 0; i < this.size(); ++i) {
            outgoing[i] = this.getDouble(i);
        }
        return outgoing;
    }
    
    public boolean[] getBooleanArray() {
        final boolean[] outgoing = new boolean[this.size()];
        for (int i = 0; i < this.size(); ++i) {
            outgoing[i] = this.getBoolean(i);
        }
        return outgoing;
    }
    
    public JSONArray append(final String value) {
        this.append((Object)value);
        return this;
    }
    
    public JSONArray append(final int value) {
        this.append((Object)value);
        return this;
    }
    
    public JSONArray append(final long value) {
        this.append((Object)value);
        return this;
    }
    
    public JSONArray append(final float value) {
        return this.append((double)value);
    }
    
    public JSONArray append(final double value) {
        final Double d = value;
        JSONObject.testValidity(d);
        this.append(d);
        return this;
    }
    
    public JSONArray append(final boolean value) {
        this.append(value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public JSONArray append(final JSONArray value) {
        this.myArrayList.add(value);
        return this;
    }
    
    public JSONArray append(final JSONObject value) {
        this.myArrayList.add(value);
        return this;
    }
    
    protected JSONArray append(final Object value) {
        this.myArrayList.add(value);
        return this;
    }
    
    public JSONArray setString(final int index, final String value) {
        this.set(index, value);
        return this;
    }
    
    public JSONArray setInt(final int index, final int value) {
        this.set(index, value);
        return this;
    }
    
    public JSONArray setLong(final int index, final long value) {
        return this.set(index, value);
    }
    
    public JSONArray setFloat(final int index, final float value) {
        return this.setDouble(index, value);
    }
    
    public JSONArray setDouble(final int index, final double value) {
        return this.set(index, value);
    }
    
    public JSONArray setBoolean(final int index, final boolean value) {
        return this.set(index, value ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONArray setJSONArray(final int index, final JSONArray value) {
        this.set(index, value);
        return this;
    }
    
    public JSONArray setJSONObject(final int index, final JSONObject value) {
        this.set(index, value);
        return this;
    }
    
    private JSONArray set(final int index, final Object value) {
        JSONObject.testValidity(value);
        if (index < 0) {
            throw new RuntimeException("JSONArray[" + index + "] not found.");
        }
        if (index < this.size()) {
            this.myArrayList.set(index, value);
        }
        else {
            while (index != this.size()) {
                this.append(JSONObject.NULL);
            }
            this.append(value);
        }
        return this;
    }
    
    public int size() {
        return this.myArrayList.size();
    }
    
    public boolean isNull(final int index) {
        return JSONObject.NULL.equals(this.opt(index));
    }
    
    public Object remove(final int index) {
        final Object o = this.opt(index);
        this.myArrayList.remove(index);
        return o;
    }
    
    public boolean save(final File file, final String options) {
        final PrintWriter writer = JSONObject.createWriter(file);
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
            final String[] opts = JSONObject.split(options, ',');
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
        final StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            final String string = this.writeInternal(sw, indentFactor, 0).toString();
            monitorexit(sw.getBuffer());
            return string;
        }
    }
    
    protected Writer writeInternal(final Writer writer, final int indentFactor, final int indent) {
        try {
            boolean commanate = false;
            final int length = this.size();
            writer.write(91);
            final int thisFactor = (indentFactor == -1) ? 0 : indentFactor;
            if (length == 1) {
                JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
            }
            else if (length != 0) {
                final int newIndent = indent + thisFactor;
                for (int i = 0; i < length; ++i) {
                    if (commanate) {
                        writer.write(44);
                    }
                    if (indentFactor != -1) {
                        writer.write(10);
                    }
                    JSONObject.indent(writer, newIndent);
                    JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newIndent);
                    commanate = true;
                }
                if (indentFactor != -1) {
                    writer.write(10);
                }
                JSONObject.indent(writer, indent);
            }
            writer.write(93);
            return writer;
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String join(final String separator) {
        final int len = this.size();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return sb.toString();
    }
}
