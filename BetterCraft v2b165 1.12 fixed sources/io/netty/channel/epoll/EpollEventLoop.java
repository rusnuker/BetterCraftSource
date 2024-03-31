// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import io.netty.util.internal.PlatformDependent;
import java.util.Queue;
import java.io.IOException;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Executor;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.Callable;
import io.netty.util.IntSupplier;
import io.netty.channel.SelectStrategy;
import io.netty.util.collection.IntObjectMap;
import io.netty.channel.unix.FileDescriptor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.SingleThreadEventLoop;

final class EpollEventLoop extends SingleThreadEventLoop
{
    private static final InternalLogger logger;
    private static final AtomicIntegerFieldUpdater<EpollEventLoop> WAKEN_UP_UPDATER;
    private final FileDescriptor epollFd;
    private final FileDescriptor eventFd;
    private final IntObjectMap<AbstractEpollChannel> channels;
    private final boolean allowGrowing;
    private final EpollEventArray events;
    private final IovArray iovArray;
    private final SelectStrategy selectStrategy;
    private final IntSupplier selectNowSupplier;
    private final Callable<Integer> pendingTasksCallable;
    private volatile int wakenUp;
    private volatile int ioRatio;
    
    EpollEventLoop(final EventLoopGroup parent, final Executor executor, final int maxEvents, final SelectStrategy strategy, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, false, EpollEventLoop.DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
        this.channels = new IntObjectHashMap<AbstractEpollChannel>(4096);
        this.iovArray = new IovArray();
        this.selectNowSupplier = new IntSupplier() {
            @Override
            public int get() throws Exception {
                return Native.epollWait(EpollEventLoop.this.epollFd.intValue(), EpollEventLoop.this.events, 0);
            }
        };
        this.pendingTasksCallable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return SingleThreadEventLoop.this.pendingTasks();
            }
        };
        this.ioRatio = 50;
        this.selectStrategy = ObjectUtil.checkNotNull(strategy, "strategy");
        if (maxEvents == 0) {
            this.allowGrowing = true;
            this.events = new EpollEventArray(4096);
        }
        else {
            this.allowGrowing = false;
            this.events = new EpollEventArray(maxEvents);
        }
        boolean success = false;
        FileDescriptor epollFd = null;
        FileDescriptor eventFd = null;
        try {
            epollFd = (this.epollFd = Native.newEpollCreate());
            eventFd = (this.eventFd = Native.newEventFd());
            try {
                Native.epollCtlAdd(epollFd.intValue(), eventFd.intValue(), Native.EPOLLIN);
            }
            catch (final IOException e) {
                throw new IllegalStateException("Unable to add eventFd filedescriptor to epoll", e);
            }
            success = true;
        }
        finally {
            if (!success) {
                if (epollFd != null) {
                    try {
                        epollFd.close();
                    }
                    catch (final Exception ex) {}
                }
                if (eventFd != null) {
                    try {
                        eventFd.close();
                    }
                    catch (final Exception ex2) {}
                }
            }
        }
    }
    
    IovArray cleanArray() {
        this.iovArray.clear();
        return this.iovArray;
    }
    
    @Override
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop && EpollEventLoop.WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
            Native.eventFdWrite(this.eventFd.intValue(), 1L);
        }
    }
    
    void add(final AbstractEpollChannel ch) throws IOException {
        assert this.inEventLoop();
        final int fd = ch.fd().intValue();
        Native.epollCtlAdd(this.epollFd.intValue(), fd, ch.flags);
        this.channels.put(fd, ch);
    }
    
    void modify(final AbstractEpollChannel ch) throws IOException {
        assert this.inEventLoop();
        Native.epollCtlMod(this.epollFd.intValue(), ch.fd().intValue(), ch.flags);
    }
    
    void remove(final AbstractEpollChannel ch) throws IOException {
        assert this.inEventLoop();
        if (ch.isOpen()) {
            final int fd = ch.fd().intValue();
            if (this.channels.remove(fd) != null) {
                Native.epollCtlDel(this.epollFd.intValue(), ch.fd().intValue());
            }
        }
    }
    
    @Override
    protected Queue<Runnable> newTaskQueue(final int maxPendingTasks) {
        return PlatformDependent.newMpscQueue(maxPendingTasks);
    }
    
    @Override
    public int pendingTasks() {
        if (this.inEventLoop()) {
            return super.pendingTasks();
        }
        return this.submit(this.pendingTasksCallable).syncUninterruptibly().getNow();
    }
    
    public int getIoRatio() {
        return this.ioRatio;
    }
    
    public void setIoRatio(final int ioRatio) {
        if (ioRatio <= 0 || ioRatio > 100) {
            throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
        }
        this.ioRatio = ioRatio;
    }
    
    private int epollWait(final boolean oldWakenUp) throws IOException {
        int selectCnt = 0;
        long currentTimeNanos = System.nanoTime();
        final long selectDeadLineNanos = currentTimeNanos + this.delayNanos(currentTimeNanos);
        while (true) {
            final long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
            if (timeoutMillis <= 0L) {
                if (selectCnt == 0) {
                    final int ready = Native.epollWait(this.epollFd.intValue(), this.events, 0);
                    if (ready > 0) {
                        return ready;
                    }
                }
                return 0;
            }
            if (this.hasTasks() && EpollEventLoop.WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
                return Native.epollWait(this.epollFd.intValue(), this.events, 0);
            }
            final int selectedKeys = Native.epollWait(this.epollFd.intValue(), this.events, (int)timeoutMillis);
            ++selectCnt;
            if (selectedKeys != 0 || oldWakenUp || this.wakenUp == 1 || this.hasTasks() || this.hasScheduledTasks()) {
                return selectedKeys;
            }
            currentTimeNanos = System.nanoTime();
        }
    }
    
    @Override
    protected void run() {
    Label_0000_Outer:
        while (true) {
            while (true) {
                try {
                    int strategy = 0;
                Label_0088:
                    while (true) {
                        strategy = this.selectStrategy.calculateStrategy(this.selectNowSupplier, this.hasTasks());
                        switch (strategy) {
                            case -2: {
                                continue Label_0000_Outer;
                            }
                            case -1: {
                                strategy = this.epollWait(EpollEventLoop.WAKEN_UP_UPDATER.getAndSet(this, 0) == 1);
                                if (this.wakenUp == 1) {
                                    Native.eventFdWrite(this.eventFd.intValue(), 1L);
                                    break Label_0088;
                                }
                                break Label_0088;
                            }
                            default: {
                                break Label_0088;
                            }
                        }
                    }
                    final int ioRatio = this.ioRatio;
                    if (ioRatio == 100) {
                        try {
                            if (strategy > 0) {
                                this.processReady(this.events, strategy);
                            }
                        }
                        finally {
                            this.runAllTasks();
                        }
                    }
                    else {
                        final long ioStartTime = System.nanoTime();
                        try {
                            if (strategy > 0) {
                                this.processReady(this.events, strategy);
                            }
                        }
                        finally {
                            final long ioTime = System.nanoTime() - ioStartTime;
                            this.runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                        }
                    }
                    if (this.allowGrowing && strategy == this.events.length()) {
                        this.events.increase();
                    }
                }
                catch (final Throwable t) {
                    handleLoopException(t);
                }
                try {
                    Label_0256: {
                        if (!this.isShuttingDown()) {
                            break Label_0256;
                        }
                        this.closeAll();
                        if (this.confirmShutdown()) {
                            break;
                        }
                        break Label_0256;
                    }
                    continue;
                }
                catch (final Throwable t) {
                    handleLoopException(t);
                    continue;
                }
                continue;
            }
        }
    }
    
    private static void handleLoopException(final Throwable t) {
        EpollEventLoop.logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        }
        catch (final InterruptedException ex) {}
    }
    
    private void closeAll() {
        try {
            Native.epollWait(this.epollFd.intValue(), this.events, 0);
        }
        catch (final IOException ex) {}
        final Collection<AbstractEpollChannel> array = new ArrayList<AbstractEpollChannel>(this.channels.size());
        for (final AbstractEpollChannel channel : this.channels.values()) {
            array.add(channel);
        }
        for (final AbstractEpollChannel ch : array) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }
    
    private void processReady(final EpollEventArray events, final int ready) {
        for (int i = 0; i < ready; ++i) {
            final int fd = events.fd(i);
            if (fd == this.eventFd.intValue()) {
                Native.eventFdRead(this.eventFd.intValue());
            }
            else {
                final long ev = events.events(i);
                final AbstractEpollChannel ch = this.channels.get(fd);
                if (ch != null) {
                    final AbstractEpollChannel.AbstractEpollUnsafe unsafe = (AbstractEpollChannel.AbstractEpollUnsafe)ch.unsafe();
                    if ((ev & (long)(Native.EPOLLERR | Native.EPOLLOUT)) != 0x0L) {
                        unsafe.epollOutReady();
                    }
                    if ((ev & (long)(Native.EPOLLERR | Native.EPOLLIN)) != 0x0L) {
                        unsafe.epollInReady();
                    }
                    if ((ev & (long)Native.EPOLLRDHUP) != 0x0L) {
                        unsafe.epollRdHupReady();
                    }
                }
                else {
                    try {
                        Native.epollCtlDel(this.epollFd.intValue(), fd);
                    }
                    catch (final IOException ex) {}
                }
            }
        }
    }
    
    @Override
    protected void cleanup() {
        try {
            try {
                this.epollFd.close();
            }
            catch (final IOException e) {
                EpollEventLoop.logger.warn("Failed to close the epoll fd.", e);
            }
            try {
                this.eventFd.close();
            }
            catch (final IOException e) {
                EpollEventLoop.logger.warn("Failed to close the event fd.", e);
            }
        }
        finally {
            this.iovArray.release();
            this.events.free();
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(EpollEventLoop.class);
        WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater(EpollEventLoop.class, "wakenUp");
    }
}
