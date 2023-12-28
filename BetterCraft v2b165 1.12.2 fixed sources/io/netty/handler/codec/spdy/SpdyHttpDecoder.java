// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.util.AsciiString;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.List;
import java.util.Iterator;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import io.netty.handler.codec.http.FullHttpMessage;
import java.util.Map;
import io.netty.handler.codec.MessageToMessageDecoder;

public class SpdyHttpDecoder extends MessageToMessageDecoder<SpdyFrame>
{
    private final boolean validateHeaders;
    private final int spdyVersion;
    private final int maxContentLength;
    private final Map<Integer, FullHttpMessage> messageMap;
    
    public SpdyHttpDecoder(final SpdyVersion version, final int maxContentLength) {
        this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>(), true);
    }
    
    public SpdyHttpDecoder(final SpdyVersion version, final int maxContentLength, final boolean validateHeaders) {
        this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>(), validateHeaders);
    }
    
    protected SpdyHttpDecoder(final SpdyVersion version, final int maxContentLength, final Map<Integer, FullHttpMessage> messageMap) {
        this(version, maxContentLength, messageMap, true);
    }
    
    protected SpdyHttpDecoder(final SpdyVersion version, final int maxContentLength, final Map<Integer, FullHttpMessage> messageMap, final boolean validateHeaders) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        if (maxContentLength <= 0) {
            throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
        }
        this.spdyVersion = version.getVersion();
        this.maxContentLength = maxContentLength;
        this.messageMap = messageMap;
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        for (final Map.Entry<Integer, FullHttpMessage> entry : this.messageMap.entrySet()) {
            ReferenceCountUtil.safeRelease(entry.getValue());
        }
        this.messageMap.clear();
        super.channelInactive(ctx);
    }
    
    protected FullHttpMessage putMessage(final int streamId, final FullHttpMessage message) {
        return this.messageMap.put(streamId, message);
    }
    
    protected FullHttpMessage getMessage(final int streamId) {
        return this.messageMap.get(streamId);
    }
    
    protected FullHttpMessage removeMessage(final int streamId) {
        return this.messageMap.remove(streamId);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final SpdyFrame msg, final List<Object> out) throws Exception {
        if (msg instanceof SpdySynStreamFrame) {
            final SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
            final int streamId = spdySynStreamFrame.streamId();
            if (SpdyCodecUtil.isServerId(streamId)) {
                final int associatedToStreamId = spdySynStreamFrame.associatedStreamId();
                if (associatedToStreamId == 0) {
                    final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INVALID_STREAM);
                    ctx.writeAndFlush(spdyRstStreamFrame);
                    return;
                }
                if (spdySynStreamFrame.isLast()) {
                    final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame);
                    return;
                }
                if (spdySynStreamFrame.isTruncated()) {
                    final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame);
                    return;
                }
                try {
                    final FullHttpRequest httpRequestWithEntity = createHttpRequest(spdySynStreamFrame, ctx.alloc());
                    httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
                    httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID, associatedToStreamId);
                    httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.PRIORITY, spdySynStreamFrame.priority());
                    out.add(httpRequestWithEntity);
                }
                catch (final Throwable ignored) {
                    final SpdyRstStreamFrame spdyRstStreamFrame2 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame2);
                }
            }
            else {
                if (spdySynStreamFrame.isTruncated()) {
                    final SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
                    spdySynReplyFrame.setLast(true);
                    final SpdyHeaders frameHeaders = spdySynReplyFrame.headers();
                    ((Headers<AsciiString, Object, Headers>)frameHeaders).setInt(SpdyHeaders.HttpNames.STATUS, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.code());
                    ((Headers<AsciiString, Object, Headers>)frameHeaders).setObject(SpdyHeaders.HttpNames.VERSION, HttpVersion.HTTP_1_0);
                    ctx.writeAndFlush(spdySynReplyFrame);
                    return;
                }
                try {
                    final FullHttpRequest httpRequestWithEntity2 = createHttpRequest(spdySynStreamFrame, ctx.alloc());
                    httpRequestWithEntity2.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
                    if (spdySynStreamFrame.isLast()) {
                        out.add(httpRequestWithEntity2);
                    }
                    else {
                        this.putMessage(streamId, httpRequestWithEntity2);
                    }
                }
                catch (final Throwable t) {
                    final SpdySynReplyFrame spdySynReplyFrame2 = new DefaultSpdySynReplyFrame(streamId);
                    spdySynReplyFrame2.setLast(true);
                    final SpdyHeaders frameHeaders2 = spdySynReplyFrame2.headers();
                    ((Headers<AsciiString, Object, Headers>)frameHeaders2).setInt(SpdyHeaders.HttpNames.STATUS, HttpResponseStatus.BAD_REQUEST.code());
                    ((Headers<AsciiString, Object, Headers>)frameHeaders2).setObject(SpdyHeaders.HttpNames.VERSION, HttpVersion.HTTP_1_0);
                    ctx.writeAndFlush(spdySynReplyFrame2);
                }
            }
        }
        else if (msg instanceof SpdySynReplyFrame) {
            final SpdySynReplyFrame spdySynReplyFrame3 = (SpdySynReplyFrame)msg;
            final int streamId = spdySynReplyFrame3.streamId();
            if (spdySynReplyFrame3.isTruncated()) {
                final SpdyRstStreamFrame spdyRstStreamFrame3 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
                ctx.writeAndFlush(spdyRstStreamFrame3);
                return;
            }
            try {
                final FullHttpResponse httpResponseWithEntity = createHttpResponse(spdySynReplyFrame3, ctx.alloc(), this.validateHeaders);
                httpResponseWithEntity.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
                if (spdySynReplyFrame3.isLast()) {
                    HttpUtil.setContentLength(httpResponseWithEntity, 0L);
                    out.add(httpResponseWithEntity);
                }
                else {
                    this.putMessage(streamId, httpResponseWithEntity);
                }
            }
            catch (final Throwable t) {
                final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                ctx.writeAndFlush(spdyRstStreamFrame);
            }
        }
        else if (msg instanceof SpdyHeadersFrame) {
            final SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
            final int streamId = spdyHeadersFrame.streamId();
            FullHttpMessage fullHttpMessage = this.getMessage(streamId);
            if (fullHttpMessage == null) {
                if (SpdyCodecUtil.isServerId(streamId)) {
                    if (spdyHeadersFrame.isTruncated()) {
                        final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
                        ctx.writeAndFlush(spdyRstStreamFrame);
                        return;
                    }
                    try {
                        fullHttpMessage = createHttpResponse(spdyHeadersFrame, ctx.alloc(), this.validateHeaders);
                        fullHttpMessage.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
                        if (spdyHeadersFrame.isLast()) {
                            HttpUtil.setContentLength(fullHttpMessage, 0L);
                            out.add(fullHttpMessage);
                        }
                        else {
                            this.putMessage(streamId, fullHttpMessage);
                        }
                    }
                    catch (final Throwable t2) {
                        final SpdyRstStreamFrame spdyRstStreamFrame2 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                        ctx.writeAndFlush(spdyRstStreamFrame2);
                    }
                }
                return;
            }
            if (!spdyHeadersFrame.isTruncated()) {
                for (final Map.Entry<CharSequence, CharSequence> e : spdyHeadersFrame.headers()) {
                    fullHttpMessage.headers().add(e.getKey(), e.getValue());
                }
            }
            if (spdyHeadersFrame.isLast()) {
                HttpUtil.setContentLength(fullHttpMessage, fullHttpMessage.content().readableBytes());
                this.removeMessage(streamId);
                out.add(fullHttpMessage);
            }
        }
        else if (msg instanceof SpdyDataFrame) {
            final SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
            final int streamId = spdyDataFrame.streamId();
            final FullHttpMessage fullHttpMessage = this.getMessage(streamId);
            if (fullHttpMessage == null) {
                return;
            }
            final ByteBuf content = fullHttpMessage.content();
            if (content.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes()) {
                this.removeMessage(streamId);
                throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
            }
            final ByteBuf spdyDataFrameData = spdyDataFrame.content();
            final int spdyDataFrameDataLen = spdyDataFrameData.readableBytes();
            content.writeBytes(spdyDataFrameData, spdyDataFrameData.readerIndex(), spdyDataFrameDataLen);
            if (spdyDataFrame.isLast()) {
                HttpUtil.setContentLength(fullHttpMessage, content.readableBytes());
                this.removeMessage(streamId);
                out.add(fullHttpMessage);
            }
        }
        else if (msg instanceof SpdyRstStreamFrame) {
            final SpdyRstStreamFrame spdyRstStreamFrame4 = (SpdyRstStreamFrame)msg;
            final int streamId = spdyRstStreamFrame4.streamId();
            this.removeMessage(streamId);
        }
    }
    
    private static FullHttpRequest createHttpRequest(final SpdyHeadersFrame requestFrame, final ByteBufAllocator alloc) throws Exception {
        final SpdyHeaders headers = requestFrame.headers();
        final HttpMethod method = HttpMethod.valueOf(headers.getAsString(SpdyHeaders.HttpNames.METHOD));
        final String url = headers.getAsString(SpdyHeaders.HttpNames.PATH);
        final HttpVersion httpVersion = HttpVersion.valueOf(headers.getAsString(SpdyHeaders.HttpNames.VERSION));
        ((Headers<AsciiString, V, T>)headers).remove(SpdyHeaders.HttpNames.METHOD);
        ((Headers<AsciiString, V, T>)headers).remove(SpdyHeaders.HttpNames.PATH);
        ((Headers<AsciiString, V, T>)headers).remove(SpdyHeaders.HttpNames.VERSION);
        boolean release = true;
        final ByteBuf buffer = alloc.buffer();
        try {
            final FullHttpRequest req = new DefaultFullHttpRequest(httpVersion, method, url, buffer);
            ((Headers<AsciiString, V, T>)headers).remove(SpdyHeaders.HttpNames.SCHEME);
            final CharSequence host = ((Headers<AsciiString, CharSequence, T>)headers).get(SpdyHeaders.HttpNames.HOST);
            ((Headers<AsciiString, V, T>)headers).remove(SpdyHeaders.HttpNames.HOST);
            req.headers().set(HttpHeaderNames.HOST, host);
            for (final Map.Entry<CharSequence, CharSequence> e : requestFrame.headers()) {
                req.headers().add(e.getKey(), e.getValue());
            }
            HttpUtil.setKeepAlive(req, true);
            req.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
            release = false;
            return req;
        }
        finally {
            if (release) {
                buffer.release();
            }
        }
    }
    
    private static FullHttpResponse createHttpResponse(final SpdyHeadersFrame responseFrame, final ByteBufAllocator alloc, final boolean validateHeaders) throws Exception {
        final SpdyHeaders headers = responseFrame.headers();
        final HttpResponseStatus status = HttpResponseStatus.parseLine(((Headers<AsciiString, CharSequence, T>)headers).get(SpdyHeaders.HttpNames.STATUS));
        final HttpVersion version = HttpVersion.valueOf(headers.getAsString(SpdyHeaders.HttpNames.VERSION));
        ((Headers<AsciiString, V, T>)headers).remove(SpdyHeaders.HttpNames.STATUS);
        ((Headers<AsciiString, V, T>)headers).remove(SpdyHeaders.HttpNames.VERSION);
        boolean release = true;
        final ByteBuf buffer = alloc.buffer();
        try {
            final FullHttpResponse res = new DefaultFullHttpResponse(version, status, buffer, validateHeaders);
            for (final Map.Entry<CharSequence, CharSequence> e : responseFrame.headers()) {
                res.headers().add(e.getKey(), e.getValue());
            }
            HttpUtil.setKeepAlive(res, true);
            res.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
            res.headers().remove(HttpHeaderNames.TRAILER);
            release = false;
            return res;
        }
        finally {
            if (release) {
                buffer.release();
            }
        }
    }
}
