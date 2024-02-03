// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.logging;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LoggerAdapterJava extends LoggerAdapterAbstract
{
    private static final Level[] LEVELS;
    private final Logger logger;
    
    public LoggerAdapterJava(final String name) {
        super(name);
        this.logger = getLogger(name);
    }
    
    @Override
    public String getType() {
        return "java.util.logging Log Adapter";
    }
    
    @Override
    public void catching(final org.spongepowered.asm.logging.Level level, final Throwable t) {
        this.warn("Catching {}: {}", t.getClass().getName(), t.getMessage(), t);
    }
    
    @Override
    public void debug(final String message, final Object... params) {
        final FormattedMessage formatted = new FormattedMessage(message, params);
        this.logger.fine(formatted.getMessage());
        if (formatted.hasThrowable()) {
            this.logger.fine(formatted.getThrowable().toString());
        }
    }
    
    @Override
    public void debug(final String message, final Throwable t) {
        this.logger.fine(message);
        this.logger.fine(t.toString());
    }
    
    @Override
    public void error(final String message, final Object... params) {
        final FormattedMessage formatted = new FormattedMessage(message, params);
        this.logger.severe(formatted.getMessage());
        if (formatted.hasThrowable()) {
            this.logger.severe(formatted.getThrowable().toString());
        }
    }
    
    @Override
    public void error(final String message, final Throwable t) {
        this.logger.severe(message);
        this.logger.severe(t.toString());
    }
    
    @Override
    public void fatal(final String message, final Object... params) {
        final FormattedMessage formatted = new FormattedMessage(message, params);
        this.logger.severe(formatted.getMessage());
        if (formatted.hasThrowable()) {
            this.logger.severe(formatted.getThrowable().toString());
        }
    }
    
    @Override
    public void fatal(final String message, final Throwable t) {
        this.logger.severe(message);
        this.logger.severe(t.toString());
    }
    
    @Override
    public void info(final String message, final Object... params) {
        final FormattedMessage formatted = new FormattedMessage(message, params);
        this.logger.info(formatted.getMessage());
        if (formatted.hasThrowable()) {
            this.logger.info(formatted.getThrowable().toString());
        }
    }
    
    @Override
    public void info(final String message, final Throwable t) {
        this.logger.info(message);
        this.logger.info(t.toString());
    }
    
    @Override
    public void log(final org.spongepowered.asm.logging.Level level, final String message, final Object... params) {
        final Level logLevel = LoggerAdapterJava.LEVELS[level.ordinal()];
        final FormattedMessage formatted = new FormattedMessage(message, params);
        this.logger.log(logLevel, formatted.getMessage());
        if (formatted.hasThrowable()) {
            this.logger.log(LoggerAdapterJava.LEVELS[level.ordinal()], formatted.getThrowable().toString());
        }
    }
    
    @Override
    public void log(final org.spongepowered.asm.logging.Level level, final String message, final Throwable t) {
        final Level logLevel = LoggerAdapterJava.LEVELS[level.ordinal()];
        this.logger.log(logLevel, message);
        this.logger.log(logLevel, t.toString());
    }
    
    @Override
    public <T extends Throwable> T throwing(final T t) {
        this.warn("Throwing {}: {}", t.getClass().getName(), t.getMessage(), t);
        return t;
    }
    
    @Override
    public void trace(final String message, final Object... params) {
        final FormattedMessage formatted = new FormattedMessage(message, params);
        this.logger.finer(formatted.getMessage());
        if (formatted.hasThrowable()) {
            this.logger.finer(formatted.getThrowable().toString());
        }
    }
    
    @Override
    public void trace(final String message, final Throwable t) {
        this.logger.finer(message);
        this.logger.finer(t.toString());
    }
    
    @Override
    public void warn(final String message, final Object... params) {
        final FormattedMessage formatted = new FormattedMessage(message, params);
        this.logger.warning(formatted.getMessage());
        if (formatted.hasThrowable()) {
            this.logger.warning(formatted.getThrowable().toString());
        }
    }
    
    @Override
    public void warn(final String message, final Throwable t) {
        this.logger.warning(message);
        this.logger.warning(t.toString());
    }
    
    private static Logger getLogger(final String name) {
        final LogManager logManager = LogManager.getLogManager();
        final Logger logger = logManager.getLogger(name);
        if (logger != null) {
            return logger;
        }
        return LogManager.getLogManager().getLogger("global");
    }
    
    static {
        LEVELS = new Level[] { Level.SEVERE, Level.SEVERE, Level.WARNING, Level.INFO, Level.FINE, Level.FINER };
    }
}
