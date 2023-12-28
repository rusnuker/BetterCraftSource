// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;

public final class MqttConnectPayload
{
    private final String clientIdentifier;
    private final String willTopic;
    private final String willMessage;
    private final String userName;
    private final String password;
    
    public MqttConnectPayload(final String clientIdentifier, final String willTopic, final String willMessage, final String userName, final String password) {
        this.clientIdentifier = clientIdentifier;
        this.willTopic = willTopic;
        this.willMessage = willMessage;
        this.userName = userName;
        this.password = password;
    }
    
    public String clientIdentifier() {
        return this.clientIdentifier;
    }
    
    public String willTopic() {
        return this.willTopic;
    }
    
    public String willMessage() {
        return this.willMessage;
    }
    
    public String userName() {
        return this.userName;
    }
    
    public String password() {
        return this.password;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "clientIdentifier=" + this.clientIdentifier + ", willTopic=" + this.willTopic + ", willMessage=" + this.willMessage + ", userName=" + this.userName + ", password=" + this.password + ']';
    }
}
