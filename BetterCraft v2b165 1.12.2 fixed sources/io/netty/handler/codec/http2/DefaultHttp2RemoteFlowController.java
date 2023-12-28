// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.ArrayDeque;
import io.netty.util.BooleanSupplier;
import java.util.Deque;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;

public class DefaultHttp2RemoteFlowController implements Http2RemoteFlowController
{
    private static final InternalLogger logger;
    private static final int MIN_WRITABLE_CHUNK = 32768;
    private final Http2Connection connection;
    private final Http2Connection.PropertyKey stateKey;
    private final StreamByteDistributor streamByteDistributor;
    private final FlowState connectionState;
    private int initialWindowSize;
    private WritabilityMonitor monitor;
    private ChannelHandlerContext ctx;
    
    public DefaultHttp2RemoteFlowController(final Http2Connection connection) {
        this(connection, (Listener)null);
    }
    
    public DefaultHttp2RemoteFlowController(final Http2Connection connection, final StreamByteDistributor streamByteDistributor) {
        this(connection, streamByteDistributor, null);
    }
    
    public DefaultHttp2RemoteFlowController(final Http2Connection connection, final Listener listener) {
        this(connection, new WeightedFairQueueByteDistributor(connection), listener);
    }
    
    public DefaultHttp2RemoteFlowController(final Http2Connection connection, final StreamByteDistributor streamByteDistributor, final Listener listener) {
        this.initialWindowSize = 65535;
        this.connection = ObjectUtil.checkNotNull(connection, "connection");
        this.streamByteDistributor = ObjectUtil.checkNotNull(streamByteDistributor, "streamWriteDistributor");
        this.stateKey = connection.newKey();
        this.connectionState = new FlowState(connection.connectionStream());
        connection.connectionStream().setProperty(this.stateKey, this.connectionState);
        this.listener(listener);
        this.monitor.windowSize(this.connectionState, this.initialWindowSize);
        connection.addListener(new Http2ConnectionAdapter() {
            @Override
            public void onStreamAdded(final Http2Stream stream) {
                stream.setProperty(DefaultHttp2RemoteFlowController.this.stateKey, new FlowState(stream));
            }
            
            @Override
            public void onStreamActive(final Http2Stream stream) {
                DefaultHttp2RemoteFlowController.this.monitor.windowSize(DefaultHttp2RemoteFlowController.this.state(stream), DefaultHttp2RemoteFlowController.this.initialWindowSize);
            }
            
            @Override
            public void onStreamClosed(final Http2Stream stream) {
                DefaultHttp2RemoteFlowController.this.state(stream).cancel();
            }
            
            @Override
            public void onStreamHalfClosed(final Http2Stream stream) {
                if (Http2Stream.State.HALF_CLOSED_LOCAL.equals(stream.state())) {
                    DefaultHttp2RemoteFlowController.this.state(stream).cancel();
                }
            }
        });
    }
    
    @Override
    public void channelHandlerContext(final ChannelHandlerContext ctx) throws Http2Exception {
        this.ctx = ObjectUtil.checkNotNull(ctx, "ctx");
        this.channelWritabilityChanged();
        if (this.isChannelWritable()) {
            this.writePendingBytes();
        }
    }
    
    @Override
    public ChannelHandlerContext channelHandlerContext() {
        return this.ctx;
    }
    
    @Override
    public void initialWindowSize(final int newWindowSize) throws Http2Exception {
        assert !(!this.ctx.executor().inEventLoop());
        this.monitor.initialWindowSize(newWindowSize);
    }
    
    @Override
    public int initialWindowSize() {
        return this.initialWindowSize;
    }
    
    @Override
    public int windowSize(final Http2Stream stream) {
        return this.state(stream).windowSize();
    }
    
    @Override
    public boolean isWritable(final Http2Stream stream) {
        return this.monitor.isWritable(this.state(stream));
    }
    
    @Override
    public void channelWritabilityChanged() throws Http2Exception {
        this.monitor.channelWritabilityChange();
    }
    
    @Override
    public void updateDependencyTree(final int childStreamId, final int parentStreamId, final short weight, final boolean exclusive) {
        this.streamByteDistributor.updateDependencyTree(childStreamId, parentStreamId, weight, exclusive);
    }
    
    private boolean isChannelWritable() {
        return this.ctx != null && this.isChannelWritable0();
    }
    
    private boolean isChannelWritable0() {
        return this.ctx.channel().isWritable();
    }
    
    @Override
    public void listener(final Listener listener) {
        this.monitor = ((listener == null) ? new WritabilityMonitor() : new ListenerWritabilityMonitor(listener));
    }
    
    @Override
    public void incrementWindowSize(final Http2Stream stream, final int delta) throws Http2Exception {
        assert !(!this.ctx.executor().inEventLoop());
        this.monitor.incrementWindowSize(this.state(stream), delta);
    }
    
