// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.SystemPropertyUtil;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import io.netty.util.internal.ObjectUtil;
import java.util.LinkedHashSet;
import java.util.concurrent.ThreadFactory;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.Executor;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;

public abstract class SingleThreadEventExecutor extends AbstractScheduledEventExecutor implements OrderedEventExecutor
{
    static final int DEFAULT_MAX_PENDING_EXECUTOR_TASKS;
    private static final InternalLogger logger;
    private static final int ST_NOT_STARTED = 1;
    private static final int ST_STARTED = 2;
    private static final int ST_SHUTTING_DOWN = 3;
    private static final int ST_SHUTDOWN = 4;
    private static final int ST_TERMINATED = 5;
    private static final Runnable WAKEUP_TASK;
    private static final Runnable NOOP_TASK;
    private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER;
    private static final AtomicReferenceFieldUpdater<SingleThreadEventExecutor, ThreadProperties> PROPERTIES_UPDATER;
    private final Queue<Runnable> taskQueue;
    private volatile Thread thread;
    private volatile ThreadProperties threadProperties;
    private final Executor executor;
    private volatile boolean interrupted;
    private final Semaphore threadLock;
    private final Set<Runnable> shutdownHooks;
    private final boolean addTaskWakesUp;
    private final int maxPendingTasks;
    private final RejectedExecutionHandler rejectedExecutionHandler;
    private long lastExecutionTime;
    private volatile int state;
    private volatile long gracefulShutdownQuietPeriod;
    private volatile long gracefulShutdownTimeout;
    private long gracefulShutdownStartTime;
    private final Promise<?> terminationFuture;
    private static final long SCHEDULE_PURGE_INTERVAL;
    
    protected SingleThreadEventExecutor(final EventExecutorGroup parent, final ThreadFactory threadFactory, final boolean addTaskWakesUp) {
        this(parent, new ThreadPerTaskExecutor(threadFactory), addTaskWakesUp);
    }
    
    protected SingleThreadEventExecutor(final EventExecutorGroup parent, final ThreadFactory threadFactory, final boolean addTaskWakesUp, final int maxPendingTasks, final RejectedExecutionHandler rejectedHandler) {
        this(parent, new ThreadPerTaskExecutor(threadFactory), addTaskWakesUp, maxPendingTasks, rejectedHandler);
    }
    
    protected SingleThreadEventExecutor(final EventExecutorGroup parent, final Executor executor, final boolean addTaskWakesUp) {
        this(parent, executor, addTaskWakesUp, SingleThreadEventExecutor.DEFAULT_MAX_PENDING_EXECUTOR_TASKS, RejectedExecutionHandlers.reject());
    }
    
    protected SingleThreadEventExecutor(final EventExecutorGroup parent, final Executor executor, final boolean addTaskWakesUp, final int maxPendingTasks, final RejectedExecutionHandler rejectedHandler) {
        super(parent);
        this.threadLock = new Semaphore(0);
        this.shutdownHooks = new LinkedHashSet<Runnable>();
        this.state = 1;
        this.terminationFuture = new DefaultPromise<Object>(GlobalEventExecutor.INSTANCE);
        this.addTaskWakesUp = addTaskWakesUp;
        this.maxPendingTasks = Math.max(16, maxPendingTasks);
        this.executor = ObjectUtil.checkNotNull(executor, "executor");
        this.taskQueue = this.newTaskQueue(this.maxPendingTasks);
        this.rejectedExecutionHandler = ObjectUtil.checkNotNull(rejectedHandler, "rejectedHandler");
    }
    
    @Deprecated
    protected Queue<Runnable> newTaskQueue() {
        return this.newTaskQueue(this.maxPendingTasks);
    }
    
    protected Queue<Runnable> newTaskQueue(final int maxPendingTasks) {
        return new LinkedBlockingQueue<Runnable>(maxPendingTasks);
    }
    
    protected void interruptThread() {
        final Thread currentThread = this.thread;
        if (currentThread == null) {
            this.interrupted = true;
        }
        else {
            currentThread.interrupt();
        }
    }
    
