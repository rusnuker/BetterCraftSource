// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class DefaultEventLoopGroup extends MultithreadEventLoopGroup
{
    public DefaultEventLoopGroup() {
        this(0);
    }
    
    public DefaultEventLoopGroup(final int nThreads) {
        this(nThreads, (ThreadFactory)null);
    }
    
    public DefaultEventLoopGroup(final int nThreads, final ThreadFactory threadFactory) {
        super(nThreads, threadFactory, new Object[0]);
    }
    
    public DefaultEventLoopGroup(final int nThreads, final Executor executor) {
        super(nThreads, executor, new Object[0]);
    }
    
    @Override
    protected EventLoop newChild(final Executor executor, final Object... args) throws Exception {
        return new DefaultEventLoop(this, executor);
    }
}
