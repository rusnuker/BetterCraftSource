// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.EventLoop;
import io.netty.channel.ChannelFuture;
import java.util.Iterator;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.collection.IntObjectHashMap;
import java.util.ArrayList;
import io.netty.util.collection.IntObjectMap;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelDuplexHandler;

public final class Http2MultiplexCodec extends ChannelDuplexHandler
{
    private static final InternalLogger logger;
    private final Http2StreamChannelBootstrap bootstrap;
    private final List<Http2StreamChannel> channelsToFireChildReadComplete;
    private final boolean server;
    private ChannelHandlerContext ctx;
    private volatile Runnable flushTask;
    private final IntObjectMap<Http2StreamChannel> childChannels;
    
    public Http2MultiplexCodec(final boolean server, final Http2StreamChannelBootstrap bootstrap) {
        this.channelsToFireChildReadComplete = new ArrayList<Http2StreamChannel>();
        this.childChannels = new IntObjectHashMap<Http2StreamChannel>();
        if (bootstrap.parentChannel() != null) {
            throw new IllegalStateException("The parent channel must not be set on the bootstrap.");
        }
        this.server = server;
        this.bootstrap = new Http2StreamChannelBootstrap(bootstrap);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.bootstrap.parentChannel(ctx.channel());
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        if (!(cause instanceof Http2Exception.StreamException)) {
            ctx.fireExceptionCaught(cause);
            return;
        }
        final Http2Exception.StreamException streamEx = (Http2Exception.StreamException)cause;
        try {
            final Http2StreamChannel childChannel = this.childChannels.get(streamEx.streamId());
            if (childChannel != null) {
                childChannel.pipeline().fireExceptionCaught((Throwable)streamEx);
            }
            else {
                Http2MultiplexCodec.logger.warn(String.format("Exception caught for unknown HTTP/2 stream '%d'", streamEx.streamId()), streamEx);
            }
        }
        finally {
            this.onStreamClosed(streamEx.streamId());
        }
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) {
        ctx.flush();
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (!(msg instanceof Http2Frame)) {
            ctx.fireChannelRead(msg);
            return;
        }
        if (msg instanceof Http2StreamFrame) {
            final Http2StreamFrame frame = (Http2StreamFrame)msg;
            final int streamId = frame.streamId();
            final Http2StreamChannel childChannel = this.childChannels.get(streamId);
            if (childChannel == null) {
                ReferenceCountUtil.release(msg);
                throw new Http2Exception.StreamException(streamId, Http2Error.STREAM_CLOSED, String.format("Received %s frame for an unknown stream %d", frame.name(), streamId));
            }
            this.fireChildReadAndRegister(childChannel, frame);
        }
        else {
            if (!(msg instanceof Http2GoAwayFrame)) {
                ReferenceCountUtil.release(msg);
                throw new UnsupportedMessageTypeException(msg, (Class<?>[])new Class[0]);
            }
            final Http2GoAwayFrame goAwayFrame = (Http2GoAwayFrame)msg;
            for (final IntObjectMap.PrimitiveEntry<Http2StreamChannel> entry : this.childChannels.entries()) {
                final Http2StreamChannel childChannel2 = entry.value();
                final int streamId2 = entry.key();
                if (streamId2 > goAwayFrame.lastStreamId() && Http2CodecUtil.isOutboundStream(this.server, streamId2)) {
                    childChannel2.pipeline().fireUserEventTriggered((Object)goAwayFrame.retainedDuplicate());
                }
            }
            goAwayFrame.release();
        }
    }
    
    private void fireChildReadAndRegister(final Http2StreamChannel childChannel, final Http2StreamFrame frame) {
        childChannel.fireChildRead(frame);
        if (!childChannel.inStreamsToFireChildReadComplete) {
            this.channelsToFireChildReadComplete.add(childChannel);
            childChannel.inStreamsToFireChildReadComplete = true;
        }
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (evt instanceof Http2StreamActiveEvent) {
            final Http2StreamActiveEvent activeEvent = (Http2StreamActiveEvent)evt;
            this.onStreamActive(activeEvent.streamId(), activeEvent.headers());
        }
        else if (evt instanceof Http2StreamClosedEvent) {
            this.onStreamClosed(((Http2StreamClosedEvent)evt).streamId());
        }
        else {
            ctx.fireUserEventTriggered(evt);
        }
    }
    