    @Override
    public void addFlowControlled(final Http2Stream stream, final FlowControlled frame) {
        assert !(!this.ctx.executor().inEventLoop());
        ObjectUtil.checkNotNull(frame, "frame");
        try {
            this.monitor.enqueueFrame(this.state(stream), frame);
        }
        catch (final Throwable t) {
            frame.error(this.ctx, t);
        }
    }
    
    @Override
    public boolean hasFlowControlled(final Http2Stream stream) {
        return this.state(stream).hasFrame();
    }
    
    private FlowState state(final Http2Stream stream) {
        return stream.getProperty(this.stateKey);
    }
    
    private int connectionWindowSize() {
        return this.connectionState.windowSize();
    }
    
    private int minUsableChannelBytes() {
        return Math.max(this.ctx.channel().config().getWriteBufferLowWaterMark(), 32768);
    }
    
    private int maxUsableChannelBytes() {
        final int channelWritableBytes = (int)Math.min(2147483647L, this.ctx.channel().bytesBeforeUnwritable());
        final int useableBytes = (channelWritableBytes > 0) ? Math.max(channelWritableBytes, this.minUsableChannelBytes()) : 0;
        return Math.min(this.connectionState.windowSize(), useableBytes);
    }
    
    private int writableBytes() {
        return Math.min(this.connectionWindowSize(), this.maxUsableChannelBytes());
    }
    
