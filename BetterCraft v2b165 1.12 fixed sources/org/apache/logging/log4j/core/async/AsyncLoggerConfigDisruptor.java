// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.Level;
import com.lmax.disruptor.RingBuffer;
import org.apache.logging.log4j.core.util.ExecutorServices;
import java.util.concurrent.TimeUnit;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import java.util.concurrent.Executor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import java.util.concurrent.ExecutorService;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.core.LogEvent;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.EventFactory;
import org.apache.logging.log4j.core.AbstractLifeCycle;

public class AsyncLoggerConfigDisruptor extends AbstractLifeCycle implements AsyncLoggerConfigDelegate
{
    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
    private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
    private static final EventFactory<Log4jEventWrapper> FACTORY;
    private static final EventFactory<Log4jEventWrapper> MUTABLE_FACTORY;
    private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> TRANSLATOR;
    private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> MUTABLE_TRANSLATOR;
    private static final ThreadFactory THREAD_FACTORY;
    private int ringBufferSize;
    private AsyncQueueFullPolicy asyncQueueFullPolicy;
    private Boolean mutable;
    private volatile Disruptor<Log4jEventWrapper> disruptor;
    private ExecutorService executor;
    private long backgroundThreadId;
    private EventFactory<Log4jEventWrapper> factory;
    private EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> translator;
    
    public AsyncLoggerConfigDisruptor() {
        this.mutable = Boolean.FALSE;
    }
    
    @Override
    public void setLogEventFactory(final LogEventFactory logEventFactory) {
        this.mutable = (this.mutable || logEventFactory instanceof ReusableLogEventFactory);
    }
    
    @Override
    public synchronized void start() {
        if (this.disruptor != null) {
            AsyncLoggerConfigDisruptor.LOGGER.trace("AsyncLoggerConfigDisruptor not starting new disruptor for this configuration, using existing object.");
            return;
        }
        AsyncLoggerConfigDisruptor.LOGGER.trace("AsyncLoggerConfigDisruptor creating new disruptor for this configuration.");
        this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLoggerConfig.RingBufferSize");
        final WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLoggerConfig.WaitStrategy");
        this.executor = Executors.newSingleThreadExecutor(AsyncLoggerConfigDisruptor.THREAD_FACTORY);
        this.backgroundThreadId = DisruptorUtil.getExecutorThreadId(this.executor);
        this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
        this.translator = (this.mutable ? AsyncLoggerConfigDisruptor.MUTABLE_TRANSLATOR : AsyncLoggerConfigDisruptor.TRANSLATOR);
        this.factory = (this.mutable ? AsyncLoggerConfigDisruptor.MUTABLE_FACTORY : AsyncLoggerConfigDisruptor.FACTORY);
        this.disruptor = (Disruptor<Log4jEventWrapper>)new Disruptor((EventFactory)this.factory, this.ringBufferSize, (Executor)this.executor, ProducerType.MULTI, waitStrategy);
        final ExceptionHandler<Log4jEventWrapper> errorHandler = DisruptorUtil.getAsyncLoggerConfigExceptionHandler();
        this.disruptor.handleExceptionsWith((ExceptionHandler)errorHandler);
        final Log4jEventWrapperHandler[] handlers = { new Log4jEventWrapperHandler() };
        this.disruptor.handleEventsWith((EventHandler[])handlers);
        AsyncLoggerConfigDisruptor.LOGGER.debug("Starting AsyncLoggerConfig disruptor for this configuration with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", (Object)this.disruptor.getRingBuffer().getBufferSize(), waitStrategy.getClass().getSimpleName(), errorHandler);
        this.disruptor.start();
        super.start();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        final Disruptor<Log4jEventWrapper> temp = this.disruptor;
        if (temp == null) {
            AsyncLoggerConfigDisruptor.LOGGER.trace("AsyncLoggerConfigDisruptor: disruptor for this configuration already shut down.");
            return true;
        }
        this.setStopping();
        AsyncLoggerConfigDisruptor.LOGGER.trace("AsyncLoggerConfigDisruptor: shutting down disruptor for this configuration.");
        this.disruptor = null;
        for (int i = 0; hasBacklog(temp) && i < 200; ++i) {
            try {
                Thread.sleep(50L);
            }
            catch (final InterruptedException ex) {}
        }
        temp.shutdown();
        AsyncLoggerConfigDisruptor.LOGGER.trace("AsyncLoggerConfigDisruptor: shutting down disruptor executor for this configuration.");
        ExecutorServices.shutdown(this.executor, timeout, timeUnit, this.toString());
        this.executor = null;
        if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
            AsyncLoggerConfigDisruptor.LOGGER.trace("AsyncLoggerConfigDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy));
        }
        this.setStopped();
        return true;
    }
    
    private static boolean hasBacklog(final Disruptor<?> theDisruptor) {
        final RingBuffer<?> ringBuffer = (RingBuffer<?>)theDisruptor.getRingBuffer();
        return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }
    
    @Override
    public EventRoute getEventRoute(final Level logLevel) {
        final int remainingCapacity = this.remainingDisruptorCapacity();
        if (remainingCapacity < 0) {
            return EventRoute.DISCARD;
        }
        return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
    }
    
