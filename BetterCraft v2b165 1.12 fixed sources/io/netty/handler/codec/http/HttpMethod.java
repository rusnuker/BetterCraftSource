// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.util.HashMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.AsciiString;
import java.util.Map;

public class HttpMethod implements Comparable<HttpMethod>
{
    public static final HttpMethod OPTIONS;
    public static final HttpMethod GET;
    public static final HttpMethod HEAD;
    public static final HttpMethod POST;
    public static final HttpMethod PUT;
    public static final HttpMethod PATCH;
    public static final HttpMethod DELETE;
    public static final HttpMethod TRACE;
    public static final HttpMethod CONNECT;
    private static final Map<String, HttpMethod> methodMap;
    private final AsciiString name;
    
    public static HttpMethod valueOf(final String name) {
        final HttpMethod result = HttpMethod.methodMap.get(name);
        return (result != null) ? result : new HttpMethod(name);
    }
    
    public HttpMethod(String name) {
        name = ObjectUtil.checkNotNull(name, "name").trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        for (int i = 0; i < name.length(); ++i) {
            final char c = name.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                throw new IllegalArgumentException("invalid character in name");
            }
        }
        this.name = new AsciiString(name);
    }
    
    public String name() {
        return this.name.toString();
    }
    
    public AsciiString asciiName() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        return this.name().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof HttpMethod)) {
            return false;
        }
        final HttpMethod that = (HttpMethod)o;
        return this.name().equals(that.name());
    }
    
    @Override
    public String toString() {
        return this.name.toString();
    }
    
    @Override
    public int compareTo(final HttpMethod o) {
        return this.name().compareTo(o.name());
    }
    
    static {
        OPTIONS = new HttpMethod("OPTIONS");
        GET = new HttpMethod("GET");
        HEAD = new HttpMethod("HEAD");
        POST = new HttpMethod("POST");
        PUT = new HttpMethod("PUT");
        PATCH = new HttpMethod("PATCH");
        DELETE = new HttpMethod("DELETE");
        TRACE = new HttpMethod("TRACE");
        CONNECT = new HttpMethod("CONNECT");
        (methodMap = new HashMap<String, HttpMethod>()).put(HttpMethod.OPTIONS.toString(), HttpMethod.OPTIONS);
        HttpMethod.methodMap.put(HttpMethod.GET.toString(), HttpMethod.GET);
        HttpMethod.methodMap.put(HttpMethod.HEAD.toString(), HttpMethod.HEAD);
        HttpMethod.methodMap.put(HttpMethod.POST.toString(), HttpMethod.POST);
        HttpMethod.methodMap.put(HttpMethod.PUT.toString(), HttpMethod.PUT);
        HttpMethod.methodMap.put(HttpMethod.PATCH.toString(), HttpMethod.PATCH);
        HttpMethod.methodMap.put(HttpMethod.DELETE.toString(), HttpMethod.DELETE);
        HttpMethod.methodMap.put(HttpMethod.TRACE.toString(), HttpMethod.TRACE);
        HttpMethod.methodMap.put(HttpMethod.CONNECT.toString(), HttpMethod.CONNECT);
    }
}
