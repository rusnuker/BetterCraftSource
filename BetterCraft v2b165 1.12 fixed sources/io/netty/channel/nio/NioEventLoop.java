// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.nio;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import java.util.ArrayList;
import io.netty.channel.EventLoop;
import java.nio.channels.CancelledKeyException;
import java.util.Set;
import java.util.Iterator;
import java.nio.channels.SelectionKey;
import io.netty.channel.EventLoopException;
import java.nio.channels.SelectableChannel;
import java.util.Queue;
import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import io.netty.util.internal.ReflectionUtil;
import java.security.AccessController;
import io.netty.util.internal.PlatformDependent;
import java.security.PrivilegedAction;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Executor;
import io.netty.channel.SelectStrategy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.Selector;
import java.util.concurrent.Callable;
import io.netty.util.IntSupplier;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.SingleThreadEventLoop;

public final class NioEventLoop extends SingleThreadEventLoop
{
    private static final InternalLogger logger;
    private static final int CLEANUP_INTERVAL = 256;
    private static final boolean DISABLE_KEYSET_OPTIMIZATION;
    private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
    private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
    private final IntSupplier selectNowSupplier;
    private final Callable<Integer> pendingTasksCallable;
    private Selector selector;
    private Selector unwrappedSelector;
    private SelectedSelectionKeySet selectedKeys;
    private final SelectorProvider provider;
    private final AtomicBoolean wakenUp;
    private final SelectStrategy selectStrategy;
    private volatile int ioRatio;
    private int cancelledKeys;
    private boolean needsToSelectAgain;
    
