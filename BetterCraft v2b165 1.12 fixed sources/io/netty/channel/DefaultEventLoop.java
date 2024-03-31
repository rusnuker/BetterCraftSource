// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class DefaultEventLoop extends SingleThreadEventLoop
{
    public DefaultEventLoop() {
        this((EventLoopGroup)null);
    }
    
    public DefaultEventLoop(final ThreadFactory threadFactory) {
        this(null, threadFactory);
    }
    
    public DefaultEventLoop(final Executor executor) {
        this(null, executor);
    }
    
    public DefaultEventLoop(final EventLoopGroup parent) {
        this(parent, new DefaultThreadFactory(DefaultEventLoop.class));
    }
    
    public DefaultEventLoop(final EventLoopGroup parent, final ThreadFactory threadFactory) {
        super(parent, threadFactory, true);
    }
    
    public DefaultEventLoop(final EventLoopGroup parent, final Executor executor) {
        super(parent, executor, true);
    }
    
    @Override
    protected void run() {
        do {
            final Runnable task = this.takeTask();
            if (task != null) {
                task.run();
                this.updateLastExecutionTime();
            }
        } while (!this.confirmShutdown());
    }
}
