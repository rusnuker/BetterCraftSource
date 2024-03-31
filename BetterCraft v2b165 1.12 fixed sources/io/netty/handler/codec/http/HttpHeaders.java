// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;
import java.util.Set;
import java.util.List;
import io.netty.buffer.ByteBufUtil;
import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import io.netty.util.AsciiString;
import io.netty.handler.codec.DateFormatter;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public abstract class HttpHeaders implements Iterable<Map.Entry<String, String>>
{
    @Deprecated
    public static final HttpHeaders EMPTY_HEADERS;
    
    @Deprecated
    public static boolean isKeepAlive(final HttpMessage message) {
        return HttpUtil.isKeepAlive(message);
    }
    
    @Deprecated
    public static void setKeepAlive(final HttpMessage message, final boolean keepAlive) {
        HttpUtil.setKeepAlive(message, keepAlive);
    }
    
    @Deprecated
    public static String getHeader(final HttpMessage message, final String name) {
        return message.headers().get(name);
    }
    
    @Deprecated
    public static String getHeader(final HttpMessage message, final CharSequence name) {
        return message.headers().get(name);
    }
    
    @Deprecated
    public static String getHeader(final HttpMessage message, final String name, final String defaultValue) {
        return message.headers().get(name, defaultValue);
    }
    
    @Deprecated
    public static String getHeader(final HttpMessage message, final CharSequence name, final String defaultValue) {
        return message.headers().get(name, defaultValue);
    }
    
    @Deprecated
    public static void setHeader(final HttpMessage message, final String name, final Object value) {
        message.headers().set(name, value);
    }
    
    @Deprecated
    public static void setHeader(final HttpMessage message, final CharSequence name, final Object value) {
        message.headers().set(name, value);
    }
    
    @Deprecated
    public static void setHeader(final HttpMessage message, final String name, final Iterable<?> values) {
        message.headers().set(name, values);
    }
    
    @Deprecated
    public static void setHeader(final HttpMessage message, final CharSequence name, final Iterable<?> values) {
        message.headers().set(name, values);
    }
    
    @Deprecated
    public static void addHeader(final HttpMessage message, final String name, final Object value) {
        message.headers().add(name, value);
    }
    
    @Deprecated
    public static void addHeader(final HttpMessage message, final CharSequence name, final Object value) {
        message.headers().add(name, value);
    }
    
    @Deprecated
    public static void removeHeader(final HttpMessage message, final String name) {
        message.headers().remove(name);
    }
    
    @Deprecated
    public static void removeHeader(final HttpMessage message, final CharSequence name) {
        message.headers().remove(name);
    }
    
    @Deprecated
    public static void clearHeaders(final HttpMessage message) {
        message.headers().clear();
    }
    
    @Deprecated
    public static int getIntHeader(final HttpMessage message, final String name) {
        return getIntHeader(message, (CharSequence)name);
    }
    
    @Deprecated
    public static int getIntHeader(final HttpMessage message, final CharSequence name) {
        final String value = message.headers().get(name);
        if (value == null) {
            throw new NumberFormatException("header not found: " + (Object)name);
        }
        return Integer.parseInt(value);
    }
    
    @Deprecated
    public static int getIntHeader(final HttpMessage message, final String name, final int defaultValue) {
        return message.headers().getInt(name, defaultValue);
    }
    
    @Deprecated
    public static int getIntHeader(final HttpMessage message, final CharSequence name, final int defaultValue) {
        return message.headers().getInt(name, defaultValue);
    }
    
    @Deprecated
    public static void setIntHeader(final HttpMessage message, final String name, final int value) {
        message.headers().setInt(name, value);
    }
    
    @Deprecated
    public static void setIntHeader(final HttpMessage message, final CharSequence name, final int value) {
        message.headers().setInt(name, value);
    }
    
    @Deprecated
    public static void setIntHeader(final HttpMessage message, final String name, final Iterable<Integer> values) {
        message.headers().set(name, values);
    }
    
    @Deprecated
    public static void setIntHeader(final HttpMessage message, final CharSequence name, final Iterable<Integer> values) {
        message.headers().set(name, values);
    }
    
    @Deprecated
    public static void addIntHeader(final HttpMessage message, final String name, final int value) {
        message.headers().add(name, value);
    }
    
    @Deprecated
    public static void addIntHeader(final HttpMessage message, final CharSequence name, final int value) {
        message.headers().addInt(name, value);
    }
    
    @Deprecated
    public static Date getDateHeader(final HttpMessage message, final String name) throws ParseException {
        return getDateHeader(message, (CharSequence)name);
    }
    
    @Deprecated
    public static Date getDateHeader(final HttpMessage message, final CharSequence name) throws ParseException {
        final String value = message.headers().get(name);
        if (value == null) {
            throw new ParseException("header not found: " + (Object)name, 0);
        }
        final Date date = DateFormatter.parseHttpDate(value);
        if (date == null) {
            throw new ParseException("header can't be parsed into a Date: " + value, 0);
        }
        return date;
    }
    
    @Deprecated
    public static Date getDateHeader(final HttpMessage message, final String name, final Date defaultValue) {
        return getDateHeader(message, (CharSequence)name, defaultValue);
    }
    
    @Deprecated
    public static Date getDateHeader(final HttpMessage message, final CharSequence name, final Date defaultValue) {
        final String value = getHeader(message, name);
        final Date date = DateFormatter.parseHttpDate(value);
        return (date != null) ? date : defaultValue;
    }
    
    @Deprecated
    public static void setDateHeader(final HttpMessage message, final String name, final Date value) {
        setDateHeader(message, (CharSequence)name, value);
    }
    
    @Deprecated
    public static void setDateHeader(final HttpMessage message, final CharSequence name, final Date value) {
        if (value != null) {
            message.headers().set(name, DateFormatter.format(value));
        }
        else {
            message.headers().set(name, null);
        }
    }
    
    @Deprecated
    public static void setDateHeader(final HttpMessage message, final String name, final Iterable<Date> values) {
        message.headers().set(name, values);
    }
    
    @Deprecated
    public static void setDateHeader(final HttpMessage message, final CharSequence name, final Iterable<Date> values) {
        message.headers().set(name, values);
    }
    
    @Deprecated
    public static void addDateHeader(final HttpMessage message, final String name, final Date value) {
        message.headers().add(name, value);
    }
    
    @Deprecated
    public static void addDateHeader(final HttpMessage message, final CharSequence name, final Date value) {
        message.headers().add(name, value);
    }
    
    @Deprecated
    public static long getContentLength(final HttpMessage message) {
        return HttpUtil.getContentLength(message);
    }
    
    @Deprecated
    public static long getContentLength(final HttpMessage message, final long defaultValue) {
        return HttpUtil.getContentLength(message, defaultValue);
    }
    
    @Deprecated
    public static void setContentLength(final HttpMessage message, final long length) {
        HttpUtil.setContentLength(message, length);
    }
    
    @Deprecated
    public static String getHost(final HttpMessage message) {
        return message.headers().get(HttpHeaderNames.HOST);
    }
    
    @Deprecated
    public static String getHost(final HttpMessage message, final String defaultValue) {
        return message.headers().get(HttpHeaderNames.HOST, defaultValue);
    }
    
    @Deprecated
    public static void setHost(final HttpMessage message, final String value) {
        message.headers().set(HttpHeaderNames.HOST, value);
    }
    
    @Deprecated
    public static void setHost(final HttpMessage message, final CharSequence value) {
        message.headers().set(HttpHeaderNames.HOST, value);
    }
    
    @Deprecated
    public static Date getDate(final HttpMessage message) throws ParseException {
        return getDateHeader(message, HttpHeaderNames.DATE);
    }
    
    @Deprecated
    public static Date getDate(final HttpMessage message, final Date defaultValue) {
        return getDateHeader(message, HttpHeaderNames.DATE, defaultValue);
    }
    
    @Deprecated
    public static void setDate(final HttpMessage message, final Date value) {
        message.headers().set(HttpHeaderNames.DATE, value);
    }
    
    @Deprecated
    public static boolean is100ContinueExpected(final HttpMessage message) {
        return HttpUtil.is100ContinueExpected(message);
    }
    
    @Deprecated
    public static void set100ContinueExpected(final HttpMessage message) {
        HttpUtil.set100ContinueExpected(message, true);
    }
    
    @Deprecated
    public static void set100ContinueExpected(final HttpMessage message, final boolean set) {
        HttpUtil.set100ContinueExpected(message, set);
    }
    
    @Deprecated
    public static boolean isTransferEncodingChunked(final HttpMessage message) {
        return HttpUtil.isTransferEncodingChunked(message);
    }
    
    @Deprecated
    public static void removeTransferEncodingChunked(final HttpMessage m) {
        HttpUtil.setTransferEncodingChunked(m, false);
    }
    
    @Deprecated
    public static void setTransferEncodingChunked(final HttpMessage m) {
        HttpUtil.setTransferEncodingChunked(m, true);
    }
    
    @Deprecated
    public static boolean isContentLengthSet(final HttpMessage m) {
        return HttpUtil.isContentLengthSet(m);
    }
    
    @Deprecated
    public static boolean equalsIgnoreCase(final CharSequence name1, final CharSequence name2) {
        return AsciiString.contentEqualsIgnoreCase(name1, name2);
    }
    
    static void encode(final HttpHeaders headers, final ByteBuf buf) throws Exception {
        final Iterator<Map.Entry<CharSequence, CharSequence>> iter = headers.iteratorCharSequence();
        while (iter.hasNext()) {
            final Map.Entry<CharSequence, CharSequence> header = iter.next();
            HttpHeadersEncoder.encoderHeader(header.getKey(), header.getValue(), buf);
        }
    }
    
    public static void encodeAscii(final CharSequence seq, final ByteBuf buf) {
        if (seq instanceof AsciiString) {
            ByteBufUtil.copy((AsciiString)seq, 0, buf, seq.length());
        }
        else {
            HttpUtil.encodeAscii0(seq, buf);
        }
    }
    
    @Deprecated
    public static CharSequence newEntity(final String name) {
        return new AsciiString(name);
    }
    
    protected HttpHeaders() {
    }
    
    public abstract String get(final String p0);
    
    public String get(final CharSequence name) {
        return this.get(name.toString());
    }
    
    public String get(final CharSequence name, final String defaultValue) {
        final String value = this.get(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public abstract Integer getInt(final CharSequence p0);
    
    public abstract int getInt(final CharSequence p0, final int p1);
    
    public abstract Short getShort(final CharSequence p0);
    
    public abstract short getShort(final CharSequence p0, final short p1);
    
    public abstract Long getTimeMillis(final CharSequence p0);
    
    public abstract long getTimeMillis(final CharSequence p0, final long p1);
    
    public abstract List<String> getAll(final String p0);
    
    public List<String> getAll(final CharSequence name) {
        return this.getAll(name.toString());
    }
    
    public abstract List<Map.Entry<String, String>> entries();
    
    public abstract boolean contains(final String p0);
    
    @Deprecated
    @Override
    public abstract Iterator<Map.Entry<String, String>> iterator();
    
    public abstract Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence();
    
    public boolean contains(final CharSequence name) {
        return this.contains(name.toString());
    }
    
    public abstract boolean isEmpty();
    
    public abstract int size();
    
    public abstract Set<String> names();
    
    public abstract HttpHeaders add(final String p0, final Object p1);
    
    public HttpHeaders add(final CharSequence name, final Object value) {
        return this.add(name.toString(), value);
    }
    
    public abstract HttpHeaders add(final String p0, final Iterable<?> p1);
    
    public HttpHeaders add(final CharSequence name, final Iterable<?> values) {
        return this.add(name.toString(), values);
    }
    
    public HttpHeaders add(final HttpHeaders headers) {
        if (headers == null) {
            throw new NullPointerException("headers");
        }
        for (final Map.Entry<String, String> e : headers) {
            this.add(e.getKey(), e.getValue());
        }
        return this;
    }
    
    public abstract HttpHeaders addInt(final CharSequence p0, final int p1);
    
    public abstract HttpHeaders addShort(final CharSequence p0, final short p1);
    
    public abstract HttpHeaders set(final String p0, final Object p1);
    
    public HttpHeaders set(final CharSequence name, final Object value) {
        return this.set(name.toString(), value);
    }
    
    public abstract HttpHeaders set(final String p0, final Iterable<?> p1);
    
    public HttpHeaders set(final CharSequence name, final Iterable<?> values) {
        return this.set(name.toString(), values);
    }
    
    public HttpHeaders set(final HttpHeaders headers) {
        ObjectUtil.checkNotNull(headers, "headers");
        this.clear();
        if (headers.isEmpty()) {
            return this;
        }
        for (final Map.Entry<String, String> entry : headers) {
            this.add(entry.getKey(), entry.getValue());
        }
        return this;
    }
    
    public HttpHeaders setAll(final HttpHeaders headers) {
        ObjectUtil.checkNotNull(headers, "headers");
        if (headers.isEmpty()) {
            return this;
        }
        for (final Map.Entry<String, String> entry : headers) {
            this.set(entry.getKey(), entry.getValue());
        }
        return this;
    }
    
    public abstract HttpHeaders setInt(final CharSequence p0, final int p1);
    
    public abstract HttpHeaders setShort(final CharSequence p0, final short p1);
    
    public abstract HttpHeaders remove(final String p0);
    
    public HttpHeaders remove(final CharSequence name) {
        return this.remove(name.toString());
    }
    
    public abstract HttpHeaders clear();
    
    public boolean contains(final String name, final String value, final boolean ignoreCase) {
        final List<String> values = this.getAll(name);
        if (values.isEmpty()) {
            return false;
        }
        for (final String v : values) {
            if (ignoreCase) {
                if (v.equalsIgnoreCase(value)) {
                    return true;
                }
                continue;
            }
            else {
                if (v.equals(value)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public boolean containsValue(final CharSequence name, final CharSequence value, final boolean ignoreCase) {
        final List<String> values = this.getAll(name);
        if (values.isEmpty()) {
            return false;
        }
        for (final String v : values) {
            if (contains(v, value, ignoreCase)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean contains(final String value, final CharSequence expected, final boolean ignoreCase) {
        final String[] parts = value.split(",");
        if (ignoreCase) {
            for (final String s : parts) {
                if (AsciiString.contentEqualsIgnoreCase(expected, s.trim())) {
                    return true;
                }
            }
        }
        else {
            for (final String s : parts) {
                if (AsciiString.contentEquals(expected, s.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public final String getAsString(final CharSequence name) {
        return this.get(name);
    }
    
    public final List<String> getAllAsString(final CharSequence name) {
        return this.getAll(name);
    }
    
    public final Iterator<Map.Entry<String, String>> iteratorAsString() {
        return this.iterator();
    }
    
    public boolean contains(final CharSequence name, final CharSequence value, final boolean ignoreCase) {
        return this.contains(name.toString(), value.toString(), ignoreCase);
    }
    
    static {
        EMPTY_HEADERS = EmptyHttpHeaders.instance();
    }
    
    @Deprecated
    public static final class Names
    {
        public static final String ACCEPT = "Accept";
        public static final String ACCEPT_CHARSET = "Accept-Charset";
        public static final String ACCEPT_ENCODING = "Accept-Encoding";
        public static final String ACCEPT_LANGUAGE = "Accept-Language";
        public static final String ACCEPT_RANGES = "Accept-Ranges";
        public static final String ACCEPT_PATCH = "Accept-Patch";
        public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
        public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
        public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
        public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
        public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
        public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
        public static final String AGE = "Age";
        public static final String ALLOW = "Allow";
        public static final String AUTHORIZATION = "Authorization";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String CONNECTION = "Connection";
        public static final String CONTENT_BASE = "Content-Base";
        public static final String CONTENT_ENCODING = "Content-Encoding";
        public static final String CONTENT_LANGUAGE = "Content-Language";
        public static final String CONTENT_LENGTH = "Content-Length";
        public static final String CONTENT_LOCATION = "Content-Location";
        public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
        public static final String CONTENT_MD5 = "Content-MD5";
        public static final String CONTENT_RANGE = "Content-Range";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String COOKIE = "Cookie";
        public static final String DATE = "Date";
        public static final String ETAG = "ETag";
        public static final String EXPECT = "Expect";
        public static final String EXPIRES = "Expires";
        public static final String FROM = "From";
        public static final String HOST = "Host";
        public static final String IF_MATCH = "If-Match";
        public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
        public static final String IF_NONE_MATCH = "If-None-Match";
        public static final String IF_RANGE = "If-Range";
        public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
        public static final String LAST_MODIFIED = "Last-Modified";
        public static final String LOCATION = "Location";
        public static final String MAX_FORWARDS = "Max-Forwards";
        public static final String ORIGIN = "Origin";
        public static final String PRAGMA = "Pragma";
        public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
        public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
        public static final String RANGE = "Range";
        public static final String REFERER = "Referer";
        public static final String RETRY_AFTER = "Retry-After";
        public static final String SEC_WEBSOCKET_KEY1 = "Sec-WebSocket-Key1";
        public static final String SEC_WEBSOCKET_KEY2 = "Sec-WebSocket-Key2";
        public static final String SEC_WEBSOCKET_LOCATION = "Sec-WebSocket-Location";
        public static final String SEC_WEBSOCKET_ORIGIN = "Sec-WebSocket-Origin";
        public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
        public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
        public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
        public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
        public static final String SERVER = "Server";
        public static final String SET_COOKIE = "Set-Cookie";
        public static final String SET_COOKIE2 = "Set-Cookie2";
        public static final String TE = "TE";
        public static final String TRAILER = "Trailer";
        public static final String TRANSFER_ENCODING = "Transfer-Encoding";
        public static final String UPGRADE = "Upgrade";
        public static final String USER_AGENT = "User-Agent";
        public static final String VARY = "Vary";
        public static final String VIA = "Via";
        public static final String WARNING = "Warning";
        public static final String WEBSOCKET_LOCATION = "WebSocket-Location";
        public static final String WEBSOCKET_ORIGIN = "WebSocket-Origin";
        public static final String WEBSOCKET_PROTOCOL = "WebSocket-Protocol";
        public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
        
        private Names() {
        }
    }
    
    @Deprecated
    public static final class Values
    {
        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String BASE64 = "base64";
        public static final String BINARY = "binary";
        public static final String BOUNDARY = "boundary";
        public static final String BYTES = "bytes";
        public static final String CHARSET = "charset";
        public static final String CHUNKED = "chunked";
        public static final String CLOSE = "close";
        public static final String COMPRESS = "compress";
        public static final String CONTINUE = "100-continue";
        public static final String DEFLATE = "deflate";
        public static final String GZIP = "gzip";
        public static final String GZIP_DEFLATE = "gzip,deflate";
        public static final String IDENTITY = "identity";
        public static final String KEEP_ALIVE = "keep-alive";
        public static final String MAX_AGE = "max-age";
        public static final String MAX_STALE = "max-stale";
        public static final String MIN_FRESH = "min-fresh";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
        public static final String MUST_REVALIDATE = "must-revalidate";
        public static final String NO_CACHE = "no-cache";
        public static final String NO_STORE = "no-store";
        public static final String NO_TRANSFORM = "no-transform";
        public static final String NONE = "none";
        public static final String ONLY_IF_CACHED = "only-if-cached";
        public static final String PRIVATE = "private";
        public static final String PROXY_REVALIDATE = "proxy-revalidate";
        public static final String PUBLIC = "public";
        public static final String QUOTED_PRINTABLE = "quoted-printable";
        public static final String S_MAXAGE = "s-maxage";
        public static final String TRAILERS = "trailers";
        public static final String UPGRADE = "Upgrade";
        public static final String WEBSOCKET = "WebSocket";
        
        private Values() {
        }
    }
}
