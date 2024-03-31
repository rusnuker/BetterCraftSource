// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.concurrent.RejectedExecutionException;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Queue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import io.netty.util.internal.ObjectUtil;

public final class NonStickyEventExecutorGroup implements EventExecutorGroup
{
    private final EventExecutorGroup group;
    private final int maxTaskExecutePerRun;
    
    public NonStickyEventExecutorGroup(final EventExecutorGroup group) {
        this(group, 1024);
    }
    
    public NonStickyEventExecutorGroup(final EventExecutorGroup group, final int maxTaskExecutePerRun) {
        this.group = verify(group);
        this.maxTaskExecutePerRun = ObjectUtil.checkPositive(maxTaskExecutePerRun, "maxTaskExecutePerRun");
    }
    
    private static EventExecutorGroup verify(final EventExecutorGroup group) {
        for (final EventExecutor executor : ObjectUtil.checkNotNull(group, "group")) {
            if (executor instanceof OrderedEventExecutor) {
                throw new IllegalArgumentException("EventExecutorGroup " + group + " contains OrderedEventExecutors: " + executor);
            }
        }
        return group;
    }
    
    private NonStickyOrderedEventExecutor newExecutor(final EventExecutor executor) {
        return new NonStickyOrderedEventExecutor(executor, this.maxTaskExecutePerRun);
    }
    
    @Override
    public boolean isShuttingDown() {
        return this.group.isShuttingDown();
    }
    
    @Override
    public Future<?> shutdownGracefully() {
        return this.group.shutdownGracefully();
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        return this.group.shutdownGracefully(quietPeriod, timeout, unit);
    }
    
    @Override
    public Future<?> terminationFuture() {
        return this.group.terminationFuture();
    }
    
    @Override
    public void shutdown() {
        this.group.shutdown();
    }
    
    @Override
    public List<Runnable> shutdownNow() {
        return this.group.shutdownNow();
    }
    
    @Override
    public EventExecutor next() {
        return this.newExecutor(this.group.next());
    }
    
    @Override
    public Iterator<EventExecutor> iterator() {
        final Iterator<EventExecutor> itr = this.group.iterator();
        return new Iterator<EventExecutor>() {
            @Override
            public boolean hasNext() {
                return itr.hasNext();
            }
            
            @Override
            public EventExecutor next() {
                return NonStickyEventExecutorGroup.this.newExecutor(itr.next());
            }
            
            @Override
            public void remove() {
                itr.remove();
            }
        };
    }
    
    @Override
    public Future<?> submit(final Runnable task) {
        return this.group.submit(task);
    }
    
    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        return this.group.submit(task, result);
    }
    
    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return this.group.submit(task);
    }
    
    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return this.group.schedule(command, delay, unit);
    }
    
    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return this.group.schedule(callable, delay, unit);
    }
    
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return this.group.scheduleAtFixedRate(command, initialDelay, period, unit);
    }
    
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return this.group.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
    
    @Override
    public boolean isShutdown() {
        return this.group.isShutdown();
    }
    
    @Override
    public boolean isTerminated() {
        return this.group.isTerminated();
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.group.awaitTermination(timeout, unit);
    }
    
    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.group.invokeAll(tasks);
    }
    
    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.group.invokeAll(tasks, timeout, unit);
    }
    
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.group.invokeAny(tasks);
    }
    
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.group.invokeAny(tasks, timeout, unit);
    }
    
    @Override
    public void execute(final Runnable command) {
        this.group.execute(command);
    }
    
    private static final class NonStickyOrderedEventExecutor extends AbstractEventExecutor implements Runnable, OrderedEventExecutor
    {
        private final EventExecutor executor;
        private final Queue<Runnable> tasks;
        private static final int NONE = 0;
        private static final int SUBMITTED = 1;
        private static final int RUNNING = 2;
        private final AtomicInteger state;
        private final int maxTaskExecutePerRun;
        
        NonStickyOrderedEventExecutor(final EventExecutor executor, final int maxTaskExecutePerRun) {
            super(executor);
            this.tasks = PlatformDependent.newMpscQueue();
            this.state = new AtomicInteger();
            this.executor = executor;
            this.maxTaskExecutePerRun = maxTaskExecutePerRun;
        }
        
        @Override
        public void run() {
            if (!this.state.compareAndSet(1, 2)) {
                return;
            }
            while (true) {
                int i = 0;
                try {
                    while (i < this.maxTaskExecutePerRun) {
                        final Runnable task = this.tasks.poll();
                        if (task == null) {
                            break;
                        }
                        AbstractEventExecutor.safeExecute(task);
                        ++i;
                    }
                    if (i == this.maxTaskExecutePerRun) {
                        try {
                            this.state.set(1);
                            this.executor.execute(this);
                            return;
                        }
                        catch (final Throwable ignore) {
                            this.state.set(2);
                            continue;
                        }
                    }
                    this.state.set(0);
                }
                finally {
                    if (i == this.maxTaskExecutePerRun) {
                        try {
                            this.state.set(1);
                            this.executor.execute(this);
                            return;
                        }
                        catch (final Throwable ignore2) {
                            this.state.set(2);
                        }
                    }
                    this.state.set(0);
                }
            }
        }
        
        @Override
        public boolean inEventLoop(final Thread thread) {
            return false;
        }
        
        @Override
        public boolean inEventLoop() {
            return false;
        }
        
        @Override
        public boolean isShuttingDown() {
            return this.executor.isShutdown();
        }
        
        @Override
        public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
            return this.executor.shutdownGracefully(quietPeriod, timeout, unit);
        }
        
        @Override
        public Future<?> terminationFuture() {
            return this.executor.terminationFuture();
        }
        
        @Override
        public void shutdown() {
            this.executor.shutdown();
        }
        
        @Override
        public boolean isShutdown() {
            return this.executor.isShutdown();
        }
        
        @Override
        public boolean isTerminated() {
            return this.executor.isTerminated();
        }
        
        @Override
        public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            return this.executor.awaitTermination(timeout, unit);
        }
        
        @Override
        public void execute(final Runnable command) {
            if (!this.tasks.offer(command)) {
                throw new RejectedExecutionException();
            }
            if (this.state.compareAndSet(0, 1)) {
                try {
                    this.executor.execute(this);
                }
                catch (final Throwable e) {
                    this.tasks.remove(command);
                    PlatformDependent.throwException(e);
                }
            }
        }
    }
}
