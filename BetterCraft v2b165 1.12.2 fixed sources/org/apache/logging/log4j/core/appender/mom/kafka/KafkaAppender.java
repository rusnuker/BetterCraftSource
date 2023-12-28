// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.mom.kafka;

import org.apache.logging.log4j.core.util.Builder;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.util.StringEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "Kafka", category = "Core", elementType = "appender", printObject = true)
public final class KafkaAppender extends AbstractAppender
{
    private final KafkaManager manager;
    
    @Deprecated
    public static KafkaAppender createAppender(@PluginElement("Layout") final Layout<? extends Serializable> layout, @PluginElement("Filter") final Filter filter, @Required(message = "No name provided for KafkaAppender") @PluginAttribute("name") final String name, @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) final boolean ignoreExceptions, @Required(message = "No topic provided for KafkaAppender") @PluginAttribute("topic") final String topic, @PluginElement("Properties") final Property[] properties, @PluginConfiguration final Configuration configuration) {
        final KafkaManager kafkaManager = new KafkaManager(configuration.getLoggerContext(), name, topic, true, properties);
        return new KafkaAppender(name, layout, filter, ignoreExceptions, kafkaManager);
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private KafkaAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final KafkaManager manager) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = Objects.requireNonNull(manager, "manager");
    }
    
    @Override
    public void append(final LogEvent event) {
        if (event.getLoggerName().startsWith("org.apache.kafka")) {
            KafkaAppender.LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), this.getName());
        }
        else {
            try {
                final Layout<? extends Serializable> layout = this.getLayout();
                byte[] data;
                if (layout != null) {
                    if (layout instanceof SerializedLayout) {
                        final byte[] header = layout.getHeader();
                        final byte[] body = layout.toByteArray(event);
                        data = new byte[header.length + body.length];
                        System.arraycopy(header, 0, data, 0, header.length);
                        System.arraycopy(body, 0, data, header.length, body.length);
                    }
                    else {
                        data = layout.toByteArray(event);
                    }
                }
                else {
                    data = StringEncoder.toBytes(event.getMessage().getFormattedMessage(), StandardCharsets.UTF_8);
                }
                this.manager.send(data);
            }
            catch (final Exception e) {
                KafkaAppender.LOGGER.error("Unable to write to Kafka [{}] for appender [{}].", this.manager.getName(), this.getName(), e);
                throw new AppenderLoggingException("Unable to write to Kafka in appender: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public void start() {
        super.start();
        this.manager.startup();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        stopped &= this.manager.stop(timeout, timeUnit);
        this.setStopped();
        return stopped;
    }
    
    @Override
    public String toString() {
        return "KafkaAppender{name=" + this.getName() + ", state=" + this.getState() + ", topic=" + this.manager.getTopic() + '}';
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<KafkaAppender>
    {
        @PluginAttribute("topic")
        private String topic;
        @PluginAttribute(value = "syncSend", defaultBoolean = true)
        private boolean syncSend;
        @PluginElement("Properties")
        private Property[] properties;
        
        @Override
        public KafkaAppender build() {
            final KafkaManager kafkaManager = new KafkaManager(this.getConfiguration().getLoggerContext(), this.getName(), this.topic, this.syncSend, this.properties);
            return new KafkaAppender(this.getName(), this.getLayout(), this.getFilter(), this.isIgnoreExceptions(), kafkaManager, null);
        }
        
        public String getTopic() {
            return this.topic;
        }
        
        public Property[] getProperties() {
            return this.properties;
        }
        
        public B setTopic(final String topic) {
            this.topic = topic;
            return this.asBuilder();
        }
        
        public B setSyncSend(final boolean syncSend) {
            this.syncSend = syncSend;
            return this.asBuilder();
        }
        
        public B setProperties(final Property[] properties) {
            this.properties = properties;
            return this.asBuilder();
        }
    }
}