    protected Runnable pollTask() {
        assert this.inEventLoop();
        return pollTaskFrom(this.taskQueue);
    }
    
    protected static Runnable pollTaskFrom(final Queue<Runnable> taskQueue) {
        Runnable task;
        do {
            task = taskQueue.poll();
        } while (task == SingleThreadEventExecutor.WAKEUP_TASK);
        return task;
    }
    
    protected Runnable takeTask() {
        assert this.inEventLoop();
        if (!(this.taskQueue instanceof BlockingQueue)) {
            throw new UnsupportedOperationException();
        }
        final BlockingQueue<Runnable> taskQueue = (BlockingQueue)this.taskQueue;
        while (true) {
            final ScheduledFutureTask<?> scheduledTask = this.peekScheduledTask();
            if (scheduledTask == null) {
                Runnable task = null;
                try {
                    task = taskQueue.take();
                    if (task == SingleThreadEventExecutor.WAKEUP_TASK) {
                        task = null;
                    }
                }
                catch (final InterruptedException ex) {}
                return task;
            }
            final long delayNanos = scheduledTask.delayNanos();
            Runnable task2 = null;
            if (delayNanos > 0L) {
                try {
                    task2 = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                }
                catch (final InterruptedException e) {
                    return null;
                }
            }
            if (task2 == null) {
                this.fetchFromScheduledTaskQueue();
                task2 = taskQueue.poll();
            }
            if (task2 != null) {
                return task2;
            }
        }
    }
    
    private boolean fetchFromScheduledTaskQueue() {
        final long nanoTime = AbstractScheduledEventExecutor.nanoTime();
        for (Runnable scheduledTask = this.pollScheduledTask(nanoTime); scheduledTask != null; scheduledTask = this.pollScheduledTask(nanoTime)) {
            if (!this.taskQueue.offer(scheduledTask)) {
                this.scheduledTaskQueue().add((ScheduledFutureTask)scheduledTask);
                return false;
            }
        }
        return true;
    }
    
    protected Runnable peekTask() {
        assert this.inEventLoop();
        return this.taskQueue.peek();
    }
    
    protected boolean hasTasks() {
        assert this.inEventLoop();
        return !this.taskQueue.isEmpty();
    }
    
    public int pendingTasks() {
        return this.taskQueue.size();
    }
    
    protected void addTask(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (!this.offerTask(task)) {
            this.reject(task);
        }
    }
    
    final boolean offerTask(final Runnable task) {
        if (this.isShutdown()) {
            reject();
        }
        return this.taskQueue.offer(task);
    }
    
    protected boolean removeTask(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        return this.taskQueue.remove(task);
    }
    
    protected boolean runAllTasks() {
        assert this.inEventLoop();
        boolean ranAtLeastOne = false;
        boolean fetchedAll;
        do {
            fetchedAll = this.fetchFromScheduledTaskQueue();
            if (this.runAllTasksFrom(this.taskQueue)) {
                ranAtLeastOne = true;
            }
        } while (!fetchedAll);
        if (ranAtLeastOne) {
            this.lastExecutionTime = ScheduledFutureTask.nanoTime();
        }
        this.afterRunningAllTasks();
        return ranAtLeastOne;
    }
    
    protected final boolean runAllTasksFrom(final Queue<Runnable> taskQueue) {
        Runnable task = pollTaskFrom(taskQueue);
        if (task == null) {
            return false;
        }
        do {
            AbstractEventExecutor.safeExecute(task);
            task = pollTaskFrom(taskQueue);
        } while (task != null);
        return true;
    }
    
    protected boolean runAllTasks(final long timeoutNanos) {
        this.fetchFromScheduledTaskQueue();
        Runnable task = this.pollTask();
        if (task == null) {
            this.afterRunningAllTasks();
            return false;
        }
        final long deadline = ScheduledFutureTask.nanoTime() + timeoutNanos;
        long runTasks = 0L;
        while (true) {
            do {
                AbstractEventExecutor.safeExecute(task);
                ++runTasks;
                if ((runTasks & 0x3FL) == 0x0L) {
                    final long lastExecutionTime = ScheduledFutureTask.nanoTime();
                    if (lastExecutionTime >= deadline) {
                        this.afterRunningAllTasks();
                        this.lastExecutionTime = lastExecutionTime;
                        return true;
                    }
                }
                task = this.pollTask();
            } while (task != null);
            final long lastExecutionTime = ScheduledFutureTask.nanoTime();
            continue;
        }
    }
    
