// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

public final class MqttConnectMessage extends MqttMessage
{
    public MqttConnectMessage(final MqttFixedHeader mqttFixedHeader, final MqttConnectVariableHeader variableHeader, final MqttConnectPayload payload) {
        super(mqttFixedHeader, variableHeader, payload);
    }
    
    @Override
    public MqttConnectVariableHeader variableHeader() {
        return (MqttConnectVariableHeader)super.variableHeader();
    }
    
    @Override
    public MqttConnectPayload payload() {
        return (MqttConnectPayload)super.payload();
    }
}
