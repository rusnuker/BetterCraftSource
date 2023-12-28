// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

public class ValidationException extends JAXBException
{
    public ValidationException(final String message) {
        this(message, null, null);
    }
    
    public ValidationException(final String message, final String errorCode) {
        this(message, errorCode, null);
    }
    
    public ValidationException(final Throwable exception) {
        this(null, null, exception);
    }
    
    public ValidationException(final String message, final Throwable exception) {
        this(message, null, exception);
    }
    
    public ValidationException(final String message, final String errorCode, final Throwable exception) {
        super(message, errorCode, exception);
    }
}
