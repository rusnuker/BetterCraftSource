// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import io.netty.util.internal.logging.InternalLogger;

public final class SystemPropertyUtil
{
    private static final InternalLogger logger;
    
    public static boolean contains(final String key) {
        return get(key) != null;
    }
    
    public static String get(final String key) {
        return get(key, null);
    }
    
    public static String get(final String key, final String def) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (key.isEmpty()) {
            throw new IllegalArgumentException("key must not be empty.");
        }
        String value = null;
        try {
            if (System.getSecurityManager() == null) {
                value = System.getProperty(key);
            }
            else {
                value = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        return System.getProperty(key);
                    }
                });
            }
        }
        catch (final Exception e) {
            SystemPropertyUtil.logger.warn("Unable to retrieve a system property '{}'; default values will be used.", key, e);
        }
        if (value == null) {
            return def;
        }
        return value;
    }
    
    public static boolean getBoolean(final String key, final boolean def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        value = value.trim().toLowerCase();
        if (value.isEmpty()) {
            return true;
        }
        if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
            return true;
        }
        if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
            return false;
        }
        SystemPropertyUtil.logger.warn("Unable to parse the boolean system property '{}':{} - using the default value: {}", key, value, def);
        return def;
    }
    
    public static int getInt(final String key, final int def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        value = value.trim();
        try {
            return Integer.parseInt(value);
        }
        catch (final Exception ex) {
            SystemPropertyUtil.logger.warn("Unable to parse the integer system property '{}':{} - using the default value: {}", key, value, def);
            return def;
        }
    }
    
    public static long getLong(final String key, final long def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        value = value.trim();
        try {
            return Long.parseLong(value);
        }
        catch (final Exception ex) {
            SystemPropertyUtil.logger.warn("Unable to parse the long integer system property '{}':{} - using the default value: {}", key, value, def);
            return def;
        }
    }
    
    private SystemPropertyUtil() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SystemPropertyUtil.class);
    }
}
