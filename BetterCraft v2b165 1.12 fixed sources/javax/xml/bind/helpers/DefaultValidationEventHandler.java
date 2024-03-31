// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.helpers;

import org.w3c.dom.Node;
import java.net.URL;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class DefaultValidationEventHandler implements ValidationEventHandler
{
    @Override
    public boolean handleEvent(final ValidationEvent event) {
        if (event == null) {
            throw new IllegalArgumentException();
        }
        String severity = null;
        boolean retVal = false;
        switch (event.getSeverity()) {
            case 0: {
                severity = Messages.format("DefaultValidationEventHandler.Warning");
                retVal = true;
                break;
            }
            case 1: {
                severity = Messages.format("DefaultValidationEventHandler.Error");
                retVal = false;
                break;
            }
            case 2: {
                severity = Messages.format("DefaultValidationEventHandler.FatalError");
                retVal = false;
                break;
            }
            default: {
                assert false : Messages.format("DefaultValidationEventHandler.UnrecognizedSeverity", event.getSeverity());
                break;
            }
        }
        final String location = this.getLocation(event);
        System.out.println(Messages.format("DefaultValidationEventHandler.SeverityMessage", severity, event.getMessage(), location));
        return retVal;
    }
    
    private String getLocation(final ValidationEvent event) {
        final StringBuffer msg = new StringBuffer();
        final ValidationEventLocator locator = event.getLocator();
        if (locator != null) {
            final URL url = locator.getURL();
            final Object obj = locator.getObject();
            final Node node = locator.getNode();
            final int line = locator.getLineNumber();
            if (url != null || line != -1) {
                msg.append("line " + line);
                if (url != null) {
                    msg.append(" of " + url);
                }
            }
            else if (obj != null) {
                msg.append(" obj: " + obj.toString());
            }
            else if (node != null) {
                msg.append(" node: " + node.toString());
            }
        }
        else {
            msg.append(Messages.format("DefaultValidationEventHandler.LocationUnavailable"));
        }
        return msg.toString();
    }
}
