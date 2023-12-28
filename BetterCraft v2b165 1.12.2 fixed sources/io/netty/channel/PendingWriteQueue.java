// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.Recycler;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseCombiner;
import io.netty.util.internal.logging.InternalLogger;

public final class PendingWriteQueue
{
    private static final InternalLogger logger;
    private static final int PENDING_WRITE_OVERHEAD;
    private final ChannelHandlerContext ctx;
    private final ChannelOutboundBuffer buffer;
    private final MessageSizeEstimator.Handle estimatorHandle;
    private PendingWrite head;
    private PendingWrite tail;
    private int size;
    private long bytes;
    
    public PendingWriteQueue(final ChannelHandlerContext ctx) {
        if (ctx == null) {
            throw new NullPointerException("ctx");
        }
        this.ctx = ctx;
        this.buffer = ctx.channel().unsafe().outboundBuffer();
        this.estimatorHandle = ctx.channel().config().getMessageSizeEstimator().newHandle();
    }
    
    public boolean isEmpty() {
        assert this.ctx.executor().inEventLoop();
        return this.head == null;
    }
    
    public int size() {
        assert this.ctx.executor().inEventLoop();
        return this.size;
    }
    
    public long bytes() {
        assert this.ctx.executor().inEventLoop();
        return this.bytes;
    }
    
    private int size(final Object msg) {
        int messageSize = this.estimatorHandle.size(msg);
        if (messageSize < 0) {
            messageSize = 0;
        }
        return messageSize + PendingWriteQueue.PENDING_WRITE_OVERHEAD;
    }
    
    public void add(final Object msg, final ChannelPromise promise) {
        assert this.ctx.executor().inEventLoop();
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        final int messageSize = this.size(msg);
        final PendingWrite write = PendingWrite.newInstance(msg, messageSize, promise);
        final PendingWrite currentTail = this.tail;
        if (currentTail == null) {
            final PendingWrite pendingWrite = write;
            this.head = pendingWrite;
            this.tail = pendingWrite;
        }
        else {
            currentTail.next = write;
            this.tail = write;
        }
        ++this.size;
        this.bytes += messageSize;
        if (this.buffer != null) {
            this.buffer.incrementPendingOutboundBytes(write.size);
        }
    }
    
    public ChannelFuture removeAndWriteAll() {
        assert this.ctx.executor().inEventLoop();
        if (this.isEmpty()) {
            return null;
        }
        final ChannelPromise p = this.ctx.newPromise();
        final PromiseCombiner combiner = new PromiseCombiner();
        try {
            for (PendingWrite write = this.head; write != null; write = this.head) {
                final PendingWrite pendingWrite = null;
                this.tail = pendingWrite;
                this.head = pendingWrite;
                this.size = 0;
                this.bytes = 0L;
                while (write != null) {
                    final PendingWrite next = write.next;
                    final Object msg = write.msg;
                    final ChannelPromise promise = write.promise;
                    this.recycle(write, false);
                    combiner.add(promise);
                    this.ctx.write(msg, promise);
                    write = next;
                }
            }
            combiner.finish(p);
        }
        catch (final Throwable cause) {
            p.setFailure(cause);
        }
        this.assertEmpty();
        return p;
    }
    
    public void removeAndFailAll(final Throwable cause) {
        assert this.ctx.executor().inEventLoop();
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        for (PendingWrite write = this.head; write != null; write = this.head) {
            final PendingWrite pendingWrite = null;
            this.tail = pendingWrite;
            this.head = pendingWrite;
            this.size = 0;
            this.bytes = 0L;
            while (write != null) {
                final PendingWrite next = write.next;
                ReferenceCountUtil.safeRelease(write.msg);
                final ChannelPromise promise = write.promise;
                this.recycle(write, false);
                safeFail(promise, cause);
                write = next;
            }
        }
        this.assertEmpty();
    }
    
    public void removeAndFail(final Throwable cause) {
        assert this.ctx.executor().inEventLoop();
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        final PendingWrite write = this.head;
        if (write == null) {
            return;
        }
        ReferenceCountUtil.safeRelease(write.msg);
        final ChannelPromise promise = write.promise;
        safeFail(promise, cause);
        this.recycle(write, true);
    }
    
    private void assertEmpty() {
        assert this.tail == null && this.head == null && this.size == 0;
    }
    
    public ChannelFuture removeAndWrite() {
        assert this.ctx.executor().inEventLoop();
        final PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        final Object msg = write.msg;
        final ChannelPromise promise = write.promise;
        this.recycle(write, true);
        return this.ctx.write(msg, promise);
    }
    
    public ChannelPromise remove() {
        assert this.ctx.executor().inEventLoop();
        final PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        final ChannelPromise promise = write.promise;
        ReferenceCountUtil.safeRelease(write.msg);
        this.recycle(write, true);
        return promise;
    }
    
    public Object current() {
        assert this.ctx.executor().inEventLoop();
        final PendingWrite write = this.head;
        if (write == null) {
            return null;
        }
        return write.msg;
    }
    
    private void recycle(final PendingWrite write, final boolean update) {
        final PendingWrite next = write.next;
        final long writeSize = write.size;
        if (update) {
            if (next == null) {
                final PendingWrite pendingWrite = null;
                this.tail = pendingWrite;
                this.head = pendingWrite;
                this.size = 0;
                this.bytes = 0L;
            }
            else {
                this.head = next;
                --this.size;
                this.bytes -= writeSize;
                assert this.size > 0 && this.bytes >= 0L;
            }
        }
        write.recycle();
        if (this.buffer != null) {
            this.buffer.decrementPendingOutboundBytes(writeSize);
        }
    }
    
    private static void safeFail(final ChannelPromise promise, final Throwable cause) {
        if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
            PendingWriteQueue.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PendingWriteQueue.class);
        PENDING_WRITE_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.pendingWriteSizeOverhead", 64);
    }
    
    static final class PendingWrite
    {
        private static final Recycler<PendingWrite> RECYCLER;
        private final Recycler.Handle<PendingWrite> handle;
        private PendingWrite next;
        private long size;
        private ChannelPromise promise;
        private Object msg;
        
        private PendingWrite(final Recycler.Handle<PendingWrite> handle) {
            this.handle = handle;
        }
        
        static PendingWrite newInstance(final Object msg, final int size, final ChannelPromise promise) {
            final PendingWrite write = PendingWrite.RECYCLER.get();
            write.size = size;
            write.msg = msg;
            write.promise = promise;
            return write;
        }
        
        private void recycle() {
            this.size = 0L;
            this.next = null;
            this.msg = null;
            this.promise = null;
            this.handle.recycle(this);
        }
        
        static {
            RECYCLER = new Recycler<PendingWrite>() {
                @Override
                protected PendingWrite newObject(final Handle<PendingWrite> handle) {
                    return new PendingWrite((Handle)handle);
                }
            };
        }
    }
}
