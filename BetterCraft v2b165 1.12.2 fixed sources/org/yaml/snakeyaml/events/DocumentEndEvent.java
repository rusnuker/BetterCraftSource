// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class DocumentEndEvent extends Event
{
    private final boolean explicit;
    
    public DocumentEndEvent(final Mark startMark, final Mark endMark, final boolean explicit) {
        super(startMark, endMark);
        this.explicit = explicit;
    }
    
    public boolean getExplicit() {
        return this.explicit;
    }
    
    @Override
    public ID getEventId() {
        return ID.DocumentEnd;
    }
}
