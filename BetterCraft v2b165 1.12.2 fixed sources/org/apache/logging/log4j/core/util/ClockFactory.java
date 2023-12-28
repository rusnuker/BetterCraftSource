// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.status.StatusLogger;

public final class ClockFactory
{
    public static final String PROPERTY_NAME = "log4j.Clock";
    private static final StatusLogger LOGGER;
    
    private ClockFactory() {
    }
    
    public static Clock getClock() {
        return createClock();
    }
    
    private static Clock createClock() {
        final String userRequest = PropertiesUtil.getProperties().getStringProperty("log4j.Clock");
        if (userRequest == null || "SystemClock".equals(userRequest)) {
            ClockFactory.LOGGER.trace("Using default SystemClock for timestamps.");
            return new SystemClock();
        }
        if (CachedClock.class.getName().equals(userRequest) || "CachedClock".equals(userRequest)) {
            ClockFactory.LOGGER.trace("Using specified CachedClock for timestamps.");
            return CachedClock.instance();
        }
        if (CoarseCachedClock.class.getName().equals(userRequest) || "CoarseCachedClock".equals(userRequest)) {
            ClockFactory.LOGGER.trace("Using specified CoarseCachedClock for timestamps.");
            return CoarseCachedClock.instance();
        }
        try {
            final Clock result = Loader.newCheckedInstanceOf(userRequest, Clock.class);
            ClockFactory.LOGGER.trace("Using {} for timestamps.", result.getClass().getName());
            return result;
        }
        catch (final Exception e) {
            final String fmt = "Could not create {}: {}, using default SystemClock for timestamps.";
            ClockFactory.LOGGER.error("Could not create {}: {}, using default SystemClock for timestamps.", userRequest, e);
            return new SystemClock();
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
