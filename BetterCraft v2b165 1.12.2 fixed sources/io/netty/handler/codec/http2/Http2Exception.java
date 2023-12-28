// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import io.netty.util.internal.ObjectUtil;

public class Http2Exception extends Exception
{
    private static final long serialVersionUID = -6941186345430164209L;
    private final Http2Error error;
    private final ShutdownHint shutdownHint;
    
    public Http2Exception(final Http2Error error) {
        this(error, ShutdownHint.HARD_SHUTDOWN);
    }
    
    public Http2Exception(final Http2Error error, final ShutdownHint shutdownHint) {
        this.error = ObjectUtil.checkNotNull(error, "error");
        this.shutdownHint = ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }
    
    public Http2Exception(final Http2Error error, final String message) {
        this(error, message, ShutdownHint.HARD_SHUTDOWN);
    }
    
    public Http2Exception(final Http2Error error, final String message, final ShutdownHint shutdownHint) {
        super(message);
        this.error = ObjectUtil.checkNotNull(error, "error");
        this.shutdownHint = ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }
    
    public Http2Exception(final Http2Error error, final String message, final Throwable cause) {
        this(error, message, cause, ShutdownHint.HARD_SHUTDOWN);
    }
    
    public Http2Exception(final Http2Error error, final String message, final Throwable cause, final ShutdownHint shutdownHint) {
        super(message, cause);
        this.error = ObjectUtil.checkNotNull(error, "error");
        this.shutdownHint = ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }
    
    public Http2Error error() {
        return this.error;
    }
    
    public ShutdownHint shutdownHint() {
        return this.shutdownHint;
    }
    
    public static Http2Exception connectionError(final Http2Error error, final String fmt, final Object... args) {
        return new Http2Exception(error, String.format(fmt, args));
    }
    
    public static Http2Exception connectionError(final Http2Error error, final Throwable cause, final String fmt, final Object... args) {
        return new Http2Exception(error, String.format(fmt, args), cause);
    }
    
    public static Http2Exception closedStreamError(final Http2Error error, final String fmt, final Object... args) {
        return new ClosedStreamCreationException(error, String.format(fmt, args));
    }
    
    public static Http2Exception streamError(final int id, final Http2Error error, final String fmt, final Object... args) {
        return (0 == id) ? connectionError(error, fmt, args) : new StreamException(id, error, String.format(fmt, args));
    }
    
    public static Http2Exception streamError(final int id, final Http2Error error, final Throwable cause, final String fmt, final Object... args) {
        return (0 == id) ? connectionError(error, cause, fmt, args) : new StreamException(id, error, String.format(fmt, args), cause);
    }
    
    public static Http2Exception headerListSizeError(final int id, final Http2Error error, final boolean onDecode, final String fmt, final Object... args) {
        return (0 == id) ? connectionError(error, fmt, args) : new HeaderListSizeException(id, error, String.format(fmt, args), onDecode);
    }
    
    public static boolean isStreamError(final Http2Exception e) {
        return e instanceof StreamException;
    }
    
    public static int streamId(final Http2Exception e) {
        return isStreamError(e) ? ((StreamException)e).streamId() : 0;
    }
    
    public enum ShutdownHint
    {
        NO_SHUTDOWN, 
        GRACEFUL_SHUTDOWN, 
        HARD_SHUTDOWN;
    }
    
    public static final class ClosedStreamCreationException extends Http2Exception
    {
        private static final long serialVersionUID = -6746542974372246206L;
        
        public ClosedStreamCreationException(final Http2Error error) {
            super(error);
        }
        
        public ClosedStreamCreationException(final Http2Error error, final String message) {
            super(error, message);
        }
        
        public ClosedStreamCreationException(final Http2Error error, final String message, final Throwable cause) {
            super(error, message, cause);
        }
    }
    
    public static class StreamException extends Http2Exception
    {
        private static final long serialVersionUID = 602472544416984384L;
        private final int streamId;
        
        StreamException(final int streamId, final Http2Error error, final String message) {
            super(error, message, ShutdownHint.NO_SHUTDOWN);
            this.streamId = streamId;
        }
        
        StreamException(final int streamId, final Http2Error error, final String message, final Throwable cause) {
            super(error, message, cause, ShutdownHint.NO_SHUTDOWN);
            this.streamId = streamId;
        }
        
        public int streamId() {
            return this.streamId;
        }
    }
    
    public static final class HeaderListSizeException extends StreamException
    {
        private final boolean decode;
        
        HeaderListSizeException(final int streamId, final Http2Error error, final String message, final boolean decode) {
            super(streamId, error, message);
            this.decode = decode;
        }
        
        public boolean duringDecode() {
            return this.decode;
        }
    }
    
    public static final class CompositeStreamException extends Http2Exception implements Iterable<StreamException>
    {
        private static final long serialVersionUID = 7091134858213711015L;
        private final List<StreamException> exceptions;
        
        public CompositeStreamException(final Http2Error error, final int initialCapacity) {
            super(error, ShutdownHint.NO_SHUTDOWN);
            this.exceptions = new ArrayList<StreamException>(initialCapacity);
        }
        
        public void add(final StreamException e) {
            this.exceptions.add(e);
        }
        
        @Override
        public Iterator<StreamException> iterator() {
            return this.exceptions.iterator();
        }
    }
}
