// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.logging;

import org.apache.logging.log4j.Logger;

final class Log4J2Logger extends AbstractInternalLogger
{
    private static final long serialVersionUID = 5485418394879791397L;
    private final transient Logger logger;
    
    Log4J2Logger(final Logger logger) {
        super(logger.getName());
        this.logger = logger;
    }
    
    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }
    
    @Override
    public void trace(final String msg) {
        this.logger.trace(msg);
    }
    
    @Override
    public void trace(final String format, final Object arg) {
        this.logger.trace(format, arg);
    }
    
    @Override
    public void trace(final String format, final Object argA, final Object argB) {
        this.logger.trace(format, argA, argB);
    }
    
    @Override
    public void trace(final String format, final Object... arguments) {
        this.logger.trace(format, arguments);
    }
    
    @Override
    public void trace(final String msg, final Throwable t) {
        this.logger.trace(msg, t);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }
    
    @Override
    public void debug(final String msg) {
        this.logger.debug(msg);
    }
    
    @Override
    public void debug(final String format, final Object arg) {
        this.logger.debug(format, arg);
    }
    
    @Override
    public void debug(final String format, final Object argA, final Object argB) {
        this.logger.debug(format, argA, argB);
    }
    
    @Override
    public void debug(final String format, final Object... arguments) {
        this.logger.debug(format, arguments);
    }
    
    @Override
    public void debug(final String msg, final Throwable t) {
        this.logger.debug(msg, t);
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }
    
    @Override
    public void info(final String msg) {
        this.logger.info(msg);
    }
    
    @Override
    public void info(final String format, final Object arg) {
        this.logger.info(format, arg);
    }
    
    @Override
    public void info(final String format, final Object argA, final Object argB) {
        this.logger.info(format, argA, argB);
    }
    
    @Override
    public void info(final String format, final Object... arguments) {
        this.logger.info(format, arguments);
    }
    
    @Override
    public void info(final String msg, final Throwable t) {
        this.logger.info(msg, t);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }
    
    @Override
    public void warn(final String msg) {
        this.logger.warn(msg);
    }
    
    @Override
    public void warn(final String format, final Object arg) {
        this.logger.warn(format, arg);
    }
    
    @Override
    public void warn(final String format, final Object... arguments) {
        this.logger.warn(format, arguments);
    }
    
    @Override
    public void warn(final String format, final Object argA, final Object argB) {
        this.logger.warn(format, argA, argB);
    }
    
    @Override
    public void warn(final String msg, final Throwable t) {
        this.logger.warn(msg, t);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }
    
    @Override
    public void error(final String msg) {
        this.logger.error(msg);
    }
    
    @Override
    public void error(final String format, final Object arg) {
        this.logger.error(format, arg);
    }
    
    @Override
    public void error(final String format, final Object argA, final Object argB) {
        this.logger.error(format, argA, argB);
    }
    
    @Override
    public void error(final String format, final Object... arguments) {
        this.logger.error(format, arguments);
    }
    
    @Override
    public void error(final String msg, final Throwable t) {
        this.logger.error(msg, t);
    }
}
