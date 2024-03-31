// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.message.TimestampMessage;
import java.util.Map;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.message.ParameterizedMessage;
import java.util.Arrays;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.core.LogEvent;

public class RingBufferLogEvent implements LogEvent, ReusableMessage, CharSequence
{
    public static final Factory FACTORY;
    private static final long serialVersionUID = 8462119088943934758L;
    private static final Message EMPTY;
    private int threadPriority;
    private long threadId;
    private long currentTimeMillis;
    private long nanoTime;
    private short parameterCount;
    private boolean includeLocation;
    private boolean endOfBatch;
    private Level level;
    private String threadName;
    private String loggerName;
    private Message message;
    private StringBuilder messageText;
    private Object[] parameters;
    private transient Throwable thrown;
    private ThrowableProxy thrownProxy;
    private StringMap contextData;
    private Marker marker;
    private String fqcn;
    private StackTraceElement location;
    private ThreadContext.ContextStack contextStack;
    private transient AsyncLogger asyncLogger;
    
    public RingBufferLogEvent() {
        this.endOfBatch = false;
        this.contextData = ContextDataFactory.createContextData();
    }
    
    public void setValues(final AsyncLogger anAsyncLogger, final String aLoggerName, final Marker aMarker, final String theFqcn, final Level aLevel, final Message msg, final Throwable aThrowable, final StringMap mutableContextData, final ThreadContext.ContextStack aContextStack, final long threadId, final String threadName, final int threadPriority, final StackTraceElement aLocation, final long aCurrentTimeMillis, final long aNanoTime) {
        this.threadPriority = threadPriority;
        this.threadId = threadId;
        this.currentTimeMillis = aCurrentTimeMillis;
        this.nanoTime = aNanoTime;
        this.level = aLevel;
        this.threadName = threadName;
        this.loggerName = aLoggerName;
        this.setMessage(msg);
        this.thrown = aThrowable;
        this.thrownProxy = null;
        this.marker = aMarker;
        this.fqcn = theFqcn;
        this.location = aLocation;
        this.contextData = mutableContextData;
        this.contextStack = aContextStack;
        this.asyncLogger = anAsyncLogger;
    }
    
    @Override
    public LogEvent toImmutable() {
        return this.createMemento();
    }
    
    private void setMessage(final Message msg) {
        if (msg instanceof ReusableMessage) {
            final ReusableMessage reusable = (ReusableMessage)msg;
            reusable.formatTo(this.getMessageTextForWriting());
            if (this.parameters != null) {
                this.parameters = reusable.swapParameters(this.parameters);
                this.parameterCount = reusable.getParameterCount();
            }
        }
        else {
            if (msg != null && !this.canFormatMessageInBackground(msg)) {
                msg.getFormattedMessage();
            }
            this.message = msg;
        }
    }
    
    private boolean canFormatMessageInBackground(final Message message) {
        return Constants.FORMAT_MESSAGES_IN_BACKGROUND || message.getClass().isAnnotationPresent(AsynchronouslyFormattable.class);
    }
    
