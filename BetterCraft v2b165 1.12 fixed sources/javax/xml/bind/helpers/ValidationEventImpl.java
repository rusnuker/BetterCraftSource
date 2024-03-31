// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.helpers;

import java.text.MessageFormat;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.ValidationEvent;

public class ValidationEventImpl implements ValidationEvent
{
    private int severity;
    private String message;
    private Throwable linkedException;
    private ValidationEventLocator locator;
    
    public ValidationEventImpl(final int _severity, final String _message, final ValidationEventLocator _locator) {
        this(_severity, _message, _locator, null);
    }
    
    public ValidationEventImpl(final int _severity, final String _message, final ValidationEventLocator _locator, final Throwable _linkedException) {
        this.setSeverity(_severity);
        this.message = _message;
        this.locator = _locator;
        this.linkedException = _linkedException;
    }
    
    @Override
    public int getSeverity() {
        return this.severity;
    }
    
    public void setSeverity(final int _severity) {
        if (_severity != 0 && _severity != 1 && _severity != 2) {
            throw new IllegalArgumentException(Messages.format("ValidationEventImpl.IllegalSeverity"));
        }
        this.severity = _severity;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String _message) {
        this.message = _message;
    }
    
    @Override
    public Throwable getLinkedException() {
        return this.linkedException;
    }
    
    public void setLinkedException(final Throwable _linkedException) {
        this.linkedException = _linkedException;
    }
    
    @Override
    public ValidationEventLocator getLocator() {
        return this.locator;
    }
    
    public void setLocator(final ValidationEventLocator _locator) {
        this.locator = _locator;
    }
    
    @Override
    public String toString() {
        String s = null;
        switch (this.getSeverity()) {
            case 0: {
                s = "WARNING";
                break;
            }
            case 1: {
                s = "ERROR";
                break;
            }
            case 2: {
                s = "FATAL_ERROR";
                break;
            }
            default: {
                s = String.valueOf(this.getSeverity());
                break;
            }
        }
        return MessageFormat.format("[severity={0},message={1},locator={2}]", s, this.getMessage(), this.getLocator());
    }
}