    private void onStreamActive(final int streamId, final Http2HeadersFrame headersFrame) {
        Http2StreamChannel childChannel;
        if (Http2CodecUtil.isOutboundStream(this.server, streamId)) {
            if (!(headersFrame instanceof ChannelCarryingHeadersFrame)) {
                throw new IllegalArgumentException("needs to be wrapped");
            }
            childChannel = ((ChannelCarryingHeadersFrame)headersFrame).channel();
            childChannel.streamId(streamId);
        }
        else {
            final ChannelFuture future = this.bootstrap.connect(streamId);
            childChannel = (Http2StreamChannel)future.channel();
        }
        final Http2StreamChannel existing = this.childChannels.put(streamId, childChannel);
        assert existing == null;
    }
    
    private void onStreamClosed(final int streamId) {
        final Http2StreamChannel childChannel = this.childChannels.remove(streamId);
        if (childChannel != null) {
            final EventLoop eventLoop = childChannel.eventLoop();
            if (eventLoop.inEventLoop()) {
                this.onStreamClosed0(childChannel);
            }
            else {
                eventLoop.execute(new Runnable() {
                    @Override
                    public void run() {
                        Http2MultiplexCodec.this.onStreamClosed0(childChannel);
                    }
                });
            }
        }
    }
    
    private void onStreamClosed0(final Http2StreamChannel childChannel) {
        assert childChannel.eventLoop().inEventLoop();
        childChannel.onStreamClosedFired = true;
        childChannel.fireChildRead(AbstractHttp2StreamChannel.CLOSE_MESSAGE);
    }
    
    void flushFromStreamChannel() {
        final EventExecutor executor = this.ctx.executor();
        if (executor.inEventLoop()) {
            this.flush(this.ctx);
        }
        else {
            Runnable task = this.flushTask;
            if (task == null) {
                final Runnable flushTask = new Runnable() {
                    @Override
                    public void run() {
                        Http2MultiplexCodec.this.flush(Http2MultiplexCodec.this.ctx);
                    }
                };
                this.flushTask = flushTask;
                task = flushTask;
            }
            executor.execute(task);
        }
    }
    
    void writeFromStreamChannel(final Object msg, final boolean flush) {
        this.writeFromStreamChannel(msg, this.ctx.newPromise(), flush);
    }
    
