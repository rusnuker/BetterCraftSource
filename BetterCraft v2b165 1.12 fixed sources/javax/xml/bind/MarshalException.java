// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

public class MarshalException extends JAXBException
{
    public MarshalException(final String message) {
        this(message, null, null);
    }
    
    public MarshalException(final String message, final String errorCode) {
        this(message, errorCode, null);
    }
    
    public MarshalException(final Throwable exception) {
        this(null, null, exception);
    }
    
    public MarshalException(final String message, final Throwable exception) {
        this(message, null, exception);
    }
    
    public MarshalException(final String message, final String errorCode, final Throwable exception) {
        super(message, errorCode, exception);
    }
}
