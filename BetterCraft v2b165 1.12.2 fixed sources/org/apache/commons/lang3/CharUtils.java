// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.lang3;

public class CharUtils
{
    private static final String[] CHAR_STRING_ARRAY;
    private static final char[] HEX_DIGITS;
    public static final char LF = '\n';
    public static final char CR = '\r';
    
    @Deprecated
    public static Character toCharacterObject(final char ch) {
        return ch;
    }
    
    public static Character toCharacterObject(final String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return str.charAt(0);
    }
    
    public static char toChar(final Character ch) {
        if (ch == null) {
            throw new IllegalArgumentException("The Character must not be null");
        }
        return ch;
    }
    
    public static char toChar(final Character ch, final char defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return ch;
    }
    
    public static char toChar(final String str) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("The String must not be empty");
        }
        return str.charAt(0);
    }
    
    public static char toChar(final String str, final char defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return str.charAt(0);
    }
    
    public static int toIntValue(final char ch) {
        if (!isAsciiNumeric(ch)) {
            throw new IllegalArgumentException("The character " + ch + " is not in the range '0' - '9'");
        }
        return ch - '0';
    }
    
    public static int toIntValue(final char ch, final int defaultValue) {
        if (!isAsciiNumeric(ch)) {
            return defaultValue;
        }
        return ch - '0';
    }
    
    public static int toIntValue(final Character ch) {
        if (ch == null) {
            throw new IllegalArgumentException("The character must not be null");
        }
        return toIntValue((char)ch);
    }
    
    public static int toIntValue(final Character ch, final int defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return toIntValue((char)ch, defaultValue);
    }
    
    public static String toString(final char ch) {
        if (ch < '\u0080') {
            return CharUtils.CHAR_STRING_ARRAY[ch];
        }
        return new String(new char[] { ch });
    }
    
    public static String toString(final Character ch) {
        if (ch == null) {
            return null;
        }
        return toString((char)ch);
    }
    
    public static String unicodeEscaped(final char ch) {
        final StringBuilder sb = new StringBuilder(6);
        sb.append("\\u");
        sb.append(CharUtils.HEX_DIGITS[ch >> 12 & 0xF]);
        sb.append(CharUtils.HEX_DIGITS[ch >> 8 & 0xF]);
        sb.append(CharUtils.HEX_DIGITS[ch >> 4 & 0xF]);
        sb.append(CharUtils.HEX_DIGITS[ch & '\u000f']);
        return sb.toString();
    }
    
    public static String unicodeEscaped(final Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped((char)ch);
    }
    
    public static boolean isAscii(final char ch) {
        return ch < '\u0080';
    }
    
    public static boolean isAsciiPrintable(final char ch) {
        return ch >= ' ' && ch < '\u007f';
    }
    
    public static boolean isAsciiControl(final char ch) {
        return ch < ' ' || ch == '\u007f';
    }
    
    public static boolean isAsciiAlpha(final char ch) {
        return isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch);
    }
    
    public static boolean isAsciiAlphaUpper(final char ch) {
        return ch >= 'A' && ch <= 'Z';
    }
    
    public static boolean isAsciiAlphaLower(final char ch) {
        return ch >= 'a' && ch <= 'z';
    }
    
    public static boolean isAsciiNumeric(final char ch) {
        return ch >= '0' && ch <= '9';
    }
    
    public static boolean isAsciiAlphanumeric(final char ch) {
        return isAsciiAlpha(ch) || isAsciiNumeric(ch);
    }
    
    public static int compare(final char x, final char y) {
        return x - y;
    }
    
    static {
        CHAR_STRING_ARRAY = new String[128];
        HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (char c = '\0'; c < CharUtils.CHAR_STRING_ARRAY.length; ++c) {
            CharUtils.CHAR_STRING_ARRAY[c] = String.valueOf(c);
        }
    }
}
