// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cookie;

import java.util.Date;
import io.netty.handler.codec.DateFormatter;
import io.netty.util.internal.ObjectUtil;

public final class ClientCookieDecoder extends CookieDecoder
{
    public static final ClientCookieDecoder STRICT;
    public static final ClientCookieDecoder LAX;
    
    private ClientCookieDecoder(final boolean strict) {
        super(strict);
    }
    
    public Cookie decode(final String header) {
        final int headerLen = ObjectUtil.checkNotNull(header, "header").length();
        if (headerLen == 0) {
            return null;
        }
        CookieBuilder cookieBuilder = null;
        int i = 0;
        while (i != headerLen) {
            final char c = header.charAt(i);
            if (c == ',') {
                return cookieBuilder.cookie();
            }
            if (c == '\t' || c == '\n' || c == '\u000b' || c == '\f' || c == '\r' || c == ' ' || c == ';') {
                ++i;
            }
            else {
                final int nameBegin = i;
                int nameEnd;
                int valueBegin;
                int valueEnd;
                while (true) {
                    final char curChar = header.charAt(i);
                    if (curChar == ';') {
                        nameEnd = i;
                        valueEnd = (valueBegin = -1);
                        break;
                    }
                    if (curChar == '=') {
                        nameEnd = i;
                        if (++i == headerLen) {
                            valueEnd = (valueBegin = 0);
                            break;
                        }
                        valueBegin = i;
                        final int semiPos = header.indexOf(59, i);
                        i = (valueEnd = ((semiPos > 0) ? semiPos : headerLen));
                        break;
                    }
                    else {
                        if (++i == headerLen) {
                            nameEnd = headerLen;
                            valueEnd = (valueBegin = -1);
                            break;
                        }
                        continue;
                    }
                }
                if (valueEnd > 0 && header.charAt(valueEnd - 1) == ',') {
                    --valueEnd;
                }
                if (cookieBuilder == null) {
                    final DefaultCookie cookie = this.initCookie(header, nameBegin, nameEnd, valueBegin, valueEnd);
                    if (cookie == null) {
                        return null;
                    }
                    cookieBuilder = new CookieBuilder(cookie, header);
                }
                else {
                    cookieBuilder.appendAttribute(nameBegin, nameEnd, valueBegin, valueEnd);
                }
            }
        }
        return cookieBuilder.cookie();
    }
    
    static {
        STRICT = new ClientCookieDecoder(true);
        LAX = new ClientCookieDecoder(false);
    }
    
    private static class CookieBuilder
    {
        private final String header;
        private final DefaultCookie cookie;
        private String domain;
        private String path;
        private long maxAge;
        private int expiresStart;
        private int expiresEnd;
        private boolean secure;
        private boolean httpOnly;
        
        CookieBuilder(final DefaultCookie cookie, final String header) {
            this.maxAge = Long.MIN_VALUE;
            this.cookie = cookie;
            this.header = header;
        }
        
        private long mergeMaxAgeAndExpires() {
            if (this.maxAge != Long.MIN_VALUE) {
                return this.maxAge;
            }
            if (isValueDefined(this.expiresStart, this.expiresEnd)) {
                final Date expiresDate = DateFormatter.parseHttpDate(this.header, this.expiresStart, this.expiresEnd);
                if (expiresDate != null) {
                    final long maxAgeMillis = expiresDate.getTime() - System.currentTimeMillis();
                    return maxAgeMillis / 1000L + ((maxAgeMillis % 1000L != 0L) ? 1 : 0);
                }
            }
            return Long.MIN_VALUE;
        }
        
        Cookie cookie() {
            this.cookie.setDomain(this.domain);
            this.cookie.setPath(this.path);
            this.cookie.setMaxAge(this.mergeMaxAgeAndExpires());
            this.cookie.setSecure(this.secure);
            this.cookie.setHttpOnly(this.httpOnly);
            return this.cookie;
        }
        
        void appendAttribute(final int keyStart, final int keyEnd, final int valueStart, final int valueEnd) {
            final int length = keyEnd - keyStart;
            if (length == 4) {
                this.parse4(keyStart, valueStart, valueEnd);
            }
            else if (length == 6) {
                this.parse6(keyStart, valueStart, valueEnd);
            }
            else if (length == 7) {
                this.parse7(keyStart, valueStart, valueEnd);
            }
            else if (length == 8) {
                this.parse8(keyStart);
            }
        }
        
        private void parse4(final int nameStart, final int valueStart, final int valueEnd) {
            if (this.header.regionMatches(true, nameStart, "Path", 0, 4)) {
                this.path = this.computeValue(valueStart, valueEnd);
            }
        }
        
        private void parse6(final int nameStart, final int valueStart, final int valueEnd) {
            if (this.header.regionMatches(true, nameStart, "Domain", 0, 5)) {
                this.domain = this.computeValue(valueStart, valueEnd);
            }
            else if (this.header.regionMatches(true, nameStart, "Secure", 0, 5)) {
                this.secure = true;
            }
        }
        
        private void setMaxAge(final String value) {
            try {
                this.maxAge = Math.max(Long.parseLong(value), 0L);
            }
            catch (final NumberFormatException ex) {}
        }
        
        private void parse7(final int nameStart, final int valueStart, final int valueEnd) {
            if (this.header.regionMatches(true, nameStart, "Expires", 0, 7)) {
                this.expiresStart = valueStart;
                this.expiresEnd = valueEnd;
            }
            else if (this.header.regionMatches(true, nameStart, "Max-Age", 0, 7)) {
                this.setMaxAge(this.computeValue(valueStart, valueEnd));
            }
        }
        
        private void parse8(final int nameStart) {
            if (this.header.regionMatches(true, nameStart, "HTTPOnly", 0, 8)) {
                this.httpOnly = true;
            }
        }
        
        private static boolean isValueDefined(final int valueStart, final int valueEnd) {
            return valueStart != -1 && valueStart != valueEnd;
        }
        
        private String computeValue(final int valueStart, final int valueEnd) {
            return isValueDefined(valueStart, valueEnd) ? this.header.substring(valueStart, valueEnd) : null;
        }
    }
}
