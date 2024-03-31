// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.ArrayDeque;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.Queue;
import io.netty.util.internal.logging.InternalLogger;

public final class ImmediateEventExecutor extends AbstractEventExecutor
{
    private static final InternalLogger logger;
    public static final ImmediateEventExecutor INSTANCE;
    private static final FastThreadLocal<Queue<Runnable>> DELAYED_RUNNABLES;
    private static final FastThreadLocal<Boolean> RUNNING;
    private final Future<?> terminationFuture;
    
    private ImmediateEventExecutor() {
        this.terminationFuture = new FailedFuture<Object>(GlobalEventExecutor.INSTANCE, new UnsupportedOperationException());
    }
    
    @Override
    public boolean inEventLoop() {
        return true;
    }
    
    @Override
    public boolean inEventLoop(final Thread thread) {
        return true;
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        return this.terminationFuture();
    }
    
    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }
    
    @Deprecated
    @Override
    public void shutdown() {
    }
    
    @Override
    public boolean isShuttingDown() {
        return false;
    }
    
    @Override
    public boolean isShutdown() {
        return false;
    }
    
    @Override
    public boolean isTerminated() {
        return false;
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) {
        return false;
    }
    
    @Override
    public void execute(final Runnable command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        if (!ImmediateEventExecutor.RUNNING.get()) {
            ImmediateEventExecutor.RUNNING.set(true);
            try {
                command.run();
            }
            catch (final Throwable cause) {
                ImmediateEventExecutor.logger.info("Throwable caught while executing Runnable {}", command, cause);
            }
            finally {
                final Queue<Runnable> delayedRunnables = ImmediateEventExecutor.DELAYED_RUNNABLES.get();
                Runnable runnable;
                while ((runnable = delayedRunnables.poll()) != null) {
                    try {
                        runnable.run();
                    }
                    catch (final Throwable cause2) {
                        ImmediateEventExecutor.logger.info("Throwable caught while executing Runnable {}", runnable, cause2);
                    }
                }
                ImmediateEventExecutor.RUNNING.set(false);
            }
        }
        else {
            ImmediateEventExecutor.DELAYED_RUNNABLES.get().add(command);
        }
    }
    
    @Override
    public <V> Promise<V> newPromise() {
        return new ImmediatePromise<V>(this);
    }
    
    @Override
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return new ImmediateProgressivePromise<V>(this);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ImmediateEventExecutor.class);
        INSTANCE = new ImmediateEventExecutor();
        DELAYED_RUNNABLES = new FastThreadLocal<Queue<Runnable>>() {
            @Override
            protected Queue<Runnable> initialValue() throws Exception {
                return new ArrayDeque<Runnable>();
            }
        };
        RUNNING = new FastThreadLocal<Boolean>() {
            @Override
            protected Boolean initialValue() throws Exception {
                return false;
            }
        };
    }
    
    static class ImmediatePromise<V> extends DefaultPromise<V>
    {
        ImmediatePromise(final EventExecutor executor) {
            super(executor);
        }
        
        @Override
        protected void checkDeadLock() {
        }
    }
    
    static class ImmediateProgressivePromise<V> extends DefaultProgressivePromise<V>
    {
        ImmediateProgressivePromise(final EventExecutor executor) {
            super(executor);
        }
        
        @Override
        protected void checkDeadLock() {
        }
    }
}
