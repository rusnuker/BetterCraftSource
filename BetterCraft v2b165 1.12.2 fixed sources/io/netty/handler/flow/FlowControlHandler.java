// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.flow;

import io.netty.util.Recycler;
import java.util.ArrayDeque;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelDuplexHandler;

public class FlowControlHandler extends ChannelDuplexHandler
{
    private static final InternalLogger logger;
    private final boolean releaseMessages;
    private RecyclableArrayDeque queue;
    private ChannelConfig config;
    private boolean shouldConsume;
    
    public FlowControlHandler() {
        this(true);
    }
    
    public FlowControlHandler(final boolean releaseMessages) {
        this.releaseMessages = releaseMessages;
    }
    
    boolean isQueueEmpty() {
        return this.queue.isEmpty();
    }
    
    private void destroy() {
        if (this.queue != null) {
            if (!this.queue.isEmpty()) {
                FlowControlHandler.logger.trace("Non-empty queue: {}", this.queue);
                if (this.releaseMessages) {
                    Object msg;
                    while ((msg = this.queue.poll()) != null) {
                        ReferenceCountUtil.safeRelease(msg);
                    }
                }
            }
            this.queue.recycle();
            this.queue = null;
        }
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.config = ctx.channel().config();
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.destroy();
        ctx.fireChannelInactive();
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        if (this.dequeue(ctx, 1) == 0) {
            this.shouldConsume = true;
            ctx.read();
        }
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (this.queue == null) {
            this.queue = RecyclableArrayDeque.newInstance();
        }
        this.queue.offer(msg);
        final int minConsume = this.shouldConsume ? 1 : 0;
        this.shouldConsume = false;
        this.dequeue(ctx, minConsume);
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
    }
    
    private int dequeue(final ChannelHandlerContext ctx, final int minConsume) {
        if (this.queue != null) {
            int consumed = 0;
            while (consumed < minConsume || this.config.isAutoRead()) {
                final Object msg = this.queue.poll();
                if (msg == null) {
                    break;
                }
                ++consumed;
                ctx.fireChannelRead(msg);
            }
            if (this.queue.isEmpty() && consumed > 0) {
                ctx.fireChannelReadComplete();
            }
            return consumed;
        }
        return 0;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(FlowControlHandler.class);
    }
    
    private static final class RecyclableArrayDeque extends ArrayDeque<Object>
    {
        private static final long serialVersionUID = 0L;
        private static final int DEFAULT_NUM_ELEMENTS = 2;
        private static final Recycler<RecyclableArrayDeque> RECYCLER;
        private final Recycler.Handle<RecyclableArrayDeque> handle;
        
        public static RecyclableArrayDeque newInstance() {
            return RecyclableArrayDeque.RECYCLER.get();
        }
        
        private RecyclableArrayDeque(final int numElements, final Recycler.Handle<RecyclableArrayDeque> handle) {
            super(numElements);
            this.handle = handle;
        }
        
        public void recycle() {
            this.clear();
            this.handle.recycle(this);
        }
        
        static {
            RECYCLER = new Recycler<RecyclableArrayDeque>() {
                @Override
                protected RecyclableArrayDeque newObject(final Handle<RecyclableArrayDeque> handle) {
                    return new RecyclableArrayDeque(2, (Handle)handle);
                }
            };
        }
    }
}
