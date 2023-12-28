// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

public class PropertyException extends JAXBException
{
    public PropertyException(final String message) {
        super(message);
    }
    
    public PropertyException(final String message, final String errorCode) {
        super(message, errorCode);
    }
    
    public PropertyException(final Throwable exception) {
        super(exception);
    }
    
    public PropertyException(final String message, final Throwable exception) {
        super(message, exception);
    }
    
    public PropertyException(final String message, final String errorCode, final Throwable exception) {
        super(message, errorCode, exception);
    }
    
    public PropertyException(final String name, final Object value) {
        super(Messages.format("PropertyException.NameValue", name, value.toString()));
    }
}
