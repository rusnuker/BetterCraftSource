// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.io.PrintWriter;
import java.io.PrintStream;

public class JAXBException extends Exception
{
    private String errorCode;
    private volatile Throwable linkedException;
    static final long serialVersionUID = -5621384651494307979L;
    
    public JAXBException(final String message) {
        this(message, null, null);
    }
    
    public JAXBException(final String message, final String errorCode) {
        this(message, errorCode, null);
    }
    
    public JAXBException(final Throwable exception) {
        this(null, null, exception);
    }
    
    public JAXBException(final String message, final Throwable exception) {
        this(message, null, exception);
    }
    
    public JAXBException(final String message, final String errorCode, final Throwable exception) {
        super(message);
        this.errorCode = errorCode;
        this.linkedException = exception;
    }
    
    public String getErrorCode() {
        return this.errorCode;
    }
    
    public Throwable getLinkedException() {
        return this.linkedException;
    }
    
    public void setLinkedException(final Throwable exception) {
        this.linkedException = exception;
    }
    
    @Override
    public String toString() {
        return (this.linkedException == null) ? super.toString() : (String.valueOf(super.toString()) + "\n - with linked exception:\n[" + this.linkedException.toString() + "]");
    }
    
    @Override
    public void printStackTrace(final PrintStream s) {
        super.printStackTrace(s);
    }
    
    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
    
    @Override
    public void printStackTrace(final PrintWriter s) {
        super.printStackTrace(s);
    }
    
    @Override
    public Throwable getCause() {
        return this.linkedException;
    }
}
