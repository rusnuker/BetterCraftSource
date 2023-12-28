// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;
import java.util.Collections;
import java.util.List;

public final class MqttUnsubscribePayload
{
    private final List<String> topics;
    
    public MqttUnsubscribePayload(final List<String> topics) {
        this.topics = Collections.unmodifiableList((List<? extends String>)topics);
    }
    
    public List<String> topics() {
        return this.topics;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(StringUtil.simpleClassName(this)).append('[');
        for (int i = 0; i < this.topics.size() - 1; ++i) {
            builder.append("topicName = ").append(this.topics.get(i)).append(", ");
        }
        builder.append("topicName = ").append(this.topics.get(this.topics.size() - 1)).append(']');
        return builder.toString();
    }
}
