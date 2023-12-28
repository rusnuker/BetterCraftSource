// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Date;
import java.util.List;
import io.netty.handler.codec.DateFormatter;
import java.util.TreeSet;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Set;
import io.netty.util.internal.logging.InternalLogger;

@Deprecated
public final class CookieDecoder
{
    private final InternalLogger logger;
    private static final CookieDecoder STRICT;
    private static final CookieDecoder LAX;
    private static final String COMMENT = "Comment";
    private static final String COMMENTURL = "CommentURL";
    private static final String DISCARD = "Discard";
    private static final String PORT = "Port";
    private static final String VERSION = "Version";
    private final boolean strict;
    
    public static Set<Cookie> decode(final String header) {
        return decode(header, true);
    }
    
    public static Set<Cookie> decode(final String header, final boolean strict) {
        return (strict ? CookieDecoder.STRICT : CookieDecoder.LAX).doDecode(header);
    }
    
    private Set<Cookie> doDecode(final String header) {
        final List<String> names = new ArrayList<String>(8);
        final List<String> values = new ArrayList<String>(8);
        extractKeyValuePairs(header, names, values);
        if (names.isEmpty()) {
            return Collections.emptySet();
        }
        int version = 0;
        int i;
        if (names.get(0).equalsIgnoreCase("Version")) {
            try {
                version = Integer.parseInt(values.get(0));
            }
            catch (final NumberFormatException ex) {}
            i = 1;
        }
        else {
            i = 0;
        }
        if (names.size() <= i) {
            return Collections.emptySet();
        }
        final Set<Cookie> cookies = new TreeSet<Cookie>();
        while (i < names.size()) {
            String name = names.get(i);
            String value = values.get(i);
            if (value == null) {
                value = "";
            }
            final Cookie c = this.initCookie(name, value);
            if (c == null) {
                break;
            }
            boolean discard = false;
            boolean secure = false;
            boolean httpOnly = false;
            String comment = null;
            String commentURL = null;
            String domain = null;
            String path = null;
            long maxAge = Long.MIN_VALUE;
            final List<Integer> ports = new ArrayList<Integer>(2);
            for (int j = i + 1; j < names.size(); ++j, ++i) {
                name = names.get(j);
                value = values.get(j);
                if ("Discard".equalsIgnoreCase(name)) {
                    discard = true;
                }
                else if ("Secure".equalsIgnoreCase(name)) {
                    secure = true;
                }
                else if ("HTTPOnly".equalsIgnoreCase(name)) {
                    httpOnly = true;
                }
                else if ("Comment".equalsIgnoreCase(name)) {
                    comment = value;
                }
                else if ("CommentURL".equalsIgnoreCase(name)) {
                    commentURL = value;
                }
                else if ("Domain".equalsIgnoreCase(name)) {
                    domain = value;
                }
                else if ("Path".equalsIgnoreCase(name)) {
                    path = value;
                }
                else if ("Expires".equalsIgnoreCase(name)) {
                    final Date date = DateFormatter.parseHttpDate(value);
                    if (date != null) {
                        final long maxAgeMillis = date.getTime() - System.currentTimeMillis();
                        maxAge = maxAgeMillis / 1000L + ((maxAgeMillis % 1000L != 0L) ? 1 : 0);
                    }
                }
                else if ("Max-Age".equalsIgnoreCase(name)) {
                    maxAge = Integer.parseInt(value);
                }
                else if ("Version".equalsIgnoreCase(name)) {
                    version = Integer.parseInt(value);
                }
                else {
                    if (!"Port".equalsIgnoreCase(name)) {
                        break;
                    }
                    final String[] split;
                    final String[] portList = split = value.split(",");
                    for (final String s1 : split) {
                        try {
                            ports.add(Integer.valueOf(s1));
                        }
                        catch (final NumberFormatException ex2) {}
                    }
                }
            }
            c.setVersion(version);
            c.setMaxAge(maxAge);
            c.setPath(path);
            c.setDomain(domain);
            c.setSecure(secure);
            c.setHttpOnly(httpOnly);
            if (version > 0) {
                c.setComment(comment);
            }
            if (version > 1) {
                c.setCommentUrl(commentURL);
                c.setPorts(ports);
                c.setDiscard(discard);
            }
            cookies.add(c);
            ++i;
        }
        return cookies;
    }
    
