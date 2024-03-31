// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.EventLoop;
import java.util.Iterator;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.concurrent.EventExecutorChooserFactory;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import io.netty.channel.DefaultSelectStrategyFactory;
import java.util.concurrent.Executor;
import io.netty.channel.SelectStrategyFactory;
import java.util.concurrent.ThreadFactory;
import io.netty.channel.MultithreadEventLoopGroup;

public final class EpollEventLoopGroup extends MultithreadEventLoopGroup
{
    public EpollEventLoopGroup() {
        this(0);
    }
    
    public EpollEventLoopGroup(final int nThreads) {
        this(nThreads, (ThreadFactory)null);
    }
    
    public EpollEventLoopGroup(final int nThreads, final SelectStrategyFactory selectStrategyFactory) {
        this(nThreads, (ThreadFactory)null, selectStrategyFactory);
    }
    
    public EpollEventLoopGroup(final int nThreads, final ThreadFactory threadFactory) {
        this(nThreads, threadFactory, 0);
    }
    
    public EpollEventLoopGroup(final int nThreads, final Executor executor) {
        this(nThreads, executor, DefaultSelectStrategyFactory.INSTANCE);
    }
    
    public EpollEventLoopGroup(final int nThreads, final ThreadFactory threadFactory, final SelectStrategyFactory selectStrategyFactory) {
        this(nThreads, threadFactory, 0, selectStrategyFactory);
    }
    
    @Deprecated
    public EpollEventLoopGroup(final int nThreads, final ThreadFactory threadFactory, final int maxEventsAtOnce) {
        this(nThreads, threadFactory, maxEventsAtOnce, DefaultSelectStrategyFactory.INSTANCE);
    }
    
    @Deprecated
    public EpollEventLoopGroup(final int nThreads, final ThreadFactory threadFactory, final int maxEventsAtOnce, final SelectStrategyFactory selectStrategyFactory) {
        super(nThreads, threadFactory, new Object[] { maxEventsAtOnce, selectStrategyFactory, RejectedExecutionHandlers.reject() });
        Epoll.ensureAvailability();
    }
    
    public EpollEventLoopGroup(final int nThreads, final Executor executor, final SelectStrategyFactory selectStrategyFactory) {
        super(nThreads, executor, new Object[] { 0, selectStrategyFactory, RejectedExecutionHandlers.reject() });
        Epoll.ensureAvailability();
    }
    
    public EpollEventLoopGroup(final int nThreads, final Executor executor, final EventExecutorChooserFactory chooserFactory, final SelectStrategyFactory selectStrategyFactory) {
        super(nThreads, executor, chooserFactory, new Object[] { 0, selectStrategyFactory, RejectedExecutionHandlers.reject() });
        Epoll.ensureAvailability();
    }
    
    public EpollEventLoopGroup(final int nThreads, final Executor executor, final EventExecutorChooserFactory chooserFactory, final SelectStrategyFactory selectStrategyFactory, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(nThreads, executor, chooserFactory, new Object[] { 0, selectStrategyFactory, rejectedExecutionHandler });
        Epoll.ensureAvailability();
    }
    
    public void setIoRatio(final int ioRatio) {
        for (final EventExecutor e : this) {
            ((EpollEventLoop)e).setIoRatio(ioRatio);
        }
    }
    
    @Override
    protected EventLoop newChild(final Executor executor, final Object... args) throws Exception {
        return new EpollEventLoop(this, executor, (int)args[0], ((SelectStrategyFactory)args[1]).newSelectStrategy(), (RejectedExecutionHandler)args[2]);
    }
}
