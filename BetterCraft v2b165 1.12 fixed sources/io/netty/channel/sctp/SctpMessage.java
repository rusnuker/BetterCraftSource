// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.sctp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import com.sun.nio.sctp.MessageInfo;
import io.netty.buffer.DefaultByteBufHolder;

public final class SctpMessage extends DefaultByteBufHolder
{
    private final int streamIdentifier;
    private final int protocolIdentifier;
    private final boolean unordered;
    private final MessageInfo msgInfo;
    
    public SctpMessage(final int protocolIdentifier, final int streamIdentifier, final ByteBuf payloadBuffer) {
        this(protocolIdentifier, streamIdentifier, false, payloadBuffer);
    }
    
    public SctpMessage(final int protocolIdentifier, final int streamIdentifier, final boolean unordered, final ByteBuf payloadBuffer) {
        super(payloadBuffer);
        this.protocolIdentifier = protocolIdentifier;
        this.streamIdentifier = streamIdentifier;
        this.unordered = unordered;
        this.msgInfo = null;
    }
    
    public SctpMessage(final MessageInfo msgInfo, final ByteBuf payloadBuffer) {
        super(payloadBuffer);
        if (msgInfo == null) {
            throw new NullPointerException("msgInfo");
        }
        this.msgInfo = msgInfo;
        this.streamIdentifier = msgInfo.streamNumber();
        this.protocolIdentifier = msgInfo.payloadProtocolID();
        this.unordered = msgInfo.isUnordered();
    }
    
    public int streamIdentifier() {
        return this.streamIdentifier;
    }
    
    public int protocolIdentifier() {
        return this.protocolIdentifier;
    }
    
    public boolean isUnordered() {
        return this.unordered;
    }
    
    public MessageInfo messageInfo() {
        return this.msgInfo;
    }
    
    public boolean isComplete() {
        return this.msgInfo == null || this.msgInfo.isComplete();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SctpMessage sctpFrame = (SctpMessage)o;
        return this.protocolIdentifier == sctpFrame.protocolIdentifier && this.streamIdentifier == sctpFrame.streamIdentifier && this.unordered == sctpFrame.unordered && this.content().equals(sctpFrame.content());
    }
    
    @Override
    public int hashCode() {
        int result = this.streamIdentifier;
        result = 31 * result + this.protocolIdentifier;
        result = 31 * result + (this.unordered ? 1231 : 1237);
        result = 31 * result + this.content().hashCode();
        return result;
    }
    
    @Override
    public SctpMessage copy() {
        return (SctpMessage)super.copy();
    }
    
    @Override
    public SctpMessage duplicate() {
        return (SctpMessage)super.duplicate();
    }
    
    @Override
    public SctpMessage retainedDuplicate() {
        return (SctpMessage)super.retainedDuplicate();
    }
    
    @Override
    public SctpMessage replace(final ByteBuf content) {
        if (this.msgInfo == null) {
            return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.unordered, content);
        }
        return new SctpMessage(this.msgInfo, content);
    }
    
    @Override
    public SctpMessage retain() {
        super.retain();
        return this;
    }
    
    @Override
    public SctpMessage retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public SctpMessage touch() {
        super.touch();
        return this;
    }
    
    @Override
    public SctpMessage touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    @Override
    public String toString() {
        return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", unordered=" + this.unordered + ", data=" + this.contentToString() + '}';
    }
}
