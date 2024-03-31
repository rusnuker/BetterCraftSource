// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.util.BitSet;

@Deprecated
final class CookieUtil
{
    private static final BitSet VALID_COOKIE_VALUE_OCTETS;
    private static final BitSet VALID_COOKIE_NAME_OCTETS;
    
    private static BitSet validCookieValueOctets() {
        final BitSet bits = new BitSet(8);
        for (int i = 35; i < 127; ++i) {
            bits.set(i);
        }
        bits.set(34, false);
        bits.set(44, false);
        bits.set(59, false);
        bits.set(92, false);
        return bits;
    }
    
    private static BitSet validCookieNameOctets(final BitSet validCookieValueOctets) {
        final BitSet bits = new BitSet(8);
        bits.or(validCookieValueOctets);
        bits.set(40, false);
        bits.set(41, false);
        bits.set(60, false);
        bits.set(62, false);
        bits.set(64, false);
        bits.set(58, false);
        bits.set(47, false);
        bits.set(91, false);
        bits.set(93, false);
        bits.set(63, false);
        bits.set(61, false);
        bits.set(123, false);
        bits.set(125, false);
        bits.set(32, false);
        bits.set(9, false);
        return bits;
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
    
    private CookieUtil() {
    }
    
    static {
        VALID_COOKIE_VALUE_OCTETS = validCookieValueOctets();
        VALID_COOKIE_NAME_OCTETS = validCookieNameOctets(CookieUtil.VALID_COOKIE_VALUE_OCTETS);
    }
}
