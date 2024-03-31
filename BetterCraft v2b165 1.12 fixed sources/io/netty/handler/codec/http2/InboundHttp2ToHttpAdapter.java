// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpStatusClass;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.util.internal.ObjectUtil;

public class InboundHttp2ToHttpAdapter extends Http2EventAdapter
{
    private static final ImmediateSendDetector DEFAULT_SEND_DETECTOR;
    private final int maxContentLength;
    private final ImmediateSendDetector sendDetector;
    private final Http2Connection.PropertyKey messageKey;
    private final boolean propagateSettings;
    protected final Http2Connection connection;
    protected final boolean validateHttpHeaders;
    
    protected InboundHttp2ToHttpAdapter(final Http2Connection connection, final int maxContentLength, final boolean validateHttpHeaders, final boolean propagateSettings) {
        ObjectUtil.checkNotNull(connection, "connection");
        if (maxContentLength <= 0) {
            throw new IllegalArgumentException("maxContentLength: " + maxContentLength + " (expected: > 0)");
        }
        this.connection = connection;
        this.maxContentLength = maxContentLength;
        this.validateHttpHeaders = validateHttpHeaders;
        this.propagateSettings = propagateSettings;
        this.sendDetector = InboundHttp2ToHttpAdapter.DEFAULT_SEND_DETECTOR;
        this.messageKey = connection.newKey();
    }
    
    protected final void removeMessage(final Http2Stream stream, final boolean release) {
        final FullHttpMessage msg = stream.removeProperty(this.messageKey);
        if (release && msg != null) {
            msg.release();
        }
    }
    
    protected final FullHttpMessage getMessage(final Http2Stream stream) {
        return stream.getProperty(this.messageKey);
    }
    
    protected final void putMessage(final Http2Stream stream, final FullHttpMessage message) {
        final FullHttpMessage previous = stream.setProperty(this.messageKey, message);
        if (previous != message && previous != null) {
            previous.release();
        }
    }
    
    @Override
    public void onStreamRemoved(final Http2Stream stream) {
        this.removeMessage(stream, true);
    }
    
    protected void fireChannelRead(final ChannelHandlerContext ctx, final FullHttpMessage msg, final boolean release, final Http2Stream stream) {
        this.removeMessage(stream, release);
        HttpUtil.setContentLength(msg, msg.content().readableBytes());
        ctx.fireChannelRead((Object)msg);
    }
    
    protected FullHttpMessage newMessage(final Http2Stream stream, final Http2Headers headers, final boolean validateHttpHeaders, final ByteBufAllocator alloc) throws Http2Exception {
        return (FullHttpMessage)(this.connection.isServer() ? HttpConversionUtil.toFullHttpRequest(stream.id(), headers, alloc, validateHttpHeaders) : HttpConversionUtil.toHttpResponse(stream.id(), headers, alloc, validateHttpHeaders));
    }
    
    protected FullHttpMessage processHeadersBegin(final ChannelHandlerContext ctx, final Http2Stream stream, final Http2Headers headers, final boolean endOfStream, final boolean allowAppend, final boolean appendToTrailer) throws Http2Exception {
        FullHttpMessage msg = this.getMessage(stream);
        boolean release = true;
        if (msg == null) {
            msg = this.newMessage(stream, headers, this.validateHttpHeaders, ctx.alloc());
        }
        else if (allowAppend) {
            release = false;
            HttpConversionUtil.addHttp2ToHttpHeaders(stream.id(), headers, msg, appendToTrailer);
        }
        else {
            release = false;
            msg = null;
        }
        if (this.sendDetector.mustSendImmediately(msg)) {
            final FullHttpMessage copy = endOfStream ? null : this.sendDetector.copyIfNeeded(msg);
            this.fireChannelRead(ctx, msg, release, stream);
            return copy;
        }
        return msg;
    }
    
    private void processHeadersEnd(final ChannelHandlerContext ctx, final Http2Stream stream, final FullHttpMessage msg, final boolean endOfStream) {
        if (endOfStream) {
            this.fireChannelRead(ctx, msg, this.getMessage(stream) != msg, stream);
        }
        else {
            this.putMessage(stream, msg);
        }
    }
    
