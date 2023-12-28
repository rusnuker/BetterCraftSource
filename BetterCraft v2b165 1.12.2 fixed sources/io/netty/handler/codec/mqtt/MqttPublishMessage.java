// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.ReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public class MqttPublishMessage extends MqttMessage implements ByteBufHolder
{
    public MqttPublishMessage(final MqttFixedHeader mqttFixedHeader, final MqttPublishVariableHeader variableHeader, final ByteBuf payload) {
        super(mqttFixedHeader, variableHeader, payload);
    }
    
    @Override
    public MqttPublishVariableHeader variableHeader() {
        return (MqttPublishVariableHeader)super.variableHeader();
    }
    
    @Override
    public ByteBuf payload() {
        return this.content();
    }
    
    @Override
    public ByteBuf content() {
        final ByteBuf data = (ByteBuf)super.payload();
        if (data.refCnt() <= 0) {
            throw new IllegalReferenceCountException(data.refCnt());
        }
        return data;
    }
    
    @Override
    public MqttPublishMessage copy() {
        return this.replace(this.content().copy());
    }
    
    @Override
    public MqttPublishMessage duplicate() {
        return this.replace(this.content().duplicate());
    }
    
    @Override
    public MqttPublishMessage retainedDuplicate() {
        return this.replace(this.content().retainedDuplicate());
    }
    
    @Override
    public MqttPublishMessage replace(final ByteBuf content) {
        return new MqttPublishMessage(this.fixedHeader(), this.variableHeader(), content);
    }
    
    @Override
    public int refCnt() {
        return this.content().refCnt();
    }
    
    @Override
    public MqttPublishMessage retain() {
        this.content().retain();
        return this;
    }
    
    @Override
    public MqttPublishMessage retain(final int increment) {
        this.content().retain(increment);
        return this;
    }
    
    @Override
    public MqttPublishMessage touch() {
        this.content().touch();
        return this;
    }
    
    @Override
    public MqttPublishMessage touch(final Object hint) {
        this.content().touch(hint);
        return this;
    }
    
    @Override
    public boolean release() {
        return this.content().release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.content().release(decrement);
    }
}
