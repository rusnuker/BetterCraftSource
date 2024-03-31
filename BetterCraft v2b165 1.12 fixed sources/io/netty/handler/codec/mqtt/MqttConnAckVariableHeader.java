// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;

public final class MqttConnAckVariableHeader
{
    private final MqttConnectReturnCode connectReturnCode;
    private final boolean sessionPresent;
    
    public MqttConnAckVariableHeader(final MqttConnectReturnCode connectReturnCode, final boolean sessionPresent) {
        this.connectReturnCode = connectReturnCode;
        this.sessionPresent = sessionPresent;
    }
    
    public MqttConnectReturnCode connectReturnCode() {
        return this.connectReturnCode;
    }
    
    public boolean isSessionPresent() {
        return this.sessionPresent;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "connectReturnCode=" + this.connectReturnCode + ", sessionPresent=" + this.sessionPresent + ']';
    }
}
