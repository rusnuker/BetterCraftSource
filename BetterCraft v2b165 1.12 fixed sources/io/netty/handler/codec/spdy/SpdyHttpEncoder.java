// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.util.Iterator;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.AsciiString;
import java.util.Map;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.MessageToMessageEncoder;

public class SpdyHttpEncoder extends MessageToMessageEncoder<HttpObject>
{
    private int currentStreamId;
    private final boolean validateHeaders;
    private final boolean headersToLowerCase;
    
    public SpdyHttpEncoder(final SpdyVersion version) {
        this(version, true, true);
    }
    
    public SpdyHttpEncoder(final SpdyVersion version, final boolean headersToLowerCase, final boolean validateHeaders) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.headersToLowerCase = headersToLowerCase;
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final HttpObject msg, final List<Object> out) throws Exception {
        boolean valid = false;
        boolean last = false;
        if (msg instanceof HttpRequest) {
            final HttpRequest httpRequest = (HttpRequest)msg;
            final SpdySynStreamFrame spdySynStreamFrame = this.createSynStreamFrame(httpRequest);
            out.add(spdySynStreamFrame);
            last = (spdySynStreamFrame.isLast() || spdySynStreamFrame.isUnidirectional());
            valid = true;
        }
        if (msg instanceof HttpResponse) {
            final HttpResponse httpResponse = (HttpResponse)msg;
            final SpdyHeadersFrame spdyHeadersFrame = this.createHeadersFrame(httpResponse);
            out.add(spdyHeadersFrame);
            last = spdyHeadersFrame.isLast();
            valid = true;
        }
        if (msg instanceof HttpContent && !last) {
            final HttpContent chunk = (HttpContent)msg;
            chunk.content().retain();
            final SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(this.currentStreamId, chunk.content());
            if (chunk instanceof LastHttpContent) {
                final LastHttpContent trailer = (LastHttpContent)chunk;
                final HttpHeaders trailers = trailer.trailingHeaders();
                if (trailers.isEmpty()) {
                    spdyDataFrame.setLast(true);
                    out.add(spdyDataFrame);
                }
                else {
                    final SpdyHeadersFrame spdyHeadersFrame2 = new DefaultSpdyHeadersFrame(this.currentStreamId, this.validateHeaders);
                    spdyHeadersFrame2.setLast(true);
                    final Iterator<Map.Entry<CharSequence, CharSequence>> itr = trailers.iteratorCharSequence();
                    while (itr.hasNext()) {
                        final Map.Entry<CharSequence, CharSequence> entry = itr.next();
                        final CharSequence headerName = this.headersToLowerCase ? AsciiString.of(entry.getKey()).toLowerCase() : entry.getKey();
                        ((Headers<CharSequence, CharSequence, Headers>)spdyHeadersFrame2.headers()).add(headerName, entry.getValue());
                    }
                    out.add(spdyDataFrame);
                    out.add(spdyHeadersFrame2);
                }
            }
            else {
                out.add(spdyDataFrame);
            }
            valid = true;
        }
        if (!valid) {
            throw new UnsupportedMessageTypeException(msg, (Class<?>[])new Class[0]);
        }
    }
    
    private SpdySynStreamFrame createSynStreamFrame(final HttpRequest httpRequest) throws Exception {
        final HttpHeaders httpHeaders = httpRequest.headers();
        final int streamId = httpHeaders.getInt(SpdyHttpHeaders.Names.STREAM_ID);
        final int associatedToStreamId = httpHeaders.getInt(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID, 0);
        final byte priority = (byte)httpHeaders.getInt(SpdyHttpHeaders.Names.PRIORITY, 0);
        CharSequence scheme = httpHeaders.get(SpdyHttpHeaders.Names.SCHEME);
        httpHeaders.remove(SpdyHttpHeaders.Names.STREAM_ID);
        httpHeaders.remove(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID);
        httpHeaders.remove(SpdyHttpHeaders.Names.PRIORITY);
        httpHeaders.remove(SpdyHttpHeaders.Names.SCHEME);
        httpHeaders.remove(HttpHeaderNames.CONNECTION);
        httpHeaders.remove("Keep-Alive");
        httpHeaders.remove("Proxy-Connection");
        httpHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
        final SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamId, associatedToStreamId, priority, this.validateHeaders);
        final SpdyHeaders frameHeaders = spdySynStreamFrame.headers();
        ((Headers<AsciiString, String, Headers>)frameHeaders).set(SpdyHeaders.HttpNames.METHOD, httpRequest.method().name());
        ((Headers<AsciiString, String, Headers>)frameHeaders).set(SpdyHeaders.HttpNames.PATH, httpRequest.uri());
        ((Headers<AsciiString, String, Headers>)frameHeaders).set(SpdyHeaders.HttpNames.VERSION, httpRequest.protocolVersion().text());
        final CharSequence host = httpHeaders.get(HttpHeaderNames.HOST);
        httpHeaders.remove(HttpHeaderNames.HOST);
        ((Headers<AsciiString, CharSequence, Headers>)frameHeaders).set(SpdyHeaders.HttpNames.HOST, host);
        if (scheme == null) {
            scheme = "https";
        }
        ((Headers<AsciiString, CharSequence, Headers>)frameHeaders).set(SpdyHeaders.HttpNames.SCHEME, scheme);
        final Iterator<Map.Entry<CharSequence, CharSequence>> itr = httpHeaders.iteratorCharSequence();
        while (itr.hasNext()) {
            final Map.Entry<CharSequence, CharSequence> entry = itr.next();
            final CharSequence headerName = this.headersToLowerCase ? AsciiString.of(entry.getKey()).toLowerCase() : entry.getKey();
            ((Headers<CharSequence, CharSequence, Headers>)frameHeaders).add(headerName, entry.getValue());
        }
        this.currentStreamId = spdySynStreamFrame.streamId();
        if (associatedToStreamId == 0) {
            spdySynStreamFrame.setLast(isLast(httpRequest));
        }
        else {
            spdySynStreamFrame.setUnidirectional(true);
        }
        return spdySynStreamFrame;
    }
    
    private SpdyHeadersFrame createHeadersFrame(final HttpResponse httpResponse) throws Exception {
        final HttpHeaders httpHeaders = httpResponse.headers();
        final int streamId = httpHeaders.getInt(SpdyHttpHeaders.Names.STREAM_ID);
        httpHeaders.remove(SpdyHttpHeaders.Names.STREAM_ID);
        httpHeaders.remove(HttpHeaderNames.CONNECTION);
        httpHeaders.remove("Keep-Alive");
        httpHeaders.remove("Proxy-Connection");
        httpHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
        SpdyHeadersFrame spdyHeadersFrame;
        if (SpdyCodecUtil.isServerId(streamId)) {
            spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId, this.validateHeaders);
        }
        else {
            spdyHeadersFrame = new DefaultSpdySynReplyFrame(streamId, this.validateHeaders);
        }
        final SpdyHeaders frameHeaders = spdyHeadersFrame.headers();
        ((Headers<AsciiString, AsciiString, Headers>)frameHeaders).set(SpdyHeaders.HttpNames.STATUS, httpResponse.status().codeAsText());
        ((Headers<AsciiString, String, Headers>)frameHeaders).set(SpdyHeaders.HttpNames.VERSION, httpResponse.protocolVersion().text());
        final Iterator<Map.Entry<CharSequence, CharSequence>> itr = httpHeaders.iteratorCharSequence();
        while (itr.hasNext()) {
            final Map.Entry<CharSequence, CharSequence> entry = itr.next();
            final CharSequence headerName = this.headersToLowerCase ? AsciiString.of(entry.getKey()).toLowerCase() : entry.getKey();
            ((Headers<CharSequence, CharSequence, Headers>)spdyHeadersFrame.headers()).add(headerName, entry.getValue());
        }
        this.currentStreamId = streamId;
        spdyHeadersFrame.setLast(isLast(httpResponse));
        return spdyHeadersFrame;
    }
    
    private static boolean isLast(final HttpMessage httpMessage) {
        if (httpMessage instanceof FullHttpMessage) {
            final FullHttpMessage fullMessage = (FullHttpMessage)httpMessage;
            if (fullMessage.trailingHeaders().isEmpty() && !fullMessage.content().isReadable()) {
                return true;
            }
        }
        return false;
    }
}
