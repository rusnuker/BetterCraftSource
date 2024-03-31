// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.handler.codec.DecoderException;

public final class MqttIdentifierRejectedException extends DecoderException
{
    private static final long serialVersionUID = -1323503322689614981L;
    
    public MqttIdentifierRejectedException() {
    }
    
    public MqttIdentifierRejectedException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MqttIdentifierRejectedException(final String message) {
        super(message);
    }
    
    public MqttIdentifierRejectedException(final Throwable cause) {
        super(cause);
    }
}