    void writeFromStreamChannel(final Object msg, final ChannelPromise promise, final boolean flush) {
        final EventExecutor executor = this.ctx.executor();
        if (executor.inEventLoop()) {
            this.writeFromStreamChannel0(msg, flush, promise);
        }
        else {
            try {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Http2MultiplexCodec.this.writeFromStreamChannel0(msg, flush, promise);
                    }
                });
            }
            catch (final Throwable cause) {
                promise.setFailure(cause);
            }
        }
    }
    
    private void writeFromStreamChannel0(final Object msg, final boolean flush, final ChannelPromise promise) {
        try {
            this.write(this.ctx, msg, promise);
        }
        catch (final Throwable cause) {
            promise.tryFailure(cause);
        }
        if (flush) {
            this.flush(this.ctx);
        }
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        for (int i = 0; i < this.channelsToFireChildReadComplete.size(); ++i) {
            final Http2StreamChannel childChannel = this.channelsToFireChildReadComplete.get(i);
            childChannel.inStreamsToFireChildReadComplete = false;
            childChannel.fireChildReadComplete();
        }
        this.channelsToFireChildReadComplete.clear();
    }
    
    ChannelFuture createStreamChannel(final Channel parentChannel, final EventLoopGroup group, final ChannelHandler handler, final Map<ChannelOption<?>, Object> options, final Map<AttributeKey<?>, Object> attrs, final int streamId) {
        final Http2StreamChannel channel = new Http2StreamChannel(parentChannel);
        if (Http2CodecUtil.isStreamIdValid(streamId)) {
            assert !Http2CodecUtil.isOutboundStream(this.server, streamId);
            assert this.ctx.channel().eventLoop().inEventLoop();
            channel.streamId(streamId);
        }
        channel.pipeline().addLast(handler);
        initOpts(channel, options);
        initAttrs(channel, attrs);
        final ChannelFuture future = group.register(channel);
        if (future.cause() != null) {
            if (channel.isRegistered()) {
                channel.close();
            }
            else {
                channel.unsafe().closeForcibly();
            }
        }
        return future;
    }
    
    private static void initOpts(final Channel channel, final Map<ChannelOption<?>, Object> opts) {
        if (opts != null) {
            for (final Map.Entry<ChannelOption<?>, Object> e : opts.entrySet()) {
                try {
                    if (channel.config().setOption(e.getKey(), e.getValue())) {
                        continue;
                    }
                    Http2MultiplexCodec.logger.warn("Unknown channel option: " + e);
                }
                catch (final Throwable t) {
                    Http2MultiplexCodec.logger.warn("Failed to set a channel option: " + channel, t);
                }
            }
        }
    }
    
    private static void initAttrs(final Channel channel, final Map<AttributeKey<?>, Object> attrs) {
        if (attrs != null) {
            for (final Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet()) {
                channel.attr(e.getKey()).set(e.getValue());
            }
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Http2MultiplexCodec.class);
    }
    
    final class Http2StreamChannel extends AbstractHttp2StreamChannel implements ChannelFutureListener
    {
        boolean onStreamClosedFired;
        boolean inStreamsToFireChildReadComplete;
        
        Http2StreamChannel(final Channel parentChannel) {
            super(parentChannel);
        }
        
        @Override
        protected void doClose() throws Exception {
            if (!this.onStreamClosedFired && Http2CodecUtil.isStreamIdValid(this.streamId())) {
                final Http2StreamFrame resetFrame = new DefaultHttp2ResetFrame(Http2Error.CANCEL).streamId(this.streamId());
                Http2MultiplexCodec.this.writeFromStreamChannel(resetFrame, true);
            }
            super.doClose();
        }
        
        @Override
        protected void doWrite(final Object msg) {
            if (msg instanceof Http2StreamFrame) {
                Http2StreamFrame frame = (Http2StreamFrame)msg;
                final ChannelPromise promise = Http2MultiplexCodec.this.ctx.newPromise();
                if (Http2CodecUtil.isStreamIdValid(frame.streamId())) {
                    ReferenceCountUtil.release(frame);
                    throw new IllegalArgumentException("Stream id must not be set on the frame. Was: " + frame.streamId());
                }
                if (!Http2CodecUtil.isStreamIdValid(this.streamId())) {
                    if (!(frame instanceof Http2HeadersFrame)) {
                        ReferenceCountUtil.release(frame);
                        throw new IllegalArgumentException("The first frame must be a headers frame. Was: " + frame.name());
                    }
                    frame = new ChannelCarryingHeadersFrame((Http2HeadersFrame)frame, this);
                    promise.addListener((GenericFutureListener<? extends Future<? super Void>>)this);
                }
                else {
                    frame.streamId(this.streamId());
                }
                Http2MultiplexCodec.this.writeFromStreamChannel(frame, promise, false);
            }
            else {
                if (!(msg instanceof Http2GoAwayFrame)) {
                    ReferenceCountUtil.release(msg);
                    throw new IllegalArgumentException("Message must be an Http2GoAwayFrame or Http2StreamFrame: " + msg);
                }
                final ChannelPromise promise2 = Http2MultiplexCodec.this.ctx.newPromise();
                promise2.addListener((GenericFutureListener<? extends Future<? super Void>>)this);
                Http2MultiplexCodec.this.writeFromStreamChannel(msg, promise2, false);
            }
        }
        
        @Override
        protected void doWriteComplete() {
            Http2MultiplexCodec.this.flushFromStreamChannel();
        }
        
        @Override
        protected EventExecutor preferredEventExecutor() {
            return Http2MultiplexCodec.this.ctx.executor();
        }
        
        @Override
        protected void bytesConsumed(final int bytes) {
            Http2MultiplexCodec.this.ctx.write(new DefaultHttp2WindowUpdateFrame(bytes).streamId(this.streamId()));
        }
        
        @Override
        public void operationComplete(final ChannelFuture future) throws Exception {
            final Throwable cause = future.cause();
            if (cause != null) {
                this.pipeline().fireExceptionCaught(cause);
                this.close();
            }
        }
    }
    
    private static final class ChannelCarryingHeadersFrame implements Http2HeadersFrame
    {
        private final Http2HeadersFrame frame;
        private final Http2StreamChannel childChannel;
        
        ChannelCarryingHeadersFrame(final Http2HeadersFrame frame, final Http2StreamChannel childChannel) {
            this.frame = frame;
            this.childChannel = childChannel;
        }
        
        @Override
        public Http2Headers headers() {
            return this.frame.headers();
        }
        
        @Override
        public boolean isEndStream() {
            return this.frame.isEndStream();
        }
        
        @Override
        public int padding() {
            return this.frame.padding();
        }
        
        @Override
        public Http2StreamFrame streamId(final int streamId) {
            return this.frame.streamId(streamId);
        }
        
        @Override
        public int streamId() {
            return this.frame.streamId();
        }
        
        @Override
        public String name() {
            return this.frame.name();
        }
        
        Http2StreamChannel channel() {
            return this.childChannel;
        }
    }
}
