// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import java.util.HashMap;
import org.apache.logging.log4j.core.Appender;
import java.util.Collections;
import java.util.Arrays;
import org.apache.logging.log4j.core.Filter;
import java.util.ArrayList;
import java.util.Map;
import org.apache.logging.log4j.Level;
import java.util.List;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

@Plugin(name = "logger", category = "Core", printObject = true)
public class LoggerConfig extends AbstractFilterable
{
    public static final String ROOT = "root";
    private static LogEventFactory LOG_EVENT_FACTORY;
    private List<AppenderRef> appenderRefs;
    private final AppenderControlArraySet appenders;
    private final String name;
    private LogEventFactory logEventFactory;
    private Level level;
    private boolean additive;
    private boolean includeLocation;
    private LoggerConfig parent;
    private Map<Property, Boolean> propertiesMap;
    private final List<Property> properties;
    private final boolean propertiesRequireLookup;
    private final Configuration config;
    private final ReliabilityStrategy reliabilityStrategy;
    
    public LoggerConfig() {
        this.appenderRefs = new ArrayList<AppenderRef>();
        this.appenders = new AppenderControlArraySet();
        this.additive = true;
        this.includeLocation = true;
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.level = Level.ERROR;
        this.name = "";
        this.properties = null;
        this.propertiesRequireLookup = false;
        this.config = null;
        this.reliabilityStrategy = new DefaultReliabilityStrategy(this);
    }
    
    public LoggerConfig(final String name, final Level level, final boolean additive) {
        this.appenderRefs = new ArrayList<AppenderRef>();
        this.appenders = new AppenderControlArraySet();
        this.additive = true;
        this.includeLocation = true;
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.name = name;
        this.level = level;
        this.additive = additive;
        this.properties = null;
        this.propertiesRequireLookup = false;
        this.config = null;
        this.reliabilityStrategy = new DefaultReliabilityStrategy(this);
    }
    
    protected LoggerConfig(final String name, final List<AppenderRef> appenders, final Filter filter, final Level level, final boolean additive, final Property[] properties, final Configuration config, final boolean includeLocation) {
        super(filter);
        this.appenderRefs = new ArrayList<AppenderRef>();
        this.appenders = new AppenderControlArraySet();
        this.additive = true;
        this.includeLocation = true;
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.name = name;
        this.appenderRefs = appenders;
        this.level = level;
        this.additive = additive;
        this.includeLocation = includeLocation;
        this.config = config;
        if (properties != null && properties.length > 0) {
            this.properties = Collections.unmodifiableList((List<? extends Property>)Arrays.asList((T[])Arrays.copyOf((T[])properties, properties.length)));
        }
        else {
            this.properties = null;
        }
        this.propertiesRequireLookup = containsPropertyRequiringLookup(properties);
        this.reliabilityStrategy = config.getReliabilityStrategy(this);
    }
    
