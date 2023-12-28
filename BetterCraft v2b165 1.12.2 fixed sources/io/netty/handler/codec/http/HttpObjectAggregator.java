// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.handler.codec.DecoderResult;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.handler.codec.MessageAggregator;

public class HttpObjectAggregator extends MessageAggregator<HttpObject, HttpMessage, HttpContent, FullHttpMessage>
{
    private static final InternalLogger logger;
    private static final FullHttpResponse CONTINUE;
    private static final FullHttpResponse EXPECTATION_FAILED;
    private static final FullHttpResponse TOO_LARGE_CLOSE;
    private static final FullHttpResponse TOO_LARGE;
    private final boolean closeOnExpectationFailed;
    
    public HttpObjectAggregator(final int maxContentLength) {
        this(maxContentLength, false);
    }
    
    public HttpObjectAggregator(final int maxContentLength, final boolean closeOnExpectationFailed) {
        super(maxContentLength);
        this.closeOnExpectationFailed = closeOnExpectationFailed;
    }
    
    @Override
    protected boolean isStartMessage(final HttpObject msg) throws Exception {
        return msg instanceof HttpMessage;
    }
    
    @Override
    protected boolean isContentMessage(final HttpObject msg) throws Exception {
        return msg instanceof HttpContent;
    }
    
    @Override
    protected boolean isLastContentMessage(final HttpContent msg) throws Exception {
        return msg instanceof LastHttpContent;
    }
    
    @Override
    protected boolean isAggregated(final HttpObject msg) throws Exception {
        return msg instanceof FullHttpMessage;
    }
    
    @Override
    protected boolean isContentLengthInvalid(final HttpMessage start, final int maxContentLength) {
        return HttpUtil.getContentLength(start, -1L) > maxContentLength;
    }
    
    @Override
    protected Object newContinueResponse(final HttpMessage start, final int maxContentLength, final ChannelPipeline pipeline) {
        if (HttpUtil.isUnsupportedExpectation(start)) {
            pipeline.fireUserEventTriggered((Object)HttpExpectationFailedEvent.INSTANCE);
            return HttpObjectAggregator.EXPECTATION_FAILED.retainedDuplicate();
        }
        if (!HttpUtil.is100ContinueExpected(start)) {
            return null;
        }
        if (HttpUtil.getContentLength(start, -1L) <= maxContentLength) {
            return HttpObjectAggregator.CONTINUE.retainedDuplicate();
        }
        pipeline.fireUserEventTriggered((Object)HttpExpectationFailedEvent.INSTANCE);
        return HttpObjectAggregator.TOO_LARGE.retainedDuplicate();
    }
    
    @Override
    protected boolean closeAfterContinueResponse(final Object msg) {
        return this.closeOnExpectationFailed && this.ignoreContentAfterContinueResponse(msg);
    }
    
    @Override
    protected boolean ignoreContentAfterContinueResponse(final Object msg) {
        if (msg instanceof HttpResponse) {
            final HttpResponse httpResponse = (HttpResponse)msg;
            return httpResponse.status().codeClass().equals(HttpStatusClass.CLIENT_ERROR);
        }
        return false;
    }
    
    @Override
    protected FullHttpMessage beginAggregation(final HttpMessage start, final ByteBuf content) throws Exception {
        assert !(start instanceof FullHttpMessage);
        HttpUtil.setTransferEncodingChunked(start, false);
        AggregatedFullHttpMessage ret;
        if (start instanceof HttpRequest) {
            ret = new AggregatedFullHttpRequest((HttpRequest)start, content, null);
        }
        else {
            if (!(start instanceof HttpResponse)) {
                throw new Error();
            }
            ret = new AggregatedFullHttpResponse((HttpResponse)start, content, null);
        }
        return ret;
    }
    
    @Override
    protected void aggregate(final FullHttpMessage aggregated, final HttpContent content) throws Exception {
        if (content instanceof LastHttpContent) {
            ((AggregatedFullHttpMessage)aggregated).setTrailingHeaders(((LastHttpContent)content).trailingHeaders());
        }
    }
    
