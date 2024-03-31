// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.launchwrapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogWrapper
{
    public static LogWrapper log;
    private Logger myLog;
    private static boolean configured;
    
    static {
        LogWrapper.log = new LogWrapper();
    }
    
    private static void configureLogging() {
        LogWrapper.log.myLog = LogManager.getLogger("LaunchWrapper");
        LogWrapper.configured = true;
    }
    
    public static void retarget(final Logger to) {
        LogWrapper.log.myLog = to;
    }
    
    public static void log(final String logChannel, final Level level, final String format, final Object... data) {
        makeLog(logChannel);
        LogManager.getLogger(logChannel).log(level, String.format(format, data));
    }
    
    public static void log(final Level level, final String format, final Object... data) {
        if (!LogWrapper.configured) {
            configureLogging();
        }
        LogWrapper.log.myLog.log(level, String.format(format, data));
    }
    
    public static void log(final String logChannel, final Level level, final Throwable ex, final String format, final Object... data) {
        makeLog(logChannel);
        LogManager.getLogger(logChannel).log(level, String.format(format, data), ex);
    }
    
    public static void log(final Level level, final Throwable ex, final String format, final Object... data) {
        if (!LogWrapper.configured) {
            configureLogging();
        }
        LogWrapper.log.myLog.log(level, String.format(format, data), ex);
    }
    
    public static void severe(final String format, final Object... data) {
        log(Level.ERROR, format, data);
    }
    
    public static void warning(final String format, final Object... data) {
        log(Level.WARN, format, data);
    }
    
    public static void info(final String format, final Object... data) {
        log(Level.INFO, format, data);
    }
    
    public static void fine(final String format, final Object... data) {
        log(Level.DEBUG, format, data);
    }
    
    public static void finer(final String format, final Object... data) {
        log(Level.TRACE, format, data);
    }
    
    public static void finest(final String format, final Object... data) {
        log(Level.TRACE, format, data);
    }
    
    public static void makeLog(final String logChannel) {
        LogManager.getLogger(logChannel);
    }
}
