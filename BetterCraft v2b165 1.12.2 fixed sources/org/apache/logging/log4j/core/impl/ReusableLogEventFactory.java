// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.message.TimestampMessage;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.async.ThreadNameCachingStrategy;

public class ReusableLogEventFactory implements LogEventFactory
{
    private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY;
    private static final Clock CLOCK;
    private static ThreadLocal<MutableLogEvent> mutableLogEventThreadLocal;
    private final ContextDataInjector injector;
    
    public ReusableLogEventFactory() {
        this.injector = ContextDataInjectorFactory.createInjector();
    }
    
    @Override
    public LogEvent createEvent(final String loggerName, final Marker marker, final String fqcn, final Level level, final Message message, final List<Property> properties, final Throwable t) {
        MutableLogEvent result = ReusableLogEventFactory.mutableLogEventThreadLocal.get();
        if (result == null || result.reserved) {
            final boolean initThreadLocal = result == null;
            result = new MutableLogEvent();
            result.setThreadId(Thread.currentThread().getId());
            result.setThreadName(Thread.currentThread().getName());
            result.setThreadPriority(Thread.currentThread().getPriority());
            if (initThreadLocal) {
                ReusableLogEventFactory.mutableLogEventThreadLocal.set(result);
            }
        }
        result.reserved = true;
        result.clear();
        result.setLoggerName(loggerName);
        result.setMarker(marker);
        result.setLoggerFqcn(fqcn);
        result.setLevel((level == null) ? Level.OFF : level);
        result.setMessage(message);
        result.setThrown(t);
        result.setContextData(this.injector.injectContextData(properties, (StringMap)result.getContextData()));
        result.setContextStack((ThreadContext.getDepth() == 0) ? ThreadContext.EMPTY_STACK : ThreadContext.cloneStack());
        result.setTimeMillis((message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : ReusableLogEventFactory.CLOCK.currentTimeMillis());
        result.setNanoTime(Log4jLogEvent.getNanoClock().nanoTime());
        if (ReusableLogEventFactory.THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
            result.setThreadName(Thread.currentThread().getName());
            result.setThreadPriority(Thread.currentThread().getPriority());
        }
        return result;
    }
    
    public static void release(final LogEvent logEvent) {
        if (logEvent instanceof MutableLogEvent) {
            ((MutableLogEvent)logEvent).reserved = false;
        }
    }
    
    static {
        THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
        CLOCK = ClockFactory.getClock();
        ReusableLogEventFactory.mutableLogEventThreadLocal = new ThreadLocal<MutableLogEvent>();
    }
}
