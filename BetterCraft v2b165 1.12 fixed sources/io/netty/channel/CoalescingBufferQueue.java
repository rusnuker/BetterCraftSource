// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import java.util.Collection;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayDeque;

public final class CoalescingBufferQueue
{
    private final Channel channel;
    private final ArrayDeque<Object> bufAndListenerPairs;
    private int readableBytes;
    
    public CoalescingBufferQueue(final Channel channel) {
        this(channel, 4);
    }
    
    public CoalescingBufferQueue(final Channel channel, final int initSize) {
        this.channel = ObjectUtil.checkNotNull(channel, "channel");
        this.bufAndListenerPairs = new ArrayDeque<Object>(initSize);
    }
    
    public void add(final ByteBuf buf) {
        this.add(buf, (ChannelFutureListener)null);
    }
    
    public void add(final ByteBuf buf, final ChannelPromise promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        this.add(buf, promise.isVoid() ? null : new ChannelPromiseNotifier(new ChannelPromise[] { promise }));
    }
    
    public void add(final ByteBuf buf, final ChannelFutureListener listener) {
        ObjectUtil.checkNotNull(buf, "buf");
        if (this.readableBytes > Integer.MAX_VALUE - buf.readableBytes()) {
            throw new IllegalStateException("buffer queue length overflow: " + this.readableBytes + " + " + buf.readableBytes());
        }
        this.bufAndListenerPairs.add(buf);
        if (listener != null) {
            this.bufAndListenerPairs.add(listener);
        }
        this.readableBytes += buf.readableBytes();
    }
    
    public ByteBuf remove(int bytes, final ChannelPromise aggregatePromise) {
        if (bytes < 0) {
            throw new IllegalArgumentException("bytes (expected >= 0): " + bytes);
        }
        ObjectUtil.checkNotNull(aggregatePromise, "aggregatePromise");
        if (this.bufAndListenerPairs.isEmpty()) {
            return Unpooled.EMPTY_BUFFER;
        }
        bytes = Math.min(bytes, this.readableBytes);
        ByteBuf toReturn = null;
        final int originalBytes = bytes;
        while (true) {
            final Object entry = this.bufAndListenerPairs.poll();
            if (entry == null) {
                break;
            }
            if (entry instanceof ChannelFutureListener) {
                aggregatePromise.addListener((GenericFutureListener<? extends Future<? super Void>>)entry);
            }
            else {
                final ByteBuf entryBuffer = (ByteBuf)entry;
                if (entryBuffer.readableBytes() > bytes) {
                    this.bufAndListenerPairs.addFirst(entryBuffer);
                    if (bytes > 0) {
                        toReturn = this.compose(toReturn, entryBuffer.readRetainedSlice(bytes));
                        bytes = 0;
                        break;
                    }
                    break;
                }
                else {
                    toReturn = this.compose(toReturn, entryBuffer);
                    bytes -= entryBuffer.readableBytes();
                }
            }
        }
        this.readableBytes -= originalBytes - bytes;
        assert this.readableBytes >= 0;
        return toReturn;
    }
    
    private ByteBuf compose(final ByteBuf current, final ByteBuf next) {
        if (current == null) {
            return next;
        }
        if (current instanceof CompositeByteBuf) {
            final CompositeByteBuf composite = (CompositeByteBuf)current;
            composite.addComponent(true, next);
            return composite;
        }
        final CompositeByteBuf composite = this.channel.alloc().compositeBuffer(this.bufAndListenerPairs.size() + 2);
        composite.addComponent(true, current);
        composite.addComponent(true, next);
        return composite;
    }
    
    public int readableBytes() {
        return this.readableBytes;
    }
    
    public boolean isEmpty() {
        return this.bufAndListenerPairs.isEmpty();
    }
    
    public void releaseAndFailAll(final Throwable cause) {
        this.releaseAndCompleteAll(this.channel.newFailedFuture(cause));
    }
    
    private void releaseAndCompleteAll(final ChannelFuture future) {
        this.readableBytes = 0;
        Throwable pending = null;
        while (true) {
            final Object entry = this.bufAndListenerPairs.poll();
            if (entry == null) {
                break;
            }
            try {
                if (entry instanceof ByteBuf) {
                    ReferenceCountUtil.safeRelease(entry);
                }
                else {
                    ((ChannelFutureListener)entry).operationComplete(future);
                }
            }
            catch (final Throwable t) {
                pending = t;
            }
        }
        if (pending != null) {
            throw new IllegalStateException(pending);
        }
    }
    
    public void copyTo(final CoalescingBufferQueue dest) {
        dest.bufAndListenerPairs.addAll(this.bufAndListenerPairs);
        dest.readableBytes += this.readableBytes;
    }
}
