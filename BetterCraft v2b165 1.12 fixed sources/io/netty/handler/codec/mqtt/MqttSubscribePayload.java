// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;
import java.util.Collections;
import java.util.List;

public final class MqttSubscribePayload
{
    private final List<MqttTopicSubscription> topicSubscriptions;
    
    public MqttSubscribePayload(final List<MqttTopicSubscription> topicSubscriptions) {
        this.topicSubscriptions = Collections.unmodifiableList((List<? extends MqttTopicSubscription>)topicSubscriptions);
    }
    
    public List<MqttTopicSubscription> topicSubscriptions() {
        return this.topicSubscriptions;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(StringUtil.simpleClassName(this)).append('[');
        for (int i = 0; i < this.topicSubscriptions.size() - 1; ++i) {
            builder.append(this.topicSubscriptions.get(i)).append(", ");
        }
        builder.append(this.topicSubscriptions.get(this.topicSubscriptions.size() - 1));
        builder.append(']');
        return builder.toString();
    }
}
