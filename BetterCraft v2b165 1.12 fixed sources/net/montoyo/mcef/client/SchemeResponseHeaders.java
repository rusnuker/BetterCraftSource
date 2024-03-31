// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import org.cef.misc.StringRef;
import org.cef.misc.IntRef;
import org.cef.network.CefResponse;
import net.montoyo.mcef.api.ISchemeResponseHeaders;

public class SchemeResponseHeaders implements ISchemeResponseHeaders
{
    private final CefResponse response;
    private final IntRef length;
    private final StringRef redirURL;
    
    public SchemeResponseHeaders(final CefResponse r, final IntRef l, final StringRef url) {
        this.response = r;
        this.length = l;
        this.redirURL = url;
    }
    
    @Override
    public void setMimeType(final String mt) {
        this.response.setMimeType(mt);
    }
    
    @Override
    public void setStatus(final int status) {
        this.response.setStatus(status);
    }
    
    @Override
    public void setStatusText(final String st) {
        this.response.setStatusText(st);
    }
    
    @Override
    public void setResponseLength(final int len) {
        this.length.set(len);
    }
    
    @Override
    public void setRedirectURL(final String r) {
        this.redirURL.set(r);
    }
}
