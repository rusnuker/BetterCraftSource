// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.AsciiString;

public final class HttpHeaderValues
{
    public static final AsciiString APPLICATION_JSON;
    public static final AsciiString APPLICATION_X_WWW_FORM_URLENCODED;
    public static final AsciiString APPLICATION_OCTET_STREAM;
    public static final AsciiString ATTACHMENT;
    public static final AsciiString BASE64;
    public static final AsciiString BINARY;
    public static final AsciiString BOUNDARY;
    public static final AsciiString BYTES;
    public static final AsciiString CHARSET;
    public static final AsciiString CHUNKED;
    public static final AsciiString CLOSE;
    public static final AsciiString COMPRESS;
    public static final AsciiString CONTINUE;
    public static final AsciiString DEFLATE;
    public static final AsciiString X_DEFLATE;
    public static final AsciiString FILE;
    public static final AsciiString FILENAME;
    public static final AsciiString FORM_DATA;
    public static final AsciiString GZIP;
    public static final AsciiString GZIP_DEFLATE;
    public static final AsciiString X_GZIP;
    public static final AsciiString IDENTITY;
    public static final AsciiString KEEP_ALIVE;
    public static final AsciiString MAX_AGE;
    public static final AsciiString MAX_STALE;
    public static final AsciiString MIN_FRESH;
    public static final AsciiString MULTIPART_FORM_DATA;
    public static final AsciiString MULTIPART_MIXED;
    public static final AsciiString MUST_REVALIDATE;
    public static final AsciiString NAME;
    public static final AsciiString NO_CACHE;
    public static final AsciiString NO_STORE;
    public static final AsciiString NO_TRANSFORM;
    public static final AsciiString NONE;
    public static final AsciiString ZERO;
    public static final AsciiString ONLY_IF_CACHED;
    public static final AsciiString PRIVATE;
    public static final AsciiString PROXY_REVALIDATE;
    public static final AsciiString PUBLIC;
    public static final AsciiString QUOTED_PRINTABLE;
    public static final AsciiString S_MAXAGE;
    public static final AsciiString TEXT_PLAIN;
    public static final AsciiString TRAILERS;
    public static final AsciiString UPGRADE;
    public static final AsciiString WEBSOCKET;
    
    private HttpHeaderValues() {
    }
    
    static {
        APPLICATION_JSON = new AsciiString("application/json");
        APPLICATION_X_WWW_FORM_URLENCODED = new AsciiString("application/x-www-form-urlencoded");
        APPLICATION_OCTET_STREAM = new AsciiString("application/octet-stream");
        ATTACHMENT = new AsciiString("attachment");
        BASE64 = new AsciiString("base64");
        BINARY = new AsciiString("binary");
        BOUNDARY = new AsciiString("boundary");
        BYTES = new AsciiString("bytes");
        CHARSET = new AsciiString("charset");
        CHUNKED = new AsciiString("chunked");
        CLOSE = new AsciiString("close");
        COMPRESS = new AsciiString("compress");
        CONTINUE = new AsciiString("100-continue");
        DEFLATE = new AsciiString("deflate");
        X_DEFLATE = new AsciiString("x-deflate");
        FILE = new AsciiString("file");
        FILENAME = new AsciiString("filename");
        FORM_DATA = new AsciiString("form-data");
        GZIP = new AsciiString("gzip");
        GZIP_DEFLATE = new AsciiString("gzip,deflate");
        X_GZIP = new AsciiString("x-gzip");
        IDENTITY = new AsciiString("identity");
        KEEP_ALIVE = new AsciiString("keep-alive");
        MAX_AGE = new AsciiString("max-age");
        MAX_STALE = new AsciiString("max-stale");
        MIN_FRESH = new AsciiString("min-fresh");
        MULTIPART_FORM_DATA = new AsciiString("multipart/form-data");
        MULTIPART_MIXED = new AsciiString("multipart/mixed");
        MUST_REVALIDATE = new AsciiString("must-revalidate");
        NAME = new AsciiString("name");
        NO_CACHE = new AsciiString("no-cache");
        NO_STORE = new AsciiString("no-store");
        NO_TRANSFORM = new AsciiString("no-transform");
        NONE = new AsciiString("none");
        ZERO = new AsciiString("0");
        ONLY_IF_CACHED = new AsciiString("only-if-cached");
        PRIVATE = new AsciiString("private");
        PROXY_REVALIDATE = new AsciiString("proxy-revalidate");
        PUBLIC = new AsciiString("public");
        QUOTED_PRINTABLE = new AsciiString("quoted-printable");
        S_MAXAGE = new AsciiString("s-maxage");
        TEXT_PLAIN = new AsciiString("text/plain");
        TRAILERS = new AsciiString("trailers");
        UPGRADE = new AsciiString("upgrade");
        WEBSOCKET = new AsciiString("websocket");
    }
}
