// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.authlib;

public abstract class HttpUserAuthentication extends BaseUserAuthentication
{
    protected HttpUserAuthentication(final HttpAuthenticationService authenticationService) {
        super(authenticationService);
    }
    
    @Override
    public HttpAuthenticationService getAuthenticationService() {
        return (HttpAuthenticationService)super.getAuthenticationService();
    }
}
