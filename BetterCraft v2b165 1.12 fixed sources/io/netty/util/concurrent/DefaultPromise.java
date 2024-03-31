// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.CancellationException;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.StringUtil;
import java.util.concurrent.TimeUnit;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.Signal;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;

public class DefaultPromise<V> extends AbstractFuture<V> implements Promise<V>
{
    private static final InternalLogger logger;
    private static final InternalLogger rejectedExecutionLogger;
    private static final int MAX_LISTENER_STACK_DEPTH;
    private static final AtomicReferenceFieldUpdater<DefaultPromise, Object> RESULT_UPDATER;
    private static final Signal SUCCESS;
    private static final Signal UNCANCELLABLE;
    private static final CauseHolder CANCELLATION_CAUSE_HOLDER;
    private volatile Object result;
    private final EventExecutor executor;
    private Object listeners;
    private short waiters;
    private boolean notifyingListeners;
    
    public DefaultPromise(final EventExecutor executor) {
        this.executor = ObjectUtil.checkNotNull(executor, "executor");
    }
    
    protected DefaultPromise() {
        this.executor = null;
    }
    
    @Override
    public Promise<V> setSuccess(final V result) {
        if (this.setSuccess0(result)) {
            this.notifyListeners();
            return this;
        }
        throw new IllegalStateException("complete already: " + this);
    }
    
    @Override
    public boolean trySuccess(final V result) {
        if (this.setSuccess0(result)) {
            this.notifyListeners();
            return true;
        }
        return false;
    }
    
    @Override
    public Promise<V> setFailure(final Throwable cause) {
        if (this.setFailure0(cause)) {
            this.notifyListeners();
            return this;
        }
        throw new IllegalStateException("complete already: " + this, cause);
    }
    
    @Override
    public boolean tryFailure(final Throwable cause) {
        if (this.setFailure0(cause)) {
            this.notifyListeners();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean setUncancellable() {
        if (DefaultPromise.RESULT_UPDATER.compareAndSet(this, null, DefaultPromise.UNCANCELLABLE)) {
            return true;
        }
        final Object result = this.result;
        return !isDone0(result) || !isCancelled0(result);
    }
    
    @Override
    public boolean isSuccess() {
        final Object result = this.result;
        return result != null && result != DefaultPromise.UNCANCELLABLE && !(result instanceof CauseHolder);
    }
    
    @Override
    public boolean isCancellable() {
        return this.result == null;
    }
    
    @Override
    public Throwable cause() {
        final Object result = this.result;
        return (result instanceof CauseHolder) ? ((CauseHolder)result).cause : null;
    }
    
    @Override
    public Promise<V> addListener(final GenericFutureListener<? extends Future<? super V>> listener) {
        ObjectUtil.checkNotNull(listener, "listener");
        synchronized (this) {
            this.addListener0(listener);
        }
        if (this.isDone()) {
            this.notifyListeners();
        }
        return this;
    }
    
    @Override
    public Promise<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... listeners) {
        ObjectUtil.checkNotNull(listeners, "listeners");
        synchronized (this) {
            for (final GenericFutureListener<? extends Future<? super V>> listener : listeners) {
                if (listener == null) {
                    break;
                }
                this.addListener0(listener);
            }
        }
        if (this.isDone()) {
            this.notifyListeners();
        }
        return this;
    }
    
    @Override
    public Promise<V> removeListener(final GenericFutureListener<? extends Future<? super V>> listener) {
        ObjectUtil.checkNotNull(listener, "listener");
        synchronized (this) {
            this.removeListener0(listener);
        }
        return this;
    }
    
    @Override
    public Promise<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... listeners) {
        ObjectUtil.checkNotNull(listeners, "listeners");
        synchronized (this) {
            for (final GenericFutureListener<? extends Future<? super V>> listener : listeners) {
                if (listener == null) {
                    break;
                }
                this.removeListener0(listener);
            }
        }
        return this;
    }
    
    @Override
    public Promise<V> await() throws InterruptedException {
        if (this.isDone()) {
            return this;
        }
        if (Thread.interrupted()) {
            throw new InterruptedException(this.toString());
        }
        this.checkDeadLock();
        synchronized (this) {
            while (!this.isDone()) {
                this.incWaiters();
                try {
                    this.wait();
                }
                finally {
                    this.decWaiters();
                }
            }
        }
        return this;
    }
    