    private static void extractKeyValuePairs(final String header, final List<String> names, final List<String> values) {
        final int headerLen = header.length();
        int i = 0;
    Label_0008:
        while (true) {
            while (i != headerLen) {
                switch (header.charAt(i)) {
                    case '\t':
                    case '\n':
                    case '\u000b':
                    case '\f':
                    case '\r':
                    case ' ':
                    case ',':
                    case ';': {
                        ++i;
                        continue;
                    }
                    default: {
                        while (i != headerLen) {
                            if (header.charAt(i) != '$') {
                                String name = null;
                                String value = null;
                                Label_0494: {
                                    if (i == headerLen) {
                                        name = null;
                                        value = null;
                                    }
                                    else {
                                        final int newNameStart = i;
                                        do {
                                            switch (header.charAt(i)) {
                                                case ';': {
                                                    name = header.substring(newNameStart, i);
                                                    value = null;
                                                    break Label_0494;
                                                }
                                                case '=': {
                                                    name = header.substring(newNameStart, i);
                                                    if (++i == headerLen) {
                                                        value = "";
                                                        break Label_0494;
                                                    }
                                                    final int newValueStart = i;
                                                    char c = header.charAt(i);
                                                    if (c == '\"' || c == '\'') {
                                                        final StringBuilder newValueBuf = new StringBuilder(header.length() - i);
                                                        final char q = c;
                                                        boolean hadBackslash = false;
                                                        ++i;
                                                        while (i != headerLen) {
                                                            if (hadBackslash) {
                                                                hadBackslash = false;
                                                                c = header.charAt(i++);
                                                                switch (c) {
                                                                    case '\"':
                                                                    case '\'':
                                                                    case '\\': {
                                                                        newValueBuf.setCharAt(newValueBuf.length() - 1, c);
                                                                        continue;
                                                                    }
                                                                    default: {
                                                                        newValueBuf.append(c);
                                                                        continue;
                                                                    }
                                                                }
                                                            }
                                                            else {
                                                                c = header.charAt(i++);
                                                                if (c == q) {
                                                                    value = newValueBuf.toString();
                                                                    break Label_0494;
                                                                }
                                                                newValueBuf.append(c);
                                                                if (c != '\\') {
                                                                    continue;
                                                                }
                                                                hadBackslash = true;
                                                            }
                                                        }
                                                        value = newValueBuf.toString();
                                                        break Label_0494;
                                                    }
                                                    final int semiPos = header.indexOf(59, i);
                                                    if (semiPos > 0) {
                                                        value = header.substring(newValueStart, semiPos);
                                                        i = semiPos;
                                                    }
                                                    else {
                                                        value = header.substring(newValueStart);
                                                        i = headerLen;
                                                    }
                                                    break Label_0494;
                                                }
                                                default: {
                                                    continue;
                                                }
                                            }
                                        } while (++i != headerLen);
                                        name = header.substring(newNameStart);
                                        value = null;
                                    }
                                }
                                names.add(name);
                                values.add(value);
                                continue Label_0008;
                            }
                            ++i;
                        }
                    }
                }
            }
            break;
        }
    }
    
    private CookieDecoder(final boolean strict) {
        this.logger = InternalLoggerFactory.getInstance(this.getClass());
        this.strict = strict;
    }
    
    private DefaultCookie initCookie(final String name, final String value) {
        if (name == null || name.length() == 0) {
            this.logger.debug("Skipping cookie with null name");
            return null;
        }
        if (value == null) {
            this.logger.debug("Skipping cookie with null value");
            return null;
        }
        final CharSequence unwrappedValue = CookieUtil.unwrapValue(value);
        if (unwrappedValue == null) {
            this.logger.debug("Skipping cookie because starting quotes are not properly balanced in '{}'", unwrappedValue);
            return null;
        }
        int invalidOctetPos;
        if (this.strict && (invalidOctetPos = CookieUtil.firstInvalidCookieNameOctet(name)) >= 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping cookie because name '{}' contains invalid char '{}'", name, name.charAt(invalidOctetPos));
            }
            return null;
        }
        final boolean wrap = unwrappedValue.length() != value.length();
        if (this.strict && (invalidOctetPos = CookieUtil.firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping cookie because value '{}' contains invalid char '{}'", unwrappedValue, unwrappedValue.charAt(invalidOctetPos));
            }
            return null;
        }
        final DefaultCookie cookie = new DefaultCookie(name, unwrappedValue.toString());
        cookie.setWrap(wrap);
        return cookie;
    }
    
    static {
        STRICT = new CookieDecoder(true);
        LAX = new CookieDecoder(false);
    }
}
