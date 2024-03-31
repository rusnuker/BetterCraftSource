// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.DefaultByteBufHolder;

public class FullBulkStringRedisMessage extends DefaultByteBufHolder implements LastBulkStringRedisContent
{
    public static final FullBulkStringRedisMessage NULL_INSTANCE;
    public static final FullBulkStringRedisMessage EMPTY_INSTANCE;
    
    private FullBulkStringRedisMessage() {
        this(Unpooled.EMPTY_BUFFER);
    }
    
    public FullBulkStringRedisMessage(final ByteBuf content) {
        super(content);
    }
    
    public boolean isNull() {
        return false;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "content=" + this.content() + ']';
    }
    
    @Override
    public FullBulkStringRedisMessage copy() {
        return (FullBulkStringRedisMessage)super.copy();
    }
    
    @Override
    public FullBulkStringRedisMessage duplicate() {
        return (FullBulkStringRedisMessage)super.duplicate();
    }
    
    @Override
    public FullBulkStringRedisMessage retainedDuplicate() {
        return (FullBulkStringRedisMessage)super.retainedDuplicate();
    }
    
    @Override
    public FullBulkStringRedisMessage replace(final ByteBuf content) {
        return new FullBulkStringRedisMessage(content);
    }
    
    @Override
    public FullBulkStringRedisMessage retain() {
        super.retain();
        return this;
    }
    
    @Override
    public FullBulkStringRedisMessage retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public FullBulkStringRedisMessage touch() {
        super.touch();
        return this;
    }
    
    @Override
    public FullBulkStringRedisMessage touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    static {
        NULL_INSTANCE = new FullBulkStringRedisMessage() {
            @Override
            public boolean isNull() {
                return true;
            }
            
            @Override
            public ByteBuf content() {
                return Unpooled.EMPTY_BUFFER;
            }
            
            @Override
            public FullBulkStringRedisMessage copy() {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage duplicate() {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage retainedDuplicate() {
                return this;
            }
            
            @Override
            public int refCnt() {
                return 1;
            }
            
            @Override
            public FullBulkStringRedisMessage retain() {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage retain(final int increment) {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage touch() {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage touch(final Object hint) {
                return this;
            }
            
            @Override
            public boolean release() {
                return false;
            }
            
            @Override
            public boolean release(final int decrement) {
                return false;
            }
        };
        EMPTY_INSTANCE = new FullBulkStringRedisMessage() {
            @Override
            public ByteBuf content() {
                return Unpooled.EMPTY_BUFFER;
            }
            
            @Override
            public FullBulkStringRedisMessage copy() {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage duplicate() {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage retainedDuplicate() {
                return this;
            }
            
            @Override
            public int refCnt() {
                return 1;
            }
            
            @Override
            public FullBulkStringRedisMessage retain() {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage retain(final int increment) {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage touch() {
                return this;
            }
            
            @Override
            public FullBulkStringRedisMessage touch(final Object hint) {
                return this;
            }
            
            @Override
            public boolean release() {
                return false;
            }
            
            @Override
            public boolean release(final int decrement) {
                return false;
            }
        };
    }
}
