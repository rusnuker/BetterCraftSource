// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Iterator;
import io.netty.buffer.ByteBufUtil;
import java.util.Map;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import java.util.TreeMap;

public class StreamBufferingEncoder extends DecoratingHttp2ConnectionEncoder
{
    private final TreeMap<Integer, PendingStream> pendingStreams;
    private int maxConcurrentStreams;
    private boolean closed;
    
    public StreamBufferingEncoder(final Http2ConnectionEncoder delegate) {
        this(delegate, 100);
    }
    
    public StreamBufferingEncoder(final Http2ConnectionEncoder delegate, final int initialMaxConcurrentStreams) {
        super(delegate);
        this.pendingStreams = new TreeMap<Integer, PendingStream>();
        this.maxConcurrentStreams = initialMaxConcurrentStreams;
        this.connection().addListener(new Http2ConnectionAdapter() {
            @Override
            public void onGoAwayReceived(final int lastStreamId, final long errorCode, final ByteBuf debugData) {
                StreamBufferingEncoder.this.cancelGoAwayStreams(lastStreamId, errorCode, debugData);
            }
            
            @Override
            public void onStreamClosed(final Http2Stream stream) {
                StreamBufferingEncoder.this.tryCreatePendingStreams();
            }
        });
    }
    
    public int numBufferedStreams() {
        return this.pendingStreams.size();
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.writeHeaders(ctx, streamId, headers, 0, (short)16, false, padding, endStream, promise);
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endOfStream, final ChannelPromise promise) {
        if (this.closed) {
            return promise.setFailure((Throwable)new Http2ChannelClosedException());
        }
        if (this.isExistingStream(streamId) || this.connection().goAwayReceived()) {
            return super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
        }
        if (this.canCreateStream()) {
            return super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
        }
        PendingStream pendingStream = this.pendingStreams.get(streamId);
        if (pendingStream == null) {
            pendingStream = new PendingStream(ctx, streamId);
            this.pendingStreams.put(streamId, pendingStream);
        }
        pendingStream.frames.add(new HeadersFrame(headers, streamDependency, weight, exclusive, padding, endOfStream, promise));
        return promise;
    }
    
