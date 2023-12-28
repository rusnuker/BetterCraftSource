// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

public final class HttpExpectationFailedEvent
{
    public static final HttpExpectationFailedEvent INSTANCE;
    
    private HttpExpectationFailedEvent() {
    }
    
    static {
        INSTANCE = new HttpExpectationFailedEvent();
    }
}
