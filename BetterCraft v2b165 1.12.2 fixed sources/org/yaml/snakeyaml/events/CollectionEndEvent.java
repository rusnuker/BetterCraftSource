// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public abstract class CollectionEndEvent extends Event
{
    public CollectionEndEvent(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
}