    @Override
    public void writePendingBytes() throws Http2Exception {
        this.monitor.writePendingBytes();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultHttp2RemoteFlowController.class);
    }
    
    private final class FlowState implements StreamByteDistributor.StreamState
    {
        private final Http2Stream stream;
        private final Deque<FlowControlled> pendingWriteQueue;
        private int window;
        private int pendingBytes;
        private boolean markedWritable;
        private boolean writing;
        private boolean cancelled;
        private BooleanSupplier isWritableSupplier;
        
        FlowState(final Http2Stream stream) {
            this.isWritableSupplier = new BooleanSupplier() {
                @Override
                public boolean get() throws Exception {
                    return FlowState.this.windowSize() > FlowState.this.pendingBytes();
                }
            };
            this.stream = stream;
            this.pendingWriteQueue = new ArrayDeque<FlowControlled>(2);
        }
        
        boolean isWritable() {
            try {
                return this.isWritableSupplier.get();
            }
            catch (final Throwable cause) {
                throw new Error("isWritableSupplier should never throw!", cause);
            }
        }
        
        @Override
        public Http2Stream stream() {
            return this.stream;
        }
        
        boolean markedWritability() {
            return this.markedWritable;
        }
        
        void markedWritability(final boolean isWritable) {
            this.markedWritable = isWritable;
        }
        
        @Override
        public int windowSize() {
            return this.window;
        }
        
        void windowSize(final int initialWindowSize) {
            this.window = initialWindowSize;
        }
        
        int writeAllocatedBytes(int allocated) {
            final int initialAllocated = allocated;
            Throwable cause = null;
            int writtenBytes = 0;
            try {
                assert !this.writing;
                this.writing = true;
                boolean writeOccurred = false;
                FlowControlled frame;
                while (!this.cancelled && (frame = this.peek()) != null) {
                    final int maxBytes = Math.min(allocated, this.writableWindow());
                    if (maxBytes <= 0 && frame.size() > 0) {
                        break;
                    }
                    writeOccurred = true;
                    final int initialFrameSize = frame.size();
                    try {
                        frame.write(DefaultHttp2RemoteFlowController.this.ctx, Math.max(0, maxBytes));
                        if (frame.size() != 0) {
                            continue;
                        }
                        this.pendingWriteQueue.remove();
                        frame.writeComplete();
                    }
                    finally {
                        allocated -= initialFrameSize - frame.size();
                    }
                }
                if (!writeOccurred) {
                    return -1;
                }
            }
            catch (final Throwable t) {
                this.cancelled = true;
                cause = t;
            }
            finally {
                this.writing = false;
                writtenBytes = initialAllocated - allocated;
                this.decrementPendingBytes(writtenBytes, false);
                this.decrementFlowControlWindow(writtenBytes);
                if (this.cancelled) {
                    this.cancel(cause);
                }
            }
            return writtenBytes;
        }
        
        int incrementStreamWindow(final int delta) throws Http2Exception {
            if (delta > 0 && Integer.MAX_VALUE - delta < this.window) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Window size overflow for stream: %d", this.stream.id());
            }
            this.window += delta;
            DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            return this.window;
        }
        
        private int writableWindow() {
            return Math.min(this.window, DefaultHttp2RemoteFlowController.this.connectionWindowSize());
        }
        
        @Override
        public int pendingBytes() {
            return this.pendingBytes;
        }
        
        void enqueueFrame(final FlowControlled frame) {
            final FlowControlled last = this.pendingWriteQueue.peekLast();
            if (last == null) {
                this.enqueueFrameWithoutMerge(frame);
                return;
            }
            final int lastSize = last.size();
            if (last.merge(DefaultHttp2RemoteFlowController.this.ctx, frame)) {
                this.incrementPendingBytes(last.size() - lastSize, true);
                return;
            }
            this.enqueueFrameWithoutMerge(frame);
        }
        
        private void enqueueFrameWithoutMerge(final FlowControlled frame) {
            this.pendingWriteQueue.offer(frame);
            this.incrementPendingBytes(frame.size(), true);
        }
        
        @Override
        public boolean hasFrame() {
            return !this.pendingWriteQueue.isEmpty();
        }
        
        private FlowControlled peek() {
            return this.pendingWriteQueue.peek();
        }
        
        void cancel() {
            this.cancel(null);
        }
        
        private void cancel(final Throwable cause) {
            this.cancelled = true;
            if (this.writing) {
                return;
            }
            while (true) {
                final FlowControlled frame = this.pendingWriteQueue.poll();
                if (frame == null) {
                    break;
                }
                this.writeError(frame, Http2Exception.streamError(this.stream.id(), Http2Error.INTERNAL_ERROR, cause, "Stream closed before write could take place", new Object[0]));
            }
            DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            this.isWritableSupplier = BooleanSupplier.FALSE_SUPPLIER;
            DefaultHttp2RemoteFlowController.this.monitor.stateCancelled(this);
        }
        
        private void incrementPendingBytes(final int numBytes, final boolean updateStreamableBytes) {
            this.pendingBytes += numBytes;
            DefaultHttp2RemoteFlowController.this.monitor.incrementPendingBytes(numBytes);
            if (updateStreamableBytes) {
                DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            }
        }
        
        private void decrementPendingBytes(final int bytes, final boolean updateStreamableBytes) {
            this.incrementPendingBytes(-bytes, updateStreamableBytes);
        }
        
        private void decrementFlowControlWindow(final int bytes) {
            try {
                final int negativeBytes = -bytes;
                DefaultHttp2RemoteFlowController.this.connectionState.incrementStreamWindow(negativeBytes);
                this.incrementStreamWindow(negativeBytes);
            }
            catch (final Http2Exception e) {
                throw new IllegalStateException("Invalid window state when writing frame: " + e.getMessage(), e);
            }
        }
        
        private void writeError(final FlowControlled frame, final Http2Exception cause) {
            assert DefaultHttp2RemoteFlowController.this.ctx != null;
            this.decrementPendingBytes(frame.size(), true);
            frame.error(DefaultHttp2RemoteFlowController.this.ctx, cause);
        }
    }
    
    private class WritabilityMonitor
    {
        private boolean inWritePendingBytes;
        private long totalPendingBytes;
        private final StreamByteDistributor.Writer writer;
        
        private WritabilityMonitor() {
            this.writer = new StreamByteDistributor.Writer() {
                @Override
                public void write(final Http2Stream stream, final int numBytes) {
                    DefaultHttp2RemoteFlowController.this.state(stream).writeAllocatedBytes(numBytes);
                }
            };
        }
        
        void channelWritabilityChange() throws Http2Exception {
        }
        
        void stateCancelled(final FlowState state) {
        }
        
        void windowSize(final FlowState state, final int initialWindowSize) {
            state.windowSize(initialWindowSize);
        }
        
        void incrementWindowSize(final FlowState state, final int delta) throws Http2Exception {
            state.incrementStreamWindow(delta);
        }
        
        void enqueueFrame(final FlowState state, final FlowControlled frame) throws Http2Exception {
            state.enqueueFrame(frame);
        }
        
        final void incrementPendingBytes(final int delta) {
            this.totalPendingBytes += delta;
        }
        
        final boolean isWritable(final FlowState state) {
            return this.isWritableConnection() && state.isWritable();
        }
        
        final void writePendingBytes() throws Http2Exception {
            if (this.inWritePendingBytes) {
                return;
            }
            this.inWritePendingBytes = true;
            try {
                int bytesToWrite = DefaultHttp2RemoteFlowController.this.writableBytes();
                while (DefaultHttp2RemoteFlowController.this.streamByteDistributor.distribute(bytesToWrite, this.writer) && (bytesToWrite = DefaultHttp2RemoteFlowController.this.writableBytes()) > 0 && DefaultHttp2RemoteFlowController.this.isChannelWritable0()) {}
            }
            finally {
                this.inWritePendingBytes = false;
            }
        }
        
        void initialWindowSize(final int newWindowSize) throws Http2Exception {
            if (newWindowSize < 0) {
                throw new IllegalArgumentException("Invalid initial window size: " + newWindowSize);
            }
            final int delta = newWindowSize - DefaultHttp2RemoteFlowController.this.initialWindowSize;
            DefaultHttp2RemoteFlowController.this.initialWindowSize = newWindowSize;
            DefaultHttp2RemoteFlowController.this.connection.forEachActiveStream(new Http2StreamVisitor() {
                @Override
                public boolean visit(final Http2Stream stream) throws Http2Exception {
                    DefaultHttp2RemoteFlowController.this.state(stream).incrementStreamWindow(delta);
                    return true;
                }
            });
            if (delta > 0 && DefaultHttp2RemoteFlowController.this.isChannelWritable()) {
                this.writePendingBytes();
            }
        }
        
        final boolean isWritableConnection() {
            return DefaultHttp2RemoteFlowController.this.connectionState.windowSize() - this.totalPendingBytes > 0L && DefaultHttp2RemoteFlowController.this.isChannelWritable();
        }
    }
    
    private final class ListenerWritabilityMonitor extends WritabilityMonitor
    {
        private final Listener listener;
        private final Http2StreamVisitor checkStreamWritabilityVisitor;
        
        ListenerWritabilityMonitor(final Listener listener) {
            this.checkStreamWritabilityVisitor = new Http2StreamVisitor() {
                @Override
                public boolean visit(final Http2Stream stream) throws Http2Exception {
                    final FlowState state = DefaultHttp2RemoteFlowController.this.state(stream);
                    if (ListenerWritabilityMonitor.this.isWritable(state) != state.markedWritability()) {
                        ListenerWritabilityMonitor.this.notifyWritabilityChanged(state);
                    }
                    return true;
                }
            };
            this.listener = listener;
        }
        
        @Override
        void windowSize(final FlowState state, final int initialWindowSize) {
            super.windowSize(state, initialWindowSize);
            try {
                this.checkStateWritability(state);
            }
            catch (final Http2Exception e) {
                throw new RuntimeException("Caught unexpected exception from window", e);
            }
        }
        
        @Override
        void incrementWindowSize(final FlowState state, final int delta) throws Http2Exception {
            super.incrementWindowSize(state, delta);
            this.checkStateWritability(state);
        }
        
        @Override
        void initialWindowSize(final int newWindowSize) throws Http2Exception {
            super.initialWindowSize(newWindowSize);
            if (this.isWritableConnection()) {
                this.checkAllWritabilityChanged();
            }
        }
        
        @Override
        void enqueueFrame(final FlowState state, final FlowControlled frame) throws Http2Exception {
            super.enqueueFrame(state, frame);
            this.checkConnectionThenStreamWritabilityChanged(state);
        }
        
        @Override
        void stateCancelled(final FlowState state) {
            try {
                this.checkConnectionThenStreamWritabilityChanged(state);
            }
            catch (final Http2Exception e) {
                throw new RuntimeException("Caught unexpected exception from checkAllWritabilityChanged", e);
            }
        }
        
        @Override
        void channelWritabilityChange() throws Http2Exception {
            if (DefaultHttp2RemoteFlowController.this.connectionState.markedWritability() != DefaultHttp2RemoteFlowController.this.isChannelWritable()) {
                this.checkAllWritabilityChanged();
            }
        }
        
        private void checkStateWritability(final FlowState state) throws Http2Exception {
            if (this.isWritable(state) != state.markedWritability()) {
                if (state == DefaultHttp2RemoteFlowController.this.connectionState) {
                    this.checkAllWritabilityChanged();
                }
                else {
                    this.notifyWritabilityChanged(state);
                }
            }
        }
        
        private void notifyWritabilityChanged(final FlowState state) {
            state.markedWritability(!state.markedWritability());
            try {
                this.listener.writabilityChanged(state.stream);
            }
            catch (final Throwable cause) {
                DefaultHttp2RemoteFlowController.logger.error("Caught Throwable from listener.writabilityChanged", cause);
            }
        }
        
        private void checkConnectionThenStreamWritabilityChanged(final FlowState state) throws Http2Exception {
            if (this.isWritableConnection() != DefaultHttp2RemoteFlowController.this.connectionState.markedWritability()) {
                this.checkAllWritabilityChanged();
            }
            else if (this.isWritable(state) != state.markedWritability()) {
                this.notifyWritabilityChanged(state);
            }
        }
        
        private void checkAllWritabilityChanged() throws Http2Exception {
            DefaultHttp2RemoteFlowController.this.connectionState.markedWritability(this.isWritableConnection());
            DefaultHttp2RemoteFlowController.this.connection.forEachActiveStream(this.checkStreamWritabilityVisitor);
        }
    }
}