    private StringBuilder getMessageTextForWriting() {
        if (this.messageText == null) {
            this.messageText = new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE);
        }
        this.messageText.setLength(0);
        return this.messageText;
    }
    
    public void execute(final boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
        this.asyncLogger.actualAsyncLog(this);
    }
    
    @Override
    public boolean isEndOfBatch() {
        return this.endOfBatch;
    }
    
    @Override
    public void setEndOfBatch(final boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
    }
    
    @Override
    public boolean isIncludeLocation() {
        return this.includeLocation;
    }
    
    @Override
    public void setIncludeLocation(final boolean includeLocation) {
        this.includeLocation = includeLocation;
    }
    
    @Override
    public String getLoggerName() {
        return this.loggerName;
    }
    
    @Override
    public Marker getMarker() {
        return this.marker;
    }
    
    @Override
    public String getLoggerFqcn() {
        return this.fqcn;
    }
    
    @Override
    public Level getLevel() {
        if (this.level == null) {
            this.level = Level.OFF;
        }
        return this.level;
    }
    
    @Override
    public Message getMessage() {
        if (this.message == null) {
            return (this.messageText == null) ? RingBufferLogEvent.EMPTY : this;
        }
        return this.message;
    }
    
    @Override
    public String getFormattedMessage() {
        return (this.messageText != null) ? this.messageText.toString() : ((this.message == null) ? null : this.message.getFormattedMessage());
    }
    
    @Override
    public String getFormat() {
        return null;
    }
    
    @Override
    public Object[] getParameters() {
        return (Object[])((this.parameters == null) ? null : Arrays.copyOf(this.parameters, this.parameterCount));
    }
    
    @Override
    public Throwable getThrowable() {
        return this.getThrown();
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        buffer.append((CharSequence)this.messageText);
    }
    
    @Override
    public Object[] swapParameters(final Object[] emptyReplacement) {
        final Object[] result = this.parameters;
        this.parameters = emptyReplacement;
        return result;
    }
    
    @Override
    public short getParameterCount() {
        return this.parameterCount;
    }
    
    @Override
    public Message memento() {
        if (this.message != null) {
            return this.message;
        }
        final Object[] params = (this.parameters == null) ? new Object[0] : Arrays.copyOf(this.parameters, this.parameterCount);
        return new ParameterizedMessage(this.messageText.toString(), params);
    }
    
    @Override
    public int length() {
        return this.messageText.length();
    }
    
    @Override
    public char charAt(final int index) {
        return this.messageText.charAt(index);
    }
    
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return this.messageText.subSequence(start, end);
    }
    
    private Message getNonNullImmutableMessage() {
        return (this.message != null) ? this.message : new SimpleMessage(String.valueOf(this.messageText));
    }
    
    @Override
    public Throwable getThrown() {
        if (this.thrown == null && this.thrownProxy != null) {
            this.thrown = this.thrownProxy.getThrowable();
        }
        return this.thrown;
    }
    
    @Override
    public ThrowableProxy getThrownProxy() {
        if (this.thrownProxy == null && this.thrown != null) {
            this.thrownProxy = new ThrowableProxy(this.thrown);
        }
        return this.thrownProxy;
    }
    
    @Override
    public ReadOnlyStringMap getContextData() {
        return this.contextData;
    }
    
    void setContextData(final StringMap contextData) {
        this.contextData = contextData;
    }
    
    @Override
    public Map<String, String> getContextMap() {
        return this.contextData.toMap();
    }
    
    @Override
    public ThreadContext.ContextStack getContextStack() {
        return this.contextStack;
    }
    
    @Override
    public long getThreadId() {
        return this.threadId;
    }
    
    @Override
    public String getThreadName() {
        return this.threadName;
    }
    
    @Override
    public int getThreadPriority() {
        return this.threadPriority;
    }
    
    @Override
    public StackTraceElement getSource() {
        return this.location;
    }
    
    @Override
    public long getTimeMillis() {
        return (this.message instanceof TimestampMessage) ? ((TimestampMessage)this.message).getTimestamp() : this.currentTimeMillis;
    }
    
    @Override
    public long getNanoTime() {
        return this.nanoTime;
    }
    
    public void clear() {
        this.asyncLogger = null;
        this.loggerName = null;
        this.marker = null;
        this.fqcn = null;
        this.level = null;
        this.message = null;
        this.thrown = null;
        this.thrownProxy = null;
        this.contextStack = null;
        this.location = null;
        if (this.contextData != null) {
            if (this.contextData.isFrozen()) {
                this.contextData = null;
            }
            else {
                this.contextData.clear();
            }
        }
        this.trimMessageText();
        if (this.parameters != null) {
            for (int i = 0; i < this.parameters.length; ++i) {
                this.parameters[i] = null;
            }
        }
    }
    
    private void trimMessageText() {
        if (this.messageText != null && this.messageText.length() > Constants.MAX_REUSABLE_MESSAGE_SIZE) {
            this.messageText.setLength(Constants.MAX_REUSABLE_MESSAGE_SIZE);
            this.messageText.trimToSize();
        }
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        this.getThrownProxy();
        out.defaultWriteObject();
    }
    
    public LogEvent createMemento() {
        return new Log4jLogEvent.Builder(this).build();
    }
    
    public void initializeBuilder(final Log4jLogEvent.Builder builder) {
        builder.setContextData(this.contextData).setContextStack(this.contextStack).setEndOfBatch(this.endOfBatch).setIncludeLocation(this.includeLocation).setLevel(this.getLevel()).setLoggerFqcn(this.fqcn).setLoggerName(this.loggerName).setMarker(this.marker).setMessage(this.getNonNullImmutableMessage()).setNanoTime(this.nanoTime).setSource(this.location).setThreadId(this.threadId).setThreadName(this.threadName).setThreadPriority(this.threadPriority).setThrown(this.getThrown()).setThrownProxy(this.thrownProxy).setTimeMillis(this.currentTimeMillis);
    }
    
    static {
        FACTORY = new Factory();
        EMPTY = new SimpleMessage("");
    }
    
    private static class Factory implements EventFactory<RingBufferLogEvent>
    {
        public RingBufferLogEvent newInstance() {
            final RingBufferLogEvent result = new RingBufferLogEvent();
            if (Constants.ENABLE_THREADLOCALS) {
                result.messageText = new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE);
                result.parameters = new Object[10];
            }
            return result;
        }
    }
}
