// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.Collections;
import java.util.List;
import io.netty.util.AbstractReferenceCounted;

public class ArrayRedisMessage extends AbstractReferenceCounted implements RedisMessage
{
    private final List<RedisMessage> children;
    public static final ArrayRedisMessage NULL_INSTANCE;
    public static final ArrayRedisMessage EMPTY_INSTANCE;
    
    private ArrayRedisMessage() {
        this.children = Collections.emptyList();
    }
    
    public ArrayRedisMessage(final List<RedisMessage> children) {
        this.children = ObjectUtil.checkNotNull(children, "children");
    }
    
    public final List<RedisMessage> children() {
        return this.children;
    }
    
    public boolean isNull() {
        return false;
    }
    
    @Override
    protected void deallocate() {
        for (final RedisMessage msg : this.children) {
            ReferenceCountUtil.release(msg);
        }
    }
    
    @Override
    public ArrayRedisMessage touch(final Object hint) {
        for (final RedisMessage msg : this.children) {
            ReferenceCountUtil.touch(msg);
        }
        return this;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "children=" + this.children.size() + ']';
    }
    
    static {
        NULL_INSTANCE = new ArrayRedisMessage() {
            @Override
            public boolean isNull() {
                return true;
            }
            
            @Override
            public ArrayRedisMessage retain() {
                return this;
            }
            
            @Override
            public ArrayRedisMessage retain(final int increment) {
                return this;
            }
            
            @Override
            public ArrayRedisMessage touch() {
                return this;
            }
            
            @Override
            public ArrayRedisMessage touch(final Object hint) {
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
            
            @Override
            public String toString() {
                return "NullArrayRedisMessage";
            }
        };
        EMPTY_INSTANCE = new ArrayRedisMessage() {
            @Override
            public boolean isNull() {
                return false;
            }
            
            @Override
            public ArrayRedisMessage retain() {
                return this;
            }
            
            @Override
            public ArrayRedisMessage retain(final int increment) {
                return this;
            }
            
            @Override
            public ArrayRedisMessage touch() {
                return this;
            }
            
            @Override
            public ArrayRedisMessage touch(final Object hint) {
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
            
            @Override
            public String toString() {
                return "EmptyArrayRedisMessage";
            }
        };
    }
}
