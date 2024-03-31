// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ConnectTimeoutException;
import java.nio.channels.ConnectionPendingException;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.AbstractChannel;
import io.netty.channel.EventLoop;
import java.util.concurrent.Executor;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.FileRegion;
import io.netty.channel.DefaultFileRegion;
import java.io.IOException;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.unix.Socket;
import io.netty.channel.Channel;
import java.nio.channels.WritableByteChannel;
import io.netty.channel.unix.FileDescriptor;
import java.util.Queue;
import java.net.SocketAddress;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelPromise;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.socket.DuplexChannel;

public abstract class AbstractEpollStreamChannel extends AbstractEpollChannel implements DuplexChannel
{
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private static final InternalLogger logger;
    private static final ClosedChannelException DO_CLOSE_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException CLEAR_SPLICE_QUEUE_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SPLICE_TO_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException FAIL_SPLICE_IF_CLOSED_CLOSED_CHANNEL_EXCEPTION;
    private ChannelPromise connectPromise;
    private ScheduledFuture<?> connectTimeoutFuture;
    private SocketAddress requestedRemoteAddress;
    private Queue<SpliceInTask> spliceQueue;
    private FileDescriptor pipeIn;
    private FileDescriptor pipeOut;
    private WritableByteChannel byteChannel;
    
    @Deprecated
    protected AbstractEpollStreamChannel(final Channel parent, final int fd) {
        this(parent, new Socket(fd));
    }
    
    @Deprecated
    protected AbstractEpollStreamChannel(final int fd) {
        this(new Socket(fd));
    }
    
    @Deprecated
    protected AbstractEpollStreamChannel(final FileDescriptor fd) {
        this(new Socket(fd.intValue()));
    }
    
    @Deprecated
    protected AbstractEpollStreamChannel(final Socket fd) {
        this(fd, AbstractEpollChannel.isSoErrorZero(fd));
    }
    
    protected AbstractEpollStreamChannel(final Channel parent, final Socket fd) {
        super(parent, fd, Native.EPOLLIN, true);
        this.flags |= Native.EPOLLRDHUP;
    }
    
    protected AbstractEpollStreamChannel(final Socket fd, final boolean active) {
        super(null, fd, Native.EPOLLIN, active);
        this.flags |= Native.EPOLLRDHUP;
    }
    
    @Override
    protected AbstractEpollUnsafe newUnsafe() {
        return new EpollStreamUnsafe();
    }
    
    @Override
    public ChannelMetadata metadata() {
        return AbstractEpollStreamChannel.METADATA;
    }
    
    public final ChannelFuture spliceTo(final AbstractEpollStreamChannel ch, final int len) {
        return this.spliceTo(ch, len, this.newPromise());
    }
    
    public final ChannelFuture spliceTo(final AbstractEpollStreamChannel ch, final int len, final ChannelPromise promise) {
        if (ch.eventLoop() != this.eventLoop()) {
            throw new IllegalArgumentException("EventLoops are not the same.");
        }
        if (len < 0) {
            throw new IllegalArgumentException("len: " + len + " (expected: >= 0)");
        }
        if (ch.config().getEpollMode() != EpollMode.LEVEL_TRIGGERED || this.config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
            throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
        }
        ObjectUtil.checkNotNull(promise, "promise");
        if (!this.isOpen()) {
            promise.tryFailure(AbstractEpollStreamChannel.SPLICE_TO_CLOSED_CHANNEL_EXCEPTION);
        }
        else {
            this.addToSpliceQueue(new SpliceInChannelTask(ch, len, promise));
            this.failSpliceIfClosed(promise);
        }
        return promise;
    }
    
    public final ChannelFuture spliceTo(final FileDescriptor ch, final int offset, final int len) {
        return this.spliceTo(ch, offset, len, this.newPromise());
    }
    
    public final ChannelFuture spliceTo(final FileDescriptor ch, final int offset, final int len, final ChannelPromise promise) {
        if (len < 0) {
            throw new IllegalArgumentException("len: " + len + " (expected: >= 0)");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be >= 0 but was " + offset);
        }
        if (this.config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
            throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
        }
        ObjectUtil.checkNotNull(promise, "promise");
        if (!this.isOpen()) {
            promise.tryFailure(AbstractEpollStreamChannel.SPLICE_TO_CLOSED_CHANNEL_EXCEPTION);
        }
        else {
            this.addToSpliceQueue(new SpliceFdTask(ch, offset, len, promise));
            this.failSpliceIfClosed(promise);
        }
        return promise;
    }
    
