// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.io.PrintStream;

public class TypeConstraintException extends RuntimeException
{
    private String errorCode;
    private volatile Throwable linkedException;
    static final long serialVersionUID = -3059799699420143848L;
    
    public TypeConstraintException(final String message) {
        this(message, null, null);
    }
    
    public TypeConstraintException(final String message, final String errorCode) {
        this(message, errorCode, null);
    }
    
    public TypeConstraintException(final Throwable exception) {
        this(null, null, exception);
    }
    
    public TypeConstraintException(final String message, final Throwable exception) {
        this(message, null, exception);
    }
    
    public TypeConstraintException(final String message, final String errorCode, final Throwable exception) {
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
        if (this.linkedException != null) {
            this.linkedException.printStackTrace(s);
            s.println("--------------- linked to ------------------");
        }
        super.printStackTrace(s);
    }
    
    @Override
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }
}