    @Override
    public ChannelFuture writeRstStream(final ChannelHandlerContext ctx, final int streamId, final long errorCode, final ChannelPromise promise) {
        if (this.isExistingStream(streamId)) {
            return super.writeRstStream(ctx, streamId, errorCode, promise);
        }
        final PendingStream stream = this.pendingStreams.remove(streamId);
        if (stream != null) {
            stream.close(null);
            promise.setSuccess();
        }
        else {
            promise.setFailure((Throwable)Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream does not exist %d", streamId));
        }
        return promise;
    }
    
    @Override
    public ChannelFuture writeData(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endOfStream, final ChannelPromise promise) {
        if (this.isExistingStream(streamId)) {
            return super.writeData(ctx, streamId, data, padding, endOfStream, promise);
        }
        final PendingStream pendingStream = this.pendingStreams.get(streamId);
        if (pendingStream != null) {
            pendingStream.frames.add(new DataFrame(data, padding, endOfStream, promise));
        }
        else {
            ReferenceCountUtil.safeRelease(data);
            promise.setFailure((Throwable)Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream does not exist %d", streamId));
        }
        return promise;
    }
    
    @Override
    public void remoteSettings(final Http2Settings settings) throws Http2Exception {
        super.remoteSettings(settings);
        this.maxConcurrentStreams = this.connection().local().maxActiveStreams();
        this.tryCreatePendingStreams();
    }
    
    @Override
    public void close() {
        try {
            if (!this.closed) {
                this.closed = true;
                final Http2ChannelClosedException e = new Http2ChannelClosedException();
                while (!this.pendingStreams.isEmpty()) {
                    final PendingStream stream = this.pendingStreams.pollFirstEntry().getValue();
                    stream.close(e);
                }
            }
        }
        finally {
            super.close();
        }
    }
    
    private void tryCreatePendingStreams() {
        while (!this.pendingStreams.isEmpty() && this.canCreateStream()) {
            final Map.Entry<Integer, PendingStream> entry = this.pendingStreams.pollFirstEntry();
            final PendingStream pendingStream = entry.getValue();
            try {
                pendingStream.sendFrames();
            }
            catch (final Throwable t) {
                pendingStream.close(t);
            }
        }
    }
    
    private void cancelGoAwayStreams(final int lastStreamId, final long errorCode, final ByteBuf debugData) {
        final Iterator<PendingStream> iter = this.pendingStreams.values().iterator();
        final Exception e = new Http2GoAwayException(lastStreamId, errorCode, ByteBufUtil.getBytes(debugData));
        while (iter.hasNext()) {
            final PendingStream stream = iter.next();
            if (stream.streamId > lastStreamId) {
                iter.remove();
                stream.close(e);
            }
        }
    }
    
    private boolean canCreateStream() {
        return this.connection().local().numActiveStreams() < this.maxConcurrentStreams;
    }
    
    private boolean isExistingStream(final int streamId) {
        return streamId <= this.connection().local().lastStreamCreated();
    }
    
    public static final class Http2ChannelClosedException extends Http2Exception
    {
        private static final long serialVersionUID = 4768543442094476971L;
        
        public Http2ChannelClosedException() {
            super(Http2Error.REFUSED_STREAM, "Connection closed");
        }
    }
    
    public static final class Http2GoAwayException extends Http2Exception
    {
        private static final long serialVersionUID = 1326785622777291198L;
        private final int lastStreamId;
        private final long errorCode;
        private final byte[] debugData;
        
        public Http2GoAwayException(final int lastStreamId, final long errorCode, final byte[] debugData) {
            super(Http2Error.STREAM_CLOSED);
            this.lastStreamId = lastStreamId;
            this.errorCode = errorCode;
            this.debugData = debugData;
        }
        
        public int lastStreamId() {
            return this.lastStreamId;
        }
        
        public long errorCode() {
            return this.errorCode;
        }
        
        public byte[] debugData() {
            return this.debugData;
        }
    }
    
    private static final class PendingStream
    {
        final ChannelHandlerContext ctx;
        final int streamId;
        final Queue<Frame> frames;
        
        PendingStream(final ChannelHandlerContext ctx, final int streamId) {
            this.frames = new ArrayDeque<Frame>(2);
            this.ctx = ctx;
            this.streamId = streamId;
        }
        
        void sendFrames() {
            for (final Frame frame : this.frames) {
                frame.send(this.ctx, this.streamId);
            }
        }
        
        void close(final Throwable t) {
            for (final Frame frame : this.frames) {
                frame.release(t);
            }
        }
    }
    
    private abstract static class Frame
    {
        final ChannelPromise promise;
        
        Frame(final ChannelPromise promise) {
            this.promise = promise;
        }
        
        void release(final Throwable t) {
            if (t == null) {
                this.promise.setSuccess();
            }
            else {
                this.promise.setFailure(t);
            }
        }
        
        abstract void send(final ChannelHandlerContext p0, final int p1);
    }
    
    private final class HeadersFrame extends Frame
    {
        final Http2Headers headers;
        final int streamDependency;
        final short weight;
        final boolean exclusive;
        final int padding;
        final boolean endOfStream;
        
        HeadersFrame(final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endOfStream, final ChannelPromise promise) {
            super(promise);
            this.headers = headers;
            this.streamDependency = streamDependency;
            this.weight = weight;
            this.exclusive = exclusive;
            this.padding = padding;
            this.endOfStream = endOfStream;
        }
        
        @Override
        void send(final ChannelHandlerContext ctx, final int streamId) {
            StreamBufferingEncoder.this.writeHeaders(ctx, streamId, this.headers, this.streamDependency, this.weight, this.exclusive, this.padding, this.endOfStream, this.promise);
        }
    }
    
    private final class DataFrame extends Frame
    {
        final ByteBuf data;
        final int padding;
        final boolean endOfStream;
        
        DataFrame(final ByteBuf data, final int padding, final boolean endOfStream, final ChannelPromise promise) {
            super(promise);
            this.data = data;
            this.padding = padding;
            this.endOfStream = endOfStream;
        }
        
        @Override
        void release(final Throwable t) {
            super.release(t);
            ReferenceCountUtil.safeRelease(this.data);
        }
        
        @Override
        void send(final ChannelHandlerContext ctx, final int streamId) {
            StreamBufferingEncoder.this.writeData(ctx, streamId, this.data, this.padding, this.endOfStream, this.promise);
        }
    }
}
