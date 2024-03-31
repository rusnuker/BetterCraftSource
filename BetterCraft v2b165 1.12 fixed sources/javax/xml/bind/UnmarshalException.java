// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

public class UnmarshalException extends JAXBException
{
    public UnmarshalException(final String message) {
        this(message, null, null);
    }
    
    public UnmarshalException(final String message, final String errorCode) {
        this(message, errorCode, null);
    }
    
    public UnmarshalException(final Throwable exception) {
        this(null, null, exception);
    }
    
    public UnmarshalException(final String message, final Throwable exception) {
        this(message, null, exception);
    }
    
    public UnmarshalException(final String message, final String errorCode, final Throwable exception) {
        super(message, errorCode, exception);
    }
}
