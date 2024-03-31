// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.internal.StringUtil;

public final class ErrorRedisMessage extends AbstractStringRedisMessage
{
    public ErrorRedisMessage(final String content) {
        super(content);
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "content=" + this.content() + ']';
    }
}
