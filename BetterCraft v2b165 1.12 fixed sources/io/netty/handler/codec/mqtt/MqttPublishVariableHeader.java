// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;

public final class MqttPublishVariableHeader
{
    private final String topicName;
    private final int messageId;
    
    public MqttPublishVariableHeader(final String topicName, final int messageId) {
        this.topicName = topicName;
        this.messageId = messageId;
    }
    
    public String topicName() {
        return this.topicName;
    }
    
    public int messageId() {
        return this.messageId;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "topicName=" + this.topicName + ", messageId=" + this.messageId + ']';
    }
}
