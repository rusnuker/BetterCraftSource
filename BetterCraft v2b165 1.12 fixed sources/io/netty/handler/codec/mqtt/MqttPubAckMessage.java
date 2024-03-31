// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

public final class MqttPubAckMessage extends MqttMessage
{
    public MqttPubAckMessage(final MqttFixedHeader mqttFixedHeader, final MqttMessageIdVariableHeader variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }
    
    @Override
    public MqttMessageIdVariableHeader variableHeader() {
        return (MqttMessageIdVariableHeader)super.variableHeader();
    }
}
