// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.helpers;

import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.ParseConversionEvent;

public class ParseConversionEventImpl extends ValidationEventImpl implements ParseConversionEvent
{
    public ParseConversionEventImpl(final int _severity, final String _message, final ValidationEventLocator _locator) {
        super(_severity, _message, _locator);
    }
    
    public ParseConversionEventImpl(final int _severity, final String _message, final ValidationEventLocator _locator, final Throwable _linkedException) {
        super(_severity, _message, _locator, _linkedException);
    }
}
