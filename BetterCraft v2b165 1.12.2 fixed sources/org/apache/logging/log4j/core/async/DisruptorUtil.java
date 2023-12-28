// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import org.apache.logging.log4j.util.LoaderUtil;
import com.lmax.disruptor.ExceptionHandler;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.util.Constants;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import java.util.concurrent.TimeUnit;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import java.util.Locale;
import com.lmax.disruptor.WaitStrategy;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.Logger;

final class DisruptorUtil
{
    private static final Logger LOGGER;
    private static final int RINGBUFFER_MIN_SIZE = 128;
    private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
    private static final int RINGBUFFER_NO_GC_DEFAULT_SIZE = 4096;
    
    private DisruptorUtil() {
    }
    
    static long getTimeout(final String propertyName, final long defaultTimeout) {
        return PropertiesUtil.getProperties().getLongProperty(propertyName, defaultTimeout);
    }
    
    static WaitStrategy createWaitStrategy(final String propertyName) {
        final String key = propertyName.startsWith("AsyncLogger.") ? "AsyncLogger.Timeout" : "AsyncLoggerConfig.Timeout";
        final long timeoutMillis = getTimeout(key, 10L);
        return createWaitStrategy(propertyName, timeoutMillis);
    }
    
    static WaitStrategy createWaitStrategy(final String propertyName, final long timeoutMillis) {
        final String strategy = PropertiesUtil.getProperties().getStringProperty(propertyName, "TIMEOUT");
        DisruptorUtil.LOGGER.trace("property {}={}", propertyName, strategy);
        final String upperCase;
        final String strategyUp = upperCase = strategy.toUpperCase(Locale.ROOT);
        switch (upperCase) {
            case "SLEEP": {
                return (WaitStrategy)new SleepingWaitStrategy();
            }
            case "YIELD": {
                return (WaitStrategy)new YieldingWaitStrategy();
            }
            case "BLOCK": {
                return (WaitStrategy)new BlockingWaitStrategy();
            }
            case "BUSYSPIN": {
                return (WaitStrategy)new BusySpinWaitStrategy();
            }
            case "TIMEOUT": {
                return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
            }
            default: {
                return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
            }
        }
    }
    
    static int calculateRingBufferSize(final String propertyName) {
        int ringBufferSize = Constants.ENABLE_THREADLOCALS ? 4096 : 262144;
        final String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, String.valueOf(ringBufferSize));
        try {
            int size = Integer.parseInt(userPreferredRBSize);
            if (size < 128) {
                size = 128;
                DisruptorUtil.LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", userPreferredRBSize, 128);
            }
            ringBufferSize = size;
        }
        catch (final Exception ex) {
            DisruptorUtil.LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", userPreferredRBSize, ringBufferSize);
        }
        return Integers.ceilingNextPowerOfTwo(ringBufferSize);
    }
    
    static ExceptionHandler<RingBufferLogEvent> getAsyncLoggerExceptionHandler() {
        final String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ExceptionHandler");
        if (cls == null) {
            return (ExceptionHandler<RingBufferLogEvent>)new AsyncLoggerDefaultExceptionHandler();
        }
        try {
            final Class<? extends ExceptionHandler<RingBufferLogEvent>> klass = (Class<? extends ExceptionHandler<RingBufferLogEvent>>)LoaderUtil.loadClass(cls);
            return (ExceptionHandler<RingBufferLogEvent>)klass.newInstance();
        }
        catch (final Exception ignored) {
            DisruptorUtil.LOGGER.debug("Invalid AsyncLogger.ExceptionHandler value: error creating {}: ", cls, ignored);
            return (ExceptionHandler<RingBufferLogEvent>)new AsyncLoggerDefaultExceptionHandler();
        }
    }
    
    static ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper> getAsyncLoggerConfigExceptionHandler() {
        final String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLoggerConfig.ExceptionHandler");
        if (cls == null) {
            return (ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>)new AsyncLoggerConfigDefaultExceptionHandler();
        }
        try {
            final Class<? extends ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>> klass = (Class<? extends ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>>)LoaderUtil.loadClass(cls);
            return (ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>)klass.newInstance();
        }
        catch (final Exception ignored) {
            DisruptorUtil.LOGGER.debug("Invalid AsyncLoggerConfig.ExceptionHandler value: error creating {}: ", cls, ignored);
            return (ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>)new AsyncLoggerConfigDefaultExceptionHandler();
        }
    }
    
    public static long getExecutorThreadId(final ExecutorService executor) {
        final Future<Long> result = executor.submit((Callable<Long>)new Callable<Long>() {
            @Override
            public Long call() {
                return Thread.currentThread().getId();
            }
        });
        try {
            return result.get();
        }
        catch (final Exception ex) {
            final String msg = "Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.";
            throw new IllegalStateException("Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.", ex);
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
