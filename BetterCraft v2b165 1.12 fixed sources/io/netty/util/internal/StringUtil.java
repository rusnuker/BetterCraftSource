// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public final class StringUtil
{
    public static final String EMPTY_STRING = "";
    public static final String NEWLINE;
    public static final char DOUBLE_QUOTE = '\"';
    public static final char COMMA = ',';
    public static final char LINE_FEED = '\n';
    public static final char CARRIAGE_RETURN = '\r';
    public static final char TAB = '\t';
    private static final String[] BYTE2HEX_PAD;
    private static final String[] BYTE2HEX_NOPAD;
    private static final int CSV_NUMBER_ESCAPE_CHARACTERS = 7;
    private static final char PACKAGE_SEPARATOR_CHAR = '.';
    
    private StringUtil() {
    }
    
    public static String substringAfter(final String value, final char delim) {
        final int pos = value.indexOf(delim);
        if (pos >= 0) {
            return value.substring(pos + 1);
        }
        return null;
    }
    
    public static boolean commonSuffixOfLength(final String s, final String p, final int len) {
        return s != null && p != null && len >= 0 && s.regionMatches(s.length() - len, p, p.length() - len, len);
    }
    
    public static String byteToHexStringPadded(final int value) {
        return StringUtil.BYTE2HEX_PAD[value & 0xFF];
    }
    
    public static <T extends Appendable> T byteToHexStringPadded(final T buf, final int value) {
        try {
            buf.append(byteToHexStringPadded(value));
        }
        catch (final IOException e) {
            PlatformDependent.throwException(e);
        }
        return buf;
    }
    
    public static String toHexStringPadded(final byte[] src) {
        return toHexStringPadded(src, 0, src.length);
    }
    
    public static String toHexStringPadded(final byte[] src, final int offset, final int length) {
        return toHexStringPadded(new StringBuilder(length << 1), src, offset, length).toString();
    }
    
    public static <T extends Appendable> T toHexStringPadded(final T dst, final byte[] src) {
        return toHexStringPadded(dst, src, 0, src.length);
    }
    
    public static <T extends Appendable> T toHexStringPadded(final T dst, final byte[] src, final int offset, final int length) {
        for (int end = offset + length, i = offset; i < end; ++i) {
            byteToHexStringPadded(dst, src[i]);
        }
        return dst;
    }
    
    public static String byteToHexString(final int value) {
        return StringUtil.BYTE2HEX_NOPAD[value & 0xFF];
    }
    
    public static <T extends Appendable> T byteToHexString(final T buf, final int value) {
        try {
            buf.append(byteToHexString(value));
        }
        catch (final IOException e) {
            PlatformDependent.throwException(e);
        }
        return buf;
    }
    
    public static String toHexString(final byte[] src) {
        return toHexString(src, 0, src.length);
    }
    
    public static String toHexString(final byte[] src, final int offset, final int length) {
        return toHexString(new StringBuilder(length << 1), src, offset, length).toString();
    }
    
    public static <T extends Appendable> T toHexString(final T dst, final byte[] src) {
        return toHexString(dst, src, 0, src.length);
    }
    
    public static <T extends Appendable> T toHexString(final T dst, final byte[] src, final int offset, final int length) {
        assert length >= 0;
        if (length == 0) {
            return dst;
        }
        final int end = offset + length;
        int endMinusOne;
        int i;
        for (endMinusOne = end - 1, i = offset; i < endMinusOne && src[i] == 0; ++i) {}
        byteToHexString(dst, src[i++]);
        final int remaining = end - i;
        toHexStringPadded((Appendable)dst, src, i, remaining);
        return dst;
    }
    
    public static String simpleClassName(final Object o) {
        if (o == null) {
            return "null_object";
        }
        return simpleClassName(o.getClass());
    }
    
    public static String simpleClassName(final Class<?> clazz) {
        final String className = ObjectUtil.checkNotNull(clazz, "clazz").getName();
        final int lastDotIdx = className.lastIndexOf(46);
        if (lastDotIdx > -1) {
            return className.substring(lastDotIdx + 1);
        }
        return className;
    }
    
    public static CharSequence escapeCsv(final CharSequence value) {
        final int length = ObjectUtil.checkNotNull(value, "value").length();
        if (length == 0) {
            return value;
        }
        final int last = length - 1;
        final boolean quoted = isDoubleQuote(value.charAt(0)) && isDoubleQuote(value.charAt(last)) && length != 1;
        boolean foundSpecialCharacter = false;
        boolean escapedDoubleQuote = false;
        final StringBuilder escaped = new StringBuilder(length + 7).append('\"');
        for (int i = 0; i < length; ++i) {
            final char current = value.charAt(i);
            switch (current) {
                case '\"': {
                    if (i == 0 || i == last) {
                        if (!quoted) {
                            escaped.append('\"');
                        }
                        continue;
                    }
                    else {
                        final boolean isNextCharDoubleQuote = isDoubleQuote(value.charAt(i + 1));
                        if (!isDoubleQuote(value.charAt(i - 1)) && (!isNextCharDoubleQuote || i + 1 == last)) {
                            escaped.append('\"');
                            escapedDoubleQuote = true;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case '\n':
                case '\r':
                case ',': {
                    foundSpecialCharacter = true;
                    break;
                }
            }
            escaped.append(current);
        }
        return (escapedDoubleQuote || (foundSpecialCharacter && !quoted)) ? escaped.append('\"') : value;
    }
    
    public static CharSequence unescapeCsv(final CharSequence value) {
        final int length = ObjectUtil.checkNotNull(value, "value").length();
        if (length == 0) {
            return value;
        }
        final int last = length - 1;
        final boolean quoted = isDoubleQuote(value.charAt(0)) && isDoubleQuote(value.charAt(last)) && length != 1;
        if (!quoted) {
            validateCsvFormat(value);
            return value;
        }
        final StringBuilder unescaped = InternalThreadLocalMap.get().stringBuilder();
        for (int i = 1; i < last; ++i) {
            final char current = value.charAt(i);
            if (current == '\"') {
                if (!isDoubleQuote(value.charAt(i + 1)) || i + 1 == last) {
                    throw newInvalidEscapedCsvFieldException(value, i);
                }
                ++i;
            }
            unescaped.append(current);
        }
        return unescaped.toString();
    }
    
    public static List<CharSequence> unescapeCsvFields(final CharSequence value) {
        final List<CharSequence> unescaped = new ArrayList<CharSequence>(2);
        final StringBuilder current = InternalThreadLocalMap.get().stringBuilder();
        boolean quoted = false;
        final int last = value.length() - 1;
        for (int i = 0; i <= last; ++i) {
            final char c = value.charAt(i);
            if (quoted) {
                switch (c) {
                    case '\"': {
                        if (i == last) {
                            unescaped.add(current.toString());
                            return unescaped;
                        }
                        final char next = value.charAt(++i);
                        if (next == '\"') {
                            current.append('\"');
                            break;
                        }
                        if (next == ',') {
                            quoted = false;
                            unescaped.add(current.toString());
                            current.setLength(0);
                            break;
                        }
                        throw newInvalidEscapedCsvFieldException(value, i - 1);
                    }
                    default: {
                        current.append(c);
                        break;
                    }
                }
            }
            else {
                switch (c) {
                    case ',': {
                        unescaped.add(current.toString());
                        current.setLength(0);
                        break;
                    }
                    case '\"': {
                        if (current.length() == 0) {
                            quoted = true;
                            break;
                        }
                        throw newInvalidEscapedCsvFieldException(value, i);
                    }
                    case '\n':
                    case '\r': {
                        throw newInvalidEscapedCsvFieldException(value, i);
                    }
                    default: {
                        current.append(c);
                        break;
                    }
                }
            }
        }
        if (quoted) {
            throw newInvalidEscapedCsvFieldException(value, last);
        }
        unescaped.add(current.toString());
        return unescaped;
    }
    
    private static void validateCsvFormat(final CharSequence value) {
        final int length = value.length();
        int i = 0;
        while (i < length) {
            switch (value.charAt(i)) {
                case '\n':
                case '\r':
                case '\"':
                case ',': {
                    throw newInvalidEscapedCsvFieldException(value, i);
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
    }
    
    private static IllegalArgumentException newInvalidEscapedCsvFieldException(final CharSequence value, final int index) {
        return new IllegalArgumentException("invalid escaped CSV field: " + (Object)value + " index: " + index);
    }
    
    public static int length(final String s) {
        return (s == null) ? 0 : s.length();
    }
    
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }
    
    public static int indexOfNonWhiteSpace(final CharSequence seq, int offset) {
        while (offset < seq.length()) {
            if (!Character.isWhitespace(seq.charAt(offset))) {
                return offset;
            }
            ++offset;
        }
        return -1;
    }
    
    public static boolean isSurrogate(final char c) {
        return c >= '\ud800' && c <= '\udfff';
    }
    
    private static boolean isDoubleQuote(final char c) {
        return c == '\"';
    }
    
    public static boolean endsWith(final CharSequence s, final char c) {
        final int len = s.length();
        return len > 0 && s.charAt(len - 1) == c;
    }
    
    static {
        NEWLINE = System.getProperty("line.separator");
        BYTE2HEX_PAD = new String[256];
        BYTE2HEX_NOPAD = new String[256];
        int i;
        for (i = 0; i < 10; ++i) {
            StringUtil.BYTE2HEX_PAD[i] = "0" + i;
            StringUtil.BYTE2HEX_NOPAD[i] = String.valueOf(i);
        }
        while (i < 16) {
            final char c = (char)(97 + i - 10);
            StringUtil.BYTE2HEX_PAD[i] = "0" + c;
            StringUtil.BYTE2HEX_NOPAD[i] = String.valueOf(c);
            ++i;
        }
        while (i < StringUtil.BYTE2HEX_PAD.length) {
            final String str = Integer.toHexString(i);
            StringUtil.BYTE2HEX_PAD[i] = str;
            StringUtil.BYTE2HEX_NOPAD[i] = str;
            ++i;
        }
    }
}