    @Override
    public int onDataRead(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endOfStream) throws Http2Exception {
        final Http2Stream stream = this.connection.stream(streamId);
        final FullHttpMessage msg = this.getMessage(stream);
        if (msg == null) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Data Frame received for unknown stream id %d", streamId);
        }
        final ByteBuf content = msg.content();
        final int dataReadableBytes = data.readableBytes();
        if (content.readableBytes() > this.maxContentLength - dataReadableBytes) {
            throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Content length exceeded max of %d for stream id %d", this.maxContentLength, streamId);
        }
        content.writeBytes(data, data.readerIndex(), dataReadableBytes);
        if (endOfStream) {
            this.fireChannelRead(ctx, msg, false, stream);
        }
        return dataReadableBytes + padding;
    }
    
    @Override
    public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endOfStream) throws Http2Exception {
        final Http2Stream stream = this.connection.stream(streamId);
        final FullHttpMessage msg = this.processHeadersBegin(ctx, stream, headers, endOfStream, true, true);
        if (msg != null) {
            this.processHeadersEnd(ctx, stream, msg, endOfStream);
        }
    }
    
    @Override
    public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endOfStream) throws Http2Exception {
        final Http2Stream stream = this.connection.stream(streamId);
        final FullHttpMessage msg = this.processHeadersBegin(ctx, stream, headers, endOfStream, true, true);
        if (msg != null) {
            if (streamDependency != 0) {
                msg.headers().setInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID.text(), streamDependency);
            }
            msg.headers().setShort(HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), weight);
            this.processHeadersEnd(ctx, stream, msg, endOfStream);
        }
    }
    
    @Override
    public void onRstStreamRead(final ChannelHandlerContext ctx, final int streamId, final long errorCode) throws Http2Exception {
        final Http2Stream stream = this.connection.stream(streamId);
        final FullHttpMessage msg = this.getMessage(stream);
        if (msg != null) {
            this.onRstStreamRead(stream, msg);
        }
        ctx.fireExceptionCaught((Throwable)Http2Exception.streamError(streamId, Http2Error.valueOf(errorCode), "HTTP/2 to HTTP layer caught stream reset", new Object[0]));
    }
    
    @Override
    public void onPushPromiseRead(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding) throws Http2Exception {
        final Http2Stream promisedStream = this.connection.stream(promisedStreamId);
        final FullHttpMessage msg = this.processHeadersBegin(ctx, promisedStream, headers, false, false, false);
        if (msg == null) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Push Promise Frame received for pre-existing stream id %d", promisedStreamId);
        }
        msg.headers().setInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_PROMISE_ID.text(), streamId);
        msg.headers().setShort(HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), (short)16);
        this.processHeadersEnd(ctx, promisedStream, msg, false);
    }
    
    @Override
    public void onSettingsRead(final ChannelHandlerContext ctx, final Http2Settings settings) throws Http2Exception {
        if (this.propagateSettings) {
            ctx.fireChannelRead((Object)settings);
        }
    }
    
    protected void onRstStreamRead(final Http2Stream stream, final FullHttpMessage msg) {
        this.removeMessage(stream, true);
    }
    
    static {
        DEFAULT_SEND_DETECTOR = new ImmediateSendDetector() {
            @Override
            public boolean mustSendImmediately(final FullHttpMessage msg) {
                if (msg instanceof FullHttpResponse) {
                    return ((FullHttpResponse)msg).status().codeClass() == HttpStatusClass.INFORMATIONAL;
                }
                return msg instanceof FullHttpRequest && msg.headers().contains(HttpHeaderNames.EXPECT);
            }
            
            @Override
            public FullHttpMessage copyIfNeeded(final FullHttpMessage msg) {
                if (msg instanceof FullHttpRequest) {
                    final FullHttpRequest copy = ((FullHttpRequest)msg).replace(Unpooled.buffer(0));
                    copy.headers().remove(HttpHeaderNames.EXPECT);
                    return copy;
                }
                return null;
            }
        };
    }
    
    private interface ImmediateSendDetector
    {
        boolean mustSendImmediately(final FullHttpMessage p0);
        
        FullHttpMessage copyIfNeeded(final FullHttpMessage p0);
    }
}
