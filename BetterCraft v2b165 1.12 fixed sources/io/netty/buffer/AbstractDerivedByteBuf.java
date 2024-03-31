// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.ByteBuffer;

@Deprecated
public abstract class AbstractDerivedByteBuf extends AbstractByteBuf
{
    protected AbstractDerivedByteBuf(final int maxCapacity) {
        super(maxCapacity);
    }
    
    @Override
    public final int refCnt() {
        return this.refCnt0();
    }
    
    int refCnt0() {
        return this.unwrap().refCnt();
    }
    
    @Override
    public final ByteBuf retain() {
        return this.retain0();
    }
    
    ByteBuf retain0() {
        this.unwrap().retain();
        return this;
    }
    
    @Override
    public final ByteBuf retain(final int increment) {
        return this.retain0(increment);
    }
    
    ByteBuf retain0(final int increment) {
        this.unwrap().retain(increment);
        return this;
    }
    
    @Override
    public final ByteBuf touch() {
        return this.touch0();
    }
    
    ByteBuf touch0() {
        this.unwrap().touch();
        return this;
    }
    
    @Override
    public final ByteBuf touch(final Object hint) {
        return this.touch0(hint);
    }
    
    ByteBuf touch0(final Object hint) {
        this.unwrap().touch(hint);
        return this;
    }
    
    @Override
    public final boolean release() {
        return this.release0();
    }
    
    boolean release0() {
        return this.unwrap().release();
    }
    
    @Override
    public final boolean release(final int decrement) {
        return this.release0(decrement);
    }
    
    boolean release0(final int decrement) {
        return this.unwrap().release(decrement);
    }
    
    @Override
    public boolean isReadOnly() {
        return this.unwrap().isReadOnly();
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        return this.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        return this.unwrap().nioBuffer(index, length);
    }
}