    private static boolean containsPropertyRequiringLookup(final Property[] properties) {
        if (properties == null) {
            return false;
        }
        for (int i = 0; i < properties.length; ++i) {
            if (properties[i].isValueNeedsLookup()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setParent(final LoggerConfig parent) {
        this.parent = parent;
    }
    
    public LoggerConfig getParent() {
        return this.parent;
    }
    
    public void addAppender(final Appender appender, final Level level, final Filter filter) {
        this.appenders.add(new AppenderControl(appender, level, filter));
    }
    
    public void removeAppender(final String name) {
        AppenderControl removed = null;
        while ((removed = this.appenders.remove(name)) != null) {
            this.cleanupFilter(removed);
        }
    }
    
    public Map<String, Appender> getAppenders() {
        return this.appenders.asMap();
    }
    
    protected void clearAppenders() {
        do {
            final AppenderControl[] arr$;
            final AppenderControl[] original = arr$ = this.appenders.clear();
            for (final AppenderControl ctl : arr$) {
                this.cleanupFilter(ctl);
            }
        } while (!this.appenders.isEmpty());
    }
    
    private void cleanupFilter(final AppenderControl ctl) {
        final Filter filter = ctl.getFilter();
        if (filter != null) {
            ctl.removeFilter(filter);
            filter.stop();
        }
    }
    
    public List<AppenderRef> getAppenderRefs() {
        return this.appenderRefs;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    public Level getLevel() {
        return (this.level == null) ? this.parent.getLevel() : this.level;
    }
    
    public LogEventFactory getLogEventFactory() {
        return this.logEventFactory;
    }
    
    public void setLogEventFactory(final LogEventFactory logEventFactory) {
        this.logEventFactory = logEventFactory;
    }
    
    public boolean isAdditive() {
        return this.additive;
    }
    
    public void setAdditive(final boolean additive) {
        this.additive = additive;
    }
    
    public boolean isIncludeLocation() {
        return this.includeLocation;
    }
    
    @Deprecated
    public Map<Property, Boolean> getProperties() {
        if (this.properties == null) {
            return null;
        }
        if (this.propertiesMap == null) {
            final Map<Property, Boolean> result = new HashMap<Property, Boolean>(this.properties.size() * 2);
            for (int i = 0; i < this.properties.size(); ++i) {
                result.put(this.properties.get(i), this.properties.get(i).isValueNeedsLookup());
            }
            this.propertiesMap = Collections.unmodifiableMap((Map<? extends Property, ? extends Boolean>)result);
        }
        return this.propertiesMap;
    }
    
    public List<Property> getPropertyList() {
        return this.properties;
    }
    
    public boolean isPropertiesRequireLookup() {
        return this.propertiesRequireLookup;
    }
    
    @PerformanceSensitive({ "allocation" })
    public void log(final String loggerName, final String fqcn, final Marker marker, final Level level, final Message data, final Throwable t) {
        List<Property> props = null;
        if (!this.propertiesRequireLookup) {
            props = this.properties;
        }
        else if (this.properties != null) {
            props = new ArrayList<Property>(this.properties.size());
            final LogEvent event = Log4jLogEvent.newBuilder().setMessage(data).setMarker(marker).setLevel(level).setLoggerName(loggerName).setLoggerFqcn(fqcn).setThrown(t).build();
            for (int i = 0; i < this.properties.size(); ++i) {
                final Property prop = this.properties.get(i);
                final String value = prop.isValueNeedsLookup() ? this.config.getStrSubstitutor().replace(event, prop.getValue()) : prop.getValue();
                props.add(Property.createProperty(prop.getName(), value));
            }
        }
        final LogEvent logEvent = this.logEventFactory.createEvent(loggerName, marker, fqcn, level, data, props, t);
        try {
            this.log(logEvent);
        }
        finally {
            ReusableLogEventFactory.release(logEvent);
        }
    }
    
    public void log(final LogEvent event) {
        if (!this.isFiltered(event)) {
            this.processLogEvent(event);
        }
    }
    
    public ReliabilityStrategy getReliabilityStrategy() {
        return this.reliabilityStrategy;
    }
    
    private void processLogEvent(final LogEvent event) {
        event.setIncludeLocation(this.isIncludeLocation());
        this.callAppenders(event);
        this.logParent(event);
    }
    
    private void logParent(final LogEvent event) {
        if (this.additive && this.parent != null) {
            this.parent.log(event);
        }
    }
    
    @PerformanceSensitive({ "allocation" })
    protected void callAppenders(final LogEvent event) {
        final AppenderControl[] controls = this.appenders.get();
        for (int i = 0; i < controls.length; ++i) {
            controls[i].callAppender(event);
        }
    }
    
    @Override
    public String toString() {
        return Strings.isEmpty(this.name) ? "root" : this.name;
    }
    
    @Deprecated
    public static LoggerConfig createLogger(final String additivity, final Level level, @PluginAttribute("name") final String loggerName, final String includeLocation, final AppenderRef[] refs, final Property[] properties, @PluginConfiguration final Configuration config, final Filter filter) {
        if (loggerName == null) {
            LoggerConfig.LOGGER.error("Loggers cannot be configured without a name");
            return null;
        }
        final List<AppenderRef> appenderRefs = Arrays.asList(refs);
        final String name = loggerName.equals("root") ? "" : loggerName;
        final boolean additive = Booleans.parseBoolean(additivity, true);
        return new LoggerConfig(name, appenderRefs, filter, level, additive, properties, config, includeLocation(includeLocation));
    }
    
    @PluginFactory
    public static LoggerConfig createLogger(@PluginAttribute(value = "additivity", defaultBoolean = true) final boolean additivity, @PluginAttribute("level") final Level level, @Required(message = "Loggers cannot be configured without a name") @PluginAttribute("name") final String loggerName, @PluginAttribute("includeLocation") final String includeLocation, @PluginElement("AppenderRef") final AppenderRef[] refs, @PluginElement("Properties") final Property[] properties, @PluginConfiguration final Configuration config, @PluginElement("Filter") final Filter filter) {
        final String name = loggerName.equals("root") ? "" : loggerName;
        return new LoggerConfig(name, Arrays.asList(refs), filter, level, additivity, properties, config, includeLocation(includeLocation));
    }
    
    protected static boolean includeLocation(final String includeLocationConfigValue) {
        if (includeLocationConfigValue == null) {
            final boolean sync = !AsyncLoggerContextSelector.isSelected();
            return sync;
        }
        return Boolean.parseBoolean(includeLocationConfigValue);
    }
    
    static {
        LoggerConfig.LOG_EVENT_FACTORY = null;
        final String factory = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");
        if (factory != null) {
            try {
                final Class<?> clazz = LoaderUtil.loadClass(factory);
                if (clazz != null && LogEventFactory.class.isAssignableFrom(clazz)) {
                    LoggerConfig.LOG_EVENT_FACTORY = (LogEventFactory)clazz.newInstance();
                }
            }
            catch (final Exception ex) {
                LoggerConfig.LOGGER.error("Unable to create LogEventFactory {}", factory, ex);
            }
        }
        if (LoggerConfig.LOG_EVENT_FACTORY == null) {
            LoggerConfig.LOG_EVENT_FACTORY = (Constants.ENABLE_THREADLOCALS ? new ReusableLogEventFactory() : new DefaultLogEventFactory());
        }
    }
    
    @Plugin(name = "root", category = "Core", printObject = true)
    public static class RootLogger extends LoggerConfig
    {
        @PluginFactory
        public static LoggerConfig createLogger(@PluginAttribute("additivity") final String additivity, @PluginAttribute("level") final Level level, @PluginAttribute("includeLocation") final String includeLocation, @PluginElement("AppenderRef") final AppenderRef[] refs, @PluginElement("Properties") final Property[] properties, @PluginConfiguration final Configuration config, @PluginElement("Filter") final Filter filter) {
            final List<AppenderRef> appenderRefs = Arrays.asList(refs);
            final Level actualLevel = (level == null) ? Level.ERROR : level;
            final boolean additive = Booleans.parseBoolean(additivity, true);
            return new LoggerConfig("", appenderRefs, filter, actualLevel, additive, properties, config, LoggerConfig.includeLocation(includeLocation));
        }
    }
}
