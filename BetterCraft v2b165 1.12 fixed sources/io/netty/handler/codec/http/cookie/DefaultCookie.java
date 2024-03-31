// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cookie;

import io.netty.util.internal.ObjectUtil;

public class DefaultCookie implements Cookie
{
    private final String name;
    private String value;
    private boolean wrap;
    private String domain;
    private String path;
    private long maxAge;
    private boolean secure;
    private boolean httpOnly;
    
    public DefaultCookie(String name, final String value) {
        this.maxAge = Long.MIN_VALUE;
        name = ObjectUtil.checkNotNull(name, "name").trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.name = name;
        this.setValue(value);
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public String value() {
        return this.value;
    }
    
    @Override
    public void setValue(final String value) {
        this.value = ObjectUtil.checkNotNull(value, "value");
    }
    
    @Override
    public boolean wrap() {
        return this.wrap;
    }
    
    @Override
    public void setWrap(final boolean wrap) {
        this.wrap = wrap;
    }
    
    @Override
    public String domain() {
        return this.domain;
    }
    
    @Override
    public void setDomain(final String domain) {
        this.domain = CookieUtil.validateAttributeValue("domain", domain);
    }
    
    @Override
    public String path() {
        return this.path;
    }
    
    @Override
    public void setPath(final String path) {
        this.path = CookieUtil.validateAttributeValue("path", path);
    }
    
    @Override
    public long maxAge() {
        return this.maxAge;
    }
    
    @Override
    public void setMaxAge(final long maxAge) {
        this.maxAge = maxAge;
    }
    
    @Override
    public boolean isSecure() {
        return this.secure;
    }
    
    @Override
    public void setSecure(final boolean secure) {
        this.secure = secure;
    }
    
    @Override
    public boolean isHttpOnly() {
        return this.httpOnly;
    }
    
    @Override
    public void setHttpOnly(final boolean httpOnly) {
        this.httpOnly = httpOnly;
    }
    
    @Override
    public int hashCode() {
        return this.name().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cookie)) {
            return false;
        }
        final Cookie that = (Cookie)o;
        if (!this.name().equals(that.name())) {
            return false;
        }
        if (this.path() == null) {
            if (that.path() != null) {
                return false;
            }
        }
        else {
            if (that.path() == null) {
                return false;
            }
            if (!this.path().equals(that.path())) {
                return false;
            }
        }
        if (this.domain() == null) {
            return that.domain() == null;
        }
        return this.domain().equalsIgnoreCase(that.domain());
    }
    
    @Override
    public int compareTo(final Cookie c) {
        int v = this.name().compareTo(c.name());
        if (v != 0) {
            return v;
        }
        if (this.path() == null) {
            if (c.path() != null) {
                return -1;
            }
        }
        else {
            if (c.path() == null) {
                return 1;
            }
            v = this.path().compareTo(c.path());
            if (v != 0) {
                return v;
            }
        }
        if (this.domain() == null) {
            if (c.domain() != null) {
                return -1;
            }
            return 0;
        }
        else {
            if (c.domain() == null) {
                return 1;
            }
            v = this.domain().compareToIgnoreCase(c.domain());
            return v;
        }
    }
    
    @Deprecated
    protected String validateValue(final String name, final String value) {
        return CookieUtil.validateAttributeValue(name, value);
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = CookieUtil.stringBuilder().append(this.name()).append('=').append(this.value());
        if (this.domain() != null) {
            buf.append(", domain=").append(this.domain());
        }
        if (this.path() != null) {
            buf.append(", path=").append(this.path());
        }
        if (this.maxAge() >= 0L) {
            buf.append(", maxAge=").append(this.maxAge()).append('s');
        }
        if (this.isSecure()) {
            buf.append(", secure");
        }
        if (this.isHttpOnly()) {
            buf.append(", HTTPOnly");
        }
        return buf.toString();
    }
}
