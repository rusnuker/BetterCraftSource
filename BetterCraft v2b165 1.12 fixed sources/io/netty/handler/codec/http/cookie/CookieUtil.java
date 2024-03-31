// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cookie;

import io.netty.util.internal.InternalThreadLocalMap;
import java.util.BitSet;

final class CookieUtil
{
    private static final BitSet VALID_COOKIE_NAME_OCTETS;
    private static final BitSet VALID_COOKIE_VALUE_OCTETS;
    private static final BitSet VALID_COOKIE_ATTRIBUTE_VALUE_OCTETS;
    
    private static BitSet validCookieNameOctets() {
        final BitSet bits = new BitSet();
        for (int i = 32; i < 127; ++i) {
            bits.set(i);
        }
        final int[] array;
        final int[] separators = array = new int[] { 40, 41, 60, 62, 64, 44, 59, 58, 92, 34, 47, 91, 93, 63, 61, 123, 125, 32, 9 };
        for (final int separator : array) {
            bits.set(separator, false);
        }
        return bits;
    }
    
    private static BitSet validCookieValueOctets() {
        final BitSet bits = new BitSet();
        bits.set(33);
        for (int i = 35; i <= 43; ++i) {
            bits.set(i);
        }
        for (int i = 45; i <= 58; ++i) {
            bits.set(i);
        }
        for (int i = 60; i <= 91; ++i) {
            bits.set(i);
        }
        for (int i = 93; i <= 126; ++i) {
            bits.set(i);
        }
        return bits;
    }
    
    private static BitSet validCookieAttributeValueOctets() {
        final BitSet bits = new BitSet();
        for (int i = 32; i < 127; ++i) {
            bits.set(i);
        }
        bits.set(59, false);
        return bits;
    }
    
    static StringBuilder stringBuilder() {
        return InternalThreadLocalMap.get().stringBuilder();
    }
    
    static String stripTrailingSeparatorOrNull(final StringBuilder buf) {
        return (buf.length() == 0) ? null : stripTrailingSeparator(buf);
    }
    
    static String stripTrailingSeparator(final StringBuilder buf) {
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 2);
        }
        return buf.toString();
    }
    
    static void add(final StringBuilder sb, final String name, final long val) {
        sb.append(name);
        sb.append('=');
        sb.append(val);
        sb.append(';');
        sb.append(' ');
    }
    
    static void add(final StringBuilder sb, final String name, final String val) {
        sb.append(name);
        sb.append('=');
        sb.append(val);
        sb.append(';');
        sb.append(' ');
    }
    
    static void add(final StringBuilder sb, final String name) {
        sb.append(name);
        sb.append(';');
        sb.append(' ');
    }
    
    static void addQuoted(final StringBuilder sb, final String name, String val) {
        if (val == null) {
            val = "";
        }
        sb.append(name);
        sb.append('=');
        sb.append('\"');
        sb.append(val);
        sb.append('\"');
        sb.append(';');
        sb.append(' ');
    }
    
    static int firstInvalidCookieNameOctet(final CharSequence cs) {
        return firstInvalidOctet(cs, CookieUtil.VALID_COOKIE_NAME_OCTETS);
    }
    
    static int firstInvalidCookieValueOctet(final CharSequence cs) {
        return firstInvalidOctet(cs, CookieUtil.VALID_COOKIE_VALUE_OCTETS);
    }
    
    static int firstInvalidOctet(final CharSequence cs, final BitSet bits) {
        for (int i = 0; i < cs.length(); ++i) {
            final char c = cs.charAt(i);
            if (!bits.get(c)) {
                return i;
            }
        }
        return -1;
    }
    
    static CharSequence unwrapValue(final CharSequence cs) {
        final int len = cs.length();
        if (len <= 0 || cs.charAt(0) != '\"') {
            return cs;
        }
        if (len >= 2 && cs.charAt(len - 1) == '\"') {
            return (len == 2) ? "" : cs.subSequence(1, len - 1);
        }
        return null;
    }
    
    static String validateAttributeValue(final String name, String value) {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.isEmpty()) {
            return null;
        }
        final int i = firstInvalidOctet(value, CookieUtil.VALID_COOKIE_ATTRIBUTE_VALUE_OCTETS);
        if (i != -1) {
            throw new IllegalArgumentException(name + " contains the prohibited characters: " + value.charAt(i));
        }
        return value;
    }
    
    private CookieUtil() {
    }
    
    static {
        VALID_COOKIE_NAME_OCTETS = validCookieNameOctets();
        VALID_COOKIE_VALUE_OCTETS = validCookieValueOctets();
        VALID_COOKIE_ATTRIBUTE_VALUE_OCTETS = validCookieAttributeValueOctets();
    }
}
