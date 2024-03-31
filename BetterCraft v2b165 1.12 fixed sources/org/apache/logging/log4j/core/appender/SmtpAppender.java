// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.HtmlLayout;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.net.SmtpManager;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "SMTP", category = "Core", elementType = "appender", printObject = true)
public final class SmtpAppender extends AbstractAppender
{
    private static final int DEFAULT_BUFFER_SIZE = 512;
    private final SmtpManager manager;
    
    private SmtpAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final SmtpManager manager, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
    }
    
    @PluginFactory
    public static SmtpAppender createAppender(@PluginConfiguration final Configuration config, @PluginAttribute("name") @Required final String name, @PluginAttribute("to") final String to, @PluginAttribute("cc") final String cc, @PluginAttribute("bcc") final String bcc, @PluginAttribute("from") final String from, @PluginAttribute("replyTo") final String replyTo, @PluginAttribute("subject") final String subject, @PluginAttribute("smtpProtocol") final String smtpProtocol, @PluginAttribute("smtpHost") final String smtpHost, @PluginAttribute(value = "smtpPort", defaultString = "0") @ValidPort final String smtpPortStr, @PluginAttribute("smtpUsername") final String smtpUsername, @PluginAttribute(value = "smtpPassword", sensitive = true) final String smtpPassword, @PluginAttribute("smtpDebug") final String smtpDebug, @PluginAttribute("bufferSize") final String bufferSizeStr, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") final String ignore) {
        if (name == null) {
            SmtpAppender.LOGGER.error("No name provided for SmtpAppender");
            return null;
        }
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final int smtpPort = AbstractAppender.parseInt(smtpPortStr, 0);
        final boolean isSmtpDebug = Boolean.parseBoolean(smtpDebug);
        final int bufferSize = (bufferSizeStr == null) ? 512 : Integer.parseInt(bufferSizeStr);
        if (layout == null) {
            layout = HtmlLayout.createDefaultLayout();
        }
        if (filter == null) {
            filter = ThresholdFilter.createFilter(null, null, null);
        }
        final Configuration configuration = (config != null) ? config : new DefaultConfiguration();
        final SmtpManager manager = SmtpManager.getSmtpManager(configuration, to, cc, bcc, from, replyTo, subject, smtpProtocol, smtpHost, smtpPort, smtpUsername, smtpPassword, isSmtpDebug, filter.toString(), bufferSize);
        if (manager == null) {
            return null;
        }
        return new SmtpAppender(name, filter, layout, manager, ignoreExceptions);
    }
    
    @Override
    public boolean isFiltered(final LogEvent event) {
        final boolean filtered = super.isFiltered(event);
        if (filtered) {
            this.manager.add(event);
        }
        return filtered;
    }
    
    @Override
    public void append(final LogEvent event) {
        this.manager.sendEvents(this.getLayout(), event);
    }
}
