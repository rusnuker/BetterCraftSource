// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.ObjectUtil;

public final class MqttFixedHeader
{
    private final MqttMessageType messageType;
    private final boolean isDup;
    private final MqttQoS qosLevel;
    private final boolean isRetain;
    private final int remainingLength;
    
    public MqttFixedHeader(final MqttMessageType messageType, final boolean isDup, final MqttQoS qosLevel, final boolean isRetain, final int remainingLength) {
        this.messageType = ObjectUtil.checkNotNull(messageType, "messageType");
        this.isDup = isDup;
        this.qosLevel = ObjectUtil.checkNotNull(qosLevel, "qosLevel");
        this.isRetain = isRetain;
        this.remainingLength = remainingLength;
    }
    
    public MqttMessageType messageType() {
        return this.messageType;
    }
    
    public boolean isDup() {
        return this.isDup;
    }
    
    public MqttQoS qosLevel() {
        return this.qosLevel;
    }
    
    public boolean isRetain() {
        return this.isRetain;
    }
    
    public int remainingLength() {
        return this.remainingLength;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "messageType=" + this.messageType + ", isDup=" + this.isDup + ", qosLevel=" + this.qosLevel + ", isRetain=" + this.isRetain + ", remainingLength=" + this.remainingLength + ']';
    }
}
