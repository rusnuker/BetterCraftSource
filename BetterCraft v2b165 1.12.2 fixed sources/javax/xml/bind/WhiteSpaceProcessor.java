// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

abstract class WhiteSpaceProcessor
{
    public static String replace(final String text) {
        return replace((CharSequence)text).toString();
    }
    
    public static CharSequence replace(final CharSequence text) {
        int i;
        for (i = text.length() - 1; i >= 0 && !isWhiteSpaceExceptSpace(text.charAt(i)); --i) {}
        if (i < 0) {
            return text;
        }
        final StringBuilder buf = new StringBuilder(text);
        buf.setCharAt(i--, ' ');
        while (i >= 0) {
            if (isWhiteSpaceExceptSpace(buf.charAt(i))) {
                buf.setCharAt(i, ' ');
            }
            --i;
        }
        return new String(buf);
    }
    
    public static CharSequence trim(final CharSequence text) {
        int len;
        int start;
        for (len = text.length(), start = 0; start < len && isWhiteSpace(text.charAt(start)); ++start) {}
        int end;
        for (end = len - 1; end > start && isWhiteSpace(text.charAt(end)); --end) {}
        if (start == 0 && end == len - 1) {
            return text;
        }
        return text.subSequence(start, end + 1);
    }
    
    public static String collapse(final String text) {
        return collapse((CharSequence)text).toString();
    }
    
    public static CharSequence collapse(final CharSequence text) {
        int len;
        int s;
        for (len = text.length(), s = 0; s < len && !isWhiteSpace(text.charAt(s)); ++s) {}
        if (s == len) {
            return text;
        }
        final StringBuilder result = new StringBuilder(len);
        if (s != 0) {
            for (int i = 0; i < s; ++i) {
                result.append(text.charAt(i));
            }
            result.append(' ');
        }
        boolean inStripMode = true;
        for (int j = s + 1; j < len; ++j) {
            final char ch = text.charAt(j);
            final boolean b = isWhiteSpace(ch);
            if (!inStripMode || !b) {
                inStripMode = b;
                if (inStripMode) {
                    result.append(' ');
                }
                else {
                    result.append(ch);
                }
            }
        }
        len = result.length();
        if (len > 0 && result.charAt(len - 1) == ' ') {
            result.setLength(len - 1);
        }
        return result;
    }
    
    public static final boolean isWhiteSpace(final CharSequence s) {
        for (int i = s.length() - 1; i >= 0; --i) {
            if (!isWhiteSpace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static final boolean isWhiteSpace(final char ch) {
        return ch <= ' ' && (ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ');
    }
    
    protected static final boolean isWhiteSpaceExceptSpace(final char ch) {
        return ch < ' ' && (ch == '\t' || ch == '\n' || ch == '\r');
    }
}
