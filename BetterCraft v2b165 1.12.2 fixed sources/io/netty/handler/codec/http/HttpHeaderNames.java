// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.AsciiString;

public final class HttpHeaderNames
{
    public static final AsciiString ACCEPT;
    public static final AsciiString ACCEPT_CHARSET;
    public static final AsciiString ACCEPT_ENCODING;
    public static final AsciiString ACCEPT_LANGUAGE;
    public static final AsciiString ACCEPT_RANGES;
    public static final AsciiString ACCEPT_PATCH;
    public static final AsciiString ACCESS_CONTROL_ALLOW_CREDENTIALS;
    public static final AsciiString ACCESS_CONTROL_ALLOW_HEADERS;
    public static final AsciiString ACCESS_CONTROL_ALLOW_METHODS;
    public static final AsciiString ACCESS_CONTROL_ALLOW_ORIGIN;
    public static final AsciiString ACCESS_CONTROL_EXPOSE_HEADERS;
    public static final AsciiString ACCESS_CONTROL_MAX_AGE;
    public static final AsciiString ACCESS_CONTROL_REQUEST_HEADERS;
    public static final AsciiString ACCESS_CONTROL_REQUEST_METHOD;
    public static final AsciiString AGE;
    public static final AsciiString ALLOW;
    public static final AsciiString AUTHORIZATION;
    public static final AsciiString CACHE_CONTROL;
    public static final AsciiString CONNECTION;
    public static final AsciiString CONTENT_BASE;
    public static final AsciiString CONTENT_ENCODING;
    public static final AsciiString CONTENT_LANGUAGE;
    public static final AsciiString CONTENT_LENGTH;
    public static final AsciiString CONTENT_LOCATION;
    public static final AsciiString CONTENT_TRANSFER_ENCODING;
    public static final AsciiString CONTENT_DISPOSITION;
    public static final AsciiString CONTENT_MD5;
    public static final AsciiString CONTENT_RANGE;
    public static final AsciiString CONTENT_TYPE;
    public static final AsciiString COOKIE;
    public static final AsciiString DATE;
    public static final AsciiString ETAG;
    public static final AsciiString EXPECT;
    public static final AsciiString EXPIRES;
    public static final AsciiString FROM;
    public static final AsciiString HOST;
    public static final AsciiString IF_MATCH;
    public static final AsciiString IF_MODIFIED_SINCE;
    public static final AsciiString IF_NONE_MATCH;
    public static final AsciiString IF_RANGE;
    public static final AsciiString IF_UNMODIFIED_SINCE;
    @Deprecated
    public static final AsciiString KEEP_ALIVE;
    public static final AsciiString LAST_MODIFIED;
    public static final AsciiString LOCATION;
    public static final AsciiString MAX_FORWARDS;
    public static final AsciiString ORIGIN;
    public static final AsciiString PRAGMA;
    public static final AsciiString PROXY_AUTHENTICATE;
    public static final AsciiString PROXY_AUTHORIZATION;
    @Deprecated
    public static final AsciiString PROXY_CONNECTION;
    public static final AsciiString RANGE;
    public static final AsciiString REFERER;
    public static final AsciiString RETRY_AFTER;
    public static final AsciiString SEC_WEBSOCKET_KEY1;
    public static final AsciiString SEC_WEBSOCKET_KEY2;
    public static final AsciiString SEC_WEBSOCKET_LOCATION;
    public static final AsciiString SEC_WEBSOCKET_ORIGIN;
    public static final AsciiString SEC_WEBSOCKET_PROTOCOL;
    public static final AsciiString SEC_WEBSOCKET_VERSION;
    public static final AsciiString SEC_WEBSOCKET_KEY;
    public static final AsciiString SEC_WEBSOCKET_ACCEPT;
    public static final AsciiString SEC_WEBSOCKET_EXTENSIONS;
    public static final AsciiString SERVER;
    public static final AsciiString SET_COOKIE;
    public static final AsciiString SET_COOKIE2;
    public static final AsciiString TE;
    public static final AsciiString TRAILER;
    public static final AsciiString TRANSFER_ENCODING;
    public static final AsciiString UPGRADE;
    public static final AsciiString USER_AGENT;
    public static final AsciiString VARY;
    public static final AsciiString VIA;
    public static final AsciiString WARNING;
    public static final AsciiString WEBSOCKET_LOCATION;
    public static final AsciiString WEBSOCKET_ORIGIN;
    public static final AsciiString WEBSOCKET_PROTOCOL;
    public static final AsciiString WWW_AUTHENTICATE;
    
    private HttpHeaderNames() {
    }
    
