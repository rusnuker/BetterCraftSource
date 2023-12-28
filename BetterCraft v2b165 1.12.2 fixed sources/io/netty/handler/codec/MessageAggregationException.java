// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

public class MessageAggregationException extends IllegalStateException
{
    private static final long serialVersionUID = -1995826182950310255L;
    
    public MessageAggregationException() {
    }
    
    public MessageAggregationException(final String s) {
        super(s);
    }
    
    public MessageAggregationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MessageAggregationException(final Throwable cause) {
        super(cause);
    }
}
