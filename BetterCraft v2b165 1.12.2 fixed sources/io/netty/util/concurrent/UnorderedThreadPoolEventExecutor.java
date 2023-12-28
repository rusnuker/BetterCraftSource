// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.concurrent.Delayed;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.Collections;
import java.util.concurrent.ThreadFactory;
import java.util.Set;
import io.netty.util.internal.logging.InternalLogger;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public final class UnorderedThreadPoolEventExecutor extends ScheduledThreadPoolExecutor implements EventExecutor
{
    private static final InternalLogger logger;
    private final Promise<?> terminationFuture;
    private final Set<EventExecutor> executorSet;
    
    public UnorderedThreadPoolEventExecutor(final int corePoolSize) {
        this(corePoolSize, new DefaultThreadFactory(UnorderedThreadPoolEventExecutor.class));
    }
    
    public UnorderedThreadPoolEventExecutor(final int corePoolSize, final ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
        this.terminationFuture = GlobalEventExecutor.INSTANCE.newPromise();
        this.executorSet = (Set<EventExecutor>)Collections.singleton(this);
    }
    
    public UnorderedThreadPoolEventExecutor(final int corePoolSize, final RejectedExecutionHandler handler) {
        this(corePoolSize, new DefaultThreadFactory(UnorderedThreadPoolEventExecutor.class), handler);
    }
    
    public UnorderedThreadPoolEventExecutor(final int corePoolSize, final ThreadFactory threadFactory, final RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
        this.terminationFuture = GlobalEventExecutor.INSTANCE.newPromise();
        this.executorSet = (Set<EventExecutor>)Collections.singleton(this);
    }
    
    @Override
    public EventExecutor next() {
        return this;
    }
    
    @Override
    public EventExecutorGroup parent() {
        return this;
    }
    
    @Override
    public boolean inEventLoop() {
        return false;
    }
    
    @Override
    public boolean inEventLoop(final Thread thread) {
        return false;
    }
    
    @Override
    public <V> Promise<V> newPromise() {
        return new DefaultPromise<V>(this);
    }
    
    @Override
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return new DefaultProgressivePromise<V>(this);
    }
    
    @Override
    public <V> Future<V> newSucceededFuture(final V result) {
        return new SucceededFuture<V>(this, result);
    }
    
    @Override
    public <V> Future<V> newFailedFuture(final Throwable cause) {
        return new FailedFuture<V>(this, cause);
    }
    
    @Override
    public boolean isShuttingDown() {
        return this.isShutdown();
    }
    
    @Override
    public List<Runnable> shutdownNow() {
        final List<Runnable> tasks = super.shutdownNow();
        this.terminationFuture.trySuccess(null);
        return tasks;
    }
    
    @Override
    public void shutdown() {
        super.shutdown();
        this.terminationFuture.trySuccess(null);
    }
    
    @Override
    public Future<?> shutdownGracefully() {
        return this.shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        this.shutdown();
        return this.terminationFuture();
    }
    
    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }
    
    @Override
    public Iterator<EventExecutor> iterator() {
        return this.executorSet.iterator();
    }
    
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(final Runnable runnable, final RunnableScheduledFuture<V> task) {
        return (runnable instanceof NonNotifyRunnable) ? task : new RunnableScheduledFutureTask<V>(this, runnable, task);
    }
    
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(final Callable<V> callable, final RunnableScheduledFuture<V> task) {
        return new RunnableScheduledFutureTask<V>(this, callable, task);
    }
    
    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return (ScheduledFuture)super.schedule(command, delay, unit);
    }
    
    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return (ScheduledFuture)super.schedule(callable, delay, unit);
    }
    
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return (ScheduledFuture)super.scheduleAtFixedRate(command, initialDelay, period, unit);
    }
    
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return (ScheduledFuture)super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
    
    @Override
    public Future<?> submit(final Runnable task) {
        return (Future)super.submit(task);
    }
    
    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        return (Future)super.submit(task, result);
    }
    
    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return (Future)super.submit(task);
    }
    
    @Override
    public void execute(final Runnable command) {
        super.schedule(new NonNotifyRunnable(command), 0L, TimeUnit.NANOSECONDS);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(UnorderedThreadPoolEventExecutor.class);
    }
    
    private static final class RunnableScheduledFutureTask<V> extends PromiseTask<V> implements RunnableScheduledFuture<V>, ScheduledFuture<V>
    {
        private final RunnableScheduledFuture<V> future;
        
        RunnableScheduledFutureTask(final EventExecutor executor, final Runnable runnable, final RunnableScheduledFuture<V> future) {
            super(executor, runnable, null);
            this.future = future;
        }
        
        RunnableScheduledFutureTask(final EventExecutor executor, final Callable<V> callable, final RunnableScheduledFuture<V> future) {
            super(executor, callable);
            this.future = future;
        }
        
        @Override
        public void run() {
            if (!this.isPeriodic()) {
                super.run();
            }
            else if (!this.isDone()) {
                try {
                    this.task.call();
                }
                catch (final Throwable cause) {
                    if (!this.tryFailureInternal(cause)) {
                        UnorderedThreadPoolEventExecutor.logger.warn("Failure during execution of task", cause);
                    }
                }
            }
        }
        
        @Override
        public boolean isPeriodic() {
            return this.future.isPeriodic();
        }
        
        @Override
        public long getDelay(final TimeUnit unit) {
            return this.future.getDelay(unit);
        }
        
        @Override
        public int compareTo(final Delayed o) {
            return this.future.compareTo(o);
        }
    }
    
    private static final class NonNotifyRunnable implements Runnable
    {
        private final Runnable task;
        
        NonNotifyRunnable(final Runnable task) {
            this.task = task;
        }
        
        @Override
        public void run() {
            this.task.run();
        }
    }
}