    @Override
    public Promise<V> awaitUninterruptibly() {
        if (this.isDone()) {
            return this;
        }
        this.checkDeadLock();
        boolean interrupted = false;
        synchronized (this) {
            while (!this.isDone()) {
                this.incWaiters();
                try {
                    this.wait();
                }
                catch (final InterruptedException e) {
                    interrupted = true;
                }
                finally {
                    this.decWaiters();
                }
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return this;
    }
    
    @Override
    public boolean await(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.await0(unit.toNanos(timeout), true);
    }
    
    @Override
    public boolean await(final long timeoutMillis) throws InterruptedException {
        return this.await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
    }
    
    @Override
    public boolean awaitUninterruptibly(final long timeout, final TimeUnit unit) {
        try {
            return this.await0(unit.toNanos(timeout), false);
        }
        catch (final InterruptedException e) {
            throw new InternalError();
        }
    }
    
    @Override
    public boolean awaitUninterruptibly(final long timeoutMillis) {
        try {
            return this.await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
        }
        catch (final InterruptedException e) {
            throw new InternalError();
        }
    }
    
    @Override
    public V getNow() {
        final Object result = this.result;
        if (result instanceof CauseHolder || result == DefaultPromise.SUCCESS) {
            return null;
        }
        return (V)result;
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        if (DefaultPromise.RESULT_UPDATER.compareAndSet(this, null, DefaultPromise.CANCELLATION_CAUSE_HOLDER)) {
            this.checkNotifyWaiters();
            this.notifyListeners();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isCancelled() {
        return isCancelled0(this.result);
    }
    
    @Override
    public boolean isDone() {
        return isDone0(this.result);
    }
    
    @Override
    public Promise<V> sync() throws InterruptedException {
        this.await();
        this.rethrowIfFailed();
        return this;
    }
    
    @Override
    public Promise<V> syncUninterruptibly() {
        this.awaitUninterruptibly();
        this.rethrowIfFailed();
        return this;
    }
    
    @Override
    public String toString() {
        return this.toStringBuilder().toString();
    }
    
    protected StringBuilder toStringBuilder() {
        final StringBuilder buf = new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('@').append(Integer.toHexString(this.hashCode()));
        final Object result = this.result;
        if (result == DefaultPromise.SUCCESS) {
            buf.append("(success)");
        }
        else if (result == DefaultPromise.UNCANCELLABLE) {
            buf.append("(uncancellable)");
        }
        else if (result instanceof CauseHolder) {
            buf.append("(failure: ").append(((CauseHolder)result).cause).append(')');
        }
        else if (result != null) {
            buf.append("(success: ").append(result).append(')');
        }
        else {
            buf.append("(incomplete)");
        }
        return buf;
    }
    
    protected EventExecutor executor() {
        return this.executor;
    }
    
    protected void checkDeadLock() {
        final EventExecutor e = this.executor();
        if (e != null && e.inEventLoop()) {
            throw new BlockingOperationException(this.toString());
        }
    }
    
    protected static void notifyListener(final EventExecutor eventExecutor, final Future<?> future, final GenericFutureListener<?> listener) {
        ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
        ObjectUtil.checkNotNull(future, "future");
        ObjectUtil.checkNotNull(listener, "listener");
        notifyListenerWithStackOverFlowProtection(eventExecutor, future, listener);
    }
    
    private void notifyListeners() {
        final EventExecutor executor = this.executor();
        if (executor.inEventLoop()) {
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final int stackDepth = threadLocals.futureListenerStackDepth();
            if (stackDepth < DefaultPromise.MAX_LISTENER_STACK_DEPTH) {
                threadLocals.setFutureListenerStackDepth(stackDepth + 1);
                try {
                    this.notifyListenersNow();
                }
                finally {
                    threadLocals.setFutureListenerStackDepth(stackDepth);
                }
                return;
            }
        }
        safeExecute(executor, new Runnable() {
            @Override
            public void run() {
                DefaultPromise.this.notifyListenersNow();
            }
        });
    }
    
    private static void notifyListenerWithStackOverFlowProtection(final EventExecutor executor, final Future<?> future, final GenericFutureListener<?> listener) {
        if (executor.inEventLoop()) {
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final int stackDepth = threadLocals.futureListenerStackDepth();
            if (stackDepth < DefaultPromise.MAX_LISTENER_STACK_DEPTH) {
                threadLocals.setFutureListenerStackDepth(stackDepth + 1);
                try {
                    notifyListener0(future, listener);
                }
                finally {
                    threadLocals.setFutureListenerStackDepth(stackDepth);
                }
                return;
            }
        }
        safeExecute(executor, new Runnable() {
            @Override
            public void run() {
                notifyListener0(future, listener);
            }
        });
    }
    
    private void notifyListenersNow() {
        Object listeners;
        synchronized (this) {
            if (this.notifyingListeners || this.listeners == null) {
                return;
            }
            this.notifyingListeners = true;
            listeners = this.listeners;
            this.listeners = null;
        }
        while (true) {
            if (listeners instanceof DefaultFutureListeners) {
                this.notifyListeners0((DefaultFutureListeners)listeners);
            }
            else {
                notifyListener0(this, (GenericFutureListener)listeners);
            }
            synchronized (this) {
                if (this.listeners == null) {
                    this.notifyingListeners = false;
                    return;
                }
                listeners = this.listeners;
                this.listeners = null;
            }
        }
    }
    
    private void notifyListeners0(final DefaultFutureListeners listeners) {
        final GenericFutureListener<?>[] a = listeners.listeners();
        for (int size = listeners.size(), i = 0; i < size; ++i) {
            notifyListener0(this, a[i]);
        }
    }
    
    private static void notifyListener0(final Future future, final GenericFutureListener l) {
        try {
            l.operationComplete(future);
        }
        catch (final Throwable t) {
            DefaultPromise.logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationComplete()", t);
        }
    }
    
    private void addListener0(final GenericFutureListener<? extends Future<? super V>> listener) {
        if (this.listeners == null) {
            this.listeners = listener;
        }
        else if (this.listeners instanceof DefaultFutureListeners) {
            ((DefaultFutureListeners)this.listeners).add(listener);
        }
        else {
            this.listeners = new DefaultFutureListeners((GenericFutureListener<? extends Future<?>>)this.listeners, listener);
        }
    }
    
    private void removeListener0(final GenericFutureListener<? extends Future<? super V>> listener) {
        if (this.listeners instanceof DefaultFutureListeners) {
            ((DefaultFutureListeners)this.listeners).remove(listener);
        }
        else if (this.listeners == listener) {
            this.listeners = null;
        }
    }
    
    private boolean setSuccess0(final V result) {
        return this.setValue0((result == null) ? DefaultPromise.SUCCESS : result);
    }
    
    private boolean setFailure0(final Throwable cause) {
        return this.setValue0(new CauseHolder(ObjectUtil.checkNotNull(cause, "cause")));
    }
    
    private boolean setValue0(final Object objResult) {
        if (DefaultPromise.RESULT_UPDATER.compareAndSet(this, null, objResult) || DefaultPromise.RESULT_UPDATER.compareAndSet(this, DefaultPromise.UNCANCELLABLE, objResult)) {
            this.checkNotifyWaiters();
            return true;
        }
        return false;
    }
    
    private synchronized void checkNotifyWaiters() {
        if (this.waiters > 0) {
            this.notifyAll();
        }
    }
    
    private void incWaiters() {
        if (this.waiters == 32767) {
            throw new IllegalStateException("too many waiters: " + this);
        }
        ++this.waiters;
    }
    
    private void decWaiters() {
        --this.waiters;
    }
    
    private void rethrowIfFailed() {
        final Throwable cause = this.cause();
        if (cause == null) {
            return;
        }
        PlatformDependent.throwException(cause);
    }
    
    private boolean await0(final long timeoutNanos, final boolean interruptable) throws InterruptedException {
        if (this.isDone()) {
            return true;
        }
        if (timeoutNanos <= 0L) {
            return this.isDone();
        }
        if (interruptable && Thread.interrupted()) {
            throw new InterruptedException(this.toString());
        }
        this.checkDeadLock();
        final long startTime = System.nanoTime();
        long waitTime = timeoutNanos;
        boolean interrupted = false;
        try {
            do {
                synchronized (this) {
                    if (this.isDone()) {
                        return true;
                    }
                    this.incWaiters();
                    try {
                        this.wait(waitTime / 1000000L, (int)(waitTime % 1000000L));
                    }
                    catch (final InterruptedException e) {
                        if (interruptable) {
                            throw e;
                        }
                        interrupted = true;
                    }
                    finally {
                        this.decWaiters();
                    }
                }
                if (this.isDone()) {
                    return true;
                }
                waitTime = timeoutNanos - (System.nanoTime() - startTime);
            } while (waitTime > 0L);
            return this.isDone();
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    void notifyProgressiveListeners(final long progress, final long total) {
        final Object listeners = this.progressiveListeners();
        if (listeners == null) {
            return;
        }
        final ProgressiveFuture<V> self = (ProgressiveFuture<V>)this;
        final EventExecutor executor = this.executor();
        if (executor.inEventLoop()) {
            if (listeners instanceof GenericProgressiveFutureListener[]) {
                notifyProgressiveListeners0(self, (GenericProgressiveFutureListener<?>[])listeners, progress, total);
            }
            else {
                notifyProgressiveListener0(self, (GenericProgressiveFutureListener)listeners, progress, total);
            }
        }
        else if (listeners instanceof GenericProgressiveFutureListener[]) {
            final GenericProgressiveFutureListener<?>[] array = (GenericProgressiveFutureListener<?>[])listeners;
            safeExecute(executor, new Runnable() {
                @Override
                public void run() {
                    notifyProgressiveListeners0(self, array, progress, total);
                }
            });
        }
        else {
            final GenericProgressiveFutureListener<ProgressiveFuture<V>> l = (GenericProgressiveFutureListener<ProgressiveFuture<V>>)listeners;
            safeExecute(executor, new Runnable() {
                @Override
                public void run() {
                    notifyProgressiveListener0(self, l, progress, total);
                }
            });
        }
    }
    
    private synchronized Object progressiveListeners() {
        final Object listeners = this.listeners;
        if (listeners == null) {
            return null;
        }
        if (listeners instanceof DefaultFutureListeners) {
            final DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
            final int progressiveSize = dfl.progressiveSize();
            switch (progressiveSize) {
                case 0: {
                    return null;
                }
                case 1: {
                    for (final GenericFutureListener<?> l : dfl.listeners()) {
                        if (l instanceof GenericProgressiveFutureListener) {
                            return l;
                        }
                    }
                    return null;
                }
                default: {
                    final GenericFutureListener<?>[] array = dfl.listeners();
                    final GenericProgressiveFutureListener<?>[] copy = new GenericProgressiveFutureListener[progressiveSize];
                    int i = 0;
                    int j = 0;
                    while (j < progressiveSize) {
                        final GenericFutureListener<?> k = array[i];
                        if (k instanceof GenericProgressiveFutureListener) {
                            copy[j++] = (GenericProgressiveFutureListener)k;
                        }
                        ++i;
                    }
                    return copy;
                }
            }
        }
        else {
            if (listeners instanceof GenericProgressiveFutureListener) {
                return listeners;
            }
            return null;
        }
    }
    
    private static void notifyProgressiveListeners0(final ProgressiveFuture<?> future, final GenericProgressiveFutureListener<?>[] listeners, final long progress, final long total) {
        for (final GenericProgressiveFutureListener<?> l : listeners) {
            if (l == null) {
                break;
            }
            notifyProgressiveListener0(future, l, progress, total);
        }
    }
    
    private static void notifyProgressiveListener0(final ProgressiveFuture future, final GenericProgressiveFutureListener l, final long progress, final long total) {
        try {
            l.operationProgressed(future, progress, total);
        }
        catch (final Throwable t) {
            DefaultPromise.logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationProgressed()", t);
        }
    }
    
    private static boolean isCancelled0(final Object result) {
        return result instanceof CauseHolder && ((CauseHolder)result).cause instanceof CancellationException;
    }
    
    private static boolean isDone0(final Object result) {
        return result != null && result != DefaultPromise.UNCANCELLABLE;
    }
    
    private static void safeExecute(final EventExecutor executor, final Runnable task) {
        try {
            executor.execute(task);
        }
        catch (final Throwable t) {
            DefaultPromise.rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", t);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
        rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
        MAX_LISTENER_STACK_DEPTH = Math.min(8, SystemPropertyUtil.getInt("io.netty.defaultPromise.maxListenerStackDepth", 8));
        RESULT_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultPromise.class, Object.class, "result");
        SUCCESS = Signal.valueOf(DefaultPromise.class, "SUCCESS");
        UNCANCELLABLE = Signal.valueOf(DefaultPromise.class, "UNCANCELLABLE");
        CANCELLATION_CAUSE_HOLDER = new CauseHolder(ThrowableUtil.unknownStackTrace(new CancellationException(), DefaultPromise.class, "cancel(...)"));
    }
    
    private static final class CauseHolder
    {
        final Throwable cause;
        
        CauseHolder(final Throwable cause) {
            this.cause = cause;
        }
    }
}
