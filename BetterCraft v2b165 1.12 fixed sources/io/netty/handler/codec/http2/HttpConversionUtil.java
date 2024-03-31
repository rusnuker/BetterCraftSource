// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.HttpScheme;
import io.netty.util.internal.StringUtil;
import io.netty.util.ByteProcessor;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import java.net.URI;
import io.netty.handler.codec.http.HttpMessage;
import java.util.Iterator;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.util.Map;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.util.internal.ObjectUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.AsciiString;

public final class HttpConversionUtil
{
    private static final CharSequenceMap<AsciiString> HTTP_TO_HTTP2_HEADER_BLACKLIST;
    public static final HttpMethod OUT_OF_MESSAGE_SEQUENCE_METHOD;
    public static final String OUT_OF_MESSAGE_SEQUENCE_PATH = "";
    public static final HttpResponseStatus OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE;
    private static final AsciiString EMPTY_REQUEST_PATH;
    
    private HttpConversionUtil() {
    }
    
    public static HttpResponseStatus parseStatus(final CharSequence status) throws Http2Exception {
        HttpResponseStatus result;
        try {
            result = HttpResponseStatus.parseLine(status);
            if (result == HttpResponseStatus.SWITCHING_PROTOCOLS) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 status code '%d'", result.code());
            }
        }
        catch (final Http2Exception e) {
            throw e;
        }
        catch (final Throwable t) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t, "Unrecognized HTTP status code '%s' encountered in translation to HTTP/1.x", status);
        }
        return result;
    }
    
    public static FullHttpResponse toHttpResponse(final int streamId, final Http2Headers http2Headers, final ByteBufAllocator alloc, final boolean validateHttpHeaders) throws Http2Exception {
        final HttpResponseStatus status = parseStatus(http2Headers.status());
        final FullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, alloc.buffer(), validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
        }
        catch (final Http2Exception e) {
            msg.release();
            throw e;
        }
        catch (final Throwable t) {
            msg.release();
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
        return msg;
    }
    
    public static FullHttpRequest toFullHttpRequest(final int streamId, final Http2Headers http2Headers, final ByteBufAllocator alloc, final boolean validateHttpHeaders) throws Http2Exception {
        final CharSequence method = ObjectUtil.checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
        final CharSequence path = ObjectUtil.checkNotNull(http2Headers.path(), "path header cannot be null in conversion to HTTP/1.x");
        final FullHttpRequest msg = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), alloc.buffer(), validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
        }
        catch (final Http2Exception e) {
            msg.release();
            throw e;
        }
        catch (final Throwable t) {
            msg.release();
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
        return msg;
    }
    
    public static HttpRequest toHttpRequest(final int streamId, final Http2Headers http2Headers, final boolean validateHttpHeaders) throws Http2Exception {
        final CharSequence method = ObjectUtil.checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
        final CharSequence path = ObjectUtil.checkNotNull(http2Headers.path(), "path header cannot be null in conversion to HTTP/1.x");
        final HttpRequest msg = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg.headers(), msg.protocolVersion(), false, true);
        }
        catch (final Http2Exception e) {
            throw e;
        }
        catch (final Throwable t) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
        return msg;
    }
    
    public static void addHttp2ToHttpHeaders(final int streamId, final Http2Headers sourceHeaders, final FullHttpMessage destinationMessage, final boolean addToTrailer) throws Http2Exception {
        addHttp2ToHttpHeaders(streamId, sourceHeaders, addToTrailer ? destinationMessage.trailingHeaders() : destinationMessage.headers(), destinationMessage.protocolVersion(), addToTrailer, destinationMessage instanceof HttpRequest);
    }
    
    public static void addHttp2ToHttpHeaders(final int streamId, final Http2Headers inputHeaders, final HttpHeaders outputHeaders, final HttpVersion httpVersion, final boolean isTrailer, final boolean isRequest) throws Http2Exception {
        final Http2ToHttpHeaderTranslator translator = new Http2ToHttpHeaderTranslator(streamId, outputHeaders, isRequest);
        try {
            for (final Map.Entry<CharSequence, CharSequence> entry : inputHeaders) {
                translator.translate(entry);
            }
        }
        catch (final Http2Exception ex) {
            throw ex;
        }
        catch (final Throwable t) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
        outputHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
        outputHeaders.remove(HttpHeaderNames.TRAILER);
        if (!isTrailer) {
            outputHeaders.setInt(ExtensionHeaderNames.STREAM_ID.text(), streamId);
            HttpUtil.setKeepAlive(outputHeaders, httpVersion, true);
        }
    }
    
    public static Http2Headers toHttp2Headers(final HttpMessage in, final boolean validateHeaders) {
        final HttpHeaders inHeaders = in.headers();
        final Http2Headers out = new DefaultHttp2Headers(validateHeaders, inHeaders.size());
        if (in instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest)in;
            final URI requestTargetUri = URI.create(request.uri());
            out.path(toHttp2Path(requestTargetUri));
            out.method(request.method().asciiName());
            setHttp2Scheme(inHeaders, requestTargetUri, out);
            if (!HttpUtil.isOriginForm(requestTargetUri) && !HttpUtil.isAsteriskForm(requestTargetUri)) {
                final String host = inHeaders.getAsString(HttpHeaderNames.HOST);
                setHttp2Authority((host == null || host.isEmpty()) ? requestTargetUri.getAuthority() : host, out);
            }
        }
        else if (in instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse)in;
            out.status(new AsciiString(Integer.toString(response.status().code())));
        }
        toHttp2Headers(inHeaders, out);
        return out;
    }
    
    public static Http2Headers toHttp2Headers(final HttpHeaders inHeaders, final boolean validateHeaders) {
        if (inHeaders.isEmpty()) {
            return EmptyHttp2Headers.INSTANCE;
        }
        final Http2Headers out = new DefaultHttp2Headers(validateHeaders, inHeaders.size());
        toHttp2Headers(inHeaders, out);
        return out;
    }
    
    public static void toHttp2Headers(final HttpHeaders inHeaders, final Http2Headers out) {
        final Iterator<Map.Entry<CharSequence, CharSequence>> iter = inHeaders.iteratorCharSequence();
        while (iter.hasNext()) {
            final Map.Entry<CharSequence, CharSequence> entry = iter.next();
            final AsciiString aName = AsciiString.of(entry.getKey()).toLowerCase();
            if (!HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.contains(aName)) {
                if (aName.contentEqualsIgnoreCase(HttpHeaderNames.TE) && !AsciiString.contentEqualsIgnoreCase(entry.getValue(), HttpHeaderValues.TRAILERS)) {
                    throw new IllegalArgumentException("Invalid value for " + (Object)HttpHeaderNames.TE + ": " + (Object)entry.getValue());
                }
                if (aName.contentEqualsIgnoreCase(HttpHeaderNames.COOKIE)) {
                    final AsciiString value = AsciiString.of(entry.getValue());
                    try {
                        int index = value.forEachByte(ByteProcessor.FIND_SEMI_COLON);
                        if (index != -1) {
                            int start = 0;
                            do {
                                ((Headers<AsciiString, AsciiString, Headers>)out).add(HttpHeaderNames.COOKIE, value.subSequence(start, index, false));
                                start = index + 2;
                            } while (start < value.length() && (index = value.forEachByte(start, value.length() - start, ByteProcessor.FIND_SEMI_COLON)) != -1);
                            if (start >= value.length()) {
                                throw new IllegalArgumentException("cookie value is of unexpected format: " + (Object)value);
                            }
                            ((Headers<AsciiString, AsciiString, Headers>)out).add(HttpHeaderNames.COOKIE, value.subSequence(start, value.length(), false));
                        }
                        else {
                            ((Headers<AsciiString, AsciiString, Headers>)out).add(HttpHeaderNames.COOKIE, value);
                        }
                    }
                    catch (final Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
                else {
                    ((Headers<AsciiString, CharSequence, Headers>)out).add(aName, entry.getValue());
                }
            }
        }
    }
    
    private static AsciiString toHttp2Path(final URI uri) {
        final StringBuilder pathBuilder = new StringBuilder(StringUtil.length(uri.getRawPath()) + StringUtil.length(uri.getRawQuery()) + StringUtil.length(uri.getRawFragment()) + 2);
        if (!StringUtil.isNullOrEmpty(uri.getRawPath())) {
            pathBuilder.append(uri.getRawPath());
        }
        if (!StringUtil.isNullOrEmpty(uri.getRawQuery())) {
            pathBuilder.append('?');
            pathBuilder.append(uri.getRawQuery());
        }
        if (!StringUtil.isNullOrEmpty(uri.getRawFragment())) {
            pathBuilder.append('#');
            pathBuilder.append(uri.getRawFragment());
        }
        final String path = pathBuilder.toString();
        return path.isEmpty() ? HttpConversionUtil.EMPTY_REQUEST_PATH : new AsciiString(path);
    }
    
    private static void setHttp2Authority(final String autority, final Http2Headers out) {
        if (autority != null) {
            final int endOfUserInfo = autority.indexOf(64);
            if (endOfUserInfo < 0) {
                out.authority(new AsciiString(autority));
            }
            else {
                if (endOfUserInfo + 1 >= autority.length()) {
                    throw new IllegalArgumentException("autority: " + autority);
                }
                out.authority(new AsciiString(autority.substring(endOfUserInfo + 1)));
            }
        }
    }
    
    private static void setHttp2Scheme(final HttpHeaders in, final URI uri, final Http2Headers out) {
        final String value = uri.getScheme();
        if (value != null) {
            out.scheme(new AsciiString(value));
            return;
        }
        final CharSequence cValue = in.get(ExtensionHeaderNames.SCHEME.text());
        if (cValue != null) {
            out.scheme(AsciiString.of(cValue));
            return;
        }
        if (uri.getPort() == HttpScheme.HTTPS.port()) {
            out.scheme(HttpScheme.HTTPS.name());
        }
        else {
            if (uri.getPort() != HttpScheme.HTTP.port()) {
                throw new IllegalArgumentException(":scheme must be specified. see https://tools.ietf.org/html/rfc7540#section-8.1.2.3");
            }
            out.scheme(HttpScheme.HTTP.name());
        }
    }
    
    static {
        (HTTP_TO_HTTP2_HEADER_BLACKLIST = new CharSequenceMap<AsciiString>()).add(HttpHeaderNames.CONNECTION, AsciiString.EMPTY_STRING);
        final AsciiString keepAlive = HttpHeaderNames.KEEP_ALIVE;
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add(keepAlive, AsciiString.EMPTY_STRING);
        final AsciiString proxyConnection = HttpHeaderNames.PROXY_CONNECTION;
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add(proxyConnection, AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.TRANSFER_ENCODING, AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.HOST, AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.UPGRADE, AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.STREAM_ID.text(), AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.SCHEME.text(), AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.PATH.text(), AsciiString.EMPTY_STRING);
        OUT_OF_MESSAGE_SEQUENCE_METHOD = HttpMethod.OPTIONS;
        OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE = HttpResponseStatus.OK;
        EMPTY_REQUEST_PATH = new AsciiString("/");
    }
    
    public enum ExtensionHeaderNames
    {
        STREAM_ID("x-http2-stream-id"), 
        SCHEME("x-http2-scheme"), 
        PATH("x-http2-path"), 
        STREAM_PROMISE_ID("x-http2-stream-promise-id"), 
        STREAM_DEPENDENCY_ID("x-http2-stream-dependency-id"), 
        STREAM_WEIGHT("x-http2-stream-weight");
        
        private final AsciiString text;
        
        private ExtensionHeaderNames(final String text) {
            this.text = new AsciiString(text);
        }
        
        public AsciiString text() {
            return this.text;
        }
    }
    
    private static final class Http2ToHttpHeaderTranslator
    {
        private static final CharSequenceMap<AsciiString> REQUEST_HEADER_TRANSLATIONS;
        private static final CharSequenceMap<AsciiString> RESPONSE_HEADER_TRANSLATIONS;
        private final int streamId;
        private final HttpHeaders output;
        private final CharSequenceMap<AsciiString> translations;
        
        Http2ToHttpHeaderTranslator(final int streamId, final HttpHeaders output, final boolean request) {
            this.streamId = streamId;
            this.output = output;
            this.translations = (request ? Http2ToHttpHeaderTranslator.REQUEST_HEADER_TRANSLATIONS : Http2ToHttpHeaderTranslator.RESPONSE_HEADER_TRANSLATIONS);
        }
        
        public void translate(final Map.Entry<CharSequence, CharSequence> entry) throws Http2Exception {
            final CharSequence name = entry.getKey();
            final CharSequence value = entry.getValue();
            final AsciiString translatedName = this.translations.get(name);
            if (translatedName != null) {
                this.output.add(translatedName, AsciiString.of(value));
            }
            else if (!Http2Headers.PseudoHeaderName.isPseudoHeader(name)) {
                if (name.length() == 0 || name.charAt(0) == ':') {
                    throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 header '%s' encountered in translation to HTTP/1.x", name);
                }
                if (HttpHeaderNames.COOKIE.equals(name)) {
                    final String existingCookie = this.output.get(HttpHeaderNames.COOKIE);
                    this.output.set(HttpHeaderNames.COOKIE, (existingCookie != null) ? (existingCookie + "; " + (Object)value) : value);
                }
                else {
                    this.output.add(name, value);
                }
            }
        }
        
        static {
            REQUEST_HEADER_TRANSLATIONS = new CharSequenceMap<AsciiString>();
            (RESPONSE_HEADER_TRANSLATIONS = new CharSequenceMap<AsciiString>()).add(Http2Headers.PseudoHeaderName.AUTHORITY.value(), HttpHeaderNames.HOST);
            Http2ToHttpHeaderTranslator.RESPONSE_HEADER_TRANSLATIONS.add(Http2Headers.PseudoHeaderName.SCHEME.value(), ExtensionHeaderNames.SCHEME.text());
            Http2ToHttpHeaderTranslator.REQUEST_HEADER_TRANSLATIONS.add((Headers<?, ?, ?>)Http2ToHttpHeaderTranslator.RESPONSE_HEADER_TRANSLATIONS);
            Http2ToHttpHeaderTranslator.RESPONSE_HEADER_TRANSLATIONS.add(Http2Headers.PseudoHeaderName.PATH.value(), ExtensionHeaderNames.PATH.text());
        }
    }
}
