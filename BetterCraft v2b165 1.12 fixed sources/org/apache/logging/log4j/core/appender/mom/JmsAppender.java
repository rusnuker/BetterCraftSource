// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.mom;

import java.util.Properties;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.concurrent.TimeUnit;
import javax.jms.Message;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.LogEvent;
import javax.jms.JMSException;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import javax.jms.MessageProducer;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "JMS", category = "Core", elementType = "appender", printObject = true)
@PluginAliases({ "JMSQueue", "JMSTopic" })
public class JmsAppender extends AbstractAppender
{
    private final JmsManager manager;
    private final MessageProducer producer;
    
    protected JmsAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final JmsManager manager) throws JMSException {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
        this.producer = this.manager.createMessageProducer();
    }
    
    @Override
    public void append(final LogEvent event) {
        try {
            final Message message = this.manager.createMessage((Serializable)this.getLayout().toSerializable(event));
            message.setJMSTimestamp(event.getTimeMillis());
            this.producer.send(message);
        }
        catch (final JMSException e) {
            throw new AppenderLoggingException((Throwable)e);
        }
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        stopped &= this.manager.stop(timeout, timeUnit);
        this.setStopped();
        return stopped;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<JmsAppender>
    {
        @PluginBuilderAttribute
        @Required(message = "A name for the JmsAppender must be specified")
        private String name;
        @PluginBuilderAttribute
        private String factoryName;
        @PluginBuilderAttribute
        private String providerUrl;
        @PluginBuilderAttribute
        private String urlPkgPrefixes;
        @PluginBuilderAttribute
        private String securityPrincipalName;
        @PluginBuilderAttribute(sensitive = true)
        private String securityCredentials;
        @PluginBuilderAttribute
        @Required(message = "A javax.jms.ConnectionFactory JNDI name must be specified")
        private String factoryBindingName;
        @PluginBuilderAttribute
        @PluginAliases({ "queueBindingName", "topicBindingName" })
        @Required(message = "A javax.jms.Destination JNDI name must be specified")
        private String destinationBindingName;
        @PluginBuilderAttribute
        private String username;
        @PluginBuilderAttribute(sensitive = true)
        private String password;
        @PluginElement("Layout")
        private Layout<? extends Serializable> layout;
        @PluginElement("Filter")
        private Filter filter;
        @PluginBuilderAttribute
        private boolean ignoreExceptions;
        private JmsManager jmsManager;
        
        private Builder() {
            this.layout = SerializedLayout.createLayout();
            this.ignoreExceptions = true;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setFactoryName(final String factoryName) {
            this.factoryName = factoryName;
            return this;
        }
        
        public Builder setProviderUrl(final String providerUrl) {
            this.providerUrl = providerUrl;
            return this;
        }
        
        public Builder setUrlPkgPrefixes(final String urlPkgPrefixes) {
            this.urlPkgPrefixes = urlPkgPrefixes;
            return this;
        }
        
        public Builder setSecurityPrincipalName(final String securityPrincipalName) {
            this.securityPrincipalName = securityPrincipalName;
            return this;
        }
        
        public Builder setSecurityCredentials(final String securityCredentials) {
            this.securityCredentials = securityCredentials;
            return this;
        }
        
        public Builder setFactoryBindingName(final String factoryBindingName) {
            this.factoryBindingName = factoryBindingName;
            return this;
        }
        
        public Builder setDestinationBindingName(final String destinationBindingName) {
            this.destinationBindingName = destinationBindingName;
            return this;
        }
        
        public Builder setUsername(final String username) {
            this.username = username;
            return this;
        }
        
        public Builder setPassword(final String password) {
            this.password = password;
            return this;
        }
        
        public Builder setLayout(final Layout<? extends Serializable> layout) {
            this.layout = layout;
            return this;
        }
        
        public Builder setFilter(final Filter filter) {
            this.filter = filter;
            return this;
        }
        
        public Builder setJmsManager(final JmsManager jmsManager) {
            this.jmsManager = jmsManager;
            return this;
        }
        
        public Builder setIgnoreExceptions(final boolean ignoreExceptions) {
            this.ignoreExceptions = ignoreExceptions;
            return this;
        }
        
        @Override
        public JmsAppender build() {
            JmsManager actualJmsManager = this.jmsManager;
            if (actualJmsManager == null) {
                final JndiManager jndiManager = JndiManager.getJndiManager(this.factoryName, this.providerUrl, this.urlPkgPrefixes, this.securityPrincipalName, this.securityCredentials, null);
                actualJmsManager = JmsManager.getJmsManager(this.name, jndiManager, this.factoryBindingName, this.destinationBindingName, this.username, this.password);
            }
            try {
                return new JmsAppender(this.name, this.filter, this.layout, this.ignoreExceptions, actualJmsManager);
            }
            catch (final JMSException e) {
                JmsAppender.LOGGER.error("Error creating JmsAppender [{}].", this.name, e);
                return null;
            }
        }
    }
}
