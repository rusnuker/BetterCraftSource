// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.Unpooled;
import io.netty.channel.CoalescingBufferQueue;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayDeque;

public class DefaultHttp2ConnectionEncoder implements Http2ConnectionEncoder
{
    private final Http2FrameWriter frameWriter;
    private final Http2Connection connection;
    private Http2LifecycleManager lifecycleManager;
    private final ArrayDeque<Http2Settings> outstandingLocalSettingsQueue;
    
    public DefaultHttp2ConnectionEncoder(final Http2Connection connection, final Http2FrameWriter frameWriter) {
        this.outstandingLocalSettingsQueue = new ArrayDeque<Http2Settings>(4);
        this.connection = ObjectUtil.checkNotNull(connection, "connection");
        this.frameWriter = ObjectUtil.checkNotNull(frameWriter, "frameWriter");
        if (connection.remote().flowController() == null) {
            connection.remote().flowController(new DefaultHttp2RemoteFlowController(connection));
        }
    }
    
    @Override
    public void lifecycleManager(final Http2LifecycleManager lifecycleManager) {
        this.lifecycleManager = ObjectUtil.checkNotNull(lifecycleManager, "lifecycleManager");
    }
    
    @Override
    public Http2FrameWriter frameWriter() {
        return this.frameWriter;
    }
    
    @Override
    public Http2Connection connection() {
        return this.connection;
    }
    
    @Override
    public final Http2RemoteFlowController flowController() {
        return this.connection().remote().flowController();
    }
    
