// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.util;

import java.util.ArrayList;
import javax.xml.bind.ValidationEvent;
import java.util.List;
import javax.xml.bind.ValidationEventHandler;

public class ValidationEventCollector implements ValidationEventHandler
{
    private final List<ValidationEvent> events;
    
    public ValidationEventCollector() {
        this.events = new ArrayList<ValidationEvent>();
    }
    
    public ValidationEvent[] getEvents() {
        return this.events.toArray(new ValidationEvent[this.events.size()]);
    }
    
    public void reset() {
        this.events.clear();
    }
    
    public boolean hasEvents() {
        return !this.events.isEmpty();
    }
    
    @Override
    public boolean handleEvent(final ValidationEvent event) {
        this.events.add(event);
        boolean retVal = true;
        switch (event.getSeverity()) {
            case 0: {
                retVal = true;
                break;
            }
            case 1: {
                retVal = true;
                break;
            }
            case 2: {
                retVal = false;
                break;
            }
            default: {
                _assert(false, Messages.format("ValidationEventCollector.UnrecognizedSeverity", event.getSeverity()));
                break;
            }
        }
        return retVal;
    }
    
    private static void _assert(final boolean b, final String msg) {
        if (!b) {
            throw new InternalError(msg);
        }
    }
}
