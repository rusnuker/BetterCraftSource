// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.http;

public interface HttpRequestFactory
{
    HttpRequest newHttpRequest(final RequestLine p0) throws MethodNotSupportedException;
    
    HttpRequest newHttpRequest(final String p0, final String p1) throws MethodNotSupportedException;
}
