// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cookie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import io.netty.handler.codec.DateFormatter;
import java.util.Date;
import io.netty.util.internal.ObjectUtil;

public final class ServerCookieEncoder extends CookieEncoder
{
    public static final ServerCookieEncoder STRICT;
    public static final ServerCookieEncoder LAX;
    
    private ServerCookieEncoder(final boolean strict) {
        super(strict);
    }
    
    public String encode(final String name, final String value) {
        return this.encode(new DefaultCookie(name, value));
    }
    
    public String encode(final Cookie cookie) {
        final String name = ObjectUtil.checkNotNull(cookie, "cookie").name();
        final String value = (cookie.value() != null) ? cookie.value() : "";
        this.validateCookie(name, value);
        final StringBuilder buf = CookieUtil.stringBuilder();
        if (cookie.wrap()) {
            CookieUtil.addQuoted(buf, name, value);
        }
        else {
            CookieUtil.add(buf, name, value);
        }
        if (cookie.maxAge() != Long.MIN_VALUE) {
            CookieUtil.add(buf, "Max-Age", cookie.maxAge());
            final Date expires = new Date(cookie.maxAge() * 1000L + System.currentTimeMillis());
            buf.append("Expires");
            buf.append('=');
            DateFormatter.append(expires, buf);
            buf.append(';');
            buf.append(' ');
        }
        if (cookie.path() != null) {
            CookieUtil.add(buf, "Path", cookie.path());
        }
        if (cookie.domain() != null) {
            CookieUtil.add(buf, "Domain", cookie.domain());
        }
        if (cookie.isSecure()) {
            CookieUtil.add(buf, "Secure");
        }
        if (cookie.isHttpOnly()) {
            CookieUtil.add(buf, "HTTPOnly");
        }
        return CookieUtil.stripTrailingSeparator(buf);
    }
    
    private static List<String> dedup(final List<String> encoded, final Map<String, Integer> nameToLastIndex) {
        final boolean[] isLastInstance = new boolean[encoded.size()];
        for (final int idx : nameToLastIndex.values()) {
            isLastInstance[idx] = true;
        }
        final List<String> dedupd = new ArrayList<String>(nameToLastIndex.size());
        for (int i = 0, n = encoded.size(); i < n; ++i) {
            if (isLastInstance[i]) {
                dedupd.add(encoded.get(i));
            }
        }
        return dedupd;
    }
    
    public List<String> encode(final Cookie... cookies) {
        if (ObjectUtil.checkNotNull(cookies, "cookies").length == 0) {
            return Collections.emptyList();
        }
        final List<String> encoded = new ArrayList<String>(cookies.length);
        final Map<String, Integer> nameToIndex = (this.strict && cookies.length > 1) ? new HashMap<String, Integer>() : null;
        boolean hasDupdName = false;
        for (int i = 0; i < cookies.length; ++i) {
            final Cookie c = cookies[i];
            encoded.add(this.encode(c));
            if (nameToIndex != null) {
                hasDupdName |= (nameToIndex.put(c.name(), i) != null);
            }
        }
        return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
    }
    
    public List<String> encode(final Collection<? extends Cookie> cookies) {
        if (ObjectUtil.checkNotNull(cookies, "cookies").isEmpty()) {
            return Collections.emptyList();
        }
        final List<String> encoded = new ArrayList<String>(cookies.size());
        final Map<String, Integer> nameToIndex = (this.strict && cookies.size() > 1) ? new HashMap<String, Integer>() : null;
        int i = 0;
        boolean hasDupdName = false;
        for (final Cookie c : cookies) {
            encoded.add(this.encode(c));
            if (nameToIndex != null) {
                hasDupdName |= (nameToIndex.put(c.name(), i++) != null);
            }
        }
        return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
    }
    
    public List<String> encode(final Iterable<? extends Cookie> cookies) {
        final Iterator<? extends Cookie> cookiesIt = ObjectUtil.checkNotNull(cookies, "cookies").iterator();
        if (!cookiesIt.hasNext()) {
            return Collections.emptyList();
        }
        final List<String> encoded = new ArrayList<String>();
        final Cookie firstCookie = (Cookie)cookiesIt.next();
        final Map<String, Integer> nameToIndex = (this.strict && cookiesIt.hasNext()) ? new HashMap<String, Integer>() : null;
        int i = 0;
        encoded.add(this.encode(firstCookie));
        boolean hasDupdName = nameToIndex != null && nameToIndex.put(firstCookie.name(), i++) != null;
        while (cookiesIt.hasNext()) {
            final Cookie c = (Cookie)cookiesIt.next();
            encoded.add(this.encode(c));
            if (nameToIndex != null) {
                hasDupdName |= (nameToIndex.put(c.name(), i++) != null);
            }
        }
        return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
    }
    
    static {
        STRICT = new ServerCookieEncoder(true);
        LAX = new ServerCookieEncoder(false);
    }
}