    static {
        ACCEPT = new AsciiString("accept");
        ACCEPT_CHARSET = new AsciiString("accept-charset");
        ACCEPT_ENCODING = new AsciiString("accept-encoding");
        ACCEPT_LANGUAGE = new AsciiString("accept-language");
        ACCEPT_RANGES = new AsciiString("accept-ranges");
        ACCEPT_PATCH = new AsciiString("accept-patch");
        ACCESS_CONTROL_ALLOW_CREDENTIALS = new AsciiString("access-control-allow-credentials");
        ACCESS_CONTROL_ALLOW_HEADERS = new AsciiString("access-control-allow-headers");
        ACCESS_CONTROL_ALLOW_METHODS = new AsciiString("access-control-allow-methods");
        ACCESS_CONTROL_ALLOW_ORIGIN = new AsciiString("access-control-allow-origin");
        ACCESS_CONTROL_EXPOSE_HEADERS = new AsciiString("access-control-expose-headers");
        ACCESS_CONTROL_MAX_AGE = new AsciiString("access-control-max-age");
        ACCESS_CONTROL_REQUEST_HEADERS = new AsciiString("access-control-request-headers");
        ACCESS_CONTROL_REQUEST_METHOD = new AsciiString("access-control-request-method");
        AGE = new AsciiString("age");
        ALLOW = new AsciiString("allow");
        AUTHORIZATION = new AsciiString("authorization");
        CACHE_CONTROL = new AsciiString("cache-control");
        CONNECTION = new AsciiString("connection");
        CONTENT_BASE = new AsciiString("content-base");
        CONTENT_ENCODING = new AsciiString("content-encoding");
        CONTENT_LANGUAGE = new AsciiString("content-language");
        CONTENT_LENGTH = new AsciiString("content-length");
        CONTENT_LOCATION = new AsciiString("content-location");
        CONTENT_TRANSFER_ENCODING = new AsciiString("content-transfer-encoding");
        CONTENT_DISPOSITION = new AsciiString("content-disposition");
        CONTENT_MD5 = new AsciiString("content-md5");
        CONTENT_RANGE = new AsciiString("content-range");
        CONTENT_TYPE = new AsciiString("content-type");
        COOKIE = new AsciiString("cookie");
        DATE = new AsciiString("date");
        ETAG = new AsciiString("etag");
        EXPECT = new AsciiString("expect");
        EXPIRES = new AsciiString("expires");
        FROM = new AsciiString("from");
        HOST = new AsciiString("host");
        IF_MATCH = new AsciiString("if-match");
        IF_MODIFIED_SINCE = new AsciiString("if-modified-since");
        IF_NONE_MATCH = new AsciiString("if-none-match");
        IF_RANGE = new AsciiString("if-range");
        IF_UNMODIFIED_SINCE = new AsciiString("if-unmodified-since");
        KEEP_ALIVE = new AsciiString("keep-alive");
        LAST_MODIFIED = new AsciiString("last-modified");
        LOCATION = new AsciiString("location");
        MAX_FORWARDS = new AsciiString("max-forwards");
        ORIGIN = new AsciiString("origin");
        PRAGMA = new AsciiString("pragma");
        PROXY_AUTHENTICATE = new AsciiString("proxy-authenticate");
        PROXY_AUTHORIZATION = new AsciiString("proxy-authorization");
        PROXY_CONNECTION = new AsciiString("proxy-connection");
        RANGE = new AsciiString("range");
        REFERER = new AsciiString("referer");
        RETRY_AFTER = new AsciiString("retry-after");
        SEC_WEBSOCKET_KEY1 = new AsciiString("sec-websocket-key1");
        SEC_WEBSOCKET_KEY2 = new AsciiString("sec-websocket-key2");
        SEC_WEBSOCKET_LOCATION = new AsciiString("sec-websocket-location");
        SEC_WEBSOCKET_ORIGIN = new AsciiString("sec-websocket-origin");
        SEC_WEBSOCKET_PROTOCOL = new AsciiString("sec-websocket-protocol");
        SEC_WEBSOCKET_VERSION = new AsciiString("sec-websocket-version");
        SEC_WEBSOCKET_KEY = new AsciiString("sec-websocket-key");
        SEC_WEBSOCKET_ACCEPT = new AsciiString("sec-websocket-accept");
        SEC_WEBSOCKET_EXTENSIONS = new AsciiString("sec-websocket-extensions");
        SERVER = new AsciiString("server");
        SET_COOKIE = new AsciiString("set-cookie");
        SET_COOKIE2 = new AsciiString("set-cookie2");
        TE = new AsciiString("te");
        TRAILER = new AsciiString("trailer");
        TRANSFER_ENCODING = new AsciiString("transfer-encoding");
        UPGRADE = new AsciiString("upgrade");
        USER_AGENT = new AsciiString("user-agent");
        VARY = new AsciiString("vary");
        VIA = new AsciiString("via");
        WARNING = new AsciiString("warning");
        WEBSOCKET_LOCATION = new AsciiString("websocket-location");
        WEBSOCKET_ORIGIN = new AsciiString("websocket-origin");
        WEBSOCKET_PROTOCOL = new AsciiString("websocket-protocol");
        WWW_AUTHENTICATE = new AsciiString("www-authenticate");
    }
}