    protected void afterRunningAllTasks() {
    }
    
    protected long delayNanos(final long currentTimeNanos) {
        final ScheduledFutureTask<?> scheduledTask = this.peekScheduledTask();
        if (scheduledTask == null) {
            return SingleThreadEventExecutor.SCHEDULE_PURGE_INTERVAL;
        }
        return scheduledTask.delayNanos(currentTimeNanos);
    }
    
    protected void updateLastExecutionTime() {
        this.lastExecutionTime = ScheduledFutureTask.nanoTime();
    }
    
    protected abstract void run();
    
    protected void cleanup() {
    }
    
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop || SingleThreadEventExecutor.STATE_UPDATER.get(this) == 3) {
            this.taskQueue.offer(SingleThreadEventExecutor.WAKEUP_TASK);
        }
    }
    
    @Override
    public boolean inEventLoop(final Thread thread) {
        return thread == this.thread;
    }
    
    public void addShutdownHook(final Runnable task) {
        if (this.inEventLoop()) {
            this.shutdownHooks.add(task);
        }
        else {
            this.execute(new Runnable() {
                @Override
                public void run() {
                    SingleThreadEventExecutor.this.shutdownHooks.add(task);
                }
            });
        }
    }
    
    public void removeShutdownHook(final Runnable task) {
        if (this.inEventLoop()) {
            this.shutdownHooks.remove(task);
        }
        else {
            this.execute(new Runnable() {
                @Override
                public void run() {
                    SingleThreadEventExecutor.this.shutdownHooks.remove(task);
                }
            });
        }
    }
    
    private boolean runShutdownHooks() {
        boolean ran = false;
        while (!this.shutdownHooks.isEmpty()) {
            final List<Runnable> copy = new ArrayList<Runnable>(this.shutdownHooks);
            this.shutdownHooks.clear();
            for (final Runnable task : copy) {
                try {
                    task.run();
                }
                catch (final Throwable t) {
                    SingleThreadEventExecutor.logger.warn("Shutdown hook raised an exception.", t);
                }
                finally {
                    ran = true;
                }
            }
        }
        if (ran) {
            this.lastExecutionTime = ScheduledFutureTask.nanoTime();
        }
        return ran;
    }
    
    @Override
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        if (quietPeriod < 0L) {
            throw new IllegalArgumentException("quietPeriod: " + quietPeriod + " (expected >= 0)");
        }
        if (timeout < quietPeriod) {
            throw new IllegalArgumentException("timeout: " + timeout + " (expected >= quietPeriod (" + quietPeriod + "))");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (this.isShuttingDown()) {
            return this.terminationFuture();
        }
        final boolean inEventLoop = this.inEventLoop();
        while (!this.isShuttingDown()) {
            boolean wakeup = true;
            final int oldState = SingleThreadEventExecutor.STATE_UPDATER.get(this);
            int newState = 0;
            if (inEventLoop) {
                newState = 3;
            }
            else {
                switch (oldState) {
                    case 1:
                    case 2: {
                        newState = 3;
                        break;
                    }
                    default: {
                        newState = oldState;
                        wakeup = false;
                        break;
                    }
                }
            }
            if (SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(this, oldState, newState)) {
                this.gracefulShutdownQuietPeriod = unit.toNanos(quietPeriod);
                this.gracefulShutdownTimeout = unit.toNanos(timeout);
                if (oldState == 1) {
                    this.doStartThread();
                }
                if (wakeup) {
                    this.wakeup(inEventLoop);
                }
                return this.terminationFuture();
            }
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
        if (this.isShutdown()) {
            return;
        }
        final boolean inEventLoop = this.inEventLoop();
        while (!this.isShuttingDown()) {
            boolean wakeup = true;
            final int oldState = SingleThreadEventExecutor.STATE_UPDATER.get(this);
            int newState = 0;
            if (inEventLoop) {
                newState = 4;
            }
            else {
                switch (oldState) {
                    case 1:
                    case 2:
                    case 3: {
                        newState = 4;
                        break;
                    }
                    default: {
                        newState = oldState;
                        wakeup = false;
                        break;
                    }
                }
            }
            if (SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(this, oldState, newState)) {
                if (oldState == 1) {
                    this.doStartThread();
                }
                if (wakeup) {
                    this.wakeup(inEventLoop);
                }
            }
        }
    }
    
    @Override
    public boolean isShuttingDown() {
        return SingleThreadEventExecutor.STATE_UPDATER.get(this) >= 3;
    }
    
    @Override
    public boolean isShutdown() {
        return SingleThreadEventExecutor.STATE_UPDATER.get(this) >= 4;
    }
    
    @Override
    public boolean isTerminated() {
        return SingleThreadEventExecutor.STATE_UPDATER.get(this) == 5;
    }
    
    protected boolean confirmShutdown() {
        if (!this.isShuttingDown()) {
            return false;
        }
        if (!this.inEventLoop()) {
            throw new IllegalStateException("must be invoked from an event loop");
        }
        this.cancelScheduledTasks();
        if (this.gracefulShutdownStartTime == 0L) {
            this.gracefulShutdownStartTime = ScheduledFutureTask.nanoTime();
        }
        if (this.runAllTasks() || this.runShutdownHooks()) {
            if (this.isShutdown()) {
                return true;
            }
            if (this.gracefulShutdownQuietPeriod == 0L) {
                return true;
            }
            this.wakeup(true);
            return false;
        }
        else {
            final long nanoTime = ScheduledFutureTask.nanoTime();
            if (this.isShutdown() || nanoTime - this.gracefulShutdownStartTime > this.gracefulShutdownTimeout) {
                return true;
            }
            if (nanoTime - this.lastExecutionTime <= this.gracefulShutdownQuietPeriod) {
                this.wakeup(true);
                try {
                    Thread.sleep(100L);
                }
                catch (final InterruptedException ex) {}
                return false;
            }
            return true;
        }
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (this.inEventLoop()) {
            throw new IllegalStateException("cannot await termination of the current thread");
        }
        if (this.threadLock.tryAcquire(timeout, unit)) {
            this.threadLock.release();
        }
        return this.isTerminated();
    }
    
    @Override
    public void execute(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        final boolean inEventLoop = this.inEventLoop();
        if (inEventLoop) {
            this.addTask(task);
        }
        else {
            this.startThread();
            this.addTask(task);
            if (this.isShutdown() && this.removeTask(task)) {
                reject();
            }
        }
        if (!this.addTaskWakesUp && this.wakesUpForTask(task)) {
            this.wakeup(inEventLoop);
        }
    }
    
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        this.throwIfInEventLoop("invokeAny");
        return super.invokeAny(tasks);
    }
    
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        this.throwIfInEventLoop("invokeAny");
        return super.invokeAny(tasks, timeout, unit);
    }
    
    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        this.throwIfInEventLoop("invokeAll");
        return super.invokeAll(tasks);
    }
    
    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        this.throwIfInEventLoop("invokeAll");
        return super.invokeAll(tasks, timeout, unit);
    }
    
    private void throwIfInEventLoop(final String method) {
        if (this.inEventLoop()) {
            throw new RejectedExecutionException("Calling " + method + " from within the EventLoop is not allowed");
        }
    }
    
    public final ThreadProperties threadProperties() {
        ThreadProperties threadProperties = this.threadProperties;
        if (threadProperties == null) {
            Thread thread = this.thread;
            if (thread == null) {
                assert !this.inEventLoop();
                this.submit(SingleThreadEventExecutor.NOOP_TASK).syncUninterruptibly();
                thread = this.thread;
                assert thread != null;
            }
            threadProperties = new DefaultThreadProperties(thread);
            if (!SingleThreadEventExecutor.PROPERTIES_UPDATER.compareAndSet(this, null, threadProperties)) {
                threadProperties = this.threadProperties;
            }
        }
        return threadProperties;
    }
    
    protected boolean wakesUpForTask(final Runnable task) {
        return true;
    }
    
    protected static void reject() {
        throw new RejectedExecutionException("event executor terminated");
    }
    
    protected final void reject(final Runnable task) {
        this.rejectedExecutionHandler.rejected(task, this);
    }
    
    private void startThread() {
        if (SingleThreadEventExecutor.STATE_UPDATER.get(this) == 1 && SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(this, 1, 2)) {
            this.doStartThread();
        }
    }
    
    private void doStartThread() {
        assert this.thread == null;
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                SingleThreadEventExecutor.this.thread = Thread.currentThread();
                if (SingleThreadEventExecutor.this.interrupted) {
                    SingleThreadEventExecutor.this.thread.interrupt();
                }
                boolean success = false;
                SingleThreadEventExecutor.this.updateLastExecutionTime();
                try {
                    SingleThreadEventExecutor.this.run();
                    success = true;
                }
                catch (final Throwable t) {
                    SingleThreadEventExecutor.logger.warn("Unexpected exception from an event executor: ", t);
                    int oldState;
                    do {
                        oldState = SingleThreadEventExecutor.STATE_UPDATER.get(SingleThreadEventExecutor.this);
                    } while (oldState < 3 && !SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState, 3));
                    if (success && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
                        SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called before run() implementation terminates.");
                    }
                    try {
                        while (!SingleThreadEventExecutor.this.confirmShutdown()) {}
                    }
                    finally {
                        try {
                            SingleThreadEventExecutor.this.cleanup();
                        }
                        finally {
                            SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                            SingleThreadEventExecutor.this.threadLock.release();
                            if (!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                                SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                            }
                            SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                        }
                    }
                }
                finally {
                    int oldState2;
                    do {
                        oldState2 = SingleThreadEventExecutor.STATE_UPDATER.get(SingleThreadEventExecutor.this);
                    } while (oldState2 < 3 && !SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState2, 3));
                    if (success && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
                        SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called before run() implementation terminates.");
                    }
                    try {
                        while (!SingleThreadEventExecutor.this.confirmShutdown()) {}
                    }
                    finally {
                        try {
                            SingleThreadEventExecutor.this.cleanup();
                        }
                        finally {
                            SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                            SingleThreadEventExecutor.this.threadLock.release();
                            if (!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                                SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                            }
                            SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                        }
                    }
                }
            }
        });
    }
    
    static {
        DEFAULT_MAX_PENDING_EXECUTOR_TASKS = Math.max(16, SystemPropertyUtil.getInt("io.netty.eventexecutor.maxPendingTasks", Integer.MAX_VALUE));
        logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
        WAKEUP_TASK = new Runnable() {
            @Override
            public void run() {
            }
        };
        NOOP_TASK = new Runnable() {
            @Override
            public void run() {
            }
        };
        STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
        PROPERTIES_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SingleThreadEventExecutor.class, ThreadProperties.class, "threadProperties");
        SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
    }
    
    private static final class DefaultThreadProperties implements ThreadProperties
    {
        private final Thread t;
        
        DefaultThreadProperties(final Thread t) {
            this.t = t;
        }
        
        @Override
        public Thread.State state() {
            return this.t.getState();
        }
        
        @Override
        public int priority() {
            return this.t.getPriority();
        }
        
        @Override
        public boolean isInterrupted() {
            return this.t.isInterrupted();
        }
        
        @Override
        public boolean isDaemon() {
            return this.t.isDaemon();
        }
        
        @Override
        public String name() {
            return this.t.getName();
        }
        
        @Override
        public long id() {
            return this.t.getId();
        }
        
        @Override
        public StackTraceElement[] stackTrace() {
            return this.t.getStackTrace();
        }
        
        @Override
        public boolean isAlive() {
            return this.t.isAlive();
        }
    }
}
