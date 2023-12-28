// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.util.Map;

public final class StringBuilders
{
    private StringBuilders() {
    }
    
    public static StringBuilder appendDqValue(final StringBuilder sb, final Object value) {
        return sb.append('\"').append(value).append('\"');
    }
    
    public static StringBuilder appendKeyDqValue(final StringBuilder sb, final Map.Entry<String, String> entry) {
        return appendKeyDqValue(sb, entry.getKey(), entry.getValue());
    }
    
    public static StringBuilder appendKeyDqValue(final StringBuilder sb, final String key, final Object value) {
        return sb.append(key).append('=').append('\"').append(value).append('\"');
    }
    
    public static void appendValue(final StringBuilder stringBuilder, final Object obj) {
        if (obj == null || obj instanceof String) {
            stringBuilder.append((String)obj);
        }
        else if (obj instanceof StringBuilderFormattable) {
            ((StringBuilderFormattable)obj).formatTo(stringBuilder);
        }
        else if (obj instanceof CharSequence) {
            stringBuilder.append((CharSequence)obj);
        }
        else if (obj instanceof Integer) {
            stringBuilder.append((int)obj);
        }
        else if (obj instanceof Long) {
            stringBuilder.append((long)obj);
        }
        else if (obj instanceof Double) {
            stringBuilder.append((double)obj);
        }
        else if (obj instanceof Boolean) {
            stringBuilder.append((boolean)obj);
        }
        else if (obj instanceof Character) {
            stringBuilder.append((char)obj);
        }
        else if (obj instanceof Short) {
            stringBuilder.append((short)obj);
        }
        else if (obj instanceof Float) {
            stringBuilder.append((float)obj);
        }
        else {
            stringBuilder.append(obj);
        }
    }
    
    public static boolean equals(final CharSequence left, final int leftOffset, final int leftLength, final CharSequence right, final int rightOffset, final int rightLength) {
        if (leftLength == rightLength) {
            for (int i = 0; i < rightLength; ++i) {
                if (left.charAt(i + leftOffset) != right.charAt(i + rightOffset)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static boolean equalsIgnoreCase(final CharSequence left, final int leftOffset, final int leftLength, final CharSequence right, final int rightOffset, final int rightLength) {
        if (leftLength == rightLength) {
            for (int i = 0; i < rightLength; ++i) {
                if (Character.toLowerCase(left.charAt(i + leftOffset)) != Character.toLowerCase(right.charAt(i + rightOffset))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