    @Override
    public void remoteSettings(final Http2Settings settings) throws Http2Exception {
        final Boolean pushEnabled = settings.pushEnabled();
        final Http2FrameWriter.Configuration config = this.configuration();
        final Http2HeadersEncoder.Configuration outboundHeaderConfig = config.headersConfiguration();
        final Http2FrameSizePolicy outboundFrameSizePolicy = config.frameSizePolicy();
        if (pushEnabled != null) {
            if (!this.connection.isServer() && pushEnabled) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Client received a value of ENABLE_PUSH specified to other than 0", new Object[0]);
            }
            this.connection.remote().allowPushTo(pushEnabled);
        }
        final Long maxConcurrentStreams = settings.maxConcurrentStreams();
        if (maxConcurrentStreams != null) {
            this.connection.local().maxActiveStreams((int)Math.min(maxConcurrentStreams, 2147483647L));
        }
        final Long headerTableSize = settings.headerTableSize();
        if (headerTableSize != null) {
            outboundHeaderConfig.maxHeaderTableSize((int)Math.min(headerTableSize, 2147483647L));
        }
        final Long maxHeaderListSize = settings.maxHeaderListSize();
        if (maxHeaderListSize != null) {
            outboundHeaderConfig.maxHeaderListSize(maxHeaderListSize);
        }
        final Integer maxFrameSize = settings.maxFrameSize();
        if (maxFrameSize != null) {
            outboundFrameSizePolicy.maxFrameSize(maxFrameSize);
        }
        final Integer initialWindowSize = settings.initialWindowSize();
        if (initialWindowSize != null) {
            this.flowController().initialWindowSize(initialWindowSize);
        }
    }
    
    @Override
    public ChannelFuture writeData(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endOfStream, final ChannelPromise promise) {
        Http2Stream stream;
        try {
            stream = this.requireStream(streamId);
            switch (stream.state()) {
                case OPEN:
                case HALF_CLOSED_REMOTE: {
                    break;
                }
                default: {
                    throw new IllegalStateException(String.format("Stream %d in unexpected state: %s", stream.id(), stream.state()));
                }
            }
        }
        catch (final Throwable e) {
            data.release();
            return promise.setFailure(e);
        }
        this.flowController().addFlowControlled(stream, new FlowControlledData(stream, data, padding, endOfStream, promise));
        return promise;
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.writeHeaders(ctx, streamId, headers, 0, (short)16, false, padding, endStream, promise);
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endOfStream, ChannelPromise promise) {
        try {
            Http2Stream stream = this.connection.stream(streamId);
            if (stream == null) {
                stream = this.connection.local().createStream(streamId, endOfStream);
            }
            else {
                switch (stream.state()) {
                    case RESERVED_LOCAL: {
                        stream.open(endOfStream);
                        break;
                    }
                    case OPEN:
                    case HALF_CLOSED_REMOTE: {
                        break;
                    }
                    default: {
                        throw new IllegalStateException(String.format("Stream %d in unexpected state: %s", stream.id(), stream.state()));
                    }
                }
            }
            final Http2RemoteFlowController flowController = this.flowController();
            if (!endOfStream || !flowController.hasFlowControlled(stream)) {
                if (endOfStream) {
                    final Http2Stream finalStream = stream;
                    final ChannelFutureListener closeStreamLocalListener = new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            DefaultHttp2ConnectionEncoder.this.lifecycleManager.closeStreamLocal(finalStream, future);
                        }
                    };
                    promise = promise.unvoid().addListener((GenericFutureListener<? extends Future<? super Void>>)closeStreamLocalListener);
                }
                final ChannelFuture future = this.frameWriter.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
                final Throwable failureCause = future.cause();
                if (failureCause == null) {
                    stream.headersSent();
                }
                else {
                    this.lifecycleManager.onError(ctx, failureCause);
                }
                return future;
            }
            flowController.addFlowControlled(stream, new FlowControlledHeaders(stream, headers, streamDependency, weight, exclusive, padding, true, promise));
            return promise;
        }
        catch (final Throwable t) {
            this.lifecycleManager.onError(ctx, t);
            promise.tryFailure(t);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture writePriority(final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive, final ChannelPromise promise) {
        return this.frameWriter.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
    }
    
    @Override
    public ChannelFuture writeRstStream(final ChannelHandlerContext ctx, final int streamId, final long errorCode, final ChannelPromise promise) {
        return this.lifecycleManager.resetStream(ctx, streamId, errorCode, promise);
    }
    
    @Override
    public ChannelFuture writeSettings(final ChannelHandlerContext ctx, final Http2Settings settings, final ChannelPromise promise) {
        this.outstandingLocalSettingsQueue.add(settings);
        try {
            final Boolean pushEnabled = settings.pushEnabled();
            if (pushEnabled != null && this.connection.isServer()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
            }
        }
        catch (final Throwable e) {
            return promise.setFailure(e);
        }
        return this.frameWriter.writeSettings(ctx, settings, promise);
    }
    
    @Override
    public ChannelFuture writeSettingsAck(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        return this.frameWriter.writeSettingsAck(ctx, promise);
    }
    
    @Override
    public ChannelFuture writePing(final ChannelHandlerContext ctx, final boolean ack, final ByteBuf data, final ChannelPromise promise) {
        return this.frameWriter.writePing(ctx, ack, data, promise);
    }
    
    @Override
    public ChannelFuture writePushPromise(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding, final ChannelPromise promise) {
        try {
            if (this.connection.goAwayReceived()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Sending PUSH_PROMISE after GO_AWAY received.", new Object[0]);
            }
            final Http2Stream stream = this.requireStream(streamId);
            this.connection.local().reservePushStream(promisedStreamId, stream);
            final ChannelFuture future = this.frameWriter.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
            final Throwable failureCause = future.cause();
            if (failureCause == null) {
                stream.pushPromiseSent();
            }
            else {
                this.lifecycleManager.onError(ctx, failureCause);
            }
            return future;
        }
        catch (final Throwable t) {
            this.lifecycleManager.onError(ctx, t);
            promise.tryFailure(t);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture writeGoAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData, final ChannelPromise promise) {
        return this.lifecycleManager.goAway(ctx, lastStreamId, errorCode, debugData, promise);
    }
    
    @Override
    public ChannelFuture writeWindowUpdate(final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement, final ChannelPromise promise) {
        return promise.setFailure((Throwable)new UnsupportedOperationException("Use the Http2[Inbound|Outbound]FlowController objects to control window sizes"));
    }
    
    @Override
    public ChannelFuture writeFrame(final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf payload, final ChannelPromise promise) {
        return this.frameWriter.writeFrame(ctx, frameType, streamId, flags, payload, promise);
    }
    
    @Override
    public void close() {
        this.frameWriter.close();
    }
    
    @Override
    public Http2Settings pollSentSettings() {
        return this.outstandingLocalSettingsQueue.poll();
    }
    
    @Override
    public Http2FrameWriter.Configuration configuration() {
        return this.frameWriter.configuration();
    }
    
    private Http2Stream requireStream(final int streamId) {
        final Http2Stream stream = this.connection.stream(streamId);
        if (stream == null) {
            String message;
            if (this.connection.streamMayHaveExisted(streamId)) {
                message = "Stream no longer exists: " + streamId;
            }
            else {
                message = "Stream does not exist: " + streamId;
            }
            throw new IllegalArgumentException(message);
        }
        return stream;
    }
    
    private final class FlowControlledData extends FlowControlledBase
    {
        private final CoalescingBufferQueue queue;
        private int dataSize;
        
        FlowControlledData(final Http2Stream stream, final ByteBuf buf, final int padding, final boolean endOfStream, final ChannelPromise promise) {
            super(stream, padding, endOfStream, promise);
            (this.queue = new CoalescingBufferQueue(promise.channel())).add(buf, promise);
            this.dataSize = this.queue.readableBytes();
        }
        
        @Override
        public int size() {
            return this.dataSize + this.padding;
        }
        
        @Override
        public void error(final ChannelHandlerContext ctx, final Throwable cause) {
            this.queue.releaseAndFailAll(cause);
            DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, cause);
        }
        
        @Override
        public void write(final ChannelHandlerContext ctx, final int allowedBytes) {
            final int queuedData = this.queue.readableBytes();
            if (!this.endOfStream) {
                if (queuedData == 0) {
                    final ChannelPromise writePromise = ctx.newPromise().addListener((GenericFutureListener<? extends Future<? super Void>>)this);
                    this.queue.remove(0, writePromise).release();
                    ctx.write(Unpooled.EMPTY_BUFFER, writePromise);
                    return;
                }
                if (allowedBytes == 0) {
                    return;
                }
            }
            final int writeableData = Math.min(queuedData, allowedBytes);
            final ChannelPromise writePromise2 = ctx.newPromise().addListener((GenericFutureListener<? extends Future<? super Void>>)this);
            final ByteBuf toWrite = this.queue.remove(writeableData, writePromise2);
            this.dataSize = this.queue.readableBytes();
            final int writeablePadding = Math.min(allowedBytes - writeableData, this.padding);
            this.padding -= writeablePadding;
            DefaultHttp2ConnectionEncoder.this.frameWriter().writeData(ctx, this.stream.id(), toWrite, writeablePadding, this.endOfStream && this.size() == 0, writePromise2);
        }
        
        @Override
        public boolean merge(final ChannelHandlerContext ctx, final Http2RemoteFlowController.FlowControlled next) {
            final FlowControlledData nextData;
            if (FlowControlledData.class != next.getClass() || Integer.MAX_VALUE - (nextData = (FlowControlledData)next).size() < this.size()) {
                return false;
            }
            nextData.queue.copyTo(this.queue);
            this.dataSize = this.queue.readableBytes();
            this.padding = Math.max(this.padding, nextData.padding);
            this.endOfStream = nextData.endOfStream;
            return true;
        }
    }
    
    private final class FlowControlledHeaders extends FlowControlledBase
    {
        private final Http2Headers headers;
        private final int streamDependency;
        private final short weight;
        private final boolean exclusive;
        
        FlowControlledHeaders(final Http2Stream stream, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endOfStream, final ChannelPromise promise) {
            super(stream, padding, endOfStream, promise);
            this.headers = headers;
            this.streamDependency = streamDependency;
            this.weight = weight;
            this.exclusive = exclusive;
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public void error(final ChannelHandlerContext ctx, final Throwable cause) {
            if (ctx != null) {
                DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, cause);
            }
            this.promise.tryFailure(cause);
        }
        
        @Override
        public void write(final ChannelHandlerContext ctx, final int allowedBytes) {
            if (this.promise.isVoid()) {
                this.promise = ctx.newPromise();
            }
            this.promise.addListener((GenericFutureListener<? extends Future<? super Void>>)this);
            final ChannelFuture f = DefaultHttp2ConnectionEncoder.this.frameWriter.writeHeaders(ctx, this.stream.id(), this.headers, this.streamDependency, this.weight, this.exclusive, this.padding, this.endOfStream, this.promise);
            final Throwable failureCause = f.cause();
            if (failureCause == null) {
                this.stream.headersSent();
            }
            else {
                DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, failureCause);
            }
        }
        
        @Override
        public boolean merge(final ChannelHandlerContext ctx, final Http2RemoteFlowController.FlowControlled next) {
            return false;
        }
    }
    
    public abstract class FlowControlledBase implements Http2RemoteFlowController.FlowControlled, ChannelFutureListener
    {
        protected final Http2Stream stream;
        protected ChannelPromise promise;
        protected boolean endOfStream;
        protected int padding;
        
        FlowControlledBase(final Http2Stream stream, final int padding, final boolean endOfStream, final ChannelPromise promise) {
            if (padding < 0) {
                throw new IllegalArgumentException("padding must be >= 0");
            }
            this.padding = padding;
            this.endOfStream = endOfStream;
            this.stream = stream;
            this.promise = promise;
        }
        
        @Override
        public void writeComplete() {
            if (this.endOfStream) {
                DefaultHttp2ConnectionEncoder.this.lifecycleManager.closeStreamLocal(this.stream, this.promise);
            }
        }
        
        @Override
        public void operationComplete(final ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                this.error(DefaultHttp2ConnectionEncoder.this.flowController().channelHandlerContext(), future.cause());
            }
        }
    }
}
