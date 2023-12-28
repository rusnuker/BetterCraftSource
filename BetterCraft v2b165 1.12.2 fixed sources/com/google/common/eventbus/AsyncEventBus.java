// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.eventbus;

import java.util.concurrent.Executor;
import com.google.common.annotations.Beta;

@Beta
public class AsyncEventBus extends EventBus
{
    public AsyncEventBus(final String identifier, final Executor executor) {
        super(identifier, executor, Dispatcher.legacyAsync(), LoggingHandler.INSTANCE);
    }
    
    public AsyncEventBus(final Executor executor, final SubscriberExceptionHandler subscriberExceptionHandler) {
        super("default", executor, Dispatcher.legacyAsync(), subscriberExceptionHandler);
    }
    
    public AsyncEventBus(final Executor executor) {
        super("default", executor, Dispatcher.legacyAsync(), LoggingHandler.INSTANCE);
    }
}
