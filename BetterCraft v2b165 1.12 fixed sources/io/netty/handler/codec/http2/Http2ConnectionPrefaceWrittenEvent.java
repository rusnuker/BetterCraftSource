// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

public final class Http2ConnectionPrefaceWrittenEvent
{
    static final Http2ConnectionPrefaceWrittenEvent INSTANCE;
    
    private Http2ConnectionPrefaceWrittenEvent() {
    }
    
    static {
        INSTANCE = new Http2ConnectionPrefaceWrittenEvent();
    }
}
