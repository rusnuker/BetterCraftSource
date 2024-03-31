// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import java.nio.charset.UnsupportedCharsetException;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.net.URI;
import io.netty.util.AsciiString;

public final class HttpUtil
{
    @Deprecated
    static final EmptyHttpHeaders EMPTY_HEADERS;
    private static final AsciiString CHARSET_EQUALS;
    private static final AsciiString SEMICOLON;
    
    private HttpUtil() {
    }
    
    public static boolean isOriginForm(final URI uri) {
        return uri.getScheme() == null && uri.getSchemeSpecificPart() == null && uri.getHost() == null && uri.getAuthority() == null;
    }
    
    public static boolean isAsteriskForm(final URI uri) {
        return "*".equals(uri.getPath()) && uri.getScheme() == null && uri.getSchemeSpecificPart() == null && uri.getHost() == null && uri.getAuthority() == null && uri.getQuery() == null && uri.getFragment() == null;
    }
    
    public static boolean isKeepAlive(final HttpMessage message) {
        final CharSequence connection = message.headers().get(HttpHeaderNames.CONNECTION);
        if (connection != null && HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection)) {
            return false;
        }
        if (message.protocolVersion().isKeepAliveDefault()) {
            return !HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection);
        }
        return HttpHeaderValues.KEEP_ALIVE.contentEqualsIgnoreCase(connection);
    }
    
    public static void setKeepAlive(final HttpMessage message, final boolean keepAlive) {
        setKeepAlive(message.headers(), message.protocolVersion(), keepAlive);
    }
    
    public static void setKeepAlive(final HttpHeaders h, final HttpVersion httpVersion, final boolean keepAlive) {
        if (httpVersion.isKeepAliveDefault()) {
            if (keepAlive) {
                h.remove(HttpHeaderNames.CONNECTION);
            }
            else {
                h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            }
        }
        else if (keepAlive) {
            h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        else {
            h.remove(HttpHeaderNames.CONNECTION);
        }
    }
    
    public static long getContentLength(final HttpMessage message) {
        final String value = message.headers().get(HttpHeaderNames.CONTENT_LENGTH);
        if (value != null) {
            return Long.parseLong(value);
        }
        final long webSocketContentLength = getWebSocketContentLength(message);
        if (webSocketContentLength >= 0L) {
            return webSocketContentLength;
        }
        throw new NumberFormatException("header not found: " + (Object)HttpHeaderNames.CONTENT_LENGTH);
    }
    
    public static long getContentLength(final HttpMessage message, final long defaultValue) {
        final String value = message.headers().get(HttpHeaderNames.CONTENT_LENGTH);
        if (value != null) {
            try {
                return Long.parseLong(value);
            }
            catch (final NumberFormatException ignore) {
                return defaultValue;
            }
        }
        final long webSocketContentLength = getWebSocketContentLength(message);
        if (webSocketContentLength >= 0L) {
            return webSocketContentLength;
        }
        return defaultValue;
    }
    
    public static int getContentLength(final HttpMessage message, final int defaultValue) {
        return (int)Math.min(2147483647L, getContentLength(message, (long)defaultValue));
    }
    
    private static int getWebSocketContentLength(final HttpMessage message) {
        final HttpHeaders h = message.headers();
        if (message instanceof HttpRequest) {
            final HttpRequest req = (HttpRequest)message;
            if (HttpMethod.GET.equals(req.method()) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2)) {
                return 8;
            }
        }
        else if (message instanceof HttpResponse) {
            final HttpResponse res = (HttpResponse)message;
            if (res.status().code() == 101 && h.contains(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_LOCATION)) {
                return 16;
            }
        }
        return -1;
    }
    
    public static void setContentLength(final HttpMessage message, final long length) {
        message.headers().set(HttpHeaderNames.CONTENT_LENGTH, length);
    }
    
    public static boolean isContentLengthSet(final HttpMessage m) {
        return m.headers().contains(HttpHeaderNames.CONTENT_LENGTH);
    }
    
    public static boolean is100ContinueExpected(final HttpMessage message) {
        if (!isExpectHeaderValid(message)) {
            return false;
        }
        final String expectValue = message.headers().get(HttpHeaderNames.EXPECT);
        return HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue);
    }
    
    static boolean isUnsupportedExpectation(final HttpMessage message) {
        if (!isExpectHeaderValid(message)) {
            return false;
        }
        final String expectValue = message.headers().get(HttpHeaderNames.EXPECT);
        return expectValue != null && !HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue);
    }
    
    private static boolean isExpectHeaderValid(final HttpMessage message) {
        return message instanceof HttpRequest && message.protocolVersion().compareTo(HttpVersion.HTTP_1_1) >= 0;
    }
    
    public static void set100ContinueExpected(final HttpMessage message, final boolean expected) {
        if (expected) {
            message.headers().set(HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE);
        }
        else {
            message.headers().remove(HttpHeaderNames.EXPECT);
        }
    }
    
    public static boolean isTransferEncodingChunked(final HttpMessage message) {
        return message.headers().contains(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED, true);
    }
    
    public static void setTransferEncodingChunked(final HttpMessage m, final boolean chunked) {
        if (chunked) {
            m.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
            m.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
        }
        else {
            final List<String> encodings = m.headers().getAll(HttpHeaderNames.TRANSFER_ENCODING);
            if (encodings.isEmpty()) {
                return;
            }
            final List<CharSequence> values = new ArrayList<CharSequence>(encodings);
            final Iterator<CharSequence> valuesIt = values.iterator();
            while (valuesIt.hasNext()) {
                final CharSequence value = valuesIt.next();
                if (HttpHeaderValues.CHUNKED.contentEqualsIgnoreCase(value)) {
                    valuesIt.remove();
                }
            }
            if (values.isEmpty()) {
                m.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
            }
            else {
                m.headers().set(HttpHeaderNames.TRANSFER_ENCODING, values);
            }
        }
    }
    
    public static Charset getCharset(final HttpMessage message) {
        return getCharset(message, CharsetUtil.ISO_8859_1);
    }
    
    public static Charset getCharset(final HttpMessage message, final Charset defaultCharset) {
        final CharSequence charsetCharSequence = getCharsetAsSequence(message);
        if (charsetCharSequence != null) {
            try {
                return Charset.forName(charsetCharSequence.toString());
            }
            catch (final UnsupportedCharsetException unsupportedException) {
                return defaultCharset;
            }
        }
        return defaultCharset;
    }
    
    @Deprecated
    public static CharSequence getCharsetAsString(final HttpMessage message) {
        return getCharsetAsSequence(message);
    }
    
    public static CharSequence getCharsetAsSequence(final HttpMessage message) {
        final CharSequence contentTypeValue = message.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue != null) {
            final int indexOfCharset = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, HttpUtil.CHARSET_EQUALS, 0);
            if (indexOfCharset != -1) {
                final int indexOfEncoding = indexOfCharset + HttpUtil.CHARSET_EQUALS.length();
                if (indexOfEncoding < contentTypeValue.length()) {
                    return contentTypeValue.subSequence(indexOfEncoding, contentTypeValue.length());
                }
            }
        }
        return null;
    }
    
    public static CharSequence getMimeType(final HttpMessage message) {
        final CharSequence contentTypeValue = message.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue == null) {
            return null;
        }
        final int indexOfSemicolon = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, HttpUtil.SEMICOLON, 0);
        if (indexOfSemicolon != -1) {
            return contentTypeValue.subSequence(0, indexOfSemicolon);
        }
        return (contentTypeValue.length() > 0) ? contentTypeValue : null;
    }
    
    static void encodeAscii0(final CharSequence seq, final ByteBuf buf) {
        for (int length = seq.length(), i = 0; i < length; ++i) {
            buf.writeByte(c2b(seq.charAt(i)));
        }
    }
    
    private static byte c2b(final char c) {
        return (byte)((c > '\u00ff') ? 63 : ((byte)c));
    }
    
    static {
        EMPTY_HEADERS = new EmptyHttpHeaders();
        CHARSET_EQUALS = AsciiString.of((Object)HttpHeaderValues.CHARSET + "=");
        SEMICOLON = AsciiString.of(";");
    }
}