    private int remainingDisruptorCapacity() {
        final Disruptor<Log4jEventWrapper> temp = this.disruptor;
        if (this.hasLog4jBeenShutDown(temp)) {
            return -1;
        }
        return (int)temp.getRingBuffer().remainingCapacity();
    }
    
    private boolean hasLog4jBeenShutDown(final Disruptor<Log4jEventWrapper> aDisruptor) {
        if (aDisruptor == null) {
            AsyncLoggerConfigDisruptor.LOGGER.warn("Ignoring log event after log4j was shut down");
            return true;
        }
        return false;
    }
    
    @Override
    public void enqueueEvent(final LogEvent event, final AsyncLoggerConfig asyncLoggerConfig) {
        try {
            final LogEvent logEvent = this.prepareEvent(event);
            this.enqueue(logEvent, asyncLoggerConfig);
        }
        catch (final NullPointerException npe) {
            AsyncLoggerConfigDisruptor.LOGGER.warn("Ignoring log event after log4j was shut down.");
        }
    }
    
    private LogEvent prepareEvent(final LogEvent event) {
        final LogEvent logEvent = this.ensureImmutable(event);
        if (logEvent instanceof Log4jLogEvent && logEvent.getMessage() instanceof ReusableMessage) {
            ((Log4jLogEvent)logEvent).makeMessageImmutable();
        }
        return logEvent;
    }
    
    private void enqueue(final LogEvent logEvent, final AsyncLoggerConfig asyncLoggerConfig) {
        this.disruptor.getRingBuffer().publishEvent((EventTranslatorTwoArg)this.translator, (Object)logEvent, (Object)asyncLoggerConfig);
    }
    
    @Override
    public boolean tryEnqueue(final LogEvent event, final AsyncLoggerConfig asyncLoggerConfig) {
        final LogEvent logEvent = this.prepareEvent(event);
        return this.disruptor.getRingBuffer().tryPublishEvent((EventTranslatorTwoArg)this.translator, (Object)logEvent, (Object)asyncLoggerConfig);
    }
    
    private LogEvent ensureImmutable(final LogEvent event) {
        LogEvent result = event;
        if (event instanceof RingBufferLogEvent) {
            result = ((RingBufferLogEvent)event).createMemento();
        }
        return result;
    }
    
    @Override
    public RingBufferAdmin createRingBufferAdmin(final String contextName, final String loggerConfigName) {
        return RingBufferAdmin.forAsyncLoggerConfig((RingBuffer<?>)this.disruptor.getRingBuffer(), contextName, loggerConfigName);
    }
    
    static {
        FACTORY = (EventFactory)new EventFactory<Log4jEventWrapper>() {
            public Log4jEventWrapper newInstance() {
                return new Log4jEventWrapper();
            }
        };
        MUTABLE_FACTORY = (EventFactory)new EventFactory<Log4jEventWrapper>() {
            public Log4jEventWrapper newInstance() {
                return new Log4jEventWrapper(new MutableLogEvent());
            }
        };
        TRANSLATOR = (EventTranslatorTwoArg)new EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig>() {
            public void translateTo(final Log4jEventWrapper ringBufferElement, final long sequence, final LogEvent logEvent, final AsyncLoggerConfig loggerConfig) {
                ringBufferElement.event = logEvent;
                ringBufferElement.loggerConfig = loggerConfig;
            }
        };
        MUTABLE_TRANSLATOR = (EventTranslatorTwoArg)new EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig>() {
            public void translateTo(final Log4jEventWrapper ringBufferElement, final long sequence, final LogEvent logEvent, final AsyncLoggerConfig loggerConfig) {
                ((MutableLogEvent)ringBufferElement.event).initFrom(logEvent);
                ringBufferElement.loggerConfig = loggerConfig;
            }
        };
        THREAD_FACTORY = Log4jThreadFactory.createDaemonThreadFactory("AsyncLoggerConfig");
    }
    
    public static class Log4jEventWrapper
    {
        private AsyncLoggerConfig loggerConfig;
        private LogEvent event;
        
        public Log4jEventWrapper() {
        }
        
        public Log4jEventWrapper(final MutableLogEvent mutableLogEvent) {
            this.event = mutableLogEvent;
        }
        
        public void clear() {
            this.loggerConfig = null;
            if (this.event instanceof MutableLogEvent) {
                ((MutableLogEvent)this.event).clear();
            }
            else {
                this.event = null;
            }
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.event);
        }
    }
    
    private static class Log4jEventWrapperHandler implements SequenceReportingEventHandler<Log4jEventWrapper>
    {
        private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
        private Sequence sequenceCallback;
        private int counter;
        
        public void setSequenceCallback(final Sequence sequenceCallback) {
            this.sequenceCallback = sequenceCallback;
        }
        
        public void onEvent(final Log4jEventWrapper event, final long sequence, final boolean endOfBatch) throws Exception {
            event.event.setEndOfBatch(endOfBatch);
            event.loggerConfig.asyncCallAppenders(event.event);
            event.clear();
            this.notifyIntermediateProgress(sequence);
        }
        
        private void notifyIntermediateProgress(final long sequence) {
            if (++this.counter > 50) {
                this.sequenceCallback.set(sequence);
                this.counter = 0;
            }
        }
    }
}
