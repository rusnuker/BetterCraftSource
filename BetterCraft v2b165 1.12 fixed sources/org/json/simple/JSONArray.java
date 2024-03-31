// 
// Decompiled by Procyon v0.6.0
// 

package org.json.simple;

import java.io.StringWriter;
import java.io.IOException;
import java.util.Iterator;
import java.io.Writer;
import java.util.Collection;
import java.util.ArrayList;

public class JSONArray extends ArrayList implements JSONAware, JSONStreamAware
{
    private static final long serialVersionUID = 3957988303675231981L;
    
    public JSONArray() {
    }
    
    public JSONArray(final Collection c) {
        super(c);
    }
    
    public static void writeJSONString(final Collection collection, final Writer out) throws IOException {
        if (collection == null) {
            out.write("null");
            return;
        }
        boolean first = true;
        final Iterator iter = collection.iterator();
        out.write(91);
        while (iter.hasNext()) {
            if (first) {
                first = false;
            }
            else {
                out.write(44);
            }
            final Object value = iter.next();
            if (value == null) {
                out.write("null");
            }
            else {
                JSONValue.writeJSONString(value, out);
            }
        }
        out.write(93);
    }
    
    @Override
    public void writeJSONString(final Writer out) throws IOException {
        writeJSONString(this, out);
    }
    
    public static String toJSONString(final Collection collection) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(collection, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final byte[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[");
            out.write(String.valueOf(array[0]));
            for (int i = 1; i < array.length; ++i) {
                out.write(",");
                out.write(String.valueOf(array[i]));
            }
            out.write("]");
        }
    }
    
    public static String toJSONString(final byte[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final short[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[");
            out.write(String.valueOf(array[0]));
            for (int i = 1; i < array.length; ++i) {
                out.write(",");
                out.write(String.valueOf(array[i]));
            }
            out.write("]");
        }
    }
    
    public static String toJSONString(final short[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final int[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[");
            out.write(String.valueOf(array[0]));
            for (int i = 1; i < array.length; ++i) {
                out.write(",");
                out.write(String.valueOf(array[i]));
            }
            out.write("]");
        }
    }
    
    public static String toJSONString(final int[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final long[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[");
            out.write(String.valueOf(array[0]));
            for (int i = 1; i < array.length; ++i) {
                out.write(",");
                out.write(String.valueOf(array[i]));
            }
            out.write("]");
        }
    }
    
    public static String toJSONString(final long[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final float[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[");
            out.write(String.valueOf(array[0]));
            for (int i = 1; i < array.length; ++i) {
                out.write(",");
                out.write(String.valueOf(array[i]));
            }
            out.write("]");
        }
    }
    
    public static String toJSONString(final float[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final double[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[");
            out.write(String.valueOf(array[0]));
            for (int i = 1; i < array.length; ++i) {
                out.write(",");
                out.write(String.valueOf(array[i]));
            }
            out.write("]");
        }
    }
    
    public static String toJSONString(final double[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final boolean[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[");
            out.write(String.valueOf(array[0]));
            for (int i = 1; i < array.length; ++i) {
                out.write(",");
                out.write(String.valueOf(array[i]));
            }
            out.write("]");
        }
    }
    
    public static String toJSONString(final boolean[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final char[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[\"");
            out.write(String.valueOf(array[0]));
            for (int i = 1; i < array.length; ++i) {
                out.write("\",\"");
                out.write(String.valueOf(array[i]));
            }
            out.write("\"]");
        }
    }
    
    public static String toJSONString(final char[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeJSONString(final Object[] array, final Writer out) throws IOException {
        if (array == null) {
            out.write("null");
        }
        else if (array.length == 0) {
            out.write("[]");
        }
        else {
            out.write("[");
            JSONValue.writeJSONString(array[0], out);
            for (int i = 1; i < array.length; ++i) {
                out.write(",");
                JSONValue.writeJSONString(array[i], out);
            }
            out.write("]");
        }
    }
    
    public static String toJSONString(final Object[] array) {
        final StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
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
}