    NioEventLoop(final NioEventLoopGroup parent, final Executor executor, final SelectorProvider selectorProvider, final SelectStrategy strategy, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, false, NioEventLoop.DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
        this.selectNowSupplier = new IntSupplier() {
            @Override
            public int get() throws Exception {
                return NioEventLoop.this.selectNow();
            }
        };
        this.pendingTasksCallable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return SingleThreadEventLoop.this.pendingTasks();
            }
        };
        this.wakenUp = new AtomicBoolean();
        this.ioRatio = 50;
        if (selectorProvider == null) {
            throw new NullPointerException("selectorProvider");
        }
        if (strategy == null) {
            throw new NullPointerException("selectStrategy");
        }
        this.provider = selectorProvider;
        this.selector = this.openSelector();
        this.selectStrategy = strategy;
    }
    
    private Selector openSelector() {
        try {
            this.unwrappedSelector = this.provider.openSelector();
        }
        catch (final IOException e) {
            throw new ChannelException("failed to open a new selector", e);
        }
        if (NioEventLoop.DISABLE_KEYSET_OPTIMIZATION) {
            return this.unwrappedSelector;
        }
        final SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
        final Object maybeSelectorImplClass = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    return Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
                }
                catch (final Throwable cause) {
                    return cause;
                }
            }
        });
        if (!(maybeSelectorImplClass instanceof Class) || !((Class)maybeSelectorImplClass).isAssignableFrom(this.unwrappedSelector.getClass())) {
            if (maybeSelectorImplClass instanceof Throwable) {
                final Throwable t = (Throwable)maybeSelectorImplClass;
                NioEventLoop.logger.trace("failed to instrument a special java.util.Set into: {}", this.unwrappedSelector, t);
            }
            return this.unwrappedSelector;
        }
        final Class<?> selectorImplClass = (Class<?>)maybeSelectorImplClass;
        final Object maybeException = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    final Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
                    final Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
                    Throwable cause = ReflectionUtil.trySetAccessible(selectedKeysField);
                    if (cause != null) {
                        return cause;
                    }
                    cause = ReflectionUtil.trySetAccessible(publicSelectedKeysField);
                    if (cause != null) {
                        return cause;
                    }
                    selectedKeysField.set(NioEventLoop.this.unwrappedSelector, selectedKeySet);
                    publicSelectedKeysField.set(NioEventLoop.this.unwrappedSelector, selectedKeySet);
                    return null;
                }
                catch (final NoSuchFieldException e) {
                    return e;
                }
                catch (final IllegalAccessException e2) {
                    return e2;
                }
            }
        });
        if (maybeException instanceof Exception) {
            this.selectedKeys = null;
            final Exception e2 = (Exception)maybeException;
            NioEventLoop.logger.trace("failed to instrument a special java.util.Set into: {}", this.unwrappedSelector, e2);
            return this.unwrappedSelector;
        }
        this.selectedKeys = selectedKeySet;
        NioEventLoop.logger.trace("instrumented a special java.util.Set into: {}", this.unwrappedSelector);
        return new SelectedSelectionKeySetSelector(this.unwrappedSelector, selectedKeySet);
    }
    
    public SelectorProvider selectorProvider() {
        return this.provider;
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
    
    public void register(final SelectableChannel ch, final int interestOps, final NioTask<?> task) {
        if (ch == null) {
            throw new NullPointerException("ch");
        }
        if (interestOps == 0) {
            throw new IllegalArgumentException("interestOps must be non-zero.");
        }
        if ((interestOps & ~ch.validOps()) != 0x0) {
            throw new IllegalArgumentException("invalid interestOps: " + interestOps + "(validOps: " + ch.validOps() + ')');
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (this.isShutdown()) {
            throw new IllegalStateException("event loop shut down");
        }
        try {
            ch.register(this.selector, interestOps, task);
        }
        catch (final Exception e) {
            throw new EventLoopException("failed to register a channel", e);
        }
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
    
    public void rebuildSelector() {
        if (!this.inEventLoop()) {
            this.execute(new Runnable() {
                @Override
                public void run() {
                    NioEventLoop.this.rebuildSelector0();
                }
            });
            return;
        }
        this.rebuildSelector0();
    }
    
    private void rebuildSelector0() {
        final Selector oldSelector = this.selector;
        if (oldSelector == null) {
            return;
        }
        Selector newSelector;
        try {
            newSelector = this.openSelector();
        }
        catch (final Exception e) {
            NioEventLoop.logger.warn("Failed to create a new Selector.", e);
            return;
        }
        int nChannels = 0;
        for (final SelectionKey key : oldSelector.keys()) {
            final Object a = key.attachment();
            try {
                if (!key.isValid() || key.channel().keyFor(newSelector) != null) {
                    continue;
                }
                final int interestOps = key.interestOps();
                key.cancel();
                final SelectionKey newKey = key.channel().register(newSelector, interestOps, a);
                if (a instanceof AbstractNioChannel) {
                    ((AbstractNioChannel)a).selectionKey = newKey;
                }
                ++nChannels;
            }
            catch (final Exception e2) {
                NioEventLoop.logger.warn("Failed to re-register a Channel to the new Selector.", e2);
                if (a instanceof AbstractNioChannel) {
                    final AbstractNioChannel ch = (AbstractNioChannel)a;
                    ch.unsafe().close(ch.unsafe().voidPromise());
                }
                else {
                    final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                    invokeChannelUnregistered(task, key, e2);
                }
            }
        }
        this.selector = newSelector;
        try {
            oldSelector.close();
        }
        catch (final Throwable t) {
            if (NioEventLoop.logger.isWarnEnabled()) {
                NioEventLoop.logger.warn("Failed to close the old Selector.", t);
            }
        }
        NioEventLoop.logger.info("Migrated " + nChannels + " channel(s) to the new Selector.");
    }
    
    @Override
    protected void run() {
    Label_0000_Outer:
        while (true) {
            while (true) {
                try {
                Label_0077:
                    while (true) {
                        switch (this.selectStrategy.calculateStrategy(this.selectNowSupplier, this.hasTasks())) {
                            case -2: {
                                continue Label_0000_Outer;
                            }
                            case -1: {
                                this.select(this.wakenUp.getAndSet(false));
                                if (this.wakenUp.get()) {
                                    this.selector.wakeup();
                                    break Label_0077;
                                }
                                break Label_0077;
                            }
                            default: {
                                break Label_0077;
                            }
                        }
                    }
                    this.cancelledKeys = 0;
                    this.needsToSelectAgain = false;
                    final int ioRatio = this.ioRatio;
                    if (ioRatio == 100) {
                        try {
                            this.processSelectedKeys();
                        }
                        finally {
                            this.runAllTasks();
                        }
                    }
                    else {
                        final long ioStartTime = System.nanoTime();
                        try {
                            this.processSelectedKeys();
                        }
                        finally {
                            final long ioTime = System.nanoTime() - ioStartTime;
                            this.runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                        }
                    }
                }
                catch (final Throwable t) {
                    handleLoopException(t);
                }
                try {
                    Label_0210: {
                        if (!this.isShuttingDown()) {
                            break Label_0210;
                        }
                        this.closeAll();
                        if (this.confirmShutdown()) {
                            return;
                        }
                        break Label_0210;
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
        NioEventLoop.logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        }
        catch (final InterruptedException ex) {}
    }
    
    private void processSelectedKeys() {
        if (this.selectedKeys != null) {
            this.processSelectedKeysOptimized();
        }
        else {
            this.processSelectedKeysPlain(this.selector.selectedKeys());
        }
    }
    
    @Override
    protected void cleanup() {
        try {
            this.selector.close();
        }
        catch (final IOException e) {
            NioEventLoop.logger.warn("Failed to close a selector.", e);
        }
    }
    
    void cancel(final SelectionKey key) {
        key.cancel();
        ++this.cancelledKeys;
        if (this.cancelledKeys >= 256) {
            this.cancelledKeys = 0;
            this.needsToSelectAgain = true;
        }
    }
    
    @Override
    protected Runnable pollTask() {
        final Runnable task = super.pollTask();
        if (this.needsToSelectAgain) {
            this.selectAgain();
        }
        return task;
    }
    
    private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys) {
        if (selectedKeys.isEmpty()) {
            return;
        }
        Iterator<SelectionKey> i = selectedKeys.iterator();
        while (true) {
            final SelectionKey k = i.next();
            final Object a = k.attachment();
            i.remove();
            if (a instanceof AbstractNioChannel) {
                this.processSelectedKey(k, (AbstractNioChannel)a);
            }
            else {
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                processSelectedKey(k, task);
            }
            if (!i.hasNext()) {
                break;
            }
            if (!this.needsToSelectAgain) {
                continue;
            }
            this.selectAgain();
            selectedKeys = this.selector.selectedKeys();
            if (selectedKeys.isEmpty()) {
                break;
            }
            i = selectedKeys.iterator();
        }
    }
    
    private void processSelectedKeysOptimized() {
        for (int i = 0; i < this.selectedKeys.size; ++i) {
            final SelectionKey k = this.selectedKeys.keys[i];
            this.selectedKeys.keys[i] = null;
            final Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                this.processSelectedKey(k, (AbstractNioChannel)a);
            }
            else {
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                processSelectedKey(k, task);
            }
            if (this.needsToSelectAgain) {
                this.selectedKeys.reset(i + 1);
                this.selectAgain();
                i = -1;
            }
        }
    }
    
    private void processSelectedKey(final SelectionKey k, final AbstractNioChannel ch) {
        final AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
        if (k.isValid()) {
            try {
                final int readyOps = k.readyOps();
                if ((readyOps & 0x8) != 0x0) {
                    int ops = k.interestOps();
                    ops &= 0xFFFFFFF7;
                    k.interestOps(ops);
                    unsafe.finishConnect();
                }
                if ((readyOps & 0x4) != 0x0) {
                    ch.unsafe().forceFlush();
                }
                if ((readyOps & 0x11) != 0x0 || readyOps == 0) {
                    unsafe.read();
                }
            }
            catch (final CancelledKeyException ignored) {
                unsafe.close(unsafe.voidPromise());
            }
            return;
        }
        EventLoop eventLoop;
        try {
            eventLoop = ch.eventLoop();
        }
        catch (final Throwable ignored2) {
            return;
        }
        if (eventLoop != this || eventLoop == null) {
            return;
        }
        unsafe.close(unsafe.voidPromise());
    }
    
    private static void processSelectedKey(final SelectionKey k, final NioTask<SelectableChannel> task) {
        int state = 0;
        try {
            task.channelReady(k.channel(), k);
            state = 1;
        }
        catch (final Exception e) {
            k.cancel();
            invokeChannelUnregistered(task, k, e);
            state = 2;
        }
        finally {
            switch (state) {
                case 0: {
                    k.cancel();
                    invokeChannelUnregistered(task, k, null);
                    break;
                }
                case 1: {
                    if (!k.isValid()) {
                        invokeChannelUnregistered(task, k, null);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void closeAll() {
        this.selectAgain();
        final Set<SelectionKey> keys = this.selector.keys();
        final Collection<AbstractNioChannel> channels = new ArrayList<AbstractNioChannel>(keys.size());
        for (final SelectionKey k : keys) {
            final Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                channels.add((AbstractNioChannel)a);
            }
            else {
                k.cancel();
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                invokeChannelUnregistered(task, k, null);
            }
        }
        for (final AbstractNioChannel ch : channels) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }
    
    private static void invokeChannelUnregistered(final NioTask<SelectableChannel> task, final SelectionKey k, final Throwable cause) {
        try {
            task.channelUnregistered(k.channel(), cause);
        }
        catch (final Exception e) {
            NioEventLoop.logger.warn("Unexpected exception while running NioTask.channelUnregistered()", e);
        }
    }
    
    @Override
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop && this.wakenUp.compareAndSet(false, true)) {
            this.selector.wakeup();
        }
    }
    
    Selector unwrappedSelector() {
        return this.unwrappedSelector;
    }
    
    int selectNow() throws IOException {
        try {
            return this.selector.selectNow();
        }
        finally {
            if (this.wakenUp.get()) {
                this.selector.wakeup();
            }
        }
    }
    
    private void select(final boolean oldWakenUp) throws IOException {
        Selector selector = this.selector;
        try {
            int selectCnt = 0;
            long currentTimeNanos = System.nanoTime();
            final long selectDeadLineNanos = currentTimeNanos + this.delayNanos(currentTimeNanos);
            while (true) {
                final long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
                if (timeoutMillis <= 0L) {
                    if (selectCnt == 0) {
                        selector.selectNow();
                        selectCnt = 1;
                        break;
                    }
                    break;
                }
                else {
                    if (this.hasTasks() && this.wakenUp.compareAndSet(false, true)) {
                        selector.selectNow();
                        selectCnt = 1;
                        break;
                    }
                    final int selectedKeys = selector.select(timeoutMillis);
                    ++selectCnt;
                    if (selectedKeys != 0 || oldWakenUp || this.wakenUp.get() || this.hasTasks()) {
                        break;
                    }
                    if (this.hasScheduledTasks()) {
                        break;
                    }
                    if (Thread.interrupted()) {
                        if (NioEventLoop.logger.isDebugEnabled()) {
                            NioEventLoop.logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
                        }
                        selectCnt = 1;
                        break;
                    }
                    final long time = System.nanoTime();
                    if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos) {
                        selectCnt = 1;
                    }
                    else if (NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD) {
                        NioEventLoop.logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding Selector {}.", (Object)selectCnt, selector);
                        this.rebuildSelector();
                        selector = this.selector;
                        selector.selectNow();
                        selectCnt = 1;
                        break;
                    }
                    currentTimeNanos = time;
                }
            }
            if (selectCnt > 3 && NioEventLoop.logger.isDebugEnabled()) {
                NioEventLoop.logger.debug("Selector.select() returned prematurely {} times in a row for Selector {}.", (Object)(selectCnt - 1), selector);
            }
        }
        catch (final CancelledKeyException e) {
            if (NioEventLoop.logger.isDebugEnabled()) {
                NioEventLoop.logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector {} - JDK bug?", selector, e);
            }
        }
    }
    
    private void selectAgain() {
        this.needsToSelectAgain = false;
        try {
            this.selector.selectNow();
        }
        catch (final Throwable t) {
            NioEventLoop.logger.warn("Failed to update SelectionKeys.", t);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
        DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
        final String key = "sun.nio.ch.bugLevel";
        final String buglevel = SystemPropertyUtil.get("sun.nio.ch.bugLevel");
        if (buglevel == null) {
            try {
                AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Void>() {
                    @Override
                    public Void run() {
                        System.setProperty("sun.nio.ch.bugLevel", "");
                        return null;
                    }
                });
            }
            catch (final SecurityException e) {
                NioEventLoop.logger.debug("Unable to get/set System Property: sun.nio.ch.bugLevel", e);
            }
        }
        int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
        if (selectorAutoRebuildThreshold < 3) {
            selectorAutoRebuildThreshold = 0;
        }
        SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
        if (NioEventLoop.logger.isDebugEnabled()) {
            NioEventLoop.logger.debug("-Dio.netty.noKeySetOptimization: {}", (Object)NioEventLoop.DISABLE_KEYSET_OPTIMIZATION);
            NioEventLoop.logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", (Object)NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD);
        }
    }
}
