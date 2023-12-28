// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;

public enum MqttVersion
{
    MQTT_3_1("MQIsdp", (byte)3), 
    MQTT_3_1_1("MQTT", (byte)4);
    
    private final String name;
    private final byte level;
    
    private MqttVersion(final String protocolName, final byte protocolLevel) {
        this.name = ObjectUtil.checkNotNull(protocolName, "protocolName");
        this.level = protocolLevel;
    }
    
    public String protocolName() {
        return this.name;
    }
    
    public byte[] protocolNameBytes() {
        return this.name.getBytes(CharsetUtil.UTF_8);
    }
    
    public byte protocolLevel() {
        return this.level;
    }
    
    public static MqttVersion fromProtocolNameAndLevel(final String protocolName, final byte protocolLevel) {
        final MqttVersion[] values = values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final MqttVersion mv = values[i];
            if (mv.name.equals(protocolName)) {
                if (mv.level == protocolLevel) {
                    return mv;
                }
                throw new MqttUnacceptableProtocolVersionException(protocolName + " and " + protocolLevel + " are not match");
            }
            else {
                ++i;
            }
        }
        throw new MqttUnacceptableProtocolVersionException(protocolName + "is unknown protocol name");
    }
}
