// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.unix;

import java.nio.channels.NotYetConnectedException;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.EmptyArrays;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.channels.AlreadyConnectedException;
import java.net.NoRouteToHostException;
import java.nio.channels.ConnectionPendingException;

public final class Errors
{
    public static final int ERRNO_ENOTCONN_NEGATIVE;
    public static final int ERRNO_EBADF_NEGATIVE;
    public static final int ERRNO_EPIPE_NEGATIVE;
    public static final int ERRNO_ECONNRESET_NEGATIVE;
    public static final int ERRNO_EAGAIN_NEGATIVE;
    public static final int ERRNO_EWOULDBLOCK_NEGATIVE;
    public static final int ERRNO_EINPROGRESS_NEGATIVE;
    public static final int ERROR_ECONNREFUSED_NEGATIVE;
    public static final int ERROR_EISCONN_NEGATIVE;
    public static final int ERROR_EALREADY_NEGATIVE;
    public static final int ERROR_ENETUNREACH_NEGATIVE;
    private static final String[] ERRORS;
    
    static void throwConnectException(final String method, final NativeConnectException refusedCause, final int err) throws IOException {
        if (err == refusedCause.expectedErr()) {
            throw refusedCause;
        }
        if (err == Errors.ERROR_EALREADY_NEGATIVE) {
            throw new ConnectionPendingException();
        }
        if (err == Errors.ERROR_ENETUNREACH_NEGATIVE) {
            throw new NoRouteToHostException();
        }
        if (err == Errors.ERROR_EISCONN_NEGATIVE) {
            throw new AlreadyConnectedException();
        }
        throw new ConnectException(method + "(..) failed: " + Errors.ERRORS[-err]);
    }
    
    public static NativeIoException newConnectionResetException(final String method, final int errnoNegative) {
        final NativeIoException exception = newIOException(method, errnoNegative);
        exception.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        return exception;
    }
    
    public static NativeIoException newIOException(final String method, final int err) {
        return new NativeIoException(method, err);
    }
    
    public static int ioResult(final String method, final int err, final NativeIoException resetCause, final ClosedChannelException closedCause) throws IOException {
        if (err == Errors.ERRNO_EAGAIN_NEGATIVE || err == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return 0;
        }
        if (err == resetCause.expectedErr()) {
            throw resetCause;
        }
        if (err == Errors.ERRNO_EBADF_NEGATIVE) {
            throw closedCause;
        }
        if (err == Errors.ERRNO_ENOTCONN_NEGATIVE) {
            throw new NotYetConnectedException();
        }
        throw newIOException(method, err);
    }
    
    private Errors() {
    }
    
    static {
        ERRNO_ENOTCONN_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoENOTCONN();
        ERRNO_EBADF_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEBADF();
        ERRNO_EPIPE_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEPIPE();
        ERRNO_ECONNRESET_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoECONNRESET();
        ERRNO_EAGAIN_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEAGAIN();
        ERRNO_EWOULDBLOCK_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEWOULDBLOCK();
        ERRNO_EINPROGRESS_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEINPROGRESS();
        ERROR_ECONNREFUSED_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errorECONNREFUSED();
        ERROR_EISCONN_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errorEISCONN();
        ERROR_EALREADY_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errorEALREADY();
        ERROR_ENETUNREACH_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errorENETUNREACH();
        ERRORS = new String[512];
        for (int i = 0; i < Errors.ERRORS.length; ++i) {
            Errors.ERRORS[i] = ErrorsStaticallyReferencedJniMethods.strError(i);
        }
    }
    
    public static final class NativeIoException extends IOException
    {
        private static final long serialVersionUID = 8222160204268655526L;
        private final int expectedErr;
        
        public NativeIoException(final String method, final int expectedErr) {
            super(method + "(..) failed: " + Errors.ERRORS[-expectedErr]);
            this.expectedErr = expectedErr;
        }
        
        public int expectedErr() {
            return this.expectedErr;
        }
    }
    
    static final class NativeConnectException extends ConnectException
    {
        private static final long serialVersionUID = -5532328671712318161L;
        private final int expectedErr;
        
        NativeConnectException(final String method, final int expectedErr) {
            super(method + "(..) failed: " + Errors.ERRORS[-expectedErr]);
            this.expectedErr = expectedErr;
        }
        
        int expectedErr() {
            return this.expectedErr;
        }
    }
}
