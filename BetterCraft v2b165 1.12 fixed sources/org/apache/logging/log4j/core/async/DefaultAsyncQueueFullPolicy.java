// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;

public class DefaultAsyncQueueFullPolicy implements AsyncQueueFullPolicy
{
    @Override
    public EventRoute getRoute(final long backgroundThreadId, final Level level) {
        return EventRoute.SYNCHRONOUS;
    }
}
