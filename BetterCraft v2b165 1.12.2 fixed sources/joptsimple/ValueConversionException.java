// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

public class ValueConversionException extends RuntimeException
{
    private static final long serialVersionUID = -1L;
    
    public ValueConversionException(final String message) {
        this(message, null);
    }
    
    public ValueConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
