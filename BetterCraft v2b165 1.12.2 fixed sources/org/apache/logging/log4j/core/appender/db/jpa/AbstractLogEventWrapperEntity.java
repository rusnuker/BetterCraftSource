// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jpa;

import org.apache.logging.log4j.core.AbstractLogEvent;
import org.apache.logging.log4j.ThreadContext;
import java.util.Map;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import javax.persistence.Transient;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import javax.persistence.InheritanceType;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import org.apache.logging.log4j.core.LogEvent;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractLogEventWrapperEntity implements LogEvent
{
    private static final long serialVersionUID = 1L;
    private final LogEvent wrappedEvent;
    
    protected AbstractLogEventWrapperEntity() {
        this(new NullLogEvent());
    }
    
    protected AbstractLogEventWrapperEntity(final LogEvent wrappedEvent) {
        if (wrappedEvent == null) {
            throw new IllegalArgumentException("The wrapped event cannot be null.");
        }
        this.wrappedEvent = wrappedEvent;
    }
    
    @Override
    public LogEvent toImmutable() {
        return Log4jLogEvent.createMemento(this);
    }
    
    @Transient
    protected final LogEvent getWrappedEvent() {
        return this.wrappedEvent;
    }
    
    public void setLevel(final Level level) {
    }
    
    public void setLoggerName(final String loggerName) {
    }
    
    public void setSource(final StackTraceElement source) {
    }
    
    public void setMessage(final Message message) {
    }
    
    public void setMarker(final Marker marker) {
    }
    
    public void setThreadId(final long threadId) {
    }
    
    public void setThreadName(final String threadName) {
    }
    
    public void setThreadPriority(final int threadPriority) {
    }
    
    public void setNanoTime(final long nanoTime) {
    }
    
    public void setTimeMillis(final long millis) {
    }
    
    public void setThrown(final Throwable throwable) {
    }
    
    public void setContextData(final ReadOnlyStringMap contextData) {
    }
    
    public void setContextMap(final Map<String, String> map) {
    }
    
    public void setContextStack(final ThreadContext.ContextStack contextStack) {
    }
    
    public void setLoggerFqcn(final String fqcn) {
    }
    
    @Transient
    @Override
    public final boolean isIncludeLocation() {
        return this.getWrappedEvent().isIncludeLocation();
    }
    
    @Override
    public final void setIncludeLocation(final boolean locationRequired) {
        this.getWrappedEvent().setIncludeLocation(locationRequired);
    }
    
    @Transient
    @Override
    public final boolean isEndOfBatch() {
        return this.getWrappedEvent().isEndOfBatch();
    }
    
    @Override
    public final void setEndOfBatch(final boolean endOfBatch) {
        this.getWrappedEvent().setEndOfBatch(endOfBatch);
    }
    
    @Transient
    @Override
    public ReadOnlyStringMap getContextData() {
        return this.getWrappedEvent().getContextData();
    }
    
    private static class NullLogEvent extends AbstractLogEvent
    {
        private static final long serialVersionUID = 1L;
    }
}
