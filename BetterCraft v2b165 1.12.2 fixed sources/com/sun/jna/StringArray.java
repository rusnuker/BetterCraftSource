// 
// Decompiled by Procyon v0.6.0
// 

package com.sun.jna;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class StringArray extends Memory implements Function.PostCallRead
{
    private String encoding;
    private List<NativeString> natives;
    private Object[] original;
    
    public StringArray(final String[] strings) {
        this(strings, false);
    }
    
    public StringArray(final String[] strings, final boolean wide) {
        this((Object[])strings, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
    }
    
    public StringArray(final String[] strings, final String encoding) {
        this((Object[])strings, encoding);
    }
    
    public StringArray(final WString[] strings) {
        this(strings, "--WIDE-STRING--");
    }
    
    private StringArray(final Object[] strings, final String encoding) {
        super((strings.length + 1) * Pointer.SIZE);
        this.natives = new ArrayList<NativeString>();
        this.original = strings;
        this.encoding = encoding;
        for (int i = 0; i < strings.length; ++i) {
            Pointer p = null;
            if (strings[i] != null) {
                final NativeString ns = new NativeString(strings[i].toString(), encoding);
                this.natives.add(ns);
                p = ns.getPointer();
            }
            this.setPointer(Pointer.SIZE * i, p);
        }
        this.setPointer(Pointer.SIZE * strings.length, null);
    }
    
    @Override
    public void read() {
        final boolean returnWide = this.original instanceof WString[];
        final boolean wide = "--WIDE-STRING--".equals(this.encoding);
        for (int si = 0; si < this.original.length; ++si) {
            final Pointer p = this.getPointer(si * Pointer.SIZE);
            Object s = null;
            if (p != null) {
                s = (wide ? p.getWideString(0L) : p.getString(0L, this.encoding));
                if (returnWide) {
                    s = new WString((String)s);
                }
            }
            this.original[si] = s;
        }
    }
    
    @Override
    public String toString() {
        final boolean wide = "--WIDE-STRING--".equals(this.encoding);
        String s = wide ? "const wchar_t*[]" : "const char*[]";
        s += Arrays.asList(this.original);
        return s;
    }
}
