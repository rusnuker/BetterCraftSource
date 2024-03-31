// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class DefaultEventExecutorGroup extends MultithreadEventExecutorGroup
{
    public DefaultEventExecutorGroup(final int nThreads) {
        this(nThreads, null);
    }
    
    public DefaultEventExecutorGroup(final int nThreads, final ThreadFactory threadFactory) {
        this(nThreads, threadFactory, SingleThreadEventExecutor.DEFAULT_MAX_PENDING_EXECUTOR_TASKS, RejectedExecutionHandlers.reject());
    }
    
    public DefaultEventExecutorGroup(final int nThreads, final ThreadFactory threadFactory, final int maxPendingTasks, final RejectedExecutionHandler rejectedHandler) {
        super(nThreads, threadFactory, new Object[] { maxPendingTasks, rejectedHandler });
    }
    
    @Override
    protected EventExecutor newChild(final Executor executor, final Object... args) throws Exception {
        return new DefaultEventExecutor(this, executor, (int)args[0], (RejectedExecutionHandler)args[1]);
    }
}
