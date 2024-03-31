// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.core.config.ReliabilityStrategy;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.util.Supplier;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.util.StringMap;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.apache.logging.log4j.core.util.Constants;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.status.StatusLogger;
import com.lmax.disruptor.EventTranslatorVararg;
import org.apache.logging.log4j.core.Logger;

public class AsyncLogger extends Logger implements EventTranslatorVararg<RingBufferLogEvent>
{
    private static final StatusLogger LOGGER;
    private static final Clock CLOCK;
    private static final ContextDataInjector CONTEXT_DATA_INJECTOR;
    private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY;
    private final ThreadLocal<RingBufferLogEventTranslator> threadLocalTranslator;
    private final AsyncLoggerDisruptor loggerDisruptor;
    private volatile boolean includeLocation;
    private volatile NanoClock nanoClock;
    
    public AsyncLogger(final LoggerContext context, final String name, final MessageFactory messageFactory, final AsyncLoggerDisruptor loggerDisruptor) {
        super(context, name, messageFactory);
        this.threadLocalTranslator = new ThreadLocal<RingBufferLogEventTranslator>();
        this.loggerDisruptor = loggerDisruptor;
        this.includeLocation = this.privateConfig.loggerConfig.isIncludeLocation();
        this.nanoClock = context.getConfiguration().getNanoClock();
    }
    
    @Override
    protected void updateConfiguration(final Configuration newConfig) {
        this.nanoClock = newConfig.getNanoClock();
        this.includeLocation = newConfig.getLoggerConfig(this.name).isIncludeLocation();
        super.updateConfiguration(newConfig);
    }
    
    NanoClock getNanoClock() {
        return this.nanoClock;
    }
    
    private RingBufferLogEventTranslator getCachedTranslator() {
        RingBufferLogEventTranslator result = this.threadLocalTranslator.get();
        if (result == null) {
            result = new RingBufferLogEventTranslator();
            this.threadLocalTranslator.set(result);
        }
        return result;
    }
    
    @Override
    public void logMessage(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        if (this.loggerDisruptor.isUseThreadLocals()) {
            this.logWithThreadLocalTranslator(fqcn, level, marker, message, thrown);
        }
        else {
            this.logWithVarargTranslator(fqcn, level, marker, message, thrown);
        }
    }
    
    private boolean isReused(final Message message) {
        return message instanceof ReusableMessage;
    }
    
    private void logWithThreadLocalTranslator(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        final RingBufferLogEventTranslator translator = this.getCachedTranslator();
        this.initTranslator(translator, fqcn, level, marker, message, thrown);
        this.initTranslatorThreadValues(translator);
        this.publish(translator);
    }
    
    private void publish(final RingBufferLogEventTranslator translator) {
        if (!this.loggerDisruptor.tryPublish(translator)) {
            this.handleRingBufferFull(translator);
        }
    }
    
    private void handleRingBufferFull(final RingBufferLogEventTranslator translator) {
        final EventRoute eventRoute = this.loggerDisruptor.getEventRoute(translator.level);
        switch (eventRoute) {
            case ENQUEUE: {
                this.loggerDisruptor.enqueueLogMessageInfo(translator);
                break;
            }
            case SYNCHRONOUS: {
                this.logMessageInCurrentThread(translator.fqcn, translator.level, translator.marker, translator.message, translator.thrown);
                break;
            }
            case DISCARD: {
                break;
            }
            default: {
                throw new IllegalStateException("Unknown EventRoute " + eventRoute);
            }
        }
    }
    
    private void initTranslator(final RingBufferLogEventTranslator translator, final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        translator.setBasicValues(this, this.name, marker, fqcn, level, message, thrown, ThreadContext.getImmutableStack(), this.calcLocationIfRequested(fqcn), AsyncLogger.CLOCK.currentTimeMillis(), this.nanoClock.nanoTime());
    }
    
    private void initTranslatorThreadValues(final RingBufferLogEventTranslator translator) {
        if (AsyncLogger.THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
            translator.updateThreadValues();
        }
    }
    
    private StackTraceElement calcLocationIfRequested(final String fqcn) {
        return this.includeLocation ? Log4jLogEvent.calcLocation(fqcn) : null;
    }
    
    private void logWithVarargTranslator(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        final Disruptor<RingBufferLogEvent> disruptor = this.loggerDisruptor.getDisruptor();
        if (disruptor == null) {
            AsyncLogger.LOGGER.error("Ignoring log event after Log4j has been shut down.");
            return;
        }
        if (!this.canFormatMessageInBackground(message) && !this.isReused(message)) {
            message.getFormattedMessage();
        }
        disruptor.getRingBuffer().publishEvent((EventTranslatorVararg)this, new Object[] { this, this.calcLocationIfRequested(fqcn), fqcn, level, marker, message, thrown });
    }
    
    private boolean canFormatMessageInBackground(final Message message) {
        return Constants.FORMAT_MESSAGES_IN_BACKGROUND || message.getClass().isAnnotationPresent(AsynchronouslyFormattable.class);
    }
    
    public void translateTo(final RingBufferLogEvent event, final long sequence, final Object... args) {
        final AsyncLogger asyncLogger = (AsyncLogger)args[0];
        final StackTraceElement location = (StackTraceElement)args[1];
        final String fqcn = (String)args[2];
        final Level level = (Level)args[3];
        final Marker marker = (Marker)args[4];
        final Message message = (Message)args[5];
        final Throwable thrown = (Throwable)args[6];
        final ThreadContext.ContextStack contextStack = ThreadContext.getImmutableStack();
        final Thread currentThread = Thread.currentThread();
        final String threadName = AsyncLogger.THREAD_NAME_CACHING_STRATEGY.getThreadName();
        event.setValues(asyncLogger, asyncLogger.getName(), marker, fqcn, level, message, thrown, AsyncLogger.CONTEXT_DATA_INJECTOR.injectContextData(null, (StringMap)event.getContextData()), contextStack, currentThread.getId(), threadName, currentThread.getPriority(), location, AsyncLogger.CLOCK.currentTimeMillis(), this.nanoClock.nanoTime());
    }
    
    void logMessageInCurrentThread(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        final ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
        strategy.log(this, this.getName(), fqcn, marker, level, message, thrown);
    }
    
    public void actualAsyncLog(final RingBufferLogEvent event) {
        final List<Property> properties = this.privateConfig.loggerConfig.getPropertyList();
        if (properties != null) {
            StringMap contextData = (StringMap)event.getContextData();
            if (contextData.isFrozen()) {
                final StringMap temp = ContextDataFactory.createContextData();
                temp.putAll(contextData);
                contextData = temp;
            }
            for (int i = 0; i < properties.size(); ++i) {
                final Property prop = properties.get(i);
                if (contextData.getValue(prop.getName()) == null) {
                    final String value = prop.isValueNeedsLookup() ? this.privateConfig.config.getStrSubstitutor().replace(event, prop.getValue()) : prop.getValue();
                    contextData.putValue(prop.getName(), value);
                }
            }
            event.setContextData(contextData);
        }
        final ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
        strategy.log(this, event);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        CLOCK = ClockFactory.getClock();
        CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
        THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
    }
}
