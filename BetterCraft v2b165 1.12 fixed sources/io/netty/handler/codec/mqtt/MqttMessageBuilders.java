// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import java.util.ArrayList;
import java.util.List;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public final class MqttMessageBuilders
{
    public static ConnectBuilder connect() {
        return new ConnectBuilder();
    }
    
    public static ConnAckBuilder connAck() {
        return new ConnAckBuilder();
    }
    
    public static PublishBuilder publish() {
        return new PublishBuilder();
    }
    
    public static SubscribeBuilder subscribe() {
        return new SubscribeBuilder();
    }
    
    public static UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder();
    }
    
    private MqttMessageBuilders() {
    }
    
    public static final class PublishBuilder
    {
        private String topic;
        private boolean retained;
        private MqttQoS qos;
        private ByteBuf payload;
        private int messageId;
        
        PublishBuilder() {
        }
        
        public PublishBuilder topicName(final String topic) {
            this.topic = topic;
            return this;
        }
        
        public PublishBuilder retained(final boolean retained) {
            this.retained = retained;
            return this;
        }
        
        public PublishBuilder qos(final MqttQoS qos) {
            this.qos = qos;
            return this;
        }
        
        public PublishBuilder payload(final ByteBuf payload) {
            this.payload = payload;
            return this;
        }
        
        public PublishBuilder messageId(final int messageId) {
            this.messageId = messageId;
            return this;
        }
        
        public MqttPublishMessage build() {
            final MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, this.qos, this.retained, 0);
            final MqttPublishVariableHeader mqttVariableHeader = new MqttPublishVariableHeader(this.topic, this.messageId);
            return new MqttPublishMessage(mqttFixedHeader, mqttVariableHeader, Unpooled.buffer().writeBytes(this.payload));
        }
    }
    
    public static final class ConnectBuilder
    {
        private MqttVersion version;
        private String clientId;
        private boolean cleanSession;
        private boolean hasUser;
        private boolean hasPassword;
        private int keepAliveSecs;
        private boolean willFlag;
        private boolean willRetain;
        private MqttQoS willQos;
        private String willTopic;
        private String willMessage;
        private String username;
        private String password;
        
        ConnectBuilder() {
            this.version = MqttVersion.MQTT_3_1_1;
            this.willQos = MqttQoS.AT_MOST_ONCE;
        }
        
        public ConnectBuilder protocolVersion(final MqttVersion version) {
            this.version = version;
            return this;
        }
        
        public ConnectBuilder clientId(final String clientId) {
            this.clientId = clientId;
            return this;
        }
        
        public ConnectBuilder cleanSession(final boolean cleanSession) {
            this.cleanSession = cleanSession;
            return this;
        }
        
        public ConnectBuilder keepAlive(final int keepAliveSecs) {
            this.keepAliveSecs = keepAliveSecs;
            return this;
        }
        
        public ConnectBuilder willFlag(final boolean willFlag) {
            this.willFlag = willFlag;
            return this;
        }
        
        public ConnectBuilder willQoS(final MqttQoS willQos) {
            this.willQos = willQos;
            return this;
        }
        
        public ConnectBuilder willTopic(final String willTopic) {
            this.willTopic = willTopic;
            return this;
        }
        
        public ConnectBuilder willMessage(final String willMessage) {
            this.willMessage = willMessage;
            return this;
        }
        
        public ConnectBuilder willRetain(final boolean willRetain) {
            this.willRetain = willRetain;
            return this;
        }
        
        public ConnectBuilder hasUser(final boolean value) {
            this.hasUser = value;
            return this;
        }
        
        public ConnectBuilder hasPassword(final boolean value) {
            this.hasPassword = value;
            return this;
        }
        
        public ConnectBuilder username(final String username) {
            this.hasUser = true;
            this.username = username;
            return this;
        }
        
        public ConnectBuilder password(final String password) {
            this.hasPassword = true;
            this.password = password;
            return this;
        }
        
        public MqttConnectMessage build() {
            final MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
            final MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(this.version.protocolName(), this.version.protocolLevel(), this.hasUser, this.hasPassword, this.willRetain, this.willQos.value(), this.willFlag, this.cleanSession, this.keepAliveSecs);
            final MqttConnectPayload mqttConnectPayload = new MqttConnectPayload(this.clientId, this.willTopic, this.willMessage, this.username, this.password);
            return new MqttConnectMessage(mqttFixedHeader, mqttConnectVariableHeader, mqttConnectPayload);
        }
    }
    
    public static final class SubscribeBuilder
    {
        private List<MqttTopicSubscription> subscriptions;
        private int messageId;
        
        SubscribeBuilder() {
        }
        
        public SubscribeBuilder addSubscription(final MqttQoS qos, final String topic) {
            if (this.subscriptions == null) {
                this.subscriptions = new ArrayList<MqttTopicSubscription>(5);
            }
            this.subscriptions.add(new MqttTopicSubscription(topic, qos));
            return this;
        }
        
        public SubscribeBuilder messageId(final int messageId) {
            this.messageId = messageId;
            return this;
        }
        
        public MqttSubscribeMessage build() {
            final MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0);
            final MqttMessageIdVariableHeader mqttVariableHeader = MqttMessageIdVariableHeader.from(this.messageId);
            final MqttSubscribePayload mqttSubscribePayload = new MqttSubscribePayload(this.subscriptions);
            return new MqttSubscribeMessage(mqttFixedHeader, mqttVariableHeader, mqttSubscribePayload);
        }
    }
    
    public static final class UnsubscribeBuilder
    {
        private List<String> topicFilters;
        private int messageId;
        
        UnsubscribeBuilder() {
        }
        
        public UnsubscribeBuilder addTopicFilter(final String topic) {
            if (this.topicFilters == null) {
                this.topicFilters = new ArrayList<String>(5);
            }
            this.topicFilters.add(topic);
            return this;
        }
        
        public UnsubscribeBuilder messageId(final int messageId) {
            this.messageId = messageId;
            return this;
        }
        
        public MqttUnsubscribeMessage build() {
            final MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0);
            final MqttMessageIdVariableHeader mqttVariableHeader = MqttMessageIdVariableHeader.from(this.messageId);
            final MqttUnsubscribePayload mqttSubscribePayload = new MqttUnsubscribePayload(this.topicFilters);
            return new MqttUnsubscribeMessage(mqttFixedHeader, mqttVariableHeader, mqttSubscribePayload);
        }
    }
    
    public static final class ConnAckBuilder
    {
        private MqttConnectReturnCode returnCode;
        private boolean sessionPresent;
        
        ConnAckBuilder() {
        }
        
        public ConnAckBuilder returnCode(final MqttConnectReturnCode returnCode) {
            this.returnCode = returnCode;
            return this;
        }
        
        public ConnAckBuilder sessionPresent(final boolean sessionPresent) {
            this.sessionPresent = sessionPresent;
            return this;
        }
        
        public MqttConnAckMessage build() {
            final MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
            final MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(this.returnCode, this.sessionPresent);
            return new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
        }
    }
}
