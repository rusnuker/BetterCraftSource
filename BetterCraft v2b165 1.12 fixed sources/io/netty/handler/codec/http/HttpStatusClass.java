// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.AsciiString;

public enum HttpStatusClass
{
    INFORMATIONAL(100, 200, "Informational"), 
    SUCCESS(200, 300, "Success"), 
    REDIRECTION(300, 400, "Redirection"), 
    CLIENT_ERROR(400, 500, "Client Error"), 
    SERVER_ERROR(500, 600, "Server Error"), 
    UNKNOWN(0, 0, "Unknown Status") {
        @Override
        public boolean contains(final int code) {
            return code < 100 || code >= 600;
        }
    };
    
    private final int min;
    private final int max;
    private final AsciiString defaultReasonPhrase;
    
    public static HttpStatusClass valueOf(final int code) {
        if (HttpStatusClass.INFORMATIONAL.contains(code)) {
            return HttpStatusClass.INFORMATIONAL;
        }
        if (HttpStatusClass.SUCCESS.contains(code)) {
            return HttpStatusClass.SUCCESS;
        }
        if (HttpStatusClass.REDIRECTION.contains(code)) {
            return HttpStatusClass.REDIRECTION;
        }
        if (HttpStatusClass.CLIENT_ERROR.contains(code)) {
            return HttpStatusClass.CLIENT_ERROR;
        }
        if (HttpStatusClass.SERVER_ERROR.contains(code)) {
            return HttpStatusClass.SERVER_ERROR;
        }
        return HttpStatusClass.UNKNOWN;
    }
    
    private HttpStatusClass(final int min, final int max, final String defaultReasonPhrase) {
        this.min = min;
        this.max = max;
        this.defaultReasonPhrase = new AsciiString(defaultReasonPhrase);
    }
    
    public boolean contains(final int code) {
        return code >= this.min && code < this.max;
    }
    
    AsciiString defaultReasonPhrase() {
        return this.defaultReasonPhrase;
    }
}
