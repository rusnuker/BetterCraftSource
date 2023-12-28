// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import java.util.Date;

public final class CefCookie
{
    public final String name;
    public final String value;
    public final String domain;
    public final String path;
    public final boolean secure;
    public final boolean httponly;
    public final Date creation;
    public final Date lastAccess;
    public final boolean hasExpires;
    public final Date expires;
    
    public CefCookie(final String name, final String value, final String domain, final String path, final boolean secure, final boolean httponly, final Date creation, final Date lastAccess, final boolean hasExpires, final Date expires) {
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.httponly = httponly;
        this.creation = creation;
        this.lastAccess = lastAccess;
        this.hasExpires = hasExpires;
        this.expires = expires;
    }
}
