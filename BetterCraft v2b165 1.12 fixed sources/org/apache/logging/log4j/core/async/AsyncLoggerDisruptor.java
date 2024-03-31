// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventTranslator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import com.lmax.disruptor.RingBuffer;
import org.apache.logging.log4j.core.util.ExecutorServices;
import com.lmax.disruptor.TimeoutException;
import java.util.concurrent.TimeUnit;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import java.util.concurrent.Executor;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import java.util.concurrent.ExecutorService;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.logging.log4j.core.AbstractLifeCycle;

class AsyncLoggerDisruptor extends AbstractLifeCycle
{
    private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
    private volatile Disruptor<RingBufferLogEvent> disruptor;
    private ExecutorService executor;
    private String contextName;
    private boolean useThreadLocalTranslator;
    private long backgroundThreadId;
    private AsyncQueueFullPolicy asyncQueueFullPolicy;
    private int ringBufferSize;
    
    AsyncLoggerDisruptor(final String contextName) {
        this.useThreadLocalTranslator = true;
        this.contextName = contextName;
    }
    
    public String getContextName() {
        return this.contextName;
    }
    
    public void setContextName(final String name) {
        this.contextName = name;
    }
    
    Disruptor<RingBufferLogEvent> getDisruptor() {
        return this.disruptor;
    }
    
    @Override
    public synchronized void start() {
        if (this.disruptor != null) {
            AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor not starting new disruptor for this context, using existing object.", this.contextName);
            return;
        }
        AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor creating new disruptor for this context.", this.contextName);
        this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLogger.RingBufferSize");
        final WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLogger.WaitStrategy");
        this.executor = Executors.newSingleThreadExecutor(Log4jThreadFactory.createDaemonThreadFactory("AsyncLogger[" + this.contextName + "]"));
        this.backgroundThreadId = DisruptorUtil.getExecutorThreadId(this.executor);
        this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
        this.disruptor = (Disruptor<RingBufferLogEvent>)new Disruptor((EventFactory)RingBufferLogEvent.FACTORY, this.ringBufferSize, (Executor)this.executor, ProducerType.MULTI, waitStrategy);
        final ExceptionHandler<RingBufferLogEvent> errorHandler = DisruptorUtil.getAsyncLoggerExceptionHandler();
        this.disruptor.handleExceptionsWith((ExceptionHandler)errorHandler);
        final RingBufferLogEventHandler[] handlers = { new RingBufferLogEventHandler() };
        this.disruptor.handleEventsWith((EventHandler[])handlers);
        AsyncLoggerDisruptor.LOGGER.debug("[{}] Starting AsyncLogger disruptor for this context with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", this.contextName, this.disruptor.getRingBuffer().getBufferSize(), waitStrategy.getClass().getSimpleName(), errorHandler);
        this.disruptor.start();
        AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggers use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
        super.start();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        final Disruptor<RingBufferLogEvent> temp = this.getDisruptor();
        if (temp == null) {
            AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor: disruptor for this context already shut down.", this.contextName);
            return true;
        }
        this.setStopping();
        AsyncLoggerDisruptor.LOGGER.debug("[{}] AsyncLoggerDisruptor: shutting down disruptor for this context.", this.contextName);
        this.disruptor = null;
        for (int i = 0; hasBacklog(temp) && i < 200; ++i) {
            try {
                Thread.sleep(50L);
            }
            catch (final InterruptedException ex) {}
        }
        try {
            temp.shutdown(timeout, timeUnit);
        }
        catch (final TimeoutException e) {
            temp.shutdown();
        }
        AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggerDisruptor: shutting down disruptor executor.", this.contextName);
        ExecutorServices.shutdown(this.executor, timeout, timeUnit, this.toString());
        this.executor = null;
        if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
            AsyncLoggerDisruptor.LOGGER.trace("AsyncLoggerDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy));
        }
        this.setStopped();
        return true;
    }
    
    private static boolean hasBacklog(final Disruptor<?> theDisruptor) {
        final RingBuffer<?> ringBuffer = (RingBuffer<?>)theDisruptor.getRingBuffer();
        return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }
    
    public RingBufferAdmin createRingBufferAdmin(final String jmxContextName) {
        final RingBuffer<RingBufferLogEvent> ring = (RingBuffer<RingBufferLogEvent>)((this.disruptor == null) ? null : this.disruptor.getRingBuffer());
        return RingBufferAdmin.forAsyncLogger(ring, jmxContextName);
    }
    
    EventRoute getEventRoute(final Level logLevel) {
        final int remainingCapacity = this.remainingDisruptorCapacity();
        if (remainingCapacity < 0) {
            return EventRoute.DISCARD;
        }
        return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
    }
    
    private int remainingDisruptorCapacity() {
        final Disruptor<RingBufferLogEvent> temp = this.disruptor;
        if (this.hasLog4jBeenShutDown(temp)) {
            return -1;
        }
        return (int)temp.getRingBuffer().remainingCapacity();
    }
    
    private boolean hasLog4jBeenShutDown(final Disruptor<RingBufferLogEvent> aDisruptor) {
        if (aDisruptor == null) {
            AsyncLoggerDisruptor.LOGGER.warn("Ignoring log event after log4j was shut down");
            return true;
        }
        return false;
    }
    
    public boolean tryPublish(final RingBufferLogEventTranslator translator) {
        try {
            return this.disruptor.getRingBuffer().tryPublishEvent((EventTranslator)translator);
        }
        catch (final NullPointerException npe) {
            AsyncLoggerDisruptor.LOGGER.warn("[{}] Ignoring log event after log4j was shut down.", this.contextName);
            return false;
        }
    }
    
    void enqueueLogMessageInfo(final RingBufferLogEventTranslator translator) {
        try {
            this.disruptor.publishEvent((EventTranslator)translator);
        }
        catch (final NullPointerException npe) {
            AsyncLoggerDisruptor.LOGGER.warn("[{}] Ignoring log event after log4j was shut down.", this.contextName);
        }
    }
    
    public boolean isUseThreadLocals() {
        return this.useThreadLocalTranslator;
    }
    
    public void setUseThreadLocals(final boolean allow) {
        this.useThreadLocalTranslator = allow;
        AsyncLoggerDisruptor.LOGGER.trace("[{}] AsyncLoggers have been modified to use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
    }
}
