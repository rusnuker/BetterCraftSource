// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.helpers;

import java.text.MessageFormat;
import java.util.ResourceBundle;

class Messages
{
    static final String INPUTSTREAM_NOT_NULL = "AbstractUnmarshallerImpl.ISNotNull";
    static final String MUST_BE_BOOLEAN = "AbstractMarshallerImpl.MustBeBoolean";
    static final String MUST_BE_STRING = "AbstractMarshallerImpl.MustBeString";
    static final String SEVERITY_MESSAGE = "DefaultValidationEventHandler.SeverityMessage";
    static final String LOCATION_UNAVAILABLE = "DefaultValidationEventHandler.LocationUnavailable";
    static final String UNRECOGNIZED_SEVERITY = "DefaultValidationEventHandler.UnrecognizedSeverity";
    static final String WARNING = "DefaultValidationEventHandler.Warning";
    static final String ERROR = "DefaultValidationEventHandler.Error";
    static final String FATAL_ERROR = "DefaultValidationEventHandler.FatalError";
    static final String ILLEGAL_SEVERITY = "ValidationEventImpl.IllegalSeverity";
    static final String MUST_NOT_BE_NULL = "Shared.MustNotBeNull";
    
    static String format(final String property) {
        return format(property, null);
    }
    
    static String format(final String property, final Object arg1) {
        return format(property, new Object[] { arg1 });
    }
    
    static String format(final String property, final Object arg1, final Object arg2) {
        return format(property, new Object[] { arg1, arg2 });
    }
    
    static String format(final String property, final Object arg1, final Object arg2, final Object arg3) {
        return format(property, new Object[] { arg1, arg2, arg3 });
    }
    
    static String format(final String property, final Object[] args) {
        final String text = ResourceBundle.getBundle(Messages.class.getName()).getString(property);
        return MessageFormat.format(text, args);
    }
}
