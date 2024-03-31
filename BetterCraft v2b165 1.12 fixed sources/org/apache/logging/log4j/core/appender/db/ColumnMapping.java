// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db;

import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Date;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ColumnMapping", category = "Core", printObject = true)
public class ColumnMapping
{
    private static final Logger LOGGER;
    private final String name;
    private final StringLayout layout;
    private final String literalValue;
    private final Class<?> type;
    
    private ColumnMapping(final String name, final StringLayout layout, final String literalValue, final Class<?> type) {
        this.name = name;
        this.layout = layout;
        this.literalValue = literalValue;
        this.type = type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public StringLayout getLayout() {
        return this.layout;
    }
    
    public String getLiteralValue() {
        return this.literalValue;
    }
    
    public Class<?> getType() {
        return this.type;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<ColumnMapping>
    {
        @PluginBuilderAttribute
        @Required(message = "No column name provided")
        private String name;
        @PluginElement("Layout")
        private StringLayout layout;
        @PluginBuilderAttribute
        private String pattern;
        @PluginBuilderAttribute
        private String literal;
        @PluginBuilderAttribute
        @Required(message = "No conversion type provided")
        private Class<?> type;
        @PluginConfiguration
        private Configuration configuration;
        
        public Builder() {
            this.type = String.class;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setLayout(final StringLayout layout) {
            this.layout = layout;
            return this;
        }
        
        public Builder setPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }
        
        public Builder setLiteral(final String literal) {
            this.literal = literal;
            return this;
        }
        
        public Builder setType(final Class<?> type) {
            this.type = type;
            return this;
        }
        
        public Builder setConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        @Override
        public ColumnMapping build() {
            if (this.pattern != null) {
                this.layout = PatternLayout.newBuilder().withPattern(this.pattern).withConfiguration(this.configuration).build();
            }
            if (this.layout == null && this.literal == null && !Date.class.isAssignableFrom(this.type) && !ReadOnlyStringMap.class.isAssignableFrom(this.type) && !ThreadContextMap.class.isAssignableFrom(this.type) && !ThreadContextStack.class.isAssignableFrom(this.type)) {
                ColumnMapping.LOGGER.error("No layout or literal value specified and type ({}) is not compatible with ThreadContextMap, ThreadContextStack, or java.util.Date", this.type);
                return null;
            }
            return new ColumnMapping(this.name, this.layout, this.literal, this.type, null);
        }
    }
}
