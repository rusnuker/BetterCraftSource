// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.io;

import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.SortedMap;
import java.nio.charset.Charset;

public class Charsets
{
    @Deprecated
    public static final Charset ISO_8859_1;
    @Deprecated
    public static final Charset US_ASCII;
    @Deprecated
    public static final Charset UTF_16;
    @Deprecated
    public static final Charset UTF_16BE;
    @Deprecated
    public static final Charset UTF_16LE;
    @Deprecated
    public static final Charset UTF_8;
    
    public static SortedMap<String, Charset> requiredCharsets() {
        final TreeMap<String, Charset> m = new TreeMap<String, Charset>(String.CASE_INSENSITIVE_ORDER);
        m.put(Charsets.ISO_8859_1.name(), Charsets.ISO_8859_1);
        m.put(Charsets.US_ASCII.name(), Charsets.US_ASCII);
        m.put(Charsets.UTF_16.name(), Charsets.UTF_16);
        m.put(Charsets.UTF_16BE.name(), Charsets.UTF_16BE);
        m.put(Charsets.UTF_16LE.name(), Charsets.UTF_16LE);
        m.put(Charsets.UTF_8.name(), Charsets.UTF_8);
        return Collections.unmodifiableSortedMap((SortedMap<String, ? extends Charset>)m);
    }
    
    public static Charset toCharset(final Charset charset) {
        return (charset == null) ? Charset.defaultCharset() : charset;
    }
    
    public static Charset toCharset(final String charset) {
        return (charset == null) ? Charset.defaultCharset() : Charset.forName(charset);
    }
    
    static {
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        US_ASCII = Charset.forName("US-ASCII");
        UTF_16 = Charset.forName("UTF-16");
        UTF_16BE = Charset.forName("UTF-16BE");
        UTF_16LE = Charset.forName("UTF-16LE");
        UTF_8 = Charset.forName("UTF-8");
    }
}
