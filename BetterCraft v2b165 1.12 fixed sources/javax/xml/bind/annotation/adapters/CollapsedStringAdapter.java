// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation.adapters;

public class CollapsedStringAdapter extends XmlAdapter<String, String>
{
    @Override
    public String unmarshal(final String text) {
        if (text == null) {
            return null;
        }
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
        return result.toString();
    }
    
    @Override
    public String marshal(final String s) {
        return s;
    }
    
    protected static boolean isWhiteSpace(final char ch) {
        return ch <= ' ' && (ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ');
    }
}
