// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.handler.codec.CodecException;

public final class RedisCodecException extends CodecException
{
    private static final long serialVersionUID = 5570454251549268063L;
    
    public RedisCodecException(final String message) {
        super(message);
    }
    
    public RedisCodecException(final Throwable cause) {
        super(cause);
    }
}
