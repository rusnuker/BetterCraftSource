// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public final class DefaultEventExecutor extends SingleThreadEventExecutor
{
    public DefaultEventExecutor() {
        this((EventExecutorGroup)null);
    }
    
    public DefaultEventExecutor(final ThreadFactory threadFactory) {
        this(null, threadFactory);
    }
    
    public DefaultEventExecutor(final Executor executor) {
        this(null, executor);
    }
    
    public DefaultEventExecutor(final EventExecutorGroup parent) {
        this(parent, new DefaultThreadFactory(DefaultEventExecutor.class));
    }
    
    public DefaultEventExecutor(final EventExecutorGroup parent, final ThreadFactory threadFactory) {
        super(parent, threadFactory, true);
    }
    
    public DefaultEventExecutor(final EventExecutorGroup parent, final Executor executor) {
        super(parent, executor, true);
    }
    
    public DefaultEventExecutor(final EventExecutorGroup parent, final ThreadFactory threadFactory, final int maxPendingTasks, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, threadFactory, true, maxPendingTasks, rejectedExecutionHandler);
    }
    
    public DefaultEventExecutor(final EventExecutorGroup parent, final Executor executor, final int maxPendingTasks, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, true, maxPendingTasks, rejectedExecutionHandler);
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
