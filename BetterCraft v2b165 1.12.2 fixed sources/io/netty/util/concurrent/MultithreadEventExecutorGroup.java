// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;

public abstract class MultithreadEventExecutorGroup extends AbstractEventExecutorGroup
{
    private final EventExecutor[] children;
    private final Set<EventExecutor> readonlyChildren;
    private final AtomicInteger terminatedChildren;
    private final Promise<?> terminationFuture;
    private final EventExecutorChooserFactory.EventExecutorChooser chooser;
    
    protected MultithreadEventExecutorGroup(final int nThreads, final ThreadFactory threadFactory, final Object... args) {
        this(nThreads, (threadFactory == null) ? null : new ThreadPerTaskExecutor(threadFactory), args);
    }
    
    protected MultithreadEventExecutorGroup(final int nThreads, final Executor executor, final Object... args) {
        this(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, args);
    }
    
    protected MultithreadEventExecutorGroup(final int nThreads, Executor executor, final EventExecutorChooserFactory chooserFactory, final Object... args) {
        this.terminatedChildren = new AtomicInteger();
        this.terminationFuture = new DefaultPromise<Object>(GlobalEventExecutor.INSTANCE);
        if (nThreads <= 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
        }
        if (executor == null) {
            executor = new ThreadPerTaskExecutor(this.newDefaultThreadFactory());
        }
        this.children = new EventExecutor[nThreads];
        for (int i = 0; i < nThreads; ++i) {
            boolean success = false;
            try {
                this.children[i] = this.newChild(executor, args);
                success = true;
            }
            catch (final Exception e) {
                throw new IllegalStateException("failed to create a child event loop", e);
            }
            finally {
                if (!success) {
                    for (int j = 0; j < i; ++j) {
                        this.children[j].shutdownGracefully();
                    }
                    for (int j = 0; j < i; ++j) {
                        final EventExecutor e2 = this.children[j];
                        try {
                            while (!e2.isTerminated()) {
                                e2.awaitTermination(2147483647L, TimeUnit.SECONDS);
                            }
                        }
                        catch (final InterruptedException interrupted) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
        }
        this.chooser = chooserFactory.newChooser(this.children);
        final FutureListener<Object> terminationListener = new FutureListener<Object>() {
            @Override
            public void operationComplete(final Future<Object> future) throws Exception {
                if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
                    MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null);
                }
            }
        };
        for (final EventExecutor e3 : this.children) {
            e3.terminationFuture().addListener(terminationListener);
        }
        final Set<EventExecutor> childrenSet = new LinkedHashSet<EventExecutor>(this.children.length);
        Collections.addAll(childrenSet, this.children);
        this.readonlyChildren = Collections.unmodifiableSet((Set<? extends EventExecutor>)childrenSet);
    }
    
    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(this.getClass());
    }
    
    @Override
    public EventExecutor next() {
        return this.chooser.next();
    }
    
    @Override
    public Iterator<EventExecutor> iterator() {
        return this.readonlyChildren.iterator();
    }
    
    public final int executorCount() {
        return this.children.length;
    }
    
    protected abstract EventExecutor newChild(final Executor p0, final Object... p1) throws Exception;
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        for (final EventExecutor l : this.children) {
            l.shutdownGracefully(quietPeriod, timeout, unit);
        }
        return this.terminationFuture();
    }
    
    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }
    
    @Deprecated
    @Override
    public void shutdown() {
        for (final EventExecutor l : this.children) {
            l.shutdown();
        }
    }
    
    @Override
    public boolean isShuttingDown() {
        for (final EventExecutor l : this.children) {
            if (!l.isShuttingDown()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isShutdown() {
        for (final EventExecutor l : this.children) {
            if (!l.isShutdown()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isTerminated() {
        for (final EventExecutor l : this.children) {
            if (!l.isTerminated()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        final long deadline = System.nanoTime() + unit.toNanos(timeout);
    Label_0084:
        for (final EventExecutor l : this.children) {
            long timeLeft;
            do {
                timeLeft = deadline - System.nanoTime();
                if (timeLeft <= 0L) {
                    break Label_0084;
                }
            } while (!l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS));
        }
        return this.isTerminated();
    }
}
