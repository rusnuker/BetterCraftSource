// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.internal.ObjectUtil;

public abstract class AbstractStringRedisMessage implements RedisMessage
{
    private final String content;
    
    AbstractStringRedisMessage(final String content) {
        this.content = ObjectUtil.checkNotNull(content, "content");
    }
    
    public final String content() {
        return this.content;
    }
}