    @Override
    protected void finishAggregation(final FullHttpMessage aggregated) throws Exception {
        if (!HttpUtil.isContentLengthSet(aggregated)) {
            aggregated.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(aggregated.content().readableBytes()));
        }
    }
    
    @Override
    protected void handleOversizedMessage(final ChannelHandlerContext ctx, final HttpMessage oversized) throws Exception {
        if (oversized instanceof HttpRequest) {
            if (oversized instanceof FullHttpMessage || (!HttpUtil.is100ContinueExpected(oversized) && !HttpUtil.isKeepAlive(oversized))) {
                final ChannelFuture future = ctx.writeAndFlush(HttpObjectAggregator.TOO_LARGE_CLOSE.retainedDuplicate());
                future.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            HttpObjectAggregator.logger.debug("Failed to send a 413 Request Entity Too Large.", future.cause());
                        }
                        ctx.close();
                    }
                });
            }
            else {
                ctx.writeAndFlush(HttpObjectAggregator.TOO_LARGE.retainedDuplicate()).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            HttpObjectAggregator.logger.debug("Failed to send a 413 Request Entity Too Large.", future.cause());
                            ctx.close();
                        }
                    }
                });
            }
            final HttpObjectDecoder decoder = ctx.pipeline().get(HttpObjectDecoder.class);
            if (decoder != null) {
                decoder.reset();
            }
            return;
        }
        if (oversized instanceof HttpResponse) {
            ctx.close();
            throw new TooLongFrameException("Response entity too large: " + oversized);
        }
        throw new IllegalStateException();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(HttpObjectAggregator.class);
        CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
        EXPECTATION_FAILED = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.EXPECTATION_FAILED, Unpooled.EMPTY_BUFFER);
        TOO_LARGE_CLOSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER);
        TOO_LARGE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER);
        HttpObjectAggregator.EXPECTATION_FAILED.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        HttpObjectAggregator.TOO_LARGE.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        HttpObjectAggregator.TOO_LARGE_CLOSE.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        HttpObjectAggregator.TOO_LARGE_CLOSE.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
    }
    
    private abstract static class AggregatedFullHttpMessage implements FullHttpMessage
    {
        protected final HttpMessage message;
        private final ByteBuf content;
        private HttpHeaders trailingHeaders;
        
        AggregatedFullHttpMessage(final HttpMessage message, final ByteBuf content, final HttpHeaders trailingHeaders) {
            this.message = message;
            this.content = content;
            this.trailingHeaders = trailingHeaders;
        }
        
        @Override
        public HttpHeaders trailingHeaders() {
            final HttpHeaders trailingHeaders = this.trailingHeaders;
            if (trailingHeaders == null) {
                return EmptyHttpHeaders.INSTANCE;
            }
            return trailingHeaders;
        }
        
        void setTrailingHeaders(final HttpHeaders trailingHeaders) {
            this.trailingHeaders = trailingHeaders;
        }
        
        @Override
        public HttpVersion getProtocolVersion() {
            return this.message.protocolVersion();
        }
        
        @Override
        public HttpVersion protocolVersion() {
            return this.message.protocolVersion();
        }
        
        @Override
        public FullHttpMessage setProtocolVersion(final HttpVersion version) {
            this.message.setProtocolVersion(version);
            return this;
        }
        
        @Override
        public HttpHeaders headers() {
            return this.message.headers();
        }
        
        @Override
        public DecoderResult decoderResult() {
            return this.message.decoderResult();
        }
        
        @Override
        public DecoderResult getDecoderResult() {
            return this.message.decoderResult();
        }
        
        @Override
        public void setDecoderResult(final DecoderResult result) {
            this.message.setDecoderResult(result);
        }
        
        @Override
        public ByteBuf content() {
            return this.content;
        }
        
        @Override
        public int refCnt() {
            return this.content.refCnt();
        }
        
        @Override
        public FullHttpMessage retain() {
            this.content.retain();
            return this;
        }
        
        @Override
        public FullHttpMessage retain(final int increment) {
            this.content.retain(increment);
            return this;
        }
        
        @Override
        public FullHttpMessage touch(final Object hint) {
            this.content.touch(hint);
            return this;
        }
        
        @Override
        public FullHttpMessage touch() {
            this.content.touch();
            return this;
        }
        
        @Override
        public boolean release() {
            return this.content.release();
        }
        
        @Override
        public boolean release(final int decrement) {
            return this.content.release(decrement);
        }
        
        @Override
        public abstract FullHttpMessage copy();
        
        @Override
        public abstract FullHttpMessage duplicate();
        
        @Override
        public abstract FullHttpMessage retainedDuplicate();
    }
    
    private static final class AggregatedFullHttpRequest extends AggregatedFullHttpMessage implements FullHttpRequest
    {
        AggregatedFullHttpRequest(final HttpRequest request, final ByteBuf content, final HttpHeaders trailingHeaders) {
            super(request, content, trailingHeaders);
        }
        
        @Override
        public FullHttpRequest copy() {
            return this.replace(this.content().copy());
        }
        
        @Override
        public FullHttpRequest duplicate() {
            return this.replace(this.content().duplicate());
        }
        
        @Override
        public FullHttpRequest retainedDuplicate() {
            return this.replace(this.content().retainedDuplicate());
        }
        
        @Override
        public FullHttpRequest replace(final ByteBuf content) {
            final DefaultFullHttpRequest dup = new DefaultFullHttpRequest(this.protocolVersion(), this.method(), this.uri(), content);
            dup.headers().set(this.headers());
            dup.trailingHeaders().set(this.trailingHeaders());
            dup.setDecoderResult(this.decoderResult());
            return dup;
        }
        
        @Override
        public FullHttpRequest retain(final int increment) {
            super.retain(increment);
            return this;
        }
        
        @Override
        public FullHttpRequest retain() {
            super.retain();
            return this;
        }
        
        @Override
        public FullHttpRequest touch() {
            super.touch();
            return this;
        }
        
        @Override
        public FullHttpRequest touch(final Object hint) {
            super.touch(hint);
            return this;
        }
        
        @Override
        public FullHttpRequest setMethod(final HttpMethod method) {
            ((HttpRequest)this.message).setMethod(method);
            return this;
        }
        
        @Override
        public FullHttpRequest setUri(final String uri) {
            ((HttpRequest)this.message).setUri(uri);
            return this;
        }
        
        @Override
        public HttpMethod getMethod() {
            return ((HttpRequest)this.message).method();
        }
        
        @Override
        public String getUri() {
            return ((HttpRequest)this.message).uri();
        }
        
        @Override
        public HttpMethod method() {
            return this.getMethod();
        }
        
        @Override
        public String uri() {
            return this.getUri();
        }
        
        @Override
        public FullHttpRequest setProtocolVersion(final HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }
        
        @Override
        public String toString() {
            return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
        }
    }
    
    private static final class AggregatedFullHttpResponse extends AggregatedFullHttpMessage implements FullHttpResponse
    {
        AggregatedFullHttpResponse(final HttpResponse message, final ByteBuf content, final HttpHeaders trailingHeaders) {
            super(message, content, trailingHeaders);
        }
        
        @Override
        public FullHttpResponse copy() {
            return this.replace(this.content().copy());
        }
        
        @Override
        public FullHttpResponse duplicate() {
            return this.replace(this.content().duplicate());
        }
        
        @Override
        public FullHttpResponse retainedDuplicate() {
            return this.replace(this.content().retainedDuplicate());
        }
        
        @Override
        public FullHttpResponse replace(final ByteBuf content) {
            final DefaultFullHttpResponse dup = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), content);
            dup.headers().set(this.headers());
            dup.trailingHeaders().set(this.trailingHeaders());
            dup.setDecoderResult(this.decoderResult());
            return dup;
        }
        
        @Override
        public FullHttpResponse setStatus(final HttpResponseStatus status) {
            ((HttpResponse)this.message).setStatus(status);
            return this;
        }
        
        @Override
        public HttpResponseStatus getStatus() {
            return ((HttpResponse)this.message).status();
        }
        
        @Override
        public HttpResponseStatus status() {
            return this.getStatus();
        }
        
        @Override
        public FullHttpResponse setProtocolVersion(final HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }
        
        @Override
        public FullHttpResponse retain(final int increment) {
            super.retain(increment);
            return this;
        }
        
        @Override
        public FullHttpResponse retain() {
            super.retain();
            return this;
        }
        
        @Override
        public FullHttpResponse touch(final Object hint) {
            super.touch(hint);
            return this;
        }
        
        @Override
        public FullHttpResponse touch() {
            super.touch();
            return this;
        }
        
        @Override
        public String toString() {
            return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
        }
    }
}