    private void failSpliceIfClosed(final ChannelPromise promise) {
        if (!this.isOpen() && promise.tryFailure(AbstractEpollStreamChannel.FAIL_SPLICE_IF_CLOSED_CLOSED_CHANNEL_EXCEPTION)) {
            this.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    AbstractEpollStreamChannel.this.clearSpliceQueue();
                }
            });
        }
    }
    
    private boolean writeBytes(final ChannelOutboundBuffer in, final ByteBuf buf, final int writeSpinCount) throws Exception {
        final int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            in.remove();
            return true;
        }
        if (buf.hasMemoryAddress() || buf.nioBufferCount() == 1) {
            final int writtenBytes = this.doWriteBytes(buf, writeSpinCount);
            in.removeBytes(writtenBytes);
            return writtenBytes == readableBytes;
        }
        final ByteBuffer[] nioBuffers = buf.nioBuffers();
        return this.writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes, writeSpinCount);
    }
    
    private boolean writeBytesMultiple(final ChannelOutboundBuffer in, final IovArray array, final int writeSpinCount) throws IOException {
        final long initialExpectedWrittenBytes;
        long expectedWrittenBytes = initialExpectedWrittenBytes = array.size();
        int cnt = array.count();
        assert expectedWrittenBytes != 0L;
        assert cnt != 0;
        boolean done = false;
        int offset = 0;
        final int end = offset + cnt;
        for (int i = writeSpinCount - 1; i >= 0; --i) {
            long localWrittenBytes = this.fd().writevAddresses(array.memoryAddress(offset), cnt);
            if (localWrittenBytes == 0L) {
                break;
            }
            expectedWrittenBytes -= localWrittenBytes;
            if (expectedWrittenBytes == 0L) {
                done = true;
                break;
            }
            do {
                final long bytes = array.processWritten(offset, localWrittenBytes);
                if (bytes == -1L) {
                    break;
                }
                ++offset;
                --cnt;
                localWrittenBytes -= bytes;
            } while (offset < end && localWrittenBytes > 0L);
        }
        in.removeBytes(initialExpectedWrittenBytes - expectedWrittenBytes);
        return done;
    }
    
    private boolean writeBytesMultiple(final ChannelOutboundBuffer in, final ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes, final int writeSpinCount) throws IOException {
        assert expectedWrittenBytes != 0L;
        final long initialExpectedWrittenBytes = expectedWrittenBytes;
        boolean done = false;
        int offset = 0;
        final int end = offset + nioBufferCnt;
        for (int i = writeSpinCount - 1; i >= 0; --i) {
            long localWrittenBytes = this.fd().writev(nioBuffers, offset, nioBufferCnt);
            if (localWrittenBytes == 0L) {
                break;
            }
            expectedWrittenBytes -= localWrittenBytes;
            if (expectedWrittenBytes == 0L) {
                done = true;
                break;
            }
            do {
                final ByteBuffer buffer = nioBuffers[offset];
                final int pos = buffer.position();
                final int bytes = buffer.limit() - pos;
                if (bytes > localWrittenBytes) {
                    buffer.position(pos + (int)localWrittenBytes);
                    break;
                }
                ++offset;
                --nioBufferCnt;
                localWrittenBytes -= bytes;
            } while (offset < end && localWrittenBytes > 0L);
        }
        in.removeBytes(initialExpectedWrittenBytes - expectedWrittenBytes);
        return done;
    }
    
    private boolean writeDefaultFileRegion(final ChannelOutboundBuffer in, final DefaultFileRegion region, final int writeSpinCount) throws Exception {
        final long regionCount = region.count();
        if (region.transferred() >= regionCount) {
            in.remove();
            return true;
        }
        final long baseOffset = region.position();
        boolean done = false;
        long flushedAmount = 0L;
        for (int i = writeSpinCount - 1; i >= 0; --i) {
            final long offset = region.transferred();
            final long localFlushedAmount = Native.sendfile(this.fd().intValue(), region, baseOffset, offset, regionCount - offset);
            if (localFlushedAmount == 0L) {
                break;
            }
            flushedAmount += localFlushedAmount;
            if (region.transferred() >= regionCount) {
                done = true;
                break;
            }
        }
        if (flushedAmount > 0L) {
            in.progress(flushedAmount);
        }
        if (done) {
            in.remove();
        }
        return done;
    }
    
    private boolean writeFileRegion(final ChannelOutboundBuffer in, final FileRegion region, final int writeSpinCount) throws Exception {
        if (region.transferred() >= region.count()) {
            in.remove();
            return true;
        }
        boolean done = false;
        long flushedAmount = 0L;
        if (this.byteChannel == null) {
            this.byteChannel = new SocketWritableByteChannel();
        }
        for (int i = writeSpinCount - 1; i >= 0; --i) {
            final long localFlushedAmount = region.transferTo(this.byteChannel, region.transferred());
            if (localFlushedAmount == 0L) {
                break;
            }
            flushedAmount += localFlushedAmount;
            if (region.transferred() >= region.count()) {
                done = true;
                break;
            }
        }
        if (flushedAmount > 0L) {
            in.progress(flushedAmount);
        }
        if (done) {
            in.remove();
        }
        return done;
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        final int writeSpinCount = this.config().getWriteSpinCount();
        while (true) {
            final int msgCount = in.size();
            if (msgCount == 0) {
                this.clearFlag(Native.EPOLLOUT);
                return;
            }
            if (msgCount > 1 && in.current() instanceof ByteBuf) {
                if (!this.doWriteMultiple(in, writeSpinCount)) {
                    break;
                }
                continue;
            }
            else {
                if (!this.doWriteSingle(in, writeSpinCount)) {
                    break;
                }
                continue;
            }
        }
        this.setFlag(Native.EPOLLOUT);
    }
    
    protected boolean doWriteSingle(final ChannelOutboundBuffer in, final int writeSpinCount) throws Exception {
        final Object msg = in.current();
        if (msg instanceof ByteBuf) {
            if (!this.writeBytes(in, (ByteBuf)msg, writeSpinCount)) {
                return false;
            }
        }
        else if (msg instanceof DefaultFileRegion) {
            if (!this.writeDefaultFileRegion(in, (DefaultFileRegion)msg, writeSpinCount)) {
                return false;
            }
        }
        else if (msg instanceof FileRegion) {
            if (!this.writeFileRegion(in, (FileRegion)msg, writeSpinCount)) {
                return false;
            }
        }
        else {
            if (!(msg instanceof SpliceOutTask)) {
                throw new Error();
            }
            if (!((SpliceOutTask)msg).spliceOut()) {
                return false;
            }
            in.remove();
        }
        return true;
    }
    
    private boolean doWriteMultiple(final ChannelOutboundBuffer in, final int writeSpinCount) throws Exception {
        if (PlatformDependent.hasUnsafe()) {
            final IovArray array = ((EpollEventLoop)this.eventLoop()).cleanArray();
            in.forEachFlushedMessage(array);
            final int cnt = array.count();
            if (cnt >= 1) {
                if (!this.writeBytesMultiple(in, array, writeSpinCount)) {
                    return false;
                }
            }
            else {
                in.removeBytes(0L);
            }
        }
        else {
            final ByteBuffer[] buffers = in.nioBuffers();
            final int cnt = in.nioBufferCount();
            if (cnt >= 1) {
                if (!this.writeBytesMultiple(in, buffers, cnt, in.nioBufferSize(), writeSpinCount)) {
                    return false;
                }
            }
            else {
                in.removeBytes(0L);
            }
        }
        return true;
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf)msg;
            if (!buf.hasMemoryAddress() && (PlatformDependent.hasUnsafe() || !buf.isDirect())) {
                if (buf instanceof CompositeByteBuf) {
                    final CompositeByteBuf comp = (CompositeByteBuf)buf;
                    if (!comp.isDirect() || comp.nioBufferCount() > Native.IOV_MAX) {
                        buf = this.newDirectBuffer(buf);
                        assert buf.hasMemoryAddress();
                    }
                }
                else {
                    buf = this.newDirectBuffer(buf);
                    assert buf.hasMemoryAddress();
                }
            }
            return buf;
        }
        if (msg instanceof FileRegion || msg instanceof SpliceOutTask) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + AbstractEpollStreamChannel.EXPECTED_TYPES);
    }
    
    private void shutdownOutput0(final ChannelPromise promise) {
        try {
            this.fd().shutdown(false, true);
            promise.setSuccess();
        }
        catch (final Throwable cause) {
            promise.setFailure(cause);
        }
    }
    
    private void shutdownInput0(final ChannelPromise promise) {
        try {
            this.fd().shutdown(true, false);
            promise.setSuccess();
        }
        catch (final Throwable cause) {
            promise.setFailure(cause);
        }
    }
    
    private void shutdown0(final ChannelPromise promise) {
        try {
            this.fd().shutdown(true, true);
            promise.setSuccess();
        }
        catch (final Throwable cause) {
            promise.setFailure(cause);
        }
    }
    
    @Override
    public boolean isOutputShutdown() {
        return this.fd().isOutputShutdown();
    }
    
    @Override
    public boolean isInputShutdown() {
        return this.fd().isInputShutdown();
    }
    
    @Override
    public boolean isShutdown() {
        return this.fd().isShutdown();
    }
    
    @Override
    public ChannelFuture shutdownOutput() {
        return this.shutdownOutput(this.newPromise());
    }
    
    @Override
    public ChannelFuture shutdownOutput(final ChannelPromise promise) {
        final Executor closeExecutor = ((EpollStreamUnsafe)this.unsafe()).prepareToClose();
        if (closeExecutor != null) {
            closeExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractEpollStreamChannel.this.shutdownOutput0(promise);
                }
            });
        }
        else {
            final EventLoop loop = this.eventLoop();
            if (loop.inEventLoop()) {
                this.shutdownOutput0(promise);
            }
            else {
                loop.execute(new Runnable() {
                    @Override
                    public void run() {
                        AbstractEpollStreamChannel.this.shutdownOutput0(promise);
                    }
                });
            }
        }
        return promise;
    }
    
    @Override
    public ChannelFuture shutdownInput() {
        return this.shutdownInput(this.newPromise());
    }
    
    @Override
    public ChannelFuture shutdownInput(final ChannelPromise promise) {
        final Executor closeExecutor = ((EpollStreamUnsafe)this.unsafe()).prepareToClose();
        if (closeExecutor != null) {
            closeExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractEpollStreamChannel.this.shutdownInput0(promise);
                }
            });
        }
        else {
            final EventLoop loop = this.eventLoop();
            if (loop.inEventLoop()) {
                this.shutdownInput0(promise);
            }
            else {
                loop.execute(new Runnable() {
                    @Override
                    public void run() {
                        AbstractEpollStreamChannel.this.shutdownInput0(promise);
                    }
                });
            }
        }
        return promise;
    }
    
    @Override
    public ChannelFuture shutdown() {
        return this.shutdown(this.newPromise());
    }
    
    @Override
    public ChannelFuture shutdown(final ChannelPromise promise) {
        final Executor closeExecutor = ((EpollStreamUnsafe)this.unsafe()).prepareToClose();
        if (closeExecutor != null) {
            closeExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractEpollStreamChannel.this.shutdown0(promise);
                }
            });
        }
        else {
            final EventLoop loop = this.eventLoop();
            if (loop.inEventLoop()) {
                this.shutdown0(promise);
            }
            else {
                loop.execute(new Runnable() {
                    @Override
                    public void run() {
                        AbstractEpollStreamChannel.this.shutdown0(promise);
                    }
                });
            }
        }
        return promise;
    }
    
    @Override
    protected void doClose() throws Exception {
        try {
            final ChannelPromise promise = this.connectPromise;
            if (promise != null) {
                promise.tryFailure(AbstractEpollStreamChannel.DO_CLOSE_CLOSED_CHANNEL_EXCEPTION);
                this.connectPromise = null;
            }
            final ScheduledFuture<?> future = this.connectTimeoutFuture;
            if (future != null) {
                future.cancel(false);
                this.connectTimeoutFuture = null;
            }
            super.doClose();
        }
        finally {
            safeClosePipe(this.pipeIn);
            safeClosePipe(this.pipeOut);
            this.clearSpliceQueue();
        }
    }
    
    private void clearSpliceQueue() {
        if (this.spliceQueue == null) {
            return;
        }
        while (true) {
            final SpliceInTask task = this.spliceQueue.poll();
            if (task == null) {
                break;
            }
            task.promise.tryFailure(AbstractEpollStreamChannel.CLEAR_SPLICE_QUEUE_CLOSED_CHANNEL_EXCEPTION);
        }
    }
    
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.fd().bind(localAddress);
        }
        boolean success = false;
        try {
            final boolean connected = this.fd().connect(remoteAddress);
            if (!connected) {
                this.setFlag(Native.EPOLLOUT);
            }
            success = true;
            return connected;
        }
        finally {
            if (!success) {
                this.doClose();
            }
        }
    }
    
    private static void safeClosePipe(final FileDescriptor fd) {
        if (fd != null) {
            try {
                fd.close();
            }
            catch (final IOException e) {
                if (AbstractEpollStreamChannel.logger.isWarnEnabled()) {
                    AbstractEpollStreamChannel.logger.warn("Error while closing a pipe", e);
                }
            }
        }
    }
    
    private void addToSpliceQueue(final SpliceInTask task) {
        final EventLoop eventLoop = this.eventLoop();
        if (eventLoop.inEventLoop()) {
            this.addToSpliceQueue0(task);
        }
        else {
            eventLoop.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractEpollStreamChannel.this.addToSpliceQueue0(task);
                }
            });
        }
    }
    
    private void addToSpliceQueue0(final SpliceInTask task) {
        if (this.spliceQueue == null) {
            this.spliceQueue = PlatformDependent.newMpscQueue();
        }
        this.spliceQueue.add(task);
    }
    
    static {
        METADATA = new ChannelMetadata(false, 16);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
        logger = InternalLoggerFactory.getInstance(AbstractEpollStreamChannel.class);
        DO_CLOSE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "doClose()");
        CLEAR_SPLICE_QUEUE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "clearSpliceQueue()");
        SPLICE_TO_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "spliceTo(...)");
        FAIL_SPLICE_IF_CLOSED_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "failSpliceIfClosed(...)");
    }
    
    class EpollStreamUnsafe extends AbstractEpollUnsafe
    {
        @Override
        protected Executor prepareToClose() {
            return super.prepareToClose();
        }
        
        private void handleReadException(final ChannelPipeline pipeline, final ByteBuf byteBuf, final Throwable cause, final boolean close, final EpollRecvByteAllocatorHandle allocHandle) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    this.readPending = false;
                    pipeline.fireChannelRead((Object)byteBuf);
                }
                else {
                    byteBuf.release();
                }
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            pipeline.fireExceptionCaught(cause);
            if (close || cause instanceof IOException) {
                this.shutdownInput(false);
            }
        }
        
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            try {
                if (AbstractEpollStreamChannel.this.connectPromise != null) {
                    throw new ConnectionPendingException();
                }
                final boolean wasActive = AbstractEpollStreamChannel.this.isActive();
                if (AbstractEpollStreamChannel.this.doConnect(remoteAddress, localAddress)) {
                    this.fulfillConnectPromise(promise, wasActive);
                }
                else {
                    AbstractEpollStreamChannel.this.connectPromise = promise;
                    AbstractEpollStreamChannel.this.requestedRemoteAddress = remoteAddress;
                    final int connectTimeoutMillis = AbstractEpollStreamChannel.this.config().getConnectTimeoutMillis();
                    if (connectTimeoutMillis > 0) {
                        AbstractEpollStreamChannel.this.connectTimeoutFuture = AbstractEpollStreamChannel.this.eventLoop().schedule((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                final ChannelPromise connectPromise = AbstractEpollStreamChannel.this.connectPromise;
                                final ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
                                if (connectPromise != null && connectPromise.tryFailure(cause)) {
                                    EpollStreamUnsafe.this.close(EpollStreamUnsafe.this.voidPromise());
                                }
                            }
                        }, (long)connectTimeoutMillis, TimeUnit.MILLISECONDS);
                    }
                    promise.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            if (future.isCancelled()) {
                                if (AbstractEpollStreamChannel.this.connectTimeoutFuture != null) {
                                    AbstractEpollStreamChannel.this.connectTimeoutFuture.cancel(false);
                                }
                                AbstractEpollStreamChannel.this.connectPromise = null;
                                EpollStreamUnsafe.this.close(EpollStreamUnsafe.this.voidPromise());
                            }
                        }
                    });
                }
            }
            catch (final Throwable t) {
                this.closeIfClosed();
                promise.tryFailure(this.annotateConnectException(t, remoteAddress));
            }
        }
        
        private void fulfillConnectPromise(final ChannelPromise promise, final boolean wasActive) {
            if (promise == null) {
                return;
            }
            AbstractEpollStreamChannel.this.active = true;
            final boolean active = AbstractEpollStreamChannel.this.isActive();
            final boolean promiseSet = promise.trySuccess();
            if (!wasActive && active) {
                AbstractEpollStreamChannel.this.pipeline().fireChannelActive();
            }
            if (!promiseSet) {
                this.close(this.voidPromise());
            }
        }
        
        private void fulfillConnectPromise(final ChannelPromise promise, final Throwable cause) {
            if (promise == null) {
                return;
            }
            promise.tryFailure(cause);
            this.closeIfClosed();
        }
        
        private void finishConnect() {
            assert AbstractEpollStreamChannel.this.eventLoop().inEventLoop();
            boolean connectStillInProgress = false;
            try {
                final boolean wasActive = AbstractEpollStreamChannel.this.isActive();
                if (!this.doFinishConnect()) {
                    connectStillInProgress = true;
                    return;
                }
                this.fulfillConnectPromise(AbstractEpollStreamChannel.this.connectPromise, wasActive);
            }
            catch (final Throwable t) {
                this.fulfillConnectPromise(AbstractEpollStreamChannel.this.connectPromise, this.annotateConnectException(t, AbstractEpollStreamChannel.this.requestedRemoteAddress));
            }
            finally {
                if (!connectStillInProgress) {
                    if (AbstractEpollStreamChannel.this.connectTimeoutFuture != null) {
                        AbstractEpollStreamChannel.this.connectTimeoutFuture.cancel(false);
                    }
                    AbstractEpollStreamChannel.this.connectPromise = null;
                }
            }
        }
        
        @Override
        void epollOutReady() {
            if (AbstractEpollStreamChannel.this.connectPromise != null) {
                this.finishConnect();
            }
            else {
                super.epollOutReady();
            }
        }
        
        boolean doFinishConnect() throws Exception {
            if (AbstractEpollStreamChannel.this.fd().finishConnect()) {
                AbstractEpollStreamChannel.this.clearFlag(Native.EPOLLOUT);
                return true;
            }
            AbstractEpollStreamChannel.this.setFlag(Native.EPOLLOUT);
            return false;
        }
        
        @Override
        EpollRecvByteAllocatorHandle newEpollHandle(final RecvByteBufAllocator.ExtendedHandle handle) {
            return new EpollRecvByteAllocatorStreamingHandle(handle);
        }
        
        @Override
        void epollInReady() {
            final ChannelConfig config = AbstractEpollStreamChannel.this.config();
            if (AbstractEpollStreamChannel.this.shouldBreakEpollInReady(config)) {
                this.clearEpollIn0();
                return;
            }
            final EpollRecvByteAllocatorHandle allocHandle = this.recvBufAllocHandle();
            allocHandle.edgeTriggered(AbstractEpollStreamChannel.this.isFlagSet(Native.EPOLLET));
            final ChannelPipeline pipeline = AbstractEpollStreamChannel.this.pipeline();
            final ByteBufAllocator allocator = config.getAllocator();
            allocHandle.reset(config);
            this.epollInBefore();
            ByteBuf byteBuf = null;
            boolean close = false;
            try {
                do {
                    if (AbstractEpollStreamChannel.this.spliceQueue != null) {
                        final SpliceInTask spliceTask = AbstractEpollStreamChannel.this.spliceQueue.peek();
                        if (spliceTask != null) {
                            if (!spliceTask.spliceIn(allocHandle)) {
                                break;
                            }
                            if (AbstractEpollStreamChannel.this.isActive()) {
                                AbstractEpollStreamChannel.this.spliceQueue.remove();
                                continue;
                            }
                            continue;
                        }
                    }
                    byteBuf = allocHandle.allocate(allocator);
                    allocHandle.lastBytesRead(AbstractEpollStreamChannel.this.doReadBytes(byteBuf));
                    if (allocHandle.lastBytesRead() <= 0) {
                        byteBuf.release();
                        byteBuf = null;
                        close = (allocHandle.lastBytesRead() < 0);
                        break;
                    }
                    allocHandle.incMessagesRead(1);
                    this.readPending = false;
                    pipeline.fireChannelRead((Object)byteBuf);
                    byteBuf = null;
                    if (AbstractEpollStreamChannel.this.shouldBreakEpollInReady(config)) {
                        break;
                    }
                } while (allocHandle.continueReading());
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                if (close) {
                    this.shutdownInput(false);
                }
            }
            catch (final Throwable t) {
                this.handleReadException(pipeline, byteBuf, t, close, allocHandle);
            }
            finally {
                this.epollInFinally(config);
            }
        }
    }
    
    protected abstract class SpliceInTask
    {
        final ChannelPromise promise;
        int len;
        
        protected SpliceInTask(final int len, final ChannelPromise promise) {
            this.promise = promise;
            this.len = len;
        }
        
        abstract boolean spliceIn(final RecvByteBufAllocator.Handle p0);
        
        protected final int spliceIn(final FileDescriptor pipeOut, final RecvByteBufAllocator.Handle handle) throws IOException {
            int length = Math.min(handle.guess(), this.len);
            int splicedIn = 0;
            while (true) {
                final int localSplicedIn = Native.splice(AbstractEpollStreamChannel.this.fd().intValue(), -1L, pipeOut.intValue(), -1L, length);
                if (localSplicedIn == 0) {
                    break;
                }
                splicedIn += localSplicedIn;
                length -= localSplicedIn;
            }
            return splicedIn;
        }
    }
    
    private final class SpliceInChannelTask extends SpliceInTask implements ChannelFutureListener
    {
        private final AbstractEpollStreamChannel ch;
        
        SpliceInChannelTask(final AbstractEpollStreamChannel ch, final int len, final ChannelPromise promise) {
            super(len, promise);
            this.ch = ch;
        }
        
        @Override
        public void operationComplete(final ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                this.promise.setFailure(future.cause());
            }
        }
        
        public boolean spliceIn(final RecvByteBufAllocator.Handle handle) {
            assert this.ch.eventLoop().inEventLoop();
            if (this.len == 0) {
                this.promise.setSuccess();
                return true;
            }
            try {
                FileDescriptor pipeOut = this.ch.pipeOut;
                if (pipeOut == null) {
                    final FileDescriptor[] pipe = FileDescriptor.pipe();
                    this.ch.pipeIn = pipe[0];
                    pipeOut = (this.ch.pipeOut = pipe[1]);
                }
                final int splicedIn = this.spliceIn(pipeOut, handle);
                if (splicedIn > 0) {
                    if (this.len != Integer.MAX_VALUE) {
                        this.len -= splicedIn;
                    }
                    ChannelPromise splicePromise;
                    if (this.len == 0) {
                        splicePromise = this.promise;
                    }
                    else {
                        splicePromise = this.ch.newPromise().addListener((GenericFutureListener<? extends Future<? super Void>>)this);
                    }
                    final boolean autoRead = AbstractEpollStreamChannel.this.config().isAutoRead();
                    this.ch.unsafe().write(new SpliceOutTask(this.ch, splicedIn, autoRead), splicePromise);
                    this.ch.unsafe().flush();
                    if (autoRead && !splicePromise.isDone()) {
                        AbstractEpollStreamChannel.this.config().setAutoRead(false);
                    }
                }
                return this.len == 0;
            }
            catch (final Throwable cause) {
                this.promise.setFailure(cause);
                return true;
            }
        }
    }
    
    private final class SpliceOutTask
    {
        private final AbstractEpollStreamChannel ch;
        private final boolean autoRead;
        private int len;
        
        SpliceOutTask(final AbstractEpollStreamChannel ch, final int len, final boolean autoRead) {
            this.ch = ch;
            this.len = len;
            this.autoRead = autoRead;
        }
        
        public boolean spliceOut() throws Exception {
            assert this.ch.eventLoop().inEventLoop();
            try {
                final int splicedOut = Native.splice(this.ch.pipeIn.intValue(), -1L, this.ch.fd().intValue(), -1L, this.len);
                this.len -= splicedOut;
                if (this.len == 0) {
                    if (this.autoRead) {
                        AbstractEpollStreamChannel.this.config().setAutoRead(true);
                    }
                    return true;
                }
                return false;
            }
            catch (final IOException e) {
                if (this.autoRead) {
                    AbstractEpollStreamChannel.this.config().setAutoRead(true);
                }
                throw e;
            }
        }
    }
    
    private final class SpliceFdTask extends SpliceInTask
    {
        private final FileDescriptor fd;
        private final ChannelPromise promise;
        private final int offset;
        
        SpliceFdTask(final FileDescriptor fd, final int offset, final int len, final ChannelPromise promise) {
            super(len, promise);
            this.fd = fd;
            this.promise = promise;
            this.offset = offset;
        }
        
        public boolean spliceIn(final RecvByteBufAllocator.Handle handle) {
            assert AbstractEpollStreamChannel.this.eventLoop().inEventLoop();
            if (this.len == 0) {
                this.promise.setSuccess();
                return true;
            }
            try {
                final FileDescriptor[] pipe = FileDescriptor.pipe();
                final FileDescriptor pipeIn = pipe[0];
                final FileDescriptor pipeOut = pipe[1];
                try {
                    int splicedIn = this.spliceIn(pipeOut, handle);
                    if (splicedIn > 0) {
                        if (this.len != Integer.MAX_VALUE) {
                            this.len -= splicedIn;
                        }
                        do {
                            final int splicedOut = Native.splice(pipeIn.intValue(), -1L, this.fd.intValue(), this.offset, splicedIn);
                            splicedIn -= splicedOut;
                        } while (splicedIn > 0);
                        if (this.len == 0) {
                            this.promise.setSuccess();
                            return true;
                        }
                    }
                    return false;
                }
                finally {
                    safeClosePipe(pipeIn);
                    safeClosePipe(pipeOut);
                }
            }
            catch (final Throwable cause) {
                this.promise.setFailure(cause);
                return true;
            }
        }
    }
    
    private final class SocketWritableByteChannel implements WritableByteChannel
    {
        @Override
        public int write(final ByteBuffer src) throws IOException {
            final int position = src.position();
            final int limit = src.limit();
            int written;
            if (src.isDirect()) {
                written = AbstractEpollStreamChannel.this.fd().write(src, position, src.limit());
            }
            else {
                final int readableBytes = limit - position;
                ByteBuf buffer = null;
                try {
                    if (readableBytes == 0) {
                        buffer = Unpooled.EMPTY_BUFFER;
                    }
                    else {
                        final ByteBufAllocator alloc = AbstractEpollStreamChannel.this.alloc();
                        if (alloc.isDirectBufferPooled()) {
                            buffer = alloc.directBuffer(readableBytes);
                        }
                        else {
                            buffer = ByteBufUtil.threadLocalDirectBuffer();
                            if (buffer == null) {
                                buffer = Unpooled.directBuffer(readableBytes);
                            }
                        }
                    }
                    buffer.writeBytes(src.duplicate());
                    final ByteBuffer nioBuffer = buffer.internalNioBuffer(buffer.readerIndex(), readableBytes);
                    written = AbstractEpollStreamChannel.this.fd().write(nioBuffer, nioBuffer.position(), nioBuffer.limit());
                }
                finally {
                    if (buffer != null) {
                        buffer.release();
                    }
                }
            }
            if (written > 0) {
                src.position(position + written);
            }
            return written;
        }
        
        @Override
        public boolean isOpen() {
            return AbstractEpollStreamChannel.this.fd().isOpen();
        }
        
        @Override
        public void close() throws IOException {
            AbstractEpollStreamChannel.this.fd().close();
        }
    }
}
