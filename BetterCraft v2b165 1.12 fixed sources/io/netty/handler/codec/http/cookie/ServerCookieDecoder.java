// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cookie;

import java.util.TreeSet;
import java.util.Collections;
import io.netty.util.internal.ObjectUtil;
import java.util.Set;

public final class ServerCookieDecoder extends CookieDecoder
{
    private static final String RFC2965_VERSION = "$Version";
    private static final String RFC2965_PATH = "$Path";
    private static final String RFC2965_DOMAIN = "$Domain";
    private static final String RFC2965_PORT = "$Port";
    public static final ServerCookieDecoder STRICT;
    public static final ServerCookieDecoder LAX;
    
    private ServerCookieDecoder(final boolean strict) {
        super(strict);
    }
    
    public Set<Cookie> decode(final String header) {
        final int headerLen = ObjectUtil.checkNotNull(header, "header").length();
        if (headerLen == 0) {
            return Collections.emptySet();
        }
        final Set<Cookie> cookies = new TreeSet<Cookie>();
        int i = 0;
        boolean rfc2965Style = false;
        if (header.regionMatches(true, 0, "$Version", 0, "$Version".length())) {
            i = header.indexOf(59) + 1;
            rfc2965Style = true;
        }
        while (i != headerLen) {
            final char c = header.charAt(i);
            if (c == '\t' || c == '\n' || c == '\u000b' || c == '\f' || c == '\r' || c == ' ' || c == ',' || c == ';') {
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
                if (rfc2965Style) {
                    if (header.regionMatches(nameBegin, "$Path", 0, "$Path".length()) || header.regionMatches(nameBegin, "$Domain", 0, "$Domain".length())) {
                        continue;
                    }
                    if (header.regionMatches(nameBegin, "$Port", 0, "$Port".length())) {
                        continue;
                    }
                }
                final DefaultCookie cookie = this.initCookie(header, nameBegin, nameEnd, valueBegin, valueEnd);
                if (cookie == null) {
                    continue;
                }
                cookies.add(cookie);
            }
        }
        return cookies;
    }
    
    static {
        STRICT = new ServerCookieDecoder(true);
        LAX = new ServerCookieDecoder(false);
    }
}
