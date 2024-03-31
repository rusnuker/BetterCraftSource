// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.LockSupport;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.TimeUnit;

public final class RejectedExecutionHandlers
{
    private static final RejectedExecutionHandler REJECT;
    
    private RejectedExecutionHandlers() {
    }
    
    public static RejectedExecutionHandler reject() {
        return RejectedExecutionHandlers.REJECT;
    }
    
    public static RejectedExecutionHandler backoff(final int retries, final long backoffAmount, final TimeUnit unit) {
        ObjectUtil.checkPositive(retries, "retries");
        final long backOffNanos = unit.toNanos(backoffAmount);
        return new RejectedExecutionHandler() {
            @Override
            public void rejected(final Runnable task, final SingleThreadEventExecutor executor) {
                if (!executor.inEventLoop()) {
                    for (int i = 0; i < retries; ++i) {
                        executor.wakeup(false);
                        LockSupport.parkNanos(backOffNanos);
                        if (executor.offerTask(task)) {
                            return;
                        }
                    }
                }
                throw new RejectedExecutionException();
            }
        };
    }
    
    static {
        REJECT = new RejectedExecutionHandler() {
            @Override
            public void rejected(final Runnable task, final SingleThreadEventExecutor executor) {
                throw new RejectedExecutionException();
            }
        };
    }
}
