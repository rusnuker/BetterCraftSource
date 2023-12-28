// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cookie;

import java.util.List;
import io.netty.util.internal.InternalThreadLocalMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import io.netty.util.internal.ObjectUtil;
import java.util.Comparator;

public final class ClientCookieEncoder extends CookieEncoder
{
    public static final ClientCookieEncoder STRICT;
    public static final ClientCookieEncoder LAX;
    private static final Comparator<Cookie> COOKIE_COMPARATOR;
    
    private ClientCookieEncoder(final boolean strict) {
        super(strict);
    }
    
    public String encode(final String name, final String value) {
        return this.encode(new DefaultCookie(name, value));
    }
    
    public String encode(final Cookie cookie) {
        final StringBuilder buf = CookieUtil.stringBuilder();
        this.encode(buf, ObjectUtil.checkNotNull(cookie, "cookie"));
        return CookieUtil.stripTrailingSeparator(buf);
    }
    
    public String encode(final Cookie... cookies) {
        if (ObjectUtil.checkNotNull(cookies, "cookies").length == 0) {
            return null;
        }
        final StringBuilder buf = CookieUtil.stringBuilder();
        if (this.strict) {
            if (cookies.length == 1) {
                this.encode(buf, cookies[0]);
            }
            else {
                final Cookie[] cookiesSorted = Arrays.copyOf(cookies, cookies.length);
                Arrays.sort(cookiesSorted, ClientCookieEncoder.COOKIE_COMPARATOR);
                for (final Cookie c : cookiesSorted) {
                    this.encode(buf, c);
                }
            }
        }
        else {
            for (final Cookie c2 : cookies) {
                this.encode(buf, c2);
            }
        }
        return CookieUtil.stripTrailingSeparatorOrNull(buf);
    }
    
    public String encode(final Collection<? extends Cookie> cookies) {
        if (ObjectUtil.checkNotNull(cookies, "cookies").isEmpty()) {
            return null;
        }
        final StringBuilder buf = CookieUtil.stringBuilder();
        if (this.strict) {
            if (cookies.size() == 1) {
                this.encode(buf, (Cookie)cookies.iterator().next());
            }
            else {
                final Cookie[] cookiesSorted = cookies.toArray(new Cookie[cookies.size()]);
                Arrays.sort(cookiesSorted, ClientCookieEncoder.COOKIE_COMPARATOR);
                for (final Cookie c : cookiesSorted) {
                    this.encode(buf, c);
                }
            }
        }
        else {
            for (final Cookie c2 : cookies) {
                this.encode(buf, c2);
            }
        }
        return CookieUtil.stripTrailingSeparatorOrNull(buf);
    }
    
    public String encode(final Iterable<? extends Cookie> cookies) {
        final Iterator<? extends Cookie> cookiesIt = ObjectUtil.checkNotNull(cookies, "cookies").iterator();
        if (!cookiesIt.hasNext()) {
            return null;
        }
        final StringBuilder buf = CookieUtil.stringBuilder();
        if (this.strict) {
            final Cookie firstCookie = (Cookie)cookiesIt.next();
            if (!cookiesIt.hasNext()) {
                this.encode(buf, firstCookie);
            }
            else {
                final List<Cookie> cookiesList = (List<Cookie>)InternalThreadLocalMap.get().arrayList();
                cookiesList.add(firstCookie);
                while (cookiesIt.hasNext()) {
                    cookiesList.add((Cookie)cookiesIt.next());
                }
                final Cookie[] cookiesSorted = cookiesList.toArray(new Cookie[cookiesList.size()]);
                Arrays.sort(cookiesSorted, ClientCookieEncoder.COOKIE_COMPARATOR);
                for (final Cookie c : cookiesSorted) {
                    this.encode(buf, c);
                }
            }
        }
        else {
            while (cookiesIt.hasNext()) {
                this.encode(buf, (Cookie)cookiesIt.next());
            }
        }
        return CookieUtil.stripTrailingSeparatorOrNull(buf);
    }
    
    private void encode(final StringBuilder buf, final Cookie c) {
        final String name = c.name();
        final String value = (c.value() != null) ? c.value() : "";
        this.validateCookie(name, value);
        if (c.wrap()) {
            CookieUtil.addQuoted(buf, name, value);
        }
        else {
            CookieUtil.add(buf, name, value);
        }
    }
    
    static {
        STRICT = new ClientCookieEncoder(true);
        LAX = new ClientCookieEncoder(false);
        COOKIE_COMPARATOR = new Comparator<Cookie>() {
            @Override
            public int compare(final Cookie c1, final Cookie c2) {
                final String path1 = c1.path();
                final String path2 = c2.path();
                final int len1 = (path1 == null) ? Integer.MAX_VALUE : path1.length();
                final int len2 = (path2 == null) ? Integer.MAX_VALUE : path2.length();
                final int diff = len2 - len1;
                if (diff != 0) {
                    return diff;
                }
                return -1;
            }
        };
    }
}
