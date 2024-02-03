// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.mojang;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.logging.LoggerAdapterAbstract;

public class LoggerAdapterLog4j2 extends LoggerAdapterAbstract
{
    private static final Level[] LEVELS;
    private final Logger logger;
    
    public LoggerAdapterLog4j2(final String name) {
        super(name);
        this.logger = LogManager.getLogger(name);
    }
    
    @Override
    public String getType() {
        return "Log4j2 (via LaunchWrapper)";
    }
    
    @Override
    public void catching(final org.spongepowered.asm.logging.Level level, final Throwable t) {
        this.logger.catching(LoggerAdapterLog4j2.LEVELS[level.ordinal()], t);
    }
    
    @Override
    public void catching(final Throwable t) {
        this.logger.catching(t);
    }
    
    @Override
    public void debug(final String message, final Object... params) {
        this.logger.debug(message, params);
    }
    
    @Override
    public void debug(final String message, final Throwable t) {
        this.logger.debug(message, t);
    }
    
    @Override
    public void error(final String message, final Object... params) {
        this.logger.error(message, params);
    }
    
    @Override
    public void error(final String message, final Throwable t) {
        this.logger.error(message, t);
    }
    
    @Override
    public void fatal(final String message, final Object... params) {
        this.logger.fatal(message, params);
    }
    
    @Override
    public void fatal(final String message, final Throwable t) {
        this.logger.fatal(message, t);
    }
    
    @Override
    public void info(final String message, final Object... params) {
        this.logger.info(message, params);
    }
    
    @Override
    public void info(final String message, final Throwable t) {
        this.logger.info(message, t);
    }
    
    @Override
    public void log(final org.spongepowered.asm.logging.Level level, final String message, final Object... params) {
        this.logger.log(LoggerAdapterLog4j2.LEVELS[level.ordinal()], message, params);
    }
    
    @Override
    public void log(final org.spongepowered.asm.logging.Level level, final String message, final Throwable t) {
        this.logger.log(LoggerAdapterLog4j2.LEVELS[level.ordinal()], message, t);
    }
    
    @Override
    public <T extends Throwable> T throwing(final T t) {
        return this.logger.throwing(t);
    }
    
    @Override
    public void trace(final String message, final Object... params) {
        this.logger.trace(message, params);
    }
    
    @Override
    public void trace(final String message, final Throwable t) {
        this.logger.trace(message, t);
    }
    
    @Override
    public void warn(final String message, final Object... params) {
        this.logger.warn(message, params);
    }
    
    @Override
    public void warn(final String message, final Throwable t) {
        this.logger.warn(message, t);
    }
    
    static {
        LEVELS = new Level[] { Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG, Level.TRACE };
    }
}
