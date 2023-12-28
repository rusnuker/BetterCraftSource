// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.http.impl.auth;

import org.apache.http.protocol.HttpContext;
import org.apache.http.auth.AuthScheme;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthSchemeFactory;

@Immutable
public class SPNegoSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider
{
    private final boolean stripPort;
    
    public SPNegoSchemeFactory(final boolean stripPort) {
        this.stripPort = stripPort;
    }
    
    public SPNegoSchemeFactory() {
        this(false);
    }
    
    public boolean isStripPort() {
        return this.stripPort;
    }
    
    public AuthScheme newInstance(final HttpParams params) {
        return new SPNegoScheme(this.stripPort);
    }
    
    public AuthScheme create(final HttpContext context) {
        return new SPNegoScheme(this.stripPort);
    }
}
